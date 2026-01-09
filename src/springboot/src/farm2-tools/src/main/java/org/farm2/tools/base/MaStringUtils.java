package org.farm2.tools.base;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaStringUtils {

    private volatile String cachedMachineId = null;
    private static final Pattern GUID_PATTERN = Pattern.compile("[A-Fa-f0-9]{8}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{12}");

    /**
     * 获取全局唯一且稳定的机器码（重装系统前不变）
     */
    public String getStableMachineId() {
        if (cachedMachineId == null) {
            synchronized (this) {
                if (cachedMachineId == null) {
                    cachedMachineId = computeStableMachineId();
                }
            }
        }
        return cachedMachineId;
    }

    private String computeStableMachineId() {
        String os = System.getProperty("os.name", "").toLowerCase();
        try {
            if (os.contains("win")) {
                String guid = getWindowsMachineGuid();
                if (StringUtils.isNotBlank(guid)) {
                    return guid;
                }
                return getWindowsOsFingerprint();
            } else if (os.contains("linux") || os.contains("unix") || os.contains("sunos") || os.contains("solaris")) {
                String machineId = getLinuxMachineId();
                if (StringUtils.isNotBlank(machineId) && !machineId.equals("uninitialized")) {
                    return machineId;
                }
                return getLinuxOsFingerprint();
            } else {
                // macOS, BSD, AIX, 或未知系统
                return getGenericOsFingerprint();
            }
        } catch (Exception e) {
            return "MACHINE-ID-NOT-AVAILABLE";
        }
    }

    private String getGenericOsFingerprint() {
        String name = System.getProperty("os.name", "UnknownOS");
        String version = System.getProperty("os.version", "0");
        String arch = System.getProperty("os.arch", "x86_64");

        String cleanName = name.replaceAll("[^a-zA-Z0-9]", "");
        String cleanVersion = version.replaceAll("[^a-zA-Z0-9.]", "");

        return "OS-" + cleanName + "-" + cleanVersion + "-" + arch;
    }

    // =============== Windows ===============

    private String getWindowsMachineGuid() {
        String output = executeCommandWithTimeout(
                new String[]{"reg", "query", "HKLM\\SOFTWARE\\Microsoft\\Cryptography", "/v", "MachineGuid"},
                3
        );
        if (output == null) return null;

        Matcher matcher = GUID_PATTERN.matcher(output);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return null;
    }

    private String getWindowsOsFingerprint() {
        String output = executeCommandWithTimeout(
                new String[]{"cmd.exe", "/c", "ver"},
                2
        );
        if (output == null || output.isEmpty()) {
            return "WIN-OS-UNKNOWN";
        }

        String cleaned = output.replaceAll("[^0-9.]", " ").trim();
        String[] parts = cleaned.split("\\s+");
        if (parts.length > 0 && parts[0].contains(".")) {
            return "WIN-" + parts[0];
        }
        return "WIN-OS-UNKNOWN";
    }

    // =============== Linux ===============

    private String getLinuxMachineId() {
        try {
            if (Files.exists(Paths.get("/etc/machine-id"))) {
                String id = Files.readString(Paths.get("/etc/machine-id")).trim();
                if (StringUtils.isNotBlank(id) && !id.equals("uninitialized")) {
                    return id;
                }
            }
            if (Files.exists(Paths.get("/var/lib/dbus/machine-id"))) {
                String id = Files.readString(Paths.get("/var/lib/dbus/machine-id")).trim();
                if (StringUtils.isNotBlank(id)) {
                    return id;
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private String getLinuxOsFingerprint() {
        // 尝试 /etc/os-release（主流 Linux 标准）
        try {
            java.nio.file.Path path = Paths.get("/etc/os-release");
            if (Files.exists(path)) {
                String content = Files.readString(path);
                String name = "", version = "";
                for (String line : content.split("\n")) {
                    if (line.startsWith("NAME=")) {
                        name = line.split("=", 2)[1].replaceAll("\"", "").trim();
                    } else if (line.startsWith("VERSION_ID=")) {
                        version = line.split("=", 2)[1].replaceAll("\"", "").trim();
                    }
                }
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(version)) {
                    String cleanName = name.replaceAll("[^a-zA-Z0-9]", "");
                    return "LINUX-" + cleanName + "-" + version;
                }
            }
        } catch (Exception ignored) {
        }

        // fallback: uname -r（内核版本）
        String kernelOutput = executeCommandWithTimeout(new String[]{"uname", "-r"}, 2);
        if (kernelOutput != null && StringUtils.isNotBlank(kernelOutput)) {
            String cleanKernel = kernelOutput.replaceAll("[^a-zA-Z0-9.-]", "");
            return "LINUX-KERNEL-" + cleanKernel;
        }

        return "LINUX-OS-UNKNOWN";
    }

    // =============== 命令执行工具 ===============

    private String executeCommandWithTimeout(String[] command, long timeoutSeconds) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);

            // 启动线程消费 stderr，防止子进程因缓冲区满而阻塞（不存储内容）
            Process finalProcess = process;
            Thread errReader = new Thread(() -> {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(finalProcess.getErrorStream()))) {
                    while (br.readLine() != null); // 仅消费，不保存
                } catch (IOException ignored) {
                }
            });
            errReader.start();

            // 等待进程结束（带超时）
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                errReader.interrupt();
                return null;
            }

            if (process.exitValue() != 0) {
                return null;
            }

            // 读取标准输出
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return output.toString().trim();
            }

        } catch (Exception ignored) {
            if (process != null) {
                process.destroyForcibly();
            }
            return null;
        }
    }
}
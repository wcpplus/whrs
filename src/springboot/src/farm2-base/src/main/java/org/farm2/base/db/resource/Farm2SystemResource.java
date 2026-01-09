package org.farm2.base.db.resource;

import org.farm2.tools.structure.ResourceInfo;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 获取系统资源工具类
 */
public class Farm2SystemResource {


    /**
     * 获得资源用量信息ResourceInfo{ percent：已用百分比 all总量  use已用量 }
     *
     * @param basepath 附件存储目录
     * @return
     */
    public static ResourceInfo getDiskPercentage(Path basepath) {
        try {
            FileStore store = null;
            if (!basepath.toFile().exists()) {
                basepath.toFile().mkdirs();
            }
            store = Files.getFileStore(basepath);
            long total = store.getTotalSpace();
            long used = total - store.getUnallocatedSpace();
            double percentUsed = total > 0 ? (double) used / total * 100 : 0;
            return new ResourceInfo(store.toString(), total, used, (int) percentUsed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得内存用量信息ResourceInfo{ percent：已用百分比 all总量  use已用量 }
     *
     * @return
     */
    public static ResourceInfo getRamPercentage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        com.sun.management.OperatingSystemMXBean sunBean = (com.sun.management.OperatingSystemMXBean) operatingSystemMXBean;
        long totalPhysicalMemorySize = sunBean.getTotalPhysicalMemorySize(); // 总物理内存
        long freePhysicalMemorySize = sunBean.getFreePhysicalMemorySize(); // 空闲物理内存
        long usedMemory = totalPhysicalMemorySize - freePhysicalMemorySize;
        double percentUsed = totalPhysicalMemorySize > 0 ? (double) usedMemory / totalPhysicalMemorySize * 100 : 0;
        return new ResourceInfo("ram", totalPhysicalMemorySize, usedMemory, (int) percentUsed);
    }

    /**
     * 获得cpu用量信息ResourceInfo{ percent：已用百分比 all总资源量  use已用资源量 }
     *
     * @return
     */
    public static ResourceInfo getCpuPercentage() {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 获取总的CPU核心数
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        // 获取系统CPU负载，这个值是所有处理器核心的平均负载
        double systemCpuLoad = operatingSystemMXBean.getSystemLoadAverage();

        return new ResourceInfo("cpu负载", (int) systemCpuLoad);
    }
}

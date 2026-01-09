package org.farm2.wdap.convertor.utils;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.face.FarmParameter;
import org.farm2.base.web.Farm2BeanFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.concurrent.*;

@Slf4j
public class OpenOffceUtils {

    /**
     * 获得openoffcie服务链接失败后会重启openoffice，再链接一次（使用完成后必须关闭链接）
     *
     * @return
     * @throws ConnectException
     */
    public static OpenOfficeConnection openConnection() throws ConnectException {
        if (!isStart()) {
            reStart();
        }
        FarmParameter parameter = (FarmParameter) Farm2BeanFactory.getBean(FarmParameter.class);
        // 创建Openoffice连接
        OpenOfficeConnection con = new SocketOpenOfficeConnection(
                parameter.getStringParameter("farm2.config.openoffice.host"),
                parameter.getIntParameter("farm2.config..openoffice.port"));
        for (int i = 0; i < 10; i++) {
            try {
                con.connect();
                log.warn("openoffice连接成功!");
                return con;
            } catch (ConnectException e) {
                if (i >= 5) {
                    throw e;
                } else {
                    log.warn("openoffice连接失败，正在重新连接...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return con;
    }

    /**
     * openoffce服务是否启动（超时和链接失败都算失败,只检查一次）
     *
     * @return
     */
    public static boolean isStart() {
        // 超過3秒連不上就認爲是服務死掉了
        long outTime = 3000;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // FutureTask 可以设置超时时间的任务，超时则算链接失败
        FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean isStart = false;
                FarmParameter parameter = (FarmParameter) Farm2BeanFactory.getBean(FarmParameter.class);
                OpenOfficeConnection con = new SocketOpenOfficeConnection(
                        parameter.getStringParameter("farm2.config.openoffice.host"),
                        parameter.getIntParameter("farm2.config..openoffice.port"));
                try {
                    con.connect();
                    if (con.isConnected()) {
                        isStart = true;
                    } else {
                        isStart = false;
                    }
                } catch (ConnectException e1) {
                    isStart = false;
                } finally {
                    if (isStart) {
                        con.disconnect();
                    }
                }
                return isStart;
            }
        });
        executor.execute(future);
        boolean isStart = false;
        try {
            // 在一定时间内执行该任务
            isStart = future.get(outTime, TimeUnit.MILLISECONDS); // 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
        } catch (Exception e) {
            future.cancel(true);
        } finally {
            executor.shutdown();
        }
        return isStart;
    }

    /**
     * 启动office转换服务(相当于重启，先杀掉进程再启动进程)
     */
    public static void reStart() {
        shutdown();
        try {
            Runtime runtime = Runtime.getRuntime();
            log.info("run[start]---openoffice service...");
            FarmParameter parameter = (FarmParameter) Farm2BeanFactory.getBean(FarmParameter.class);
            if (!OsUtils.isWindows()) {
                String START_LINUX_CMD = parameter.getStringParameter("farm2.config.openoffice.start.linux.cmd");
                log.info("is linux run cmd:" + START_LINUX_CMD);
                String[] linuxcmd = new String[]{"sh", "-c", START_LINUX_CMD};
                runtime.exec(linuxcmd);
            } else {
                String START_WIN_CMD = parameter.getStringParameter("farm2.config.openoffice.start.windows.cmd");
                log.info("is windows run cmd:" + START_WIN_CMD);
                runtime.exec(START_WIN_CMD);
            }
        } catch (IOException exception) {
            log.error("Error:run[start]---openoffice service:" + exception.getMessage(), exception);
        }
    }

    public static void shutdown() {
        try {
            Runtime runtime = Runtime.getRuntime();
            log.info("run[kill]---openoffice service...");
            FarmParameter parameter = (FarmParameter) Farm2BeanFactory.getBean(FarmParameter.class);
            Process proc1 = null;
            if (!OsUtils.isWindows()) {
                log.info("is linux run cmd:" + parameter.getStringParameter("farm2.config.openoffice.kill.linux.cmd"));
                String[] linuxcmd = new String[]{"sh", "-c", parameter.getStringParameter("farm2.config.openoffice.kill.linux.cmd")};
                proc1 = runtime.exec(linuxcmd);
            } else {
                log.info("is windows run cmd:" + parameter.getStringParameter("farm2.config.openoffice.kill.windows.cmd"));
                proc1 = runtime.exec(parameter.getStringParameter("farm2.config.openoffice.kill.windows.cmd"));
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(proc1.getInputStream(), parameter.getStringParameter("config.server.os.cmd.encode")));
            String msg = null;
            while ((msg = br.readLine()) != null) {
                log.info(msg);
            }
        } catch (IOException exception) {
            log.error("Error:run[kill]---openoffice service:" + exception.getMessage(), exception);
        }
    }
}

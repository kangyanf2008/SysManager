package com.ps.utils;

import com.ps.queue.LogQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerUtils {

    static Runtime runtime = Runtime.getRuntime();

    //判断是否安装服务
    public static boolean checkServerInstall(String serviceName) {
        try {
            String cmd = "sc query type=all state=all";
            String[] channelCmd = {"findstr /i service_name.*", "findstr /i " + serviceName};
            //String r = executeCMD("sc query type= all state= all | findstr /i service_name.* | findstr /i "+serviceName);
            String r = executeCMD(cmd, channelCmd);
            LogQueue.Push(r);
            if (r.indexOf(serviceName) > 0) {
                return true;
            }
        } catch (Exception e) {
            try {
                LogQueue.Push(e.getMessage());
            } catch (Exception e2) {
            }
        }
        return false;

    }

    //判断服务和映射是否运行
    public static boolean checkServerRun(String serviceName, String imageName) {
        try {
            if (serviceName != null && "" != serviceName) {
                String serviceIsRun = executeCMD("tasklist /fi \"imagename eq " + serviceName + "\"");
                if (null != serviceIsRun || serviceIsRun != "") {
                    LogQueue.Push(serviceIsRun);
                }

                if (null == serviceIsRun || serviceIsRun == "" || serviceIsRun.indexOf(serviceName) < 0) {
                    return false;
                }
            }

            //判断映像是否运行
            if (imageName != null && "" != imageName) {

                String cmd = "docker ps";
                String[] channelCmd = {"findstr /i " + imageName};
                String dockerIsRun = executeCMD(cmd, channelCmd);

                if (null != dockerIsRun || dockerIsRun != "") {
                    LogQueue.Push(dockerIsRun);
                }

                if (null != dockerIsRun || dockerIsRun != "" || dockerIsRun.indexOf(imageName) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            try {
                LogQueue.Push(e.getMessage());
            } catch (Exception e2) {
            }
        }
        return false;
    }

    //执行命令
    private static String executeCMD(String cmd) throws Exception {
        try {
            //执行指令操作放入日志队列
            LogQueue.Push(cmd);
            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (content != null) {
                sb.append(content + "\n");
                content = br.readLine();
            }
            process.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //执行命令
    private static String executeCMD(String cmd, String[] channelCmd) throws Exception {
        try {
            //执行指令操作放入日志队列
            LogQueue.Push(cmd + " | " + String.join(" | ", channelCmd));
            Process process = runtime.exec(cmd, channelCmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (content != null) {
                sb.append(content + "\n");
                content = br.readLine();
            }
            process.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}

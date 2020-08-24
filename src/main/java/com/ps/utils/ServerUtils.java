package com.ps.utils;

import com.ps.queue.LogQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerUtils {

    static Runtime runtime = Runtime.getRuntime();

    //判断是否安装服务
    public static boolean checkServerInstall(String serviceName,  boolean isLog) {
        try {
            String cmd = "sc query type=all state=all";
            String[] channelCmd = {"findstr /i service_name.*", "findstr /i " + serviceName};
            //String r = executeCMD("sc query type= all state= all | findstr /i service_name.* | findstr /i "+serviceName);
            String r = executeCMD(cmd, channelCmd, isLog);
            if (isLog) {
                LogQueue.Push(r);
            }
            if (r.toLowerCase().indexOf("com.docker.service".toLowerCase()) > 0) {
                return true;
            }
        } catch (Exception e) {
            try {
                if (isLog) {
                    LogQueue.Push(e.getMessage());
                }
            } catch (Exception e2) {
            }
        }
        return false;

    }

    //判断服务和映射是否运行
    public static boolean checkServerRun(String serviceName, String imageName, boolean isLog) {
        try {
            if (serviceName != null && "" != serviceName) {
                String[] channelCmd = {"findstr /i " + serviceName};
                String serviceIsRun = executeCMD("tasklist ", channelCmd, isLog);
                if (null != serviceIsRun || serviceIsRun != "") {
                    if (isLog) {
                        LogQueue.Push(serviceIsRun);
                    }
                }

                if (null == serviceIsRun || serviceIsRun == "" ) {
                    return false;
                }
                //判断进程是否在运行
                String lowerCase = serviceIsRun.toLowerCase();
                if ( lowerCase.indexOf("com.docker.service".toLowerCase())< 0
                        || lowerCase.indexOf("Docker Desktop.exe".toLowerCase()) < 0
                        || lowerCase.indexOf("docker-mutagen.exe".toLowerCase()) < 0
                        || lowerCase.indexOf("com.docker.proxy.exe".toLowerCase()) < 0
                        || lowerCase.indexOf("com.docker.backend.exe".toLowerCase()) < 0){
                    return false;
                }
            }

            //判断映像是否运行
            if (imageName != null && "" != imageName) {

                String cmd = "docker ps";
                String[] channelCmd = {"findstr /i " + imageName};
                String dockerIsRun = executeCMD(cmd, channelCmd, isLog);
                if (null != dockerIsRun || dockerIsRun != "") {
                    if (isLog) {
                        LogQueue.Push(dockerIsRun);
                    }
                }

                //判断是否运行
                if (null != dockerIsRun || dockerIsRun != "" || dockerIsRun.toLowerCase().indexOf(imageName.toLowerCase()) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            try {
                if (isLog) {
                    LogQueue.Push(e.getMessage());
                }
            } catch (Exception e2) {
            }
        }
        return false;
    }

    //安装程序
    public static boolean installProgram(String program, boolean isLog){

        //start /wait D:\kyf\java-workspace\SM-resource\npp.7.8.6.Installer.exe /verysilent sp-
        try {
            //String result = executeCMD("start /wait "+program+" /verysilent sp-", isLog);
            String result = executeCMD("start /wait "+program, isLog);
            if (null != result || result != "") {
                if (isLog) {
                    LogQueue.Push(result);
                }
            }
            return true;
        } catch (Exception e) {
            try {
                if (isLog) {
                    LogQueue.Push(e.getMessage());
                }
            } catch (Exception e2) {
            }
            return false;
        }

    }

    //执行命令
    private static String executeCMD(String cmd, boolean isLog) throws Exception {
        try {
            //执行指令操作放入日志队列
            if (isLog) {
                LogQueue.Push(cmd);
            }
            Process process = runtime.exec("cmd /c "+cmd);
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
    private static String executeCMD(String cmd, String[] channelCmd, boolean isLog) throws Exception {
        try {
            //执行指令操作放入日志队列
            if (isLog) {
                LogQueue.Push("cmd /c "+ cmd + " | " + String.join(" | ", channelCmd));
            }
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

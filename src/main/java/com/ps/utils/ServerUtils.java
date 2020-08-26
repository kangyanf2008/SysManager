package com.ps.utils;

import com.ps.config.LoadConfig;
import com.ps.constants.PropertiesDef;
import com.ps.env.Env;
import com.ps.queue.LogQueue;

import java.io.*;

public class ServerUtils {

    static Runtime runtime = Runtime.getRuntime();

    //判断是否安装服务
    public static boolean checkServerInstall(String serviceName, boolean isLog) {
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
    public static boolean checkDockerRun(String serviceName, boolean isLog) {
        try {
            if (serviceName != null && "" != serviceName) {
                String[] channelCmd = {"findstr /i " + serviceName};
                String serviceIsRun = executeCMD("tasklist ", channelCmd, isLog);
                if (null != serviceIsRun || serviceIsRun != "") {
                    if (isLog) {
                        LogQueue.Push(serviceIsRun);
                    }
                }

                if (null == serviceIsRun || serviceIsRun == "") {
                    return false;
                }
                //判断进程是否在运行
                String lowerCase = serviceIsRun.toLowerCase();
                //判断进程任务是否执行
                String[] images = LoadConfig.getConfig(PropertiesDef.DockerTaskList).split("\\|");
                if (images.length > 0) {
                    for (String image : images) {
                        if (lowerCase.indexOf(image.toLowerCase()) < 0) {
                            return false;
                        }
                    }
                    return true;
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

    //判断服务和映射是否运行
    public static boolean checkImageRun(String imageName, boolean isLog) {
        try {
            //判断映像是否运行
            String cmd = "docker ps";
            String dockerIsRun = executeCMD(cmd, isLog);
            if (null != dockerIsRun || dockerIsRun != "") {
                if (isLog) {
                    LogQueue.Push(dockerIsRun);
                }
                String[] images = LoadConfig.getConfig(PropertiesDef.DockerImageName).split("|");
                if (images.length > 0) {
                    String lowerCase = dockerIsRun.toLowerCase();
                    for (String image : images) {
                        if (lowerCase.indexOf(image.toLowerCase()) < 0) {
                            return false;
                        }
                    }
                } else {
                    return true;
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
    public static boolean installProgram(String program, boolean isLog) {

        //start /wait D:\kyf\java-workspace\SM-resource\npp.7.8.6.Installer.exe /verysilent sp-
        try {
            //String result = executeCMD("start /wait "+program+" /verysilent sp-", isLog);
            String result = executeCMD("start /wait " + program, isLog);
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

    //docker服务操作
    public static boolean dockerServiceOperator(String program, boolean isLog) {
        try {
            String exeBatPath = Env.getEnv(PropertiesDef.ProgramHomeKey) + "terminal-lite" + File.separator + "install_env.bat";
            String cmd = exeBatPath + " " + program;
            String result = executeCMD(cmd, isLog);
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
        Process process = null;
        try {
            String strCmd = "cmd /c " + cmd;
            //执行指令操作放入日志队列
            if (isLog) {
                LogQueue.Push(strCmd);
            }
            process = runtime.exec(strCmd);

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (content != null) {
                sb.append(content + "\n");
                content = br.readLine();
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    //执行命令
    private static String executeCMD(String cmd, String[] channelCmd, boolean isLog) throws Exception {
        Process process = null;
        try {
            //执行指令操作放入日志队列
            if (isLog) {
                LogQueue.Push("cmd /c " + cmd + " | " + String.join(" | ", channelCmd));
            }
            process = runtime.exec(cmd, channelCmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (content != null) {
                sb.append(content + "\n");
                content = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

}

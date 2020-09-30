package com.ps.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    public static boolean checkImageHaveRunHistory(boolean isLog) {
        try {
            //判断映像是否运行
            String cmd = "docker ps -a";
            String dockerIsRun = executeCMD(cmd, isLog);
            if (null != dockerIsRun || dockerIsRun != "") {
                String[] images = LoadConfig.getConfig(PropertiesDef.DockerImageName).split("\\|");
                if (images.length > 0) {
                    String lowerCase = dockerIsRun.toLowerCase();
                    for (String image : images) {
                        if (lowerCase.indexOf(image.toLowerCase()) < 0) {
                            return false;
                        }
                    }
                    return true;
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

    public static boolean checkImageRun(boolean isLog) {
        try {
            //判断映像是否运行
            String cmd = "docker ps";
            String dockerIsRun = executeCMD(cmd, isLog);
            if (null != dockerIsRun || dockerIsRun != "") {
                if (isLog) {
                    LogQueue.Push(dockerIsRun);
                }
                String[] images = LoadConfig.getConfig(PropertiesDef.DockerImageName).split("\\|");
                if (images.length > 0) {
                    String lowerCase = dockerIsRun.toLowerCase();
                    for (String image : images) {
                        if (lowerCase.indexOf(image.toLowerCase()) < 0) {
                            return false;
                        }
                    }
                    return true;
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

    //验证加载镜像是否成功
    public static boolean checkImageLoad(boolean isLog) {
        try {
            //判断映像是否运行
            String cmd = "docker images";
            String dockerIsRun = executeCMD(cmd, isLog);
            if (null != dockerIsRun || dockerIsRun != "") {
                if (isLog) {
                    LogQueue.Push(dockerIsRun);
                }
                String[] images = LoadConfig.getConfig(PropertiesDef.DockerSrcImageName).split("\\|");
                if (images.length > 0) {
                    String lowerCase = dockerIsRun.toLowerCase();
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
    public static String executeCMD(String cmd, boolean isLog) throws Exception {
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
                String tem = content + "\n";
                sb.append(tem);
                try {
                    if (isLog) {
                        LogQueue.Push(tem);
                    }
                } catch (Exception e2) {
                }
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
    public static String execute(String cmd, boolean isLog){
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
                String tem = content + "\n";
                sb.append(tem);
                try {
                    if (isLog) {
                        LogQueue.Push(tem);
                    }
                } catch (Exception e2) {
                }
                content = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            try {
                if (isLog) {
                    LogQueue.Push(e.getMessage());
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    private static String getDockerInspect(String containerName, boolean isLog) {
        Process process = null;
        try {
            String strCmd = "cmd /c docker inspect " + containerName;
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
                String tem = content + "\n";
                sb.append(tem);
                try {
                    if (isLog) {
                        LogQueue.Push(tem);
                    }
                } catch (Exception e2) {
                }
                content = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            try {
                if (isLog) {
                    LogQueue.Push(e.getMessage());
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    public static String hostIp (String containerName, boolean isLog) {
        String inspectInfo = getDockerInspect(containerName, isLog);
        if (inspectInfo == null || "".equals(inspectInfo)) {
            return "";
        }
        JSONArray arr = JSON.parseArray(inspectInfo);
        //只取第一级配置
        if (arr != null && arr.size() > 0) {
            JSONObject applicationInspectJson= (JSONObject)arr.get(0);
            if (applicationInspectJson.containsKey("HostConfig")) {
                JSONObject config = applicationInspectJson.getJSONObject("HostConfig");
                if (config.containsKey("ExtraHosts")) {
                    JSONArray extraHosts = config.getJSONArray("ExtraHosts");
                    if ( extraHosts != null && extraHosts.size() > 0 ) {
                        String hostName = extraHosts.getString(0);
                        String[] ips = hostName.split(":");
                        if (ips.length > 0 ){
                            return ips[1];
                        }
                    } //end if
                }//end if
            }//end if
        }//end if
        return "";
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
                String tem = content + "\n";
                sb.append(tem);
                try {
                    if (isLog) {
                        LogQueue.Push(tem);
                    }
                } catch (Exception e2) {
                }
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

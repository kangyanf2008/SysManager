package com.ps.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerUtils {

    static Runtime runtime = Runtime.getRuntime();

    //判断是否安装服务
    public static boolean checkServerInstall(String serviceName )  {
        try {
            String r = executeCMD("sc query type= all state= all |findstr /i service_name.* | findstr /i"+serviceName);
            if (r.indexOf(serviceName) > 0) {
                return true;
            }
        } catch (Exception e){}
        return false;

    }

    //判断服务和映射是否运行
    public static boolean checkServerRun(String serviceName, String imageName) {
        try {
            if (serviceName != null && "" != serviceName) {
                String serviceIsRun = executeCMD("tasklist /fi \"imagename eq "+serviceName+"\"");
                if (null == serviceIsRun || serviceIsRun == "" || serviceIsRun.indexOf(serviceName) < 0) {
                    return false;
                }
            }

            //判断映像是否运行
            if (imageName != null && "" != imageName) {
                String dockerIsRun = executeCMD("docker ps | findstr /i "+imageName);
                if (null == dockerIsRun || dockerIsRun == "" ||  dockerIsRun.indexOf(imageName) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e){}
        return false;
    }

    //执行命令
    private static String executeCMD(String cmd) throws Exception {
        try {
            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (content != null) {
                sb.append(content);
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

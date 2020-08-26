package com.ps.config;

import com.ps.queue.LogQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class LoadConfig {
    private static volatile Properties ps = null;

    private static Properties GetConfigProperties() {
        if (ps == null) {
            synchronized (LoadConfig.class) {
                if (ps == null)  {
                    InputStream in = null;
                    try {
                        in = LoadConfig.class.getResourceAsStream("/resources/cfg.properties");
                        BufferedReader ipss = new BufferedReader(new InputStreamReader(in));
                        ps = new Properties();
                        ps.load(ipss);
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            LogQueue.Push(e.getMessage());
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e){}
                        }
                    }
                }
                return ps;
            }
        }
        return ps;
    }

    public static String getConfig(String key) {
       String value = GetConfigProperties().getProperty(key);
       if (value != null) {
           return value;
        } else {
           return "";
        }
    }


}

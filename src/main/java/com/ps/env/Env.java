package com.ps.env;

import java.util.Map;

public class Env {
    private static volatile Map<String,String> env = null;
    private Env(){}
    public static String getEnv(String key) {
        if (env == null) {
           synchronized (Env.class){
               if (env == null) {
                   env = System.getenv();
               }
               return env.get(key);
           }
        } else {
            return env.get(key);
        }
    }
}

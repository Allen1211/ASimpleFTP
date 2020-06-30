package com.allen.ftpserver.utils;

import com.allen.ftpserver.server.Config;
import com.google.gson.Gson;

import java.io.*;

/**
 * @ClassName ResourceUtil
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class ResourceUtil {

    public static void close(Closeable closeable){
        if(closeable == null){
            return;
        }
        try {
            closeable.close();
        }catch (IOException ioe){
            // do nothing to close silently
        }
        closeable = null;
    }

    public static void closeAll(Closeable ...closeables){
        for(Closeable closeable : closeables){
            close(closeable);
        }
    }

    public static Config readConfig() throws FileNotFoundException {
        FileReader reader = new FileReader(new File("config.json"));
        Gson gson = new Gson();
        return gson.fromJson(reader, Config.class);
    }
}

package com.allen.ftpserver;

import com.allen.ftpserver.auth.Authenticator;
import com.allen.ftpserver.auth.User;
import com.allen.ftpserver.server.Config;
import com.allen.ftpserver.server.FTPServer;
import com.allen.ftpserver.utils.ResourceUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * @ClassName Main
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class Main {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        Config config;
        try {
            config = ResourceUtil.readConfig();
            assertConfigSyntaxLegal(config);
        }catch (FileNotFoundException fileNotFoundException){
            // can not found config file, use default config instead
            config = new Config("127.0.0.1",21,
                    System.getProperty("user.home"), Collections.emptyList());
        }catch (JsonSyntaxException jsonSyntaxException){
            System.out.println("Config file has en syntax error :(");
            System.out.println(jsonSyntaxException.getMessage());
            System.out.println("Closing server..");
            return;
        }catch (JsonIOException jsonIOException){
            System.out.println("Failed to read config file:(");
            System.out.println(jsonIOException.getMessage());
            System.out.println("Closing server..");
            return;
        }catch (IllegalArgumentException illegalArgumentException){
            System.out.println(illegalArgumentException.getMessage());
            return;
        }
        File userBaseDir = new File(config.getUserBaseDir());
        if(!userBaseDir.exists()){
            System.out.println("UserBaseDir does not exist :(");
            System.out.println("Closing server..");
            return;
        }
        if(config.getUsers() != null){
            Map<String,String> userPasswordMap = new HashMap<>(config.getUsers().size());
            for(User user : config.getUsers()){
                userPasswordMap.put(user.getUsername(), user.getPassword());
            }
            Authenticator.usernamePasswordMap = userPasswordMap;
        }else{
            Authenticator.usernamePasswordMap = Collections.emptyMap();
        }

        // create the server
        FTPServer ftpServer = new FTPServer(config.getServerHost(),config.getServerPort(),userBaseDir);
        try {
            // then run it
            executor.execute(ftpServer);
        }catch (Exception e){
            executor.shutdown();
        }

    }

    private static void assertConfigSyntaxLegal(Config config) {
        Pattern ipv4Pattern = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
        Pattern ipv6Pattern = Pattern.compile("^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$");
        String host = config.getServerHost();
        if(!ipv4Pattern.matcher(host).matches() && !ipv6Pattern.matcher(host).matches()){
            System.out.println("Wrong host format !");
        }else if(config.getServerPort() < 0 || config.getServerPort() > 65535){
            throw new IllegalArgumentException("Port should between 0 and 65535 !");
        }
    }

}

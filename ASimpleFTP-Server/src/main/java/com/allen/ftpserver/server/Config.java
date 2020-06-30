package com.allen.ftpserver.server;

import com.allen.ftpserver.auth.User;

import java.util.List;

/**
 * @ClassName Config
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/29
 * @Version 1.0
 */
public class Config {

    private String serverHost;

    private int serverPort;

    private String userBaseDir;

    private List<User> users;

    public String getServerHost() {
        return serverHost;
    }

    public Config(String serverHost, int serverPort, String userBaseDir, List<User> users) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.userBaseDir = userBaseDir;
        this.users = users;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getUserBaseDir() {
        return userBaseDir;
    }

    public void setUserBaseDir(String userBaseDir) {
        this.userBaseDir = userBaseDir;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

package com.allen.ftpserver.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Authenticator
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class Authenticator {

    private String username;

    private String password;

    private boolean needLogin;

    private boolean needPassword;

    private boolean isLogin;

    public static Map<String, String> usernamePasswordMap = new HashMap<>();

    public Authenticator(boolean needLogin, boolean needPassword) {
        this.needLogin = needLogin;
        if(!needLogin){
            this.isLogin = true;
            this.needPassword = false;
        }else{
            this.needPassword = needPassword;
        }
    }

    /**
     * @return true if has login
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * check whether input username is ok
     */
    public boolean validateAndSetUsername(String username){
        if(usernamePasswordMap.containsKey(username)){
            this.username = username;
            return true;
        }
        return false;
    }


    public boolean login(String password){
        if (username == null){
            return false;
        }
        String correctPassword = usernamePasswordMap.get(username);
        this.isLogin = password.equals(correctPassword);
        return this.isLogin;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public boolean isNeedPassword() {
        return needPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

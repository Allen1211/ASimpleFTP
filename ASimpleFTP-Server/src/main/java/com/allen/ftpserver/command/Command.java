package com.allen.ftpserver.command;

import com.allen.ftpserver.server.FTPConnection;

/**
 * @ClassName Command
 * @Description Interface of all ftp command
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public interface Command {


    /**
     * execute this command
     * @param args command's arguments
     * @param connection user's connection
     */
    void execute(String[] args, FTPConnection connection);

    /**
     * check whether this command needs authenticate
     * @return true if this command needs authenticate
     */
    boolean needAuth();

    /**
     * get this command's help message
     * @return command's help message
     */
    String getHelpMsg();
}

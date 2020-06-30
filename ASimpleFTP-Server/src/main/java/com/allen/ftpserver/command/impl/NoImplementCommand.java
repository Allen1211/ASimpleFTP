package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.FTPConnection;

/**
 * @ClassName NoImplementCommand
 * @Description Represents command that is not implement by Server
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class NoImplementCommand extends AbstractCommand {

    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {

    }
}

package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

/**
 * @ClassName SystCommand
 * @Description SYST command, returns server operation system type
 * @Author XianChuLun
 * @Date 2020/5/29
 * @Version 1.0
 */
public class SystCommand extends AbstractCommand {
    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        connection.sendReply(Reply.COMMAND_CONFIRM, "UNIX Type: L8");
    }
}

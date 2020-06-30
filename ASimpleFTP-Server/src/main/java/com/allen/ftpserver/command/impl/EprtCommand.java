package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

/**
 * @ClassName EprtCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class EprtCommand extends AbstractCommand {

    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        args = args[0].split("\\|");
        String protocol = args[1];
        String host = args[2];
        String port = args[3];
        DataConnection dataConnection = connection.getDataConnection();
        dataConnection.enableActiveMode(host, Integer.parseInt(port));
        connection.sendReply(Reply.COMMAND_CONFIRM);
    }
}

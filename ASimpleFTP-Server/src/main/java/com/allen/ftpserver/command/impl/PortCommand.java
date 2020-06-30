package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

/**
 * @ClassName PortCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class PortCommand extends AbstractCommand {
    public PortCommand() {
        super(true);
    }

    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        if (CollectionUtil.isEmpty(args)) {
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter");
            return;
        }
        args = args[0].split(",");
        String host = args[0] + "." + args[1] + "." + args[2] + "." + args[3];
        int port = Integer.parseInt(args[4]) * 256 + Integer.parseInt(args[5]);
        DataConnection dataConnection = connection.getDataConnection();
        dataConnection.enableActiveMode(host, port);
        connection.sendReply(Reply.COMMAND_CONFIRM);
    }
}

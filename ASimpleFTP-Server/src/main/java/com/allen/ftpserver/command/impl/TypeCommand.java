package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

/**
 * @ClassName TypeCommand
 * @Description Set the transfer Data Type
 * @Author XianChuLun
 * @Date 2020/5/29
 * @Version 1.0
 */
public class TypeCommand extends AbstractCommand {
    /**
     * execute this command
     *
     * @param args  first argument (single character) : A - ASCII, E -EBCDIC, I - Image (binary)
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        if(CollectionUtil.isEmpty(args)){
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameters");
            return;
        }
        String type = args[0];
        DataConnection dataConnection = connection.getDataConnection();
        dataConnection.setAsciiMode("A".equals(type));
        connection.sendReply(Reply.COMMAND_CONFIRM, "Type set to " + type);
    }
}

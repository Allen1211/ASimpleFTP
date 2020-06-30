package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName OptsCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class OptsCommand extends AbstractCommand {
    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        String clientCharset = args[0].toUpperCase().replace("-", "");
        if(clientCharset.contains("UTF8")){
            DataConnection dataConnection = connection.getDataConnection();
            dataConnection.setCharsets(StandardCharsets.UTF_8);
            connection.sendReply(Reply.COMMAND_CONFIRM, "Charset UTF8 On");
        }
//        else if(clientCharset.contains("GBK")){
//            connection.setCharsets(StandardCharsets.ISO_8859_1);
//            connection.sendReply(Reply.COMMAND_CONFIRM, "Charset GBK On");
//        }
        else{
            connection.sendReply(Reply.PARAMETER_WRONG, "Charset Not Support");
        }
    }
}

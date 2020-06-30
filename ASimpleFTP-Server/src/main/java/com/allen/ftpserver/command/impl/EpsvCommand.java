package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @ClassName EpsvCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class EpsvCommand extends AbstractCommand {

    public EpsvCommand() {
        super(true);
    }

    @Override
    public void execute(String[] args, FTPConnection connection) {
        DataConnection dataConnection = connection.getDataConnection();
        try {
            ServerSocket serverSocket = dataConnection.enablePassiveMode();
            int port = serverSocket.getLocalPort();

            connection.sendReply(Reply.EXTENDED_PASSIVE_MODE_OPEN,
                    "Enabled Extended Passive Mode (|||" + port + "|)");
        } catch (IOException e) {
            connection.sendReply(Reply.CANNOT_OPEN_DATA_CONNECTION, "Failed to open passive mode :(");
        }
    }
}

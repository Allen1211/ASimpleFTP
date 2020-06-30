package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @ClassName PasvCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class PasvCommand extends AbstractCommand {

    public PasvCommand() {
        super(true, null);
    }

    /**
     * execute this command
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        DataConnection dataConnection = connection.getDataConnection();
        try {
            ServerSocket serverSocket = dataConnection.enablePassiveMode();
            String host = connection.getFtpServer().getServerHost();
            int port = serverSocket.getLocalPort();
            String address = host.replace('.',',');
            String addressPort = port / 256 + "," + port % 256;

            System.out.println(address);
            System.out.println(addressPort);
            connection.sendReply(Reply.PASSIVE_MODE_OPEN,
                    "Enabled Passive Mode (" + address + "," + addressPort + ")");
        }catch(IOException e){
            connection.sendReply(Reply.CANNOT_OPEN_DATA_CONNECTION, "Failed to open passive mode :(");
        }
    }
}

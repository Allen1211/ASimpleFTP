package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.DataConnection;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

/**
 * @ClassName AborCommand
 * @Description This command tells the server to abort the previous FTP
 * service command and any associated transfer of data.
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class AborCommand extends AbstractCommand {

    public AborCommand() {
        super(false, "Tells the server to abort the previous FTP\n" +
                " service command and any associated transfer of data.\n Usage: ABOR <CRLF>");
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
        dataConnection.abortDataTransfers();
        connection.sendReply(Reply.OPERATION_SUCCESS, "All data transfers have been aborted");
    }

}

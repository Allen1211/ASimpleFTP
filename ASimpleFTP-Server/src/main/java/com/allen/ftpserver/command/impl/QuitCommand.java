package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.ResourceUtil;

/**
 * @ClassName QuitCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class QuitCommand extends AbstractCommand {

    @Override
    public void execute(String[] args, FTPConnection connection) {
        connection.sendReply(Reply.CLOSE_CONTROLLER_CONNECTION, "Bye");
        ResourceUtil.close(connection);
    }
}

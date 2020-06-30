package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.command.Command;
import com.allen.ftpserver.command.CommandFactory;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

/**
 * @ClassName HelpCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class HelpCommand extends AbstractCommand {

    @Override
    public void execute(String[] args, FTPConnection connection) {
        if(CollectionUtil.isEmpty(args)){
            // send commonly help message
        }else{
            // send help message of specific command
            String commandName = args[0];
            Command command = CommandFactory.getInstance(commandName);
            if(command == null){
                connection.sendReply(Reply.UNKNOWN_COMMAND);
                return;
            }
            String helpMsg = command.getHelpMsg();
            if(helpMsg == null){
                helpMsg = "This command has no help message available :(";
            }
            connection.sendData(helpMsg);
        }
        connection.sendReply(Reply.OPERATION_SUCCESS);
    }
}

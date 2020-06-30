package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.auth.Authenticator;
import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.command.Command;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

/**
 * @ClassName AuthCheckCommand
 * @Description decorator for command that needs authenticate
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class AuthCheckCommand extends AbstractCommand {

    // command to decorate
    protected Command command;

    public AuthCheckCommand(Command command) {
        super(false, null);
        this.command = command;
    }

    /**
     * execute the specific command only logged in
     *
     * @param args       command's arguments
     * @param connection user's connection
     */
    @Override
    public void execute(String[] args, FTPConnection connection) {
        Authenticator authenticator = connection.getAuthenticator();
        if(authenticator.isLogin()){
            command.execute(args, connection);
        }else {
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "No Permissions; Please Log in");
        }
    }

}

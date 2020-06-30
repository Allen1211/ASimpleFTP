package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.auth.Authenticator;
import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

/**
 * @ClassName UserCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class UserCommand extends AbstractCommand {
    @Override
    public void execute(String[] args, FTPConnection connection) {
        Authenticator authenticator = connection.getAuthenticator();
        if (!authenticator.isNeedLogin()) {
            connection.sendReply(Reply.LOGIN_SUCCESS, "No Need To Login");
        } else if (authenticator.isLogin()) {
            connection.sendReply(Reply.LOGIN_SUCCESS, "Already Login");
        } else {
            if (CollectionUtil.isEmpty(args)) {
                connection.sendReply(Reply.PARAMETER_WRONG, "Missing username");
            }else if(!authenticator.validateAndSetUsername(args[0])){
                connection.sendReply(Reply.CANNOT_LOGIN, "Unknown user");
            }else{
                connection.sendReply(Reply.NEED_PASSWORD);
            }
        }
    }
}

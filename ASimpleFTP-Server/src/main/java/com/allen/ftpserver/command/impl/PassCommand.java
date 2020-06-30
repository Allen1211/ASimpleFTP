package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.auth.Authenticator;
import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.file.FileSystemFactory;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

/**
 * @ClassName PassCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class PassCommand extends AbstractCommand {
    @Override
    public void execute(String[] args, FTPConnection connection) {
        Authenticator authenticator = connection.getAuthenticator();
        if (!authenticator.isNeedLogin()) {
            connection.sendReply(Reply.LOGIN_SUCCESS, "No Need To Login");
        } else if (authenticator.isLogin()) {
            connection.sendReply(Reply.LOGIN_SUCCESS);
        } else {
            if (CollectionUtil.isEmpty(args)) {
                connection.sendReply(Reply.PARAMETER_WRONG, "Missing password");
            }else if(!authenticator.login(args[0])){
                connection.sendReply(Reply.CANNOT_LOGIN, "Wrong password");
            }else{
                FileSystem fileSystem = FileSystemFactory.createInstance(
                        connection.getFtpServer().getUserBaseDir(),
                        authenticator.getUsername()
                );
                connection.setFileSystem(fileSystem);
                connection.sendReply(Reply.LOGIN_SUCCESS);
            }
        }
    }
}

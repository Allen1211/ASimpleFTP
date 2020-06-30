package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;

import java.io.IOException;

/**
 * @ClassName PwdCommand
 * @Description This command causes the name of the current working directory to be returned in the reply
 * @Author XianChuLun
 * @Date 2020/5/29
 * @Version 1.0
 */
public class PwdCommand extends AbstractCommand {

    public PwdCommand() {
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
        FileSystem fileSystem = connection.getFileSystem();
        try {
            String cwd = fileSystem.getCurrentDir().getCanonicalPath();
            System.out.println("pwd: "+ cwd);
            connection.sendReply(Reply.PATHNAME_CREATED, String.format("\"%s\" Created", cwd));
        }catch(IOException e){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "FileSystem not Support");
        }
    }
}

package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName MkdCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class MkdCommand extends AbstractCommand {

    public MkdCommand() {
        super(true, "Usage: MKD  <SP> <pathname> <CRLF>");
    }

    @Override
    public void execute(String[] args, FTPConnection connection) {
        if(CollectionUtil.isEmpty(args)){
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter: <path>");
        }
        String path = args[0];
        FileSystem fileSystem = connection.getFileSystem();
        File file = fileSystem.findFileByPath(path);
        if(file.exists()){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "Directory already exist or path name duplicate");
            return;
        }
        boolean createSuccess = fileSystem.createDirectory(file);
        if(createSuccess){
            try {
                connection.sendReply(Reply.PATHNAME_CREATED,
                        "\"" + file.getCanonicalPath() + "\" created");
            } catch (IOException e) {
                e.printStackTrace();
                connection.sendReply(Reply.ACTION_NOT_TAKEN, "FileSystem not Support");
            }
        }else{
            connection.sendReply(Reply.FILE_ACTION_ABORTED, "Failed to create directory.");
        }
    }
}

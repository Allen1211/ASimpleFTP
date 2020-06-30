package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @ClassName SizeCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class SizeCommand extends AbstractCommand {
    public SizeCommand() {
        super(true, null);
    }

    @Override
    public void execute(String[] args, FTPConnection connection) {
        if(CollectionUtil.isEmpty(args)){
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing Parameter");
            return;
        }
        FileSystem fileSystem = connection.getFileSystem();
        try {
            File file = fileSystem.findExistFileByPath(args[0]);
            connection.sendReply(Reply.DATA_CONNECTION_OPEN);
            connection.sendData(String.valueOf(file.length()));
        }catch(FileNotFoundException e){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "no such file or directory: "+args[0]);
        }
    }
}

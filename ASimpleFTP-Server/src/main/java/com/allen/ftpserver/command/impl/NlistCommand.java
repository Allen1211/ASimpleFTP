package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;
import com.allen.ftpserver.utils.FormatUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @ClassName NlistCommand
 * @Description  The server will return a stream of names of files and no other information.
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class NlistCommand extends AbstractCommand {

    public NlistCommand() {
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
        File[] files;
        FileSystem fileSystem = connection.getFileSystem();
        try {
            files = fileSystem.listDirectory(CollectionUtil.isEmpty(args)?null:getArgWithSpace(args));
        }catch(FileNotFoundException e){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "File not found!");
            return;
        }
        String data = FormatUtil.formatNameList(files);
        connection.sendData(data);
        connection.sendReply(Reply.OPERATION_SUCCESS, "directory name list successfully");

    }
}

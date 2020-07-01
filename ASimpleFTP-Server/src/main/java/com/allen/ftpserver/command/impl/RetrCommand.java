package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @ClassName RetrCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/2
 * @Version 1.0
 */
public class RetrCommand extends AbstractCommand {

    public RetrCommand() {
        super(true);
    }

    @Override
    public void execute(String[] args, FTPConnection connection) {
        if(CollectionUtil.isEmpty(args)){
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter: <path>");
        }
        String path = getArgWithSpace(args);
        FileSystem fileSystem = connection.getFileSystem();
        try {
            File file = fileSystem.findExistFileByPath(path);
            if(file.isDirectory()){
                connection.sendReply(Reply.ACTION_NOT_TAKEN, path + " : not a file, is a directory");
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            connection.sendDataAsync(fileInputStream, (isSuccess)->{
                if(isSuccess){
                    connection.sendReply(Reply.OPERATION_SUCCESS, "File transfer successfully");
                }else{
                    connection.sendReply(Reply.REQUESTED_ACTION_ABORTED, "File transfer failed");
                }
            });
        } catch (FileNotFoundException e) {
            connection.sendReply(Reply.ACTION_NOT_TAKEN, path +" : No such file or directory");
        }
    }
}

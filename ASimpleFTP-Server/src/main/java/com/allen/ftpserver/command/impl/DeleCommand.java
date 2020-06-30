package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName DeleCommand
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class DeleCommand extends AbstractCommand {

    public DeleCommand() {
        super(true, "Usage: DELE <SP> <pathname> <CRLF>");
    }

    @Override
    public void execute(String[] args, FTPConnection connection) {
        if (CollectionUtil.isEmpty(args)) {
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter: <path>");
            return;
        }
        String path = args[0];
        FileSystem fileSystem = connection.getFileSystem();
        try {
            File file = fileSystem.findExistFileByPath(path);
            if(file.isDirectory()){
                connection.sendReply(Reply.ACTION_NOT_TAKEN, "Not a single file, is a directory");
                return;
            }
            boolean success = fileSystem.deleteFile(file);
            if(success){
                connection.sendReply(Reply.ACTION_COMPLETE, "Delete successfully");
            }else{
                connection.sendReply(Reply.ACTION_NOT_TAKEN, "Failed to delete");
            }
        } catch (FileNotFoundException e) {
            connection.sendReply(Reply.ACTION_NOT_TAKEN, path + " : No such file or directory");
        } catch (IOException e) { // probably has no permission to delete
            e.printStackTrace();
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "No Permission");
        }

    }
}

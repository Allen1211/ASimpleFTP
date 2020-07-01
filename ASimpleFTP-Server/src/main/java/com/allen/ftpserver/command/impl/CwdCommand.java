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
 * @ClassName CwdCommand
 * @Description  CHANGE WORKING DIRECTORY
 * @Author XianChuLun
 * @Date 2020/5/29
 * @Version 1.0
 */
public class CwdCommand extends AbstractCommand {

    public CwdCommand() {
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
        if(CollectionUtil.isEmpty(args)){
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter: path");
            return;
        }
        String path = getArgWithSpace(args);
        FileSystem fileSystem = connection.getFileSystem();
        try {
            File file = fileSystem.findExistFileByPath(path);
            if(file.isDirectory()){
                // change the current work directory
                fileSystem.setCurrentDir(file);
                connection.sendReply(Reply.ACTION_COMPLETE,
                        "Change the current working directory to: "+ file.getCanonicalPath());
            }else{
                connection.sendReply(Reply.ACTION_NOT_TAKEN, path + " : Not a directory");
            }
        }catch(FileNotFoundException e){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, path +" : No such file or directory");
        }catch (IOException e){
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "FileSystem Not Support!");
        }
    }
}

package com.allen.ftpserver.command.impl;

import com.allen.ftpserver.command.AbstractCommand;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.server.FTPConnection;
import com.allen.ftpserver.server.Reply;
import com.allen.ftpserver.utils.CollectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName StorCommand
 * @Description This command causes the server-DTP to accept the data
 * transferred via the data connection and to store the data as a file at the server site.
 * If the file specified in the pathname exists at the server site,
 * then its contents shall be replaced by the data being transferred.
 * A new file is created at the server site if the file specified in the pathname does not already exist.
 * @Author XianChuLun
 * @Date 2020/6/3
 * @Version 1.0
 */
public class StorCommand extends AbstractCommand {

    public StorCommand() {
        super(true, "Store the file on server.\nUsage: STOR <SP> <pathname> <CRLF>");
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
            connection.sendReply(Reply.PARAMETER_WRONG, "Missing parameter: <path>");
        }
        String path = args[0];
        FileSystem fileSystem = connection.getFileSystem();
        File file = fileSystem.findFileByPath(path);
        if(file.exists()){
            connection.sendReply(Reply.FILE_ACTION_ABORTED, path + " already exists!");
            return;
        }
        try {
            if(!fileSystem.createFile(file)){
                connection.sendReply(Reply.FILE_ACTION_ABORTED, "Failed to create file.");
                return;
            }
        } catch (IOException e) {
            connection.sendReply(Reply.FILE_ACTION_ABORTED, "Failed to create file.");
        }
        try {
            connection.receiveDataAsync(new FileOutputStream(file), (success)->{
                if(success){
                    connection.sendReply(Reply.OPERATION_SUCCESS, "File store successfully");
                }else{
                    connection.sendReply(Reply.REQUESTED_ACTION_ABORTED, "File store failed");
                }
            });
        }catch(FileNotFoundException e){ // probably has no permission to open the file
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "No permission");
        }
    }
}

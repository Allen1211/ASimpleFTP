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
 * @ClassName DirCommand, or List Command
 * @Description RFC959 List command
 * If the pathname specifies a directory or other group of files ,
 * a list of files  in the specified directory. the server should transfer a list of files
 * in the specified directory.  If the pathname specifies a  file then the server should send
 * current information on the file.
 * A null argument implies the user’s current working or default directory.
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class DirCommand extends AbstractCommand {

    public DirCommand() {
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
        File[] files;
        try {
            files = fileSystem.listDirectory(CollectionUtil.isEmpty(args) ? null : getArgWithSpace(args));
        } catch (FileNotFoundException e) {
            connection.sendReply(Reply.ACTION_NOT_TAKEN, "File not found!");
            return;
        }
        String data = FormatUtil.formatDir(fileSystem, files);
        connection.sendData(data);
        connection.sendReply(Reply.OPERATION_SUCCESS, "directory list successfully");
    }
}

package com.allen.ftpserver.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName FileSystem
 * @Description Represents the local file system, provides interface for I/O operation
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public interface FileSystem {

    int LINUX = 1;
    int WIN = 2;

    /**
     * whether dir exists
     * @param dirStr directory string
     * @return True if exists
     */
    boolean isDirExist(String dirStr);

    /**
     * get the root directory
     * @return root directory
     */
    File getRootDir();

    /**
     * get the current directory
     * @return current directory
     */
    File getCurrentDir();

    /**
     * find file according to the input string path
     * relative path supported such as "../xx/xx", "./xxx"
     * @param path absolute or relative path of a file or directory
     * @return The found file, null if path is null,
     */
    File findFileByPath(String path);

    /**
     * Find file that exists, according to the input string path
     * relative path supported such as "../xx/xx", "./xxx"
     * @param path absolute or relative path of a file or directory
     * @return The found file
     * @throws FileNotFoundException if the file does not exists
     */
    File findExistFileByPath(String path) throws FileNotFoundException;

    /**
     * list directory of current path
     * @return files list
     */
    File[] listDirectory(String dirStr) throws FileNotFoundException;

    String getFilePermissionRwxFormString(File file);

    /**
     * check whether server's operation system is Linux
     * @return true if is liunx
     */
    boolean isLinux();

    /**
     * set the current working directory
     * @param currentDir current working directory
     */
    void setCurrentDir(File currentDir);

    /**
     * create file on fileSystem
     * @param file file
     * @return true if success to create
     */
    boolean createFile(File file) throws IOException;

    /**
     * create directory on fileSystem
     * @param file file
     * @return true if success to create
     */
    boolean createDirectory(File file);

    /**
     * delete file on fileSystem
     * @param file file to delete
     * @return true if success
     * @throws IOException en I/O error occurs
     */
    boolean deleteFile(File file) throws IOException;
}

package com.allen.ftpserver.file;

import com.allen.ftpserver.file.impl.LinuxFileSystem;
import com.allen.ftpserver.file.impl.WinFileSystem;

import java.io.File;

/**
 * @ClassName FileSystemFactory
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class FileSystemFactory {

    public static FileSystem createInstance(File userBaseDir,String username){
        File userRootDir = new File(userBaseDir, username);
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            return new WinFileSystem(userRootDir);
        }else{
            return new LinuxFileSystem(userRootDir);
        }
    }
}

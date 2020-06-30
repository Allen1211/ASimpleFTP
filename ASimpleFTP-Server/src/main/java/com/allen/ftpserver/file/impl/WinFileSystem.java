package com.allen.ftpserver.file.impl;

import com.allen.ftpserver.file.FileSystem;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName FileSystemImpl
 * @Description  Implement FileSystem
 * @see FileSystem
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class WinFileSystem extends AbstractFileSystem {

    public WinFileSystem(File rootDir) {
        super(rootDir);
        this.RELATIVE_PATH_PATTERN = Pattern.compile("(\\.+)(\\\\[^.]*)?");
    }

    @Override
    public String getFilePermissionRwxFormString(File file) {
        return "";
    }

    @Override
    public boolean isLinux() {
        return false;
    }

    @Override
    public File findFileByPath(String path){
        if(path == null){
            return null;
        }
        if(path.matches("[a-zA-Z]:.*")){ // absolute path
            return new File(path);
        }else if(path.matches("\\.+(\\\\.*)?")){ // relative path
            Matcher matcher = RELATIVE_PATH_PATTERN.matcher(path);
            matcher.matches();
            String dots = matcher.group(1);
            String pathWithoutDots = matcher.group(2);
            File parent = currentDir.getParentFile();
            if(pathWithoutDots == null || pathWithoutDots.isEmpty()){
                return dots.length() == 1 ? currentDir : parent;
            }else{
                return dots.length() == 1 ?
                        new File(currentDir,pathWithoutDots) : new File(parent, pathWithoutDots);
            }
        }else {
            return new File(currentDir,path);
        }
    }



}

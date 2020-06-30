package com.allen.ftpserver.file.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName FileSystemImpl
 * @Description  Implement FileSystem
 * @see com.allen.ftpserver.file.FileSystem
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class LinuxFileSystem extends AbstractFileSystem {

    public LinuxFileSystem(File rootDir) {
        super(rootDir);
        this.RELATIVE_PATH_PATTERN = Pattern.compile("(\\.+)(/[^.]*)?");
    }


    @Override
    public String getFilePermissionRwxFormString(File file) {
        Path path = Paths.get(file.getAbsolutePath());
        PosixFileAttributeView posixView = Files.getFileAttributeView(path,
                PosixFileAttributeView.class);
        try{
            PosixFileAttributes attribs = posixView.readAttributes();
            Set<PosixFilePermission> permissions = attribs.permissions();
            // Convert the file permissions into the rwxrwxrwx string form
            return PosixFilePermissions.toString(permissions);
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean isLinux() {
        return true;
    }

    @Override
    public File findFileByPath(String path){
        if(path == null){
            return null;
        }
        if(path.startsWith("/")){ //root path
            return new File(path);
        }else if(path.matches("\\.+(\\\\.*)?")){ // relative path with dot ..
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
            //            if(!file.exists()){
//                file = new File(path);
//            }
            return new File(currentDir,path);
        }
    }


}

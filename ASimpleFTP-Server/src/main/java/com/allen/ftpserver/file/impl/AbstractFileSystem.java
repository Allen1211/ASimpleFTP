package com.allen.ftpserver.file.impl;

import com.allen.ftpserver.file.FileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * @ClassName FileSystemImpl
 * @Description Implement FileSystem
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 * @see FileSystem
 */
public abstract class AbstractFileSystem implements FileSystem {

    protected final File rootDir;

    protected File currentDir;

    protected Pattern RELATIVE_PATH_PATTERN;

    public AbstractFileSystem(File rootDir) {
        this.rootDir = rootDir;
        this.currentDir = rootDir;
        if (!rootDir.exists()) {
            boolean success = rootDir.mkdirs();
            if (!success) {
                // TODO handle mkdirs fail
            }
        }
    }

    @Override
    public File[] listDirectory(String dirStr) throws FileNotFoundException {
        if (dirStr == null) {
            return currentDir.listFiles();
        }
        File dir = findExistFileByPath(dirStr);
        return dir.listFiles();
    }

    abstract public String getFilePermissionRwxFormString(File file);

    @Override
    abstract public boolean isLinux();

    @Override
    public boolean isDirExist(String path) {
        boolean exist = false;
        if (path == null) {
            return false;
        }
        File dir = new File(path);
        if (dir.exists()) {
            exist = true;
        }
        return exist;
    }

    @Override
    abstract public File findFileByPath(String path);

    @Override
    public File findExistFileByPath(String path) throws FileNotFoundException {
        File file = findFileByPath(path);
        if (file == null || !file.exists()) {
            throw new FileNotFoundException(path);
        }
        return file;
    }


    @Override
    public File getRootDir() {
        return rootDir;
    }

    @Override
    public File getCurrentDir() {
        return currentDir;
    }

    @Override
    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }

    @Override
    public boolean createFile(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                return false;
            }
        }
        if (!file.exists()) {
            return file.createNewFile() && file.exists();
        }
        return true;
    }

    @Override
    public boolean createDirectory(File file) {
        if (!file.exists()) {
            return file.mkdirs() && file.exists();
        }
        return true;
    }

    @Override
    public boolean deleteFile(File file) throws IOException {
        if (file.isDirectory()) { // delete directory reclusive
            Files.walk(file.toPath())
                 .sorted(Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(File::delete);
            return !file.exists();
        } else {
            return file.delete() && !file.exists();
        }
    }
}

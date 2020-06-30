package com.allen.ftpserver.utils;

import com.allen.ftpserver.file.FileSystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName FormatUtil
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class FormatUtil {



    public static String formatReply(int code, String msg) {
        return code + " " + msg + "\r\n";
    }

    public static String formatNameList(File[] files){
        StringBuilder sb = new StringBuilder("\r\n");
        for(File file : files){
            sb.append(file.getName()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    public static String formatDir(FileSystem fileSystem, File[] files) {
        // linux style format goes like:
        // permissions hard-links owner  group  size  last_modified_time  file_name
        //-rw-r--r--    1         1000   1000    0      May 28 01:46        1
        //-rw-r--r--    1         1000   1000    0      May 28 01:52        1.txt
        // windows style format goes like:
        // last_modified_time   dir type    size  name
        // 2020/04/21  16:18    <DIR>          .conda
        // 2020/04/21  15:55                43 .condarc
        // 2019/07/23  17:56    <DIR>          .config

        return fileSystem.isLinux() ? getLinuxStyleDirFormat(fileSystem, files)
                : getWinStyleDirFormat(fileSystem, files);
    }

    private static String getWinStyleDirFormat(FileSystem fileSystem, File[] files) {
        if(files == null){
            return "\r\n";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            String lastModified = sdf.format(new Date(file.lastModified()));
            String type = file.isDirectory() ? "<DIR>" : "";
            String size = file.isDirectory() ? "" : String.valueOf(file.length());
            String fileName = file.getName();
            String line = String.format("%s\t%s\t%s\t\t%s\r\n", lastModified,type, size,fileName);
            sb.append(line);
        }
        sb.append("\r\n");
        return sb.toString();
    }

    private static String getLinuxStyleDirFormat(FileSystem fileSystem, File[] files) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            String permissions = fileSystem.getFilePermissionRwxFormString(file);
            String lastModified = sdf.format(new Date(file.lastModified()));
            int hardLinks = file.isDirectory() ? 3 : 1;
//            String owners = Files.getOwner();
            long size = file.length();
            String fileName = file.getName();
            String line = String.format("%s\t%s\t%s\t%s\t%s\r\n",permissions, hardLinks,lastModified,size,fileName);
            sb.append(line);
        }
        sb.append("\r\n");
        return sb.toString();

    }

}

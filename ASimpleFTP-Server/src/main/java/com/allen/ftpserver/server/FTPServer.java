package com.allen.ftpserver.server;

import com.allen.ftpserver.utils.CollectionUtil;
import com.allen.ftpserver.utils.ResourceUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName FTPServer
 * @Description represent FTP Server
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class FTPServer implements Closeable, Runnable {

    private final String serverHost;

    private final int port;

    private ServerSocket serverSocket;

    private final Set<FTPConnection> ftpConnections = new CopyOnWriteArraySet<>();

    private final File userBaseDir;

    public FTPServer(String serverHost, int port, File userBaseDir) {

        this.serverHost = serverHost;
        this.port = port;
        this.userBaseDir = userBaseDir;

    }

    /**
     * run the FTP server
     */
    @Override
    public void run() {

        System.out.println("Server starting at port : " + port);

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) { // serverSocket start fail
            e.printStackTrace();
            return;
        }

        System.out.println("Server has started at port : " + port);

        while (serverSocket != null && !serverSocket.isClosed()){
            Socket clientSocket = null;
            FTPConnection ftpConnection = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("accept client connection");
            }catch(IOException e){
                ResourceUtil.close(clientSocket);
            }
            if(clientSocket != null){
                try {
                    ftpConnection = new FTPConnection(this, clientSocket);
                    ftpConnections.add(ftpConnection);
                }catch(IOException e){
                    ftpConnections.remove(ftpConnection);
                }
            }
        }

    }

    /**
     * Stop the server
     */
    @Override
    public void close() {
        // close the server socket
        ResourceUtil.close(serverSocket);
        // close all user connections
        if(!CollectionUtil.isEmpty(ftpConnections)){
            for(FTPConnection connection : ftpConnections){
                ResourceUtil.close(connection);
            }
        }
    }

    public void removeUserConnection(FTPConnection ftpConnection){
        if(ftpConnection != null){
            this.ftpConnections.remove(ftpConnection);
        }
    }

    public String getServerHost() {
        return serverHost;
    }

    public File getUserBaseDir() {
        return userBaseDir;
    }
}

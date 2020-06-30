package com.allen.ftpserver.server;

import com.allen.ftpserver.auth.Authenticator;
import com.allen.ftpserver.file.FileSystem;
import com.allen.ftpserver.utils.ResourceUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @ClassName FTPConnection
 * @Description A logical client-server point to point connection,
 * provides apis that ignore the differences between controller connection and data connection.
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class FTPConnection implements IFTPConnection, Closeable {

    private final FTPServer ftpServer;

    private final Authenticator authenticator;

    private FileSystem fileSystem;

    private final ControllerConnection controllerConnection;

    private final DataConnection dataConnection;

    private final ExecutorService controllerExecutor = Executors.newSingleThreadExecutor();

    private final ExecutorService dataTransferExecutor = new ThreadPoolExecutor(1, 4, 2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardOldestPolicy());

    public FTPConnection(FTPServer ftpServer, Socket controllerSocket) throws IOException {
        this.ftpServer = ftpServer;
        this.authenticator = new Authenticator(true, true);


        this.controllerConnection = new ControllerConnection(this, controllerSocket);
        this.dataConnection = new DataConnection(this);

        // Run as a new thread in SingleThreadExecutor
        controllerExecutor.execute(this.controllerConnection);
    }

    /**
     * close the controller connection
     */
    @Override
    public void close() {
        // close the controller socket
        if(this.controllerConnection != null){
            ResourceUtil.close(this.controllerConnection);
        }
        // remove this from FTPServer's userConnections
        this.ftpServer.removeUserConnection(this);
        // shutdown executors
        this.dataTransferExecutor.shutdown();
        this.controllerExecutor.shutdown();
    }

    @Override
    public void sendReply(Reply reply) {
        sendReply(reply, reply.getMessage());
    }

    @Override
    public void sendReply(Reply reply, String msg) {
        controllerConnection.sendReply(reply, msg);
    }

    @Override
    public void receiveData(OutputStream out) {
        dataConnection.receiveData(out);
    }

    @Override
    public void receiveDataAsync(OutputStream out, Consumer<Boolean> callback) {
        CompletableFuture.supplyAsync(()-> dataConnection.receiveData(out), dataTransferExecutor)
                .thenAccept(callback);
    }

    @Override
    public void sendData(byte[] data) {
        dataConnection.sendData(data);
    }

    @Override
    public void sendDataAsync(byte[] data, Consumer<Boolean> callback) {
        CompletableFuture.supplyAsync(()-> dataConnection.sendData(data), dataTransferExecutor)
                .thenAccept(callback);
    }

    @Override
    public void sendDataAsync(InputStream in, Consumer<Boolean> callback) {
        CompletableFuture.supplyAsync(()-> dataConnection.sendData(in), dataTransferExecutor)
                .thenAccept(callback);
    }

    @Override
    public Charset getDataConnectionCharset() {
        return dataConnection.getCharsets();
    }


    public FTPServer getFtpServer() {
        return ftpServer;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public ControllerConnection getControllerConnection() {
        return controllerConnection;
    }

    public DataConnection getDataConnection() {
        return dataConnection;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FTPConnection that = (FTPConnection) o;
        boolean fileSystemEq = false;
        if(fileSystem != null && that.fileSystem != null){
            fileSystemEq = fileSystem.equals(that.fileSystem);
        }
        return fileSystemEq &&
                controllerConnection.equals(that.controllerConnection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSystem, controllerConnection);
    }
}

package com.allen.ftpserver.server;

import com.allen.ftpserver.utils.ResourceUtil;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName DataConnection
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class DataConnection implements Closeable {

    private final FTPConnection ftpConnection;

    private CopyOnWriteArraySet<Socket> activeModeDataSockets;

    private ServerSocket passiveDataServerSocket;

    private boolean passiveMode;

    private int clientDataProtocol;

    private String clientDataHost;

    private int clientDataPort;

    private Charset charsets;

    private boolean asciiMode;

    private final int bufferSize;

    public DataConnection(FTPConnection ftpConnection) {
        this.ftpConnection = ftpConnection;
        this.activeModeDataSockets = new CopyOnWriteArraySet<>();
        this.asciiMode = false;
        this.charsets = StandardCharsets.UTF_8;
        this.passiveMode = false;
        this.bufferSize = 1024;
    }

    /**
     * read data from socket's InputStream to given OutPutStream
     *
     * @param out
     * @return
     */
    public boolean receiveData(OutputStream out) {
        Socket socket = openDataConnection();
        if (socket == null) {
            return false;
        }
        InputStream in = openSocketInputStream(socket);
        if (in == null) {
            return false;
        }
        // connection opened, then receive data
        BufferedInputStream bis = new BufferedInputStream(in);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] buffers = new byte[bufferSize];
        int readLength = -1;
        try {
            while ((readLength = bis.read(buffers)) != -1) {
                out.write(buffers, 0, readLength);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ftpConnection.sendReply(Reply.CONNECTION_CLOSED);
            return false;
        } finally {
            ResourceUtil.closeAll(bos, bis, socket);
        }
        return true;
    }

    /**
     * send byte array data
     *
     * @param datas byte array data
     */
    public boolean sendData(byte[] datas) {
        Socket socket = openDataConnection();
        if (socket == null) {
            return false;
        }
        OutputStream out = openSocketOutputStream(socket);
        if (out == null) {
            return false;
        }
        // connection opened , then send data
        try {
            doSendBytes(out, datas, datas.length);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ftpConnection.sendReply(Reply.CONNECTION_CLOSED);
            return false;
        } finally {
            activeModeDataSockets.remove(socket);
            ResourceUtil.close(out);
            ResourceUtil.close(socket);
        }

    }

    /**
     * send data from inputStream
     */
    public boolean sendData(InputStream in) {
        Socket socket = openDataConnection();
        if (socket == null) {
            return false;
        }
        OutputStream out = openSocketOutputStream(socket);
        if (out == null) {
            return false;
        }
        // connection opened , then send data
        BufferedInputStream bis = new BufferedInputStream(in);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        byte[] buffer = new byte[bufferSize];
        try {
            int readLength = -1;
            while ((readLength = bis.read(buffer)) != -1) {
                doSendBytes(out, buffer, readLength);
            }
            out.flush();
            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            ftpConnection.sendReply(Reply.CONNECTION_CLOSED);
            return false;
        } finally {
            activeModeDataSockets.remove(socket);
            ResourceUtil.closeAll(bis, bos, socket);
            ResourceUtil.close(out);
            ResourceUtil.close(socket);
        }
    }

    /**
     * do open data socket connection
     *
     * @return null if failed to open
     */
    private Socket openDataConnection() {
        Socket socket = null;
        // open connection
        try {
            // check whether passive mode is on
            if (isPassiveMode()) {
                socket = passiveDataServerSocket.accept();
            } else {
                socket = new Socket(clientDataHost, clientDataPort);
            }

            activeModeDataSockets.add(socket);
            // necessary to let client ready to receive data
            ftpConnection.sendReply(Reply.DATA_CONNECTION_OPEN);
            return socket;
        } catch (IOException e) {
//            e.printStackTrace();
            ftpConnection.sendReply(Reply.CANNOT_OPEN_DATA_CONNECTION);
            ResourceUtil.close(socket);
        }
        return null;
    }

    /**
     * do open socket's outputStream
     *
     * @return null if failed to open
     */
    private OutputStream openSocketOutputStream(Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            ftpConnection.sendReply(Reply.CANNOT_OPEN_DATA_CONNECTION);
            ResourceUtil.close(socket);
            return null;
        }
    }

    /**
     * do open socket's inputStream
     *
     * @return null if failed to open
     */
    private InputStream openSocketInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            ftpConnection.sendReply(Reply.CANNOT_OPEN_DATA_CONNECTION);
            ResourceUtil.close(socket);
            return null;
        }
    }

    /**
     * Do send the byte array based on the data transfer mode (ascii / binary)
     *
     * @param out   socket's outputStream
     * @param bytes byte array to send
     * @throws IOException if en I/O error occurs
     */
    private void doSendBytes(OutputStream out, byte[] bytes, int length) throws IOException {
        // insert \r before \n in ascii mode
        if (this.asciiMode) {
            byte preByte = 0;
            for (int i = 0; i < length; i++) {
                byte b = bytes[i];
                if (b == '\n' && preByte != '\r') {
                    out.write('\r');
                }
                out.write(b);
                preByte = b;
            }
        } else {  // send directly in binary mode
            out.write(bytes);
        }
    }

    /**
     * set data transfer mode to "passive"
     */
    public ServerSocket enablePassiveMode() throws IOException {
        if (isPassiveMode()) {
            return passiveDataServerSocket;
        } else {
            passiveDataServerSocket = new ServerSocket(0);
            passiveMode = true;
            return passiveDataServerSocket;
        }
    }

    /**
     * set data transfer mode to "active"
     *
     * @param clientDataHost client host
     * @param clientDataPort client data socket port
     */
    public void enableActiveMode(String clientDataHost, int clientDataPort) {
        if (isPassiveMode()) {
            ResourceUtil.close(passiveDataServerSocket);
        }
        passiveMode = false;
        this.clientDataHost = clientDataHost;
        this.clientDataPort = clientDataPort;
    }

    /**
     * abort all data transfer
     */
    public void abortDataTransfers() {
        close();
    }

    /**
     * Close all resources
     */
    @Override
    public void close() {
        if (this.activeModeDataSockets != null) {
            for (Socket socket : this.activeModeDataSockets) {
                ResourceUtil.close(socket);
            }
            this.activeModeDataSockets.clear();
        }
        ResourceUtil.close(this.passiveDataServerSocket);
    }

    public ServerSocket getPassiveDataServerSocket() {
        return passiveDataServerSocket;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public int getClientDataProtocol() {
        return clientDataProtocol;
    }

    public void setClientDataProtocol(int clientDataProtocol) {
        this.clientDataProtocol = clientDataProtocol;
    }

    public String getClientDataHost() {
        return clientDataHost;
    }

    public void setClientDataHost(String clientDataHost) {
        this.clientDataHost = clientDataHost;
    }

    public int getClientDataPort() {
        return clientDataPort;
    }

    public void setClientDataPort(int clientDataPort) {
        this.clientDataPort = clientDataPort;
    }

    public Charset getCharsets() {
        return charsets;
    }

    public void setCharsets(Charset charsets) {
        this.charsets = charsets;
    }

    public boolean isAsciiMode() {
        return asciiMode;
    }

    public void setAsciiMode(boolean asciiMode) {
        this.asciiMode = asciiMode;
    }

}

package com.allen.ftpserver.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * @ClassName ControllerConnection
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public interface IFTPConnection {


    /**
     * send reply via controller connection to user, with default message
     * @param reply reply defined in RFC959
     */
    void sendReply(Reply reply);

    /**
     * send reply via controller connection to user, with custom message
     * @param reply reply defined in RFC959
     * @param msg custom reply msg
     */
    void sendReply(Reply reply, String msg);

    /**
     * receive data from data connection, and store in outputStream
     */
    void receiveData(OutputStream out);

    /**
     * receive data from data connection, and store in outputStream
     */
    void receiveDataAsync(OutputStream out, Consumer<Boolean> callback);

    /**
     * send string data
     * @param data string data to send
     */
    default void sendData(String data){
        if(data == null){
            throw new NullPointerException("data can not be null");
        }
        sendData(data.getBytes(getDataConnectionCharset()));
    }

    /**
     * send byte array data
     * @param data byte array data to send
     */
    void sendData(byte[] data);

    /**
     * send string data
     * @param data string data to send
     */
    default void sendDataAsync(String data, Consumer<Boolean> callback){
        if(data == null){
            throw new NullPointerException("data can not be null");
        }
        sendDataAsync(data.getBytes(getDataConnectionCharset()), callback);
    }

    /**
     * send byte array data
     * @param callback callback function to execute
     */
    void sendDataAsync(byte[] data, Consumer<Boolean> callback);

    /**
     * send data from inputStream
     * @param in inputStream
     * @param callback callback function to execute
     */
    void sendDataAsync(InputStream in, Consumer<Boolean> callback);

    /**
     * Get dataConnection's charset, UTF-8 in default
     * @return The specific charset that the dataConnection using
     */
    Charset getDataConnectionCharset();
}

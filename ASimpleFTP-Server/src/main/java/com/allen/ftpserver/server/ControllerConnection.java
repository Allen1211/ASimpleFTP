package com.allen.ftpserver.server;

import com.allen.ftpserver.command.Command;
import com.allen.ftpserver.command.CommandFactory;
import com.allen.ftpserver.utils.FormatUtil;
import com.allen.ftpserver.utils.ResourceUtil;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * @ClassName ControllerConnection
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class ControllerConnection implements Closeable, Runnable {

    private final FTPConnection ftpConnection;

    private Socket controllerSocket;

    private final BufferedReader reader;

    private final BufferedWriter writer;

    public ControllerConnection(FTPConnection ftpConnection, Socket controllerConnection) throws IOException {
        this.ftpConnection = ftpConnection;
        this.controllerSocket = controllerConnection;
        InputStream in = controllerConnection.getInputStream();
        OutputStream out = controllerConnection.getOutputStream();
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    /**
     * keep processing
     */
    @Override
    public void run() {

        sendReply(Reply.SERVICE_READY);

        while (isConnectionAlive()) {
            String line = null;
            try {
                line = reader.readLine();
                doLog(line);
            } catch (IOException ioe) {
                ResourceUtil.close(this);
                return;
            }
            if (line == null || line.isEmpty()) {
                ResourceUtil.close(this);
                continue;
            }
            handleLineInput(line);
        }
    }

    private void doLog(String line) {
        if (ftpConnection.getAuthenticator().isLogin()) {
            String userName = ftpConnection.getAuthenticator().getUsername();
            System.out.println(String.format("(%s): %s", userName, line));
        } else {
            System.out.println(String.format("(visitor): %s", line));
        }
    }


    /**
     * send reply to user via controller connection, with default message
     *
     * @param reply reply defined in RFC959
     */
//    @Override
    public void sendReply(Reply reply) {
        sendReply(reply, reply.getMessage());
    }

    /**
     * send reply to user via controller connection, with custom message
     *
     * @param reply reply defined in RFC959
     * @param msg   custom reply msg
     */
//    @Override
    public void sendReply(Reply reply, String msg) {
        if (isConnectionAlive()) {
            String replyLine = FormatUtil.formatReply(reply.getCode(), msg);
            try {
                writer.write(replyLine);
                writer.flush();
            } catch (IOException e) {
                ResourceUtil.close(this);
            }
        }
    }

    private void handleLineInput(String line) {
        String[] parts = line.split("\\s+");
        String cmd = parts[0];
        String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : null;

        Command command = CommandFactory.getInstance(cmd);
        if (command == null) {
            this.sendReply(Reply.UNKNOWN_COMMAND);
            return;
        }
        command.execute(args, this.ftpConnection);
    }


    private boolean isConnectionAlive() {
        return controllerSocket != null && !controllerSocket.isClosed();
    }

    @Override
    public void close() {
        ResourceUtil.close(reader);
        ResourceUtil.close(writer);
        ResourceUtil.close(this.controllerSocket);
    }
}

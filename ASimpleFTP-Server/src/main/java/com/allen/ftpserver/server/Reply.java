package com.allen.ftpserver.server;

/**
 * @ClassName Reply
 * @Description
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public enum Reply {

    DATA_CONNECTION_OPEN(125, "Data connection open"),
    READY_TO_OPEN_DATA_CONNECTION(150, "File status okay; about to open data connection"),


    COMMAND_CONFIRM(200, "OK"),
    COMMAND_NOT_IMPLEMENTED(202, "Sorry! This command is not implemented by server"),
    CLOSE_CONTROLLER_CONNECTION(221, "Service closing control connection"),
    SERVICE_READY(220, "Service Ready (A-Simple-FTP-Server)"),
    LOGIN_SUCCESS(230, "Login Success"),
    OPERATION_SUCCESS(226, "operation successfully"),
    PASSIVE_MODE_OPEN(227, "Passive mode on"),
    EXTENDED_PASSIVE_MODE_OPEN(229, "Extended Passive mode on"),
    ACTION_COMPLETE(250, "Requested file action okay, completed"),
    PATHNAME_CREATED(257, "PATHNAME created"),
    NEED_PASSWORD(331, "Please enter your password"),

    CONNECTION_CLOSED(421, "Transfer aborted"),
    CANNOT_OPEN_DATA_CONNECTION(425, "Can’t open data connection"),
    REQUESTED_ACTION_ABORTED(451, " Requested action aborted. Local error in processing"),

    UNKNOWN_COMMAND(500, "Unknown command! Enter ? for help"),
    PARAMETER_WRONG(501, "Parameter wrong!"),
    CANNOT_LOGIN(530, "Login Failed!"),
    ACTION_NOT_TAKEN(550, "Requested action not taken"),
    FILE_ACTION_ABORTED(552, "Requested file action aborted");

    private int code;

    private String message;

    Reply(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

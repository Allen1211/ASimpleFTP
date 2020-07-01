package com.allen.ftpserver.command;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @ClassName Command
 * @Description The abstract base class of all ftp command
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public abstract class AbstractCommand implements Command {

    /**
     * whether this command's execution needs authentication
     */
    protected boolean needAuth;

    /**
     * command's help message
     */
    protected String helpMsg;


    public AbstractCommand() {
        this(false, null);
    }

    public AbstractCommand(boolean needAuth) {
        this(needAuth, null);
    }

    public AbstractCommand(boolean needAuth, String helpMsg) {
        this.needAuth = needAuth;
        this.helpMsg = helpMsg;
    }

    @Override
    public boolean needAuth() {
        return needAuth;
    }

    @Override
    public String getHelpMsg() {
        return helpMsg;
    }

    protected String getArgWithSpace(String[] args) {
        if (args == null) {
            throw new NullPointerException("argument array should not be null");
        } else if (args.length == 1) {
            return args[0];
        } else {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
    }

}

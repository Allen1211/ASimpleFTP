package com.allen.ftpserver.command;

import com.allen.ftpserver.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CommandFactory
 * @Description To produce and hold command instance map, get command instance by command name
 * @Author XianChuLun
 * @Date 2020/5/28
 * @Version 1.0
 */
public class CommandFactory {

    private static final Map<String, Command> COMMAND_MAP = new HashMap<>();

    static {
        COMMAND_MAP.put("DIR", wrapCommand(new DirCommand()));
        COMMAND_MAP.put("PWD", wrapCommand(new PwdCommand()));
        COMMAND_MAP.put("CWD", wrapCommand(new CwdCommand()));
        COMMAND_MAP.put("CD", wrapCommand(new CwdCommand()));
        COMMAND_MAP.put("LIST", wrapCommand(new DirCommand()));
        COMMAND_MAP.put("NLST", wrapCommand(new NlistCommand()));
        COMMAND_MAP.put("SIZE", wrapCommand(new SizeCommand()));
        COMMAND_MAP.put("RETR", wrapCommand(new RetrCommand()));
        COMMAND_MAP.put("ABOR", wrapCommand(new AborCommand()));
        COMMAND_MAP.put("STOR", wrapCommand(new StorCommand()));
        COMMAND_MAP.put("MKD", wrapCommand(new MkdCommand() ));
        COMMAND_MAP.put("DELE", wrapCommand(new DeleCommand()));
        COMMAND_MAP.put("RMD", wrapCommand(new RmdCommand()));

        COMMAND_MAP.put("XCWD", wrapCommand(new CwdCommand()));
        COMMAND_MAP.put("XPWD", wrapCommand(new PwdCommand()));
        COMMAND_MAP.put("XRMD", wrapCommand(new RmdCommand()));
        COMMAND_MAP.put("XMKD", wrapCommand(new MkdCommand() ));

        COMMAND_MAP.put("EPRT", wrapCommand(new EprtCommand()));
        COMMAND_MAP.put("EPSV", wrapCommand(new EpsvCommand()));
        COMMAND_MAP.put("OPTS", wrapCommand(new OptsCommand()));
        COMMAND_MAP.put("SYST", wrapCommand(new SystCommand()));
        COMMAND_MAP.put("TYPE", wrapCommand(new TypeCommand()));
        COMMAND_MAP.put("PASV", wrapCommand(new PasvCommand()));
        COMMAND_MAP.put("PORT", wrapCommand(new PortCommand()));
        COMMAND_MAP.put("QUIT", wrapCommand(new QuitCommand()));

        COMMAND_MAP.put("HELP", wrapCommand(new HelpCommand()));

        COMMAND_MAP.put("USER", wrapCommand(new UserCommand()));
        COMMAND_MAP.put("PASS", wrapCommand(new PassCommand()));
    }

    public static Command getInstance(String cmd) {
        if (cmd == null || cmd.isEmpty()) {
            return null;
        }
        return COMMAND_MAP.get(cmd.toUpperCase());

    }

    private static Command wrapCommand(Command command) {
        return command.needAuth() ? new AuthCheckCommand(command) : command;
    }
}

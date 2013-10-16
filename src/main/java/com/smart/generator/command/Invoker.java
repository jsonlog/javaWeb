package com.smart.generator.command;

import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.ObjectUtil;

public class Invoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean execCommand(String... params) {
        return command.exec(params);
    }

    public static void main(String[] args) {
        if (ArrayUtil.isEmpty(args) && args.length < 1) {
            System.err.println("请输入命令参数！");
            return;
        }

        String commandClassName = args[0];
        Command command = ObjectUtil.newInstance(commandClassName);

        Invoker invoker = new Invoker();
        invoker.setCommand(command);

        String[] params = new String[args.length - 1];
        System.arraycopy(args, 1, params, 0, args.length - 1);

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }
}

package com.smart.generator.command;

import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.ObjectUtil;
import com.smart.generator.util.VelocityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Invoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean execCommand(String... params) {
        return command.exec(params);
    }

    public static void main(String[] args) {
        // 验证命令行参数（至少有一个参数，即命令类名）
        if (ArrayUtil.isEmpty(args) && args.length < 1) {
            System.err.println("请输入命令参数！");
            return;
        }

        // 获取命令实例
        String commandClassName = args[0]; // 获取命令类名（第一个命令行参数）
        Command command = ObjectUtil.newInstance(commandClassName);

        // 设置命令
        Invoker invoker = new Invoker();
        invoker.setCommand(command);

        // 初始化参数（从第二个命令行参数开始）
        List<String> paramList = new ArrayList<String>();
        paramList.addAll(Arrays.asList(args).subList(1, args.length));
        String[] params = paramList.toArray(new String[paramList.size()]);

        // 执行命令
        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }
}

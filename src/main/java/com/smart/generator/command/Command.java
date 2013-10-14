package com.smart.generator.command;

public interface Command {

    /**
     * 执行
     *
     * @param params 命令参数（零个或多个）
     * @return 是否成功
     */
    boolean exec(String... params);

    /**
     * 撤销
     */
    void undo();
}

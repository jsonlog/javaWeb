package com.smart.generator.command;

public class Invoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean runCommand(String... params) {
        return command.execute(params);
    }

    public void undoCommand() {
        command.revoke();
    }
}

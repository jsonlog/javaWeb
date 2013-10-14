package com.smart.generator.command;

public class Client {

    public static void main(String[] args) {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateAppCommand()); // smart create-app

        String[] params = {
            "C:\\Smart",        // Current Path
            "demo",             // App Name
            "com.smart",        // App Group
            "demo",             // App Artifact = <App Name>
            "com.smart.demo"    // App Package = <App Group> + <App Artifact>
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            invoker.undoCommand();
        }
    }
}

package com.smart.generator.test;

import com.smart.generator.command.CreateActionCommand;
import com.smart.generator.command.CreateAppCommand;
import com.smart.generator.command.CreateEntityCommand;
import com.smart.generator.command.CreateServiceCommand;
import com.smart.generator.command.Invoker;
import org.junit.Test;

public class CommandTest {

    @Test
    public void createAppTest() {
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
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createEntityTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateEntityCommand()); // smart create-entity

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Entity Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createServiceTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateServiceCommand()); // smart create-service

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Service Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createActionTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateActionCommand()); // smart create-action

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Action Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }
}

package com.smart.generator.test;

import com.smart.generator.command.impl.CreateActionCommand;
import com.smart.generator.command.impl.CreateAppCommand;
import com.smart.generator.command.impl.CreateCRUDCommand;
import com.smart.generator.command.impl.CreateEntityCommand;
import com.smart.generator.command.impl.CreatePageCommand;
import com.smart.generator.command.impl.CreateServiceCommand;
import com.smart.generator.command.Invoker;
import com.smart.generator.command.impl.LoadDictCommand;
import org.junit.Test;

public class CommandTest {

    @Test
    public void createAppCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateAppCommand()); // smart create-app

        String[] params = {
            "C:\\Smart",        // Current Path
            "demo",             // App Name
            "com.smart",        // App Group
            "com.smart.demo"    // App Package = <App Group> + <App Artifact>
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createEntityCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateEntityCommand()); // smart create-entity <entity-name>

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
    public void createServiceCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateServiceCommand()); // smart create-service <service-name>

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
    public void createActionCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateActionCommand()); // smart create-action <action-name>

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Action Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createPageCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreatePageCommand()); // smart create-page <page-name>

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Page Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void createCRUDCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateCRUDCommand()); // smart create-crud <crud-name>

        String[] params = {
            "C:\\Smart\\demo",  // Current Path
            "product",          // Page Name
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    public void loadDictCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new LoadDictCommand()); // smart load-dict <dict-path>

        String[] params = {
            "C:\\Smart\\demo",      // Current Path
            "C:\\Smart\\db.xls",    // Dict Path
        };

        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }
}

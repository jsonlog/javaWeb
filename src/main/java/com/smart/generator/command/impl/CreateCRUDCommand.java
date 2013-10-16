package com.smart.generator.command.impl;

import com.smart.generator.command.Command;

public class CreateCRUDCommand extends Command {

    private String appPath;
    private String crudName;

    @Override
    public int getParamsLength() {
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        appPath = params[0];
        crudName = params[1];
    }

    @Override
    public void generateFiles() {
        generateEntity();
        generateService();
        generateAction();
        generatePage();
    }

    private void generateEntity() {
        Command command = new CreateEntityCommand();
        command.initVariables(appPath, crudName);
        command.generateFiles();
    }

    private void generateService() {
        Command command = new CreateServiceCommand();
        command.initVariables(appPath, crudName);
        command.generateFiles();
    }

    private void generateAction() {
        Command command = new CreateActionCommand();
        command.initVariables(appPath, crudName);
        command.generateFiles();
    }

    private void generatePage() {
        String[] pageNames = {
            crudName,
            crudName + "-create",
            crudName + "-edit",
            crudName + "-view",
        };
        Command command;
        for (String pageName : pageNames) {
            command = new CreatePageCommand();
            command.initVariables(appPath, pageName);
            command.generateFiles();
        }
    }
}

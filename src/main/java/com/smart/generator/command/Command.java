package com.smart.generator.command;

import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.StringUtil;
import com.smart.framework.util.VelocityUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public abstract class Command {

    private static final Logger logger = Logger.getLogger(Command.class);

    private static Properties config;

    protected String smartHome;

    public final boolean exec(String... params) {
        boolean result = true;
        try {
            checkParams(params);
            initSmartHome();
            setVmLoaderPath();
            initVariables(params);
            preGenerate();
            generateFiles();
            postGenerate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    private void checkParams(String[] params) {
        if (ArrayUtil.isEmpty(params) || params.length != getParamsLength()) {
            throw new RuntimeException("命令参数有误！");
        }
    }

    private void initSmartHome() {
        smartHome = System.getenv("SMART_HOME");
        if (StringUtil.isEmpty(smartHome)) {
            throw new RuntimeException("请设置环境变量！SMART_HOME");
        }
    }

    private void setVmLoaderPath() {
        String vmLoaderPath = smartHome + "/vm";
        VelocityUtil.setVmLoaderPath(vmLoaderPath);
    }

    public abstract int getParamsLength();

    public abstract void initVariables(String... params);

    public abstract void generateFiles();

    public void preGenerate() {
    }

    public void postGenerate() {
    }

    protected final String getAppName(String appPath) {
        return getConfigProperty(appPath, "app.name");
    }

    protected final String getAppPackage(String appPath) {
        return getConfigProperty(appPath, "app.package");
    }

    private String getConfigProperty(String appPath, String property) {
        String appName;
        try {
            if (config == null) {
                config = new Properties();
                config.load(new FileInputStream(appPath + "/src/main/resources/config.properties"));
            }
            appName = config.getProperty(property);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("无法找到 config.properties 文件！");
        } catch (IOException e) {
            throw new RuntimeException("加载 config.properties 文件出错！");
        }
        return appName;
    }
}

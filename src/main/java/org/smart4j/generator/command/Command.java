package org.smart4j.generator.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.ArrayUtil;

public abstract class Command {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private static Properties props;

    public final boolean exec(String... params) {
        boolean result = true;
        try {
            checkParams(params);
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

    public abstract int getParamsLength();

    public abstract void initVariables(String... params);

    public abstract void generateFiles();

    public void preGenerate() {
    }

    public void postGenerate() {
    }

    protected final String getAppName(String appPath) {
        return getConfigProperty(appPath, "smart.framework.app.name");
    }

    protected final String getAppPackage(String appPath) {
        return getConfigProperty(appPath, "smart.framework.app.base_package");
    }

    private String getConfigProperty(String appPath, String propKey) {
        String propValue;
        try {
            if (props == null) {
                props = new Properties();
                props.load(new FileInputStream(appPath + "/src/main/resources/smart.properties"));
            }
            propValue = props.getProperty(propKey);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("无法找到 smart.properties 文件！");
        } catch (IOException e) {
            throw new RuntimeException("加载 smart.properties 文件出错！");
        }
        return propValue;
    }
}

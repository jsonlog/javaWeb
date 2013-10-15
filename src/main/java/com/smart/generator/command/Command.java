package com.smart.generator.command;

import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.util.VelocityUtil;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public abstract class Command {

    private static final Logger logger = Logger.getLogger(Command.class);

    protected String smartHome;

    public final boolean exec(String... params) {
        boolean result = true;
        try {
            // 检查输入参数
            checkParams(params);

            // 初始化环境变量
            initSmartHome();

            // 设置 VM 加载路径（相对于环境变量）
            setVmLoaderPath();

            // 初始化相关变量
            initVariables(params);

            // 在创建前执行
            preGenerate();

            // 生成文件
            generateFiles();

            // 在创建后执行
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

    protected final String getAppPackage(String appPath) {
        String appPackage;
        try {
            Properties config = new Properties();
            config.load(new FileInputStream(appPath + "/src/main/resources/config.properties"));
            appPackage = config.getProperty("package");
        } catch (Exception e) {
            throw new RuntimeException("无法获取应用包名！");
        }
        return appPackage;
    }

    protected final String getCamelhumpName(String name) {
        return StringUtil.firstToUpper(StringUtil.toCamelhump(getUnderlineName(name)));
    }

    protected final String getUnderlineName(String name) {
        name = name.trim().toLowerCase();
        if (name.contains("-")) {
            name = name.replace("-", "_");
        }
        return name;
    }
}

package com.smart.generator;

import com.smart.framework.Constant;
import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import java.io.FileWriter;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

public class CodeGenerator {

    private static final Logger logger = Logger.getLogger(CodeGenerator.class);

    private static String VM_PATH;

    static {
        initVmPath();
        initVelocity();
    }

    private static void initVmPath() {
        String smartHome = System.getenv("SMART_HOME");
        if (StringUtil.isEmpty(smartHome)) {
            throw new RuntimeException("请设置环境变量！SMART_HOME");
        }
        VM_PATH = smartHome + "/vm";
    }

    private static void initVelocity() {
        Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        Velocity.setProperty(Velocity.INPUT_ENCODING, Constant.DEFAULT_CHARSET);
        Velocity.setProperty(Velocity.OUTPUT_ENCODING, Constant.DEFAULT_CHARSET);
        Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, VM_PATH);
        Velocity.init();
    }

    public static String getVmPath() {
        return VM_PATH;
    }

    public static void generateCode(String vmPath, Map<String, Object> dataMap, String filePath) {
        try {
            FileUtil.createFile(filePath);

            Template template = Velocity.getTemplate(vmPath);
            VelocityContext context = new VelocityContext(dataMap);
            FileWriter writer = new FileWriter(filePath);
            template.merge(context, writer);

            writer.close();
        } catch (Exception e) {
            logger.error("合并模板出错！", e);
            throw new RuntimeException(e);
        }
    }
}
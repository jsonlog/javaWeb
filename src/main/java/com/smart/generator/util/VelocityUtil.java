package com.smart.generator.util;

import com.smart.framework.util.ClassUtil;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtil {

    private static final Logger logger = Logger.getLogger(VelocityUtil.class);

    private static final VelocityEngine engine = new VelocityEngine();

    static {
        engine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, ClassUtil.getClassPath());
        engine.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
    }

    public static String mergeTemplate(String vmPath, Map<String, Object> dataMap) {
        String result;
        try {
            Template template = engine.getTemplate(vmPath);
            VelocityContext context = new VelocityContext(dataMap);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            result = writer.toString();
            writer.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    public static void mergeTemplate(String vmPath, Map<String, Object> dataMap, String filePath) {
        try {
            Template template = engine.getTemplate(vmPath);
            VelocityContext context = new VelocityContext(dataMap);
            FileWriter writer = new FileWriter(filePath);
            template.merge(context, writer);
            writer.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

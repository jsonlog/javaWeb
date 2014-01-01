package com.smart.framework.generator;

import java.util.Map;

public interface TemplateEngine {

    void generateDocument(String templateFilePath, Map<String, Object> dataMap, String documentFilePath);

    String generateString(String templateString, Map<String, Object> dataMap);
}
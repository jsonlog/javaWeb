package org.smart4j.generator.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smart4j.framework.util.StringUtil;
import org.smart4j.generator.CodeGenerator;
import org.smart4j.generator.bean.Column;
import org.smart4j.generator.bean.Field;
import org.smart4j.generator.bean.Table;
import org.smart4j.generator.builder.Builder;

public class EntityBuilder extends Builder {

    private static List<String> keywordList = new ArrayList<String>();
    private static Map<String, String> typeMap = new HashMap<String, String>();

    static {
        keywordList = Arrays.asList(
            "abstract", "assert",
            "boolean", "break", "byte",
            "case", "catch", "char", "class", "continue",
            "default", "do", "double",
            "else", "enum", "extends",
            "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long",
            "native",
            "new",
            "package", "private", "protected", "public",
            "return",
            "strictfp", "short", "static", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "try",
            "void", "volatile",
            "while"
        );

        typeMap.put("char", "String");
        typeMap.put("varchar", "String");
        typeMap.put("text", "String");
        typeMap.put("bigint", "long");
        typeMap.put("int", "int");
        typeMap.put("smallint", "int");
        typeMap.put("tinyint", "int");
    }

    private String packageName;

    public EntityBuilder(String outputPath, Map<Table, List<Column>> tableMap, String packageName) {
        super(outputPath, tableMap);
        this.packageName = packageName;
    }

    @Override
    public void build() {
        for (Map.Entry<Table, List<Column>> entry : tableMap.entrySet()) {
            Table table = entry.getKey();
            String tableName = table.getName();
            String entityNamePascal = StringUtil.toPascalStyle(tableName, "-");
            List<Column> columnList = entry.getValue();
            List<Field> fieldList = transformFieldList(columnList);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("app_package", packageName);
            dataMap.put("entity_name_p", entityNamePascal);
            dataMap.put("field_list", fieldList);
            dataMap.put("SU", new StringUtil());

            String vmPath = "load-dict/entity_java.vm";
            String filePath = outputPath + "/" + entityNamePascal + ".java";
            CodeGenerator.generateCode(vmPath, dataMap, filePath);
        }
    }

    private List<Field> transformFieldList(List<Column> columnList) {
        List<Field> fieldList = new ArrayList<Field>(columnList.size());
        for (Column column : columnList) {
            String fieldName = this.transformFieldName(column.getName());
            String fieldType = this.transformFieldType(column.getType());
            String fieldComment = column.getComment();
            fieldList.add(new Field(fieldName, fieldType, fieldComment));
        }
        return fieldList;
    }

    private String transformFieldName(String columnName) {
        String fieldName;
        if (keywordList.contains(columnName)) {
            fieldName = columnName + "_";
        } else {
            fieldName = columnName;
        }
        return StringUtil.underlineToCamelhump(fieldName);
    }

    private String transformFieldType(String columnType) {
        String fieldType;
        if (typeMap.containsKey(columnType)) {
            fieldType = typeMap.get(columnType);
        } else {
            fieldType = "String";
        }
        return fieldType;
    }
}

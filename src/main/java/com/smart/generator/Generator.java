package com.smart.generator;

import com.smart.framework.util.ClassUtil;
import com.smart.framework.util.FileUtil;
import com.smart.framework.util.StringUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.builder.Builder;
import com.smart.generator.builder.EntityBuilder;
import com.smart.generator.builder.SQLBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.Logger;

public class Generator {

    private static final Logger logger = Logger.getLogger(Generator.class);

    private static final Properties config = FileUtil.loadPropFile("config.properties");

    private static final String TABLE_VM = "vm/table.vm";
    private static final String ENTITY_VM = "vm/entity.vm";

    public static void main(String[] args) throws Exception {
        new Generator().generator();
    }

    public void generator() {
        String inputPath = ClassUtil.getClassPath() + "db.xls";
        String outputPath = config.getProperty("output_path");
        String packageName = config.getProperty("package_name");

        Map<Table, List<Column>> tableMap = createTableMap(inputPath);

        Builder sqlBuilder = new SQLBuilder(outputPath, TABLE_VM, tableMap);
        sqlBuilder.createFile();
        sqlBuilder.generateCode();

        Builder entityBuilder = new EntityBuilder(outputPath, ENTITY_VM, tableMap, packageName);
        entityBuilder.createFile();
        entityBuilder.generateCode();
    }

    private Map<Table, List<Column>> createTableMap(String inputPath) {
        Map<Table, List<Column>> tableMap = new LinkedHashMap<Table, List<Column>>();
        try {
            File file = new File(inputPath);
            Workbook workbook = Workbook.getWorkbook(file);
            for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheet(i);
                String tableName = sheet.getName().toLowerCase();
                String tablePK = "";
                List<Column> columnList = new ArrayList<Column>();
                for (int row = 1; row < sheet.getRows(); row++) {
                    String name = sheet.getCell(0, row).getContents().trim();
                    String type = sheet.getCell(1, row).getContents().trim();
                    String length = sheet.getCell(2, row).getContents().trim();
                    String precision = sheet.getCell(3, row).getContents().trim();
                    String notnull = sheet.getCell(4, row).getContents().trim();
                    String pk = sheet.getCell(5, row).getContents().trim();
                    String comment = sheet.getCell(6, row).getContents().trim();
                    columnList.add(new Column(name, type, length, precision, notnull, pk, comment));
                    if (StringUtil.isNotEmpty(pk)) {
                        tablePK = name;
                    }
                }
                tableMap.put(new Table(tableName, tablePK), columnList);
            }
            workbook.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return tableMap;
    }
}

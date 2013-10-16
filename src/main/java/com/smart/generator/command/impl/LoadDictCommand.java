package com.smart.generator.command.impl;

import com.smart.framework.util.StringUtil;
import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import com.smart.generator.command.impl.builder.Builder;
import com.smart.generator.command.impl.builder.impl.EntityBuilder;
import com.smart.generator.command.impl.builder.impl.SQLBuilder;
import com.smart.generator.command.Command;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

public class LoadDictCommand extends Command {

    private static final Logger logger = Logger.getLogger(LoadDictCommand.class);

    private String appPath;
    private String dictPath;

    @Override
    public int getParamsLength() {
        // 返回参数个数
        return 2;
    }

    @Override
    public void initVariables(String... params) {
        // 获取命令参数
        appPath = params[0];
        dictPath = params[1];
    }

    @Override
    public void generateFiles() {
        // 获取应用信息
        String appName = getAppName(appPath);
        String appPackage = getAppPackage(appPath);

        // 获取 Excel 表格数据
        Map<Table, List<Column>> tableMap = createTableMap(dictPath);

        // 生成 SQL
        generateSQL(tableMap, appName);

        // 生成 Enttiy
        generateEntity(tableMap, appPackage);
    }

    private void generateSQL(Map<Table, List<Column>> tableMap, String appName) {
        // 获取相关数据
        String outputPath = appPath + "/doc";

        // 生成代码
        Builder sqlBuilder = new SQLBuilder(outputPath, tableMap, appName);
        sqlBuilder.build();
    }

    private void generateEntity(Map<Table, List<Column>> tableMap, String appPackage) {
        // 获取相关数据
        String outputPath = appPath + "/src/main/java/" + appPackage.replace('.', '/') + "/entity";
        String packageName = appPackage + ".entity";

        // 生成代码
        Builder entityBuilder = new EntityBuilder(outputPath, tableMap, packageName);
        entityBuilder.build();
    }

    private Map<Table, List<Column>> createTableMap(String inputPath) {
        // 定义一个 Table Map，用于建立表与列的一对多映射关系
        Map<Table, List<Column>> tableMap = new LinkedHashMap<Table, List<Column>>();
        // 定义 Workbook，表示整个 Excel 文件
        Workbook workbook = null;
        try {
            // 获取 Workbook
            workbook = Workbook.getWorkbook(new File(inputPath));
            // 处理 Workbook
            handleWorkbook(workbook, tableMap);
        } catch (IOException e) {
            logger.error("读取 Excel 出错！", e);
            throw new RuntimeException(e);
        } catch (BiffException e) {
            logger.error("解析 Excel 出错！", e);
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            if (workbook != null) {
                workbook.close();
            }
        }
        return tableMap;
    }

    private void handleWorkbook(Workbook workbook, Map<Table, List<Column>> tableMap) {
        // 读取 Workbook（从第二个开始）
        for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
            // 获取 Sheet
            Sheet sheet = workbook.getSheet(i);
            // 处理 Sheet
            handleSheet(sheet, tableMap);
        }
    }

    private void handleSheet(Sheet sheet, Map<Table, List<Column>> tableMap) {
        // 获取表名
        String tableName = sheet.getName().toLowerCase();
        // 定义表主键
        String tablePK = "";
        // 定义一个 Column List，用于封装 Sheet 数据
        List<Column> columnList = new ArrayList<Column>();
        // 读取 Sheet（从第二行开始）
        for (int row = 1; row < sheet.getRows(); row++) {
            // 获取 Cell 数据
            String name = getData(sheet, 0, row);
            String type = getData(sheet, 1, row);
            String length = getData(sheet, 2, row);
            String precision = getData(sheet, 3, row);
            String notnull = getData(sheet, 4, row);
            String pk = getData(sheet, 5, row);
            String comment = getData(sheet, 6, row);
            // 将 Cell 数据封装为 Column，并添加到 Column List 中
            columnList.add(new Column(name, type, length, precision, notnull, pk, comment));
            // 初始化表主键
            if (StringUtil.isNotEmpty(pk)) {
                tablePK = name;
            }
        }
        // 将 Sheet 数据封装为 Table，并添加到 Table Map 中
        tableMap.put(new Table(tableName, tablePK), columnList);
    }

    private String getData(Sheet sheet, int column, int row) {
        // 获取 Cell 数据，去除了前后空格
        return sheet.getCell(column, row).getContents().trim();
    }
}

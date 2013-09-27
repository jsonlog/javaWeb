package com.smart.generator.builder;

import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import java.util.List;
import java.util.Map;

public abstract class Builder {

    protected String outputPath;
    protected String vmPath;
    protected Map<Table, List<Column>> tableMap;

    public Builder(String outputPath, String vmPath, Map<Table, List<Column>> tableMap) {
        this.outputPath = outputPath;
        this.vmPath = vmPath;
        this.tableMap = tableMap;
    }

    public void build() {
        createFile();
        generateCode();
    }

    public abstract void createFile();

    public abstract void generateCode();
}

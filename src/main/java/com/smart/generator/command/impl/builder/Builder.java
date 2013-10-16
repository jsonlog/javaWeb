package com.smart.generator.command.impl.builder;

import com.smart.generator.bean.Column;
import com.smart.generator.bean.Table;
import java.util.List;
import java.util.Map;

public abstract class Builder {

    protected String outputPath;
    protected Map<Table, List<Column>> tableMap;

    public Builder(String outputPath, Map<Table, List<Column>> tableMap) {
        this.outputPath = outputPath;
        this.tableMap = tableMap;
    }

    public abstract void build();
}

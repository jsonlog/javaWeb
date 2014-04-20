package org.smart4j.generator.bean;

public class Column {

    private String name;
    private String type;
    private String length;
    private String precision;
    private String notnull;
    private String pk;
    private String comment;

    public Column(String name, String type, String length, String precision, String notnull, String pk, String comment) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.precision = precision;
        this.notnull = notnull;
        this.pk = pk;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getNotnull() {
        return notnull;
    }

    public void setNotnull(String notnull) {
        this.notnull = notnull;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

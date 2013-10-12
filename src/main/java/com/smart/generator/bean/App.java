package com.smart.generator.bean;

import com.smart.framework.base.BaseBean;

public class App extends BaseBean {

    private String name = "";
    private String group = "";
    private String artifact = name;
    private String pkg = group + "." + artifact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }
}

package org.smart4j.sample.entity;

import java.io.Serializable;
import org.smart4j.framework.orm.annotation.Entity;

@Entity
public class Role implements Serializable {

    private long id;

    private String roleName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

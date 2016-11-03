package com.little.model.domain;

import javax.persistence.*;

/**
 * @author pengx
 * @date 2016/11/3
 */

@Entity
@Table(name = "test", schema = "little", catalog = "")
public class Test {
    @Id
    @Column(name = "id", nullable = true)
    private  int id;

    @Basic
    @Column(name = "name", nullable = true)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

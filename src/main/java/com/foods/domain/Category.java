package com.foods.domain;

import java.io.Serializable;

/**
 * Created by jacob on 2017/4/17.
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 10L;

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

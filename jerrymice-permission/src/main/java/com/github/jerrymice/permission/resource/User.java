package com.github.jerrymice.permission.resource;

import lombok.Data;

/**
 * @author tumingjian
 * @date 2018/9/17
 * 说明:
 */
public class User implements Property {
    protected String code;
    protected String name;
    private Resource resource;
    public User() {
    }

    public User(String code, String name) {
        this.code = code;
        this.name = name;
    }
    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}

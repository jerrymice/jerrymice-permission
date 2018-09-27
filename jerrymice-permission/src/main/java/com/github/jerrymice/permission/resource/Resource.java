package com.github.jerrymice.permission.resource;

/**
 * @author tumingjian
 * 说明:用户资源实体
 */
public class Resource implements Property{
    protected String code;
    protected String name;
    protected Integer type=0;

    public Resource() {
    }

    public Resource(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Resource(String code, String name, Integer type) {
        this.code = code;
        this.name = name;
        this.type = type;
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

    public void setType(Integer type) {
        this.type = type;
    }
}

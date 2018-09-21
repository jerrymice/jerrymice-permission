package com.github.jerrymice.permission.resource;

/**
 * @author tumingjian
 * @date 2018/9/17
 * 说明:
 */
public class Resource implements Property,Classify{
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

    @Override
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

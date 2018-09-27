package com.github.jerrymice.permission.resource;

/**
 * @author tumingjian
 * 说明: 当前角色信息实现类
 */
public class Character implements Property {
    protected String code;
    protected String name;

    public Character() {
    }

    public Character(String code, String name) {
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
}

package com.github.jerrymice.permission.resource;

/**
 * @author tumingjian
 * @date 2018/9/17
 * 说明:
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

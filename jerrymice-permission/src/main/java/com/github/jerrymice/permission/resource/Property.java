package com.github.jerrymice.permission.resource;

import java.io.Serializable;

/**
 * @author tumingjian
 * 说明:用户权限实体抽象接口
 */
public interface Property extends Serializable{
    /**
     * 获取当前权限实体的代码,这个代码至少必须要需要保证同一类权限实体信息中唯一
     * @return String
     */
    String getCode();

    /**
     * 获取当前权限实体的名称
     * @return String
     */
    String getName();
}

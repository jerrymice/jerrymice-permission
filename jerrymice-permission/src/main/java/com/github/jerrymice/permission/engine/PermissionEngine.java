package com.github.jerrymice.permission.engine;

import com.github.jerrymice.permission.resource.Property;

import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * 说明:权限脚本引擎接口类.
 */
public interface PermissionEngine {
    String EXTEND_VARIABLE_NAME="E";
    String PERMISSION_VARIABLE_NAME ="P";
    /**
     * 返回当前用户
     *
     * @return
     */
    Set<Property> user();

    /**
     * 返回当前用户所拥有的资源
     *
     * @return
     */
    Set<Property> resources();

    /**
     * 返回当前用户所拥有的角色信息
     *
     * @return
     */
    Set<Property> characters();

    /**
     * 返回当前用户添加的额外数据变量map
     *
     * @return
     */
    Map<String, Object> extendData();

    /**
     * 判断资源中是否存在某个资源值.
     *
     * @param value 资源在Engine中的唯一值
     * @return 返回true 或 false
     */
    boolean contain(String value);

    /**
     * 在js中执行一个bool表达式,js返回值如果返回:null,'null',false,0,undefined 字符串 那么java将返回false
     *
     * @param expression 要执行的js表达式
     * @return 返回true 或 false
     */
    boolean bool(String expression);

    /**
     * 执行表达式并返回结果
     * @param expression 表达式
     * @return 返回值
     */
    Object eval(String expression);
    void put(String name,Object value);
    /**
     * 验证表达式类型.
     *
     * @param value
     * @return
     */
    default int type(String value) {
        final String bracket="[";
        final String dot=".";
        if (value == null || value.trim() == "") {
            return 0;
        }
        if (value.equalsIgnoreCase(Boolean.TRUE.toString()) || value.equalsIgnoreCase(Boolean.FALSE.toString())) {
            throw new PermissionException(new IllegalArgumentException("permission eval 'true' is retention key word"));
        }
        if (value.contains(PERMISSION_VARIABLE_NAME +bracket) || value.contains(EXTEND_VARIABLE_NAME+bracket) || value.contains(PERMISSION_VARIABLE_NAME +dot) || value.contains(EXTEND_VARIABLE_NAME+dot)) {
            return 1;
        }
        return 0;
    }

    /**
     * 释放引擎
     */
    default void release(){

    }
}

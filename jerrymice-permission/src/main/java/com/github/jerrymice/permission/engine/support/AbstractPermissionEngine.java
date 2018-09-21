package com.github.jerrymice.permission.engine.support;

import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.PermissionException;
import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.resource.Property;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
public abstract class AbstractPermissionEngine implements PermissionEngine {
    protected PermissionLoader permissionLoader;
    protected boolean mixtureSearch = false;
    protected final String USER_VARIABLE_NAME = "U";
    protected final String CHARACTERS_VARIABLE_NAME = "C";
    protected final String RESOURCES_VARIABLE_NAME = "R";
    private final String JAVA_SCRIPT_UNDEFINED="undefined";
    protected ScriptEngine engine;
    private Map<String, Map<String, Property>> commonVariable = new HashMap<>();
    private Map<String, Object> extendData;

    public AbstractPermissionEngine(PermissionLoader permissionLoader, boolean mixtureSearch) {
        this.mixtureSearch = mixtureSearch;
        this.permissionLoader = permissionLoader;
        initCommonVariable();
    }

    protected void initCommonVariable() {
        HashSet<Property> users = new HashSet<>();
        Property user;
        if ((user = this.permissionLoader.loadUser()) != null) {
            users.add(user);
        }
        Map<String, Property> userMap = converterCode(users);
        commonVariable.put(USER_VARIABLE_NAME, userMap);
        Map<String, Property> characterMap = converterCode(this.permissionLoader.loadCharacters());
        commonVariable.put(CHARACTERS_VARIABLE_NAME, characterMap);
        Map<String, Property> resourceMap = converterCode(this.permissionLoader.loadResources());
        commonVariable.put(RESOURCES_VARIABLE_NAME, resourceMap);
        extendData=this.permissionLoader.loadExtendData();
    }

    /**
     * 延迟初始化脚本引擎
     */
    private void lazyInitScriptEngine() {
        if (engine == null) {
            this.engine = initScriptEngine();
            initScriptVariable();
        }
    }

    /**
     * 初化始脚本引擎
     *
     * @return 返回一个初始完成后的脚本引擎
     */
    protected abstract ScriptEngine initScriptEngine();

    /**
     * 初始化脚本引擎的变量
     */
    private void initScriptVariable() {
        engine.put(PERMISSION_VARIABLE_NAME, commonVariable);
        if (extendData != null) {
            engine.put(EXTEND_VARIABLE_NAME, extendData);
        } else {
            engine.put(EXTEND_VARIABLE_NAME, new HashMap<String, Object>(0));
        }
    }


    private Map<String, Property> converterCode(Set<Property> list) {
        Map<String, Property> collect = Optional.ofNullable(list).orElseGet(HashSet::new).stream()
                .collect(Collectors.toMap(i -> i.getCode(), i -> i));
        return collect;
    }

    @Override
    public Set<Property> user() {
        return commonVariable.get(USER_VARIABLE_NAME).values().stream().collect(Collectors.toSet());
    }

    @Override
    public Set<Property> resources() {
        return commonVariable.get(RESOURCES_VARIABLE_NAME).values().stream().collect(Collectors.toSet());
    }

    @Override
    public Set<Property> characters() {
        return commonVariable.get(CHARACTERS_VARIABLE_NAME).values().stream().collect(Collectors.toSet());

    }

    @Override
    public Map<String, Object> extendData() {
        return extendData;
    }

    @Override
    public boolean contain(String value) {
        if (mixtureSearch) {
            return commonVariable.get(RESOURCES_VARIABLE_NAME).get(value) != null
                    || commonVariable.get(USER_VARIABLE_NAME).get(value) != null
                    || commonVariable.get(CHARACTERS_VARIABLE_NAME).get(value) != null;
        } else {
            return commonVariable.get(RESOURCES_VARIABLE_NAME).get(value) != null;
        }
    }

    @Override
    public Object eval(String expression) {
        try {
            lazyInitScriptEngine();
            return this.engine.eval(expression);
        } catch (ScriptException e) {
            throw new PermissionException(e);
        }
    }

    @Override
    public boolean bool(String expression) {
        try {
            lazyInitScriptEngine();
            Object eval = engine.eval(expression);
            if (eval == null || eval.equals(JAVA_SCRIPT_UNDEFINED)) {
                return false;
            } else {
                if (eval.toString().equalsIgnoreCase(Boolean.FALSE.toString()) || eval.toString().equals("0")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (ScriptException e) {
            throw new PermissionException(e);
        }
    }

    @Override
    public void put(String name, Object value) {
        lazyInitScriptEngine();
        this.engine.put(name,value);
    }
}

package com.github.jerrymice.permission.engine.support;

import com.github.jerrymice.permission.engine.PermissionEngine;
import com.github.jerrymice.permission.engine.PermissionException;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.resource.Property;
import org.springframework.core.io.Resource;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tumingjian
 * 说明:permission抽象类.用于eval,contain等方法
 */
public abstract class AbstractPermissionEngine implements PermissionEngine {
    /**
     * 用户权限相关的数据载入
     */
    protected PermissionService permissionService;
    /**
     * 是否启用全局搜索
     */
    protected boolean mixtureSearch = false;
    /**
     * 当前用户信息在JS中的变量名
     */
    protected final String USER_VARIABLE_NAME = "U";
    /**
     * 当前角色列表在JS中的变量名
     */
    protected final String CHARACTERS_VARIABLE_NAME = "C";
    /**
     * 当前用户资源在JS中的变量名
     */
    protected final String RESOURCES_VARIABLE_NAME = "R";
    /**
     * JS中未定义变量的值
     */
    private final String JAVA_SCRIPT_UNDEFINED = "undefined";
    /**
     * 执行脚本引擎
     */
    protected ScriptEngine engine;
    /**
     * 通用的数据,主要存储当前用户,角色列表,资源列表信息
     */
    private Map<String, Map<String, Property>> commonVariable = new HashMap<>();
    /**
     * 扩展数据.
     */
    private Map<String, Object> extendData;
    /**
     * 扩展脚本文件
     */
    private List<Resource> extendScriptFile;

    public AbstractPermissionEngine(PermissionService permissionService, boolean mixtureSearch,List<Resource> extendScriptFile) {
        this.mixtureSearch = mixtureSearch;
        this.permissionService = permissionService;
        this.extendScriptFile=extendScriptFile;
        initCommonVariable();
    }

    /**
     * 初始用户权限数据
     */
    protected void initCommonVariable() {
        HashSet<Property> users = new HashSet<>();
        Property user;
        if ((user = this.permissionService.loadUser()) != null) {
            users.add(user);
        }
        Map<String, Property> userMap = converterCode(users);
        commonVariable.put(USER_VARIABLE_NAME, userMap);
        Map<String, Property> characterMap = converterCode(this.permissionService.loadCharacters());
        commonVariable.put(CHARACTERS_VARIABLE_NAME, characterMap);
        Map<String, Property> resourceMap = converterCode(this.permissionService.loadResources());
        commonVariable.put(RESOURCES_VARIABLE_NAME, resourceMap);
        extendData = this.permissionService.loadExtendData();
    }

    /**
     * 延迟初始化脚本引擎
     */
    private void lazyInitScriptEngine() {
        if (engine == null) {
            this.engine = initScriptEngine();
            try{
                if(extendScriptFile!=null){
                    for(Resource resource:extendScriptFile){
                        this.engine.eval(new InputStreamReader(resource.getInputStream()));
                    }
                }
            }catch (Exception e){
                throw new PermissionException(e);
            }
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

    /**
     * 将set 转换为 以Property.code为Key,Property为value的map
     * @param list
     * @return
     */
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
        this.engine.put(name, value);
    }

    public void setExtendScriptFile(List<Resource> extendScriptFile) {
        this.extendScriptFile = extendScriptFile;
    }

    public List<Resource> getsetExtendScriptFile() {
        return extendScriptFile;
    }
}

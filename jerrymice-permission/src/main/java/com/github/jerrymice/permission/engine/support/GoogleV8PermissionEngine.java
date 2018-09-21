package com.github.jerrymice.permission.engine.support;

import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.engine.GoogleV8ScriptEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author tumingjian
 * @date 2018/9/19
 * 说明:
 */
public class GoogleV8PermissionEngine extends AbstractPermissionEngine {
    public GoogleV8PermissionEngine(PermissionLoader permissionLoader) {
        this(permissionLoader, false);
    }

    public GoogleV8PermissionEngine(PermissionLoader permissionLoader, boolean mixtureSearch) {
        super(permissionLoader, mixtureSearch);
    }

    @Override
    protected ScriptEngine initScriptEngine() {
        return new GoogleV8ScriptEngine();
    }
}

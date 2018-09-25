package com.github.jerrymice.permission.engine.support;

import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.engine.GoogleV8ScriptEngine;

import javax.script.ScriptEngine;

/**
 * @author tumingjian
 * @date 2018/9/19
 * 说明:
 */
public class GoogleV8PermissionEngine extends AbstractPermissionEngine {
    private GoogleV8ScriptEngine googleV8ScriptEngine;

    public GoogleV8PermissionEngine(PermissionService permissionLoader) {
        this(permissionLoader, false);
    }

    public GoogleV8PermissionEngine(PermissionService permissionLoader, boolean mixtureSearch) {
        super(permissionLoader, mixtureSearch);
    }

    @Override
    protected ScriptEngine initScriptEngine() {
        this.googleV8ScriptEngine = new GoogleV8ScriptEngine();
        return this.googleV8ScriptEngine;
    }

    @Override
    public void release() {
        this.googleV8ScriptEngine.release();
    }
}

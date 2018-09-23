package com.github.jerrymice.permission.engine;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ScriptExecutionException;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.script.*;
import java.io.*;
import java.net.URL;

/**
 * @author tumingjian
 * @date 2018/9/19
 * 说明:
 */
@Slf4j
public class GoogleV8ScriptEngine extends AbstractScriptEngine {
    private V8 engine;
    private boolean disabledUndefinedException = true;
    final private String FILE_NAME = "v8.js";

    public GoogleV8ScriptEngine() {
        this(true);
    }

    public GoogleV8ScriptEngine(boolean disabledUndefinedException) {
        this.disabledUndefinedException = disabledUndefinedException;
        this.engine = V8.createV8Runtime();
        try {
            URL resource = GoogleV8ScriptEngine.class.getResource(FILE_NAME);
            String s = IOUtils.toString(resource, "UTF-8");
            this.engine.executeVoidScript(s);
        } catch (IOException e) {

        }
    }


    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        if (context != null && context.getBindings(ScriptContext.ENGINE_SCOPE).size() == 0) {
            context.setAttribute("context_scope", ScriptContext.ENGINE_SCOPE, ScriptContext.ENGINE_SCOPE);
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            bindings.forEach((k, v) -> put(k, v));
        }
        try {
            Object object = this.engine.executeScript(script);
            return getAndReleaseV8Object(object);
        } catch (V8ScriptExecutionException e) {
            if (e.getMessage().contains("TypeError: Cannot read property") && disabledUndefinedException) {
                log.warn("Google V8 engine running encountered 1 errors during,but not throw this exception," +
                        "eval function only return null eval," +
                        "because your disabledUndefinedException is true,warning:" + e.getMessage());
                return null;
            } else {
                throw e;
            }
        }

    }


    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return eval(readerToString(reader), context);
    }

    /**
     * Read from a reader into a local buffer and return a String with
     * the contents of the reader.
     *
     * @param scriptReader to be read.
     * @return the contents of the reader as a String.
     * @throws ScriptException on any error reading the reader.
     */
    private static String readerToString(Reader scriptReader) throws ScriptException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader;
        if (scriptReader instanceof BufferedReader) {
            reader = (BufferedReader) scriptReader;
        } else {
            reader = new BufferedReader(scriptReader);
        }
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }

    @Override
    public void put(String key, Object value) {
        ObjectMapper build = new Jackson2ObjectMapperBuilder().build();
        try {
            String s = build.writer().writeValueAsString(value);
            if (key.indexOf("P") == 0 || key.indexOf("E") == 0) {
                this.engine.executeVoidScript("var " + key + "=" + s);
            } else {
                this.engine.executeVoidScript("var " + key + "=" + s);
            }
        } catch (Exception e) {
            throw new PermissionException(e);
        }
    }

    @Override
    public Object get(String key) {
        Object object = this.engine.get(key);
        return getAndReleaseV8Object(object);
    }

    private Object getAndReleaseV8Object(Object object) {
        Object result = object;
        if (object instanceof V8Object) {
            V8Object v8Object = (V8Object) object;
            if (!v8Object.isUndefined()) {
                if (v8Object instanceof V8Array) {
                    result = V8ObjectUtils.toList((V8Array) v8Object);
                } else if (v8Object instanceof V8Object) {
                    result = V8ObjectUtils.toMap(v8Object);
                } else {
                    result = v8Object;
                }
            }
            v8Object.release();
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.engine != null && !this.engine.isReleased()) {
            this.engine.release();
        }
        super.finalize();
    }

    public void release() {
        if (this.engine.isReleased()) {
            this.engine.release();
        }
    }
}

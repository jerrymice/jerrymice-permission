package com.github.jerrymice.permission.listener;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author tumingjian
 * 说明: 一个将当前request 存入到threadlocal中的类
 */
public class PermissionEngineRequestContextHolder {
    private static final ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<>();

    private PermissionEngineRequestContextHolder() {
    }

    public static HttpSession getSession() {
        HttpServletRequest request = currentRequest.get();
        if (request != null) {
            return request.getSession(true);
        } else {
            return null;
        }
    }

    public static HttpServletRequest getRequest() {
        return currentRequest.get();
    }

    static void setRequest(HttpServletRequest request) {
        currentRequest.set(request);
    }
}

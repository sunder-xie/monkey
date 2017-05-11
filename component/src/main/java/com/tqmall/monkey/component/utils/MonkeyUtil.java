package com.tqmall.monkey.component.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by huangzhangting on 16/11/23.
 */
public class MonkeyUtil {
    public static ServletContext getServletContext(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        return webApplicationContext.getServletContext();
    }
    public static String getRealPath(String path){
        ServletContext servletContext = getServletContext();
        return servletContext.getRealPath(path);
    }
}

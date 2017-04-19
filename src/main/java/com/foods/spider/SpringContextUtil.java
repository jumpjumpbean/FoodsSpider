package com.foods.spider;

import org.springframework.context.ApplicationContext;

/**
 * Created by jacob on 2017/4/19.
 */
public class SpringContextUtil {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}

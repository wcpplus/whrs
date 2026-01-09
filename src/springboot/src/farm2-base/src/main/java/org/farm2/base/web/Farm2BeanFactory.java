package org.farm2.base.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 获得spring中的bean
 */
public class Farm2BeanFactory {
    private static ApplicationContext appContext = null;

    public static void setApplicationContext(ApplicationContext context) {
        appContext = context;
    }

    public static Object getBean(Class beanClass) {
        return appContext.getBean(beanClass);
    }

}

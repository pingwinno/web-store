package com.study.store.web.listener;

import com.study.ApplicationContext;
import com.study.ClassPathApplicationContext;
import com.study.reader.XmlBeanDefinitionReader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class InitListener implements ServletContextListener {
    public static final String APPLICATION_CONTEXT = "applicationContext";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext applicationContext = new ClassPathApplicationContext(new XmlBeanDefinitionReader());
        servletContextEvent.getServletContext()
                           .setAttribute(APPLICATION_CONTEXT, applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

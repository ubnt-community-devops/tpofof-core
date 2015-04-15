package com.tpofof.core;

import java.io.File;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class App {

	private static final CompositeConfiguration config = new CompositeConfiguration();
	private static ApplicationContext context;
	
	static {
		config.addConfiguration(new SystemConfiguration());
			File propFile = new File("app.properties");
			if (propFile.exists()) {
				try {
					config.addConfiguration(new PropertiesConfiguration("app.properties"));
				} catch (ConfigurationException e) {
					throw new ExceptionInInitializerError(e);
				}
			} else {
				System.err.println("WARNING: app.properties file does not extist!");
			}
	}
	
	public static ApplicationContext getContext() {
		if (context == null) {
			String beanPathFromConfig = config.getString("spring.config.path", null);
			if (beanPathFromConfig == null) {
				context = new AnnotationConfigApplicationContext("com.tpofof");
			} else {
				context = new AnnotationConfigApplicationContext("com.tpofof", beanPathFromConfig);
			}
		}
		
		return context;
	}
}

package com.tpofof.core.utils;

import java.io.File;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("config")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class Config extends CompositeConfiguration {

	public Config() {
		this("app.properties");
	}
	
	public Config(String...configFileNames) {
		this.addConfiguration(new SystemConfiguration());
		try {
			for (String fileName : configFileNames) {
				if (new File(fileName).exists()) {
					this.addConfiguration(new PropertiesConfiguration(fileName));
				} else {
					System.err.println("WARNING: could not load config with path " + fileName);
				}
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}

package com.tpofof.core.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.tpofof.core.utils.Config;

@Configuration
public class DataBeanConfiguration {

	@Autowired private Config config;
	
	@Bean(name="dataSource")
	@Scope("singleton")
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource(
				config.getString("db.url", "jdbc:mysql://localhost:3600/hibernate_test"),
				config.getString("db.username", "root"),
				config.getString("db.password", ""));
		ds.setDriverClassName(config.getString("db.driver", "com.mysql.jdbc.Driver"));
		return ds;
	}
}

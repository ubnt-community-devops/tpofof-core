package com.tpofof.core.utils.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("objectMapperProvider")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ObjectMapperProvider {

	@Autowired ObjectMapperDecorator decorator;
	
	public ObjectMapper get() {
		ObjectMapper objectMapper = new ObjectMapper();
		decorator.decorate(objectMapper);
		return objectMapper;
	}
}

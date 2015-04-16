package com.tpofof.core.utils.json;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpofof.core.App;

@Component("objectMapperDecorator")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ObjectMapperDecorator {

	public void decorate(ObjectMapper mapper) {
		Map<String, Module> beans = App.getContext().getBeansOfType(Module.class);
		for (Entry<String, Module> e : beans.entrySet()) {
			mapper.registerModule(e.getValue());
		}
	}
}

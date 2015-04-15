package com.tpofof.core.utils.json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

@Component("jsonUtils")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class JsonUtils {

	private ObjectMapper mapper;
	@Autowired ObjectMapperProvider objectMapperProvider;
	
	// Lazy created because of Spring bullshit
	private ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = objectMapperProvider.get();
		}
		return mapper;
	}
	
	public <ModelT> ModelT fromJson(File file, Class<ModelT> modelType) {
		if (file == null) {
			throw new IllegalArgumentException("Json file cannot be null.");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("Json file must exist.");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("Model Class cannot be null");
		}
		try {
			return getMapper().readValue(file, modelType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JsonNode fromJson(File file) {
		if (file == null) {
			throw new IllegalArgumentException("Json file cannot be null.");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("Json file must exist.");
		}
		try {
			return getMapper().readValue(file, JsonNode.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <ModelT> ModelT fromJson(String json, Class<ModelT> modelType) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("Model Class cannot be null");
		}
		try {
			return getMapper().readValue(json, modelType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <ModelT> ModelT fromJsonResponse(String json, Class<ModelT> modelType) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("Model Class cannot be null");
		}
		try {
			JsonNode node = getMapper().readTree(json);
			if (node.has("success") && node.get("success").asBoolean()) {
				if (node.get("data").get("type").asText().equals("model")) {
					return getMapper().convertValue(node.get("data").get("model"), modelType);
				} // throw away collection values?! Throw exception?
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <ModelT> List<ModelT> fromJsonList(String json, Class<ModelT> modelType) {
		List<ModelT> models = Lists.newArrayList();
		try {
			JsonNode arrayNode = getMapper().readTree(json);
			for (JsonNode modelNode : arrayNode) {
				models.add(getMapper().convertValue(modelNode, modelType));
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return models;
	}
	
	public <ModelT> List<ModelT> fromJsonListResponse(String json, Class<ModelT> modelType) {
		List<ModelT> models = Lists.newArrayList();
		try {
			JsonNode node = getMapper().readTree(json);
			if (node.has("success") && node.get("success").asBoolean()) {
				if (node.get("data").get("type").asText().equals("collection")) {
					JsonNode arrayNode = getMapper().readTree(node.get("data").get("collection").toString());
					for (JsonNode modelNode : arrayNode) {
						models.add(getMapper().convertValue(modelNode, modelType));
					}
				} // throw away model values?! Throw exception?
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return models;
	}
	
	public String toJson(Object model) {
		if (model == null) {
			throw new IllegalArgumentException("Model cannot be null.");
		}
		try {
			return getMapper().writeValueAsString(model);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JsonNode toJsonNode(Object model) {
		String jsonContent = toJson(model);
		if (jsonContent != null) {
			try {
				return getMapper().readTree(jsonContent);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  null;
	}
	
	public JsonNode parse(String json) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		try {
			return getMapper().readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ObjectNode parseObject(String json) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		try {
			JsonNode node = getMapper().readTree(json);
			return node.isArray() ? null : (ObjectNode)node;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ObjectNode getObjectNode() {
		return getMapper().createObjectNode();
	}
}

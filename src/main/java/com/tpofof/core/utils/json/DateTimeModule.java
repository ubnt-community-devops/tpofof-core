package com.tpofof.core.utils.json;

import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component("dateTimeModule")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@JsonModule
public class DateTimeModule extends SimpleModule {

	private static final long serialVersionUID = 1L;
	
	public DateTimeModule() {
		addDeserializer(DateTime.class, new DateTimeDeserializer());
		addSerializer(DateTime.class, new DateTimeSerializer());
	}

	public class DateTimeSerializer extends JsonSerializer<DateTime> {
		@Override
		public void serialize(DateTime date, JsonGenerator jgen,
				SerializerProvider serializerProvider) throws IOException,
				JsonProcessingException {
			if (date == null) {
				jgen.writeNull();
			} else {
				jgen.writeString(date.toString());
			}
		}
	}
	
	public class DateTimeDeserializer extends JsonDeserializer<DateTime> {
		@Override
		public DateTime deserialize(JsonParser jp, DeserializationContext context)
				throws IOException, JsonProcessingException {
			JsonNode node = jp.getCodec().readTree(jp);
			return new DateTime(node.asText());
		}
	}
}

package com.kanuhasu.ap.business.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Used to serialize Java.util.Date, which is not a common JSON
 * type, so we have to create a custom serialize method;.
 * 
 * @author hpanchal
 */
@Component
public class JsonDateTimeSerializer extends JsonSerializer<Date> {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
	}
}
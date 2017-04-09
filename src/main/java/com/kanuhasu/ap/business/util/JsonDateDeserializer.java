package com.kanuhasu.ap.business.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserializer extends JsonDeserializer<Date> {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
		String date = jsonparser.getText();
		try {
			return dateFormat.parse(date);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(dateFormat.parse("2017-03-10 12:52:04"));
	}
}
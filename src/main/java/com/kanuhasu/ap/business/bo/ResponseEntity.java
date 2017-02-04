package com.kanuhasu.ap.business.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kanuhasu.ap.business.type.response.Param;

public class ResponseEntity implements Serializable {
	private static final long serialVersionUID = 1012695220974239571L;
	
	private Map<String, String> responseData;
	private Object responseEntity;

	public ResponseEntity() {
	}
	
	public ResponseEntity(Builder builder) {
		this.responseData = builder.responseData;
		this.responseEntity = builder.responseEntity;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Map<String, String> responseData;
		private Object responseEntity;
		
		public Builder responseData(Map<String, String> responseData) {
			this.responseData = responseData;
			return this;
		}
		
		public Builder responseEntity(Object responseEntity) {
			this.responseEntity = responseEntity;
			return this;
		}
		
		public ResponseEntity build() {
			return new ResponseEntity(this);
		}
	}
	
	public static ResponseEntity Success(){
		ResponseEntity successResp= ResponseEntity.builder().responseData(new HashMap<String,String>()).build();
		successResp.getResponseData().put(Param.SUCCESS.name(), Boolean.TRUE.toString());
		return successResp;
	}

	public static ResponseEntity Fail(){
		ResponseEntity successResp= ResponseEntity.builder().responseData(new HashMap<String,String>()).build();
		successResp.getResponseData().put(Param.SUCCESS.name(), Boolean.FALSE.toString());
		return successResp;
	}
	
	public Map<String, String> getResponseData() {
		return responseData;
	}
	
	public void setResponseData(Map<String, String> responseData) {
		this.responseData = responseData;
	}
	
	public Object getResponseEntity() {
		return responseEntity;
	}
	
	public void setResponseEntity(Object responseEntity) {
		this.responseEntity = responseEntity;
	}
}

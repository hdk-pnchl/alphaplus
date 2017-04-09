package com.kanuhasu.ap.business.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kanuhasu.ap.business.type.response.Param;

public class Response implements Serializable {
	private static final long serialVersionUID = 1012695220974239571L;
	
	private Map<String, String> responseData;
	private Object responseEntity;
	private List<Alert> alertData;
	public Response() {
	}
	
	public Response(Builder builder) {
		this.responseData = builder.responseData;
		this.responseEntity = builder.responseEntity;
		this.alertData = builder.alertData;
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
		private List<Alert> alertData;

		public Builder responseData(Map<String, String> responseData) {
			this.responseData = responseData;
			return this;
		}

		public Builder alertData(List<Alert> alertData) {
			this.alertData = alertData;
			return this;
		}
		
		public Builder responseEntity(Object responseEntity) {
			this.responseEntity = responseEntity;
			return this;
		}
		
		public Response build() {
			return new Response(this);
		}
	}
	
	public static Response Success(){
		Response successResp= Response.builder()
				.responseData(new HashMap<String,String>())
				.build();
		successResp.getResponseData().put(Param.ERROR.name(), Boolean.FALSE.toString());
		return successResp;
	}

	public static Response Fail(){
		Response successResp= Response.builder()
				.responseData(new HashMap<String,String>())
				.build();
		successResp.getResponseData().put(Param.ERROR.name(), Boolean.TRUE.toString());
		return successResp;
	}
	
	public Response putParam(String key, String value){
		if(this.getResponseData()==null){
			this.setResponseData(new HashMap<String,String>());
		}
		this.getResponseData().put(key, value);
		return this;
	}
	public String getParam(String key){
		if(this.getResponseData()==null){
			return null;
		}
		return this.getResponseData().get(key);
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

	public List<Alert> getAlertData() {
		return alertData;
	}
	
	public void addAlert(Alert alert){
		if(this.alertData==null){
			this.alertData= new ArrayList<Alert>();
		}
		this.alertData.add(alert);
	}
}

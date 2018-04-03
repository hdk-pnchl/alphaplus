package com.kanuhasu.ap.business.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.out.recon.bo.RErrorType;

public class Response implements Serializable {
	private static final long serialVersionUID = 1012695220974239571L;
	
	/** ------------| instance |------------**/
	
	private Map<String, Object> responseData= new HashMap<String,Object>();
	private Object responseEntity;
	private List<Alert> alertData;
	
	public Response() {
	}

	public Response(Object responseEntity) {
		this.setResponseEntity(responseEntity);
	}
	
	/** ------------| Constructor |------------**/
	
	public Response(Builder builder) {
		this.responseData = builder.responseData;
		this.responseEntity = builder.responseEntity;
		this.alertData = builder.alertData;
	}
	
	/** ------------| Override |------------**/

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	/** ------------| Builder |------------**/
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Map<String, Object> responseData= new HashMap<String,Object>();
		private Object responseEntity;
		private List<Alert> alertData;

		public Builder responseData(Map<String, Object> responseData) {
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
		Response resp= Response.builder().build();
		resp.getResponseData().put(Param.ERROR.name(), Boolean.FALSE);
		return resp;
	}

	public static Response Success(Object responseEntity){
		Response resp= Response.builder().build();
		resp.getResponseData().put(Param.ERROR.name(), Boolean.FALSE);
		resp.setResponseEntity(responseEntity);
		return resp;
	}
	
	public static Response Fail(){
		Response resp= Response.builder().build();
		resp.getResponseData().put(Param.ERROR.name(), Boolean.TRUE);
		return resp;
	}
	
	public static Response Fail(Object responseEntity){
		Response resp= Response.builder().build();
		resp.getResponseData().put(Param.ERROR.name(), Boolean.TRUE);
		resp.setResponseEntity(responseEntity);
		return resp;
	}
	
	public static Response build(boolean isSuccess){
		Response resp= Response.builder()
				.build();
		resp.getResponseData().put("STATUS", RErrorType.Status.parse(isSuccess));
		return resp;
	}
	
	public static Response build(boolean isSuccess, Object responseEntity){
		Response resp= Response.builder()
				.build();
		resp.getResponseData().put("STATUS", RErrorType.Status.parse(isSuccess));
		resp.setResponseEntity(responseEntity);
		return resp;
	}
	
	/** ------------| Business |------------**/
	
	public Response putParam(String key, Object value){
		this.getResponseData().put(key, value);
		return this;
	}
	public Object getParam(String key){
		return this.getResponseData().get(key);
	}
	
	public void addAlert(Alert alert){
		if(this.alertData==null){
			this.alertData= new ArrayList<Alert>();
		}
		this.alertData.add(alert);
	}
	
	public boolean status() {
		return (boolean) this.getResponseData().get(Param.ERROR.name());
	}
	
	/** ------------| Getter-Setter |------------**/
	
	public Map<String, Object> getResponseData() {
		return responseData;
	}
	
	public void setResponseData(Map<String, Object> responseData) {
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
}

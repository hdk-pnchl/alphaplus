package com.kanuhasu.ap.business.bo;

import java.io.Serializable;

import com.kanuhasu.ap.business.type.response.Param;

public class Alert implements Serializable{
	private static final long serialVersionUID = 636129773281804196L;
	
	/** ------------| instance |------------**/
	
	private String type;
	private String desc;
	
	/** ------------| Getter |------------**/
	
	public String getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
	/** ------------| Constructor |------------**/
	
	public Alert(Builder builder){
		this.type= builder.type;
		this.desc= builder.desc;
	}
	
	/** ------------| Builder |------------**/
	
	public static Builder builder(){
		return new Builder();
	}
		
	public static class Builder{
		private String type;
		private String desc;
		
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		public Builder desc(String desc) {
			this.desc = desc;
			return this;
		}
		
		public Alert build(){
			return new Alert(this);
		}
	}
	
	public static Alert success(String desc){
		return Alert.builder()
				.type(Param.Alert.success.name())
				.desc(desc)
				.build();
	}
	
	public static Alert warning(String desc){
		return Alert.builder()
				.type(Param.Alert.warning.name())
				.desc(desc)
				.build();
	}
	
	public static Alert danger(String desc){
		return Alert.builder()
				.type(Param.Alert.danger.name())
				.desc(desc)
				.build();
	}
	
	public static Alert info(String desc){
		return Alert.builder()
				.type(Param.Alert.info.name())
				.desc(desc)
				.build();
	}	
}
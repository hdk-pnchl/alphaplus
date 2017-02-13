package com.kanuhasu.ap.business.type.response;

public enum Param {
	SUCCESS("SUCCESS"), 
	ERROR_MSG("ERROR_MSG"), 
	ROW_COUNT("ROW_COUNT"), 
	TOTAL_PAGE_COUNT("TOTAL_PAGE_COUNT"), 
	CURRENT_PAGE_NO("CURRENT_PAGE_NO"), 
	ROWS_PER_PAGE("ROWS_PER_PAGE"), 
	USER_DATA("USER_DATA"), 
	EMAIL_ID("EMAIL_ID"),
	IS_EMAILID_TAKEN("IS_EMAILID_TAKEN"),
	PW_UPDATE_REQ_TOKEN("PW_UPDATE_REQ_TOKEN"),
	PW_UPDATE_REQ_DATA("PW_UPDATE_REQ_DATA"),
	PW_UPDATE_URL("PW_UPDATE_URL"),
	ERR_USER_DOESNT_EXISTS("ERR_USER_DOESNT_EXISTS"),
	COUNTRY_CODE("COUNTRY_CODE"),
	PARTNER_NAME("PARTNER_NAME"),
	CONNECTOR_NAME("CONNECTOR_NAME"),
	API_NAME("API_NAME"),
	CARRIER("CARRIER"),
	ID("ID"),
	ID_TYPE("ID_TYPE"),
	VALUE_TYPE("type"),
	VALUE("value"),
	NAME("name");

	
	Param(String val) {
		this.val = val;
	}

	private String val;

	public String val() {
		return val;
	}
	
	public static enum Type{
		STRING("string"),
		DATE("date");

		private Type(String val) {
			this.val = val;
		}

		private String val;

		public String val() {
			return val;
		}		
	}
}

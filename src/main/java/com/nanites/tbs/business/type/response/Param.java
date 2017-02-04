package com.nanites.alphaplus.business.type.response;

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
	ERR_USER_DOESNT_EXISTS("ERR_USER_DOESNT_EXISTS");

	
	Param(String desc) {
		this.desc = desc;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}

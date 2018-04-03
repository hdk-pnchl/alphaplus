package com.kanuhasu.ap.business.type.response;

public enum Param {
	STATUS, 
	ERROR_MSG, 
	ROW_COUNT, 
	TOTAL_PAGE_COUNT, 
	CURRENT_PAGE_NO, 
	ROWS_PER_PAGE, 
	DATA,
	USER_DATA, 
	EMAIL_ID,
	IS_EMAILID_TAKEN,
	PW_UPDATE_REQ_TOKEN,
	PW_UPDATE_REQ_DATA,
	PW_UPDATE_URL,
	ERR_USER_DOESNT_EXISTS,
	ERR_UPDATE_PW_TOKEN_DOESNT_EXISTS,
	type,
	value,
	name,
	ERROR;

	public static enum DataType{
		string,
		date;

		public static enum DateTime{
			DATE_AVAILABLE,
			FULL_DATE_AVAILABLE
		}		
	}
	
	public static enum Alert{
		danger,
		warning,
		success,
		info;
	}		
	
	public static enum Error{
		DATE_FORMAT("Input date is wrong. Please make sure format is: 'yyyy-MM-dd HH:mm:ss'"),
		EMAIL_ID_TAKEN("EmailID already taken.!"),		
		NAME_TAKEN("Name already taken.!"),				
		UNKNOWN_ERROR("Unknown error."),
		INVALID_REQUEST("Invalid Request.!");
		
		private String desc;
		Error(String desc){
			this.desc= desc;
		}
		
		public String desc(){
			return this.desc;
		}
	}		
}

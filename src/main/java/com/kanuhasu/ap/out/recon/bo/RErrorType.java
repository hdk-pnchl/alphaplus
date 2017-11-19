package com.kanuhasu.ap.out.recon.bo;

public enum RErrorType {
	FILE_EXT_MISSING("FileName","Extension Missing"),
	FILE_EXT("FileName","Extension not matching"),
	FILE_TYPE("FileName","Type not matching"),
	FILE_COUNTRY_CODE("FileName","Country-code not matching"),
	FILE_NETWORK_CODE("FileName","Network-code not matching"),
	FILE_DATE("FileName","Date not correct"),
	FILE_VERSION("FileName","Version not correct"),
	HEADER_MISSING("Header","Missing."),
	HEADER_FROMAT("Header","Format is wrong. It should be of 'YYYY-MM-DD 00:00:00 - HH:MM:SS UTC'"),
	HEADER_FROMAT_MISSING_000000_IN_DATE("Header","'00:00:00' is missing."),
	HEADER_REQ_SPACE("Header","' - ' is required to split date and time. And there should be a space on either side(Date and Time) of '-'"),
	HEADER_FROMAT_DATE("Header","Date format is wrong."),
	HEADER_FROMAT_DATE_UTC("Header","Date format is wrong. Not UTC."),
	HEADER_FROMAT_TIME("Header","Time format is wrong."),	
	HEADER_FROMAT_TIME_UTC_FLAG("Header","UTC should be included at the end of the header after the second time"),
	COLUMN_TXN_TYPE_NOT_MATCHING("Column","TransactionType should be one of 'charge', 'refund' or 'reverse'"),	
	COLUMN_TXN_TYPE_CASE_INCORRECT("Column","TransactionType should be lower case"),
	COLUMN_OP_TXN_STATUS_NOT_MATCHING("Column","OperatorTransactionStatus should be one of 'success' or 'fail'"),	
	COLUMN_OP_TXN_STATUS_CASE_INCORRECT("Column","OperatorTransactionStatus should be lower case"),
	COLUMN_TOTAL_AMOUNT_DECIMAL_PLACE("Column","TotalAmount - Should include 3 decimal places"),
	COLUMN_TOTAL_AMOUNT_GREATER_THAN_ZERO("Column","TotalAmount - Should not be 0"),
	COLUMN_TOTAL_AMOUNT_FORMAT_ERROR("Column","TotalAmount - Wrong format"),
	COLUMN_TOTAL_AMOUNT_UNIT_ERROR("Column","TotalAmount - Should be in major unit format"),
	COLUMN_DATE_FROMAT_ERROR("Column","TransactionDate - should be in UTC format and should include seconds (YYYY-MM-DD HH:MM:SS)"),
    COLUMN_ORDER_INCORRECT("Column","Transactions should be listed in chronological order (oldest at the top, newest at the bottom)"),
	MISSING_DATA_INSTACNE("Missing data","Not enough data to initiate Recon analysis"),
	MISSING_DATA("Missing data","Row isnt having all required column data"),
	EXECEPTION_bokuOriginalTransactionId("Exeception","bokuOriginalTransactionId not matching."),
	EXECEPTION_transactionTimestamp("Exeception","transactionTimestamp not matching."),
	EXECEPTION_transactionType("Exeception","transactionType not matching."),
	EXECEPTION_operatorTransactionStatus("Exeception","operatorTransactionStatus not matching."),
	EXECEPTION_operatorOriginalTransactionId("Exeception","operatorOriginalTransactionId not matching."),
	EXECEPTION_totalAmount("Exeception","totalAmount not matching."),
	EXECEPTION_currencyCode("Exeception","currencyCode not matching."),
	EXCEPTION_countryCode("Exeception","countryCode not matching."),
	EXECEPTION_mobileNumber("Exeception","mobileNumber not matching."),
	EXECEPTION_acr("Exeception","acr not matching."),
    EXECEPTION_MISSING_TXN("Exeception","Missing equivalent txn."),
    HEADER_FROMAT_INVALUD_LENGTH("Header","Invalid Length."),
	CHARGE_FOR_RESPECTIVE_REVERSAL_FAILED("Generic", "Respective Charge for reversal has failed."),
	MERCHANT_ID_MISMATCH("Generic", "Merchant Id mismatch");
	
	private String type;
	private String summary;

	private RErrorType(String type, String summary) {
		this.type = type;
		this.summary = summary;
	}

	public String type() {
		return type;
	}
	
	public String summary() {
		return summary;
	}
	
	public static enum Status{
		SUCCESS,
		FAIL;
		
		public static Status parse(boolean isSuccess){
			if(isSuccess){
				return SUCCESS;
			}else{
				return FAIL;
			}
		}
	}		
}

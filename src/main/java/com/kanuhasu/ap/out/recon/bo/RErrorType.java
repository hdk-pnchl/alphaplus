package com.kanuhasu.ap.out.recon.bo;

public enum RErrorType {
	//file-name
	FILE_NAME_FAULTY("FileName","It must have 5 elements in it i.e prefix(transactions), country-code, network, date, version, sub-version"),
	
	FILE_EXT_MISSING("FileName","Extension Missing"),
	FILE_EXT("FileName","Extension not matching"),
	FILE_EXT_FORM("FileName","Extension not matching with what provided in form."),
	
	FILE_NAME_PREFIX("FileName","Prefix must be 'transactions'"),
	FILE_NAME_PREFIX_FORM("FileName","Prefix not matching with what provided in form."),
	
	FILE_COUNTRYCODE_CASE("FileName","Country-code must be uppercase."),
	FILE_COUNTRYCODE_FORM("FileName","Country-code not matching with what provided in form."),
	
	FILE_NETWORKCODE_FORM("FileName","Network-code not matching"),
	
	FILE_DATE("FileName","Date not matching."),
	FILE_DATE_FORM("FileName","Date not matching with what provided in form."),
	FILE_DATE_WRONG_FROMAT("FileName","FileDate format is wrong."),
	
	FILE_VERSION("FileName","Version not correct"),
	FILE_VERSION_FAULTY_PREFIX("FileName","Version must have prefix 'v'(exatly 1s) in it ."),
	FILE_VERSION_FAULTY_HYPHEN("FileName","Version must have '-' in it."),
	FILE_VERSION_FORM("FileName","Version not matching with what provided in form."),
	
	//file-header
	HEADER_MISSING("Header","Missing."),	
	HEADER_MISSING_UTC_FLAG("Header","'UTC' must be included."),
	HEADER_MISSING_000000("Header","'00:00:00' is missing."),	
	HEADER_HYPHEN("Header","Hyphen count must be 4."),
	HEADER_FROMAT("Header","Faulty format"),
	HEADER_REQ_SPACE("Header","' - ' is required to split date and time. And there should be a space on either side(Date and Time) of '-'"),
	
	HEADER("Header","Header format is wrong."),
	HEADER_DATE_PART("Header","Date format is wrong."),
	HEADER_TIME_PART("Header","Time format is wrong."),
	HEADER_FROMAT_DATE_UTC("Header","Date format is wrong. Not UTC."),
	HEADER_FROMAT_TIME("Header","Time format is wrong."),	
	HEADER_FROMAT_INVALUD_LENGTH("Header","Invalid Length."),
	
	TXN_TYPE_NOT_MATCHING("Txn","TransactionType should be one of 'charge', 'refund' or 'reverse'"),	
	TXN_TYPE_CASE_INCORRECT("Txn","TransactionType should be lower case"),
	TXN_OP_STATUS_NOT_MATCHING("Txn","OperatorTransactionStatus should be one of 'success' or 'fail'"),	
	TXN_OP_STATUS_CASE_INCORRECT("Txn","OperatorTransactionStatus should be lower case"),
	TXN_TOTAL_AMOUNT_DECIMAL_PLACE("Txn","TotalAmount - Should include 3 decimal places"),
	TXN_TOTAL_AMOUNT_GREATER_THAN_ZERO("Txn","TotalAmount - Should not be 0"),
	TXN_TOTAL_AMOUNT_FORMAT_ERROR("Txn","TotalAmount - Wrong format"),
	TXN_TOTAL_AMOUNT_UNIT_ERROR("Txn","TotalAmount - Should be in major unit format"),
	TXN_DATE_FROMAT_ERROR("Txn","TransactionDate - should be in UTC format and should include seconds (YYYY-MM-DD HH:MM:SS)"),
    TXN_ORDER_INCORRECT("Txn","Transactions should be listed in chronological order (oldest at the top, newest at the bottom)"),
	
    MISSING_DATA_INSTACNE("Missing data","Not enough data to initiate Recon analysis"),
	MISSING_DATA("Missing data","Txn does not mandatory fields."),
	
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

package com.kanuhasu.ap.out.recon.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Txn {
	
	/** ------------| instance |------------**/
	
	private String bokuTransactionId;
	private String operatorTransactionId;
	private Date transactionTimestampObj;
	private String transactionTimestamp;
	private String transactionType;
	private String operatorTransactionStatus;
	private String operatorOriginalTransactionId;
	private String totalAmount;
	private String currencyCode;
	private String mobileNumber;
	private String acr;
	private String row;
	/**
	 * Is any of the column missing
	 */
	private boolean isMissingData;
	/**
	 * Is any of the mandatory column data empty 
	 */
	private boolean isEmptyData;
	private boolean isValid;
	private List<TxnFileProp> missingDataList = new ArrayList<TxnFileProp>();
	private List<Err> errors= new ArrayList<Err>();

	/** ------------| Constructor |------------**/

	public Txn() {
	}
	
	/** ------------| Override |------------**/
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	/** ------------| Business |------------**/
	
	/**
	 * This logic is half-cooked. 
	 * i.e. If we do not know txnType, we can not decide if bokuTxnID(in case of reverse) and operatorOriginalTransactionId(for charge) is required. 
	 */
	public void checkForEmptyData() {
		if(this.isMissingData()){
			missingDataList.add(TxnFileProp.incompleteData);
		}
		if(StringUtils.isBlank(this.getBokuTransactionId())) {
			missingDataList.add(TxnFileProp.bokuTransactionId);
		}
		if(StringUtils.isBlank(this.getOperatorTransactionId())) {
			missingDataList.add(TxnFileProp.operatorTransactionId);
		}
		if(StringUtils.isBlank(this.getTransactionTimestamp())) {
			missingDataList.add(TxnFileProp.transactionTimestamp);
		}
		if(StringUtils.isBlank(this.getTransactionType())) {
			missingDataList.add(TxnFileProp.transactionType);
		}
		if(StringUtils.isBlank(this.getOperatorOriginalTransactionId())) {
			missingDataList.add(TxnFileProp.operatorOriginalTransactionId);
		}
		if(StringUtils.isBlank(this.getOperatorTransactionStatus())) {
			missingDataList.add(TxnFileProp.operatorTransactionStatus);
		}else{
			if(this.getOperatorTransactionStatus().equals(CarrierTransactionStatus.FAILED.val())){
				missingDataList.remove(TxnFileProp.operatorTransactionId);
				missingDataList.remove(TxnFileProp.operatorOriginalTransactionId);
			}
		}
		if(StringUtils.isBlank(this.getTotalAmount())) {
			missingDataList.add(TxnFileProp.totalAmount);
		}
		if(StringUtils.isBlank(this.getCurrencyCode())) {
			missingDataList.add(TxnFileProp.currencyCode);
		}
		if(StringUtils.isBlank(this.getMobileNumber()) && StringUtils.isBlank(this.getAcr())) {
			missingDataList.add(TxnFileProp.msisdn_acr);
		}
		
		TransactionType txnType = TransactionType.parse(this.getTransactionType());
		if(txnType != null) {
			//operatorOriginalTransactionId not there for charge.
			if(txnType == TransactionType.CHARGE) {
				missingDataList.remove(TxnFileProp.operatorOriginalTransactionId);
			}
			//bokuTransactionId CAN be NULL in the case of Reversal.
			if(txnType == TransactionType.REVERSAL) {
				missingDataList.remove(TxnFileProp.bokuTransactionId);
			}
		}else{
			missingDataList.remove(TxnFileProp.operatorOriginalTransactionId);
			missingDataList.remove(TxnFileProp.bokuTransactionId);
		}

		if(!this.getMissingDataList().isEmpty()) {
			this.setEmptyData(true);
		}
	}

	public void addError(Err err) {
		this.getErrors().add(err);
	}
	
	public void addError(String key, Err err) {
		err.setKey(key);
		this.getErrors().add(err);
	}
	
	/** ------------| Getter-Setter |------------**/
	
	public String getBokuTransactionId() {
		return bokuTransactionId;
	}
	
	public void setBokuTransactionId(String bokuTransactionId) {
		this.bokuTransactionId = bokuTransactionId;
	}
	
	public String getOperatorTransactionId() {
		return operatorTransactionId;
	}
	
	public void setOperatorTransactionId(String operatorTransactionId) {
		this.operatorTransactionId = operatorTransactionId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getOperatorTransactionStatus() {
		return operatorTransactionStatus;
	}
	
	public void setOperatorTransactionStatus(String operatorTransactionStatus) {
		this.operatorTransactionStatus = operatorTransactionStatus;
	}
	
	public String getOperatorOriginalTransactionId() {
		return operatorOriginalTransactionId;
	}
	
	public void setOperatorOriginalTransactionId(String operatorOriginalTransactionId) {
		this.operatorOriginalTransactionId = operatorOriginalTransactionId;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getAcr() {
		return acr;
	}
	
	public void setAcr(String acr) {
		this.acr = acr;
	}
	
	public String getRow() {
		return row;
	}
	
	public void setRow(String row) {
		this.row = row;
	}
	
	public boolean isMissingData() {
		return isMissingData;
	}
	
	public void setMissingData(boolean isMissingData) {
		this.isMissingData = isMissingData;
	}
	
	public boolean isEmptyData() {
		return isEmptyData;
	}
	
	public void setEmptyData(boolean isEmptyData) {
		this.isEmptyData = isEmptyData;
	}
	
	public List<Err> getErrors() {
		return errors;
	}
	
	public void setErrors(List<Err> errors) {
		this.errors = errors;
	}
	
	public List<TxnFileProp> getMissingDataList() {
		return missingDataList;
	}
	
	public void setMissingDataList(List<TxnFileProp> missingDataList) {
		this.missingDataList = missingDataList;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Date getTransactionTimestampObj() {
		return transactionTimestampObj;
	}

	public String getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestampObj(Date transactionTimestampObj) {
		this.transactionTimestampObj = transactionTimestampObj;
	}

	public void setTransactionTimestamp(String transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}
}
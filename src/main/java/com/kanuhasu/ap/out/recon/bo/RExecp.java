package com.kanuhasu.ap.out.recon.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.out.recon.util.DateUtil;

public class RExecp {
	
	/** ------------| instance |------------**/
	
	private String merchantId;//0
	private String transactionType;//1
	private String bokuTransactionId;//2
	private String transactionDate;//3
	private String transactionTime;//4
	private String countryCode;//5
	private String networkCode;//6
	private String merchantTransactionId;//7
	private String operatorTransactionId;//8
	private String productDescription;//9
	private String merchantData;//10
	private String currencyCode;//11
	private String totalAmount;//12
	private String bokuOriginalTransactionId;//13
	private String operatorOriginalTransactionId;//14
	private String merchantOriginalTransactionId;//15
	private String originalTransactionDate;//16
	private String originalTransactionTime;//17
	private String refundReasonCode;//18
	private String refundSource;//19
	private String reconciliationStatus;//20
	private String reconciliationStatusDate;//21
	private String reconciliationStatusTime;//22
	private String bokuTransactionStatus;//23
	private String operatorTransactionStatus;//24
	
	private String row;
	private boolean isMissingData;
	private boolean isMissingTxn;

	private Map<String, Err> errors = new HashMap<String, Err>();
	
	/** ------------| Override |------------**/
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	/** ------------| Business |------------**/
	
	public void addError(String key, Err err) {
		this.getErrors().put(key, err);
	}
	
    public String formatExecDateToTxnDate() {
        String execTxnDateTimeStr = null;
        if (StringUtils.isNotBlank(this.getOriginalTransactionDate()) && 
        		StringUtils.isNotBlank(this.getOriginalTransactionTime()))
        {
            execTxnDateTimeStr = new StringBuilder()
            		.append(this.getOriginalTransactionDate()).append(" ")
                    .append(this.getOriginalTransactionTime()).toString();
            Date execTxnDate = DateUtil.parse(execTxnDateTimeStr, DateUtil.sdf__MMddyyyy_HH$mm$ss);
            if (execTxnDate != null) {
                execTxnDateTimeStr = DateUtil.format(execTxnDate, DateUtil.sdf__yyyy$MM$dd_HH$mm$ss);
            }

        }
        return execTxnDateTimeStr;
    }	
	
    public String formatExecAmount() {
        if (StringUtils.isNotBlank(this.getTotalAmount())) {
            return ((Float) Math.abs(Float.valueOf(this.getTotalAmount()))).toString();
        }
        return this.getTotalAmount();
    }	
	
	/** ------------| Getter-Setter |------------**/
	
	public String getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getBokuTransactionId() {
		return bokuTransactionId;
	}
	
	public void setBokuTransactionId(String bokuTransactionId) {
		this.bokuTransactionId = bokuTransactionId;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getTransactionTime() {
		return transactionTime;
	}
	
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getNetworkCode() {
		return networkCode;
	}
	
	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}
	
	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}
	
	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}
	
	public String getOperatorTransactionId() {
		return operatorTransactionId;
	}
	
	public void setOperatorTransactionId(String operatorTransactionId) {
		this.operatorTransactionId = operatorTransactionId;
	}
	
	public String getProductDescription() {
		return productDescription;
	}
	
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	public String getMerchantData() {
		return merchantData;
	}
	
	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getBokuOriginalTransactionId() {
		return bokuOriginalTransactionId;
	}
	
	public void setBokuOriginalTransactionId(String bokuOriginalTransactionId) {
		this.bokuOriginalTransactionId = bokuOriginalTransactionId;
	}
	
	public String getOperatorOriginalTransactionId() {
		return operatorOriginalTransactionId;
	}
	
	public void setOperatorOriginalTransactionId(String operatorOriginalTransactionId) {
		this.operatorOriginalTransactionId = operatorOriginalTransactionId;
	}
	
	public String getMerchantOriginalTransactionId() {
		return merchantOriginalTransactionId;
	}
	
	public void setMerchantOriginalTransactionId(String merchantOriginalTransactionId) {
		this.merchantOriginalTransactionId = merchantOriginalTransactionId;
	}
	
	public String getOriginalTransactionDate() {
		return originalTransactionDate;
	}
	
	public void setOriginalTransactionDate(String originalTransactionDate) {
		this.originalTransactionDate = originalTransactionDate;
	}
	
	public String getOriginalTransactionTime() {
		return originalTransactionTime;
	}
	
	public void setOriginalTransactionTime(String originalTransactionTime) {
		this.originalTransactionTime = originalTransactionTime;
	}
	
	public String getRefundReasonCode() {
		return refundReasonCode;
	}
	
	public void setRefundReasonCode(String refundReasonCode) {
		this.refundReasonCode = refundReasonCode;
	}
	
	public String getRefundSource() {
		return refundSource;
	}
	
	public void setRefundSource(String refundSource) {
		this.refundSource = refundSource;
	}
	
	public String getReconciliationStatus() {
		return reconciliationStatus;
	}
	
	public void setReconciliationStatus(String reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}
	
	public String getReconciliationStatusDate() {
		return reconciliationStatusDate;
	}
	
	public void setReconciliationStatusDate(String reconciliationStatusDate) {
		this.reconciliationStatusDate = reconciliationStatusDate;
	}
	
	public String getReconciliationStatusTime() {
		return reconciliationStatusTime;
	}
	
	public void setReconciliationStatusTime(String reconciliationStatusTime) {
		this.reconciliationStatusTime = reconciliationStatusTime;
	}
	
	public String getBokuTransactionStatus() {
		return bokuTransactionStatus;
	}
	
	public void setBokuTransactionStatus(String bokuTransactionStatus) {
		this.bokuTransactionStatus = bokuTransactionStatus;
	}
	
	public String getOperatorTransactionStatus() {
		return operatorTransactionStatus;
	}
	
	public void setOperatorTransactionStatus(String operatorTransactionStatus) {
		this.operatorTransactionStatus = operatorTransactionStatus;
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

	public Map<String, Err> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, Err> errors) {
		this.errors = errors;
	}

	public boolean isMissingTxn() {
		return isMissingTxn;
	}

	public void setMissingTxn(boolean isMissingTxn) {
		this.isMissingTxn = isMissingTxn;
	}	    
}
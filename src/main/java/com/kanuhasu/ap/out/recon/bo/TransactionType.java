package com.kanuhasu.ap.out.recon.bo;

import org.apache.commons.lang3.StringUtils;

public enum TransactionType {
    CHARGE("charge"), 
    REFUND("refund"), 
    REVERSAL("reversal");

    private String val;

    private TransactionType(String val) {
        this.val = val;
    }

    public String val() {
        return val;
    }

    public static TransactionType parse(String txnTypeStr) {
        TransactionType transactionType = null;
        if (StringUtils.isNotBlank(txnTypeStr)) {
            for (TransactionType txnType : TransactionType.values()) {
                if (txnTypeStr.equals(txnType.val())) {
                    transactionType = txnType;
                }
            }
        }
        return transactionType;
    }
}

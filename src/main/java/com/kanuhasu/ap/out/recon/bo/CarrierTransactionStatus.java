package com.kanuhasu.ap.out.recon.bo;

import org.apache.commons.lang3.StringUtils;

public enum CarrierTransactionStatus {
    SUCCESS("success"), 
    FAILED("failed");

    private String val;

    private CarrierTransactionStatus(String val) {
        this.val = val;
    }

    public String val() {
        return val;
    }

    public static CarrierTransactionStatus parse(String opTxnStatusStr) {
        CarrierTransactionStatus operatorTransactionStatus = null;
        if (StringUtils.isNotBlank(opTxnStatusStr)) {
            for (CarrierTransactionStatus opTxnStatus : CarrierTransactionStatus.values()) {
                if (opTxnStatusStr.equals(opTxnStatus.val())) {
                    operatorTransactionStatus = opTxnStatus;
                }
            }
        }
        return operatorTransactionStatus;
    }
}

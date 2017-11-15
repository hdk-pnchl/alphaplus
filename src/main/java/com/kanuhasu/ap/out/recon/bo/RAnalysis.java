package com.kanuhasu.ap.out.recon.bo;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.out.recon.util.DateUtil;
import com.kanuhasu.ap.out.recon.util.ReconUtil;

public class RAnalysis {

	/** ------------| instance |------------**/

    private String country;
    private String network;
    
    private File txnFile;
    private String txnFileHeader;
    private String txnFileEtx;
    private String txnFileType;
    private String txnFileVersion;
    private Date txnFileDate;    
    private List<Txn> txns = new ArrayList<Txn>();
    
    /**
     * Map<BokuOriginalTransactionId, Txn>
     */
    private Map<String, Txn> txnMap = new HashMap<String, Txn>();

    private File execFile;    
    private List<RExecp> execs = new ArrayList<RExecp>();
    
    private boolean isOrderCorrect = true;
    private boolean isRowDataCorrect = true;

    private Map<String, String> errorMap = new HashMap<String, String>();
    private List<Err> errors= new ArrayList<Err>();
    
	/** ------------| Constructor |------------**/

    public RAnalysis() {
    }

	/** ------------| Builder |------------**/

    protected RAnalysis(Builder<?> builder) {
        this.country = builder.country;
        this.network = builder.network;
    	
        this.txnFile = builder.txnFile;
        this.txnFileHeader = builder.txnFileHeader;
        this.txnFileEtx = builder.txnFileEtx;
        this.txnFileType = builder.txnFileType;        
        this.txnFileVersion = builder.txnFileVersion;
        this.txnFileDate = builder.txnFileDate;        
        this.txns = builder.txns;
        
        this.execs = builder.execs;
        this.execFile = builder.execFile;

        this.errorMap = builder.errors;
    }

    public static class Builder<T extends Builder<T>> {
        private String country;
        private String network;
        
        private File txnFile;
        private String txnFileHeader;
        private String txnFileEtx;
        private String txnFileType;
        private String txnFileVersion;
        private Date txnFileDate;    
        private List<Txn> txns = new ArrayList<Txn>();
        
        private List<RExecp> execs = new ArrayList<RExecp>();
        private File execFile;
        
        private Map<String, String> errors = new HashMap<String, String>();

        public T txns(List<Txn> txns) {
            this.txns = txns;
            return self();
        }

        public T txnFile(File txnFile) {
            this.txnFile = txnFile;
            return self();
        }

        public T execs(List<RExecp> execs) {
            this.execs = execs;
            return self();
        }

        public T execFile(File execFile) {
            this.execFile = execFile;
            return self();
        }

        public T txnFileEtx(String txnFileEtx) {
            this.txnFileEtx = txnFileEtx;
            return self();
        }

        public T txnFileType(String txnFileType) {
            this.txnFileType = txnFileType;
            return self();
        }

        public T country(String country) {
            this.country = country;
            return self();
        }

        public T network(String network) {
            this.network = network;
            return self();
        }

        public T txnFileVersion(String txnFileVersion) {
            this.txnFileVersion = txnFileVersion;
            return self();
        }

        public T txnFileDate(Date txnFileDate) {
            this.txnFileDate = txnFileDate;
            return self();
        }

        public T errors(Map<String, String> errors) {
            this.errors = errors;
            return self();
        }

        public T txnFileHeader(String txnFileHeader) {
            this.txnFileHeader = txnFileHeader;
            return self();
        }

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T)this;
        }

        public RAnalysis build() {
            return new RAnalysis(this);
        }
    }

    @SuppressWarnings("rawtypes")
    public static Builder<?> builder() {
        return new Builder();
    }

	/** ------------| Override |------------**/

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/** ------------| Business |------------**/

    private void addError(RErrorType errorT, String desc) {
        this.getErrors().add(new Err(errorT.type(), errorT, desc));
    }

    /**
     * Validations: Check for Inputs,
     * 1. Country
     * 2. File Ext
     * 3. File Type
     * 4. Network Code
     * 5. Version
     * 6. Date
     */
    public boolean isValidReconRequest() {
        StringBuilder missingInstanceData = new StringBuilder();
        if (StringUtils.isBlank(this.getCountry())) {
            missingInstanceData.append(TxnFileProp.country).append(", ");
        }
        if (StringUtils.isBlank(this.getFileEtx())) {
            missingInstanceData.append(TxnFileProp.fileExtension).append(", ");
        }
        if (StringUtils.isBlank(this.getTxnFileType())) {
            missingInstanceData.append(TxnFileProp.fileType).append(", ");
        }
        if (StringUtils.isBlank(this.getNetwork())) {
            missingInstanceData.append(TxnFileProp.networkCode).append(", ");
        }
        if (StringUtils.isBlank(this.getTxnFileVersion())) {
            missingInstanceData.append(TxnFileProp.version).append(", ");
        }
        if (this.getTxnFileDate() == null) {
            missingInstanceData.append(TxnFileProp.date).append(", ");
        }
        if (missingInstanceData.length() > 0) {
            this.addError(RErrorType.MISSING_DATA_INSTACNE, missingInstanceData.toString());
        }
        return missingInstanceData.length() == 0;
    }

    /**
     * Input sample: transactions_FR_02f802_20170429_v1_1-1.csv
     *
     * Template: transactions_<CC>_<NETWORK>_<yyyyMMdd>_v1_1-1.csv
     *
     * Validations: 
     * 1. File extension 
     * 2. File type 
     * 3. Country 
     * 4. Network 
     * 5. Date 
     * 6. Version
     */
    public boolean validateFileName() {
        String txnFileNameFull = this.getTxnFile().getName();
        String fileEle[] = txnFileNameFull.split("\\.");
        if (fileEle.length == 2) {
            // 1. File extension
            String txnFileExt = fileEle[1];
            if (StringUtils.isNotBlank(this.getFileEtx())) {
                if (!txnFileExt.equals(this.getFileEtx())) {
                    this.addError(RErrorType.FILE_EXT, ReconUtil.buildErrStr(txnFileExt, this.getFileEtx()));
                }
            } else {
                // ext did not come from FORM
                this.setTxnFileEtx(txnFileExt);
            }

            String txnFileName = fileEle[0]; // transactions_<CC>_<NETWORK>_<yyyyMMdd>_v1_1-1
            String[] txnFileNameEle = txnFileName.split("_");

            // 2. File type
            String fileNamePrefix = CommonUtil.getEleFromAry(txnFileNameEle, 0);
            if (StringUtils.isNotBlank(this.getTxnFileType())) {
                if (!this.getTxnFileType().equals(fileNamePrefix)) {
                    this.addError(RErrorType.FILE_TYPE, ReconUtil.buildErrStr(fileNamePrefix, this.getTxnFileType()));
                }
            } else {
                this.setTxnFileType(fileNamePrefix);
            }

            // 3. Country
            String countryCode = CommonUtil.getEleFromAry(txnFileNameEle, 1);
            if (!(StringUtils.upperCase(countryCode)).equals(countryCode)) {
                this.addError(RErrorType.FILE_COUNTRY_CODE,
                		ReconUtil.buildErrStr(countryCode, StringUtils.upperCase(countryCode)));
            }
            if (StringUtils.isNotBlank(this.getCountry())) {
                if (!this.getCountry().equals(countryCode)) {
                    this.addError(RErrorType.FILE_COUNTRY_CODE, ReconUtil.buildErrStr(countryCode, this.getCountry()));
                }
            } else {
                this.setCountry(countryCode);
            }

            // 4. Network
            String networkCode = CommonUtil.getEleFromAry(txnFileNameEle, 2);
            if (StringUtils.isNotBlank(this.getNetwork())) {
                if (!this.getNetwork().equals(networkCode)) {
                    this.addError(RErrorType.FILE_NETWORK_CODE, ReconUtil.buildErrStr(networkCode, this.getNetwork()));
                }
            } else {
                this.setNetwork(countryCode);
            }

            // 5. Date
            String dateStr = CommonUtil.getEleFromAry(txnFileNameEle, 3);
            if (this.getTxnFileDate() != null) {
                Date date = DateUtil.parseBasic(dateStr);
                if (!this.getTxnFileDate().equals(date)) {
                    this.addError(RErrorType.FILE_DATE,
                    		ReconUtil.buildErrStr(dateStr, DateUtil.format(this.getTxnFileDate(), DateUtil.sdf__yyyyMMdd)));
                }
            } else {
                this.setTxnFileDate(DateUtil.parse(dateStr, DateUtil.sdf__yyyyMMdd));
            }

            // 6. Version
            String versionPart1 = StringUtils.defaultString(CommonUtil.getEleFromAry(txnFileNameEle, 4), "");
            String versionPart2 = StringUtils.defaultString(CommonUtil.getEleFromAry(txnFileNameEle, 5), "");
            ;
            String version = versionPart1 + "_" + versionPart2;
            if (StringUtils.isNotBlank(this.getTxnFileVersion())) {
                if (!this.getTxnFileVersion().equals(version)) {
                    this.addError(RErrorType.FILE_VERSION, ReconUtil.buildErrStr(version, this.getTxnFileVersion()));
                }
            } else {
                this.setTxnFileVersion(version);
            }
        } else {
            this.addError(RErrorType.FILE_EXT_MISSING, ReconUtil.buildErrStr(txnFileNameFull, "csv"));
        }
        return this.getErrors().isEmpty();
    }

    /**
     * Validations:
     * 1. Header
     * 2. Column
     * 3. Exception
     */
    public boolean validateFileInternals() {
        this.validateHeader();
        this.validateColumn();
        if (this.getExecFile() != null) {
            this.validateExceptions();
        }
        return this.getErrors().isEmpty() && this.isRowDataCorrect();
    }

    /**
     * Input sample: 2016-04-18 00:00:00 - 23:59:59 UTC
     *
     * Validations: 
     * 1. Date and time range should be in UTC format (e.g. 2014-01-01 00:00:00 – 23:59:59 UTC). 
     * 2. There should be a space on either side of the hyphen between the timestamps 
     * 3. The date should only be reported once 
     * 4. "UTC" should be included at the end of the header after the second time
     */
    private void validateHeader() {
        String header = this.getTxnFileHeader();
        if (StringUtils.isNotBlank(header)) {
            if (!header.contains("UTC")) {
                this.addError(RErrorType.HEADER_FROMAT_TIME_UTC_FLAG, ReconUtil.buildErrStr(this.getTxnFileHeader(), null));
            }
            if (!header.contains("00:00:00")) {
                this.addError(RErrorType.HEADER_FROMAT_MISSING_000000_IN_DATE,
                		ReconUtil.buildErrStr(this.getTxnFileHeader(), null));
            }

            // fetch date/time and validate
            if (!header.contains(" - ")) {
                // Expected: "yyyy-MM-dd 00:00:00 HH:mm:ss UTC"

                this.addError(RErrorType.HEADER_FROMAT, ReconUtil.buildErrStr(this.getTxnFileHeader(), null));
                this.addError(RErrorType.HEADER_REQ_SPACE, ReconUtil.buildErrStr(this.getTxnFileHeader(), null));

                String[] headerEles = header.split(" ");
                if (headerEles.length >= 2) {
                    // date
                    // Expected: "yyyy-MM-dd"
                    String headerDateEleStr = CommonUtil.getEleFromAry(headerEles, 0);
                    Date headerDate = DateUtil.parse(headerDateEleStr, DateUtil.sdf__yyyy$MM$dd);
                    if (headerDate == null) {
                        this.addError(RErrorType.HEADER_FROMAT_DATE,
                        		ReconUtil.buildErrStr(headerDateEleStr, "yyyy-MM-dd 00:00:00"));
                    }
                    // time
                    // build timeStr i.e. "HH:mm:ss UTC"
                    StringBuilder headerTimEleStr = new StringBuilder();
                    for (int i = 2; i < headerEles.length; i++) {
                        headerTimEleStr.append(headerEles[i]);
                        if (i != headerEles.length) {
                            headerTimEleStr.append(" ");
                        }
                    }
                    if (StringUtils.isBlank(headerTimEleStr.toString())) {
                        this.addError(RErrorType.HEADER_FROMAT_TIME, ReconUtil.buildErrStr(header, "HH:mm:ss UTC"));
                    } else {
                        if (headerTimEleStr.toString().split(":").length != 3) {
                            this.addError(RErrorType.HEADER_FROMAT_TIME,
                            		ReconUtil.buildErrStr(headerDateEleStr, "HH:mm:ss UTC"));
                        }
                    }
                } else {
                    this.addError(RErrorType.HEADER_FROMAT_DATE, ReconUtil.buildErrStr(this.getTxnFileHeader(), null));
                }
            } else {
                String[] headerEle = header.split(" - ");
                // date
                String headerDateEleStr = headerEle[0].replace("00:00:00", "").trim();
                Date headerDate = DateUtil.parse(headerDateEleStr, DateUtil.sdf__yyyy$MM$dd);
                if (headerDate == null) {
                    this.addError(RErrorType.HEADER_FROMAT_DATE,
                    		ReconUtil.buildErrStr(headerDateEleStr, "yyyy-MM-dd 00:00:00"));
                }
                // time
                String headerTimeEle = headerEle[0];
                if (headerTimeEle.split(":").length != 3) {
                    this.addError(RErrorType.HEADER_FROMAT_TIME, ReconUtil.buildErrStr(headerDateEleStr, "HH:mm:ss UTC"));
                }
            }

            // After every other validation, check for the length of header.
            if (header.length() != 34) {
                this.addError(RErrorType.HEADER_FROMAT_INVALUD_LENGTH, ReconUtil.buildErrStr(this.getTxnFileHeader(), null));
            }
        } else {
            this.addError(RErrorType.HEADER_MISSING, this.getTxnFileHeader());
        }
    }

    /**
     * Validations:
     * 1. transactionType should be lower case charge, refund, reversal 
     * 2. operatorTransactionStatus should be, lowercase "success" or "failed" 
     * 3. totalAmount, for transactions, should not be 0 totalAmount should be in major unit format, and include 3 decimal places. e.g. 100.000 
     * 4. transactionTimestamp should be in UTC format and should include seconds (YYYY-MM-DD HH:MM:SS) 
     * 5. Require fields for Charge, Refund, Reverse 6. Transactions should be listed in chronological order (oldest at the top, newest at the bottom)
     */
    private boolean validateColumn() {
        boolean isTxnColumnDataValid = true;
        Date lastTxnDate = null;
        try {
            for (Txn txn : this.getTxns()) {
                // 1. transactionType
                String transactionTypeStr = txn.getTransactionType();
                if (StringUtils.isNotBlank(transactionTypeStr)) {
                    TransactionType txnType = TransactionType.parse(transactionTypeStr);
                    if (txnType == null) {
                        // check if case is wrong here. If found after lowering the case, its an error.
                        txnType = TransactionType.parse(transactionTypeStr.toLowerCase());
                        if (txnType == null) {
                            txn.addError(TxnFileProp.transactionType.name(),
                                    new Err(RErrorType.COLUMN_TXN_TYPE_NOT_MATCHING,
                                    		ReconUtil.buildErrStr(transactionTypeStr, "charge, refund or reversal")));
                        } else {
                            txn.addError(TxnFileProp.transactionType.name(),
                                    new Err(RErrorType.COLUMN_TXN_TYPE_CASE_INCORRECT,
                                    		ReconUtil.buildErrStr(transactionTypeStr, "charge, refund or reversal")));
                        }
                    }
                }

                // 2. operatorTransactionStatus
                String opTxnStatusStr = txn.getOperatorTransactionStatus();
                if (StringUtils.isNotBlank(opTxnStatusStr)) {
                    CarrierTransactionStatus opTxnStatus = CarrierTransactionStatus.parse(opTxnStatusStr);
                    if (opTxnStatus == null) {
                        opTxnStatus = CarrierTransactionStatus.parse(opTxnStatusStr.toLowerCase());
                        if (opTxnStatus == null) {
                            txn.addError(TxnFileProp.operatorTransactionStatus.name(),
                                    new Err(RErrorType.COLUMN_OP_TXN_STATUS_NOT_MATCHING,
                                    		ReconUtil.buildErrStr(opTxnStatusStr, "success or failed")));
                        } else {
                            txn.addError(TxnFileProp.operatorTransactionStatus.name(),
                                    new Err(RErrorType.COLUMN_OP_TXN_STATUS_CASE_INCORRECT,
                                    		ReconUtil.buildErrStr(opTxnStatusStr, "success or failed")));
                        }
                    }
                }

                // 3. TotalAmount
                String totalAmountStr = txn.getTotalAmount();
                try {
                    // TotalAmount: Should be in major unit format
                    if (!totalAmountStr.contains(".")) {
                        txn.addError(TxnFileProp.totalAmount.name(), new Err(
                                RErrorType.COLUMN_TOTAL_AMOUNT_UNIT_ERROR, ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                    BigDecimal bigDecimal = new BigDecimal(totalAmountStr);
                    // TotalAmount: Should include 3 decimal places
                    int decimalPlaces = CommonUtil.getNumberOfDecimalPlaces001(totalAmountStr);
                    if (decimalPlaces != 3) {
                        txn.addError(TxnFileProp.totalAmount.name(), new Err(
                                RErrorType.COLUMN_TOTAL_AMOUNT_DECIMAL_PLACE, ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                    // TotalAmount: Should not be 0
                    if (bigDecimal.floatValue() <= 0) {
                        txn.addError(TxnFileProp.totalAmount.name(),
                                new Err(RErrorType.COLUMN_TOTAL_AMOUNT_GREATER_THAN_ZERO,
                                		ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                } catch (NumberFormatException e) {
                    // TotalAmount: Wrong format
                    txn.addError(TxnFileProp.totalAmount.name(), new Err(RErrorType.COLUMN_TOTAL_AMOUNT_FORMAT_ERROR,
                    		ReconUtil.buildErrStr(totalAmountStr, null)));
                }

                // 4. Timestamp
                if (txn.getTransactionTimestampObj() == null) {
                    txn.addError(TxnFileProp.transactionTimestamp.name(),
                            new Err(RErrorType.COLUMN_TOTAL_AMOUNT_FORMAT_ERROR,
                            		ReconUtil.buildErrStr(txn.getTransactionTimestamp(), "yyyy-MM-dd HH:mm:ss")));
                }

                // 5. Is having required filed
                if (txn.isEmptyData()) {
                    txn.addError("GenericError_001", new Err(RErrorType.MISSING_DATA,
                            "Missing: " + this.createCSV(txn.getMissingDataList())));
                    this.addError(RErrorType.MISSING_DATA, "Missing: " + this.createCSV(txn.getMissingDataList()));
                }

                // 6. Transactions should be listed in chronological order (oldest at the top, newest at the bottom)
                if (lastTxnDate == null) {
                    lastTxnDate = txn.getTransactionTimestampObj();
                } else {
                    if (lastTxnDate.compareTo(txn.getTransactionTimestampObj()) > 0) {
                        this.setOrderCorrect(false);
                        txn.addError("GenericError_002", new Err(RErrorType.COLUMN_ORDER_INCORRECT, null));
                        this.addError(RErrorType.COLUMN_ORDER_INCORRECT, null);
                    } else {
                        lastTxnDate = txn.getTransactionTimestampObj();
                    }
                }
                boolean isTxnValid = txn.getErrors().isEmpty();
                if (isTxnColumnDataValid) {
                    isTxnColumnDataValid = isTxnValid;
                }
                txn.setValid(isTxnValid);
            }
        } catch (Exception e) {
            isTxnColumnDataValid = false;
        }
        this.setRowDataCorrect(isTxnColumnDataValid);
        return isTxnColumnDataValid;
    }

    private void validateExceptions() {
        for (RExecp excep : this.getExecs()) {
        	//Txn-Type: Charge and Refund
            if (!excep.getTransactionType().equals(TransactionType.REVERSAL.val())) {
                Txn txn = this.getTxnMap().get(excep.getBokuOriginalTransactionId());
                //compare txnProp-val with excepPropVal
                if (txn != null) {
                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getBokuTransactionId(), txn.getBokuTransactionId(),
                            TxnFileProp.bokuOriginalTransactionId, RErrorType.EXECEPTION_bokuOriginalTransactionId);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getOperatorTransactionId(), txn.getOperatorTransactionId(),
                            TxnFileProp.operatorOriginalTransactionId,
                            RErrorType.EXECEPTION_operatorOriginalTransactionId);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.formatExecDateToTxnDate(), txn.getTransactionTimestamp(),
                            TxnFileProp.transactionDate, RErrorType.EXECEPTION_transactionTimestamp);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getTransactionType(), txn.getTransactionType(),
                            TxnFileProp.transactionType, RErrorType.EXECEPTION_transactionType);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getOperatorTransactionStatus(), txn.getOperatorTransactionStatus(),
                            TxnFileProp.operatorTransactionStatus, RErrorType.EXECEPTION_operatorTransactionStatus);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getOperatorOriginalTransactionId(), txn.getOperatorOriginalTransactionId(),
                            TxnFileProp.operatorOriginalTransactionId,
                            RErrorType.EXECEPTION_operatorOriginalTransactionId);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.formatExecAmount(), txn.getTotalAmount(),
                            TxnFileProp.totalAmount, RErrorType.EXECEPTION_totalAmount);

                	ReconUtil.compareTxnPropWithExcepProp(excep, excep.getCurrencyCode(), txn.getCurrencyCode(), TxnFileProp.currencyCode,
                            RErrorType.EXECEPTION_currencyCode);
                }
                //txn not found
                else {
                    excep.setMissingTxn(true);
                    excep.addError(RErrorType.EXECEPTION_MISSING_TXN.name(),
                            new Err(RErrorType.EXECEPTION_MISSING_TXN, ReconUtil.buildErrStr(excep.getRow(), null)));
                }
            }
            //Txn-Type: Reversal
            else {
                this.validateReversalFromExcepFile(excep);
            }
        }
    }
    
    /**
     *  If we have reversal in Exception-File, there could be 3 reasons to it:
     *  	1. Respective Reversal mismatched.
     *  	2. Respective Charge (for which Reversal was triggered) mismatched.
     *  	3. Respective Refund mismatched (if carrier doesn't support Reversal, there will be Refund instead of Reversal).
     *  
     *  What to do for Reversal Exception-File
     *  	Context:
     *  		- For Reversal in Exception-File, bokuTransactionId is Reversal-ID and bokuOriginalTransactionId is BokuCharge-ID
     *  		- In case of Reversal, Carrier returns respective Charge(for which Reversal was initiated) and might also return Refund(if carrier doesnt support reversal)
     *  		- In Settlement-File, there will be no existence of reversal or respective Charge/Refund.
     *  	Algo:
     *  		1: Fetch-REVERSAL from Txn-File (with Exception-File-bokuTransactionId i.e. Reversal-ID)
     *  			1.1 REVERSAL-Found: Find the miss-match between Txn-File-Reversal and Exception-File-Reversal.
     *  			1.2 REVERSAL-NotFound: Fetch Charge from DB (with Exception-File-bokuOriginalTransactionId i.e.BokuChargeID).
     *  				(Possible reasons for not finding Reversal: Either Carrier didnt report it -or- Carrier doesnt support reversal. Thus, instead of Reversal, there will be Refund(if Charge was successful) in Txn-File.	 
     *  				12.1 Charge-Successful: Fetch Refund fromTxn-File (with bokuOriginalTransactionId i.e.BokuChargeID).
     *  					(Carrier didnt report it.)
     *  					1.3.1 Charge-Found: Find the miss-match between Fetched-Refund and Exception-File-Reversal. 
     * 						1.3.1 Charge-NotFound: Log-Error
     * 					(This is why we have entry in exception)
     *  				12.2 Charge-failed: Log-Err
     */
    private void validateReversalFromExcepFile(RExecp exec){
        //ReversalRecordEntity reversal= null; //fetch it from DB.
        Object reversal= null; //fetch it from DB.
        if(reversal!=null){
            // 1.1.1 Find the miss-match between Txn-File-Reversal and Exception-File-Reversal.
        	
        	/*
        	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getBokuTransactionId(),String.valueOf(reversal.getReversalId()),
                    TxnFileProp.bokuTransactionId, RErrorType.EXECEPTION_bokuOriginalTransactionId);
        	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getMerchantId(), reversal.getMerchantId(),
                    TxnFileProp.merchantId, RErrorType.MERCHANT_ID_MISMATCH);
        	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getCountryCode(), reversal.getCountryCode(),
                    TxnFileProp.country, RErrorType.EXCEPTION_countryCode);
        	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getBokuOriginalTransactionId(), reversal.getOriginalGatewayTransactionId(),
                    TxnFileProp.bokuOriginalTransactionId, RErrorType.EXECEPTION_bokuOriginalTransactionId);
        	ReconUtil.compareTxnPropWithExcepProp(exec, exec.formatExecDateToTxnDate(), reversal.getTimeRequested().toString(),
                    TxnFileProp.transactionDate,RErrorType.EXECEPTION_transactionTimestamp);
        	*/
        }
        if(reversal==null || exec.getErrors().isEmpty()){
            //BillingCdrEntity charge= null; //fetch it from DB.
            //if(charge!=null && charge.getResponseCode()==0){
                //if charge is success, then fetch refund.
               // RefundRecordEntity refund= null; //fetch from DB
                //if(refund!=null) {
                    //find mis-match.
                	/*
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.formatExecAmount(), refund.getRefundAmount(),
                            TxnFileProp.totalAmount, RErrorType.EXECEPTION_totalAmount);
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getBokuTransactionId(), String.valueOf(reversal.getReversalId()),
                            TxnFileProp.bokuTransactionId, RErrorType.EXECEPTION_bokuOriginalTransactionId);
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getMerchantId(), refund.getMerchantId(),
                            TxnFileProp.merchantId, RErrorType.MERCHANT_ID_MISMATCH);
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getCountryCode(), refund.getCountryCode(),
                            TxnFileProp.country, RErrorType.EXCEPTION_countryCode);
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.getCurrencyCode(), refund.getCurrency(),
                            TxnFileProp.currencyCode, RErrorType.EXECEPTION_currencyCode);
                	ReconUtil.compareTxnPropWithExcepProp(exec, exec.formatExecDateToTxnDate(), refund.getRefundDate().toString(),
                            TxnFileProp.transactionDate, RErrorType.EXECEPTION_transactionTimestamp);
                	 */
               // }
            }else{
                //If charge is failure then report charge failure.
                exec.addError(RErrorType.CHARGE_FOR_RESPECTIVE_REVERSAL_FAILED.name(),
                        new Err(RErrorType.CHARGE_FOR_RESPECTIVE_REVERSAL_FAILED, ReconUtil.buildErrStr(exec.getRow(),null)));
            }
        //}
    }
    
	public String createCSV(List<TxnFileProp> missingData){
		StringBuilder stringBuilder= new StringBuilder();
		for(TxnFileProp key: missingData){
			stringBuilder.append(key.name()).append(", ");
		}
		return stringBuilder.toString();
	}    

	/** ------------| Getter-Setter |------------**/

    public List<Txn> getTxns() {
        return txns;
    }

    public void setTxns(List<Txn> txns) {
        this.txns = txns;
    }

    public File getTxnFile() {
        return txnFile;
    }

    public void setTxnFile(File txnFile) {
        this.txnFile = txnFile;
    }

    public List<RExecp> getExecs() {
        return execs;
    }

    public void setExecs(List<RExecp> execs) {
        this.execs = execs;
    }

    public File getExecFile() {
        return execFile;
    }

    public void setExecFile(File execFile) {
        this.execFile = execFile;
    }

    public String getFileEtx() {
        return txnFileEtx;
    }

    public void setTxnFileEtx(String fileEtx) {
        this.txnFileEtx = fileEtx;
    }

    public String getTxnFileType() {
        return txnFileType;
    }

    public void setTxnFileType(String fileType) {
        this.txnFileType = fileType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTxnFileVersion() {
        return txnFileVersion;
    }

    public void setTxnFileVersion(String version) {
        this.txnFileVersion = version;
    }

    public Date getTxnFileDate() {
        return txnFileDate;
    }

    public void setTxnFileDate(Date date) {
        this.txnFileDate = date;
    }

    public List<Err> getErrors() {
        return errors;
    }

    public void setErrors(List<Err> errors) {
        this.errors = errors;
    }

    public String getTxnFileHeader() {
        return txnFileHeader;
    }

    public void setTxnFileHeader(String txnFileHeader) {
        this.txnFileHeader = txnFileHeader;
    }

    public boolean isOrderCorrect() {
        return isOrderCorrect;
    }

    public void setOrderCorrect(boolean isOrderCorrect) {
        this.isOrderCorrect = isOrderCorrect;
    }

    public boolean isRowDataCorrect() {
        return isRowDataCorrect;
    }

    public void setRowDataCorrect(boolean isRowDataCorrect) {
        this.isRowDataCorrect = isRowDataCorrect;
    }

    public Map<String, Txn> getTxnMap() {
        return txnMap;
    }

    public void setTxnMap(Map<String, Txn> txnMap) {
        this.txnMap = txnMap;
    }
}
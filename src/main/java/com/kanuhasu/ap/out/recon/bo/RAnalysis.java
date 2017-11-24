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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    private String fileName;
    
    private List<Txn> txns = new ArrayList<Txn>();
    
    /**
     * Map<BokuOriginalTransactionId, Txn>
     */
    @JsonIgnore
    private Map<String, Txn> txnMap = new HashMap<String, Txn>();

    private File execFile;    
    @JsonIgnore
    private List<RExecp> execs = new ArrayList<RExecp>();
    
    private boolean isOrderCorrect = true;
    private boolean isRowDataCorrect = true;

    private List<Err> errors= new ArrayList<Err>();
    
    /**
     * keeps count of no of txn's having error.
     */
    private int txnsInError= 0;
    
	/** ------------| Constructor |------------**/

    public RAnalysis() {
    }

	/** ------------| Builder |------------**/

    protected RAnalysis(Builder<?> builder) {
        this.country = builder.country;
        this.network = builder.network;
    	
        this.txnFile = builder.txnFile;
        this.txnFileEtx = builder.txnFileEtx;
        this.txnFileType = builder.txnFileType;        
        this.txnFileVersion = builder.txnFileVersion;
        this.txnFileDate = builder.txnFileDate;        
        
        this.execFile = builder.execFile;
        
        this.fileName = builder.fileName;
    }

    public static class Builder<T extends Builder<T>> {
        private String country;
        private String network;
        
        private File txnFile;
        private String txnFileEtx;
        private String txnFileType;
        private String txnFileVersion;
        private Date txnFileDate;    
        private String fileName;
        
        private File execFile;

        public T fileName(String fileName) {
            this.fileName = fileName;
            return self();
        }

        public T txnFile(File txnFile) {
            this.txnFile = txnFile;
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

    private void addError(Err err) {
        this.getErrors().add(err);
    }
    
    private void addError(RErrorType errorT, String desc) {
        this.getErrors().add(new Err(errorT.type(), errorT, desc));
    }
    
    private void plus1TxnErr() {
    		this.txnsInError++;
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
        if (StringUtils.isBlank(this.getNetwork())) {
            missingInstanceData.append(TxnFileProp.networkCode).append(", ");
        } 
        if (this.getTxnFile()==null) {
            missingInstanceData.append("TxnFile").append(", ");
        }         
        if (StringUtils.isBlank(this.getTxnFileEtx())) {
            missingInstanceData.append(TxnFileProp.fileExtension).append(", ");
        }
        if (StringUtils.isBlank(this.getTxnFileType())) {
            missingInstanceData.append(TxnFileProp.fileType).append(", ");
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
        String txnFileNameFull = this.getFileName();
        //file name has 2 part. 1st, name. 2nd, type (txt, csv)
        String fullFileNameEle[] = txnFileNameFull.split("\\.");
        if (fullFileNameEle.length == ReconUtil.FILE_NAME_FULL_ELE_COUNT) {
            // #1. File extension
            String txnFileExt = fullFileNameEle[1];
            //check file-ext with the one provided from form.
            if(StringUtils.isNotBlank(this.getTxnFileEtx())) {
                if(!txnFileExt.equals(this.getTxnFileEtx())) {
                    this.addError(RErrorType.FILE_EXT_FORM, ReconUtil.buildErrStr(txnFileExt, this.getTxnFileEtx()));
                }
            }
            //ext did not come from FORM
            else{
                this.setTxnFileEtx(txnFileExt);
                //extension must be "csv"
	        		if(!txnFileExt.equals(ReconUtil.FILE_NAME_EXTENSION)){
	        			this.addError(RErrorType.FILE_EXT, ReconUtil.buildErrStr(txnFileExt, ReconUtil.FILE_NAME_EXTENSION));
	        		}
            }

            // #2. file-name
            String txnFileName = fullFileNameEle[0]; // transactions_<CC>_<NETWORK>_<yyyyMMdd>_v1_1-1
            String[] txnFileNameEle = txnFileName.split("_");
            //FileName must have 5 elements in it i.e prefix(transactions), country-code, network, date, version, sub-version
            if(txnFileNameEle.length==ReconUtil.FILE_NAME_ELE_COUNT){
                // #2.1 File prefix
                String fileNamePrefix = CommonUtil.getEleFromAry(txnFileNameEle, 0);
                //check file-name-prefix with the one provided from form.	
                if(StringUtils.isNotBlank(this.getTxnFileType())){
                    if (!this.getTxnFileType().equals(fileNamePrefix)){
                        this.addError(RErrorType.FILE_NAME_PREFIX_FORM, ReconUtil.buildErrStr(fileNamePrefix, this.getTxnFileType()));
                    }
                }
                //file-name-prefix did not come from FORM.
                else{
                		this.setTxnFileType(fileNamePrefix);
                		if(!fileNamePrefix.equals(ReconUtil.FILE_NAME_PREFIX)){
                			this.addError(RErrorType.FILE_NAME_PREFIX, ReconUtil.buildErrStr(fileNamePrefix, ReconUtil.FILE_NAME_PREFIX));
                		}
                }

                // #2.2 Country
                String countryCode = CommonUtil.getEleFromAry(txnFileNameEle, 1);
                if (!StringUtils.upperCase(countryCode).equals(countryCode)) {
                    this.addError(RErrorType.FILE_COUNTRYCODE_CASE,ReconUtil.buildErrStr(countryCode, StringUtils.upperCase(countryCode)));
                }
                if (StringUtils.isNotBlank(this.getCountry())) {
                    if (!this.getCountry().equals(countryCode)) {
                        this.addError(RErrorType.FILE_COUNTRYCODE_FORM, ReconUtil.buildErrStr(countryCode, this.getCountry()));
                    }
                } else {
                		//TODO: fetch all the country code and match in here.
                    this.setCountry(countryCode);
                }

                // #2.3 Network
                String networkCode = CommonUtil.getEleFromAry(txnFileNameEle, 2);
                if (StringUtils.isNotBlank(this.getNetwork())) {
                    if (!this.getNetwork().equals(networkCode)) {
                        this.addError(RErrorType.FILE_NETWORKCODE_FORM, ReconUtil.buildErrStr(networkCode, this.getNetwork()));
                    }
                } else {
                		//TODO: fetch all the network code and match in here.
                    this.setNetwork(countryCode);
                }

                // #2.4 Date
                String fileDateStr = CommonUtil.getEleFromAry(txnFileNameEle, 3);
                if (this.getTxnFileDate() != null) {
                		//convert to the date that matches the one provided in form.
                    Date fileDate = DateUtil.parseBasic(fileDateStr);
                    if (!this.getTxnFileDate().equals(fileDate)){
                    		String formFileDateStr= DateUtil.format(this.getTxnFileDate(), DateUtil.sdf__yyyyMMdd);
                        this.addError(RErrorType.FILE_DATE_FORM, ReconUtil.buildErrStr(fileDateStr, formFileDateStr));
                    }
                } else {
                		//convert date to the expected format 
                		Date fileDate = DateUtil.parse(fileDateStr, DateUtil.sdf__yyyyMMdd);
                		if(fileDate == null){
                			this.addError(RErrorType.FILE_DATE_WRONG_FROMAT, ReconUtil.buildErrStr(fileDateStr, DateUtil.sdf__yyyyMMdd.toPattern()));
                		}else{
                			this.setTxnFileDate(DateUtil.parse(fileDateStr, DateUtil.sdf__yyyyMMdd));
                		}
                }

                // #2.5 Version. Expected "v1_1-1"
                String fileVersionMain = StringUtils.defaultString(CommonUtil.getEleFromAry(txnFileNameEle, 4), "");
                int versionPrefixCount= StringUtils.countMatches(fileVersionMain, "v");
                if(versionPrefixCount!=1){
                		this.addError(RErrorType.FILE_VERSION_FAULTY_PREFIX, ReconUtil.buildErrStr(fileVersionMain, "v1_1-1"));
                }
                String fileVersionSub = StringUtils.defaultString(CommonUtil.getEleFromAry(txnFileNameEle, 5), "");
                int versionHyphenCount= StringUtils.countMatches(fileVersionSub, "-");
                if(versionHyphenCount!=1){
                		this.addError(RErrorType.FILE_VERSION_FAULTY_HYPHEN, ReconUtil.buildErrStr(fileVersionMain, "v1_1-1"));
                }
                String fileVersion = fileVersionMain + "_" + fileVersionSub;
                if (StringUtils.isNotBlank(this.getTxnFileVersion())) {
                    if (!this.getTxnFileVersion().equals(fileVersion)) {
                        this.addError(RErrorType.FILE_VERSION_FORM, ReconUtil.buildErrStr(fileVersion, this.getTxnFileVersion()));
                    }
                } else {
                    this.setTxnFileVersion(fileVersion);
                }            		
            }else {
            		this.addError(RErrorType.FILE_NAME_FAULTY, ReconUtil.buildErrStr(txnFileName, "transactions_<CC>_<NETWORK>_<yyyyMMdd>_v1_1-1"));
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
        this.validateTxn();
        if (this.getExecFile() != null) {
            this.validateExceptions();
        }
        return this.getErrors().isEmpty() && this.isRowDataCorrect();
    }

    /**
     * Input sample: 2016-04-18 00:00:00 - 23:59:59 UTC
     * Input sample: YYY-MM-DD 00:00:00 - HH:mm:ss UTC
     *
     * Validations: 
     * 1. Date and time range should be in UTC format (e.g. 2014-01-01 00:00:00 – 23:59:59 UTC). 
     * 2. There should be a space on either side of the hyphen between the timestamps 
     * 3. The date should only be reported once 
     * 4. "UTC" should be included at the end of the header after the second time
     */
    protected void validateHeader() {
    		//TODO: add regex check.
        String header = this.getTxnFileHeader();
        if (StringUtils.isNotBlank(header)) {
        		// UTC
            if (!header.contains("UTC")) {
                this.addError(RErrorType.HEADER_MISSING_UTC_FLAG, ReconUtil.buildErrStr(this.getTxnFileHeader(), ReconUtil.HEADER_FROMAT));
            }
            //'00:00:00' ele
            if (!header.contains("00:00:00")) {
                this.addError(RErrorType.HEADER_MISSING_000000, ReconUtil.buildErrStr(this.getTxnFileHeader(), ReconUtil.HEADER_FROMAT));
            }
            int headerHyphenCount= StringUtils.countMatches(header, "-");
            if(headerHyphenCount!=3) {
            		this.addError(RErrorType.HEADER_HYPHEN, ReconUtil.buildErrStr(this.getTxnFileHeader(), ReconUtil.HEADER_FROMAT));
            }
            
            // date/time ele's 
            // there 2 parts in header. 1st date. 2nd time. if, header doesnt contain ' - ', probably it doesnt have both ele's.
            if (!header.contains(" - ")) {
                // Expected: "YYYY-MM-DD 00:00:00 - HH:mm:ss UTC"
                this.addError(RErrorType.HEADER_REQ_SPACE, ReconUtil.buildErrStr(this.getTxnFileHeader(), ReconUtil.HEADER_FROMAT));

                String[] headerEles = header.split(" ");
                if (headerEles.length >= 2) {
                    // date >> Expected: "YYYY-MM-DD 00:00:00"
                		// Expected: "yyyy-MM-dd"
                    String headerDatePart1Str = CommonUtil.getEleFromAry(headerEles, 0);
                    // Expected: "00:00:00"
                    String headerDatePart2Str = CommonUtil.getEleFromAry(headerEles, 1);
                    String headerDateStr= headerDatePart1Str + headerDatePart2Str;
                    Date headerDate = DateUtil.parse(headerDateStr);
                    if (headerDate == null) {
                        this.addError(RErrorType.HEADER_DATE_PART, ReconUtil.buildErrStr(headerDateStr, ReconUtil.HEADER_DATE_FROMAT));
                    }
                    // time > Expected "HH:mm:ss"
                    String headerTimEleStr= headerEles[headerEles.length-2];
                    if (headerTimEleStr.split(":").length != 3) {
                        this.addError(RErrorType.HEADER_TIME_PART, ReconUtil.buildErrStr(headerTimEleStr, ReconUtil.HEADER_TIME_FROMAT));
                    }
                } else {
                    this.addError(RErrorType.HEADER, ReconUtil.buildErrStr(this.getTxnFileHeader(), ReconUtil.HEADER_FROMAT));
                }
            } else {
                String[] headerEle = header.split(" - ");
                
                // date
                String headerDateStr= CommonUtil.getEleFromAry(headerEle, 0);
                Date headerDate = DateUtil.parse(headerDateStr);
                if (headerDate == null) {
                	this.addError(RErrorType.HEADER_DATE_PART, ReconUtil.buildErrStr(headerDateStr, ReconUtil.HEADER_DATE_FROMAT));
                }
                // time
                String headerTimeEleStr = headerEle[1];
                String headerTimeStr= headerTimeEleStr.replace(" UTC", "");
                if (headerTimeEleStr.split(":").length != 3) {
                		this.addError(RErrorType.HEADER_TIME_PART, ReconUtil.buildErrStr(headerTimeStr, ReconUtil.HEADER_TIME_FROMAT));
                }
            }

            // After every other validation, check for the length of header.
            if (header.length() != 34) {
            		System.out.println("["+header+"]");
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
    private boolean validateTxn() {
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
                                    new Err(RErrorType.TXN_TYPE_NOT_MATCHING,
                                    		ReconUtil.buildErrStr(transactionTypeStr, "charge, refund or reversal")));
                        } else {
                            txn.addError(TxnFileProp.transactionType.name(),
                                    new Err(RErrorType.TXN_TYPE_CASE_INCORRECT,
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
                                    new Err(RErrorType.TXN_OP_STATUS_NOT_MATCHING,
                                    		ReconUtil.buildErrStr(opTxnStatusStr, "success or failed")));
                        } else {
                            txn.addError(TxnFileProp.operatorTransactionStatus.name(),
                                    new Err(RErrorType.TXN_OP_STATUS_CASE_INCORRECT,
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
                                RErrorType.TXN_TOTAL_AMOUNT_UNIT_ERROR, ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                    BigDecimal bigDecimal = new BigDecimal(totalAmountStr);
                    // TotalAmount: Should include 3 decimal places
                    int decimalPlaces = CommonUtil.getNumberOfDecimalPlaces001(totalAmountStr);
                    if (decimalPlaces != 3) {
                        txn.addError(TxnFileProp.totalAmount.name(), new Err(
                                RErrorType.TXN_TOTAL_AMOUNT_DECIMAL_PLACE, ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                    // TotalAmount: Should not be 0
                    if (bigDecimal.floatValue() <= 0) {
                        txn.addError(TxnFileProp.totalAmount.name(),
                                new Err(RErrorType.TXN_TOTAL_AMOUNT_GREATER_THAN_ZERO,
                                		ReconUtil.buildErrStr(totalAmountStr, null)));
                    }
                } catch (NumberFormatException e) {
                    // TotalAmount: Wrong format
                    txn.addError(TxnFileProp.totalAmount.name(), new Err(RErrorType.TXN_TOTAL_AMOUNT_FORMAT_ERROR,
                    		ReconUtil.buildErrStr(totalAmountStr, null)));
                }

                // 4. Timestamp
                if (txn.getTransactionTimestampObj() == null) {
                    txn.addError(TxnFileProp.transactionTimestamp.name(),
                            new Err(RErrorType.TXN_TOTAL_AMOUNT_FORMAT_ERROR,
                            		ReconUtil.buildErrStr(txn.getTransactionTimestamp(), "yyyy-MM-dd HH:mm:ss")));
                }

                // 5. Is having all the required filed
                if (txn.isEmptyData()) {
                		String description= "Missing: " + this.createCSV(txn.getMissingDataList());
                		Err err= new Err(RErrorType.MISSING_DATA.summary(), RErrorType.MISSING_DATA, description);
                    txn.addError(err);
                    this.addError(err);
                }

                // 6. Transactions should be listed in chronological order (oldest at the top, newest at the bottom)
                if (lastTxnDate == null) {
                    lastTxnDate = txn.getTransactionTimestampObj();
                } else {
                    if (lastTxnDate.compareTo(txn.getTransactionTimestampObj()) > 0) {
                        this.setOrderCorrect(false);
	                		Err err= new Err(RErrorType.TXN_ORDER_INCORRECT.summary(), RErrorType.TXN_ORDER_INCORRECT, null);
                        txn.addError(err);
                        this.addError(err);
                    } else {
                        lastTxnDate = txn.getTransactionTimestampObj();
                    }
                }
                boolean isTxnValid = txn.getErrors().isEmpty();
                if (isTxnColumnDataValid) {
                    isTxnColumnDataValid = isTxnValid;
                }
                txn.setValid(isTxnValid);
                if(!isTxnValid) {
                		this.plus1TxnErr();                	
                }
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

	public File getTxnFile() {
		return txnFile;
	}

	public void setTxnFile(File txnFile) {
		this.txnFile = txnFile;
	}

	public String getTxnFileHeader() {
		return txnFileHeader;
	}

	public void setTxnFileHeader(String txnFileHeader) {
		if(StringUtils.isNotBlank(txnFileHeader)) {
			txnFileHeader= txnFileHeader.trim();
		}
		this.txnFileHeader = txnFileHeader;
	}

	public String getTxnFileEtx() {
		return txnFileEtx;
	}

	public void setTxnFileEtx(String txnFileEtx) {
		this.txnFileEtx = txnFileEtx;
	}

	public String getTxnFileType() {
		return txnFileType;
	}

	public void setTxnFileType(String txnFileType) {
		this.txnFileType = txnFileType;
	}

	public String getTxnFileVersion() {
		return txnFileVersion;
	}

	public void setTxnFileVersion(String txnFileVersion) {
		this.txnFileVersion = txnFileVersion;
	}

	public Date getTxnFileDate() {
		return txnFileDate;
	}

	public void setTxnFileDate(Date txnFileDate) {
		this.txnFileDate = txnFileDate;
	}

	public List<Txn> getTxns() {
		return txns;
	}

	public void setTxns(List<Txn> txns) {
		this.txns = txns;
	}

	public Map<String, Txn> getTxnMap() {
		return txnMap;
	}

	public void setTxnMap(Map<String, Txn> txnMap) {
		this.txnMap = txnMap;
	}

	public File getExecFile() {
		return execFile;
	}

	public void setExecFile(File execFile) {
		this.execFile = execFile;
	}

	public List<RExecp> getExecs() {
		return execs;
	}

	public void setExecs(List<RExecp> execs) {
		this.execs = execs;
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

	public List<Err> getErrors() {
		return errors;
	}

	public void setErrors(List<Err> errors) {
		this.errors = errors;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTxnsInError() {
		return txnsInError;
	}

	public void setTxnsInError(int txnsInError) {
		this.txnsInError = txnsInError;
	}
}
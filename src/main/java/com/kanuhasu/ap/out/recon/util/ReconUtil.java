package com.kanuhasu.ap.out.recon.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.out.recon.bo.Err;
import com.kanuhasu.ap.out.recon.bo.RAnalysis;
import com.kanuhasu.ap.out.recon.bo.RErrorType;
import com.kanuhasu.ap.out.recon.bo.RExecp;
import com.kanuhasu.ap.out.recon.bo.Txn;
import com.kanuhasu.ap.out.recon.bo.TxnFileProp;

@Component
public class ReconUtil {
	private static final Logger logger = Logger.getLogger(ReconUtil.class);
	
	/**
	 * @param eAnalysis
	 */
	public void processTxn(RAnalysis eAnalysis) {
		List<Txn> txns = new ArrayList<Txn>();
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(eAnalysis.getTxnFile());
			bufferedReader = new BufferedReader(fileReader);
			String line;
			String header= null;
			while ((line = bufferedReader.readLine()) != null) {
				if(StringUtils.isBlank(header)){
					header= line;
				}else{
					Txn txn = this.processTxnInternal(line);
					txns.add(txn);		
					eAnalysis.getTxnMap().put(txn.getBokuTransactionId(), txn);
				}
			}	
			eAnalysis.setTxnFileHeader(header);
		}
		catch (Exception e) {
			logger.error(e);
		}
		finally {
			try {
				fileReader.close();
				bufferedReader.close();
			}
			catch (IOException e) {
				logger.error(e);
			}
		}
		eAnalysis.setTxns(txns);
	}
	
	private Txn processTxnInternal(String row) {
		//remove Unicode if any
		Txn txn = new Txn();
		txn.setRow(row);
		String[] eles = row.split(",", -1);
		/*
		 * There should be 10 ele's.
		 * But 9h and 10th ele's are account. Either Msisdn or ACR.
		 * */
		/**
		 * There should be 9 comma's on each row, which means there has to be 10 tokens. a token can be empty.
		 */
		if(eles.length < 10){
			txn.setMissingData(true);
		}
		for (int i = 0; i < eles.length; i++) {
			String val = eles[i];
			switch (i) {
				case 0:
					txn.setBokuTransactionId(val);
					break;
				case 1:
					txn.setOperatorTransactionId(val);
					break;
				case 2:
					txn.setTransactionTimestamp(val);
					Date date= DateUtil.parse(val);
					txn.setTransactionTimestampObj(date);
					break;
				case 3:
					txn.setTransactionType(val);
					break;
				case 4:
					txn.setOperatorTransactionStatus(val);
					break;
				case 5:
					txn.setOperatorOriginalTransactionId(val);
					break;
				case 6:
					txn.setTotalAmount(val);
					break;
				case 7:
					txn.setCurrencyCode(val);
					break;
				case 8:
					txn.setMobileNumber(val);
					break;
				case 9:
					txn.setAcr(val);
					break;
				default:
					logger.info("Found Extra field in TxnFile : " + val);
					break;
			}
		}
		txn.checkForEmptyData();
		
		return txn;
	}
	
	/**
	 * @param eAnalysis
	 */
	public void processExec(RAnalysis eAnalysis) {
		List<RExecp> execs = new ArrayList<RExecp>();
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(eAnalysis.getExecFile());
			bufferedReader = new BufferedReader(fileReader);
			String line;
			String header= null;
			while ((line = bufferedReader.readLine()) != null) {
				if(StringUtils.isBlank(header)){
					header= line;
				}else{
					RExecp exec = this.processExecInternal(line);
					execs.add(exec);					
				}
			}
			//eAnalysis.setTxnFileHeader(header);
		}
		catch (Exception e) {
			logger.error(e);
		}
		finally {
			try {
				fileReader.close();
				bufferedReader.close();
			}
			catch (IOException e) {
				logger.error(e);
			}
		}
		eAnalysis.setExecs(execs);
	}
	
	private RExecp processExecInternal(String row) {
		row= row.replaceAll("\\P{Print}","");
		RExecp exec = new RExecp();
		exec.setRow(row);
		String[] eles = row.split(",");
		if(eles.length < 25){
			exec.setMissingData(true);
		}		
		for (int i = 0; i < eles.length; i++) {
			String val = eles[i];
			switch (i) {
				case 0:
					exec.setMerchantId(val);
					break;
				case 1:
					exec.setTransactionType(val);
					break;
				case 2:
					exec.setBokuTransactionId(val);
					break;
				case 3:
					exec.setTransactionDate(val);
					break;
				case 4:
					exec.setTransactionTime(val);
					break;
				case 5:
					exec.setCountryCode(val);
					break;
				case 6:
					exec.setNetworkCode(val);
					break;
				case 7:
					exec.setMerchantTransactionId(val);
					break;
				case 8:
					exec.setOperatorTransactionId(val);
					break;
				case 9:
					exec.setProductDescription(val);
					break;
				case 10:
					exec.setMerchantData(val);
					break;
				case 11:
					exec.setCurrencyCode(val);
					break;
				case 12:
					exec.setTotalAmount(val);
					break;
				case 13:
					exec.setBokuOriginalTransactionId(val);
					break;
				case 14:
					exec.setOperatorOriginalTransactionId(val);
					break;
				case 15:
					exec.setMerchantOriginalTransactionId(val);
					break;
				case 16:
					exec.setOriginalTransactionDate(val);
					break;
				case 17:
					exec.setOriginalTransactionTime(val);
					break;
				case 18:
					exec.setRefundReasonCode(val);
					break;
				case 19:
					exec.setRefundSource(val);
					break;
				case 20:
					exec.setReconciliationStatus(val);
					break;
				case 21:
					exec.setReconciliationStatusDate(val);
					break;
				case 22:
					exec.setReconciliationStatusTime(val);
					break;
				case 23:
					exec.setBokuTransactionStatus(val);
					break;
				case 24:
					exec.setOperatorTransactionStatus(val);
					break;
				default:
					logger.info("Found Extra field in ExecFile : " + val);
					break;
			}
		}
		return exec;
	}
	
    /**
     */
    public static void compareTxnPropWithExcepProp(RExecp exec, String excepProp, String txnProp, TxnFileProp key, RErrorType error) {
        System.out.println(txnProp + " ==>>> " + excepProp);
        if (StringUtils.isNotBlank(txnProp) && StringUtils.isNotBlank(excepProp)) {
            if (!txnProp.equals(excepProp)) {
                exec.addError(key.name(), new Err(error, ReconUtil.buildErrStr(txnProp, excepProp)));
            }
        }
    }	
    
    /**
     * @param received
     * @param expected
     * @return
     */
    public static String buildErrStr(String received, String expected) {
        StringBuilder errorStr = new StringBuilder();
        errorStr.append("Received [").append(received).append("]");
        if (expected != null) {
            errorStr.append(", ");
            errorStr.append("Expected [").append(expected).append("]");
        }
        return errorStr.toString();
    }    
}
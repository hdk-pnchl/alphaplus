package com.kanuhasu.ap.out.recon.bo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class RAnalysisTest {
	@Mock
	private RAnalysis rAnalysisMack;
	
    @Test
    public void filename_success() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transactions_FR_02f802_20170429_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(rAnalysis.getErrors().isEmpty());
    }
    
    @Test
    public void filename_prefix() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transaction_FR_02f802_20170429_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());
    }   
    
    @Test
    public void filename_6ele() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transaction_FR_02f802_20170429_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());
    }  
    
    @Test
    public void filename_extension() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transaction_FR_qa_02f802_20170429_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());  
    }  
    
    @Test
    public void filename_countrycode() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transactions_fr_02f802_20170429_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());  
    } 
    
    @Test
    public void filename_date() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transactions_fr_02f802_20172904_v1_1-1.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());  
    }   
    
    @Test
    public void filename_version() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setFileName("transactions_fr_02f802_20172904_1_11.csv");
    		rAnalysis.validateFileName();    		
    		Assert.assertTrue(!rAnalysis.getErrors().isEmpty());  
    }  
    
    @Test
    public void header_() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.setTxnFileHeader("2016-04-18 00:00:00 - 23:59:59 UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(rAnalysis.getErrors().isEmpty());  
    }    
    
    @Test
    public void header_empty() {
    		RAnalysis rAnalysis= new RAnalysis();
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_MISSING));
    		
    		rAnalysis.setTxnFileHeader("2016-04-18 00:00:00 - 23:59:59");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_MISSING_UTC_FLAG));
    		
    		rAnalysis.setTxnFileHeader("2016-04-18 - 23:59:59 UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_MISSING_UTC_FLAG));
    		
    		rAnalysis.setTxnFileHeader("2016-04-18 00:00:00 23:59:59 UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_HYPHEN));

    		rAnalysis.setTxnFileHeader("2016-04-18 00:00:00 -23:59:59 UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_REQ_SPACE));

    		rAnalysis.setTxnFileHeader("2016-04-1800:00:00-23:59:59UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER));

    		rAnalysis.setTxnFileHeader("2016-18-04 00:00:00 - 23::59:59 UTC");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_DATE_PART));
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_TIME_PART));
    		
    		rAnalysis.setTxnFileHeader("2016-04-18 00:00:00 - 23:59:59 UTC 333");
    		rAnalysis.validateHeader();
    		Assert.assertTrue(this.matchError(rAnalysis, RErrorType.HEADER_FROMAT_INVALUD_LENGTH));
    }  
    
    private boolean matchError(RAnalysis rAnalysis, RErrorType eType) {
    		for(Err err: rAnalysis.getErrors()) {
    			if(err.getSummary().equals(eType.summary())) {
    				return true;
    			}
    		}
    		return false;
    }
    
    
    
    
    
    
    
    
    
}

package com.kanuhasu.ap.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Alert;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.out.recon.bo.RAnalysis;
import com.kanuhasu.ap.out.recon.util.ReconUtil;

@CrossOrigin
@Controller
@RequestMapping("/recon")
public class ReconController implements ResourceLoaderAware {
	
	/** ------------| instance |------------**/
	
	@Autowired
	private ReconUtil reconUtil;
	@Autowired
	private ObjectMapper objectMapper;
	private ResourceLoader resourceLoader;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource formData = this.resourceLoader.getResource("classpath:data/json/test/reconFormData.json");
		Map<String, Object> reconFormDataMap = objectMapper.readValue(formData.getFile(), Map.class);
		return reconFormDataMap;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getErrColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getErrColumnData() throws IOException {
		Resource columnJson = this.resourceLoader.getResource("classpath:data/json/test/reconColumnData.json");
		List<Object> columnData = objectMapper.readValue(columnJson.getFile(), List.class);
		return columnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTxnColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getTxnColumnData() throws IOException {
		Resource columnJson = this.resourceLoader.getResource("classpath:data/json/test/reconTxnColumnData.json");
		List<Object> columnData = objectMapper.readValue(columnJson.getFile(), List.class);
		return columnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getExcepColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getExcepColumnData() throws IOException {
		Resource columnJson = this.resourceLoader.getResource("classpath:data/json/test/reconExcepColumnData.json");
		List<Object> columnData = objectMapper.readValue(columnJson.getFile(), List.class);
		return columnData;
	}
	
	@RequestMapping(value = "/saveOrUpdate1", method = RequestMethod.POST)
	@ResponseBody
	public Response saveOrUpdate(RAnalysis eAnalysis){
		Response response= null;
		return response;
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Response saveOrUpdate(HttpServletRequest request, 
			@RequestParam(value = "fileType", required = false) String fileType,
			@RequestParam(value = "fileEtx", required = false) String fileEtx,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "network", required = false) String network,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "txnDate", required = false) Date txnDate,
			@RequestParam(value = "txnFile", required = false) MultipartFile txnFile,
			@RequestParam(value = "excepFile", required = false) MultipartFile excepFile){
		Response response= null;
		try {
			File txnF = this.downloadFile(txnFile);
			File excepF = this.downloadFile(excepFile);

			RAnalysis eAnalysis = RAnalysis.builder()
					.country(country)
					.network(network)
					
					.txnFile(txnF)
					.txnFileEtx(fileEtx)
					.txnFileType(fileType)
					.txnFileVersion(version)
					.txnFileDate(txnDate)

					.execFile(excepF)
					.fileName(txnF.getName())
					.build();	
			
			boolean isReconValid= eAnalysis.validateFileName();
			if(isReconValid){
				//build txn's and exception's
				reconUtil.processTxn(eAnalysis);
				if(excepFile!=null){
					reconUtil.processExec(eAnalysis);	
				}
				isReconValid= eAnalysis.validateFileInternals();
			}
			response= Response.build(isReconValid, eAnalysis);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			response = Response.Fail();
			response.addAlert(Alert.danger("File Not Found"));
		}
		catch (IOException e) {
			e.printStackTrace();
			response = Response.Fail();
			response.addAlert(Alert.danger("IO Error"));
		}	
		catch (Exception e) {
			e.printStackTrace();
			response = Response.Fail();
			response.addAlert(Alert.danger("Something went wrong"));
		}
		finally {
			this.emptyTargetDir();
		}
		
		return response;
	}
	
	// business
	private File downloadFile(MultipartFile ipMFile) throws IOException{
		File file= null;
		if(ipMFile != null){
			File targetDir= this.fetchTargetDir();
			if(targetDir != null){
				file = new File(this.buildFileName(targetDir, ipMFile));
				BufferedOutputStream txnFileStream = new BufferedOutputStream(new FileOutputStream(file));
				txnFileStream.write(ipMFile.getBytes());
				txnFileStream.close();					
			}
		}
		return file;
	}
	
	private File fetchTargetDir(){
		String rootPath = System.getProperty("catalina.home");
		StringBuilder fileNameBuilder= new StringBuilder()
				.append(rootPath)
				.append(File.separator)
				.append("reconToolFiles");
		File targetDir = new File(fileNameBuilder.toString());
		if(!targetDir.exists())
			targetDir.mkdirs();		
		return targetDir;
	}
	
	private void emptyTargetDir(){
		File targetDir= this.fetchTargetDir();
		targetDir.delete();
	}
	
	private String buildFileName(File targetDir, MultipartFile ipMFile){
		StringBuilder fileNameBuilder= new StringBuilder();
		if(targetDir != null && ipMFile != null){
			fileNameBuilder
			.append(targetDir.getAbsolutePath())
			.append(File.separator)
			.append(ipMFile.getOriginalFilename());			
		}
		return fileNameBuilder.toString();
	}
}
package com.nanites.alphaplus.business.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchInput implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6277787184368503155L;
	
	private int pageNo;
	private int rowsPerPage;
	private Map<String, String> searchData = new HashMap<String, String>();
	
	public int getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
	public Map<String, String> getSearchData() {
		return searchData;
	}
	
	public void setSearchData(Map<String, String> searchData) {
		this.searchData = searchData;
	}
}

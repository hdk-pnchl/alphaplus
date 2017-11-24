package com.kanuhasu.ap.out.recon.bo;

/**
 * @author hpanchal
 */
public class Err {

	/** ------------| instance |------------ **/

	private String key;
	private String type;
	private String summary;
	private String desc;

	/** ------------| Constructor |------------ **/

	public Err() {
	}

	public Err(RErrorType errorT, String desc) {
		super();
		this.type = errorT.type();
		this.summary = errorT.summary();
		this.desc = desc;
	}
	
	public Err(String key, RErrorType errorT, String desc) {
		super();
		this.key= key;
		this.type = errorT.type();
		this.summary = errorT.summary();
		this.desc = desc;
	}	

	public Err(String key, String type, String summary, String desc) {
		super();
		this.key = key;
		this.type = type;
		this.summary = summary;
		this.desc = desc;
	}

	/** ------------| Getter-Setter |------------ **/

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}

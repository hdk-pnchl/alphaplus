package com.kanuhasu.ap.business.bo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kanuhasu.ap.business.bo.job.LastUpdateEntity;

@Entity
@Table(name = "Message")
public class MessageEntity extends LastUpdateEntity implements Serializable {
	/** ------------| instance |------------ **/

	private static final long serialVersionUID = -3439236310912778185L;
	private String name;
	private String emailID;
	private String message;
	private String reply;

	/** ------------| Setter-Getter |------------ **/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
}
package com.suneee.openfire.plugin;

import java.util.Date;

import org.jivesoftware.openfire.user.User;

public class SuneeeUser extends User {
	
	private String nickName;

	public SuneeeUser() {
		super();
    }
	
	public SuneeeUser(String username, String name, String nickName, String email, Date creationDate,
            Date modificationDate) {
		super(username, name, email, creationDate, modificationDate);
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}

package com.cxic.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="passport")
public class Passport implements Serializable{

	private Long id;
	private String username;
	private String password;
	private  Boolean loginFlag=false;
	private  Integer uid=0;
	private  String nickname;
	private  Integer usertype=0;
	private  String email;
	private  Date urb;
	private  String mac;
	
	
	@Override
	public String toString() {
		return "Passport [username=" + username + ", password=" + password + ", loginFlag=" + loginFlag + ", uid=" + uid
				+ ", nickname=" + nickname + ", usertype=" + usertype + ", email=" + email + ", urb=" + urb + ", mac="
				+ mac + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Passport other = (Passport) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(length=20)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(length=20)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(nullable=false,columnDefinition="bit(1) default 0")
	public Boolean isLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(Boolean loginFlag) {
		this.loginFlag = loginFlag;
	}
	@Column(nullable=false,columnDefinition="int(11) default 0")
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	@Column(length=20)
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Column(nullable=false,columnDefinition="int(11) default 0")
	public Integer getUsertype() {
		return usertype;
	}
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	@Column(length=20)	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column
	public Date getUrb() {
		return urb;
	}
	public void setUrb(Date urb) {
		this.urb = urb;
	}
	@Column(length=20)	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

}

package org.daoefang.b2c.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.daoefang.b2c.utils.Helper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "username", "password", "captcha" })
public class LoginCredential {

	protected String username;
	protected String password;
	protected String captcha;
	@XmlAttribute(name = "name")
	protected String name;

	public LoginCredential randomize() {
		if (this.username != null)
			this.username = SystemData.PREFIX + "_user_" + Helper.randomize();
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String value) {
		this.username = value;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String value) {
		this.captcha = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

}
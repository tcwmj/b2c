//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.21 at 07:52:50 PM CST 
//

package org.daoefang.b2c.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.daoefang.b2c.utils.Helper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "username", "password", "confirmPassword",
		"phoneNumber", "sex", "birthday", "email", "qq", "points",
		"experience", "registrationDate" })
@XmlRootElement(name = "member")
public class Member {

	protected String username;
	protected String password;
	protected String confirmPassword;
	protected String phoneNumber;
	protected Boolean sex;
	protected String birthday;
	protected String email;
	protected String qq;
	protected String points;
	protected String experience;
	protected String registrationDate;
	@XmlAttribute(name = "name")
	protected String name;

	public Member randomize() {
		if (this.username != null)
			this.username = Helper.randomize();
		if (this.phoneNumber != null)
			this.phoneNumber = Helper.getRandomPhoneNumber();
		if (this.email != null)
			this.email = Helper.getRandomEmail(6, 19);
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String value) {
		this.confirmPassword = value;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String value) {
		this.phoneNumber = value;
	}

	public Boolean isSex() {
		return sex;
	}

	public void setSex(Boolean value) {
		this.sex = value;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String value) {
		this.birthday = value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String value) {
		this.qq = value;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String value) {
		this.points = value;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String value) {
		this.experience = value;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String value) {
		this.registrationDate = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

}

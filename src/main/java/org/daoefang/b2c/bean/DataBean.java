package org.daoefang.b2c.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Kenny Wang
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "field", "loginCredential", "member",
		"password" })
@XmlRootElement(name = "dataBean")
public class DataBean {

	protected List<Field> field;
	protected List<LoginCredential> loginCredential;
	protected List<Member> member;
	protected List<Password> password;

	public List<Field> getField() {
		if (field == null) {
			field = new ArrayList<Field>();
		}
		return this.field;
	}

	public Field getField(String name) {
		for (Field element : this.getField()) {
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	public Field setField(String name) {
		Field element = getField(name);
		if (element == null) {
			element = new Field();
			element.setName(name);
			this.getField().add(element);
		}
		return element;
	}

	public List<LoginCredential> getLoginCredential() {
		if (loginCredential == null) {
			loginCredential = new ArrayList<LoginCredential>();
		}
		return this.loginCredential;
	}

	public LoginCredential getLoginCredential(String name) {
		for (LoginCredential element : this.getLoginCredential()) {
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	public LoginCredential setLoginCredential(String name) {
		LoginCredential element = getLoginCredential(name);
		if (element == null) {
			element = new LoginCredential();
			element.setName(name);
			this.getLoginCredential().add(element);
		}
		return element;
	}

	public List<Member> getMember() {
		if (member == null) {
			member = new ArrayList<Member>();
		}
		return this.member;
	}

	public Member getMember(String name) {
		for (Member element : this.getMember()) {
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	public Member setMember(String name) {
		Member element = getMember(name);
		if (element == null) {
			element = new Member();
			element.setName(name);
			this.getMember().add(element);
		}
		return element;
	}

	public List<Password> getPassword() {
		if (password == null) {
			password = new ArrayList<Password>();
		}
		return this.password;
	}

	public Password getPassword(String name) {
		for (Password element : this.getPassword()) {
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	public Password setPassword(String name) {
		Password element = getPassword(name);
		if (element == null) {
			element = new Password();
			element.setName(name);
			this.getPassword().add(element);
		}
		return element;
	}
}

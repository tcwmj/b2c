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
@XmlType(name = "", propOrder = { "loginCredential", "field" })
@XmlRootElement(name = "dataBean")
public class DataBean {

	protected List<LoginCredential> loginCredential;
	protected List<Field> field;

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

}

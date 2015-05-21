package org.daoefang.b2c.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.daoefang.b2c.utils.Helper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class Field {

	@XmlValue
	protected String value;
	@XmlAttribute(name = "name")
	protected String name;

	public Field randomize() {
		if (this.value != null)
			this.value = SystemData.PREFIX + "_field_" + Helper.randomize();
		return this;
	}

	public Field(String value) {
		super();
		this.value = value;
	}

	public Field() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

}

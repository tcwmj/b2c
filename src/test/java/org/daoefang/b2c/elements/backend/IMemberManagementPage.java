package org.daoefang.b2c.elements.backend;

import org.openqa.selenium.By;

public interface IMemberManagementPage extends IBackendPage {

	public static final By ADD_MEMBER = By.id("addrow");
	public static final By USERNAME = By.name("username");
	public static final By MOBILE_PHONE = By.name("mobile_phone");
	public static final By MAN = By
			.xpath("//label[contains(text(),'男')]//input");
	public static final By WOMAN = By
			.xpath("//label[contains(text(),'女')]//input");
	public static final By BIRTHDAY = By.name("birthday");
	public static final By EMAIL = By.name("email");
	public static final By QQ = By.name("qq");
	public static final By SUBMIT = By.className("button_search");
}

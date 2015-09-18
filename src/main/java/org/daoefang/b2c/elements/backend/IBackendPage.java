package org.daoefang.b2c.elements.backend;

import org.daoefang.b2c.elements.IPage;
import org.openqa.selenium.By;

public interface IBackendPage extends IPage {
	public static final By CHANGE_PASSWORD = By.linkText("更改密码");
	public static final By LOGOUT = By.linkText("退出系统");
	public static final By MAIN_FRAME = By.id("mainFrame");

	public static final By OLD_PASSWORD = By.name("opassword");
	public static final By NEW_PASSWORD = By.name("password");
	public static final By CONFIRM_PASSWORD = By.name("npassword");
	public static final By EMAIL = By.name("email");
	public static final By SUBMIT = By.className("button_search");

	public static final By LEFT_SIDE = By.className("side");
}

package org.daoefang.b2c.elements.backend;

import org.daoefang.b2c.elements.IPage;
import org.openqa.selenium.By;

public interface IBackendPage extends IPage {
	public static final By LOGOUT = By.linkText("退出系统");
}

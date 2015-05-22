package org.daoefang.b2c.pages.backend;

import org.daoefang.b2c.bean.Field;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.elements.backend.IBackendPage;
import org.daoefang.b2c.pages.Page;
import org.daoefang.b2c.utils.selenium.Driver;
import org.openqa.selenium.By;

public class BackendPage extends Page implements IBackendPage {

	public BackendPage(Driver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 导航到后台管理员地址
	 */
	public void navigateTo() {
		navigateTo(driver.backend_url);
	}

	/**
	 * 切换到后台相应的功能模块
	 * 
	 * @param field
	 */
	public void switchTo(Field field) {
		String[] layer = field.getValue().split(SystemData.SEPARATOR);
		for (int i = 0; i < layer.length; i++) {
			driver.click(By.linkText(layer[i]));
		}
	}

	public void switchToDefault() {
		driver.switchToDefault();
	}

	public void switchToFrame() {
		driver.switchToFrame(MAIN_FRAME);
	}

	public void logout() {
		driver.click(LOGOUT);
	}
}

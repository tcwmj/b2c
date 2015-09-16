package org.daoefang.b2c.pages.backend;

import org.daoefang.b2c.bean.Field;
import org.daoefang.b2c.bean.Password;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.elements.backend.IBackendPage;
import org.daoefang.b2c.pages.Page;
import org.daoefang.b2c.selenium.Driver;
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

	/**
	 * 更改后台系统管理员密码
	 * 
	 * @param password
	 */
	public void changePassword(Password password) {
		logger.debug("trying to change admin's password with a new password "
				+ password);
		driver.click(CHANGE_PASSWORD);
		switchToFrame();
		if (password.getOldPassword() != null)
			driver.input(OLD_PASSWORD, password.getOldPassword());
		if (password.getNewPassword() != null)
			driver.input(NEW_PASSWORD, password.getNewPassword());
		if (password.getConfirmPassword() != null)
			driver.input(CONFIRM_PASSWORD, password.getConfirmPassword());
		if (password.getEmail() != null)
			driver.input(EMAIL, password.getEmail());
		driver.click(SUBMIT);
		driver.acceptAlert();
		switchToDefault();
	}
}

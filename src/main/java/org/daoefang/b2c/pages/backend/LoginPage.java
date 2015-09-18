package org.daoefang.b2c.pages.backend;

import org.daoefang.b2c.bean.LoginCredential;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.elements.backend.ILoginPage;
import org.daoefang.b2c.selenium.Driver;

/**
 * @author Kenny Wang
 * 
 */
public final class LoginPage extends BackendPage implements ILoginPage {

	public LoginPage(Driver driver) {
		super(driver);
	}

	/**
	 * 位于电商后台管理系统页面，进行管理员登录操作
	 * 
	 * @param lc
	 *            用户登录信息
	 */
	public void login(LoginCredential lc) {
		logger.debug("trying to login with specified credential");
		if (lc.getUsername() != null)
			driver.input(USERNAME, lc.getUsername());
		if (lc.getPassword() != null)
			driver.input(PASSWORD, lc.getPassword());
		if (lc.getCaptcha() != null)
			driver.input(CAPTCHA, lc.getCaptcha());
		driver.click(LOGIN);
	}

	/**
	 * silent login with specified account without given captcha
	 */
	public void silentLogin(LoginCredential lc) {
		lc.setCaptcha(driver.getCookieNamed("verify"));
		login(lc);
	}

	/**
	 * silent login with admin account without given captcha
	 */
	public void silentLogin() {
		silentLogin(SystemData.ADMIN);
	}

	@Override
	public void logout() {
	}
}

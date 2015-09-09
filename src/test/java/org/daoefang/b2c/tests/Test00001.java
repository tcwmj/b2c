package org.daoefang.b2c.tests;

import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.Test;

/**
 * 验证码的解决方案
 * 
 * @author Kenny Wang
 */
public class Test00001 extends TestCase {

	/**
	 * @author Kenny Wang
	 */
	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	/**
	 * @author Kenny Wang
	 */
	@Test(description = "使用万能验证码登录")
	public void step020() {
		backend.home().login(ADMIN);
		backend.siteConfiguration().logout();
	}

	/**
	 * @author Kenny Wang
	 */
	@Test(description = "使用后台验证码接口登录")
	public void step030() {
		backend.home().silentLogin();
		backend.siteConfiguration().logout();
	}

	/**
	 * @author Kenny Wang
	 */
	@Test(description = "使用错误验证码登录")
	public void step040() {
		backend.home().login(td.getLoginCredential("invalid"));
		backend.home().assertTextDisplayed(INVALID_CAPTCHA.getValue(), true);
	}
}
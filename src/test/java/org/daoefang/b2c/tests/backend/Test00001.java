package org.daoefang.b2c.tests.backend;

import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.tests.TestCase;
import org.testng.annotations.Test;

/**
 * @author Kenny Wang
 * 
 */
public class Test00001 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	@Test(description = "使用万能验证码登录")
	public void step020() {
		backend.home().login(SystemData.ADMIN);
		backend.siteConfiguration().logout();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step030() {
		backend.home().silentLogin();
		backend.siteConfiguration().logout();
	}

	@Test(description = "使用错误验证码登录")
	public void step040() {
		backend.home().login(td.getLoginCredential("invalid"));
		backend.home().assertTextDisplayed("验证码不正确", true);
	}
}
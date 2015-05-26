package org.daoefang.b2c.tests;

import org.daoefang.b2c.elements.backend.IBackendPage;
import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.Test;

/**
 * CSS属性验证解决方案
 * 
 * @author Kenny Wang
 */
public class Test00005 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() {
		backend.home().silentLogin();
	}

	@Test(description = "验证左侧导航菜单背景色")
	public void step030() {
		driver.assertBackgroundColor(IBackendPage.LEFT_SIDE, "#417eb7");
	}

	@Test(description = "退出登录")
	public void step040() {
		backend.siteConfiguration().logout();
	}
}
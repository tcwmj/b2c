package org.daoefang.b2c.tests;

import org.daoefang.b2c.elements.backend.IMemberManagementPage;
import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.Test;

/**
 * 自定义控件解决方案
 * 
 * @author Kenny Wang
 */
public class Test00006 extends TestCase {

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
		backend.siteConfiguration().switchTo(MEMEBER_LIST);
		backend.memberManagement().switchToFrame();
		driver.click(IMemberManagementPage.ADD_MEMBER);
		backend.memberManagement().setMemberBirthday("2014-06-28");
		driver.assertAttribute(IMemberManagementPage.BIRTHDAY, "value",
				"2014-06-28");

		backend.memberManagement().setMemberBirthday("1990-01-21");
		driver.assertAttribute(IMemberManagementPage.BIRTHDAY, "value",
				"1990-01-21");

		backend.memberManagement().setMemberBirthday("2055-12-01");
		driver.assertAttribute(IMemberManagementPage.BIRTHDAY, "value",
				"2055-12-01");

		backend.memberManagement().switchToDefault();
	}

	@Test(description = "退出登录")
	public void step040() {
		backend.siteConfiguration().logout();
	}
}
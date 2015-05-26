package org.daoefang.b2c.tests;

import org.daoefang.b2c.elements.frontend.IHomePage;
import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.Test;

/**
 * 鼠标移动的解决方案
 * 
 * @author Kenny Wang
 */
public class Test00004 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		frontend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() {
		driver.moveTo(IHomePage.PHONE_DIGITAL_COMMUNICATION);
		// driver.fireEvent(IHomePage.PHONE_DIGITAL_COMMUNICATION,
		// TRIGGER_EVENT_MOUSEOVER);
		driver.click(IHomePage.PHONE);
		driver.assertTitle("手机 - 道e坊商城 - Powered by Haidao");
	}

	@Test(description = "批量添加新会员并且验证添加成功")
	public void step030() {

	}

	@Test(description = "退出登录")
	public void step040() {
	}
}
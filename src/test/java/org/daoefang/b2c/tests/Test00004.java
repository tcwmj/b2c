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

	@Test(description = "导航到前台页面")
	public void step010() {
		frontend.home().navigateTo();
	}

	@Test(description = "导航到手机商品列表页面")
	public void step020() {
		driver.moveTo(IHomePage.PHONE_DIGITAL_COMMUNICATION);
		// driver.fireEvent(IHomePage.PHONE_DIGITAL_COMMUNICATION,
		// TRIGGER_EVENT_MOUSEOVER);
		driver.click(IHomePage.PHONE);
		driver.assertTitle("手机 - 道e坊商城 - Powered by Haidao");
	}

}
package org.daoefang.b2c.tests.backend;

import org.daoefang.b2c.elements.frontend.IGoodPage;
import org.daoefang.b2c.tests.TestCase;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

/**
 * @author Kenny Wang
 * 
 */
public class Test00003 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() {
		backend.home().silentLogin();
	}

	@Test(description = "重置admin密码，处理Alert弹出框")
	public void step030() {
		backend.siteConfiguration().changePassword(td.getPassword("admin"));
	}

	@Test(description = "退出登录")
	public void step040() {
		backend.siteConfiguration().logout();
	}

	@Test(description = "导航到前台界面")
	public void step050() {
		frontend.home().navigateTo();
	}

	@Test(description = "添加购物车并清空处理Confirm弹出框")
	public void step060() {
		driver.click(By.xpath(td.getField("iphone6").getValue()));
		driver.click(IGoodPage.ADD_TO_CART);
		driver.click(IGoodPage.GOTO_SHOPPING_CART);
		driver.click(IGoodPage.CLEAR_SHOPPING_CART);
		driver.dismissAlert();
	}

}
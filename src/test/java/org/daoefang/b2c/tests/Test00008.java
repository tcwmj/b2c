package org.daoefang.b2c.tests;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.daoefang.b2c.utils.TestCase;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

/**
 * 右键操作解决方案
 * 
 * @author Kenny Wang
 */
public class Test00008 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		frontend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() throws InterruptedException, AWTException {
		By by = By
				.xpath("//img[@src='/uploadfile/image/20150519/1432027851524213.jpg']");
		driver.contextClick(by);

		Thread.sleep(5000);

		Robot robot = new Robot();

		// This will bring the selection down one by one
		robot.keyPress(KeyEvent.VK_DOWN);
		Thread.sleep(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		Thread.sleep(1000);
		// robot.keyPress(KeyEvent.VK_DOWN);
		// Thread.sleep(1000);
		// robot.keyPress(KeyEvent.VK_DOWN);
		// Thread.sleep(1000);

		// robot.keyPress(KeyEvent.VK_DOWN);

		// Thread.sleep(1000);

		// This is to release the down key, before this enter will not work
		// robot.keyRelease(KeyEvent.VK_DOWN);
		// Thread.sleep(1000);
		robot.keyPress(KeyEvent.VK_ENTER);
		driver.switchToWindow();
		Thread.sleep(5000);
	}

}
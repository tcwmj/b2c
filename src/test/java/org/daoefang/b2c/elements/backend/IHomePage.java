package org.daoefang.b2c.elements.backend;

import org.openqa.selenium.By;

/**
 * @author Kenny Wang
 * 
 */
public interface IHomePage extends IBackendPage {
	/**
	 * 登录用户名输入框
	 */
	public static final By USERNAME = By.name("username");
	/**
	 * 登录密码输入框
	 */
	public static final By PASSWORD = By.name("userpass");
	/**
	 * 登录验证码输入框
	 */
	public static final By CAPTCHA = By.name("userverify");
	/**
	 * 登录按钮
	 */
	public static final By LOGIN = By.xpath("//input[@type='submit']");
}

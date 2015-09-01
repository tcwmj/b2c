package org.daoefang.b2c.pages;

import org.daoefang.b2c.elements.IPage;
import org.daoefang.b2c.selenium.Driver;
import org.openqa.selenium.By;

public class Page implements IPage {
	protected Driver driver;

	public Page(Driver driver) {
		this.driver = driver;
	}

	/**
	 * navigate to a specified url
	 * 
	 * @param url
	 */
	protected void navigateTo(String url) {
		driver.navigateTo(url);
	}

	/**
	 * assert such locator displayed on the web or not
	 * 
	 * @param by
	 * @param displayed
	 */
	public void assertDisplayed(By by, Boolean displayed) {
		driver.assertDisplayed(by, displayed);
	}

	/**
	 * assert such locator's html text is correct
	 * 
	 * @param by
	 * @param text
	 */
	public void assertText(By by, String text) {
		driver.assertText(by, text);
	}

	/**
	 * assert such text could be selected in the web list element
	 * 
	 * @param by
	 * @param text
	 */
	public void assertTextSelectable(By by, String text) {
		driver.assertTextSelectable(by, text);
	}

	/**
	 * assert page's title
	 * 
	 * @param title
	 */
	public void assertTitle(String title) {
		driver.assertTitle(title);
	}

	/**
	 * assert selected value in the web list element
	 * 
	 * @param by
	 * @param value
	 */
	public void assertSelectedValue(By by, String text) {
		driver.assertSelectedValue(by, text);
	}

	/**
	 * assert page source contains such text
	 * 
	 * @param text
	 * @param displayed
	 */
	public void assertTextDisplayed(String text, Boolean displayed) {
		driver.assertTextDisplayed(text, displayed);
	}

	/**
	 * dismiss the alert window
	 */
	public void dismissAlert() {
		driver.dismissAlert();
	}

	/**
	 * accept the alert window
	 */
	public void acceptAlert() {
		driver.acceptAlert();
	}

	/**
	 * get text from alert window
	 * 
	 * @return
	 */
	public String getAlertText() {
		return driver.getAlertText();
	}

	/**
	 * assert text on alert window
	 * 
	 * @param text
	 */
	public void assertAlertText(String text) {
		driver.assertAlertText(text);
	}
}

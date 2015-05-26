package org.daoefang.b2c.elements.frontend;

import org.openqa.selenium.By;

/**
 * @author Kenny Wang
 * 
 */
public interface IGoodPage extends IFrontendPage {
	public static final By ADD_TO_CART = By.id("joinCarButton");
	/**
	 * 去购物车结算按钮
	 */
	public static final By GOTO_SHOPPING_CART = By.className("shopCar_T_span3");
	/**
	 * 清空购物车按钮
	 */
	public static final By CLEAR_SHOPPING_CART = By
			.xpath("//div[text()='清空购物车']");

}

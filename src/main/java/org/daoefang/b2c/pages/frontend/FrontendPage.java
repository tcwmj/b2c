package org.daoefang.b2c.pages.frontend;

import org.daoefang.b2c.elements.frontend.IFrontendPage;
import org.daoefang.b2c.pages.Page;
import org.daoefang.b2c.selenium.Driver;

public class FrontendPage extends Page implements IFrontendPage {

	public FrontendPage(Driver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void navigateTo() {
		navigateTo(driver.frontend_url);
	}
}

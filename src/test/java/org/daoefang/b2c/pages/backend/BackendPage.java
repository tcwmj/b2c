package org.daoefang.b2c.pages.backend;

import org.daoefang.b2c.elements.backend.IBackendPage;
import org.daoefang.b2c.pages.Page;
import org.daoefang.b2c.utils.selenium.Driver;

public class BackendPage extends Page implements IBackendPage {

	public BackendPage(Driver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void navigateTo() {
		navigateTo(driver.backend_url);
	}

	public void logout() {
		driver.click(LOGOUT);
	}
}

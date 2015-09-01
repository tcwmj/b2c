package org.daoefang.b2c.pages.frontend;

import org.daoefang.b2c.bean.LoginCredential;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.elements.frontend.IHomePage;
import org.daoefang.b2c.selenium.Driver;

/**
 * @author Kenny Wang
 * 
 */
public final class HomePage extends FrontendPage implements IHomePage {

	public HomePage(Driver driver) {
		super(driver);
	}

	public void login(LoginCredential loginCredential) {
	}

	public void silentLogin() {
		login(SystemData.ADMIN);
	}

}

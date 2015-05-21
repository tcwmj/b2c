package org.daoefang.b2c.pages.frontend;

import org.daoefang.b2c.bean.Field;
import org.daoefang.b2c.elements.frontend.IWelcomePage;
import org.daoefang.b2c.utils.selenium.Driver;

/**
 * @author Kenny Wang
 * 
 */
public final class WelcomePage extends FrontendPage implements IWelcomePage {

	public WelcomePage(Driver driver) {
		super(driver);
	}

	public void navigate(Field menu) {
	}

	public void logout() {
	}

}

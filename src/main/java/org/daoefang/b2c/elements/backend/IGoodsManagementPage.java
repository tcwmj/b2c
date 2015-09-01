package org.daoefang.b2c.elements.backend;

import org.openqa.selenium.By;

/**
 * @author Kenny Wang
 * 
 */
public interface IGoodsManagementPage extends IBackendPage {
	public static final By UPLOAD_FILE = By.name("file");
	public static final By START_IMPORT = By.xpath("//input[@value='开始导入']");
}

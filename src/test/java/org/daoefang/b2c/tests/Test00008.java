package org.daoefang.b2c.tests;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.daoefang.b2c.elements.backend.IGoodsManagementPage;
import org.daoefang.b2c.utils.HashType;
import org.daoefang.b2c.utils.Helper;
import org.daoefang.b2c.utils.Property;
import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.Test;

/**
 * 上传下载文件解决方案
 * 
 * @author Kenny Wang
 */
public class Test00008 extends TestCase {

	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() {
		backend.home().silentLogin();
	}

	@Test(description = "到商品信息导入页面")
	public void step030() {
		backend.siteConfiguration().switchTo(IMPORT_GOODS_INFO);
	}

	@Test(description = "下载文件并验证文件HASH")
	public void step040() throws URISyntaxException, IOException {
		URI uri = new URI(Property.GOODS_TEMPLATE_URI);
		File file = driver.downloadFile(this, uri, "csv");
		td.setField("template").setValue(file.getAbsolutePath());
		;
		System.out.println(file.getAbsolutePath());

		Helper.assertFileHash(file.toURI(), "52D0C87949E72979B8E3B258C321DC8A",
				HashType.MD5);
		Helper.assertFileHash(file.toURI(),
				"2A8C435B9BFA816766D2474831628018FBA402DC", HashType.SHA1);
	}

	@Test(description = "上传文件")
	public void step050() throws URISyntaxException, IOException {
		backend.goodsManagement().switchToFrame();
		driver.type(IGoodsManagementPage.UPLOAD_FILE, td.getField("template")
				.getValue());
		driver.click(IGoodsManagementPage.START_IMPORT);
		backend.goodsManagement().switchToDefault();
	}

	@Test(description = "退出登录")
	public void step060() throws URISyntaxException, IOException {
		backend.goodsManagement().logout();
	}
}
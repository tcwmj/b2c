package org.daoefang.b2c.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.daoefang.b2c.bean.DataBean;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.pages.backend.GoodsManagementPage;
import org.daoefang.b2c.pages.backend.LoginPage;
import org.daoefang.b2c.pages.backend.MemberManagementPage;
import org.daoefang.b2c.pages.backend.SiteConfigurationPage;
import org.daoefang.b2c.pages.frontend.GoodsPage;
import org.daoefang.b2c.pages.frontend.HomePage;
import org.daoefang.b2c.selenium.Driver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * @author Kenny Wang
 * 
 */
public class TestCase implements SystemData {

	public final static DataBean sd = getSystemData();
	protected DataBean td;

	{
		PropertyConfigurator.configure("config/log4j.properties");
	}
	protected Logger logger = Logger.getLogger(this.getClass());
	protected Driver driver;

	protected Backend backend = new Backend();
	protected Frontend frontend = new Frontend();

	/**
	 * @return the driver
	 */
	public Driver getDriver() {
		return driver;
	}

	/**
	 * whether to skip next execution of left test methods
	 */
	protected Boolean skipTest = false;

	/**
	 * @return the skipTest
	 */
	public Boolean getSkipTest() {
		return skipTest;
	}

	/**
	 * @param skipTest
	 *            the skipTest to set
	 */
	public void setSkipTest(Boolean skipTest) {
		this.skipTest = skipTest;
	}

	/**
	 * get test data file by specified test case id
	 * 
	 * @param testCaseId
	 * @param isSource
	 *            true indicates source file, false indicates target file
	 * @return
	 */
	private File getTestData(String testCaseId, Boolean isSource) {
		if (testCaseId == null)
			throw new IllegalArgumentException("parameter testCaseId was null!");
		if (isSource) {
			return new File("data/" + testCaseId + ".xml");
		} else {
			return new File("target/data/" + testCaseId + ".xml");
		}
	}

	/**
	 * get system data bean
	 * 
	 * @return
	 */
	private static DataBean getSystemData() {
		File file = new File(Property.SYSTEM_DATA);
		return getTestData(file);
	}

	/**
	 * get test data bean from specified file
	 * 
	 * @param file
	 * @return
	 */
	private static DataBean getTestData(File file) {
		String xml = "";
		try {
			xml = FileUtils.readFileToString(file, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JaxbHelper.unmarshal(xml, Property.DATA_SCHEMA, DataBean.class);
	}

	/**
	 * save test data bean into referenced test case data file
	 * 
	 * @param testCaseId
	 * @param data
	 */
	private static void setTestData(File file, DataBean data) {
		if (data != null) {
			String xml = JaxbHelper.marshal(data);
			Helper.writeFile(file, xml);
		}
	}

	@BeforeClass
	@Parameters({ "os", "os_version", "browser", "browser_version",
			"resolution", "frontend_url", "backend_url" })
	protected void setUp(@Optional String os, @Optional String os_version,
			@Optional String browser, @Optional String browser_version,
			@Optional String resolution, @Optional String frontend_url,
			@Optional String backend_url) {
		System.setProperty("testcase.log.name", this.getClass().getSimpleName());
		logger.info("Try to setup a test");

		driver = new Driver(os, os_version, browser, browser_version,
				resolution, frontend_url, backend_url);

		if (new File("data/" + getTestCaseId() + ".xml").exists())
			report("../../../data/" + getTestCaseId() + ".xml",
					"open source test data");
		report("../../../target/data/" + getTestCaseId() + ".xml",
				"open target test data");

		// if (url == null) {
		// report(Helper.getTestReportStyle(Property.FRONTEND_URL,
		// "open test server url by property config"));
		// driver.navigateTo(Property.FRONTEND_URL);
		// } else {
		// report(Helper.getTestReportStyle(url,
		// "open test server url by testng config"));
		// driver.navigateTo(url);
		// }
	}

	@AfterClass
	protected void tearDown() {
		logger.info("Try to teardown");
		driver.quit();
	}

	@BeforeMethod
	protected void startStep() {
		if (!getSkipTest()) {
			File tarFile = getTestData(getTestCaseId(), false);
			if (tarFile.exists()) {
				logger.debug("Try to load target test data...");
				td = getTestData(tarFile);
			} else {
				File srcFile = getTestData(getTestCaseId(), true);
				if (srcFile.exists()) {
					logger.debug("Try to load source test data...");
					td = getTestData(srcFile);
				} else {
					logger.debug("Try to generate new test data...");
					td = new DataBean();
				}
			}
		}
	}

	@AfterMethod
	protected void afterStep() {
		if (!getSkipTest()) {
			logger.debug("Try to save test data...");
			File file = getTestData(getTestCaseId(), false);
			setTestData(file, td);
		}
	}

	/**
	 * report the text content
	 * 
	 * @param text
	 */
	public void report(String text) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now = df.format(new Date());
		logger.info(text);
		Reporter.log(now + " " + this.getClass().getName() + " " + text
				+ "<br>");
	}

	/**
	 * report the text content with a source link to a file
	 * 
	 * @param text
	 */
	public void report(String source, String text) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now = df.format(new Date());
		logger.info(text + " " + source);
		Reporter.log(now + " " + this.getClass().getName() + " "
				+ Helper.getTestReportStyle(source, text) + "<br>");
	}

	/**
	 * get test case id of current instance
	 * 
	 * @return
	 */
	public String getTestCaseId() {
		return this.getClass().getSimpleName();
	}

	protected class Backend {

		private LoginPage loginPage;
		private SiteConfigurationPage siteConfigurationPage;
		private MemberManagementPage memberManagementPage;
		private GoodsManagementPage goodsManagementPage;

		/**
		 * @return the homePage
		 */
		public LoginPage home() {
			if (loginPage == null)
				loginPage = new LoginPage(driver);
			return loginPage;
		}

		/**
		 * @return the SiteConfigurationPage
		 */
		public SiteConfigurationPage siteConfiguration() {
			if (siteConfigurationPage == null)
				siteConfigurationPage = new SiteConfigurationPage(driver);
			return siteConfigurationPage;
		}

		/**
		 * @return the GoodsManagementPage
		 */
		public GoodsManagementPage goodsManagement() {
			if (goodsManagementPage == null)
				goodsManagementPage = new GoodsManagementPage(driver);
			return goodsManagementPage;
		}

		/**
		 * @return the MemberManagementPage
		 */
		public MemberManagementPage memberManagement() {
			if (memberManagementPage == null)
				memberManagementPage = new MemberManagementPage(driver);
			return memberManagementPage;
		}
	}

	protected class Frontend {
		private HomePage homePage;
		private GoodsPage goodPage;

		/**
		 * @return the homePage
		 */
		public HomePage home() {
			if (homePage == null)
				homePage = new HomePage(driver);
			return homePage;
		}

		/**
		 * @return the goodPage
		 */
		public GoodsPage good() {
			if (goodPage == null)
				goodPage = new GoodsPage(driver);
			return goodPage;
		}
	}

}
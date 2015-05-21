package org.daoefang.b2c.tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.daoefang.b2c.bean.DataBean;
import org.daoefang.b2c.bean.SystemData;
import org.daoefang.b2c.pages.backend.HomePage;
import org.daoefang.b2c.pages.backend.SiteConfigurationPage;
import org.daoefang.b2c.utils.Helper;
import org.daoefang.b2c.utils.JaxbHelper;
import org.daoefang.b2c.utils.Property;
import org.daoefang.b2c.utils.selenium.Driver;
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

	public static final DataBean sd = getSystemData();
	protected DataBean td;
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
			try {
				FileUtils.write(file, xml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@BeforeClass
	@Parameters({ "os", "os_version", "browser", "browser_version",
			"resolution", "frontend_url", "backend_url" })
	protected void setUp(@Optional String os, @Optional String os_version,
			@Optional String browser, @Optional String browser_version,
			@Optional String resolution, @Optional String frontend_url,
			@Optional String backend_url) {
		logger.info("setup");

		driver = new Driver(os, os_version, browser, browser_version,
				resolution, frontend_url, backend_url);

		if (new File("data/" + getTestCaseId() + ".xml").exists())
			report(Helper.getTestReportStyle("../../../data/" + getTestCaseId()
					+ ".xml", "open source test data"));
		report(Helper.getTestReportStyle("../../../target/data/"
				+ getTestCaseId() + ".xml", "open target test data"));

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
		logger.info("teardown");
		driver.quit();
	}

	@BeforeMethod
	protected void startStep() {
		if (!getSkipTest()) {
			File tarFile = getTestData(getTestCaseId(), false);
			if (tarFile.exists()) {
				logger.debug("loading target test data...");
				td = getTestData(tarFile);
			} else {
				File srcFile = getTestData(getTestCaseId(), true);
				if (srcFile.exists()) {
					logger.debug("loading source test data...");
					td = getTestData(srcFile);
				} else {
					logger.debug("generating new test data...");
					td = new DataBean();
				}
			}
		}
	}

	@AfterMethod
	protected void afterStep() {
		if (!getSkipTest()) {
			logger.debug("saving test data...");
			File file = getTestData(getTestCaseId(), false);
			setTestData(file, td);
		}
	}

	/**
	 * log the content into the report
	 * 
	 * @param s
	 */
	public void report(String s) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now = df.format(new Date());
		logger.info(s);
		Reporter.log(now + " " + this.getClass().getName() + " " + s + "<br>");
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

		private HomePage homePage;
		private SiteConfigurationPage siteConfigurationPage;

		/**
		 * @return the homePage
		 */
		public HomePage home() {
			if (homePage == null)
				homePage = new HomePage(driver);
			return homePage;
		}

		/**
		 * @return the welcomePage
		 */
		public SiteConfigurationPage siteConfiguration() {
			if (siteConfigurationPage == null)
				siteConfigurationPage = new SiteConfigurationPage(driver);
			return siteConfigurationPage;
		}
	}

	protected class Frontend {
		private HomePage homePage;
		private SiteConfigurationPage welcomePage;

		/**
		 * @return the homePage
		 */
		public HomePage home() {
			if (homePage == null)
				homePage = new HomePage(driver);
			return homePage;
		}

		/**
		 * @return the welcomePage
		 */
		public SiteConfigurationPage welcome() {
			if (welcomePage == null)
				welcomePage = new SiteConfigurationPage(driver);
			return welcomePage;
		}
	}

}
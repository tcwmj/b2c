package org.daoefang.b2c.testng;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.daoefang.b2c.utils.Property;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author Kenny Wang
 * 
 */
public class RetryAnalyzer implements IRetryAnalyzer {
	private final static Logger logger = Logger.getLogger(IRetryAnalyzer.class);
	private static final int MAX_RETRY_COUNT = Property.RETRY_COUNT;
	private int retryCount = 0;

	@Override
	public synchronized boolean retry(ITestResult testResult) {
		if (retryCount < MAX_RETRY_COUNT) {
			retryCount++;
			return true;
		}

		// set the skip test to true on such test case instance
		Method method;
		try {
			method = testResult.getInstance().getClass()
					.getMethod("setSkipTest", Boolean.class);
			method.invoke(testResult.getInstance(), true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}
}

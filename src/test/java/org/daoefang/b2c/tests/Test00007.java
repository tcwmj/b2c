package org.daoefang.b2c.tests;

import org.daoefang.b2c.utils.TestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 断言和验证
 * 
 * @author Kenny Wang
 */
public class Test00007 extends TestCase {

	@Test(description = "验证pass")
	public void step010() {
		driver.verifyEqual("abc", "abc");
	}

	@Test(description = "验证fail")
	public void step020() {
		driver.verifyEqual("123", "1234");
	}

	@Test(description = "断言pass")
	public void step030() {
		Assert.assertEquals("hello", "hello");
	}

	@Test(description = "断言fail")
	public void step040() {
		Assert.assertEquals("ho", "hi");
	}

}
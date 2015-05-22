package org.daoefang.b2c.pages.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.daoefang.b2c.bean.Member;
import org.daoefang.b2c.elements.backend.IMemberManagementPage;
import org.daoefang.b2c.utils.selenium.Driver;
import org.openqa.selenium.By;

public class MemberManagementPage extends BackendPage implements
		IMemberManagementPage {

	public MemberManagementPage(Driver driver) {
		super(driver);
	}

	/**
	 * add a member
	 * 
	 * @param member
	 */
	public void addMember(Member member) {
		switchToFrame();
		driver.click(ADD_MEMBER);
		if (member.getUsername() != null)
			driver.input(USERNAME, member.getUsername());
		if (member.getPhoneNumber() != null)
			driver.input(MOBILE_PHONE, member.getPhoneNumber());
		if (member.isSex() != null) {
			if (member.isSex())
				driver.click(MAN);
			else
				driver.click(WOMAN);
		}
		if (member.getBirthday() != null)
			driver.input(BIRTHDAY, member.getBirthday());
		if (member.getEmail() != null)
			driver.input(EMAIL, member.getEmail());
		if (member.getQq() != null)
			driver.input(QQ, member.getQq());
		driver.click(SUBMIT);
		switchToDefault();
	}

	/**
	 * asset a record displayed or not in the member list table
	 * 
	 * @param member
	 * @param displayed
	 */
	public void assertMemberList(Member member, Boolean displayed) {
		StringBuffer xpath = new StringBuffer("//tr");
		Method[] methods = member.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if ((method.getName().startsWith("getUsername")
						|| method.getName().startsWith("getPhoneNumber") || method
						.getName().equals("getEmail"))
						&& method.invoke(member) != null) {
					xpath.append("[td[div='" + method.invoke(member) + "']]");
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		switchToFrame();
		assertDisplayed(By.xpath(xpath.toString()), displayed);
		switchToDefault();
	}
}

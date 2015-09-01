package org.daoefang.b2c.pages.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.daoefang.b2c.bean.Member;
import org.daoefang.b2c.elements.backend.IMemberManagementPage;
import org.daoefang.b2c.selenium.Driver;
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

	/**
	 * 在日历控件上设置会员生日
	 * 
	 * @param birthday
	 *            by format yyyy-MM-dd
	 */
	public void setMemberBirthday(String birthday) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH));
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		System.out.println(year + "-" + month + "-" + day);
		driver.click(BIRTHDAY);
		driver.click(CALENDAR_YEAR);
		Integer iyear = Integer.parseInt(year);

		// 持续往前滚动日期直到指定日期出现
		String yearBegin = driver.getAttribute(
				By.xpath("//ul[@id='laydate_ys']/li"), "y");
		while (iyear.compareTo(Integer.parseInt(yearBegin)) < 0) {
			driver.click(By.xpath("//a[@class='laydate_tab laydate_chtop']"));
			yearBegin = driver.getAttribute(
					By.xpath("//ul[@id='laydate_ys']/li"), "y");
		}

		// 持续往后滚动日期直到指定日期出现
		String yearEnd = driver.getAttribute(
				By.xpath("//ul[@id='laydate_ys']/li[last()]"), "y");
		while (iyear.compareTo(Integer.parseInt(yearEnd)) > 0) {
			driver.click(By.xpath("//a[@class='laydate_tab laydate_chdown']"));
			yearEnd = driver.getAttribute(
					By.xpath("//ul[@id='laydate_ys']/li[last()]"), "y");
		}

		driver.click(By.xpath("//ul[@id='laydate_ys']/li[@y='" + year + "']"));
		driver.click(CALENDAR_MONTH);
		driver.click(By.xpath("//div[@id='laydate_ms']/span[@m='" + month
				+ "']"));
		driver.click(By.xpath("//table[@id='laydate_table']//td[@d='" + day
				+ "']"));
	}
}

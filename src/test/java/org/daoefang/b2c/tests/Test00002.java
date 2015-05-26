package org.daoefang.b2c.tests;

import java.util.List;

import org.daoefang.b2c.bean.Member;
import org.daoefang.b2c.elements.backend.IMemberManagementPage;
import org.daoefang.b2c.utils.TestCase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 数据驱动的使用方法
 * 
 * @author Kenny Wang
 */
public class Test00002 extends TestCase {

	@DataProvider(name = "member")
	public Object[][] createData() {
		super.startStep();
		List<Member> member = td.getMember();
		Object[][] ret = new Object[member.size()][];
		for (int i = 0; i < member.size(); i++) {
			ret[i] = new Object[] { member.get(i) };
		}
		return ret;
		// return new Object[][] { new Object[] { member.get(0) },
		// new Object[] { member.get(1) }, new Object[] { member.get(2) } };
	}

	@Test(description = "导航到后台登录界面")
	public void step010() {
		backend.home().navigateTo();
	}

	@Test(description = "使用后台验证码接口登录")
	public void step020() {
		backend.home().silentLogin();
	}

	@Test(dataProvider = "member", description = "批量添加新会员并且验证添加成功")
	public void step030(Member member) {
		backend.siteConfiguration().switchTo(MEMEBER_LIST);
		backend.memberManagement().addMember(member.randomize());
		if (member.getUsername() == null) {
			backend.memberManagement().switchToFrame();
			backend.memberManagement().assertDisplayed(
					IMemberManagementPage.SUBMIT, true);
			backend.memberManagement().switchToDefault();
		} else if (member.getPhoneNumber() == null) {
			backend.memberManagement().switchToFrame();
			backend.memberManagement().assertTextDisplayed(
					INVALID_PHONE_NUMBER.getValue(), true);
			backend.memberManagement().switchToDefault();
		} else if (member.getEmail() == null) {
			backend.memberManagement().switchToFrame();
			backend.memberManagement().assertTextDisplayed(
					INVALID_EMAIL.getValue(), true);
			backend.memberManagement().switchToDefault();
		} else
			backend.memberManagement().assertMemberList(member, true);
	}

	@Test(description = "退出登录")
	public void step040() {
		backend.memberManagement().logout();
	}
}
package org.daoefang.b2c.tests.backend;

import java.util.List;

import org.daoefang.b2c.bean.Member;
import org.daoefang.b2c.elements.backend.IMemberManagementPage;
import org.daoefang.b2c.tests.TestCase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Kenny Wang
 * 
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
		// return new Object[][] { new Object[] { lc.get(0) },
		// new Object[] { lc.get(1) }, new Object[] { lc.get(2) } };
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
		backend.memberManagement().enterAddMember();
		backend.memberManagement().addMember(member.randomize());
		if (member.getUsername() == null || member.getPhoneNumber() == null
				|| member.getEmail() == null)
			backend.memberManagement().assertDisplayed(
					IMemberManagementPage.SUBMIT, true);
		else
			backend.memberManagement().assertMemberList(member, true);
	}

	@Test(description = "使用后台验证码接口登录")
	public void step040() {
		backend.memberManagement().logout();
	}
}
package org.daoefang.b2c.bean;

import static org.daoefang.b2c.tests.TestCase.sd;

/**
 * @author Kenny Wang
 * 
 */
public interface SystemData {
	public static final String PREFIX = "sel";

	public static final String FIELD_SEPARATOR = "|";
	public static final String SEPARATOR = ">";

	public static final String TRIGGER_EVENT_CLICK = "click";
	public static final String TRIGGER_EVENT_MOUSEOVER = "mouseover";
	public static final String TRIGGER_EVENT_MOUSEOUT = "mouseout";

	public static final String FIRE_EVENT_ONCLICK = "onclick";
	public static final String FIRE_EVENT_ONCHANGE = "onchange";

	public static final Field INVALID_CAPTCHA = sd.getField("invalid_captcha");
	public static final Field INVALID_PHONE_NUMBER = sd
			.getField("invalid_phone_number");
	public static final Field INVALID_EMAIL = sd.getField("invalid_email");

	public static final LoginCredential ADMIN = sd.getLoginCredential("admin");
	public static final LoginCredential CLIENT = sd
			.getLoginCredential("client");

	public static final Field MEMEBER_LIST = sd.getField("会员列表");
	public static final Field ADD_MEMEBER = sd.getField("添加会员");

}

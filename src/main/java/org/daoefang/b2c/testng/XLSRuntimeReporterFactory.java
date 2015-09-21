package org.daoefang.b2c.testng;

import java.util.HashMap;
import java.util.Map;

public class XLSRuntimeReporterFactory {

	private static Map<String, XLSRuntimeReporter> map = new HashMap<String, XLSRuntimeReporter>();

	public static XLSRuntimeReporter getXLSRuntimeReporter(String reportName) {
		if (!map.containsKey(reportName))
			map.put(reportName, new XLSRuntimeReporter(reportName));
		return map.get(reportName);
	}
}

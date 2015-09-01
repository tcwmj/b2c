package org.daoefang.b2c.testng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

/**
 * Reported designed to render self-contained HTML top down view of a testing
 * suite.
 * 
 * @author Kenny Wang
 */
public class XLSReporter extends CustomizedReporter {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(XLSReporter.class);

	// ~ Instance fields ------------------------------------------------------
	private static final String REPORT_NAME = "customized-report.xls";

	public enum testCaseStatus {
		Passed(ITestResult.SUCCESS, "Passed"), Failed(ITestResult.FAILURE,
				"Failed"), NoRun(ITestResult.SKIP, "No Run");
		public final int index;
		public final String value;

		private testCaseStatus(int id, String value) {
			this.index = id;
			this.value = value;
		}

		public static String getValue(int index) {
			for (testCaseStatus c : testCaseStatus.values()) {
				if (c.index == index) {
					return c.value;
				}
			}
			return null;
		}
	}

	// ~ Methods --------------------------------------------------------------
	/** Creates summary of the run */
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites,
			String outdir) {
		File report = new File(outdir, REPORT_NAME);
		Workbook workbook = openReport(report);
		Sheet sheet = createReport(workbook, report);
		updateReport(sheet, report, suites);
		saveReport(workbook, report);
	}

	/**
	 * create report style
	 * 
	 * @param workbook
	 * @param report
	 * @return
	 */
	private Sheet createReport(Workbook workbook, File report) {
		Sheet sheet = workbook.getSheet("Sheet1");
		if (sheet == null) {
			sheet = workbook.createSheet("Sheet1");
		}
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("id");
		row.createCell(1).setCellValue("status");
		row.createCell(2).setCellValue("author");
		row.createCell(3).setCellValue("duration");
		row.createCell(4).setCellValue("faild step");
		row.createCell(5).setCellValue("faild reason");
		return sheet;
	}

	/**
	 * create a new report if it doesn't exist or load an existing report
	 * 
	 * @param file
	 * @return
	 */
	private Workbook openReport(File file) {
		Workbook workbook = null;
		try {
			if (!file.exists()) {
				workbook = new HSSFWorkbook();
			} else {
				workbook = new HSSFWorkbook(new FileInputStream(file.getPath()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * write test result into the report
	 * 
	 * @param sheet
	 * @param report
	 * @param suites
	 */
	private void updateReport(Sheet sheet, File report, List<ISuite> suites) {
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult suiteResult : tests.values()) {
				ITestContext testContext = suiteResult.getTestContext();
				updateTestCase(sheet, testContext.getSkippedConfigurations()
						.getAllResults());
				updateTestCase(sheet, testContext.getSkippedTests()
						.getAllResults());

				updateTestCase(sheet, testContext.getPassedConfigurations()
						.getAllResults());
				updateTestCase(sheet, testContext.getPassedTests()
						.getAllResults());

				updateTestCase(sheet, testContext.getFailedConfigurations()
						.getAllResults());
				updateTestCase(sheet, testContext.getFailedTests()
						.getAllResults());
			}
		}
	}

	/**
	 * @param sheet
	 * @param testResults
	 */
	public void updateTestCase(Sheet sheet, Set<ITestResult> testResults) {
		// update test case related information
		for (ITestResult testResult : testResults) {
			String testCaseId = testResult.getInstance().getClass()
					.getSimpleName().replace("Test", "")
					.replaceFirst("^0*", "");
			Iterator<Row> it = sheet.iterator();
			Integer rowNum = null;
			while (it.hasNext()) {
				Row row = it.next();
				if (row.getCell(0).getStringCellValue().equals(testCaseId)) {
					rowNum = row.getRowNum();
					break;
				}
			}
			if (rowNum == null) {
				rowNum = sheet.getPhysicalNumberOfRows();
			}
			Row row = sheet.createRow(rowNum);

			row.createCell(0).setCellValue(testCaseId);

			String status = testCaseStatus.getValue(testResult.getStatus());
			row.createCell(1).setCellValue(status);

			String author = this
					.getAuthors(testResult.getTestClass().getName(),
							testResult.getMethod());
			row.createCell(2).setCellValue(author);

			Long duration = testResult.getEndMillis()
					- testResult.getStartMillis();
			row.createCell(3).setCellValue(duration);

			if (status.equals(testCaseStatus.Failed.value)) {
				String failedStep = testResult.getMethod().getMethodName();
				row.createCell(4).setCellValue(failedStep);
				String failedReason = Utils.stackTrace(
						testResult.getThrowable(), false)[0];
				row.createCell(5).setCellValue(failedReason);
			}
		}
	}

	private void saveReport(Workbook workbook, File report) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(report.getPath());
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}

package org.daoefang.b2c.testng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.ITestResult;
import org.testng.internal.Utils;

public class XLSRuntimeReporter {
	private final static Logger logger = Logger
			.getLogger(XLSRuntimeReporter.class);

	private String reportName = "runtime_report.xls";
	private String reportPath = new File("").getAbsolutePath() + "\\target\\";

	private static Workbook WORKBOOK;

	private static final String TEST_SHEET_NAME = "Test Summary";
	private static final String STEP_SHEET_NAME = "Step Summary";

	/**
	 * get status description from the testng test status id
	 * 
	 * @param status
	 * @return
	 */
	private String getStatus(int status) {
		switch (status) {
		case 1:
			return "Passed";
		case 2:
			return "Failed";
		case 3:
			return "Skipped";
		case 4:
			return "SuccessPercentageFailure";
		case 16:
			return "Started";
		default:
			return "unkown";
		}
	}

	public XLSRuntimeReporter() {
		super();
	}

	public XLSRuntimeReporter(String reportName) {
		super();
		this.reportName = reportName;
	}

	public XLSRuntimeReporter generateReport() {
		if (!openReport(reportPath + reportName)) {
			// cell style
			CellStyle style = WORKBOOK.createCellStyle();
			style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// cell font style
			Font font = WORKBOOK.createFont();
			// font.setFontHeightInPoints((short)24); //font size
			// font.setFontName("楷体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);// bold or not
			font.setColor(IndexedColors.WHITE.index);// font color
			style.setFont(font);

			if (WORKBOOK.getSheet(TEST_SHEET_NAME) == null) {
				Sheet testSheet = WORKBOOK.createSheet(TEST_SHEET_NAME);
				Row testRow = testSheet.createRow(0);
				testRow.createCell(0).setCellValue("TestCase");
				testRow.getCell(0).setCellStyle(style);
				testRow.createCell(1).setCellValue("Status");
				testRow.getCell(1).setCellStyle(style);
			}

			if (WORKBOOK.getSheet(STEP_SHEET_NAME) == null) {
				Sheet stepSheet = WORKBOOK.createSheet(STEP_SHEET_NAME);
				Row stepRow = stepSheet.createRow(0);
				stepRow.createCell(0).setCellValue("TestCase");
				stepRow.getCell(0).setCellStyle(style);
				stepRow.createCell(1).setCellValue("TestStep");
				stepRow.getCell(1).setCellStyle(style);
				stepRow.createCell(2).setCellValue("Status");
				stepRow.getCell(2).setCellStyle(style);
				stepRow.createCell(3).setCellValue("Description");
				stepRow.getCell(3).setCellStyle(style);
				stepRow.createCell(4).setCellValue("Comments");
				stepRow.getCell(4).setCellStyle(style);
			}

			saveReport(reportPath + reportName);
		}
		return this;
	}

	/**
	 * update test report
	 * 
	 * @param testResult
	 */
	public void updateReport(ITestResult testResult) {
		Integer testRow = updateTestSheet(testResult);
		Integer stepRow = updateStepSheet(testResult);
		// set hyperlink between test sheet and step sheet
		// if the new status is not skipped or started, then update it
		if (testRow != null && stepRow != null && testResult.getStatus() != 3
				&& testResult.getStatus() != 16) {
			HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
			link.setAddress("'" + STEP_SHEET_NAME + "'!A" + (stepRow + 1));
			Row row = WORKBOOK.getSheet(TEST_SHEET_NAME).getRow(testRow);
			row.getCell(1).setHyperlink(link);
		}
		saveReport(reportPath + reportName);
	}

	/**
	 * update test summary sheet result
	 * 
	 * @param testResult
	 * @return
	 */
	private Integer updateTestSheet(ITestResult testResult) {
		String testCase = testResult.getInstance().getClass().getSimpleName()
				.replace("Test", "").replaceFirst("^0*", "");
		Sheet sheet = WORKBOOK.getSheet(TEST_SHEET_NAME);
		Iterator<Row> it = sheet.iterator();

		// try to update existing test result
		while (it.hasNext()) {
			Row row = it.next();
			String caseId = row.getCell(0).getStringCellValue();
			if (testCase.equals(caseId)) {
				// if the new status is not skipped or started, then update it
				if (testResult.getStatus() != 3 && testResult.getStatus() != 16) {
					row.getCell(1).setCellValue(
							getStatus(testResult.getStatus()));
					setCellStyle(testResult, row.getCell(1));
				}
				return row.getRowNum();
			}
		}

		// add a new test result
		int rowNum = sheet.getPhysicalNumberOfRows();
		Row row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue(testCase);
		row.createCell(1).setCellValue(getStatus(testResult.getStatus()));
		setCellStyle(testResult, row.getCell(1));
		return row.getRowNum();
	}

	/**
	 * update test step sheet result
	 * 
	 * @param testResult
	 * @return
	 */
	private Integer updateStepSheet(ITestResult testResult) {
		String testStep = testResult.getMethod().getMethodName();
		Sheet sheet = WORKBOOK.getSheet(STEP_SHEET_NAME);
		int rowCount = sheet.getPhysicalNumberOfRows();
		// step status is not equal to started
		if (testResult.getStatus() != 16) {
			Row row = sheet.createRow(rowCount);
			String testCase = testResult.getInstance().getClass()
					.getSimpleName().replace("Test", "")
					.replaceFirst("^0*", "");
			row.createCell(0).setCellValue(testCase);
			row.createCell(1).setCellValue(testStep);
			row.createCell(2).setCellValue(getStatus(testResult.getStatus()));
			setCellStyle(testResult, row.getCell(2));
			row.createCell(3).setCellValue(
					testResult.getMethod().getDescription());
			// if having exception and not the org.testng.SkipException
			if (testResult.getThrowable() != null
					&& testResult.getStatus() != 3)
				row.createCell(4).setCellValue(
						Utils.stackTrace(testResult.getThrowable(), false)[0]);
			return row.getRowNum();
		}
		return rowCount;
	}

	/**
	 * set cell style by the test status
	 * 
	 * @param testResult
	 * @param cell
	 */
	private void setCellStyle(ITestResult testResult, Cell cell) {
		CellStyle style = WORKBOOK.createCellStyle();
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		switch (testResult.getStatus()) {
		case 1:// passed
			style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			break;
		case 2:// failed
			style.setFillForegroundColor(IndexedColors.RED.getIndex());
			break;
		case 3:// skpped
			style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			break;
		default:
			style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		}
		cell.setCellStyle(style);
	}

	/**
	 * if file exists then open it, if file doesn't exit then create it
	 * 
	 * @param filePath
	 * @return
	 */
	private boolean openReport(String filePath) {
		FileInputStream ins = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				WORKBOOK = new HSSFWorkbook();
			} else {
				ins = new FileInputStream(filePath);
				WORKBOOK = new HSSFWorkbook(ins);
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return false;
	}

	/**
	 * save file content into the disk
	 * 
	 * @param filePath
	 */
	private void saveReport(String filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			WORKBOOK.write(fos);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
	}
}

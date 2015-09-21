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
	private final static String TEST_SHEET_NAME = "Test Summary";
	private final static String STEP_SHEET_NAME = "Step Summary";

	private String reportName = "runtime_report.xls";
	private String reportPath = new File("").getAbsolutePath() + "\\target\\";

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
		initializeReport();
	}

	public XLSRuntimeReporter(String reportName) {
		this.reportName = reportName;
		initializeReport();
	}

	/**
	 * initialize report style
	 */
	private synchronized void initializeReport() {
		if (!new File(reportPath + reportName).exists()) {
			Workbook workbook = createReport();

			// create cell style
			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// cell font style
			Font font = workbook.createFont();
			// font.setFontHeightInPoints((short)24); //font size
			// font.setFontName("楷体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);// bold or not
			font.setColor(IndexedColors.WHITE.index);// font color
			style.setFont(font);

			// if (workbook.getSheet(TEST_SHEET_NAME) == null) {
			Sheet testSheet = workbook.createSheet(TEST_SHEET_NAME);
			Row testRow = testSheet.createRow(0);
			testRow.createCell(0).setCellValue("TestCase");
			testRow.getCell(0).setCellStyle(style);
			testRow.createCell(1).setCellValue("Status");
			testRow.getCell(1).setCellStyle(style);

			// if (workbook.getSheet(STEP_SHEET_NAME) == null) {
			Sheet stepSheet = workbook.createSheet(STEP_SHEET_NAME);
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

			saveReport(workbook, reportPath + reportName);
		}
	}

	/**
	 * update test report
	 * 
	 * @param testResult
	 */
	public synchronized void updateReport(ITestResult testResult) {
		Workbook workbook = openReport(reportPath + reportName);
		Integer testRow = updateTestSheet(workbook, testResult);
		Integer stepRow = updateStepSheet(workbook, testResult);
		// set hyperlink between test sheet and step sheet
		// if the new status is not skipped or started, then update it
		if (testRow != null && stepRow != null && testResult.getStatus() != 3
				&& testResult.getStatus() != 16) {
			HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
			link.setAddress("'" + STEP_SHEET_NAME + "'!A" + (stepRow + 1));
			Row row = workbook.getSheet(TEST_SHEET_NAME).getRow(testRow);
			row.getCell(1).setHyperlink(link);
		}
		saveReport(workbook, reportPath + reportName);
	}

	/**
	 * update test summary sheet result
	 * 
	 * @param workbook
	 * @param testResult
	 * @return
	 */
	private synchronized Integer updateTestSheet(Workbook workbook,
			ITestResult testResult) {
		String testCase = testResult.getInstance().getClass().getSimpleName()
				.replace("Test", "").replaceFirst("^0*", "");
		Sheet sheet = workbook.getSheet(TEST_SHEET_NAME);
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
					setCellStyle(workbook, testResult, row.getCell(1));
				}
				return row.getRowNum();
			}
		}

		// add a new test result
		int rowNum = sheet.getPhysicalNumberOfRows();
		Row row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue(testCase);
		row.createCell(1).setCellValue(getStatus(testResult.getStatus()));
		setCellStyle(workbook, testResult, row.getCell(1));
		return row.getRowNum();
	}

	/**
	 * update test step sheet result
	 * 
	 * @param workbook
	 * @param testResult
	 * @return
	 */
	private synchronized Integer updateStepSheet(Workbook workbook,
			ITestResult testResult) {
		String testStep = testResult.getMethod().getMethodName();
		Sheet sheet = workbook.getSheet(STEP_SHEET_NAME);
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
			setCellStyle(workbook, testResult, row.getCell(2));
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
	 * @param workbook
	 * @param testResult
	 * @param cell
	 */
	private synchronized void setCellStyle(Workbook workbook,
			ITestResult testResult, Cell cell) {
		CellStyle style = workbook.createCellStyle();
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
	 * create a new report
	 * 
	 * @return
	 */
	private synchronized Workbook createReport() {
		return new HSSFWorkbook();
	}

	/**
	 * if file exists then open it, if file doesn't exit then create it
	 * 
	 * @param filePath
	 * @return
	 */
	private synchronized Workbook openReport(String filePath) {
		FileInputStream ins = null;
		Workbook workbook = null;
		try {
			ins = new FileInputStream(filePath);
			workbook = new HSSFWorkbook(ins);
		} catch (IOException e) {
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
		return workbook;
	}

	/**
	 * save file content into the disk
	 * 
	 * @param workbook
	 * @param filePath
	 */
	private synchronized void saveReport(Workbook workbook, String filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			workbook.write(fos);
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

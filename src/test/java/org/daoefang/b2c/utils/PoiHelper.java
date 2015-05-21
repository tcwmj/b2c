package org.daoefang.b2c.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Jova Qiu
 * 
 */
public class PoiHelper {

	public static <T> HSSFWorkbook marshal(List<T> list) {

		String sheetName = "Sheet1";
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		@SuppressWarnings("unused")
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFRow firstrow = sheet.createRow(0);

		Class<?> clazz = list.get(0).getClass();
		Map<String, Map<String, String>> map = Helper
				.getFeedMapping("config/" + clazz.getSimpleName() + ".map");
		Field[] fields = clazz.getDeclaredFields();
		int columnOffset = 0;
		// set headers here
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String XmlElementName;
			try {
				XmlElementName = field.getAnnotation(XmlElement.class).name();
			} catch (NullPointerException e) {
				XmlElementName = field.getName();
			}
			if (map.containsKey(XmlElementName)) {
				if (field.getType() == String.class) {

					Set<String> keyset = map.get(XmlElementName).keySet();
					for (String key : keyset) {
						firstrow.createCell(i + columnOffset).setCellValue(key);
						break;
					}
				} else if (field.getType() == List.class) {
					// TODO set field header if it's a list
					// columnOffset++;
				}
			}
		}
		// set values here
		try {
			// rows iteration
			for (short i = 0; i < list.size(); i++) {
				T rowdata = list.get(i);
				HSSFRow row = sheet.createRow(i + 1);
				// columns iteration
				for (int j = 0; j < fields.length; j++) {
					Field field = fields[j];
					String XmlElementName;
					try {
						XmlElementName = field.getAnnotation(XmlElement.class)
								.name();
					} catch (NullPointerException e) {
						XmlElementName = field.getName();
					}
					if (map.containsKey(XmlElementName)) {
						String methodName = "get"
								+ new StringBuilder()
										.append(Character.toUpperCase(field
												.getName().charAt(0)))
										.append(field.getName().substring(1))
										.toString();
						Method method = clazz.getMethod(methodName);
						String value = method.invoke(rowdata) == null ? ""
								: method.invoke(rowdata).toString();
						if (!value.isEmpty()) {
							if (field.getType() == String.class) {
								Set<Entry<String, String>> entrySet = map.get(
										XmlElementName).entrySet();
								for (Entry<String, String> entry : entrySet) {
									switch (entry.getValue()) {
									case "date":
										row.createCell(j + columnOffset)
												.setCellValue(
														new SimpleDateFormat(
																"MM/dd/yyyy")
																.parse(value));
										break;
									case "long":
										row.createCell(j + columnOffset)
												.setCellValue(
														Long.parseLong(value));
										break;
									case "decimal":
										row.createCell(j + columnOffset)
												.setCellValue(
														Float.parseFloat(value));
										break;
									default:
										row.createCell(j + columnOffset)
												.setCellValue(value);
										break;
									}
									break;
								}
							} else if (field.getType() == List.class) {
								// TODO set field value if its a list
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	public static List<Map<String, String>> unmarshal(String excel) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(excel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HSSFWorkbook workBook = null;
		try {
			workBook = new HSSFWorkbook(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HSSFSheet sheet = workBook.getSheetAt(0);
		int rowCount = sheet.getPhysicalNumberOfRows(); // sheet.getLastRowNum();
		int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < rowCount; i++) {
			Map<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < columnCount; j++) {
				map.put(sheet.getRow(0).getCell(j).getStringCellValue().trim(),
						sheet.getRow(i).getCell(j).toString().trim());
			}
			list.add(map);
		}

		return list;
	}
}
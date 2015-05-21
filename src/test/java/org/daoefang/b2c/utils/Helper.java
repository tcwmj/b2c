package org.daoefang.b2c.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xml.utils.DefaultErrorHandler;
import org.daoefang.b2c.tests.TestCase;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.zeroturnaround.zip.ZipUtil;

import bad.robot.excel.matchers.Matchers;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

/**
 * @author Kenny Wang
 * 
 */
public class Helper {

	public static String toDate(String date, String fromFormat, String toFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
		try {
			Date d = dateFormat.parse(date);
			return (new SimpleDateFormat(toFormat)).format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static boolean isDate(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				I18N.DATE_PATTERN_EN_US);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * download file from the browser, please use this method combined with
	 * method assertFileExists;
	 * 
	 * @param testcase
	 *            set to this if it was invoked in your test case
	 * @param extension
	 *            file extension
	 * @return String file absolute path
	 * 
	 */
	public static String downloadFile(TestCase testcase, String extension) {
		String fileName = testcase.getClass().getSimpleName() + "_"
				+ randomize() + "." + extension;
		String filePath = "target/data/" + fileName;
		File file = new File(filePath);
		// exe path, title, save/cancel/run, path to save, file(optional).
		String[] cmd = new String[] { Property.DOWNLOAD_FILE_EXE,
				"File Download", "Save", file.getAbsolutePath() };
		Process exec;
		try {
			exec = Runtime.getRuntime().exec(cmd);
			exec.waitFor();
			exec.destroy(); // kill the process looking to download
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testcase.report(getTestReportStyle("../../../" + filePath,
				"download file " + fileName));
		return file.getAbsolutePath();
	}

	public static void assertFileExists(String filepath) {
		assertFileExists(filepath, true, Property.FILE_ACCESSABLE);
	}

	/**
	 * @param filepath
	 * @param exist
	 * @param timeout
	 */
	public static void assertFileExists(String filepath, Boolean exist,
			int timeout) {
		File file = new File(filepath);
		long curtime = System.currentTimeMillis();
		while (System.currentTimeMillis() - curtime <= timeout * 1000) {
			if (file.exists())
				return;
		}
		Assert.fail("fail to get file " + filepath + " in " + timeout
				+ " seconds");
	}

	public static void assertFileEquals(String sourceFile, String newFile) {
		String ext = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
		switch (ext.toLowerCase()) {
		case "zip":
			assertZipFileEquals(sourceFile, newFile);
			break;
		case "xls":
			compareExcel(sourceFile, newFile);
			break;
		case "xlsx":
			compareExcel(sourceFile, newFile);
			break;
		default:
			assertTextFileEquals(sourceFile, newFile);
		}
	}

	private static void assertZipFileEquals(String sourceFile, String newFile) {
		Assert.assertTrue(ZipUtil.archiveEquals(new File(sourceFile), new File(
				newFile)));
	}

	private static void assertTextFileEquals(String sourceFile, String newFile) {
		List<String> original = readTextFile(sourceFile);
		List<String> revised = readTextFile(newFile);

		Patch patch = DiffUtils.diff(original, revised);
		if (patch.getDeltas().size() > 0) {
			StringBuffer strB = new StringBuffer();
			for (Delta delta : patch.getDeltas()) {
				strB.append(delta);
			}
			Assert.fail(" AssertTextFileEquals fail : " + strB.toString());
		}
	}

	public static void deleteFile(String filepath) {
		File file = new File(filepath);
		file.delete();
	}

	private static void compareExcel(String sourceFile, String newFile) {
		InputStream ins1 = null;
		InputStream ins2 = null;
		Workbook workbook1 = null;
		Workbook workbook2 = null;
		try {
			ins1 = new FileInputStream(sourceFile);
			ins2 = new FileInputStream(newFile);
			if (sourceFile.toLowerCase().endsWith(".xlsx")
					&& newFile.toLowerCase().endsWith(".xlsx")) {
				workbook1 = new XSSFWorkbook(ins1);
				workbook2 = new XSSFWorkbook(ins2);
			} else if (sourceFile.toLowerCase().endsWith(".xls")
					&& newFile.toLowerCase().endsWith(".xls")) {
				workbook1 = new HSSFWorkbook(ins1);
				workbook2 = new HSSFWorkbook(ins2);
			} else {
				throw new RuntimeException(
						"compareExcel Fail : Please check file name: [sourceFile : "
								+ sourceFile + "], [newFile : " + newFile + "]");
			}

			MatcherAssert.assertThat(workbook1,
					Matchers.sameWorkbook(workbook2));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ins1 != null) {
				try {
					ins1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins2 != null) {
				try {
					ins2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static List<String> readTextFile(String filename) {
		List<String> lines = new LinkedList<String>();
		BufferedReader in = null;
		String line = "";
		try {
			in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	public static String randomize() {
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		// return df.format(new Date());
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * @param xmlfile
	 * @param xsdfile
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void validateXml(File xmlfile, File xsdfile) throws Exception {
		final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
		final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);

		SAXParser parser = factory.newSAXParser();
		parser.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
		parser.setProperty(SCHEMA_SOURCE, xsdfile);

		XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setContentHandler(new DefaultHandler());
		xmlReader.setErrorHandler(new DefaultErrorHandler());
		xmlReader.parse(xmlfile.getAbsolutePath());
	}

	/**
	 * generate excel feed files
	 * 
	 * @param testcase
	 * @param list
	 * @return
	 */
	public static <T> String getXlsFeedFile(TestCase testcase, List<T> list) {
		if (list != null && !list.isEmpty()) {
			String fileName = testcase.getClass().getSimpleName() + "_"
					+ randomize() + ".xls";
			String filePath = "target/data/" + fileName;
			File file = new File(filePath);
			try {
				PoiHelper.marshal(list).write(new FileOutputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			testcase.report(getTestReportStyle("../../../" + filePath,
					"generated feed file " + fileName));
			return file.getAbsolutePath();
		}
		return null;
	}

	/**
	 * @param source
	 * @param text
	 * @return
	 */
	public static String getTestReportStyle(String source, String text) {
		return "<a href = 'javascript:void(0)' onclick=\"window.open ('"
				+ source
				+ "','newwindow','height=600,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no')\">"
				+ text + "</a>";
	}

	/**
	 * read xml and xls feed mapping rule from a file
	 * 
	 * @param file
	 * @return
	 */
	public static Map<String, Map<String, String>> getFeedMapping(String file) {
		FileInputStream isr = null;
		Reader r = null;
		try {
			isr = new FileInputStream(file);
			r = new InputStreamReader(isr, "utf-8");
			Properties props = new Properties();
			props.load(r);
			Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
			Set<Entry<Object, Object>> entrySet = props.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				if (!entry.getKey().toString().startsWith("#")) {
					Map<String, String> m = new HashMap<String, String>();
					String s = ((String) entry.getValue()).trim();
					s = s.substring(1, s.length() - 1);
					String[] kvs = s.split(",");
					for (String kv : kvs) {
						m.put(kv.substring(0, kv.indexOf('=')).trim(), kv
								.substring(kv.indexOf('=') + 1).trim());
						map.put(((String) entry.getKey()).trim(), m);
					}
				}
			}
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}

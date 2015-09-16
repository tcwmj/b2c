package org.daoefang.b2c.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xml.utils.DefaultErrorHandler;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.log4testng.Logger;
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

	private final static Logger logger = Logger.getLogger(Helper.class);

	public static String toDate(String date, String fromFormat, String toFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
		try {
			Date d = dateFormat.parse(date);
			return (new SimpleDateFormat(toFormat)).format(d);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		testcase.report("../../../" + filePath, "download file " + fileName);
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
			logger.error(e.getMessage(), e);
		} finally {
			if (ins1 != null) {
				try {
					ins1.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (ins2 != null) {
				try {
					ins2.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
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
	 * 获得一个随机数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomNumber(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

	/**
	 * 中国区手机号码段
	 */
	private static String[] tel_prefix = "139,138,137,136,135,134,159,158,152,151,150,157,182,183,184,188,187,147,130,131,132,155,156,186,185,145,155,156,186,133,153,177,180,181,189"
			.split(",");

	/**
	 * @return 随机手机号码
	 */
	public static String getRandomPhoneNumber() {
		int index = getRandomNumber(0, tel_prefix.length - 1);
		String first = tel_prefix[index];
		String second = String.valueOf(getRandomNumber(1, 888) + 10000)
				.substring(1);
		String thrid = String.valueOf(getRandomNumber(1, 9100) + 10000)
				.substring(1);
		return first + second + thrid;
	}

	private static final String base = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static final String[] email_suffix = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn"
			.split(",");

	/**
	 * 获得一个随机的email地址
	 * 
	 * @param min
	 *            最小长度
	 * @param max
	 *            最大长度
	 * @return
	 */
	public static String getRandomEmail(int min, int max) {
		int length = getRandomNumber(min, max);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = (int) (Math.random() * base.length());
			sb.append(base.charAt(number));
		}
		sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
		return sb.toString();
	}

	/**
	 * 把字符串内容写入文件，避免了中文乱码的情况
	 * 
	 * @param file
	 * @param content
	 */
	public static void writeFile(File file, String content) {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Performs a expectedFileHash check on a File.
	 * 
	 * @param uri
	 * @param hash
	 * @param hashType
	 * @throws IOException
	 */
	public static void assertFileHash(URI uri, String hash, HashType hashType)
			throws IOException {
		File file = new File(uri);
		String actualFileHash = "";

		switch (hashType) {
		case MD5:
			actualFileHash = DigestUtils.md5Hex(new FileInputStream(file))
					.toLowerCase();
			break;
		case SHA1:
			actualFileHash = DigestUtils.shaHex(new FileInputStream(file))
					.toLowerCase();
			break;
		}

		Assert.assertEquals(actualFileHash, hash.toLowerCase());
	}

}

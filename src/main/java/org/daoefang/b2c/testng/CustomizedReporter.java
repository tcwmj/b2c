package org.daoefang.b2c.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * Reported designed to render self-contained HTML top down view of a testing
 * suite.
 * 
 * @author Kenny Wang
 */
public abstract class CustomizedReporter implements IReporter {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(CustomizedReporter.class);

	// ~ Instance fields ------------------------------------------------------

	protected Set<Integer> testIds = new HashSet<Integer>();
	protected List<Integer> allRunTestIds = new ArrayList<Integer>();
	protected JavaDocBuilder builder = new JavaDocBuilder();

	// ~ Methods --------------------------------------------------------------

	/** Creates summary of the run */
	public abstract void generateReport(List<XmlSuite> xml,
			List<ISuite> suites, String outdir);

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	protected Collection<ITestNGMethod> getMethodSet(IResultMap tests,
			ISuite suite) {
		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();

		// Eliminate the repeat retry methods
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				int testId = getId(im.getTestResult());
				if (!testIds.contains(testId)) {
					testIds.add(testId);
					r.add(im);
				}
			}
		}
		Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();

		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			result.add(m.getTestMethod());
		}

		// Add all the methods that weren't invoked (e.g. skipped) that we
		// haven't added yet
		// for (ITestNGMethod m : tests.getAllMethods()) {
		// if (!result.contains(m)) {
		// result.add(m);
		// }
		// }

		for (ITestResult allResult : tests.getAllResults()) {
			int testId = getId(allResult);
			if (!testIds.contains(testId)) {
				result.add(allResult.getMethod());
			}
		}

		return result;
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	protected class TestSorter implements Comparator<IInvokedMethod> {
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		public int compare(IInvokedMethod o1, IInvokedMethod o2) {
			// System.out.println("Comparing " + o1.getMethodName() + " " +
			// o1.getDate()
			// + " and " + o2.getMethodName() + " " + o2.getDate());
			return (int) (o1.getDate() - o2.getDate());
			// int r = ((T) o1).getTestClass().getName().compareTo(((T)
			// o2).getTestClass().getName());
			// if (r == 0) {
			// r = ((T) o1).getMethodName().compareTo(((T) o2).getMethodName());
			// }
			// return r;
		}
	}

	// ~ JavaDoc-specific Methods
	// --------------------------------------------------------
	/**
	 * Get ITestNGMethod author(s) string, or class author(s) if no method
	 * author is present. Default return value is "unknown".
	 * 
	 * @param className
	 * @param method
	 * @return
	 * @author Kenny Wang
	 */
	protected String getAuthors(String className, ITestNGMethod method) {
		JavaClass cls = builder.getClassByName(className);
		DocletTag[] authors = cls.getTagsByName("author");
		// get class authors as default author name
		String allAuthors = "";
		if (authors.length == 0) {
			allAuthors = "unknown";
		} else {
			for (DocletTag author : authors) {
				allAuthors += author.getValue() + " ";
			}
		}
		// get method author name
		JavaMethod[] mtds = cls.getMethods();
		for (JavaMethod mtd : mtds) {
			if (mtd.getName().equals(method.getMethodName())) {
				authors = mtd.getTagsByName("author");
				if (authors.length != 0) {
					allAuthors = "";
					for (DocletTag author : authors) {
						allAuthors += author.getValue() + " ";
					}
				}
				break;
			}
		}
		return allAuthors.trim();
	}

	/**
	 * Get comment string of Java class.
	 * 
	 * @param className
	 * @return
	 * @author Kenny Wang
	 */
	protected String getClassComment(String className) {
		JavaClass cls = builder.getClassByName(className);
		return cls.getComment();
	}

	/**
	 * Get ITestResult id by class + method + parameters hash code.
	 * 
	 * @param result
	 * @return
	 * @author Kenny Wang
	 */
	protected int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id
				+ (result.getParameters() != null ? Arrays.hashCode(result
						.getParameters()) : 0);
		return id;
	}

	/**
	 * Get All tests id by class + method + parameters hash code.
	 * 
	 * @param context
	 * @param suite
	 * @author Kenny Wang
	 */
	protected void getAllTestIds(ITestContext context, ISuite suite) {
		IResultMap passTests = context.getPassedTests();
		IResultMap failTests = context.getFailedTests();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (passTests.getAllMethods().contains(im.getTestMethod())
					|| failTests.getAllMethods().contains(im.getTestMethod())) {
				int testId = getId(im.getTestResult());
				// m_out.println("ALLtestid=" + testId);
				allRunTestIds.add(testId);
			}
		}
	}
}

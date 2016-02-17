package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.List;

import com.starit.janjoonweb.domain.JJTestcase;

public class TestCaseChartUtil {

	private JJTestcase	testcase;
	private String		success;

	public TestCaseChartUtil(JJTestcase testcase) {
		this.testcase = testcase;
		this.success = null;
	}

	public JJTestcase getTestcase() {
		return testcase;
	}

	public void setTestcase(JJTestcase testcase) {
		this.testcase = testcase;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public static List<TestCaseChartUtil> getTestCaseUtilFromJJTesCase(List<JJTestcase> jjTestcases) {
		List<TestCaseChartUtil> list = new ArrayList<TestCaseChartUtil>();
		for (JJTestcase c : jjTestcases) {
			list.add(new TestCaseChartUtil(c));
		}
		return list;
	}

}

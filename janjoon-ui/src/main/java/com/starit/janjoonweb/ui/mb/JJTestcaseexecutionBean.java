package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcaseexecution.class, beanName = "jJTestcaseexecutionBean")
public class JJTestcaseexecutionBean {

	private JJTestcaseexecution testcaseexecution;

	private List<TestCaseexecutionRecap> testCaseexecutionRecaps;

	public JJTestcaseexecution getTestcaseexecution() {
		return testcaseexecution;
	}

	public void setTestcaseexecution(JJTestcaseexecution testcaseexecution) {
		this.testcaseexecution = testcaseexecution;
	}

	public List<TestCaseexecutionRecap> getTestCaseexecutionRecaps() {

		testCaseexecutionRecaps = new ArrayList<TestCaseexecutionRecap>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJBuild build = jJBuildBean.getBuild();

		if (testcase != null && testcase.getId() != null) {

			List<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionService
					.getTestcaseexecutions(testcase, build, true, true);

			for (JJTestcaseexecution testcaseexecution : testcaseexecutions) {
				testCaseexecutionRecaps.add(new TestCaseexecutionRecap(
						testcaseexecution));
			}
		}

		return testCaseexecutionRecaps;
	}

	public void setTestCaseexecutionRecaps(
			List<TestCaseexecutionRecap> testCaseexecutionRecaps) {
		this.testCaseexecutionRecaps = testCaseexecutionRecaps;
	}

	public void loadTestcaseexecution(
			JJTeststepexecutionBean jJTeststepexecutionBean) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJBuild build = jJBuildBean.getBuild();

		List<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionService
				.getTestcaseexecutions(testcase, build, true, true);
		if (!testcaseexecutions.isEmpty()) {
			JJTestcaseexecution testcaseexecution = testcaseexecutions.get(0);
			if (testcaseexecution.getPassed() != null) {
				newTestcaseexecution(testcase, build);
			} else {
				this.testcaseexecution = testcaseexecution;
			}
		} else {
			newTestcaseexecution(testcase, build);
		}
		
		System.out.println("##########################");
		System.out.println(testcaseexecution.getId());
		System.out.println("##########################");

		jJTeststepexecutionBean.loadTeststepexecution();

		
		
	}

	private void newTestcaseexecution(JJTestcase testcase, JJBuild build) {

		testcaseexecution = new JJTestcaseexecution();
		testcaseexecution.setName(testcase.getName());
		testcaseexecution.setDescription(testcase.getDescription());
		testcaseexecution.setCreationDate(new Date());
		testcaseexecution.setEnabled(true);
		testcaseexecution.setTestcase(testcase);

		testcaseexecution.setPassed(null);

		testcaseexecution.setBuild(build);

		jJTestcaseexecutionService.saveJJTestcaseexecution(testcaseexecution);

	}

	public class TestCaseexecutionRecap {

		private JJTestcaseexecution testcaseexecution;

		private String status;

		public TestCaseexecutionRecap(JJTestcaseexecution testcaseexecution) {
			super();
			this.testcaseexecution = testcaseexecution;

			if (testcaseexecution.getPassed() != null) {
				if (testcaseexecution.getPassed()) {
					status = "SUCCESS";
				} else {
					status = "FAILED";
				}
			} else {
				status = "Non Fini";
			}

		}

		public JJTestcaseexecution getTestcaseexecution() {
			return testcaseexecution;
		}

		public void setTestcaseexecution(JJTestcaseexecution testcaseexecution) {
			this.testcaseexecution = testcaseexecution;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

}

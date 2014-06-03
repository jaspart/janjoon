package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcaseexecution.class, beanName = "jJTestcaseexecutionBean")
public class JJTestcaseexecutionBean {

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	private JJTestcaseexecution testcaseexecution;

	private List<TestCaseexecutionRecap> testCaseexecutionRecaps;

	private Boolean status;

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
					.getTestcaseexecutions(testcase, build, true, true, false);

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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
				.getTestcaseexecutions(testcase, build, true, false, true);
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

		status = null;

		jJTeststepexecutionBean.loadTeststepexecution();

	}

	public void toto() {
		testcaseexecution.setPassed(status);

	}

	public void save() {

		Boolean status = testcaseexecution.getPassed();

		JJTestcaseexecution tce = jJTestcaseexecutionService
				.findJJTestcaseexecution(testcaseexecution.getId());

		tce.setPassed(status);
		tce.setUpdatedDate(new Date());
		jJTestcaseexecutionService.updateJJTestcaseexecution(tce);

		List<JJTask> tasks = jJTaskService.getTasks(null, null, null, null,
				tce.getTestcase(), tce.getBuild(), true, false, true);
		if (!tasks.isEmpty()) {
			JJTask task = tasks.get(0);
			task.setName(tce.getTestcase().getName() + "_"
					+ tce.getBuild().getName().trim().toUpperCase());

			task.setEndDateReal(new Date());

			long startTime = task.getStartDateReal().getTime();
			long endTime = task.getEndDateReal().getTime();
			long str = endTime - startTime;

			int workloadReal = (int) (str / 3600000);

			task.setWorkloadReal(workloadReal);
			
			task.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(task);
		}

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

	public Set<JJTestcaseexecution> getTestcaseexecutions() {
		Set<JJTestcaseexecution> testcaseexecutions = new HashSet<JJTestcaseexecution>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		return testcaseexecutions = jJTestcaseexecutionService
				.getTestcaseexecutions(jJTestcaseBean.getChapter(),
						jJBuildBean.getBuild(), true, true);
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

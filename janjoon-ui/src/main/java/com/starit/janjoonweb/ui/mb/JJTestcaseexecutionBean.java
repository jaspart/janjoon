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

	private List<JJTestcaseexecution> testcaseexecutions;

	public JJTestcaseexecution getTestcaseexecution() {
		return testcaseexecution;
	}

	public void setTestcaseexecution(JJTestcaseexecution testcaseexecution) {
		this.testcaseexecution = testcaseexecution;
	}

	public List<JJTestcaseexecution> getTestcaseexecutions() {
		testcaseexecutions = new ArrayList<JJTestcaseexecution>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJBuild build = jJBuildBean.getBuild();

		if (testcase != null && testcase.getId() != null) {

			testcaseexecutions = jJTestcaseexecutionService
					.getTestcaseexecutions(testcase, build, true, true);
		}

		return testcaseexecutions;
	}

	public void setTestcaseexecutions(
			List<JJTestcaseexecution> testcaseexecutions) {
		this.testcaseexecutions = testcaseexecutions;
	}

	public void newTestcaseexecution() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		testcaseexecution = new JJTestcaseexecution();
		testcaseexecution.setName(testcase.getName());
		testcaseexecution.setDescription(testcase.getDescription());
		testcaseexecution.setCreationDate(new Date());
		testcaseexecution.setEnabled(true);
		testcaseexecution.setTestcase(testcase);

		testcaseexecution.setPassed(null);

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJBuild build = jJBuildBean.getBuild();

		testcaseexecution.setBuild(build);

		jJTestcaseexecutionService.saveJJTestcaseexecution(testcaseexecution);

	}

}

package com.starit.janjoonweb.ui.mb;

import java.util.Date;

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

	public JJTestcaseexecution getTestcaseexecution() {
		return testcaseexecution;
	}

	public void setTestcaseexecution(JJTestcaseexecution testcaseexecution) {
		this.testcaseexecution = testcaseexecution;
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

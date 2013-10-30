package com.funder.janjoonweb.ui.mb;

import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTestcaseexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcaseexecution.class, beanName = "jJTestcaseexecutionBean")
public class JJTestcaseexecutionBean {

	private JJBuild currentBuild;
	private JJTestcaseexecution jJTestcaseexecution;

	public JJBuild getCurrentBuild() {
		return currentBuild;
	}

	public void setCurrentBuild(JJBuild currentBuild) {
		this.currentBuild = currentBuild;
	}

	public JJTestcaseexecution getjJTestcaseexecution() {
		return jJTestcaseexecution;
	}

	public void setjJTestcaseexecution(JJTestcaseexecution jJTestcaseexecution) {
		this.jJTestcaseexecution = jJTestcaseexecution;
	}

	public void initParameter() {
		jJTestcaseexecution = null;
	}

	public void createJJTestcaseexecution(JJTestcase jJTestcase) {
		JJTestcaseexecution testcaseexecution = new JJTestcaseexecution();
		testcaseexecution.setBuild(currentBuild);
		testcaseexecution.setPassed(true);
		testcaseexecution.setName(jJTestcase.getName());
		testcaseexecution.setDescription("TEST CASE EXECUTION");
		testcaseexecution.setCreationDate(new Date());
		testcaseexecution.setTestcase(jJTestcase);
		testcaseexecution.setEnabled(true);

		jJTestcaseexecutionService.saveJJTestcaseexecution(testcaseexecution);

		jJTestcaseexecution = jJTestcaseexecutionService
				.findJJTestcaseexecution(testcaseexecution.getId());

	}
}

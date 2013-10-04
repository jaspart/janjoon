package com.funder.janjoonweb.ui.mb;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.TreeNode;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJTestcase;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	private JJTestcase jJTestCase = new JJTestcase();
	
	private TreeNode testCaseRootNode;
	private TreeNode testCaseSelectedNode;
	

	public JJTestcase getjJTestCase() {
		return jJTestCase;
	}

	public void setjJTestCase(JJTestcase jJTestCase) {
		this.jJTestCase = jJTestCase;
	}

	

	public TreeNode getTestCaseRootNode() {
		return testCaseRootNode;
	}

	public void setTestCaseRootNode(TreeNode testCaseRootNode) {
		this.testCaseRootNode = testCaseRootNode;
	}

	public TreeNode getTestCaseSelectedNode() {
		return testCaseSelectedNode;
	}

	public void setTestCaseSelectedNode(TreeNode testCaseSelectedNode) {
		this.testCaseSelectedNode = testCaseSelectedNode;
	}


	public void persistTestCase(ActionEvent actionEvent) {

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage("Welcome " + jJTestCase.getName() + " "
						+ jJTestCase.getDescription() + "!"));

		System.out.println(jJTestCase.getName());
	}
}

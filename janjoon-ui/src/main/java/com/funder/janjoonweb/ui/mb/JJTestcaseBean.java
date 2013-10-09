package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTeststep;
import com.funder.janjoonweb.domain.JJTeststepService;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	private JJTestcase jJTestCase = new JJTestcase();

	private TreeNode testCaseRootNode;
	private TreeNode testCaseSelectedNode;

	private String mode = "New";
	private String name = null;
	private String description = null;

	private List<String> tmpJJTeststepList = new ArrayList<String>();

	@Autowired
	JJTeststepService jJTeststepService;

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	private void initTestCaseParameter(JJTeststepBean jJTeststepBean) {

		System.out.println("INIT");

		testCaseRootNode = new DefaultTreeNode("Root", null);

		List<JJTestcase> testcases = jJTestcaseService.getAllJJTestcase();

		for (JJTestcase jjTestcase : testcases) {

			TreeNode newNode = new DefaultTreeNode("TC-" + jjTestcase.getId()
					+ "- " + jjTestcase.getName(), testCaseRootNode);

			// Set<JJTeststep> teststeps = jjTestcase.getTeststeps();
			List<JJTeststep> teststeps = jJTeststepService
					.getJJTeststepWithJJTestcase(jJTestCase);
			for (JJTeststep jjTeststep : teststeps) {
				TreeNode newNode1 = new DefaultTreeNode("TS-"
						+ jjTeststep.getId() + "- "
						+ jjTeststep.getActionstep(), newNode);
			}
		}

		mode = "New";
		jJTestCase = new JJTestcase();
		name = null;
		description = null;
		tmpJJTeststepList = new ArrayList<String>();
		jJTeststepBean.initTestStepParameter();

	}

	public void editNode(JJTeststepBean jJTeststepBean) {

		long idjJTestCase;

		if (testCaseSelectedNode != null) {

			mode = "Edit";
			name = null;
			description = null;
			tmpJJTeststepList = new ArrayList<String>();

			idjJTestCase = Long.parseLong(getStringFromString(
					testCaseSelectedNode.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);

			name = jJTestCase.getName();
			description = jJTestCase.getDescription();

			jJTeststepBean.initTestStepParameter();

			// Set<JJTeststep> list = jJTestCase.getTeststeps();

			List<JJTeststep> list = jJTeststepService
					.getJJTeststepWithJJTestcase(jJTestCase);
			for (JJTeststep jjTeststep : list) {
				jJTeststepBean.getTestStepList().add(jjTeststep);
				tmpJJTeststepList.add(getFromString(jjTeststep));
			}

		}

	}

	public void persistTestCase(JJTeststepBean jJTeststepBean) {

		String message;
		FacesMessage fmsg = null;
		if (mode.equalsIgnoreCase("New")) {

			if (jJTeststepBean != null) {
				List<JJTeststep> list = jJTeststepBean.getTestStepList();
				System.out.println("\n list.size() " + list.size());

				List<JJTeststep> tmplist = new ArrayList<JJTeststep>();

				for (JJTeststep element : list) {

					jJTeststepService.saveJJTeststep(element);

					JJTeststep teststep = jJTeststepService
							.findJJTeststep(element.getId());
					tmplist.add(teststep);
				}

				for (JJTeststep jjTeststep : tmplist) {
					jjTeststep.setTestcase(jJTestCase);
				}

				Set<JJTeststep> teststeps = new HashSet<JJTeststep>();
				teststeps.addAll(tmplist);
				jJTestCase.setTeststeps(teststeps);
			}

			jJTestCase.setCreationDate(new Date());
			jJTestCase.setEnabled(true);

			message = "New " + jJTestCase.getName() + " Saved !";
			fmsg = new FacesMessage(message, jJTestCase.getName());

			jJTestcaseService.saveJJTestcase(jJTestCase);

		} else if (mode.equalsIgnoreCase("Edit")) {
			message = "Edit " + jJTestCase.getName() + " Saved !";
			fmsg = new FacesMessage(message, jJTestCase.getName());

			List<JJTeststep> list = jJTeststepBean.getTestStepList();

			Set<JJTeststep> finalTestSteplist = new HashSet<JJTeststep>();

			JJTestcase finalTestCase = new JJTestcase();

			if (name.equalsIgnoreCase(jJTestCase.getName())
					&& description
							.equalsIgnoreCase(jJTestCase.getDescription())) {
				finalTestCase = jJTestCase;
			} else {

				JJTestcase tmpJJTestcase = new JJTestcase();
				tmpJJTestcase.setEnabled(true);
				tmpJJTestcase.setCreationDate(jJTestCase.getCreationDate());
				tmpJJTestcase.setUpdatedDate(new Date());
				tmpJJTestcase.setName(jJTestCase.getName());
				tmpJJTestcase.setDescription(jJTestCase.getDescription());
				jJTestcaseService.saveJJTestcase(tmpJJTestcase);
				jJTestCase.setEnabled(false);
				jJTestcaseService.updateJJTestcase(jJTestCase);
				finalTestCase = jJTestcaseService.findJJTestcase(tmpJJTestcase.getId());
			}

			for (JJTeststep jjTeststep : list) {

				String str = getFromString(jjTeststep);

				for (String element : tmpJJTeststepList) {
					String id = getStringFromString(str, 0);
					String action = getStringFromString(str, 1);
					String result = getStringFromString(str, 2);
					String order = getStringFromString(str, 3);
					if (element.startsWith(id)) {

						if (getStringFromString(element, 1).equalsIgnoreCase(
								action)
								&& getStringFromString(element, 2)
										.equalsIgnoreCase(result)
								&& getStringFromString(element, 3)
										.equalsIgnoreCase(order)) {

							// jjTeststep.setTestcase(finalTestCase);
							// jJTeststepService.updateJJTeststep(jjTeststep);
							finalTestSteplist.add(jjTeststep);
							break;
						} else {
							JJTeststep ts = new JJTeststep();
							ts.setCreationDate(jjTeststep.getCreationDate());
							ts.setUpdatedDate(new Date());
							ts.setActionstep(jjTeststep.getActionstep());
							ts.setName(jjTeststep.getName());
							ts.setResultat(jjTeststep.getResultat());
							ts.setOrdering(jjTeststep.getOrdering());
							ts.setEnabled(true);
							ts.setDescription("TOTO");
							jJTeststepService.saveJJTeststep(ts);

							finalTestSteplist.add(jJTeststepService
									.findJJTeststep(jjTeststep.getId()));

							jjTeststep.setEnabled(false);
							jJTeststepService.updateJJTeststep(jjTeststep);

						}

					} else {
						jJTeststepService.saveJJTeststep(jjTeststep);
						Long Id = jjTeststep.getId();
						jjTeststep = jJTeststepService.findJJTeststep(Id);
						// jjTeststep.setTestcase(finalTestCase);
						// jJTeststepService.updateJJTeststep(jjTeststep);
						finalTestSteplist.add(jjTeststep);
					}
				}

			}
			for (JJTeststep element : finalTestSteplist) {

				element.setTestcase(finalTestCase);
				jJTeststepService.updateJJTeststep(element);

			}

			finalTestCase.setTeststeps(finalTestSteplist);
			jJTestcaseService.updateJJTestcase(finalTestCase);

		}

		FacesContext.getCurrentInstance().addMessage(null, fmsg);
		System.out.println("**********************************");

		List<JJTestcase> list = jJTestcaseService.getAllJJTestcase();
		System.out.println("\n listJJTestcase.size() " + list.size());
		for (JJTestcase jjTestcase : list) {
			System.out.println("\n jjTestcase.getName() "
					+ jjTestcase.getName());
			Set<JJTeststep> list5 = jjTestcase.getTeststeps();
			for (JJTeststep jjTeststep : list5) {
				System.out.println(jjTeststep.getActionstep() + " - "
						+ jjTeststep.getResultat());
			}
		}

		initTestCaseParameter(jJTeststepBean);

	}

	public void deleteNode(JJTeststepBean jJTeststepBean) {

		long idjJTestCase;
		JJTestcase jJTestCase;

		if (testCaseSelectedNode != null) {

			idjJTestCase = Long.parseLong(getStringFromString(
					testCaseSelectedNode.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);
			jJTestCase.setEnabled(false);
			jJTestcaseService.updateJJTestcase(jJTestCase);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Deleted"
							+ getStringFromString(testCaseSelectedNode
									.getData().toString(), 2), "Deleted"
							+ testCaseSelectedNode.getData());
			FacesContext.getCurrentInstance().addMessage(null, message);

			initTestCaseParameter(jJTeststepBean);

		}

	}

	public void onNodeSelect(NodeSelectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected " + event.getTreeNode().toString(), event
						.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private String getStringFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

	private String getFromString(JJTeststep jjTeststep) {
		String str = jjTeststep.getId() + "-" + jjTeststep.getActionstep()
				+ "-" + jjTeststep.getResultat() + "-"
				+ jjTeststep.getOrdering();
		return str;
	}

}

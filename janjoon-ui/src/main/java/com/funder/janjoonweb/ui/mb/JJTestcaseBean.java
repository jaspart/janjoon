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

			Set<JJTeststep> teststeps = jjTestcase.getTeststeps();
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

			if (name.equalsIgnoreCase(jJTestCase.getName())
					&& description
							.equalsIgnoreCase(jJTestCase.getDescription())) {

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
			}

		}

		FacesContext.getCurrentInstance().addMessage(null, fmsg);

		// List<JJTestcase> list = jJTestcaseService.getAllJJTestcase();
		// System.out.println("\n listJJTestcase.size() " + list.size());
		// for (JJTestcase jjTestcase : list) {
		// System.out.println("\n jjTestcase.getName() "
		// + jjTestcase.getName());
		// Set<JJTeststep> list5 = jjTestcase.getTeststeps();
		// for (JJTeststep jjTeststep : list5) {
		// System.out.println(jjTeststep.getActionstep() + " "
		// + jjTeststep.getResultat());
		// }
		// }

		initTestCaseParameter(jJTeststepBean);

	}

	public void editNode(JJTeststepBean jJTeststepBean) {
		// persistIndex = 2;
		// Long idChapter = Long.parseLong(getIdFromString(selectedChapterNode
		// .getData().toString(), 1));
		// JJChapter chapter = jJChapterService.findJJChapter(idChapter);
		//
		// myJJChapter = chapter;
		// // myJJChapter.setName(editJJChapter.getName());
		// // myJJChapter.setCreationDate(editJJChapter.getCreationDate());
		// // myJJChapter.setUpdatedDate(new Date());
		// // myJJChapter.setEnabled(true);
		// // myJJChapter.setDescription(editJJChapter.getDescription());
		// // myJJChapter.setParent(editJJChapter.getParent());
		// // myJJChapter.setChapters(myJJChapter.getChapters());
		// // myJJChapter.setRequirements(myJJChapter.getRequirements());
		// // myJJChapter.setCategory(editJJChapter.getCategory());
		// // myJJChapter.setProduct(editJJChapter.getProduct());
		// // myJJChapter.setProject(editJJChapter.getProject());

		long idjJTestCase;

		if (testCaseSelectedNode != null) {

			mode = "Edit";
			name = null;
			description = null;
			tmpJJTeststepList = new ArrayList<String>();

			idjJTestCase = Long.parseLong(getIdFromString(testCaseSelectedNode
					.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);

			name = jJTestCase.getName();
			description = jJTestCase.getDescription();

			jJTeststepBean.initTestStepParameter();

			Set<JJTeststep> list = jJTestCase.getTeststeps();
			for (JJTeststep jjTeststep : list) {
				jJTeststepBean.getTestStepList().add(jjTeststep);
				tmpJJTeststepList.add(jjTeststep.getId() + "-"
						+ jjTeststep.getActionstep() + "-"
						+ jjTeststep.getResultat() + "-"
						+ jjTeststep.getOrdering());
			}

		}

	}

	public void deleteNode(JJTeststepBean jJTeststepBean) {

		long idjJTestCase;
		JJTestcase jJTestCase;

		if (testCaseSelectedNode != null) {

			idjJTestCase = Long.parseLong(getIdFromString(testCaseSelectedNode
					.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);
			jJTestCase.setEnabled(false);
			jJTestcaseService.updateJJTestcase(jJTestCase);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Deleted"
							+ getIdFromString(testCaseSelectedNode.getData()
									.toString(), 2), "Deleted"
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

	private String getIdFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

}

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

import com.funder.janjoonweb.domain.JJBug;
import com.funder.janjoonweb.domain.JJBugService;
import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTestcaseexecution;
import com.funder.janjoonweb.domain.JJTestcaseexecutionService;
import com.funder.janjoonweb.domain.JJTeststep;
import com.funder.janjoonweb.domain.JJTeststepService;
import com.funder.janjoonweb.domain.JJTeststepexecution;
import com.funder.janjoonweb.domain.JJTeststepexecutionService;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	private JJTestcase jJTestCase = new JJTestcase();

	private JJTestcase selectedTestcase;

	private JJProject currentProject;
	private JJBuild currentBuild;

	private JJChapter chapter = null;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private String mode = "New";
	private String name = null;
	private String description = null;

	private int tabIndex = 0;

	private boolean disabled = true;
	private boolean passed = true;
	private boolean disabledQuit = true;
	private boolean renderedComment = false;

	private List<String> tmpJJTeststepList = new ArrayList<String>();

	@Autowired
	JJTeststepService jJTeststepService;

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	@Autowired
	JJTeststepexecutionService jJTeststepexecutionService;

	public void setjJTeststepexecutionService(
			JJTeststepexecutionService jJTeststepexecutionService) {
		this.jJTeststepexecutionService = jJTeststepexecutionService;
	}

	@Autowired
	JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	@Autowired
	JJBugService jJBugService;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public JJTestcase getjJTestCase() {
		return jJTestCase;
	}

	public void setjJTestCase(JJTestcase jJTestCase) {
		this.jJTestCase = jJTestCase;
	}

	public JJProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(JJProject currentProject) {
		this.currentProject = currentProject;
	}

	public JJBuild getCurrentBuild() {
		return currentBuild;
	}

	public void setCurrentBuild(JJBuild currentBuild) {
		this.currentBuild = currentBuild;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean getPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public boolean getDisabledQuit() {
		return disabledQuit;
	}

	public void setDisabledQuit(boolean disabledQuit) {
		this.disabledQuit = disabledQuit;
	}

	public boolean getRenderedComment() {
		return renderedComment;
	}

	public void setRenderedComment(boolean renderedComment) {
		this.renderedComment = renderedComment;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public void initTestCaseParameter(JJTeststepBean jJTeststepBean) {

		System.out.println("INIT");
		rootNode = new DefaultTreeNode("Root", null);
		if (currentProject != null) {

			TreeNode projectNode = new DefaultTreeNode("P-"
					+ currentProject.getId() + "- " + currentProject.getName(),
					rootNode);

			List<JJCategory> categorys = jJCategoryService.getAllJJCategorys();
			for (JJCategory jjCategory : categorys) {

				TreeNode categoryNode = new DefaultTreeNode("C-"
						+ jjCategory.getId() + "- " + jjCategory.getName(),
						projectNode);

				List<JJChapter> chapters = jJChapterService
						.getAllJJChaptersWithProjectAndCategory(currentProject,
								jjCategory);

				for (JJChapter jjChapter : chapters) {
					TreeNode chapterNode = new DefaultTreeNode("CH-"
							+ jjChapter.getId() + "- " + jjChapter.getName(),
							categoryNode);

					List<JJTestcase> testcases = jJTestcaseService
							.getAllJJTestcasesWithChapter(jjChapter);

					for (JJTestcase jjTestcase : testcases) {

						TreeNode testcaseNode = new DefaultTreeNode("TC-"
								+ jjTestcase.getId() + "- "
								+ jjTestcase.getName(), chapterNode);

						List<JJTeststep> teststeps = jJTeststepService
								.getJJTeststepWithTestcase(jjTestcase);
						for (JJTeststep jjTeststep : teststeps) {
							TreeNode teststepNode = new DefaultTreeNode("TS-"
									+ jjTeststep.getId() + "- "
									+ jjTeststep.getActionstep(), testcaseNode);
						}
					}
				}
			}
		}

		mode = "New";
		disabled = true;
		jJTestCase = new JJTestcase();
		name = null;
		description = null;
		chapter = null;
		tmpJJTeststepList = new ArrayList<String>();
		jJTeststepBean.initTestStepParameter();

		tabIndex = 0;

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
			jJTestCase.setChapter(chapter);

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
				finalTestCase = jJTestcaseService.findJJTestcase(tmpJJTestcase
						.getId());
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

		List<JJTestcase> list = jJTestcaseService.getAllJJTestcases();
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

	public void editTestcase(JJTeststepBean jJTeststepBean) {

		long idjJTestCase;

		if (selectedNode != null
				&& selectedNode.getData().toString().startsWith("TC")) {

			mode = "Edit";
			name = null;
			description = null;
			tmpJJTeststepList = new ArrayList<String>();

			idjJTestCase = Long.parseLong(getStringFromString(selectedNode
					.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);

			name = jJTestCase.getName();
			description = jJTestCase.getDescription();

			jJTeststepBean.initTestStepParameter();

			// Set<JJTeststep> list = jJTestCase.getTeststeps();

			List<JJTeststep> list = jJTeststepService
					.getJJTeststepWithTestcase(jJTestCase);
			for (JJTeststep jjTeststep : list) {
				jJTeststepBean.getTestStepList().add(jjTeststep);
				tmpJJTeststepList.add(getFromString(jjTeststep));
			}

		}

	}

	public void deleteTestcase(JJTeststepBean jJTeststepBean) {

		long idjJTestCase;
		JJTestcase jJTestCase;

		if (selectedNode != null
				&& selectedNode.getData().toString().startsWith("TC")) {

			idjJTestCase = Long.parseLong(getStringFromString(selectedNode
					.getData().toString(), 1));

			System.out.println(idjJTestCase);

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);
			jJTestCase.setEnabled(false);
			jJTestcaseService.updateJJTestcase(jJTestCase);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Deleted"
							+ getStringFromString(selectedNode.getData()
									.toString(), 2), "Deleted"
							+ selectedNode.getData());
			FacesContext.getCurrentInstance().addMessage(null, message);

		}

	}

	public void newTestcase(JJTeststepBean jJTeststepBean) {
		if (selectedNode != null
				&& selectedNode.getData().toString().startsWith("CH")) {
			System.out.println("New testcase");
			initTestCaseParameter(jJTeststepBean);

			long idjJChapter = Long.parseLong(getStringFromString(selectedNode
					.getData().toString(), 1));

			System.out.println(idjJChapter);

			chapter = jJChapterService.findJJChapter(idjJChapter);
			jJTestCase.setChapter(chapter);
		}
	}

	public void onNodeSelect(NodeSelectEvent event) {

		String selectedNode = event.getTreeNode().toString();
		System.out.println("selectedNode " + selectedNode);

		if (selectedNode.startsWith("TC") && currentBuild != null) {
			disabled = false;

		} else {
			disabled = true;

		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected " + event.getTreeNode().toString(), event
						.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void createTabs(JJTestcaseexecutionBean jJTestcaseexecutionBean,
			JJTeststepexecutionBean jJTeststepexecutionBean, JJBugBean jJBugBean) {
		if (selectedNode.getData().toString().startsWith("TC")) {

			jJTestcaseexecutionBean.initParameter();
			jJTeststepexecutionBean.initParameter();

			passed = true;
			renderedComment = !passed;

			selectedTestcase = null;

			long idjJTestCase;
			JJTestcase jJTestCase;

			idjJTestCase = Long.parseLong(getStringFromString(selectedNode
					.getData().toString(), 1));

			jJTestCase = jJTestcaseService.findJJTestcase(idjJTestCase);

			selectedTestcase = jJTestCase;

			// Verify if this testcase has a testcaseexecution
			JJTestcaseexecution jJTestcaseexecution = jJTestcaseexecutionService
					.getTestcaseexecutionWithTestcaseAndBuild(jJTestCase,
							currentBuild);

			if (jJTestcaseexecution == null) {
				System.out.println("jJTestcaseexecution is null");
				jJTestcaseexecutionBean.createJJTestcaseexecution(jJTestCase);
			} else {
				System.out.println("jJTestcaseexecution is not null");
				jJTestcaseexecutionBean
						.setjJTestcaseexecution(jJTestcaseexecution);
			}

			List<JJTeststep> jJTeststepList = jJTeststepService
					.getJJTeststepWithTestcase(jJTestCase);

			if (jJTeststepList.size() > 0) {
				tabIndex = 0;
			}

			for (JJTeststep jJTeststep : jJTeststepList) {
				JJTeststepexecution jJTeststepexecution = jJTeststepexecutionService
						.getTeststepexecutionWithTeststepAndBuild(jJTeststep,
								currentBuild);
				if (jJTeststepexecution == null) {
					System.out.println("jJTeststepexecution is null");
					jJTeststepexecutionBean
							.createJJTeststepexecution(jJTeststep);
				} else {
					System.out.println("jJTeststepexecution is not null");
					jJTeststepexecutionBean
							.insertJJTeststepexecution(jJTeststepexecution);
				}

			}

			for (JJTeststepexecution jJTeststepexecution : jJTeststepexecutionBean
					.getjJTeststepexecutionList()) {
				if (!jJTeststepexecution.getPassed()) {
					tabIndex = jJTeststepexecution.getTeststep().getOrdering() - 1;
					passed = false;
					JJBug jJBug = jJBugService.getBugWithTestcaseAndProject(
							jJTestCase, currentProject);
					if (jJBug != null) {
						System.out.println("Bug is not null");
						jJBugBean.setjJBug(jJBug);
					} else {
						jJBugBean.createJJBug(jJTestCase);
						System.out.println("Bug is null");
					}
					break;
				}

			}
			renderedComment = !passed;
			disabledQuit = passed;

		}
	}

	public void onTabChange(JJTeststepexecutionBean jJTeststepexecutionBean,
			JJBugBean jJBugBean) {
		System.out.println("?????????????tabIndex " + tabIndex);
		passed = jJTeststepexecutionBean.getjJTeststepexecutionList()
				.get(tabIndex).getPassed();
		disabledQuit = passed;
		renderedComment = !passed;
		JJTeststepexecution jJTeststepexecution = jJTeststepexecutionBean
				.getjJTeststepexecutionList().get(tabIndex);

		if (passed) {
			jJTeststepexecution.setPassed(true);
			jJTeststepexecutionService
					.updateJJTeststepexecution(jJTeststepexecution);
		} else {
			jJTeststepexecution.setPassed(false);
			jJTeststepexecutionService
					.updateJJTeststepexecution(jJTeststepexecution);
			JJBug jJBug = jJBugService.getBugWithTestcaseAndProject(
					selectedTestcase, currentProject);
			if (jJBug != null) {

				jJBugBean.setjJBug(jJBug);
			} else {
				jJBugBean.createJJBug(selectedTestcase);

			}
		}

		System.out.println("tabIndex " + tabIndex);
	}

	public void incrementTabIndex(
			JJTeststepexecutionBean jJTeststepexecutionBean, JJBugBean jJBugBean) {
		if (tabIndex < jJTeststepexecutionBean.getjJTeststepexecutionList()
				.size() - 1
				&& jJTeststepexecutionBean.getjJTeststepexecutionList().size() > 0)
			tabIndex++;

		passed = jJTeststepexecutionBean.getjJTeststepexecutionList()
				.get(tabIndex).getPassed();

		disabledQuit = passed;
		renderedComment = !passed;

		if (passed) {
			// cache bug comment and disable quit
		} else {
			// display bug comment and enable quit
		}

		System.out.println("tabIndex " + tabIndex);
	}

	public void managePassed(JJTeststepexecutionBean jJTeststepexecutionBean,
			JJBugBean jJBugBean) {
		System.out.println("//////////tabIndex " + tabIndex);
		disabledQuit = passed;
		renderedComment = !passed;

		JJTeststepexecution jJTeststepexecution = jJTeststepexecutionBean
				.getjJTeststepexecutionList().get(tabIndex);

		if (passed) {
			jJTeststepexecution.setPassed(true);
			jJTeststepexecutionService
					.updateJJTeststepexecution(jJTeststepexecution);
		} else {
			jJTeststepexecution.setPassed(false);
			jJTeststepexecutionService
					.updateJJTeststepexecution(jJTeststepexecution);
			JJBug jJBug = jJBugService.getBugWithTestcaseAndProject(
					selectedTestcase, currentProject);
			if (jJBug != null) {

				jJBugBean.setjJBug(jJBug);
			} else {
				jJBugBean.createJJBug(selectedTestcase);

			}
		}

	}

	public void quitRun(JJTestcaseexecutionBean jJTestcaseexecutionBean,
			JJTeststepexecutionBean jJTeststepexecutionBean, JJBugBean jJBugBean) {

		jJBugService.updateJJBug(jJBugBean.getjJBug());
		JJTestcaseexecution jJTestcaseexecution = jJTestcaseexecutionBean
				.getjJTestcaseexecution();
		jJTestcaseexecution.setPassed(false);
		jJTestcaseexecutionService
				.updateJJTestcaseexecution(jJTestcaseexecution);

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

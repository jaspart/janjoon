package com.starit.janjoonweb.ui.mb.util;

import java.util.List;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class SimulatorUtil {

	private Integer workloadAccumulated;
	private TreeNode treeObject;
	private TreeNode[] selectedObjects;
	private boolean displayProgress;
	private boolean displayDone;
	private boolean displayFinishedBug;
	private JJRequirementService jJRequirementService;
	private JJTestcaseService jjTestcaseService;
	private JJBugService jjBugService;
	private JJCategory category;

	public SimulatorUtil(JJRequirementService jJRequirementService,
			JJTestcaseService jjTestcaseService, JJBugService jjBugService,
			JJCategory category) {
		this.jJRequirementService = jJRequirementService;
		this.category = category;
		this.jjTestcaseService = jjTestcaseService;
		this.jjBugService = jjBugService;
		this.workloadAccumulated = 0;
		this.displayDone = false;
		this.displayProgress = false;
		this.displayFinishedBug = false;
		this.treeObject = initializeTreeObject();

	}

	public SimulatorUtil(JJRequirementService jJRequirementService,
			JJTestcaseService jjTestcaseService, JJBugService jjBugService,
			JJCategory category, boolean displayProg, boolean displayDone,
			boolean displayFinishedBug) {
		this.jJRequirementService = jJRequirementService;
		this.category = category;
		this.jjTestcaseService = jjTestcaseService;
		this.jjBugService = jjBugService;
		this.workloadAccumulated = 0;
		this.displayDone = displayDone;
		this.displayProgress = displayProg;
		this.displayFinishedBug = displayFinishedBug;
		this.treeObject = initializeTreeObject();

	}

	public Integer getWorkloadAccumulated() {
		return workloadAccumulated;
	}
	public void setWorkloadAccumulated(Integer workloadAccumulated) {
		this.workloadAccumulated = workloadAccumulated;
	}
	public TreeNode getTreeObject() {
		return treeObject;
	}
	public void setTreeObject(TreeNode treeObject) {
		this.treeObject = treeObject;
	}

	public TreeNode[] getSelectedObjects() {
		return selectedObjects;
	}

	public void setSelectedObjects(TreeNode[] selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

	public boolean isDisplayProgress() {
		return displayProgress;
	}

	public void setDisplayProgress(boolean displayProgress) {
		this.displayProgress = displayProgress;
	}

	public boolean isDisplayDone() {
		return displayDone;
	}

	public void setDisplayDone(boolean displayDone) {
		this.displayDone = displayDone;
	}

	public boolean isDisplayFinishedBug() {
		return displayFinishedBug;
	}

	public void setDisplayFinishedBug(boolean displayFinishedBug) {
		this.displayFinishedBug = displayFinishedBug;
	}

	private TreeNode initializeTreeObject() {
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		List<JJRequirement> list = jJRequirementService.getRequirements(
				LoginBean.getCompany(), category,
				loginBean.getAuthorizedMap("Requirement",
						LoginBean.getProject(), LoginBean.getProduct()),
				LoginBean.getVersion(), null, null, false, false, true, true,
				false, null);

		TreeNode root = new CheckboxTreeNode("Root", null);

		for (JJRequirement req : list) {
			req = JJRequirementBean.getRowState(req, jJRequirementService);
			if (verifyCondition(req)) {
				TreeNode principal = new CheckboxTreeNode("JJRequirement", req,
						root);

				for (JJRequirement r : req.getRequirementLinkUp()) {
					if (r.getEnabled())
						loadLinkedData(r, principal);
				}

				for (JJTestcase test : jjTestcaseService.getJJtestCases(req))
					new CheckboxTreeNode("JJTestcase", test, principal);

				for (JJBug bug : jjBugService.getRequirementBugs(req,
						LoginBean.getCompany(), LoginBean.getProject(),
						LoginBean.getProduct(), LoginBean.getVersion()))
					if (displayFinishedBug || (bug.getStatus() == null || !bug
							.getStatus().getName().equalsIgnoreCase("FIXED")))
						new CheckboxTreeNode("JJBug", bug, principal);
			}
		}

		return root;
	}

	private boolean verifyCondition(JJRequirement req) {
		boolean verified = true;
		verified = displayDone && displayProgress;
		if (!verified) {
			verified = displayDone || (req.getState() == null
					|| !req.getState().getName().equalsIgnoreCase("Finished"));

			if (verified)
				verified = displayProgress || (req.getState() == null || !req
						.getState().getName().equalsIgnoreCase("InProgress"));
		}
		return verified;

	}

	public void loadLinkedData(JJRequirement r, TreeNode principal) {

		r = JJRequirementBean.getRowState(r, jJRequirementService);
		if (verifyCondition(r)) {
			TreeNode father = new CheckboxTreeNode("JJRequirement", r,
					principal);

			for (JJRequirement req : r.getRequirementLinkUp()) {
				if (req.getEnabled())
					loadLinkedData(req, father);
			}

			for (JJTestcase test : jjTestcaseService.getJJtestCases(r))
				new CheckboxTreeNode("JJTestcase", test, father);

			for (JJBug bug : jjBugService.getRequirementBugs(r,
					LoginBean.getCompany(), LoginBean.getProject(),
					LoginBean.getProduct(), LoginBean.getVersion()))
				if (displayFinishedBug || (bug.getStatus() == null || !bug
						.getStatus().getName().equalsIgnoreCase("FIXED")))
					new CheckboxTreeNode("JJBug", bug, father);
		}

	}

}

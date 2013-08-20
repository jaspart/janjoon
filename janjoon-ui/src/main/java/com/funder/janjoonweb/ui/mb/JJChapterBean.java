package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJRequirementService;

@RooSerializable
@RooJsfManagedBean(entity = JJChapter.class, beanName = "jJChapterBean")
public class JJChapterBean {

	private JJChapter myJJChapter;
	private int creationColumnNumber;
	private JJProject currentProject;
	private JJProduct currentProduct;
	private JJCategory currentJJCategory;

	private TreeNode leftRoot;
	private TreeNode rightRoot;
	private TreeNode selectedLeftNode;
	private TreeNode selectedRightNode;

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public JJChapter getMyJJChapter() {
		return myJJChapter;
	}

	public void setMyJJChapter(JJChapter myJJChapter) {
		this.myJJChapter = myJJChapter;
	}

	public int getCreationColumnNumber() {
		return creationColumnNumber;
	}

	public void setCreationColumnNumber(int creationColumnNumber) {
		this.creationColumnNumber = creationColumnNumber;
	}

	public JJProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(JJProject currentProject) {
		this.currentProject = currentProject;
	}

	public JJProduct getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(JJProduct currentProduct) {
		this.currentProduct = currentProduct;
	}

	public TreeNode getLeftRoot() {
		return leftRoot;
	}

	public void setLeftRoot(TreeNode leftRoot) {
		this.leftRoot = leftRoot;
	}

	public TreeNode getRightRoot() {
		return rightRoot;
	}

	public void setRightRoot(TreeNode rightRoot) {
		this.rightRoot = rightRoot;
	}

	public TreeNode getSelectedLeftNode() {
		return selectedLeftNode;
	}

	public void setSelectedLeftNode(TreeNode selectedLeftNode) {
		this.selectedLeftNode = selectedLeftNode;
	}

	public TreeNode getSelectedRightNode() {
		return selectedRightNode;
	}

	public void setSelectedRightNode(TreeNode selectedRightNode) {
		this.selectedRightNode = selectedRightNode;
	}

	public void createJJChapter(int number, JJCategoryBean jJCategoryBean) {

		myJJChapter = new JJChapter();
		myJJChapter.setCreationDate(new Date());
		myJJChapter.setEnabled(true);

		this.creationColumnNumber = number;
		String category = null;
		switch (number) {
		case 1:
			category = "BUSINESS";
			break;
		case 2:
			category = "FUNCTIONAL";
			break;
		case 3:
			category = "TECHNICAL";
			break;

		default:
			break;
		}

		JJCategory myJJCategory = null;

		JJCategory jjCategory = jJCategoryService
				.getJJCategoryWithName(category);

		if (jjCategory != null)

			myJJCategory = jjCategory;
		else if (jJCategoryBean != null)
			myJJCategory = jJCategoryBean.createANDpersistJJCategory(category);
		currentJJCategory = myJJCategory;

		myJJChapter.setCategory(myJJCategory);

		if (currentProject != null)
			myJJChapter.setProject(currentProject);
		if (currentProduct != null)
			myJJChapter.setProduct(currentProduct);

		myJJChapter.setParent(null);

		treeBean(category);

	}

	public void persistJJChapter(int index) {

		if (index == 1) {
			jJChapterService.saveJJChapter(myJJChapter);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					myJJChapter.getName() + " is saved.", "Save Status");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		// else if (index == 2) {
		//
		// switch (editionColumnNumber) {
		// case 1:
		// mySelectedBusinessJJRequirement.setEnabled(false);
		// jJRequirementService
		// .updateJJRequirement(mySelectedBusinessJJRequirement);
		// break;
		// case 2:
		// mySelectedFunctionalJJRequirement.setEnabled(false);
		// jJRequirementService
		// .updateJJRequirement(mySelectedFunctionalJJRequirement);
		// break;
		// case 3:
		// mySelectedTechnicalJJRequirement.setEnabled(false);
		// jJRequirementService
		// .updateJJRequirement(mySelectedTechnicalJJRequirement);
		// break;
		//
		// default:
		// break;
		// }
		//
		// Set<JJRequirement> requirementsLink = new HashSet<JJRequirement>();
		// List<JJRequirement> tempList = new ArrayList<JJRequirement>();
		//
		// if (selectedBusinessJJRequirements.size() > 0) {
		// for (JJRequirement req : selectedBusinessJJRequirements)
		// req.setRequirementLink(myEditedJJRequirement);
		// tempList.addAll(tempList.size(), selectedBusinessJJRequirements);
		// }
		//
		// if (selectedFunctionalJJRequirements.size() > 0) {
		// for (JJRequirement req : selectedFunctionalJJRequirements)
		// req.setRequirementLink(myEditedJJRequirement);
		// tempList.addAll(tempList.size(),
		// selectedFunctionalJJRequirements);
		// }
		//
		// if (selectedTechnicalJJRequirements.size() > 0) {
		// for (JJRequirement req : selectedTechnicalJJRequirements)
		// req.setRequirementLink(myEditedJJRequirement);
		// tempList.addAll(tempList.size(),
		// selectedTechnicalJJRequirements);
		// }
		//
		// requirementsLink.addAll(tempList);
		// myEditedJJRequirement.setRequirementsLink(requirementsLink);
		//
		// jJRequirementService.saveJJRequirement(myEditedJJRequirement);
		// findAllJJRequirementsWithCategory(editionColumnNumber);
		// } else {
		//
		// switch (deleteColumnNumber) {
		// case 1:
		//
		// jJRequirementService
		// .updateJJRequirement(mySelectedBusinessJJRequirement);
		// break;
		// case 2:
		//
		// jJRequirementService
		// .updateJJRequirement(mySelectedFunctionalJJRequirement);
		// break;
		// case 3:
		//
		// jJRequirementService
		// .updateJJRequirement(mySelectedTechnicalJJRequirement);
		// break;
		//
		// default:
		// break;
		// }

		// findAllJJRequirementsWithCategory(deleteColumnNumber);
		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "This JJRequirement is deleted.", "Delete Status");
		//
		// FacesContext.getCurrentInstance().addMessage(null, message);
		// }
	}

	public List<JJChapter> completeChapter(String query) {
		List<JJChapter> suggestions = new ArrayList<JJChapter>();
		for (JJChapter jJChapter : jJChapterService
				.getAllJJChaptersWithProjectAndCategory(currentProject,
						currentJJCategory)) {
			String jJChapterStr = String.valueOf(jJChapter.getName() + " "
					+ jJChapter.getDescription() + " "
					+ jJChapter.getCreationDate() + " "
					+ jJChapter.getUpdatedDate());
			if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJChapter);
			}
		}
		return suggestions;
	}

	public void treeBean(String categoryName) {

		List<JJRequirement> jJRequirementList;
		if (currentProject != null)
			if (currentProduct != null)

				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndChapter(
								categoryName, currentProject, currentProduct);
			else
				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndChapter(
								categoryName, currentProject);
		else
			jJRequirementList = jJRequirementService
					.getAllJJRequirementsWithCategoryAndChapter(categoryName);

		leftRoot = new DefaultTreeNode("Root", null);

		for (int i = 0; i < jJRequirementList.size(); i++) {
			TreeNode node = new DefaultTreeNode(jJRequirementList.get(i)
					.getName(), leftRoot);
		}

		// TreeNode node0 = new DefaultTreeNode("Node 0", leftRoot);
		// TreeNode node1 = new DefaultTreeNode("Node 1", leftRoot);
		// TreeNode node2 = new DefaultTreeNode("Node 2", leftRoot);
		//
		// TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);
		// TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);
		//
		// TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);
		// TreeNode node11 = new DefaultTreeNode("Node 1.1", node1);
		//
		// TreeNode node000 = new DefaultTreeNode("Node 0.0.0", node00);
		// TreeNode node001 = new DefaultTreeNode("Node 0.0.1", node00);
		// TreeNode node010 = new DefaultTreeNode("Node 0.1.0", node01);
		//
		// TreeNode node100 = new DefaultTreeNode("Node 1.0.0", node10);
	}
}

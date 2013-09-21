package com.funder.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJChapterService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

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

	private TreeNode chapterRoot;
	private TreeNode selectedChapterNode;

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

	public TreeNode getChapterRoot() {
		return chapterRoot;
	}

	public void setChapterRoot(TreeNode chapterRoot) {
		this.chapterRoot = chapterRoot;
	}

	public TreeNode getSelectedChapterNode() {
		return selectedChapterNode;
	}

	public void setSelectedChapterNode(TreeNode selectedChapterNode) {
		this.selectedChapterNode = selectedChapterNode;
	}

	public void initParameter(int number, JJCategoryBean jJCategoryBean) {
		System.out.println("BEGIN INIT PARAMETER");

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
		chapterTreeBean(currentJJCategory.getName());
		createJJChapter();
		System.out.println("END INIT PARAMETER");
	}

	public void createJJChapter() {
		System.out.println("BEGIN CREATE JJCHAPTER");
		myJJChapter = new JJChapter();
		myJJChapter.setCreationDate(new Date());
		myJJChapter.setEnabled(true);
		myJJChapter.setCategory(currentJJCategory);

		if (currentProject != null)
			myJJChapter.setProject(currentProject);
		if (currentProduct != null)
			myJJChapter.setProduct(currentProduct);

		// myJJChapter.setParent(null);

		// treeBean(currentJJCategory.getName());
		System.out.println("END CREATE JJCHAPTER");
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

	public void chapterTreeBean(String categoryName) {
		chapterRoot = new DefaultTreeNode("Root", null);
		JJChapter parentChapter = null;

		if (currentProject != null) {
			if (currentProduct != null) {

				parentChapter = jJChapterService
						.getParentJJChapterWithProjectAndProductAndCategory(
								currentProject, currentProduct,
								currentJJCategory);

			} else {

				parentChapter = jJChapterService
						.getParentJJChapterWithProjectAndCategory(
								currentProject, currentJJCategory);
			}
		} else {

			parentChapter = jJChapterService
					.getParentJJChapterWithCategory(currentJJCategory);
		}

		Set<JJChapter> jJChapList = new HashSet<JJChapter>();
		if (parentChapter != null)
			jJChapList = parentChapter.getChapters();
		for (JJChapter jjChapter : jJChapList) {
			TreeNode node = createTree(jjChapter, chapterRoot);
		}

	}

	public void treeBean(String categoryName) {

		// Requirement Tree WHERE chapter = null
		leftRoot = new DefaultTreeNode("Root", null);

		// Chapter Tree
		rightRoot = new DefaultTreeNode("Root", null);

		List<JJRequirement> jJRequirementList;
		JJChapter parentChapter = null;
		// List<JJChapter> jJChapList;

		if (currentProject != null) {
			if (currentProduct != null) {

				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndChapter(
								categoryName, currentProject, currentProduct);
				// parentChapter = jJChapterService
				// .getParentJJChapterWithProjectAndProductAndCategory(
				// currentProject, currentProduct,
				// currentJJCategory);

			} else {
				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndChapter(
								categoryName, currentProject);
				// parentChapter = jJChapterService
				// .getParentJJChapterWithProjectAndCategory(
				// currentProject, currentJJCategory);
			}
		} else {
			jJRequirementList = jJRequirementService
					.getAllJJRequirementsWithCategoryAndChapter(categoryName);
			// parentChapter = jJChapterService
			// .getParentJJChapterWithCategory(currentJJCategory);
		}

		for (int i = 0; i < jJRequirementList.size(); i++) {
			TreeNode node = new DefaultTreeNode(jJRequirementList.get(i)
					.getName(), leftRoot);
		}

		System.out.println("parentChapter " + parentChapter);

		// TreeNode node = new DefaultTreeNode(parentChapter, rightRoot);

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

	public void onNodeSelect(NodeSelectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected", event.getTreeNode().getData().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void displaySelectedSingle() {
		if (selectedChapterNode != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Selected", selectedChapterNode.getData().toString());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void deleteNode() {

		if (selectedChapterNode != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Deleted", selectedChapterNode.getData().toString());

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		// selectedChapterNode.getChildren().clear();
		// selectedChapterNode.getParent().getChildren().remove(selectedChapterNode);
		// selectedChapterNode.setParent(null);
		// selectedChapterNode = null;
	}

	// Recursive function to create tree

	public TreeNode createTree(JJChapter treeObj, TreeNode rootNode) {
		TreeNode newNode = new DefaultTreeNode(treeObj, rootNode);

		Set<JJChapter> childNodes1 = treeObj.getChapters();

		for (JJChapter tt : childNodes1) {
			TreeNode newNode2 = createTree(tt, newNode);
		}

		return newNode;
	}

	public void preProcessPDF(Object document) throws IOException,
			BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.addTitle("THIS IS THE TITLE");
		Element elm;

		pdf.open();
		pdf.setPageSize(PageSize.A4);

		Font font = new Font(Font.COURIER, 10, Font.BOLD);
		font.setColor(new Color(0x92, 0x90, 0x83));

//		Chunk chunk = new Chunk("testing text element ", font);
//		chunk.setBackground(new Color(0xff, 0xe4, 0x00));
		

		Phrase phrase = new Phrase(20, "Business Specification");
		JJCategory category = jJCategoryService.getJJCategoryWithName("BUSINESS");
		List<JJChapter> list = jJChapterService
				.getAllJJChaptersWithProjectAndCategory(currentProject,
						category);

		for (JJChapter jjChapter : list) {
			Chunk chunk = new Chunk(jjChapter.getName(), font);
			chunk.setBackground(new Color(0xff, 0xe4, 0x00));
			
			
			phrase.add(chunk);
			
			Set<JJRequirement> listReq = jjChapter.getRequirements();
			for (JJRequirement jjRequirement : listReq) {
				Chunk chunk2 = new Chunk(jjRequirement.getName(), font);
				chunk2.setBackground(new Color(0xff, 0xe4, 0x00));
				phrase.add(chunk2);
			}
		
			
		}
		
//		for (int i = 0; i < 10; i++) {
//			Chunk chunk = new Chunk("testing text element ", font);
//			chunk.setBackground(new Color(0xff, 0xe4, 0x00));
//			phrase.add(chunk);
//		}

		Paragraph paragraph = new Paragraph(); // 1
		paragraph.add(phrase); // 2
		pdf.add(paragraph); // 3
		

		// ServletContext servletContext = (ServletContext)
		// FacesContext.getCurrentInstance().getExternalContext().getContext();
		// String logo = servletContext.getRealPath("") + File.separator +
		// "images" + File.separator + "prime_logo.png";
		//
		// pdf.add(Image.getInstance(logo));
	}

	public void postProcessXLS(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			HSSFCell cell = header.getCell(i);

			cell.setCellStyle(cellStyle);
		}
	}
}

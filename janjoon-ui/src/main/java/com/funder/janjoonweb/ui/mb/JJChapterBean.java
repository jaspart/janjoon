package com.funder.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;

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
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.TreeDragDropEvent;
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
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.codec.Base64;
import com.lowagie.text.Image;

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
	
	private JJChapter parentChapter = null;

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
			TreeNode node = createTree(jjChapter, chapterRoot, 0);
		}

	}

	public void treeBean() {
		String categoryName = "BUSINESS";
		JJCategory category = jJCategoryService
				.getJJCategoryWithName("BUSINESS");
		// Requirement Tree WHERE chapter = null
		leftRoot = new DefaultTreeNode("RootRequirement", null);

		// Chapter Tree
		rightRoot = new DefaultTreeNode("RootChapter", null);

		List<JJRequirement> jJRequirementList;
		

		if (currentProject != null) {
			if (currentProduct != null) {

				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndChapter(
								categoryName, currentProject, currentProduct);
				parentChapter = jJChapterService
						.getParentJJChapterWithProjectAndProductAndCategory(
								currentProject, currentProduct, category);

			} else {
				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndChapter(
								categoryName, currentProject);
				parentChapter = jJChapterService
						.getParentJJChapterWithProjectAndCategory(
								currentProject, category);
			}
		} else {
			jJRequirementList = jJRequirementService
					.getAllJJRequirementsWithCategoryAndChapter(categoryName);
			parentChapter = jJChapterService
					.getParentJJChapterWithCategory(category);
		}

		for (JJRequirement jjRequirement : jJRequirementList) {
			TreeNode node = new DefaultTreeNode("R-" + jjRequirement.getId()
					+ "- " + jjRequirement.getName(), leftRoot);
		}

		Set<JJChapter> jJChapList = new HashSet<JJChapter>();
		if (parentChapter != null)
			jJChapList = parentChapter.getChapters();
		for (JJChapter jjChapter : jJChapList) {
			TreeNode node = createTree(jjChapter, rightRoot, 1);
		}
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

	public TreeNode createTree(JJChapter treeObj, TreeNode rootNode, int index) {
		TreeNode newNode;
		if (index == 0) {
			newNode = new DefaultTreeNode(treeObj, rootNode);
		} else {
			newNode = new DefaultTreeNode("C-" + treeObj.getId() + "- "
					+ treeObj.getName(), rootNode);
		}
		Set<JJChapter> childNodes1 = treeObj.getChapters();

		for (JJChapter tt : childNodes1) {
			TreeNode newNode2 = createTree(tt, newNode, index);
		}

		return newNode;
	}

	public void preProcessPDF(Object document) throws IOException,
			BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.addTitle("THIS IS THE TITLE");

		pdf.open();
		pdf.setPageSize(PageSize.A4);

		Font fontTitle = new Font(Font.COURIER, 30, Font.BOLD);
		fontTitle.setColor(new Color(0x92, 0x90, 0x83));
		Font fontChapter = new Font(Font.COURIER, 15, Font.BOLD);
		fontChapter.setColor(new Color(0x92, 0x90, 0x83));
		Font fontRequirement = new Font(Font.COURIER, 10, Font.BOLD);
		fontRequirement.setColor(new Color(0x92, 0x90, 0x83));
		Font fontNote = new Font(Font.COURIER, 8, Font.BOLD);
		fontNote.setColor(new Color(0x92, 0x90, 0x83));

		StyleSheet stylez = new StyleSheet();
		stylez.loadTagStyle("body", "font", "Times New Roman");

		Phrase phrase = new Phrase(20, "Business Specification \n"
				+ currentProject.getName() + "\n" + "\n");
		JJCategory category = jJCategoryService
				.getJJCategoryWithName("BUSINESS");
		List<JJChapter> list = jJChapterService
				.getAllJJChaptersWithProjectAndCategory(currentProject,
						category);

		for (JJChapter jjChapter : list) {
			phrase.add(new Chunk("\n" + jjChapter.getName() + "\n", fontChapter));
			phrase.add(new Chunk(jjChapter.getDescription() + "\n", fontNote));

			Set<JJRequirement> listReq = jjChapter.getRequirements();
			for (JJRequirement jjRequirement : listReq) {
				phrase.add(new Chunk(jjRequirement.getName() + "\n",
						fontRequirement));
				StringReader strReader = new StringReader(
						jjRequirement.getDescription());
				ArrayList arrList = HTMLWorker.parseToList(strReader, stylez);
				for (int i = 0; i < arrList.size(); ++i) {
					Element e = (Element) arrList.get(i);
					phrase.add(e);
				}
				URL adresseImage = new URL(
						"http://www.primefaces.org/showcase/images/logo.png");
				Image photoEtu = Image.getInstance(adresseImage);
				// phrase.add(photoEtu);

				phrase.add(new Chunk(jjRequirement.getNote() + "\n", fontNote));
			}
		}

		Paragraph paragraph = new Paragraph(); // 1
		paragraph.add(phrase); // 2
		pdf.add(paragraph); // 3
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

	public void onDragDrop(TreeDragDropEvent event) {
		TreeNode dragNode = event.getDragNode();
		TreeNode dropNode = event.getDropNode();
		int dropIndex = event.getDropIndex();

		String dragNodeData = dragNode.getData().toString();
		String dropNodeData = dropNode.getData().toString();

		if (dragNodeData.startsWith("R-")) {
			long idRequirement = Long
					.parseLong(getIdFromString(dragNodeData, 1));
			JJRequirement requirement = jJRequirementService
					.findJJRequirement(idRequirement);

			if (dropNodeData.startsWith("C-")) {
				long idChapter = Long
						.parseLong(getIdFromString(dropNodeData, 1));
				JJChapter chapter = jJChapterService.findJJChapter(idChapter);
				requirement.setChapter(chapter);
				jJRequirementService.updateJJRequirement(requirement);
			}

			if (dropNodeData.equalsIgnoreCase("RootChapter") && parentChapter!=null) {
				requirement.setChapter(parentChapter);
				jJRequirementService.updateJJRequirement(requirement);
			}

			if (dropNodeData.equalsIgnoreCase("RootRequirement") || dropNodeData.startsWith("R-")) {
				
				JJChapter tmpChapter = requirement.getChapter();
				if(tmpChapter!=null){
				JJChapter chapter = jJChapterService.findJJChapter(tmpChapter.getId());
				chapter.getRequirements().remove(requirement);
				requirement.setChapter(null);
				jJRequirementService.updateJJRequirement(requirement);
				jJChapterService.updateJJChapter(chapter);
				}
				
			}

		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dragged " + dragNode.getData() + "  \nDropped on "
						+ dropNode.getData() + " at " + dropIndex,
				"Dropped on " + dropNode.getData() + " at " + dropIndex);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private String getIdFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

}

package com.funder.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.event.TreeDragDropEvent;
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
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;

@RooSerializable
@RooJsfManagedBean(entity = JJChapter.class, beanName = "jJChapterBean")
public class JJChapterBean {

	private JJChapter myJJChapter;
	private JJProject currentProject;
	private JJProduct currentProduct;
	private JJCategory currentJJCategory;

	private int creationColumnNumber;
	private int persistIndex = 1;

	private TreeNode leftRoot;
	private TreeNode rightRoot;
	private TreeNode selectedLeftNode;
	private TreeNode selectedRightNode;

	private TreeNode chapterRoot;
	private TreeNode selectedChapterNode;

	private List<JJChapter> parentChapterList;

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

	public List<JJChapter> getParentChapterList() {
		return parentChapterList;
	}

	public void setParentChapterList(List<JJChapter> parentChapterList) {
		this.parentChapterList = parentChapterList;
	}

	public void initParameter(int number, JJCategoryBean jJCategoryBean) {
		persistIndex = 1;
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

		chapterTreeBean(myJJCategory);

		myJJChapter = new JJChapter();
		myJJChapter.setCreationDate(new Date());
		myJJChapter.setEnabled(true);
		myJJChapter.setCategory(myJJCategory);

		if (currentProject != null)
			myJJChapter.setProject(currentProject);
		if (currentProduct != null)
			myJJChapter.setProduct(currentProduct);

	}

	public void persistJJChapter() {
		if (persistIndex == 1) {
			jJChapterService.saveJJChapter(myJJChapter);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					myJJChapter.getName() + " is saved.", "Save Status");

			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			// editJJChapter.setEnabled(false);
			// jJChapterService.updateJJChapter(editJJChapter);
			// jJChapterService.updateJJChapter(myJJChapter);
			myJJChapter.setUpdatedDate(new Date());
			jJChapterService.updateJJChapter(myJJChapter);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					myJJChapter.getName() + " is edited.", "Edit Status");

			FacesContext.getCurrentInstance().addMessage(null, message);

		}
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

	public void chapterTreeBean(JJCategory category) {
		chapterRoot = new DefaultTreeNode("Root", null);
		List<JJChapter> parentChapters = null;

		if (currentProject != null) {
			if (currentProduct != null) {

				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndProductAndCategory(
								currentProject, currentProduct, category);

			} else {

				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndCategory(
								currentProject, category);
			}
		} else {

			parentChapters = jJChapterService
					.getAllParentJJChapterWithCategory(category);
		}

		for (JJChapter jjChapter : parentChapters) {
			TreeNode node = createTree(jjChapter, chapterRoot, 0);
		}

		// Set<JJChapter> jJChapList = new HashSet<JJChapter>();
		// if (parentChapter != null)
		// jJChapList = parentChapter.getChapters();
		// for (JJChapter jjChapter : jJChapList) {
		// TreeNode node = createTree(jjChapter, chapterRoot, 0);
		// }

	}

	public void treeBean(int index) {

		JJCategory category = getCategory(index);
		List<JJChapter> parentChapters = null;

		// Requirement Tree WHERE requirment.getChapter = null
		leftRoot = new DefaultTreeNode("RootRequirement", null);

		// Chapter Tree ALL Chapters and Requirements requirment.getChapter !=
		// null
		rightRoot = new DefaultTreeNode("RootChapter", null);

		List<JJRequirement> jJRequirementList;

		if (currentProject != null) {
			if (currentProduct != null) {

				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndChapter(
								category.getName(), currentProject,
								currentProduct);
				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndProductAndCategory(
								currentProject, currentProduct, category);

			} else {
				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndChapter(
								category.getName(), currentProject);
				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndCategory(
								currentProject, category);
			}
		} else {
			jJRequirementList = jJRequirementService
					.getAllJJRequirementsWithCategoryAndChapter(category
							.getName());
			parentChapters = jJChapterService
					.getAllParentJJChapterWithCategory(category);
		}

		for (JJRequirement jjRequirement : jJRequirementList) {
			TreeNode node = new DefaultTreeNode("R-" + jjRequirement.getId()
					+ "- " + jjRequirement.getName(), leftRoot);
		}

		for (JJChapter jjChapter : parentChapters) {
			TreeNode node = createTree(jjChapter, rightRoot, 1);
		}
	}

	// Recursive function to create tree
	public TreeNode createTree(JJChapter treeObj, TreeNode rootNode, int index) {
		TreeNode newNode;

		newNode = new DefaultTreeNode("C-" + treeObj.getId() + "- "
				+ treeObj.getName(), rootNode);

		Set<JJChapter> childNodes1 = treeObj.getChapters();

		for (JJChapter tt : childNodes1) {
			if (tt.getEnabled()) {
				TreeNode newNode2 = createTree(tt, newNode, index);
			}
		}

		if (index == 1) {
			Set<JJRequirement> childNodes2 = treeObj.getRequirements();
			for (JJRequirement jjRequirement : childNodes2) {
				if (jjRequirement.getEnabled()) {
					TreeNode newNode3 = new DefaultTreeNode("R-"
							+ jjRequirement.getId() + "- "
							+ jjRequirement.getName(), newNode);
				}
			}
		}

		return newNode;
	}

	public void editNode() {
		persistIndex = 2;
		Long idChapter = Long.parseLong(getIdFromString(selectedChapterNode
				.getData().toString(), 1));
		JJChapter chapter = jJChapterService.findJJChapter(idChapter);

		myJJChapter = chapter;
		// myJJChapter.setName(editJJChapter.getName());
		// myJJChapter.setCreationDate(editJJChapter.getCreationDate());
		// myJJChapter.setUpdatedDate(new Date());
		// myJJChapter.setEnabled(true);
		// myJJChapter.setDescription(editJJChapter.getDescription());
		// myJJChapter.setParent(editJJChapter.getParent());
		// myJJChapter.setChapters(myJJChapter.getChapters());
		// myJJChapter.setRequirements(myJJChapter.getRequirements());
		// myJJChapter.setCategory(editJJChapter.getCategory());
		// myJJChapter.setProduct(editJJChapter.getProduct());
		// myJJChapter.setProject(editJJChapter.getProject());
	}

	public void deleteNode(JJCategoryBean jJCategoryBean) {

		System.out.println("Delete node");

		long idChapter;
		JJChapter chapter;

		List<TreeNode> list = selectedChapterNode.getChildren();
		for (TreeNode treeNode : list) {

			idChapter = Long.parseLong(getIdFromString(treeNode.getData()
					.toString(), 1));
			chapter = jJChapterService.findJJChapter(idChapter);

			idChapter = Long.parseLong(getIdFromString(selectedChapterNode
					.getParent().getData().toString(), 1));
			JJChapter parent = jJChapterService.findJJChapter(idChapter);
			chapter.setParent(parent);

			jJChapterService.updateJJChapter(chapter);

		}

		idChapter = Long.parseLong(getIdFromString(selectedChapterNode
				.getData().toString(), 1));
		chapter = jJChapterService.findJJChapter(idChapter);
		chapter.setEnabled(false);
		jJChapterService.updateJJChapter(chapter);

		// selectedChapterNode.getChildren().clear();
		// selectedChapterNode.getParent().getChildren().remove(selectedChapterNode);
		// selectedChapterNode.setParent(null);
		// selectedChapterNode = null;

		initParameter(creationColumnNumber, jJCategoryBean);
		list = chapterRoot.getChildren();
		for (TreeNode treeNode : list) {
			treeNode.setExpanded(true);
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Deleted" + selectedChapterNode.getData(), "Deleted"
						+ selectedChapterNode.getData());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void onDragDrop(TreeDragDropEvent event) {
		TreeNode dragNode = event.getDragNode();
		TreeNode dropNode = event.getDropNode();
		int dropIndex = event.getDropIndex();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dragged " + dragNode.getData() + "  \nDropped on "
						+ dropNode.getData() + " at " + dropIndex,
				"Dropped on " + dropNode.getData() + " at " + dropIndex);
		FacesContext.getCurrentInstance().addMessage(null, message);

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

				if (requirement.getChapter() != chapter) {
					requirement.setChapter(chapter);
				}
				requirement.setOrdering(dropIndex);
				jJRequirementService.updateJJRequirement(requirement);

				List<TreeNode> list = dropNode.getChildren();
				List<String> listRequirement = new ArrayList<String>();
				for (TreeNode treeNode : list) {
					String treeNodeData = treeNode.getData().toString();
					if (treeNodeData.startsWith("R-")) {
						listRequirement.add(treeNodeData);
					}
				}
				for (int i = 0; i < listRequirement.size(); i++) {
					idRequirement = Long.parseLong(getIdFromString(
							listRequirement.get(i), 1));
					requirement = jJRequirementService
							.findJJRequirement(idRequirement);
					if (requirement.getOrdering() != i) {
						requirement.setOrdering(i);
						jJRequirementService.updateJJRequirement(requirement);
					}
				}

			}

			if (dropNodeData.equalsIgnoreCase("RootRequirement")) {
				JJChapter tmpChapter = requirement.getChapter();
				if (tmpChapter != null) {
					requirement.setChapter(null);
					requirement.setOrdering(0);
					jJRequirementService.updateJJRequirement(requirement);

				}

			}

		}

		// List<JJRequirement> req =
		// jJRequirementService.findAllJJRequirements();
		// for (JJRequirement jjRequirement : req) {
		// System.out.println(" \n jjRequirement.getName() "
		// + jjRequirement.getName());
		// System.out.println(" \n jjRequirement.getChapter().getName() "
		// + jjRequirement.getChapter().getName());
		// System.out.println(" \n jjRequirement.getOrdering() "
		// + jjRequirement.getOrdering());
		//
		// }

	}

	public void preProcessPDF(Object document) throws IOException,
			BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.addTitle("THIS IS THE TITLE");

		pdf.open();
		pdf.setPageSize(PageSize.A4);
		Paragraph paragraph = new Paragraph();

		Font fontTitle = new Font(Font.TIMES_ROMAN, 30, Font.BOLD);
		fontTitle.setColor(new Color(0x24, 0x14, 0x14));
		Font fontChapter = new Font(Font.HELVETICA, 15, Font.BOLD);
		fontChapter.setColor(new Color(0x4E, 0x4E, 0x4E));
		Font fontRequirement = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
		fontRequirement.setColor(new Color(0x5A, 0x5A, 0x5A));
		Font fontNote = new Font(Font.COURIER, 8, Font.BOLD);
		fontNote.setColor(new Color(0x82, 0x82, 0x82));

		/*Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 30, Font.BOLD, new BaseColor(20, 20, 20));
		Font fontChapter = new Font(FontFamily.HELVETICA, 15, Font.BOLD, new BaseColor(30, 30, 30));
		Font fontRequirement = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(10, 10, 10));
		Font fontNote = new Font(FontFamily.COURIER, 8, Font.BOLD, new BaseColor(50, 50, 50));*/

		StyleSheet style = new StyleSheet();
		style.loadTagStyle("body", "font", "Times New Roman");

		Phrase phrase = new Phrase(20, new Chunk("\n"+"Business Specification \n"+
			currentProject.getName()+"\n"+"\n"+"\n", fontChapter));
		JJCategory category = jJCategoryService.getJJCategoryWithName("BUSINESS");

		List<JJChapter> list = jJChapterService
				.getAllJJChaptersWithProjectAndCategory(currentProject,
						category);
		paragraph.add(phrase);

		for (JJChapter jjChapter : list) {
			paragraph.add(new Chunk("\n"+jjChapter.getName()+"\n", fontChapter));
			paragraph.add(new Chunk(jjChapter.getDescription()+"\n", fontNote));

			Set<JJRequirement> listReq = jjChapter.getRequirements();
			for (JJRequirement jjRequirement : listReq) {
				if(jjRequirement.getEnabled()) {
					paragraph.add(new Chunk(jjRequirement.getName()+"\n", fontRequirement));
					StringReader strReader = new StringReader(jjRequirement.getDescription());
					System.out.println("strReader = "+strReader.getClass().getName());
					ArrayList arrList = HTMLWorker.parseToList(strReader, style);
					paragraph.addAll(arrList);
					/*for (int i = 0; i < arrList.size(); ++i) {
						Element e = (Element) arrList.get(i);
						System.out.println("ArrayElement = "+e.getClass().getName());
						paragraph.add(e);
					}*/
					if(jjRequirement.getNote().length() > 2){
						paragraph.add("Note: "+new Chunk(jjRequirement.getNote()+"\n", fontNote));
					}
				}
			}
		}

		paragraph.add(phrase);
		Image image=Image.getInstance("http://www.google.com/intl/en_ALL/images/logos/images_logo_lg.gif");
		image.scaleToFit((float)200.0, (float)49.0);
		paragraph.add(image);
		pdf.add(paragraph);
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

	private JJCategory getCategory(int index) {
		JJCategory jjCategory = null;
		switch (index) {
		case 1:
			jjCategory = jJCategoryService.getJJCategoryWithName("BUSINESS");
			break;
		case 2:
			jjCategory = jJCategoryService.getJJCategoryWithName("FUNCTIONAL");
			break;
		case 3:
			jjCategory = jJCategoryService.getJJCategoryWithName("TECHNICAL");
			break;
		}
		return jjCategory;
	}

	private String getIdFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

}

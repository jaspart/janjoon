package com.funder.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.NodeSelectEvent;
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

	private JJChapter chapter;

	private JJProject project;
	private JJProduct product;
	private JJCategory category;

	private int creationColumnNumber;
	private int persistIndex = 1;

	private TreeNode leftRoot;
	private TreeNode rightRoot;
	private TreeNode selectedLeftNode;
	private TreeNode selectedRightNode;

	private TreeNode chapterRoot;
	private TreeNode selectedChapterNode;

	private List<JJChapter> chapterList;
	private JJChapter parentChapter;

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public JJChapter getChapter() {
		return chapter;
	}

	public void setChapter(JJChapter chapter) {
		this.chapter = chapter;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public int getCreationColumnNumber() {
		return creationColumnNumber;
	}

	public void setCreationColumnNumber(int creationColumnNumber) {
		this.creationColumnNumber = creationColumnNumber;
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

	public List<JJChapter> getChapterList() {
		if (product != null) {

			chapterList = jJChapterService
					.getAllJJChaptersWithProjectAndProductAndCategory(project,
							product, category);

		} else {

			chapterList = jJChapterService
					.getAllJJChaptersWithProjectAndCategory(project, category);
		}
		return chapterList;
	}

	public void setChapterList(List<JJChapter> chapterList) {
		this.chapterList = chapterList;
	}

	public JJChapter getParentChapter() {
		return parentChapter;
	}

	public void setParentChapter(JJChapter parentChapter) {
		this.parentChapter = parentChapter;
	}

	public void initParameter(int number) {
		persistIndex = 1;
		this.creationColumnNumber = number;
		String categoryName = null;
		switch (number) {
		case 1:
			categoryName = "BUSINESS";
			break;
		case 2:
			categoryName = "FUNCTIONAL";
			break;
		case 3:
			categoryName = "TECHNICAL";
			break;

		default:
			break;
		}

		JJCategory jjCategory = jJCategoryService
				.getJJCategoryWithName(categoryName);

		category = jjCategory;

		chapterTreeBean(jjCategory);

		chapter = new JJChapter();
		chapter.setCreationDate(new Date());
		chapter.setEnabled(true);
		chapter.setCategory(jjCategory);

		if (project != null)
			chapter.setProject(project);
		if (product != null)
			chapter.setProduct(product);

		parentChapter = null;
		transferTreeBean(number);
	}

	public void persistJJChapter() {
		if (persistIndex == 1) {
			jJChapterService.saveJJChapter(chapter);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					chapter.getName() + " is saved.", "Save Status");

			FacesContext.getCurrentInstance().addMessage(null, message);

			// List<JJChapter> list = jJChapterService
			// .getAllJJChaptersWithCategory(category);
			// for (JJChapter jjChapter : list) {
			// System.out.println("Chapter: " + jjChapter.getName());
			// // System.out.println("Parent: " +
			// // jjChapter.getParent().getName());
			// System.out.println("Childs: ");
			// Set<JJChapter> childs = jjChapter.getChapters();
			// for (JJChapter jjChapter2 : childs) {
			// System.out.println("Child Name: " + jjChapter2.getName()
			// + " Order: " + jjChapter2.getOrdering());
			// }
			// System.out.println("\n\n");
			// }

		} else {
			// editJJChapter.setEnabled(false);
			// jJChapterService.updateJJChapter(editJJChapter);
			// jJChapterService.updateJJChapter(chapter);
			chapter.setUpdatedDate(new Date());
			jJChapterService.updateJJChapter(chapter);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					chapter.getName() + " is edited.", "Edit Status");

			FacesContext.getCurrentInstance().addMessage(null, message);

		}

		initParameter(creationColumnNumber);

	}

	public List<JJChapter> completeChapter(String query) {
		List<JJChapter> suggestions = new ArrayList<JJChapter>();

		for (JJChapter jJChapter : jJChapterService
				.getAllJJChaptersWithProjectAndCategory(project, category)) {
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
		chapterRoot = new DefaultTreeNode("RootChapter", null);

		List<JJChapter> parentChapters = null;

		if (product != null) {

			parentChapters = jJChapterService
					.getAllParentJJChapterWithProjectAndProductAndCategorySortedByOrder(
							project, product, category);

		} else {

			parentChapters = jJChapterService
					.getAllParentJJChapterWithProjectAndCategorySortedByOrder(
							project, category);
		}

		for (JJChapter jjChapter : parentChapters) {
			TreeNode node = createTree(jjChapter, chapterRoot, 0);
		}

		// List<TreeNode> childrenlist = chapterRoot.getChildren();
		// for (TreeNode treeNode : childrenlist) {
		// treeNode.setExpanded(true);
		// }
	}

	public void transferTreeBean(int index) {

		JJCategory category = getCategory(index);
		List<JJChapter> parentChapters = null;

		// Requirement Tree WHERE requirment.getChapter = null
		leftRoot = new DefaultTreeNode("RootRequirement", null);

		// Chapter Tree ALL Chapters and Requirements requirment.getChapter !=
		// null
		rightRoot = new DefaultTreeNode("RootChapter", null);

		List<JJRequirement> jJRequirementList;

		if (project != null) {
			if (product != null) {

				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndChapter(
								category.getName(), project, product);
				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndProductAndCategory(
								project, product, category);

			} else {
				jJRequirementList = jJRequirementService
						.getAllJJRequirementsWithProjectAndChapter(
								category.getName(), project);
				parentChapters = jJChapterService
						.getAllParentJJChapterWithProjectAndCategory(project,
								category);
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

	public TreeNode createTreeTemp(JJChapter treeObj, TreeNode rootNode,
			int index) {
		TreeNode newNode;

		newNode = new DefaultTreeNode("C-" + treeObj.getId() + "- "
				+ treeObj.getName(), rootNode);

		SortedMap<Integer, Object> elements = jJChapterService
				.getAllElement(treeObj);
		System.out.println("elements.size() " + elements.size());

		for (Object element : elements.entrySet()) {
			System.out.println("element=" + element.getClass().getName());

		}

		//
		//
		// List<JJChapter> childNodes1 = jJChapterService
		// .getAllChildsJJChapterWithParentSortedByOrder(treeObj);
		//
		// for (JJChapter tt : childNodes1) {
		// if (tt.getEnabled()) {
		// TreeNode newNode2 = createTreeTemp(tt, newNode, index);
		//
		//
		// }
		// }
		//
		// if (index == 1) {
		// Set<JJRequirement> childNodes2 = treeObj.getRequirements();
		// for (JJRequirement jjRequirement : childNodes2) {
		// if (jjRequirement.getEnabled()) {
		// TreeNode newNode3 = new DefaultTreeNode("R-"
		// + jjRequirement.getId() + "- "
		// + jjRequirement.getName(), newNode);
		//
		// }
		// }
		// }
		// newNode.setExpanded(true);

		return newNode;
	}

	// Recursive function to create tree
	public TreeNode createTree(JJChapter treeObj, TreeNode rootNode, int index) {
		TreeNode newNode;

		newNode = new DefaultTreeNode("C-" + treeObj.getId() + "- "
				+ treeObj.getName(), rootNode);

		List<JJChapter> childNodes1 = jJChapterService
				.getAllChildsJJChapterWithParentSortedByOrder(treeObj);

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
		newNode.setExpanded(true);

		return newNode;
	}

	public void editNode() {
		persistIndex = 2;
		// System.out.println(selectedChapterNode.getData().toString());
		Long idChapter = Long.parseLong(getIdFromString(selectedChapterNode
				.getData().toString(), 1));
		chapter = jJChapterService.findJJChapter(idChapter);
		parentChapter = chapter.getParent();
	}

	public void deleteNode() {

		// System.out.println("Delete node");

		long idSelectedChapter = Long.parseLong(getIdFromString(
				selectedChapterNode.getData().toString(), 1));

		JJChapter selectedChapter = jJChapterService
				.findJJChapter(idSelectedChapter);

		JJChapter parentSelectedChapter = selectedChapter.getParent();

		List<TreeNode> childrenlist = selectedChapterNode.getChildren();
		// System.out.println(childrenlist.size());
		for (TreeNode childTreeNode : childrenlist) {

			long idChildChapter = Long.parseLong(getIdFromString(childTreeNode
					.getData().toString(), 1));
			JJChapter childChapter = jJChapterService
					.findJJChapter(idChildChapter);
			childChapter.setParent(parentSelectedChapter);
			if (parentSelectedChapter != null) {
				childChapter.setOrdering(parentSelectedChapter.getChapters()
						.size());
			} else {
				childChapter
						.setOrdering(jJChapterService
								.getAllParentJJChapterWithProjectAndCategorySortedByOrder(
										project, category).size());
			}

			jJChapterService.updateJJChapter(childChapter);

		}

		selectedChapter.setEnabled(false);
		jJChapterService.updateJJChapter(selectedChapter);

		initParameter(creationColumnNumber);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Deleted" + selectedChapterNode.getData(), "Deleted"
						+ selectedChapterNode.getData());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void onDragDrop(TreeDragDropEvent event) {

		TreeNode dragNode = event.getDragNode();
		TreeNode dropNode = event.getDropNode();
		int dropIndex = event.getDropIndex();

		String dragNodeData = dragNode.getData().toString();
		String dropNodeData = dropNode.getData().toString();

		FacesMessage message = null;

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

				requirement.setOrdering(dropIndex);
				jJRequirementService.updateJJRequirement(requirement);

				List<TreeNode> listNode = dropNode.getChildren();

				for (int i = 0; i < listNode.size(); i++) {

					String treeNodeData = listNode.get(i).getData().toString();

					// System.out.println("treeNodeData " + treeNodeData);

					if (treeNodeData.startsWith("R-")) {
						idRequirement = Long.parseLong(getIdFromString(
								treeNodeData, 1));
						requirement = jJRequirementService
								.findJJRequirement(idRequirement);
						requirement.setOrdering(i);
						jJRequirementService.updateJJRequirement(requirement);
					} else if (treeNodeData.startsWith("C-")) {
						idChapter = Long.parseLong(getIdFromString(
								dropNodeData, 1));
						chapter = jJChapterService.findJJChapter(idChapter);
						chapter.setOrdering(i);
						jJChapterService.updateJJChapter(chapter);
					}

				}

				List<JJChapter> chapters = jJChapterService.getAllJJChapter();
				for (JJChapter jjChapter : chapters) {
					// System.out.println("jjChapter.getName() jjChapter.getOrdering() "+jjChapter.getName()+" - "+jjChapter.getOrdering());
				}

				List<JJRequirement> reqs = jJRequirementService
						.getAllJJRequirements();
				for (JJRequirement jjRequirement : reqs) {
					// System.out.println("jjRequirement.getChapter().getName() jjRequirement.getName() jjChapter.getOrdering() "+jjRequirement.getChapter().getName()
					// +
					// " - "+jjRequirement.getName()+" - "+jjRequirement.getOrdering());
				}

				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Dragged Requirement: " + dragNode.getData()
								+ "  \nDropped on Chapter: "
								+ dropNode.getData() + " at " + dropIndex,
						"Dropped on " + dropNode.getData() + " at " + dropIndex);

			}

			else if (dropNodeData.equalsIgnoreCase("RootRequirement")) {
				JJChapter tmpChapter = requirement.getChapter();
				if (tmpChapter != null) {
					requirement.setChapter(null);
					requirement.setOrdering(0);
					jJRequirementService.updateJJRequirement(requirement);

					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Requirement: " + dragNode.getData()
									+ " is detached from chapter",
							"Dropped on " + dropNode.getData() + " at "
									+ dropIndex);

				}

			} else if (dropNodeData.startsWith("R-")) {

				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"This action is not allowed and does not have effects",
						"Dropped on " + dropNode.getData() + " at " + dropIndex);

			} else if (dropNodeData.equalsIgnoreCase("RootChapter")) {

				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"This action not allowed and does not have effects",
						"Dropped on " + dropNode.getData() + " at " + dropIndex);

			}

		} else if (dragNodeData.startsWith("C-")) {

			if (dropNodeData.startsWith("R-")) {

				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"This action is not allowed and does not have effects",
						"Dropped on " + dropNode.getData() + " at " + dropIndex);

			}

		}

		FacesContext.getCurrentInstance().addMessage(null, message);

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

		/*
		 * Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 30, Font.BOLD, new
		 * BaseColor(20, 20, 20)); Font fontChapter = new
		 * Font(FontFamily.HELVETICA, 15, Font.BOLD, new BaseColor(30, 30, 30));
		 * Font fontRequirement = new Font(FontFamily.TIMES_ROMAN, 10,
		 * Font.BOLD, new BaseColor(10, 10, 10)); Font fontNote = new
		 * Font(FontFamily.COURIER, 8, Font.BOLD, new BaseColor(50, 50, 50));
		 */

		StyleSheet style = new StyleSheet();
		style.loadTagStyle("body", "font", "Times New Roman");

		Phrase phrase = new Phrase(20, new Chunk("\n"
				+ "Business Specification \n" + project.getName() + "\n" + "\n"
				+ "\n", fontChapter));
		JJCategory category = jJCategoryService
				.getJJCategoryWithName("BUSINESS");

		List<JJChapter> list = jJChapterService
				.getAllParentJJChapterWithProjectAndCategorySortedByOrder(
						project, category);
		paragraph.add(phrase);

		for (JJChapter jjChapter : list) {
			orderChapters(jjChapter, category, paragraph, fontNote,
					fontChapter, fontRequirement, style);
		}

		paragraph.add(phrase);
		Image image = Image
				.getInstance("http://www.google.com/intl/en_ALL/images/logos/images_logo_lg.gif");
		image.scaleToFit((float) 200.0, (float) 49.0);
		paragraph.add(image);
		pdf.add(paragraph);
	}

	private void orderChapters(JJChapter chapter, JJCategory category,
			Paragraph paragraph, Font fontNote, Font fontChapter,
			Font fontRequirement, StyleSheet style) throws IOException {

		paragraph.add(new Chunk("\n" + chapter.getName() + "\n", fontChapter));
		paragraph.add(new Chunk(chapter.getDescription() + "\n", fontNote));

		List<JJChapter> listChild = jJChapterService
				.getAllJJChaptersWithProjectAndCategoryAndParentSortedByOrder(
						project, category, chapter);
		for (JJChapter jjChapter : listChild) {
			orderChapters(jjChapter, category, paragraph, fontNote,
					fontChapter, fontRequirement, style);
		}

		Set<JJRequirement> listReq = chapter.getRequirements();
		for (JJRequirement jjRequirement : listReq) {
			if (jjRequirement.getEnabled()) {
				paragraph.add(new Chunk(jjRequirement.getName() + "\n",
						fontRequirement));
				StringReader strReader = new StringReader(
						jjRequirement.getDescription());
				System.out.println("strReader = "
						+ strReader.getClass().getName());
				ArrayList arrList = HTMLWorker.parseToList(strReader, style);
				paragraph.addAll(arrList);
				/*
				 * for (int i = 0; i < arrList.size(); ++i) { Element e =
				 * (Element) arrList.get(i);
				 * System.out.println("ArrayElement = "
				 * +e.getClass().getName()); paragraph.add(e); }
				 */
				if (jjRequirement.getNote().length() > 2) {
					paragraph.add("Note: "
							+ new Chunk(jjRequirement.getNote() + "\n",
									fontNote));
				}
			}
		}
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

	public void handleSelectChapter() {

		if (persistIndex == 1) {
			if (parentChapter != null) {
				chapter.setParent(parentChapter);
				chapter.setOrdering(parentChapter.getChapters().size());
			} else {
				chapter.setOrdering(chapterList.size());
			}
		}
		// else {
		// if (!parentChapter.equals(chapter.getParent())) {
		//
		// }
		// }

	}

	private String getIdFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

	public void closeDialog(CloseEvent event) {
		System.out.println("HAHAHAHAHAH");
		chapter = null;
	}

}

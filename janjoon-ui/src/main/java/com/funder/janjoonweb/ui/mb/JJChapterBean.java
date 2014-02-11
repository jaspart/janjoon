package com.funder.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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
import com.funder.janjoonweb.ui.mb.util.MessageFactory;
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
@SuppressWarnings("unused")
public class JJChapterBean {

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	private JJChapter chapter;
	private JJChapter parentChapter;
	private List<JJChapter> chapterList;

	private JJProject project;
	private JJProduct product;
	private JJCategory category;

	/**
	 * Transfer Tree
	 */
	private TreeNode leftRoot;
	private TreeNode selectedLeftNode;
	private TreeNode rightRoot;
	private TreeNode selectedRightNode;

	/**
	 * Manage chapter Tree
	 */
	private TreeNode chapterRoot;
	private TreeNode selectedChapterNode;

	/**
	 * SortedMap of a node's children to manage the child's order into the
	 * parent node
	 */
	// private SortedMap<Integer, Object> childElements;

	public JJChapter getChapter() {
		return chapter;
	}

	public void setChapter(JJChapter chapter) {
		this.chapter = chapter;
	}

	public JJChapter getParentChapter() {
		return parentChapter;
	}

	public void setParentChapter(JJChapter parentChapter) {
		this.parentChapter = parentChapter;
	}

	public List<JJChapter> getChapterList() {
		chapterList = jJChapterService.getAllJJChapters(project, product,
				category);
		return chapterList;
	}

	public void setChapterList(List<JJChapter> chapterList) {
		this.chapterList = chapterList;
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

	public TreeNode getLeftRoot() {
		return leftRoot;
	}

	public void setLeftRoot(TreeNode leftRoot) {
		this.leftRoot = leftRoot;
	}

	public TreeNode getSelectedLeftNode() {
		return selectedLeftNode;
	}

	public void setSelectedLeftNode(TreeNode selectedLeftNode) {
		this.selectedLeftNode = selectedLeftNode;
	}

	public TreeNode getRightRoot() {
		return rightRoot;
	}

	public void setRightRoot(TreeNode rightRoot) {
		this.rightRoot = rightRoot;
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

	public void initChapterBean(String categoryName) {
		System.out.println("Init chapterBean");
		category = jJCategoryService.getJJCategoryWithName(categoryName);

		newChapter();
		chapterTreeBean();
		transferTreeBean();
	}

	private void newChapter() {
		System.out.println("Create new chapter");
		chapter = new JJChapter();
		chapter.setCreationDate(new Date());
		chapter.setEnabled(true);
		chapter.setCategory(category);
		chapter.setProject(project);
		chapter.setProduct(product);
		parentChapter = null;
		chapter.setParent(parentChapter);

		SortedMap<Integer, Object> elements = getSortedElements(parentChapter,
				false);
		if (elements.isEmpty()) {
			chapter.setOrdering(0);
		} else {
			chapter.setOrdering(elements.size());
		}
	}

	public void editChapter() {

		Long idChapter = Long.parseLong(getSplitFromString(selectedChapterNode
				.getData().toString(), 1));
		chapter = jJChapterService.findJJChapter(idChapter);
		parentChapter = chapter.getParent();
	}

	public void deleteChapter() {

		System.out.println("Delete node");

		long idSelectedChapter = Long.parseLong(getSplitFromString(
				selectedChapterNode.getData().toString(), 1));

		JJChapter selectedChapter = jJChapterService
				.findJJChapter(idSelectedChapter);

		JJChapter parentSelectedChapter = selectedChapter.getParent();

		SortedMap<Integer, Object> elements = getSortedElements(
				parentSelectedChapter, false);

		int increment = elements.size();

		elements = getSortedElements(selectedChapter, false);

		System.out
				.println("\n***** Update chapter and order for children  *****");
		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();
			int lastOrder;
			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();

				if (chapter.getEnabled()) {
					lastOrder = chapter.getOrdering();
					chapter.setOrdering(lastOrder + increment);
					chapter.setParent(parentSelectedChapter);
					jJChapterService.updateJJChapter(chapter);
				}
			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();

				if (requirement.getEnabled()) {
					lastOrder = requirement.getOrdering();
					requirement.setOrdering(lastOrder + increment);
					requirement.setChapter(parentSelectedChapter);
					jJRequirementService.updateJJRequirement(requirement);
				}
			}

		}

		/**
		 * Make the selectedChapter as inactif
		 */
		selectedChapter.setEnabled(false);
		jJChapterService.updateJJChapter(selectedChapter);

		newChapter();
		chapterTreeBean();
		transferTreeBean();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Deleted" + selectedChapterNode.getData(), "Deleted"
						+ selectedChapterNode.getData());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void save() {
		String message = "";
		if (chapter.getId() == null) {
			System.out.println("Save new Chapter");

			jJChapterService.saveJJChapter(chapter);
			message = "message_successfully_created";

		} else {
			chapter.setUpdatedDate(new Date());
			jJChapterService.updateJJChapter(chapter);
			message = "message_successfully_updated";

		}

		newChapter();
		chapterTreeBean();
		transferTreeBean();

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJChapter");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	// public List<JJChapter> completeChapter(String query) {
	// List<JJChapter> suggestions = new ArrayList<JJChapter>();
	//
	// for (JJChapter jJChapter : jJChapterService
	// .getAllJJChaptersWithProjectAndCategory(project, category)) {
	// String jJChapterStr = String.valueOf(jJChapter.getName() + " "
	// + jJChapter.getDescription() + " "
	// + jJChapter.getCreationDate() + " "
	// + jJChapter.getUpdatedDate());
	// if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
	// suggestions.add(jJChapter);
	// }
	// }
	// return suggestions;
	// }

	public void chapterTreeBean() {
		System.out.println("Create chapter TreeBean");
		chapterRoot = new DefaultTreeNode("RootChapter", null);

		List<JJChapter> parentChapters = jJChapterService
				.getAllParentsJJChapterSortedByOrder(project, product,
						category, true);

		for (JJChapter chapter : parentChapters) {
			TreeNode node = createTree(chapter, chapterRoot, 0);
		}

	}

	public void transferTreeBean() {

		System.out.println("In tree transfer");

		// Requirement Tree WHERE requirment.getChapter = null
		leftRoot = new DefaultTreeNode("leftRoot", null);

		List<JJRequirement> jJRequirementList = jJRequirementService
				.getAllJJRequirements(category, project, product, null, null,
						true, true);

		for (JJRequirement requirement : jJRequirementList) {
			TreeNode node = new DefaultTreeNode("R-" + requirement.getId()
					+ "- " + requirement.getName(), leftRoot);
		}

		// Chapter Tree ALL Chapters and Requirements requirment.getChapter !=
		// null
		rightRoot = new DefaultTreeNode("rightRoot", null);

		List<JJChapter> parentChapters = jJChapterService
				.getAllParentsJJChapterSortedByOrder(project, product,
						category, true);

		for (JJChapter chapter : parentChapters) {
			TreeNode node = createTree(chapter, rightRoot, 1);
		}

		System.out.println("End tree transfer");
	}

	// Recursive function to create tree
	public TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			int index) {

		TreeNode newNode = new DefaultTreeNode("C-" + chapterParent.getId()
				+ "- " + chapterParent.getName(), rootNode);

		if (index == 0) {
			List<JJChapter> chapterChildren = jJChapterService
					.getAllChildsJJChapterWithParentSortedByOrder(
							chapterParent, true);

			for (JJChapter chapterChild : chapterChildren) {
				if (chapterChild.getEnabled()) {
					TreeNode newNode2 = createTree(chapterChild, newNode, index);

				}
			}
		} else if (index == 1) {

			SortedMap<Integer, Object> elements = getSortedElements(
					chapterParent, true);
			for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
				String className = entry.getValue().getClass().getSimpleName();
				int lastOrder;
				if (className.equalsIgnoreCase("JJChapter")) {

					JJChapter chapter = (JJChapter) entry.getValue();
					TreeNode newNode2 = createTree(chapter, newNode, index);

				} else if (className.equalsIgnoreCase("JJRequirement")) {

					JJRequirement requirement = (JJRequirement) entry
							.getValue();
					TreeNode newNode3 = new DefaultTreeNode("R-"
							+ requirement.getId() + "- "
							+ requirement.getName(), newNode);
				}

			}

		}

		newNode.setExpanded(true);

		return newNode;
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

	// public void postProcessXLS(Object document) {
	// HSSFWorkbook wb = (HSSFWorkbook) document;
	// HSSFSheet sheet = wb.getSheetAt(0);
	// HSSFRow header = sheet.getRow(0);
	//
	// HSSFCellStyle cellStyle = wb.createCellStyle();
	// cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
	// cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	//
	// for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
	// HSSFCell cell = header.getCell(i);
	//
	// cell.setCellStyle(cellStyle);
	// }
	// }

	public SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			boolean onlyActif) {

		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getAllChildsJJChapterWithParentSortedByOrder(parent,
							onlyActif);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getAllChildsJJRequirementWithChapterSortedByOrder(parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering(), requirement);
			}

		} else {
			List<JJChapter> chapters = jJChapterService
					.getAllParentsJJChapterSortedByOrder(project, product,
							category, onlyActif);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}
		}

		return elements;

	}

	private String getSplitFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

	public void handleSelectParentChapter() {

		SortedMap<Integer, Object> elements = getSortedElements(parentChapter,
				false);

		chapter.setParent(parentChapter);

		/**
		 * Attribute order to chapter
		 */
		if (elements.isEmpty()) {
			chapter.setOrdering(0);
		} else {
			chapter.setOrdering(elements.size());
		}

	}

	public void onNodeSelect(NodeSelectEvent event) {
		selectedChapterNode = event.getTreeNode();
	}

	public void onDragDrop(TreeDragDropEvent event) {
		TreeNode dragNode = event.getDragNode();
		TreeNode dropNode = event.getDropNode();
		int dropIndex = event.getDropIndex();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dragged " + dragNode.getData(), "Dropped on "
						+ dropNode.getData() + " at " + dropIndex);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void closeDialog(CloseEvent event) {
		System.out.println("close Dialog");
		chapter = null;
		parentChapter = null;
		chapterList = null;

		project = null;
		product = null;
		category = null;

		leftRoot = null;
		selectedLeftNode = null;
		rightRoot = null;
		selectedRightNode = null;

		chapterRoot = null;
		selectedChapterNode = null;

	}

}

package com.starit.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

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
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

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

	@Autowired
	JJTestcaseService jJTestcaseService;

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	private JJChapter chapter;
	private JJChapter parentChapter;
	private List<JJChapter> chapterList;

	private JJProject project;
	private JJCategory category;

	private JJProject chapterProject;
	private List<JJProject> chapterProjectList;

	private boolean disabledProject;

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

	private long categoryId;
	
	private String warnMessage;

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

		if (chapterProject != null) {
			if (chapter.getId() == null) {
				chapterList = jJChapterService.getChapters(chapterProject,
						category, true, new ArrayList<String>());
			}

			else {
				List<String> list = getChildren(chapter);
				list.add(String.valueOf(chapter.getId()));

				chapterList = jJChapterService.getChapters(chapterProject,
						category, true, list);
			}

			return chapterList;
		} else
			return null;
	}

	public void setChapterList(List<JJChapter> chapterList) {
		this.chapterList = chapterList;
	}

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = jJProjectBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
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

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getWarnMessage() {
		return warnMessage;
	}

	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	public JJProject getChapterProject() {
		return chapterProject;
	}

	public void setChapterProject(JJProject chapterProject) {
		this.chapterProject = chapterProject;
	}

	public List<JJProject> getChapterProjectList() {
		chapterProjectList = jJProjectService.getProjects(true);
		return chapterProjectList;
	}

	public void setChapterProjectList(List<JJProject> chapterProjectList) {
		this.chapterProjectList = chapterProjectList;
	}

	public boolean isDisabledProject() {
		return disabledProject;
	}

	public void setDisabledProject(boolean disabledProject) {
		this.disabledProject = disabledProject;
	}

	public void loadData(long categoryId) {

		this.categoryId = categoryId;

		category = jJCategoryService.findJJCategory(categoryId);

		newChapter();
		chapterTree();
		transferTree();
	}

	private void newChapter() {

		getProject();

		chapter = new JJChapter();
		chapter.setCreationDate(new Date());
		chapter.setEnabled(true);
		chapter.setCategory(category);

		chapterProject = project;
		disabledProject = false;

		chapter.setDescription(null);
		parentChapter = null;

		handleSelectParentChapter();
	}

	public void editChapter() {

		Long idChapter = Long.parseLong(getSplitFromString(selectedChapterNode
				.getData().toString(), 1));
		chapter = jJChapterService.findJJChapter(idChapter);
		parentChapter = chapter.getParent();

		chapterProject = chapter.getProject();
		disabledProject = true;
	}

	public void handleSelectProject() {
		chapterTree();
		getChapterList();
		parentChapter = null;
		chapter.setParent(null);
		handleSelectParentChapter();
	}

	public void deleteChapter() {

		long idSelectedChapter = Long.parseLong(getSplitFromString(
				selectedChapterNode.getData().toString(), 1));

		JJChapter selectedChapter = jJChapterService
				.findJJChapter(idSelectedChapter);

		JJChapter parentSelectedChapter = selectedChapter.getParent();

		SortedMap<Integer, Object> elements = getSortedElements(
				parentSelectedChapter, chapterProject, category, false);

		int increment = elements.lastKey() + 1;

		elements = getSortedElements(selectedChapter, chapterProject, category,
				false);

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
		
		chapterProject = null;

		loadData(categoryId);

		// newChapter();
		// chapterTree();
		// transferTree();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Deleted" + selectedChapterNode.getData(), "Deleted"
						+ selectedChapterNode.getData());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void save() {
		String message = "";
		if (chapter.getId() == null) {

			chapter.setProject(chapterProject);

			jJChapterService.saveJJChapter(chapter);
			message = "message_successfully_created";

		} else {
			chapter.setUpdatedDate(new Date());
			jJChapterService.updateJJChapter(chapter);
			message = "message_successfully_updated";

		}

		loadData(categoryId);
		// newChapter();
		// chapterTree();
		// transferTree();

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJChapter");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void chapterTree() {

		chapterRoot = new DefaultTreeNode("RootChapter", null);

		if (chapterProject != null) {
			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(chapterProject, category, true, true);

			for (JJChapter chapter : parentChapters) {
				TreeNode node = createTree(chapter, chapterRoot,
						chapterProject, category, 0);
			}
		}
	}

	private void transferTree() {

		if (project != null) {

			// Requirement Tree WHERE requirment.getChapter = null
			leftRoot = new DefaultTreeNode("leftRoot", null);

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJVersionBean jJVersionBean = (JJVersionBean) session
					.getAttribute("jJVersionBean");

			JJVersion version = jJVersionBean.getVersion();

			JJProductBean jJProductBean = (JJProductBean) session
					.getAttribute("jJProductBean");
			JJProduct product = jJProductBean.getProduct();

			List<JJRequirement> jJRequirementList = jJRequirementService
					.getRequirements(category, project, product, version, null,
							null, true, true, false);

			for (JJRequirement requirement : jJRequirementList) {
				TreeNode node = new DefaultTreeNode("R-" + requirement.getId()
						+ "- " + requirement.getName(), leftRoot);
			}

			// Chapter Tree ALL Chapters and Requirements requirment.getChapter
			// !=
			// null
			rightRoot = new DefaultTreeNode("rightRoot", null);

			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(project, category, true, true);

			for (JJChapter chapter : parentChapters) {
				TreeNode node = createTree(chapter, rightRoot, project,
						category, 1);
			}
		} else {
			warnMessage = "For The Manage Document Try to select a project in the header";
		}

	}

	// Recursive function to get children of chapter

	private List<String> getChildren(JJChapter parent) {
		List<String> list = new ArrayList<String>();
		List<JJChapter> children = jJChapterService.getChildrenOfParentChapter(
				parent, true, false);

		for (JJChapter child : children) {
			list.add(String.valueOf(child.getId()));
			list.addAll(getChildren(child));
		}
		return list;
	}

	// Recursive function to create tree
	public TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			JJProject project, JJCategory category, int index) {

		TreeNode newNode = new DefaultTreeNode("C-" + chapterParent.getId()
				+ "- " + chapterParent.getName(), rootNode);

		if (index == 0) {
			List<JJChapter> chapterChildren = jJChapterService
					.getChildrenOfParentChapter(chapterParent, true, true);

			for (JJChapter chapterChild : chapterChildren) {
				if (chapterChild.getEnabled()) {
					TreeNode newNode2 = createTree(chapterChild, newNode,
							project, category, index);

				}
			}
		} else if (index == 1) {

			SortedMap<Integer, Object> elements = getSortedElements(
					chapterParent, project, category, true);
			for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
				String className = entry.getValue().getClass().getSimpleName();

				if (className.equalsIgnoreCase("JJChapter")) {

					JJChapter chapter = (JJChapter) entry.getValue();
					TreeNode newNode2 = createTree(chapter, newNode, project,
							category, index);

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

	public void updateCategoryId(long id) {
		categoryId = id;
	}

	public void preProcessPDF(Object document) throws IOException,
			BadElementException, DocumentException {

		this.getProject();

		Document pdf = (Document) document;
		pdf.addTitle("THIS IS THE TITLE");

		pdf.open();
		pdf.setPageSize(PageSize.A4);

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

		JJCategory category = jJCategoryService.findJJCategory(categoryId);

		Phrase phrase = new Phrase(20, new Chunk("\n" + category.getName()
				+ " Specification \n" + project.getName() + "\n" + "\n" + "\n",
				fontChapter));

		Paragraph paragraph = new Paragraph();
		paragraph.add(phrase);

		List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
				project, category, true, true);

		for (JJChapter chapter : parentChapters) {
			createTreeDocument(chapter, category, paragraph, fontNote,
					fontChapter, fontRequirement, style);
		}

		paragraph.add(phrase);
		Image image = Image
				.getInstance("http://www.google.com/intl/en_ALL/images/logos/images_logo_lg.gif");
		image.scaleToFit((float) 200.0, (float) 49.0);
		paragraph.add(image);
		pdf.add(paragraph);
	}

	private void createTreeDocument(JJChapter chapterParent,
			JJCategory category, Paragraph paragraph, Font fontNote,
			Font fontChapter, Font fontRequirement, StyleSheet style)
			throws IOException {

		paragraph.add(new Chunk("\n" + chapterParent.getName() + "\n",
				fontChapter));
		paragraph
				.add(new Chunk(chapterParent.getDescription() + "\n", fontNote));

		SortedMap<Integer, Object> elements = getSortedElements(chapterParent,
				project, category, true);
		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();

			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();
				createTreeDocument(chapter, category, paragraph, fontNote,
						fontChapter, fontRequirement, style);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();

				paragraph.add(new Chunk(requirement.getName() + "\n",
						fontRequirement));
				StringReader strReader = new StringReader(
						requirement.getDescription());

				ArrayList arrList = HTMLWorker.parseToList(strReader, style);
				paragraph.addAll(arrList);
				/*
				 * for (int i = 0; i < arrList.size(); ++i) { Element e =
				 * (Element) arrList.get(i);
				 * System.out.println("ArrayElement = "
				 * +e.getClass().getName()); paragraph.add(e); }
				 */
				if (requirement.getNote().length() > 2) {
					paragraph
							.add("Note: "
									+ new Chunk(requirement.getNote() + "\n",
											fontNote));
				}

			}

		}

	}

	private SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			JJProject project, JJCategory category, boolean onlyActif) {

		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getChildrenOfParentChapter(parent, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering(), requirement);

			}
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					project, category, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}
		}

		return elements;

	}

	private SortedMap<Integer, JJTestcase> getSortedTestcases(
			JJRequirement requirement, JJChapter chapter) {

		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(
				requirement, chapter, false, true, false);

		for (JJTestcase testcase : testcases) {
			elements.put(testcase.getOrdering(), testcase);
		}

		return elements;

	}

	private String getSplitFromString(String s, int index) {
		String[] temp = s.split("-");
		return temp[index];
	}

	public void handleSelectParentChapter() {

		if (chapterProject != null) {

			SortedMap<Integer, Object> elements = getSortedElements(
					parentChapter, chapterProject, category, false);

			chapter.setParent(parentChapter);

			/**
			 * Attribute order to chapter
			 */
			if (elements.isEmpty()) {
				chapter.setOrdering(0);
			} else {
				chapter.setOrdering(elements.lastKey() + 1);
			}

		} else {
			chapter.setOrdering(null);
		}
	}

	public void onNodeSelect(NodeSelectEvent event) {
		selectedChapterNode = event.getTreeNode();
	}

	public void onDragDrop(TreeDragDropEvent event) {

		String dragNodeData = event.getDragNode().getData().toString();
		String dropNodeData = event.getDropNode().getData().toString();

		int dropIndex = event.getDropIndex();

		FacesMessage message = null;

		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Object> subElements = null;

		SortedMap<Integer, JJTestcase> testcases = null;
		SortedMap<Integer, JJTestcase> subTestcases = null;

		if (dragNodeData.startsWith("R-")) {

			long requirementID = Long.parseLong(getSplitFromString(
					dragNodeData, 1));

			JJRequirement REQUIREMENT = jJRequirementService
					.findJJRequirement(requirementID);

			JJChapter requirementCHAPTER = REQUIREMENT.getChapter();

			subTestcases = getSortedTestcases(REQUIREMENT, null);
			testcases = getSortedTestcases(null, requirementCHAPTER);

			if (dropNodeData.startsWith("C-")) {

				if (requirementCHAPTER != null) {

					int requirementOrder = REQUIREMENT.getOrdering();

					elements = getSortedElements(requirementCHAPTER, project,
							category, false);

					subElements = elements.tailMap(requirementOrder);

					REQUIREMENT.setChapter(null);
					REQUIREMENT.setOrdering(null);
					REQUIREMENT.setUpdatedDate(new Date());

					jJRequirementService.updateJJRequirement(REQUIREMENT);

					subElements.remove(requirementOrder);

					for (Map.Entry<Integer, Object> entry : subElements
							.entrySet()) {

						String className = entry.getValue().getClass()
								.getSimpleName();
						if (className.equalsIgnoreCase("JJChapter")) {

							JJChapter chapter = (JJChapter) entry.getValue();

							int lastOrder = chapter.getOrdering();
							chapter.setOrdering(lastOrder - 1);
							chapter.setUpdatedDate(new Date());

							jJChapterService.updateJJChapter(chapter);

						} else if (className.equalsIgnoreCase("JJRequirement")) {

							JJRequirement requirement = (JJRequirement) entry
									.getValue();

							int lastOrder = requirement.getOrdering();
							requirement.setOrdering(lastOrder - 1);
							requirement.setUpdatedDate(new Date());

							jJRequirementService
									.updateJJRequirement(requirement);
						}

					}

					if (!subTestcases.isEmpty()) {

						SortedMap<Integer, JJTestcase> tempTestcases;

						for (Map.Entry<Integer, JJTestcase> entry : subTestcases
								.entrySet()) {

							int testcaseOrder = entry.getKey();

							JJTestcase testcase = entry.getValue();

							tempTestcases = new TreeMap<Integer, JJTestcase>();
							tempTestcases = testcases.tailMap(testcaseOrder);
							tempTestcases.remove(testcaseOrder);

							testcases.remove(testcaseOrder);

							testcase.setOrdering(null);
							testcase.setUpdatedDate(new Date());
							jJTestcaseService.updateJJTestcase(testcase);

							testcase = null;

							for (Map.Entry<Integer, JJTestcase> tmpEntry : tempTestcases
									.entrySet()) {

								testcase = tmpEntry.getValue();
								int lastOrder = testcase.getOrdering();

								testcase.setOrdering(lastOrder - 1);
								testcase.setUpdatedDate(new Date());
								jJTestcaseService.updateJJTestcase(testcase);

								testcase = null;

							}

						}

						tempTestcases = null;
					}

				}

				long chapterID = Long.parseLong(getSplitFromString(
						dropNodeData, 1));

				JJChapter CHAPTER = jJChapterService.findJJChapter(chapterID);

				elements = getSortedElements(CHAPTER, project, category, false);

				SortedMap<Integer, JJTestcase> testcases1 = getSortedTestcases(
						null, CHAPTER);

				if (elements.isEmpty()) {
					REQUIREMENT.setOrdering(0);
				} else {

					if (dropIndex < elements.size()) {
						int i = 0;
						for (Map.Entry<Integer, Object> entry : elements
								.entrySet()) {
							if (i == dropIndex) {
								i = entry.getKey();
								break;
							} else {
								i++;
							}

						}

						subElements = elements.tailMap(i);
						for (Map.Entry<Integer, Object> entry : subElements
								.entrySet()) {

							String className = entry.getValue().getClass()
									.getSimpleName();
							if (className.equalsIgnoreCase("JJChapter")) {

								JJChapter chapter = (JJChapter) entry
										.getValue();

								int lastOrder = chapter.getOrdering();
								chapter.setOrdering(lastOrder + 1);
								chapter.setUpdatedDate(new Date());

								jJChapterService.updateJJChapter(chapter);

							} else if (className
									.equalsIgnoreCase("JJRequirement")) {

								JJRequirement requirement = (JJRequirement) entry
										.getValue();

								int lastOrder = requirement.getOrdering();
								requirement.setOrdering(lastOrder + 1);
								requirement.setUpdatedDate(new Date());

								jJRequirementService
										.updateJJRequirement(requirement);
							}

						}

						REQUIREMENT.setOrdering(i);

					} else {

						REQUIREMENT.setOrdering(elements.lastKey() + 1);

					}
				}

				REQUIREMENT.setChapter(CHAPTER);
				REQUIREMENT.setUpdatedDate(new Date());

				jJRequirementService.updateJJRequirement(REQUIREMENT);

				if (!subTestcases.isEmpty()) {

					int increment = 0;

					if (!testcases1.isEmpty()) {

						increment = testcases1.lastKey() + 1;
					}

					int i = 0;

					for (Map.Entry<Integer, JJTestcase> entry : subTestcases
							.entrySet()) {

						int order = i + increment;

						JJTestcase testcase = entry.getValue();

						testcase.setOrdering(order);
						testcase.setUpdatedDate(new Date());
						jJTestcaseService.updateJJTestcase(testcase);

						i++;

						testcase = null;
					}
				}

				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Requirement: " + dragNodeData
								+ "\n Dropped on Chapter: " + dropNodeData
								+ " at (" + dropIndex + ")", null);

			} else if (dropNodeData.startsWith("leftRoot")) {

				if (requirementCHAPTER == null) {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Requirement: " + dragNodeData + "\n Dropped on: "
									+ dropNodeData + " at (" + dropIndex + ")"
									+ "\n No changes found", null);

				} else {

					int requirementOrder = REQUIREMENT.getOrdering();

					elements = getSortedElements(requirementCHAPTER, project,
							category, false);

					subElements = elements.tailMap(requirementOrder);

					REQUIREMENT.setChapter(null);
					REQUIREMENT.setOrdering(null);
					REQUIREMENT.setUpdatedDate(new Date());

					jJRequirementService.updateJJRequirement(REQUIREMENT);

					subElements.remove(requirementOrder);

					for (Map.Entry<Integer, Object> entry : subElements
							.entrySet()) {

						String className = entry.getValue().getClass()
								.getSimpleName();
						if (className.equalsIgnoreCase("JJChapter")) {

							JJChapter chapter = (JJChapter) entry.getValue();

							int lastOrder = chapter.getOrdering();
							chapter.setOrdering(lastOrder - 1);
							chapter.setUpdatedDate(new Date());

							jJChapterService.updateJJChapter(chapter);

						} else if (className.equalsIgnoreCase("JJRequirement")) {

							JJRequirement requirement = (JJRequirement) entry
									.getValue();

							int lastOrder = requirement.getOrdering();
							requirement.setOrdering(lastOrder - 1);
							requirement.setUpdatedDate(new Date());

							jJRequirementService
									.updateJJRequirement(requirement);
						}

					}

					if (!subTestcases.isEmpty()) {

						SortedMap<Integer, JJTestcase> tempTestcases;

						for (Map.Entry<Integer, JJTestcase> entry : subTestcases
								.entrySet()) {

							int testcaseOrder = entry.getKey();

							JJTestcase testcase = entry.getValue();

							tempTestcases = new TreeMap<Integer, JJTestcase>();
							tempTestcases = testcases.tailMap(testcaseOrder);
							tempTestcases.remove(testcaseOrder);

							testcases.remove(testcaseOrder);

							testcase = null;

							for (Map.Entry<Integer, JJTestcase> tmpEntry : tempTestcases
									.entrySet()) {

								testcase = tmpEntry.getValue();

								if (!subTestcases.containsValue(testcase)) {

									int lastOrder = testcase.getOrdering();

									testcase.setOrdering(lastOrder - 1);
									testcase.setUpdatedDate(new Date());
									jJTestcaseService
											.updateJJTestcase(testcase);
								}

								testcase = null;

							}

						}

						tempTestcases = null;
					}

					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Requirement: " + dragNodeData
									+ " is detached from chapter",
							"\n Dropped on " + dropNodeData + " at ("
									+ dropIndex + ")");

				}

			} else if (dropNodeData.startsWith("R-")
					|| dropNodeData.startsWith("rightRoot")) {
				message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Requirement: "
								+ dragNodeData
								+ "\nDropped on: "
								+ dropNodeData
								+ " at ("
								+ dropIndex
								+ ")"
								+ "\nThis action is not allowed and does not have effects",
						null);

			}

		} else if (dragNodeData.startsWith("C-")) {

			long chapterID = Long
					.parseLong(getSplitFromString(dragNodeData, 1));

			JJChapter CHAPTER = jJChapterService.findJJChapter(chapterID);
			JJChapter chapterParent = CHAPTER.getParent();
			int chapterOrder = CHAPTER.getOrdering();

			if (dropNodeData.startsWith("C-")
					|| dropNodeData.equalsIgnoreCase("rightRoot")) {

				JJChapter newChapterPARENT = null;

				if (dropNodeData.equalsIgnoreCase("rightRoot")) {

					message = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Chapter: "
									+ dragNodeData
									+ " becomes a parent chapter in the document",
							"\n Dropped on " + dropNodeData + " at ("
									+ dropIndex + ")");
				} else {
					long newChapterPARENTID = Long
							.parseLong(getSplitFromString(dropNodeData, 1));
					newChapterPARENT = jJChapterService
							.findJJChapter(newChapterPARENTID);

					message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Chapter: " + dragNodeData
									+ "\n Dropped on Chapter: " + dropNodeData
									+ " at (" + dropIndex + ")", null);
				}

				// Update the last chapter list

				elements = getSortedElements(chapterParent, project, category,
						false);

				subElements = elements.tailMap(chapterOrder);

				CHAPTER.setParent(null);

				SortedMap<Integer, Object> tmpElements = getSortedElements(
						null, project, category, false);

				CHAPTER.setOrdering(tmpElements.lastKey() + 1);
				CHAPTER.setUpdatedDate(new Date());

				jJChapterService.updateJJChapter(CHAPTER);

				subElements.remove(chapterOrder);

				for (Map.Entry<Integer, Object> entry : subElements.entrySet()) {

					String className = entry.getValue().getClass()
							.getSimpleName();
					if (className.equalsIgnoreCase("JJChapter")) {

						JJChapter chapter = (JJChapter) entry.getValue();

						int lastOrder = chapter.getOrdering();
						chapter.setOrdering(lastOrder - 1);
						chapter.setUpdatedDate(new Date());

						jJChapterService.updateJJChapter(chapter);

					} else if (className.equalsIgnoreCase("JJRequirement")) {

						JJRequirement requirement = (JJRequirement) entry
								.getValue();

						int lastOrder = requirement.getOrdering();
						requirement.setOrdering(lastOrder - 1);
						requirement.setUpdatedDate(new Date());

						jJRequirementService.updateJJRequirement(requirement);
					}

				}

				// End Update the last chapter list

				elements = getSortedElements(newChapterPARENT, project,
						category, false);

				if (newChapterPARENT == null) {
					elements.remove(CHAPTER.getOrdering());
				}

				if (elements.isEmpty()) {
					CHAPTER.setOrdering(0);
				} else {

					if (dropIndex < elements.size()) {

						int i = 0;
						for (Map.Entry<Integer, Object> entry : elements
								.entrySet()) {
							if (i == dropIndex) {
								i = entry.getKey();
								break;
							} else {
								i++;
							}

						}

						subElements = elements.tailMap(i);
						for (Map.Entry<Integer, Object> entry : subElements
								.entrySet()) {

							String className = entry.getValue().getClass()
									.getSimpleName();
							if (className.equalsIgnoreCase("JJChapter")) {

								JJChapter chapter = (JJChapter) entry
										.getValue();

								int lastOrder = chapter.getOrdering();
								chapter.setOrdering(lastOrder + 1);
								chapter.setUpdatedDate(new Date());

								jJChapterService.updateJJChapter(chapter);

							} else if (className
									.equalsIgnoreCase("JJRequirement")) {

								JJRequirement requirement = (JJRequirement) entry
										.getValue();

								int lastOrder = requirement.getOrdering();
								requirement.setOrdering(lastOrder + 1);
								requirement.setUpdatedDate(new Date());

								jJRequirementService
										.updateJJRequirement(requirement);
							}

						}
						CHAPTER.setOrdering(i);
					} else {

						CHAPTER.setOrdering(elements.lastKey() + 1);
					}
				}

				CHAPTER.setParent(newChapterPARENT);
				CHAPTER.setUpdatedDate(new Date());
				jJChapterService.updateJJChapter(CHAPTER);

			} else if (dropNodeData.startsWith("R-")
					|| dropNodeData.equalsIgnoreCase("leftRoot")) {
				message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Chapter: "
								+ dragNodeData
								+ "\nDropped on: "
								+ dropNodeData
								+ " at ("
								+ dropIndex
								+ ")"
								+ "\nThis action is not allowed and does not have effects",
						null);

			}

		}

		FacesContext.getCurrentInstance().addMessage(null, message);

		loadData(categoryId);

		// newChapter();
		// chapterTree();
		// transferTree();

		message = null;
		elements = null;
		subElements = null;
		testcases = null;
		subTestcases = null;
	}

	public void closeDialog(CloseEvent event) {

		chapter = null;
		parentChapter = null;
		chapterList = null;

		project = null;
		category = null;

		leftRoot = null;
		selectedLeftNode = null;
		rightRoot = null;
		selectedRightNode = null;

		chapterRoot = null;
		selectedChapterNode = null;

		chapterProject = null;
		warnMessage = null;

	}

}

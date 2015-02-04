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
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
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
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.codec.Base64;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;

import org.apache.log4j.Logger;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
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

	static Logger logger = Logger.getLogger(JJChapterBean.class);

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

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

	private boolean disabledChapter;

	private boolean chapterState;

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

		if (chapter.getId() == null) {
			chapterList = jJChapterService.getChapters(((LoginBean) LoginBean
					.findBean("loginBean")).getContact().getCompany(), project,
					category, true, new ArrayList<String>());
		}

		else {
			List<String> list = getChildren(chapter);
			list.add(String.valueOf(chapter.getId()));

			chapterList = jJChapterService.getChapters(((LoginBean) LoginBean
					.findBean("loginBean")).getContact().getCompany(), project,
					category, true, list);
		}

		return chapterList;

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

	public boolean getDisabledChapter() {
		return disabledChapter;
	}

	public void setDisabledChapter(boolean disabledChapter) {
		this.disabledChapter = disabledChapter;
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
		chapter.setEnabled(true);
		chapter.setCategory(category);

		disabledProject = false;

		chapter.setDescription(null);
		parentChapter = null;

		handleSelectParentChapter();

		chapterState = true;

	}

	public void editChapter() {

		Long idChapter = Long.parseLong(getSplitFromString(selectedChapterNode
				.getData().toString(), 1));
		chapter = jJChapterService.findJJChapter(idChapter);
		parentChapter = chapter.getParent();

		disabledProject = true;

		chapterState = false;
	}

	public void deleteChapter() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJRequirementBean jJRequirementBean = (JJRequirementBean) session
				.getAttribute("jJRequirementBean");

		if (jJRequirementBean == null)
			jJRequirementBean = new JJRequirementBean();

		RequirementBean requirementBean = (RequirementBean) session
				.getAttribute("requirementBean");

		if (requirementBean != null)
			requirementBean.setRootNode(null);

		long idSelectedChapter = Long.parseLong(getSplitFromString(
				selectedChapterNode.getData().toString(), 1));

		JJChapter selectedChapter = jJChapterService
				.findJJChapter(idSelectedChapter);

		JJChapter parentSelectedChapter = selectedChapter.getParent();

		SortedMap<Integer, Object> elements = getSortedElements(
				parentSelectedChapter, project, category, false);

		int increment = elements.lastKey() + 1;

		elements = getSortedElements(selectedChapter, project, category, false);

		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();
			int lastOrder;
			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();

				if (chapter.getEnabled()) {
					lastOrder = chapter.getOrdering();
					chapter.setOrdering(lastOrder + increment);
					chapter.setParent(parentSelectedChapter);
					updateJJChapter(chapter);
				}
			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();

				if (requirement.getEnabled()) {
					lastOrder = requirement.getOrdering();
					requirement.setOrdering(lastOrder + increment);
					requirement.setChapter(parentSelectedChapter);
					jJRequirementBean.updateJJRequirement(requirement);
				}
			}

		}

		/**
		 * Make the selectedChapter as inactif
		 */
		selectedChapter.setEnabled(false);
		updateJJChapter(selectedChapter);

		loadData(categoryId);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Deleted" + selectedChapterNode.getData(), "Deleted"
						+ selectedChapterNode.getData());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void save() {
		String message = "";
		if (chapter.getId() == null) {

			chapter.setProject(project);

			saveJJChapter(chapter);
			message = "message_successfully_created";

		} else {
			updateJJChapter(chapter);
			message = "message_successfully_updated";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Chapter");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();

		if (chapterState) {
			if (getChapterDialogConfiguration()) {
				context.execute("PF('chapterDialogWidget').hide()");
			} else {
				loadData(categoryId);
			}

		} else {
			context.execute("PF('chapterDialogWidget').hide()");
			RequirementBean requirementBean=(RequirementBean) LoginBean.findBean("requirementBean");
			if(requirementBean != null)
				requirementBean.setRootNode(null);
		}
	}

	private void chapterTree() {

		chapterRoot = new DefaultTreeNode("RootChapter", null);

		List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), project, category, true, true);

		for (JJChapter chapter : parentChapters) {
			TreeNode node = createTree(chapter, chapterRoot, project, category,
					0);
		}

	}

	private void transferTree() {

		// Requirement Tree WHERE requirment.getChapter = null
		leftRoot = new DefaultTreeNode("leftRoot", null);

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		JJVersion version = jJVersionBean.getVersion();

		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		JJProduct product = jJProductBean.getProduct();

		List<JJRequirement> jJRequirementList = jJRequirementService
				.getRequirements(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getCompany(), category, project, product,
						version, null, null, true, true, false);

		for (JJRequirement requirement : jJRequirementList) {
			TreeNode node = new DefaultTreeNode("R-" + requirement.getId()
					+ "- " + requirement.getName(), leftRoot);
		}

		// Chapter Tree ALL Chapters and Requirements requirment.getChapter
		// !=
		// null
		rightRoot = new DefaultTreeNode("rightRoot", null);

		List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), project, category, true, true);

		for (JJChapter chapter : parentChapters) {
			TreeNode node = createTree(chapter, rightRoot, project, category, 1);
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
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), project, category, true, true);

		for (JJChapter chapter : parentChapters) {
			createTreeDocument(chapter, category, paragraph, fontNote,
					fontChapter, fontRequirement, style);
		}

		paragraph.add(phrase);
		pdf.add(paragraph);
	}

	private void createTreeDocument(JJChapter chapterParent,
			JJCategory category, Paragraph paragraph, Font fontNote,
			Font fontChapter, Font fontRequirement, StyleSheet style)
			throws IOException {

		paragraph.add(new Chunk("\n" + chapterParent.getName() + "\n",
				fontChapter));

		StringReader strChapitre = new StringReader(chapterParent
				.getDescription().replace("/pages/ckeditor/getimage?imageId=",
						"/images/"));
		List arrChapitre = HTMLWorker.parseToList(strChapitre, style);
		for (int i = 0; i < arrChapitre.size(); ++i) {
			Element e = (Element) arrChapitre.get(i);
			System.out.println("ChapterElement = " + e.getClass().getName());
			paragraph.setSpacingAfter(50);
			paragraph.add(e);
		}

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
				StringReader strReader = new StringReader(requirement
						.getDescription()
						.replace("/pages/ckeditor/getimage?imageId=",
								"/images/"));
				System.out.println("ExportPDF req : "
						+ requirement.getDescription()
								.replace("/pages/ckeditor/getimage?imageId=",
										"/images/"));
				List arrList = HTMLWorker.parseToList(strReader, style);
				for (int i = 0; i < arrList.size(); ++i) {
					/*
					 * String chunk = (String) arrList.get(i); String
					 * imageIndicator = "<img src='data:image/png;base64,";
					 * logger.info("Chunk of HTMLWorker : "+chunk);
					 * if(chunk.startsWith(imageIndicator)){ Image img = null;
					 * String base64Data =
					 * "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"
					 * ; base64Data = chunk.substring(imageIndicator.length(),
					 * chunk.length()-2); try { img =
					 * Image.getInstance(Base64.decode(base64Data));
					 * paragraph.add(img); } catch (Exception exception) {
					 * logger
					 * .warn("Problem with decoding b64 image ("+base64Data
					 * +") instance while exporting in pdf"); } } else {
					 */
					Element e = (Element) arrList.get(i);
					paragraph.add(e);
					/* } */
				}
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
					.getRequirementChildrenWithChapterSortedByOrder(
							((LoginBean) LoginBean.findBean("loginBean"))
									.getContact().getCompany(), parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering(), requirement);

			}
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany(), project, category, onlyActif, true);

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
				requirement, null,chapter, false, true, false);

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

		SortedMap<Integer, Object> elements = getSortedElements(parentChapter,
				project, category, false);

		chapter.setParent(parentChapter);

		/**
		 * Attribute order to chapter
		 */
		if (elements.isEmpty()) {
			chapter.setOrdering(0);
		} else {
			chapter.setOrdering(elements.lastKey() + 1);
		}

	}

	public void onNodeSelect(NodeSelectEvent event) {
		selectedChapterNode = event.getTreeNode();
	}

	public void onDragDrop(TreeDragDropEvent event) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJRequirementBean jJRequirementBean = (JJRequirementBean) session
				.getAttribute("jJRequirementBean");
		if (jJRequirementBean == null)
			jJRequirementBean = new JJRequirementBean();

		RequirementBean requirementBean = (RequirementBean) session
				.getAttribute("requirementBean");
		if (requirementBean != null)
			requirementBean.setRootNode(null);

		JJTestcaseBean testcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		if (testcaseBean == null)
			testcaseBean = new JJTestcaseBean();

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

					jJRequirementBean.updateJJRequirement(REQUIREMENT);

					subElements.remove(requirementOrder);

					for (Map.Entry<Integer, Object> entry : subElements
							.entrySet()) {

						String className = entry.getValue().getClass()
								.getSimpleName();
						if (className.equalsIgnoreCase("JJChapter")) {

							JJChapter chapter = (JJChapter) entry.getValue();

							int lastOrder = chapter.getOrdering();
							chapter.setOrdering(lastOrder - 1);

							updateJJChapter(chapter);

						} else if (className.equalsIgnoreCase("JJRequirement")) {

							JJRequirement requirement = (JJRequirement) entry
									.getValue();

							int lastOrder = requirement.getOrdering();
							requirement.setOrdering(lastOrder - 1);

							jJRequirementBean.updateJJRequirement(requirement);
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
							testcaseBean.updateJJTestcase(testcase);

							testcase = null;

							for (Map.Entry<Integer, JJTestcase> tmpEntry : tempTestcases
									.entrySet()) {

								testcase = tmpEntry.getValue();
								int lastOrder = testcase.getOrdering();

								testcase.setOrdering(lastOrder - 1);
								testcaseBean.updateJJTestcase(testcase);

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

								updateJJChapter(chapter);

							} else if (className
									.equalsIgnoreCase("JJRequirement")) {

								JJRequirement requirement = (JJRequirement) entry
										.getValue();

								int lastOrder = requirement.getOrdering();
								requirement.setOrdering(lastOrder + 1);

								jJRequirementBean
										.updateJJRequirement(requirement);
							}

						}

						REQUIREMENT.setOrdering(i);

					} else {

						REQUIREMENT.setOrdering(elements.lastKey() + 1);

					}
				}

				REQUIREMENT.setChapter(CHAPTER);

				jJRequirementBean.updateJJRequirement(REQUIREMENT);

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
						testcaseBean.updateJJTestcase(testcase);

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

					jJRequirementBean.updateJJRequirement(REQUIREMENT);

					subElements.remove(requirementOrder);

					for (Map.Entry<Integer, Object> entry : subElements
							.entrySet()) {

						String className = entry.getValue().getClass()
								.getSimpleName();
						if (className.equalsIgnoreCase("JJChapter")) {

							JJChapter chapter = (JJChapter) entry.getValue();

							int lastOrder = chapter.getOrdering();
							chapter.setOrdering(lastOrder - 1);

							updateJJChapter(chapter);

						} else if (className.equalsIgnoreCase("JJRequirement")) {

							JJRequirement requirement = (JJRequirement) entry
									.getValue();

							int lastOrder = requirement.getOrdering();
							requirement.setOrdering(lastOrder - 1);

							jJRequirementBean.updateJJRequirement(requirement);
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
									testcaseBean.updateJJTestcase(testcase);
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

				updateJJChapter(CHAPTER);

				subElements.remove(chapterOrder);

				for (Map.Entry<Integer, Object> entry : subElements.entrySet()) {

					String className = entry.getValue().getClass()
							.getSimpleName();
					if (className.equalsIgnoreCase("JJChapter")) {

						JJChapter chapter = (JJChapter) entry.getValue();

						int lastOrder = chapter.getOrdering();
						chapter.setOrdering(lastOrder - 1);

						updateJJChapter(chapter);

					} else if (className.equalsIgnoreCase("JJRequirement")) {

						JJRequirement requirement = (JJRequirement) entry
								.getValue();

						int lastOrder = requirement.getOrdering();
						requirement.setOrdering(lastOrder - 1);

						jJRequirementBean.updateJJRequirement(requirement);
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

								updateJJChapter(chapter);

							} else if (className
									.equalsIgnoreCase("JJRequirement")) {

								JJRequirement requirement = (JJRequirement) entry
										.getValue();

								int lastOrder = requirement.getOrdering();
								requirement.setOrdering(lastOrder + 1);

								jJRequirementBean
										.updateJJRequirement(requirement);
							}

						}
						CHAPTER.setOrdering(i);
					} else {

						CHAPTER.setOrdering(elements.lastKey() + 1);
					}
				}

				CHAPTER.setParent(newChapterPARENT);
				updateJJChapter(CHAPTER);

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

		message = null;
		elements = null;
		subElements = null;
		testcases = null;
		subTestcases = null;

	}

	private void editCategoryTable() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJRequirementBean jJRequirementBean = (JJRequirementBean) session
				.getAttribute("jJRequirementBean");
		if(jJRequirementBean != null && jJRequirementBean.getTableDataModelList() != null)
		jJRequirementBean.editOneColumn(category);
	}

	public void closeDialog(CloseEvent event) {

		editCategoryTable();

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

		chapterState = true;		

		if (FacesContext.getCurrentInstance().getViewRoot().getViewId()
				.contains("specifications")) {
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			try {
				ec.redirect(((HttpServletRequest) ec.getRequest())
						.getRequestURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void saveJJChapter(JJChapter b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJChapterService.saveJJChapter(b);
	}

	public void updateJJChapter(JJChapter b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJChapterService.updateJJChapter(b);
	}

	private boolean getChapterDialogConfiguration() {
		return jJConfigurationService.getDialogConfig("ChapterDialog",
				"chapter.create.saveandclose");
	}
}

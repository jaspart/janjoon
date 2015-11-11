package com.starit.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.el.ELException;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.util.StringUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;

import org.apache.log4j.Logger;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJRequirementBean.CategoryDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl;

@RooSerializable
@RooJsfManagedBean(entity = JJChapter.class, beanName = "jJChapterBean")
@SuppressWarnings({ "unused", "deprecation" })
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

		if (chapter == null || chapter.getId() == null) {
			chapterList = jJChapterService.getChapters(((LoginBean) LoginBean
					.findBean("loginBean")).getContact().getCompany(), project,
					category, true, new ArrayList<String>());
		} else {
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

		this.project = LoginBean.getProject();
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
		selectedChapterNode = null;
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

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", FacesMessage.SEVERITY_ERROR,
				selectedChapterNode.getData());

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		selectedChapterNode = null;

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
				MessageFactory.getMessage("label_chapter", "").getDetail());
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
			RequirementBean requirementBean = (RequirementBean) LoginBean
					.findBean("requirementBean");
			if (requirementBean != null)
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

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJVersion version = LoginBean.getVersion();
		JJProduct product = LoginBean.getProduct();

		List<JJRequirement> jJRequirementList = jJRequirementService
				.getRequirements(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getCompany(), category, loginBean
						.getAuthorizedMap("Requirement", project, product),
						version, null, null, true, true, false, false, null);

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

	public void updateCategoryId(CategoryDataModel tableDataModel,
			JJRequirementBean jJRequirementBean) {
		categoryId = tableDataModel.getCategoryId();
		jJRequirementBean.setCategoryDataModel(tableDataModel);
	}

	@SuppressWarnings("unchecked")
	public StreamedContent getPreProcessPDF() throws IOException,
			BadElementException, DocumentException {
		this.getProject();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		Document pdf = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(pdf, baos);
		// pdf.set
		pdf.addTitle("THIS IS THE TITLE");
		pdf.open();
		pdf.setPageSize(PageSize.A4);

		Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 30, Font.BOLD);
		fontTitle.setColor(0x24, 0x14, 0x14);

		Font fontChapter = new Font(FontFamily.HELVETICA, 15, Font.BOLD);
		fontChapter.setColor(0x4E, 0x4E, 0x4E);

		Font fontRequirement = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		fontRequirement.setColor(0x5A, 0x5A, 0x5A);

		Font fontNote = new Font(FontFamily.COURIER, 8, Font.BOLD);
		fontNote.setColor(0x82, 0x82, 0x82);

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
		((LoginBean) LoginBean.findBean("loginBean")).loadStyleSheet(style,
				"specs.document.stylesheet");
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

		List<JJRequirement> withOutChapter = jJRequirementService
				.getRequirementsWithOutChapter(((LoginBean) LoginBean
						.findBean("loginBean")).getContact().getCompany(),
						category, loginBean.getAuthorizedMap("Requirement",
								project, LoginBean.getProduct()), LoginBean
								.getVersion(), null, true, true);
		if (withOutChapter != null && !withOutChapter.isEmpty()) {
			paragraph.add(new Chunk("\n "
					+ MessageFactory.getMessage(
							"specification_tree_withOutChapter", "")
							.getDetail() + "\n", fontChapter));

			for (JJRequirement requirement : withOutChapter) {
				paragraph.add(new Chunk("Requirement : "
						+ requirement.getName() + "\n", fontRequirement));
				StringReader strReader = new StringReader(
						requirement.getDescription());

				// StringReader strReader = new StringReader(
				// requirement.getDescription());

				try {
					// List<String> imgUrlRequi = getImageUrlList(requirement
					// .getDescription());
					// int k = 0;
					List<Element> arrList = HTMLWorkerImpl.parseToList(
							strReader, style);

					for (int i = 0; i < arrList.size(); ++i) {
						Element e = (Element) arrList.get(i);

						if (e.getChunks() != null) {
							for (Chunk chunk : (List<Chunk>) e.getChunks()) {
								if (chunk.getImage() != null) {

									com.itextpdf.text.Image img = chunk
											.getImage();
									paragraph.add(Chunk.NEWLINE);
									paragraph.add(img);
									paragraph.add(Chunk.NEWLINE);

								} else {
									paragraph.add(chunk);
								}
							}
						} else {
							paragraph.add(e);
						}
					}
					paragraph.add(Chunk.NEWLINE);
					if (requirement.getNote() != null
							&& requirement.getNote().length() > 2) {
						paragraph.add("Note: "
								+ new Chunk(requirement.getNote() + "\n",
										fontNote));
					}
				} catch (ELException e) {
					System.err.println(e.getMessage());
				}

			}
		}

		// paragraph.add(phrase);
		pdf.add(paragraph);
		pdf.close();

		return new DefaultStreamedContent(new ByteArrayInputStream(
				baos.toByteArray()), "pdf", category.getName().toUpperCase()
				.trim()
				+ "-Spec.pdf");
	}

	@SuppressWarnings("unchecked")
	private void createTreeDocument(JJChapter chapterParent,
			JJCategory category, Paragraph paragraph, Font fontNote,
			Font fontChapter, Font fontRequirement, StyleSheet style)
			throws IOException {

		paragraph.add(new Chunk("\n Chapter:" + chapterParent.getName() + "\n",
				fontChapter));

		StringReader strChapitre = new StringReader(
				chapterParent.getDescription());
		// List<String> imgUrl =
		// getImageUrlList(chapterParent.getDescription());
		// int k = 0;

		List<Element> arrChapitre = HTMLWorkerImpl.parseToList(strChapitre,
				style);
		for (int i = 0; i < arrChapitre.size(); ++i) {
			Element e = (Element) arrChapitre.get(i);

			if (e.getChunks() != null) {
				for (Chunk chunk : (List<Chunk>) e.getChunks()) {
					if (chunk.getImage() != null) {
						Image img = chunk.getImage();
						paragraph.add(Chunk.NEWLINE);
						paragraph.add(img);
						paragraph.add(Chunk.NEWLINE);

					} else {
						paragraph.add(chunk);
					}
				}
			} else {
				paragraph.add(e);
			}
		}
		paragraph.add(Chunk.NEWLINE);

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
				paragraph.add(new Chunk("Requirement: " + requirement.getName()
						+ "\n", fontRequirement));
				StringReader strReader = new StringReader(
						requirement.getDescription());

				// List<String> imgUrlRequi = getImageUrlList(requirement
				// .getDescription());
				// k = 0;
				List<Element> arrList = HTMLWorkerImpl.parseToList(strReader,
						style);
				for (int i = 0; i < arrList.size(); ++i) {
					Element e = (Element) arrList.get(i);

					if (e.getChunks() != null) {
						for (Chunk chunk : (List<Chunk>) e.getChunks()) {
							if (chunk.getImage() != null) {

								Image img = chunk.getImage();
								paragraph.add(Chunk.NEWLINE);
								paragraph.add(img);
								paragraph.add(Chunk.NEWLINE);

							} else {
								paragraph.add(chunk);
							}
						}
					} else {
						paragraph.add(e);
					}
				}
				paragraph.add(Chunk.NEWLINE);
				if (requirement.getNote() != null
						&& requirement.getNote().length() > 2) {
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
							LoginBean.getProduct(), LoginBean.getVersion(),
							onlyActif);

			for (JJRequirement requirement : requirements) {
				if (requirement.getOrdering() != null)
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
				requirement, chapter, LoginBean.getVersion(), null, false,
				true, false);

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

				message = MessageFactory.getMessage(
						"chapter_successfully_droped",
						FacesMessage.SEVERITY_INFO, dragNodeData, dropNodeData,
						"Requirement");

			} else if (dropNodeData.startsWith("leftRoot")) {

				if (requirementCHAPTER == null) {
					message = MessageFactory.getMessage(
							"chapter_unsuccessfully_dropedNoChanges",
							FacesMessage.SEVERITY_WARN, dragNodeData,
							dropNodeData, "Requirement");

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

					message = MessageFactory.getMessage(
							"chapter_successfully_detached",
							FacesMessage.SEVERITY_WARN, dragNodeData,
							dropNodeData, "Requirement");

				}

			} else if (dropNodeData.startsWith("R-")
					|| dropNodeData.startsWith("rightRoot")) {
				message = MessageFactory.getMessage(
						"chapter_unsuccessfully_droped_notAllowed",
						FacesMessage.SEVERITY_ERROR, dragNodeData,
						dropNodeData, "Requirement");

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

					message = MessageFactory.getMessage(
							"chapter_parent_document",
							FacesMessage.SEVERITY_WARN, dragNodeData,
							dropNodeData,
							MessageFactory.getMessage("label_chapter", "")
									.getDetail());
				} else {
					long newChapterPARENTID = Long
							.parseLong(getSplitFromString(dropNodeData, 1));
					newChapterPARENT = jJChapterService
							.findJJChapter(newChapterPARENTID);

					message = MessageFactory.getMessage(
							"chapter_successfully_droped",
							FacesMessage.SEVERITY_INFO, dragNodeData,
							dropNodeData,
							MessageFactory.getMessage("label_chapter", "")
									.getDetail());
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
				message = MessageFactory.getMessage(
						"chapter_unsuccessfully_droped_notAllowed",
						FacesMessage.SEVERITY_INFO, dragNodeData, dropNodeData,
						MessageFactory.getMessage("label_chapter", "")
								.getDetail());

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
		if (jJRequirementBean != null
				&& jJRequirementBean.getTableDataModelList() != null)
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

		// if (FacesContext.getCurrentInstance().getViewRoot().getViewId()
		// .contains("specifications")) {
		// ExternalContext ec = FacesContext.getCurrentInstance()
		// .getExternalContext();
		// try {
		// ec.redirect(((HttpServletRequest) ec.getRequest())
		// .getRequestURI());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }

	}

	// public static String getDescriptionWithOutUrl(String description) {
	// int imgNumber = Math.min(StringUtils.countOccurrencesOf(description,
	// "<img"), StringUtils.countOccurrencesOf(description,
	// "/pages/ckeditor/getimage?imageId="));
	//
	// if (imgNumber > 0) {
	//
	// int k = 0;
	// HttpServletRequest request = ((HttpServletRequest) FacesContext
	// .getCurrentInstance().getExternalContext().getRequest());
	//
	// String url = "";
	// if (!request.getServerName().contains("localhost"))
	// url = "https" + "://" + request.getServerName()
	// + request.getContextPath() + "/images/";
	// else
	// url = "http" + "://" + request.getServerName() + ":"
	// + request.getServerPort() + request.getContextPath()
	// + "/images/";
	//
	// while (k < imgNumber) {
	// int kk = nthOccurrence(description, "alt=\"\" src=", k);
	//
	// String fileName = description
	// .substring(description.indexOf("?imageId=")
	// + "?imageId=".length(),
	// nthOccurrence(description, "style", k) - 2);
	//
	// if (imgExists(url + fileName)) {
	// description = description.substring(0,
	// nthOccurrence(description, "alt=\"\" src=", k)
	// + "alt=\"\" src=".length() + 1)
	// + url
	// + description.substring(description
	// .indexOf("?imageId=")
	// + "?imageId=".length());
	// } else {
	// description = description.substring(0,
	// nthOccurrence(description, "alt=\"\" src=", k))
	// + description.substring(nthOccurrence(description,
	// "style", k));
	// }
	//
	// k++;
	// }
	//
	// return description;
	// } else
	// return description;
	// }
	//
	// public static boolean imgExists(String URLName) {
	// try {
	// HttpURLConnection.setFollowRedirects(false);
	// // note : you may also need
	// // HttpURLConnection.setInstanceFollowRedirects(false)
	// HttpURLConnection con = (HttpURLConnection) new URL(URLName)
	// .openConnection();
	// con.setRequestMethod("HEAD");
	// return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	// public static List<String> getImageUrlList(String description) {
	// int imgNumber = Math.min(StringUtils.countOccurrencesOf(description,
	// "<img"), StringUtils.countOccurrencesOf(description,
	// "/pages/ckeditor/getimage?imageId="));
	// List<String> imgUrl = new ArrayList<String>();
	// String desc = description;
	// HttpServletRequest request = ((HttpServletRequest) FacesContext
	// .getCurrentInstance().getExternalContext().getRequest());
	//
	// if (imgNumber > 0) {
	// int k = 0;
	// while (k < imgNumber) {
	// int kk = desc.indexOf("style") + "style".length();
	// desc = desc.substring(
	// desc.indexOf("?imageId=") + "?imageId=".length(),
	// desc.indexOf("style") - 2);
	// String url = "";
	// if (!request.getServerName().contains("localhost"))
	// url = "https" + "://" + request.getServerName()
	// + request.getContextPath() + "/images/" + desc;
	// else
	// url = "http" + "://" + request.getServerName() + ":"
	// + request.getServerPort()
	// + request.getContextPath() + "/images/" + desc;
	// imgUrl.add(url);
	// desc = description.substring(kk);
	// k++;
	// }
	//
	// }
	// return imgUrl;
	// }
	//
	// public static int nthOccurrence(String str, String c, int n) {
	// int pos = str.indexOf(c, 0);
	// while (n-- > 0 && pos != -1)
	// pos = str.indexOf(c, pos + 1);
	// return pos;
	// }

	public void saveJJChapter(JJChapter b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
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

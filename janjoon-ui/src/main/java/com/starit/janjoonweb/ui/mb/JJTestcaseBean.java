package com.starit.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJVersion;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	@Autowired
	JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Autowired
	JJChapterService jJChapterService;

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = projbean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");
		this.product = prodbean.getProduct();
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void loadData() {
		
		this.getProject();
		this.getProduct();
		this.getVersion();



		createTestcaseTree();

	}

	private void createTestcaseTree() {
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJChapterBean jJChapterBean = (JJChapterBean) session
				.getAttribute("jJChapterBean");
		
		
		rootNode = new DefaultTreeNode("Root", null);

		TreeNode projectNode = new DefaultTreeNode("P-" + project.getId()
				+ "- " + project.getName(), rootNode);

		projectNode.setExpanded(true);

		List<JJCategory> categories = jJCategoryService.getCategories(null,
				false, true, true);
		for (JJCategory category : categories) {

			TreeNode categoryNode = new DefaultTreeNode("CT-"
					+ category.getId() + "- " + category.getName(), projectNode);

			categoryNode.setExpanded(true);

			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(project, product, category, true, true);

			for (JJChapter chapter : parentChapters) {
				TreeNode node = jJChapterBean.createTree(chapter, categoryNode,
						project, product, category, 2);
			}

			// List<JJChapter> chapters = jJChapterService
			// .getAllJJChaptersWithProjectAndCategory(currentProject,
			// jjCategory);
			//
			// for (JJChapter jjChapter : chapters) {
			// TreeNode chapterNode = new DefaultTreeNode("CH-"
			// + jjChapter.getId() + "- " + jjChapter.getName(),
			// categoryNode);
			//
			// // List<JJTestcase> testcases = jJTestcaseService
			// // .getAllJJTestcasesWithChapter(jjChapter);
			// //
			// // for (JJTestcase jjTestcase : testcases) {
			// //
			// // TreeNode testcaseNode = new DefaultTreeNode("TC-"
			// // + jjTestcase.getId() + "- "
			// // + jjTestcase.getName(), chapterNode);
			// // }
			// }
		}

	}

}

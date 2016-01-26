package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.itextpdf.text.Element;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.JJStatusBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.util.RequirementUtil;

@SuppressWarnings("deprecation")
public class CategoryDataModel extends LazyDataModel<RequirementUtil> {

	static Logger logger = Logger.getLogger(CategoryDataModel.class);
	private static final long serialVersionUID = 1L;
	private String nameDataModel;
	private long categoryId;
	private int activeIndex = -1;
	private float coverageProgress = -1;
	private float completionProgress = -1;
	private List<RequirementUtil> allRequirements;
	private boolean rendered;
	private TreeNode chapterTree;
	private boolean expanded;
	private String rowStyleClassFilter = null;
	private boolean filterChapter;
	private JJChapter selectedChapter;

	private JJChapterService jJChapterService;
	private JJCategoryService jJCategoryService;
	private JJRequirementService jJRequirementService;
	private JJTaskService jJTaskService;

	public String getId() {

		if (categoryId != 0)
			return categoryId + "";
		else {
			System.out.println(System.identityHashCode(this));
			return System.identityHashCode(this) + "";
		}
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {

		this.selectedChapter = null;
		this.filterChapter = false;
		this.expanded = expanded;
		if (!this.expanded)
			chapterTree = null;
	}

	public int getActiveIndex() {
		if (completionProgress == -1 && activeIndex != -1) {
			calculCompletionProgress();
			calculCoverageProgress();
		}
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getNameDataModel() {
		return nameDataModel;
	}

	public void setNameDataModel(String nameDataModel) {
		this.nameDataModel = nameDataModel;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public float getCoverageProgress() {
		return coverageProgress;

	}

	public void setCoverageProgress(float coverageProgress) {
		this.coverageProgress = coverageProgress;
	}

	public float getCompletionProgress() {

		return completionProgress;
	}

	public void setCompletionProgress(float completionProgress) {
		this.completionProgress = completionProgress;
	}

	public List<RequirementUtil> getAllRequirements() {
		return allRequirements;
	}

	public void setAllRequirements(List<RequirementUtil> requirements) {
		this.allRequirements = requirements;
	}

	public boolean getRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public JJCategory getCategory() {

		return jJCategoryService.findJJCategory(categoryId);
	}

	public void settingSousChapter(JJChapter ch, TreeNode chapterNode) {
		TreeNode node = new DefaultTreeNode("chapter", ch, chapterNode);
		List<JJChapter> sous_Chapters = jJChapterService
				.getChildrenOfParentChapter(ch, true, true);
		for (JJChapter c : sous_Chapters) {
			settingSousChapter(c, node);
		}

		chapterNode.setExpanded(true);

	}

	public TreeNode getChapterTree() {

		if (expanded && chapterTree == null) {
			chapterTree = new DefaultTreeNode("Root", null);
			JJProject project = LoginBean.getProject();

			TreeNode categoryNode = new DefaultTreeNode("category",
					jJCategoryService.findJJCategory(categoryId), chapterTree);

			categoryNode.setExpanded(true);

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					LoginBean.getCompany(), project,
					jJCategoryService.findJJCategory(categoryId), true, true);

			new DefaultTreeNode("withOutChapter", "withOutChapter",
					categoryNode);
			for (JJChapter ch : chapters) {
				TreeNode chapterNode = new DefaultTreeNode("chapter", ch,
						categoryNode);
				List<JJChapter> sous_Chapters = jJChapterService
						.getChildrenOfParentChapter(ch, true, true);
				for (JJChapter c : sous_Chapters) {
					settingSousChapter(c, chapterNode);
				}

				chapterNode.setExpanded(true);

			}
		}

		return chapterTree;
	}

	public void setChapterTree(TreeNode chapterTree) {
		this.chapterTree = chapterTree;
	}

	public String getRowStyleClassFilter() {

		return rowStyleClassFilter;

	}

	public void setRowStyleClassFilter(String rowStyleClassFilter) {
		this.rowStyleClassFilter = rowStyleClassFilter;
	}

	public boolean isFilterChapter() {
		return filterChapter;
	}

	public void setFilterChapter(boolean filterChapter) {
		this.filterChapter = filterChapter;
	}

	public JJChapter getSelectedChapter() {
		return selectedChapter;
	}

	public void setSelectedChapter(JJChapter selectedChapter) {
		this.selectedChapter = selectedChapter;
	}

	public CategoryDataModel(List<RequirementUtil> data, long categoryId,
			String nameDataModel, boolean rendered,
			JJRequirementService requirementService,
			JJCategoryService categoryService, JJChapterService chapterService,
			JJTaskService taskService) {
		if (data != null)
			this.allRequirements = new ArrayList<RequirementUtil>(
					new HashSet<RequirementUtil>(data));
		else
			this.allRequirements = new ArrayList<RequirementUtil>();
		this.categoryId = categoryId;
		this.nameDataModel = nameDataModel;
		this.rendered = rendered;
		this.activeIndex = -1;
		this.expanded = false;
		this.chapterTree = null;
		this.jJCategoryService = categoryService;
		this.jJChapterService = chapterService;
		this.jJRequirementService = requirementService;
		this.jJTaskService = taskService;
	}

	public String getTableStyle() {
		if (expanded)
			return "Container66";
		else
			return "Container100";
	}

	public void calculCoverageProgress() {
		long t = System.currentTimeMillis();
		if (categoryId != 0) {

			float compteur = 0;
			JJCategory category = jJCategoryService.findJJCategory(categoryId);
			boolean containCategory = false;

			JJStatusBean jJStatusBean = (JJStatusBean) LoginBean
					.findBean("jJStatusBean");

			if (jJStatusBean != null
					&& jJStatusBean.getCategoryDataModel() != null
					&& !jJStatusBean.getCategoryDataModel().isEmpty()) {
				for (int i = 0; i < jJStatusBean.getCategoryDataModel().size(); i++) {
					if (jJStatusBean.getCategoryDataModel().get(i)
							.getCategory().equals(category)
							&& jJStatusBean.getCategoryDataModel().get(i)
									.getCoverageProgress() > -1) {
						coverageProgress = jJStatusBean.getCategoryDataModel()
								.get(i).getCoverageProgress();
						containCategory = true;
						i = jJStatusBean.getCategoryDataModel().size();
					}

				}
			}
			if (!containCategory) {
				boolean sizeIsOne = false;

				List<JJRequirement> requirements = new ArrayList<JJRequirement>();
				for (RequirementUtil r : allRequirements) {
					requirements.add(r.getRequirement());
				}

				if (jJCategoryService.isLowLevel(category,
						LoginBean.getCompany())) {

					for (JJRequirement requirement : requirements) {

						if (jJRequirementService.haveLinkUp(requirement))
							compteur++;

					}

					sizeIsOne = true;
				} else if (jJCategoryService.isHighLevel(category,
						LoginBean.getCompany())
						&& !sizeIsOne) {

					for (JJRequirement requirement : requirements) {
						boolean linkUp = false;
						boolean linkDown = false;

						linkDown = jJRequirementService
								.haveLinkDown(requirement);
						linkUp = jJTaskService.haveTask(requirement, true,
								false, false);

						if (linkUp && linkDown) {
							compteur++;
						} else if (linkUp || linkDown) {
							compteur += 0.5;
						}

					}
				} else {

					for (JJRequirement requirement : requirements) {
						requirement = jJRequirementService
								.findJJRequirement(requirement.getId());
						boolean linkUp = false;
						boolean linkDown = false;

						linkUp = jJRequirementService.haveLinkUp(requirement);
						linkDown = jJRequirementService
								.haveLinkDown(requirement);

						if (linkUp && linkDown) {
							compteur++;
						} else if (linkUp || linkDown) {
							compteur += 0.5;
						}
					}
				}

				if (requirements.isEmpty()) {
					coverageProgress = 0;
				} else {
					coverageProgress = compteur / requirements.size();
				}

				coverageProgress = coverageProgress * 100;
			}

		}
		logger.error("calculCoverageProgress_TaskTracker="
				+ (System.currentTimeMillis() - t));
	}

	public void calculCompletionProgress() {
		long t = System.currentTimeMillis();
		if (categoryId != 0) {

			JJCategory category = jJCategoryService.findJJCategory(categoryId);

			JJStatusBean jJStatusBean = (JJStatusBean) LoginBean
					.findBean("jJStatusBean");
			boolean containCategory = false;

			if (jJStatusBean != null
					&& jJStatusBean.getCategoryDataModel() != null
					&& !jJStatusBean.getCategoryDataModel().isEmpty()) {
				for (int i = 0; i < jJStatusBean.getCategoryDataModel().size(); i++) {
					if (jJStatusBean.getCategoryDataModel().get(i)
							.getCategory().equals(category)
							&& jJStatusBean.getCategoryDataModel().get(i)
									.getCompletionProgress() > -1) {
						completionProgress = jJStatusBean
								.getCategoryDataModel().get(i)
								.getCompletionProgress();
						containCategory = true;
						i = jJStatusBean.getCategoryDataModel().size();
					}

				}
			}
			if (!containCategory) {
				float compteur = 0;
				for (RequirementUtil r : allRequirements) {
					compteur = compteur + calculCompletion(r.getRequirement());

				}

				if (allRequirements.isEmpty()) {
					completionProgress = 0;
				} else {
					completionProgress = compteur / allRequirements.size();
				}

				completionProgress = completionProgress * 100;
			}

		}
		logger.error("calculCompletionProgress_TaskTracker="
				+ (System.currentTimeMillis() - t));
	}

	private float calculCompletion(JJRequirement r) {
		float compteur = 0;
		int size = 0;
		r = jJRequirementService.findJJRequirement(r.getId());
		Set<JJRequirement> linksUp = r.getRequirementLinkUp();
		for (JJRequirement req : linksUp) {

			if (req.getEnabled()) {
				compteur = compteur + calculCompletion(req);
				size++;
			}

		}

		int hasTaskCompleted = 0;
		if (jJTaskService.haveTask(r, true, true, false)) {
			compteur++;
			hasTaskCompleted = 1;
		}
		if (size > 0) {
			compteur = compteur / (size + hasTaskCompleted);
		}

		return compteur;
	}

	@Override
	public RequirementUtil getRowData(String rowKey) {

		for (RequirementUtil req : allRequirements) {
			if (req.getRequirement().getId().toString().equals(rowKey))
				return req;
		}

		return null;
	}

	@Override
	public Object getRowKey(RequirementUtil req) {
		return req.getRequirement().getId();
	}

	public StreamedContent getFile() {

		String buffer = "<category name=\"" + nameDataModel.toUpperCase()
				+ "\">";
		for (RequirementUtil rrr : allRequirements) {
			String description = "";
			StringReader strReader = new StringReader(rrr.getRequirement()
					.getDescription());
			@SuppressWarnings("rawtypes")
			List arrList = null;
			try {

				arrList = HTMLWorker.parseToList(strReader, null);
			} catch (Exception e) {

			}
			if (arrList != null)
				for (int i = 0; i < arrList.size(); ++i) {
					description = description
							+ ((Element) arrList.get(i)).toString();
				}
			else
				description = rrr.getRequirement().getDescription();

			description = description.replace("[", " ").replace("]", "")
					.replace("&#39;", "'").replace("\"", "'")
					.replace("&&", "and").replace("<", "").replace(">", "");
			String note = rrr.getRequirement().getNote();
			if (note != null)
				note = note.replace("[", " ").replace("]", "")
						.replace("&#39;", "'").replace("\"", "'")
						.replace("&&", "and").replace("<", "").replace(">", "");

			String chapterName = "";
			if (rrr.getRequirement().getChapter() != null)
				chapterName = rrr.getRequirement().getChapter().getName();
			String s = "<requirement name=\"" + rrr.getRequirement().getName()
					+ "\"" + System.getProperty("line.separator")
					+ "description=\"" + description + "\""
					+ System.getProperty("line.separator") + "enabled=\"1\""
					+ System.getProperty("line.separator") + "note=\"" + note
					+ "\"" + System.getProperty("line.separator")
					+ "chapter=\"" + chapterName + "\" />";
			buffer = buffer + System.getProperty("line.separator") + s;
		}
		buffer = buffer + System.getProperty("line.separator") + "</category>";
		InputStream stream = new ByteArrayInputStream(buffer.getBytes());

		return new DefaultStreamedContent(stream, "xml",
				nameDataModel.toUpperCase() + "-Spec.xml");

	}

	public boolean checkChapter(RequirementUtil reqUtil) {

		boolean check = !filterChapter
				|| (selectedChapter == null && reqUtil.getRequirement()
						.getChapter() == null)
				|| (selectedChapter != null
						&& reqUtil.getRequirement().getChapter() != null && selectedChapter
							.equals(reqUtil.getRequirement().getChapter()));

		if (check)
			return check;
		else {
			List<JJChapter> list = jJChapterService.getChildrenOfParentChapter(
					selectedChapter, true, true);

			int i = 0;
			while (!check && i < list.size()) {
				check = !filterChapter
						|| (list.get(i) != null
								&& reqUtil.getRequirement().getChapter() != null && list
								.get(i).equals(
										reqUtil.getRequirement().getChapter()));
				i++;
			}

			return check;
		}
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof CategoryDataModel) && (categoryId != 0) ? categoryId == ((CategoryDataModel) object)
				.getCategoryId() : (object == this);
	}

	@Override
	public List<RequirementUtil> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		List<RequirementUtil> data = new ArrayList<RequirementUtil>();
		String filterValue = ((JJRequirementBean) LoginBean
				.findBean("jJRequirementBean")).getFilterValue();
		boolean mine = ((JJRequirementBean) LoginBean
				.findBean("jJRequirementBean")).isMine();
		JJRequirement viewLinkRequirement = ((JJRequirementBean) LoginBean
				.findBean("jJRequirementBean")).getViewLinkRequirement();
		// String filterButton = ((JJRequirementBean) LoginBean
		// .findBean("jJRequirementBean")).getFilterButton();
		JJContact mineContact = null;
		if (mine)
			mineContact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();

		for (RequirementUtil reqUtil : allRequirements) {
			boolean match = true;

			match = (filterValue == null || filterValue.isEmpty())
					|| (reqUtil.getRequirement().getName().toLowerCase()
							.contains(filterValue.toLowerCase()));
			match = match
					&& (mineContact == null
							|| (reqUtil.getRequirement().getUpdatedBy() != null && reqUtil
									.getRequirement().getUpdatedBy()
									.equals(mineContact)) || (reqUtil
							.getRequirement().getCreatedBy() != null && reqUtil
							.getRequirement().getCreatedBy()
							.equals(mineContact)));

			match = match
					&& (viewLinkRequirement == null
							|| (viewLinkRequirement.equals(reqUtil
									.getRequirement())) || (viewLinkRequirement
							.getRequirementLinkDown().contains(
									reqUtil.getRequirement()) || viewLinkRequirement
							.getRequirementLinkUp().contains(
									reqUtil.getRequirement())));

			match = match
					&& ((rowStyleClassFilter == null
							|| rowStyleClassFilter.isEmpty() || reqUtil
							.getStyle().equalsIgnoreCase(rowStyleClassFilter)));

			match = match && checkChapter(reqUtil);

			if (match) {
				data.add(reqUtil);
			}
		}

		// rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);

		// paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
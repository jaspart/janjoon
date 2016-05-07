package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.xml.sax.SAXParseException;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.CategoryDataModel;
import com.starit.janjoonweb.ui.mb.util.CategoryUtil;
import com.starit.janjoonweb.ui.mb.util.FlowStepUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.ReadXMLFile;

@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(JJRequirementBean.class);
	public static final String UPDATE_OPERATION = "update";
	public static final String DELETE_OPERATION = "delete";
	public static final String ADD_OPERATION = "add";

	public static final String SPECIFICATION_WARNING_LINKUP = "specification_warning_linkUp";
	public static final String SPECIFICATION_WARNING_LINKDOWN = "specification_warning_linkDown";
	public static final String SPECIFICATION_WARNING_NOCHAPTER = "specification_warning_NoChapter";
	public static final String SPECIFICATION_ERROR_NOCATEGORYCHAPTER = "specification_error_NoCategoryChapter";
	public static final String MESSAGE_SUCCESSFULLY_UPDATED = "message_successfully_updated";

	public static final String jJRequirement_Specified = "Specified";
	public static final String jJRequirement_UnLinked = "UnLinked";
	public static final String jJRequirement_InProgress = "InProgress";
	public static final String jJRequirement_Finished = "Finished";
	public static final String jJRequirement_InTesting = "InTesting";

	@Autowired
	private JJConfigurationService jJConfigurationService;
	@Autowired
	private JJTaskService jJTaskService;
	@Autowired
	private JJTestcaseService jJTestcaseService;
	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;
	@Autowired
	private JJBugService jJBugService;

	private List<JJTestcase> reqtestCases;
	private List<JJTestcase> reqSelectedtestCases;
	private String testCaseName;
	private int colspan;
	private String filterValue;
	private JJTaskBean jJTaskBean;
	private JJCategory lowCategory;
	private JJCategory mediumCategory;
	private JJCategory highCategory;
	private JJCategory requirementCategory;

	private List<CategoryUtil> categoryList;

	private List<CategoryDataModel> tableDataModelList;
	private CategoryDataModel categoryDataModel;

	private JJRequirement requirement;
	private JJRequirement viewLinkRequirement;

	private JJProject project;
	private JJProject requirementProject;

	private List<JJProject> requirementProjectList;

	private JJProduct product;
	private JJProduct requirementProduct;

	private List<JJProduct> requirementProductList;

	private JJVersion version;
	private JJVersion requirementVersion;

	private List<JJVersion> requirementVersionList;

	private JJChapter requirementChapter;

	private List<JJChapter> requirementChapterList;
	private JJStatus requirementStatus;

	private List<JJStatus> requirementStatusList;

	private String templateHeader;

	private String message;
	private String lowCategoryName;
	private String mediumCategoryName;
	private String highCategoryName;
	private String filterButton;

	private List<JJRequirement> lowRequirementsList;
	private List<JJRequirement> mediumRequirementsList;
	private List<JJRequirement> highRequirementsList;
	private List<JJRequirement> selectedLowRequirementsList;
	private List<JJRequirement> selectedMediumRequirementsList;
	private List<JJRequirement> selectedHighRequirementsList;

	private boolean disabledLowRequirements;
	private boolean disabledMediumRequirements;
	private boolean disabledHighRequirements;
	private boolean disabledVersion;
	private boolean disabledStatus;
	private boolean disabledTask;
	private boolean disabledProject;
	private boolean mine;

	// private boolean changeLow;
	// private boolean changeMedium;
	// private boolean changeHigh;

	private boolean initiateTask;
	private boolean disabledInitTask;

	private JJTask task;

	private List<JJRequirement> storeMapUp;
	private List<JJRequirement> storeMapDown;
	private List<String> namesList;

	private long categoryId;

	private boolean requirementState;

	private JJProject importProject;
	private List<JJProject> importProjectList;

	private JJProduct importProduct;
	private List<JJProduct> importProductList;

	private JJVersion importVersion;
	private List<JJVersion> importVersionList;

	private JJCategory importCategory;
	private List<JJCategory> importCategoryList;

	private JJStatus importStatus;
	private List<JJStatus> importStatusList;

	private boolean disableImportVersion;

	// private boolean copyTestcases;
	// private boolean copyChapters;
	// private boolean copyRequirements;
	// private boolean oldCopyRequirements;
	// private boolean disableImportButton;

	private MindmapNode reqRoot;

	private List<ImportFormat> importFormats;
	private List<ImportFormat> selectedImportFormat;

	private boolean disabledRequirement;

	public void setjJTaskBean(JJTaskBean jJTaskBean) {
		this.jJTaskBean = jJTaskBean;
	}

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public boolean isRated(JJRequirement req) {
		return (((LoginBean) LoginBean.findBean("loginBean")).getContact()
				.getRequirements().contains(req));
	}

	public String getFilterButton() {
		if (filterButton == null) {
			filterButton = "specification_filter_button";
			viewLinkRequirement = null;
		}
		return filterButton;
	}

	public void setFilterButton(String filterButton) {
		this.filterButton = filterButton;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public JJRequirement getViewLinkRequirement() {
		return viewLinkRequirement;
	}

	public void setViewLinkRequirement(JJRequirement viewLinkRequirement) {
		this.viewLinkRequirement = viewLinkRequirement;
	}

	public JJCategory getLowCategory() {
		return lowCategory;
	}

	public void setLowCategory(JJCategory lowCategory) {
		this.lowCategory = lowCategory;
	}

	public JJCategory getMediumCategory() {
		return mediumCategory;
	}

	public void setMediumCategory(JJCategory mediumCategory) {
		this.mediumCategory = mediumCategory;
	}

	public JJCategory getHighCategory() {
		return highCategory;
	}

	public void setHighCategory(JJCategory highCategory) {
		this.highCategory = highCategory;
	}

	public JJCategory getRequirementCategory() {
		return requirementCategory;
	}

	public void setRequirementCategory(JJCategory requirementCategory) {
		this.requirementCategory = requirementCategory;
	}

	public List<CategoryUtil> getCategoryList() {
		if (categoryList == null)
			categoryList = CategoryUtil
					.getCategoryList(
							jJCategoryService.getCategories(null, false, true,
									true, LoginBean.getCompany()),
							tableDataModelList);
		return categoryList;
	}

	public void setCategoryList(List<CategoryUtil> categoryList) {
		this.categoryList = categoryList;
	}

	public List<CategoryDataModel> getTableDataModelList() {
		return tableDataModelList;
	}

	public void setTableDataModelList(
			List<CategoryDataModel> tableDataModelList) {
		this.tableDataModelList = tableDataModelList;
	}

	public CategoryDataModel getCategoryDataModel() {
		return categoryDataModel;
	}

	public void setCategoryDataModel(CategoryDataModel categoryDataModel) {
		this.categoryDataModel = categoryDataModel;
	}

	public String getDisplay(CategoryDataModel dataModel) {
		if (!dataModel.getRendered())
			return "display: none;";
		else
			return "";
	}

	public String getCategoryContainerType() {

		// if (categoryList.size() <= 5)
		return "Container60";
		// else if (categoryList.size() <= 8)
		// return "Container80";
		// else
		// return "Container100";
	}

	public String getTableDataModelSizePct(CategoryDataModel dataModel) {

		int nbOpenedTables = 0;
		int expand = -1;
		if (tableDataModelList != null) {
			for (int i = 0; i < tableDataModelList.size(); i++) {
				if (tableDataModelList.get(i).getCategoryId() != 0
						&& tableDataModelList.get(i).getRendered()) {
					nbOpenedTables++;
					if (tableDataModelList.get(i).isExpanded())
						expand = i;
				}
			}
		}

		double tableDataModelSizePct = 0;
		if (nbOpenedTables > 0) {

			if (nbOpenedTables != 2)
				tableDataModelSizePct = 100 / nbOpenedTables;
			else
				tableDataModelSizePct = 49.5;

		}
		if (((LoginBean) LoginBean.findBean("loginBean")).isMobile()) {
			tableDataModelSizePct = 100;
		}

		if (tableDataModelSizePct == 100)
			return "Container100 Responsive100";
		else {
			if (expand != -1) {
				if (dataModel.equals(tableDataModelList.get(expand)))
					return "ReqContainer60 Responsive100";
				else if (nbOpenedTables == 2) {
					if (dataModel.getRendered())
						return "ReqContainer40 Responsive100";
					else
						return "";
				} else
					return "ReqContainer20 Responsive50";

			} else {
				if (nbOpenedTables != 2)
					return "ReqContainer33 Responsive50";
				else
					return "ReqContainer50 Responsive50";
			}

		}
	}

	public String getMarginLeft(CategoryUtil cc) {
		int jj = categoryList.indexOf(cc);
		if (jj == -1 || jj == 0)
			return "";
		else {
			if (categoryList.get(jj - 1).getCategory().getStage()
					.equals(cc.getCategory().getStage()))
				return "";
			else
				return "margin-left: 20px !important;";
		}
	}

	public String getTableDataModelMargin(CategoryDataModel dataModel) {

		if ((!((LoginBean) LoginBean.findBean("loginBean")).isMobile())
				&& tableDataModelList.indexOf(dataModel) != 0) {
			int nbOpenedTables = 0;
			for (int i = 0; i < tableDataModelList.size(); i++) {
				if (tableDataModelList.get(i).getCategoryId() != 0
						&& tableDataModelList.get(i).getRendered()) {
					nbOpenedTables++;
				}
			}

			if (nbOpenedTables == 3 || nbOpenedTables == 2)
				return "margin-left: 0.2%;";
			else
				return "";
		} else
			return "";

	}

	public void expandTable(CategoryDataModel dataModel) {

		for (int i = 0; i < tableDataModelList.size(); i++) {
			if (tableDataModelList.get(i).getCategoryId() != 0) {

				if (tableDataModelList.get(i).equals(dataModel))
					tableDataModelList.get(i).setExpanded(
							!tableDataModelList.get(i).isExpanded());
				else {
					tableDataModelList.get(i).setExpanded(false);
				}

			}
		}

	}

	public boolean getExpand() {

		if (!((LoginBean) LoginBean.findBean("loginBean")).isMobile()
				&& tableDataModelList != null) {
			boolean r = false;
			int tableNb = 0;
			for (int i = 0; i < tableDataModelList.size(); i++) {

				if (tableDataModelList.get(i).getCategoryId() != 0
						&& tableDataModelList.get(i).isExpanded()) {
					r = true;

				}

				if (tableDataModelList.get(i).getCategoryId() != 0
						&& tableDataModelList.get(i).getRendered()) {
					tableNb++;

				}
			}
			if (tableNb == 3)
				return r;
			else
				return false;
		}

		else
			return false;
	}

	public String getTableDataModelFloatLeft() {
		if (((LoginBean) LoginBean.findBean("loginBean")).isMobile()) {
			return "none";
		} else {
			return "left";
		}
	}

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public JJProject getProject() {

		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProject getRequirementProject() {
		return requirementProject;
	}

	public void setRequirementProject(JJProject requirementProject) {
		this.requirementProject = requirementProject;
	}

	public List<JJProject> getRequirementProjectList() {
		requirementProjectList = jJProjectService.getProjects(
				LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false);
		return requirementProjectList;
	}

	public void setRequirementProjectList(
			List<JJProject> requirementProjectList) {
		this.requirementProjectList = requirementProjectList;
	}

	public JJProduct getProduct() {

		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJProduct getRequirementProduct() {
		return requirementProduct;
	}

	public void setRequirementProduct(JJProduct requirementProduct) {
		this.requirementProduct = requirementProduct;
	}

	public List<JJProduct> getRequirementProductList() {
		requirementProductList = jJProductService.getProducts(
				LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false);
		return requirementProductList;
	}

	public void setRequirementProductList(
			List<JJProduct> requirementProductList) {
		this.requirementProductList = requirementProductList;
	}

	public JJVersion getVersion() {

		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public JJVersion getRequirementVersion() {
		return requirementVersion;
	}

	public void setRequirementVersion(JJVersion requirementVersion) {
		this.requirementVersion = requirementVersion;
	}

	public List<JJVersion> getRequirementVersionList() {
		requirementVersionList = jJVersionService.getVersions(true, true,
				requirementProduct, LoginBean.getCompany(), true);
		return requirementVersionList;
	}

	public void setRequirementVersionList(
			List<JJVersion> requirementVersionList) {
		this.requirementVersionList = requirementVersionList;
	}

	public JJChapter getRequirementChapter() {
		return requirementChapter;
	}

	public void setRequirementChapter(JJChapter requirementChapter) {
		this.requirementChapter = requirementChapter;
	}

	public List<JJChapter> getRequirementChapterList() {
		if (requirementProject != null) {
			requirementChapterList = jJChapterService.getChapters(
					LoginBean.getCompany(), requirementProject,
					requirementCategory, true, new ArrayList<String>());
			return requirementChapterList;
		} else
			return null;

	}

	public void setRequirementChapterList(
			List<JJChapter> requirementChapterList) {
		this.requirementChapterList = requirementChapterList;
	}

	public JJStatus getRequirementStatus() {
		return requirementStatus;
	}

	public void setRequirementStatus(JJStatus requirementStatus) {
		this.requirementStatus = requirementStatus;
	}

	public List<JJStatus> getRequirementStatusList() {
		return requirementStatusList;
	}

	public void setRequirementStatusList(List<JJStatus> requirementStatusList) {

		this.requirementStatusList = requirementStatusList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLowCategoryName() {
		return lowCategoryName;
	}

	public void setLowCategoryName(String lowCategoryName) {
		this.lowCategoryName = lowCategoryName;
	}

	public String getMediumCategoryName() {
		return mediumCategoryName;
	}

	public void setMediumCategoryName(String mediumCategoryName) {
		this.mediumCategoryName = mediumCategoryName;
	}

	public String getHighCategoryName() {
		return highCategoryName;
	}

	public void setHighCategoryName(String highCategoryName) {
		this.highCategoryName = highCategoryName;
	}

	public List<JJRequirement> getLowRequirementsList() {
		return lowRequirementsList;
	}

	public void setLowRequirementsList(
			List<JJRequirement> lowRequirementsList) {
		this.lowRequirementsList = lowRequirementsList;
	}

	public List<JJRequirement> getMediumRequirementsList() {
		return mediumRequirementsList;
	}

	public void setMediumRequirementsList(
			List<JJRequirement> mediumRequirementsList) {
		this.mediumRequirementsList = mediumRequirementsList;
	}

	public List<JJRequirement> getHighRequirementsList() {
		return highRequirementsList;
	}

	public void setHighRequirementsList(
			List<JJRequirement> highRequirementsList) {
		this.highRequirementsList = highRequirementsList;
	}

	public List<JJRequirement> getSelectedLowRequirementsList() {
		return selectedLowRequirementsList;
	}

	public void setSelectedLowRequirementsList(
			List<JJRequirement> selectedLowRequirementsList) {
		this.selectedLowRequirementsList = selectedLowRequirementsList;
	}

	public List<JJRequirement> getSelectedMediumRequirementsList() {
		return selectedMediumRequirementsList;
	}

	public void setSelectedMediumRequirementsList(
			List<JJRequirement> selectedMediumRequirementsList) {
		this.selectedMediumRequirementsList = selectedMediumRequirementsList;
	}

	public List<JJRequirement> getSelectedHighRequirementsList() {
		return selectedHighRequirementsList;
	}

	public void setSelectedHighRequirementsList(
			List<JJRequirement> selectedHighRequirementsList) {
		this.selectedHighRequirementsList = selectedHighRequirementsList;
	}

	public boolean getDisabledLowRequirements() {
		return disabledLowRequirements;
	}

	public void setDisabledLowRequirements(boolean disabledLowRequirements) {
		this.disabledLowRequirements = disabledLowRequirements;
	}

	public boolean getDisabledMediumRequirements() {
		return disabledMediumRequirements;
	}

	public void setDisabledMediumRequirements(
			boolean disabledMediumRequirements) {
		this.disabledMediumRequirements = disabledMediumRequirements;
	}

	public boolean getDisabledHighRequirements() {
		return disabledHighRequirements;
	}

	public void setDisabledHighRequirements(boolean disabledHighRequirements) {
		this.disabledHighRequirements = disabledHighRequirements;
	}

	public boolean getDisabledVersion() {
		return disabledVersion;
	}

	public void setDisabledVersion(boolean disabledVersion) {
		this.disabledVersion = disabledVersion;
	}

	public boolean getDisabledStatus() {
		return disabledStatus;
	}

	public void setDisabledStatus(boolean disabledStatus) {
		this.disabledStatus = disabledStatus;
	}

	public boolean getDisabledTask() {
		return disabledTask;
	}

	public void setDisabledTask(boolean disabledTask) {
		this.disabledTask = disabledTask;
	}

	public boolean getDisabledProject() {
		return disabledProject;
	}

	public void setDisabledProject(boolean disabledProject) {
		this.disabledProject = disabledProject;
	}

	public boolean getInitiateTask() {
		return initiateTask;
	}

	public void setInitiateTask(boolean initiateTask) {
		this.initiateTask = initiateTask;
	}

	public boolean getDisabledInitTask() {
		return disabledInitTask;
	}

	public void setDisabledInitTask(boolean disabledInitTask) {
		this.disabledInitTask = disabledInitTask;
	}

	public JJTask getTask() {
		return task;
	}

	public void setTask(JJTask task) {
		this.task = task;
	}

	// Import Requirement

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public JJProject getImportProject() {
		return importProject;
	}

	public void setImportProject(JJProject importProject) {
		this.importProject = importProject;
	}

	public List<JJProject> getImportProjectList() {
		importProjectList = jJProjectService.getProjects(LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false);
		return importProjectList;
	}

	public void setImportProjectList(List<JJProject> importProjectList) {
		this.importProjectList = importProjectList;
	}

	public JJProduct getImportProduct() {
		return importProduct;
	}

	public void setImportProduct(JJProduct importProduct) {
		this.importProduct = importProduct;
	}

	public List<JJProduct> getImportProductList() {

		importProductList = jJProductService.getProducts(LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false);
		return importProductList;
	}

	public void setImportProductList(List<JJProduct> importProductList) {
		this.importProductList = importProductList;
	}

	public JJVersion getImportVersion() {
		return importVersion;
	}

	public void setImportVersion(JJVersion importVersion) {
		this.importVersion = importVersion;
	}

	public List<JJVersion> getImportVersionList() {

		importVersionList = jJVersionService.getVersions(true, true,
				importProduct, LoginBean.getCompany(), true);

		return importVersionList;
	}

	public void setImportVersionList(List<JJVersion> importVersionList) {
		this.importVersionList = importVersionList;
	}

	public JJCategory getImportCategory() {
		return importCategory;
	}

	public void setImportCategory(JJCategory importCategory) {
		this.importCategory = importCategory;
	}

	public List<JJCategory> getImportCategoryList() {
		importCategoryList = jJCategoryService.getCategories(null, false, true,
				true, LoginBean.getCompany());

		return importCategoryList;
	}

	public void setImportCategoryList(List<JJCategory> importCategoryList) {
		this.importCategoryList = importCategoryList;
	}

	public JJStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(JJStatus importStatus) {
		this.importStatus = importStatus;
	}

	public List<JJStatus> getImportStatusList() {
		List<String> names = new ArrayList<String>();
		names.add("RUNNING");
		names.add("FAILED");
		names.add("PASSED");

		importStatusList = jJStatusService.getStatus("Requirement", true, names,
				true);

		return importStatusList;
	}

	public void setImportStatusList(List<JJStatus> importStatusList) {
		this.importStatusList = importStatusList;
	}

	public boolean getDisableImportVersion() {
		return disableImportVersion;
	}

	public void setDisableImportVersion(boolean disableImportVersion) {
		this.disableImportVersion = disableImportVersion;
	}

	public void newRequirement(long id) {
		long t = System.currentTimeMillis();
		message = "specification_create_header";

		requirementCategory = jJCategoryService.findJJCategory(id);

		colspan = 3;
		requirement = new JJRequirement();
		requirement.setEnabled(true);
		requirement.setDescription(null);

		requirement.setCategory(requirementCategory);

		requirementProject = project;
		disabledProject = false;
		requirement.setProject(requirementProject);

		requirementProduct = product;
		requirement.setProduct(requirementProduct);

		if (requirementProduct != null) {
			disabledVersion = false;
		} else {
			disabledVersion = true;
		}

		requirementVersion = version;
		requirement.setVersioning(requirementVersion);

		requirementChapter = null;
		requirement.setChapter(requirementChapter);
		requirement.setOrdering(null);

		requirementStatusList = jJStatusService.getStatus("Requirement", true,
				null, true);

		requirementStatus = jJStatusService.getOneStatus("NEW", "Requirement",
				true);

		requirement.setStatus(requirementStatus);

		disabledStatus = true;

		if (((RequirementBean) LoginBean.findBean("requirementBean")) == null)
			fullRequirementsList();
		else if (((RequirementBean) LoginBean.findBean("requirementBean"))
				.isReqDialogReqListrender())
			fullRequirementsList();

		selectedLowRequirementsList = new ArrayList<JJRequirement>();
		selectedMediumRequirementsList = new ArrayList<JJRequirement>();
		selectedHighRequirementsList = new ArrayList<JJRequirement>();
		testCaseName = "";
		reqtestCases = new ArrayList<JJTestcase>();
		reqSelectedtestCases = new ArrayList<JJTestcase>();

		task = new JJTask();
		disabledInitTask = false;
		initiateTask = false;
		disabledTask = true;
		requirement.setNumero(0);
		// changeLow = false;
		// changeMedium = false;
		// changeHigh = false;

		requirementState = true;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public List<JJTestcase> getReqtestCases() {
		return reqtestCases;
	}

	public void setReqtestCases(List<JJTestcase> reqtestCases) {
		this.reqtestCases = reqtestCases;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public List<JJTestcase> getReqSelectedtestCases() {
		return reqSelectedtestCases;
	}

	public void setReqSelectedtestCases(List<JJTestcase> reqSelectedtestCases) {
		this.reqSelectedtestCases = reqSelectedtestCases;
	}

	public void editRequirement() {
		long t = System.currentTimeMillis();
		message = "specification_edit_header";

		requirement = jJRequirementService
				.findJJRequirement(requirement.getId());
		requirementCategory = requirement.getCategory();

		colspan = 2;
		requirementProject = requirement.getProject();
		disabledProject = true;

		requirementProduct = requirement.getProduct();

		if (requirementProduct != null) {
			disabledVersion = false;
		} else {
			disabledVersion = true;
		}

		requirementVersion = requirement.getVersioning();

		requirementChapter = requirement.getChapter();

		namesList = new ArrayList<String>();
		namesList.add("NEW");

		requirementStatusList = jJStatusService.getStatus("Requirement", true,
				namesList, true);

		if (requirement.getStatus().getName().equalsIgnoreCase("NEW")) {
			requirementStatus = jJStatusService.getOneStatus("MODIFIED",
					"Requirement", true);
		} else {
			requirementStatus = requirement.getStatus();
		}
		disabledStatus = false;

		fullRequirementsList();
		fullSelectedRequirementsList();

		Set<JJTask> tasks = requirement.getTasks();
		if (tasks.isEmpty()) {
			initiateTask = false;
			task = new JJTask();
		} else {
			for (JJTask temTask : requirement.getTasks()) {
				task = temTask;
				initiateTask = true;
			}
		}
		disabledInitTask = true;
		disabledTask = true;

		int numero = requirement.getNumero() + 1;
		requirement.setNumero(numero);
		reqtestCases = jJTestcaseService.getJJtestCases(requirement);
		reqSelectedtestCases = reqtestCases;
		// changeLow = false;
		// changeMedium = false;
		// changeHigh = false;

		requirementState = false;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void addTestCase(JJTestcaseBean jjTestcaseBean) {
		JJTestcase b = new JJTestcase();
		b.setName(testCaseName);
		b.setEnabled(true);
		b.setRequirement(
				jJRequirementService.findJJRequirement(requirement.getId()));
		b.setDescription("TestCase for Requirement " + requirement.getName());
		b.setOrdering(jJTestcaseService.getMaxOrdering(requirement));
		b.setAutomatic(false);
		jjTestcaseBean.saveJJTestcase(b);
		testCaseName = null;

		reqtestCases = jJTestcaseService.getJJtestCases(requirement);
		reqSelectedtestCases = reqtestCases;

		FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_created", "Test", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void updateReqTestCases(JJTestcaseBean jjTestcaseBean) {
		for (JJTestcase test : reqtestCases) {
			if (!reqSelectedtestCases.contains(test)) {
				test.setEnabled(false);
				jjTestcaseBean.updateJJTestcase(test);
			}

		}
		reqtestCases = jJTestcaseService.getJJtestCases(requirement);
		reqSelectedtestCases = reqtestCases;
	}

	public void requirementInfo() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath()
						+ "/pages/requirement.jsf?requirement="
						+ requirement.getId() + "&faces-redirect=true");

	}

	private void deleteTasksAndTestcase(JJRequirement requirement) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		if (jJTaskBean == null) {
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
			if (jJTaskBean == null)
				jJTaskBean = new JJTaskBean();
		}

		JJTestcaseBean testcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		if (testcaseBean == null)
			testcaseBean = new JJTestcaseBean();

		long t = System.currentTimeMillis();
		JJRequirement req = jJRequirementService
				.findJJRequirement(requirement.getId());
		for (JJTestcase testcase : req.getTestcases()) {

			JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());
			for (JJTask task : tc.getTasks()) {
				task.setEnabled(false);
				jJTaskBean.saveJJTask(task, true, new MutableInt(0));
			}
			tc.setEnabled(false);
			testcaseBean.updateJJTestcase(tc);
		}

		for (JJTask task : req.getTasks()) {
			task.setEnabled(false);
			jJTaskBean.saveJJTask(task, true, new MutableInt(0));
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void deleteRequirement() {
		long t = System.currentTimeMillis();
		requirement = jJRequirementService
				.findJJRequirement(requirement.getId());
		requirement.setEnabled(false);

		requirement.setNumero(requirement.getNumero() != null
				? requirement.getNumero() + 1
				: 1);
		requirementStatus = jJStatusService.getOneStatus("DELETED",
				"Requirement", true);
		requirement.setStatus(requirementStatus);
		List<JJRequirement> listReq = new ArrayList<JJRequirement>(
				requirement.getRequirementLinkDown());
		for (JJRequirement req : listReq) {
			req = jJRequirementService.findJJRequirement(req.getId());
			req.getRequirementLinkUp().remove(requirement);
			req = updateJJRequirement(req);
			updateDataTable(req, req.getCategory(), UPDATE_OPERATION);
		}
		listReq = new ArrayList<JJRequirement>(
				requirement.getRequirementLinkUp());
		requirement.setRequirementLinkUp(new HashSet<JJRequirement>());
		requirement = updateJJRequirement(requirement);
		updateDataTable(requirement, requirement.getCategory(),
				DELETE_OPERATION);

		for (JJRequirement req : listReq) {

			req = updateJJRequirement(req);
			updateDataTable(req, req.getCategory(), UPDATE_OPERATION);
		}

		deleteTasksAndTestcase(requirement);

		closeDialog(false, true);
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		reset();

	}

	public void updateDataTable(JJRequirement req, JJCategory oldCategory,
			String operation) {
		int i = 0;
		RequirementBean requirementBean = (RequirementBean) LoginBean
				.findBean("requirementBean");

		if (requirementBean != null)
			requirementBean.setRootNode(null);

		if (tableDataModelList != null) {
			while (i < tableDataModelList.size()) {
				if (oldCategory != null
						&& oldCategory.getId()
								.equals(tableDataModelList.get(i)
										.getCategoryId())
						&& tableDataModelList.get(i).getRendered()) {
					List<JJRequirement> listRes = tableDataModelList.get(i)
							.getAllRequirements();
					if (operation.equalsIgnoreCase(UPDATE_OPERATION)) {
						int index = listRes.indexOf(req);
						if (index != -1)
							listRes.set(index,
									getRowState(req, jJRequirementService));
					} else if (operation.equalsIgnoreCase(DELETE_OPERATION)) {
						int index = listRes.indexOf(req);
						if (index != -1)
							listRes.remove(index);
					} else if (operation.equalsIgnoreCase(ADD_OPERATION)) {
						listRes.add(getRowState(req, jJRequirementService));
					}

					tableDataModelList.get(i).setAllRequirements(listRes);
					tableDataModelList.get(i).setCompletionProgress(-1);
					tableDataModelList.get(i).setCoverageProgress(-1);
					tableDataModelList.get(i).setActiveIndex(-1);

					flowStepUtils = null;

					if (LoginBean.findBean("jJStatusBean") != null) {
						JJStatusBean jJStatusBean = (JJStatusBean) LoginBean
								.findBean("jJStatusBean");

						if (jJStatusBean.getCategoryDataModel() != null
								&& !jJStatusBean.getCategoryDataModel()
										.isEmpty()) {

							JJCategory cat = jJCategoryService.findJJCategory(
									tableDataModelList.get(i).getCategoryId());
							int j = 0;
							while (j < jJStatusBean.getCategoryDataModel()
									.size()) {
								if (jJStatusBean.getCategoryDataModel().get(j)
										.getCategory().equals(cat)) {
									jJStatusBean.getCategoryDataModel().get(j)
											.setCompletionProgress(-1);
									jJStatusBean.getCategoryDataModel().get(j)
											.setCoverageProgress(-1);
									j = jJStatusBean.getCategoryDataModel()
											.size();
								}
								j++;
							}

						}
					}

					i = tableDataModelList.size();

				}
				i++;
			}
		}

	}

	public void filterTable() {

		int i = 0;
		while (i < tableDataModelList.size()) {
			if (tableDataModelList.get(i).getRendered()) {
				RequestContext.getCurrentInstance()
						.execute("PF('dataTable_" + i + "_Widget').filter();");
			}
			i++;

		}

	}

	public void preReleaseRequirement(long id) {
		categoryId = id;
	}

	public void releaseRequirement() {
		long t = System.currentTimeMillis();
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		for (CategoryDataModel categoryDataModel : tableDataModelList) {
			if (categoryDataModel.getCategoryId() == categoryId) {

				for (JJRequirement r : categoryDataModel.getAllRequirements()) {
					list.add(r);
				}
				break;
			}

		}

		JJStatus status = jJStatusService.getOneStatus("RELEASED",
				"Requirement", true);

		for (JJRequirement req : list) {
			req = jJRequirementService.findJJRequirement(req.getId());
			if (req.getStatus() != status) {
				req.setStatus(status);
				int numero = req.getNumero() + 1;
				req.setNumero(numero);
				req = updateJJRequirement(req);

			}
		}
		reset();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void save() throws IOException {

		long t = System.currentTimeMillis();
		requirement.setProduct(requirementProduct);
		requirement.setVersioning(requirementVersion);
		requirement.setStatus(requirementStatus);
		getRequirementOrder(requirement);
		String u = UPDATE_OPERATION;

		if (requirement.getId() == null) {
			requirement.setProject(requirementProject);
			saveJJRequirement(requirement);
			u = ADD_OPERATION;

		} else {

			if (!requirement.getEnabled()) {
				deleteTasksAndTestcase(requirement);
			}

		}
		boolean specPage = false;

		if (((RequirementBean) LoginBean.findBean("requirementBean")) == null) {
			specPage = true;
		} else if (((RequirementBean) LoginBean.findBean("requirementBean"))
				.isReqDialogReqListrender()) {
			specPage = true;
		}

		if (specPage) {
			requirement = jJRequirementService
					.findJJRequirement(requirement.getId());
			if (requirement.getCategory().equals(lowCategory)) {

				List<JJRequirement> listReq = new ArrayList<JJRequirement>(
						requirement.getRequirementLinkUp());

				for (JJRequirement req : listReq) {
					if (req.getCategory().equals(mediumCategory)
							|| req.getCategory().equals(highCategory)) {
						requirement.getRequirementLinkUp().remove(req);
					}
				}
				requirement.getRequirementLinkUp()
						.addAll(new HashSet<JJRequirement>(
								selectedMediumRequirementsList));
				requirement.getRequirementLinkUp()
						.addAll(new HashSet<JJRequirement>(
								selectedHighRequirementsList));

				requirement = updateJJRequirement(requirement);
				updateDataTable(requirement, requirement.getCategory(), u);

				for (JJRequirement req : listReq)
					if (!selectedHighRequirementsList.contains(req)
							&& !selectedMediumRequirementsList.contains(req))
						updateDataTable(req, req.getCategory(),
								UPDATE_OPERATION);

				for (JJRequirement req : selectedHighRequirementsList)
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);

				for (JJRequirement req : selectedMediumRequirementsList)
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);

			} else if (requirement.getCategory().equals(mediumCategory)) {

				List<JJRequirement> listReq = new ArrayList<JJRequirement>(
						requirement.getRequirementLinkUp());
				for (JJRequirement req : listReq) {
					if (req.getCategory().equals(highCategory)) {
						requirement.getRequirementLinkUp().remove(req);
					}
				}
				requirement.getRequirementLinkUp()
						.addAll(new HashSet<JJRequirement>(
								selectedHighRequirementsList));

				for (JJRequirement req : listReq)
					if (!selectedHighRequirementsList.contains(req))
						updateDataTable(req, req.getCategory(),
								UPDATE_OPERATION);

				for (JJRequirement req : selectedHighRequirementsList)
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);

				listReq = new ArrayList<JJRequirement>(
						requirement.getRequirementLinkDown());
				for (JJRequirement req : listReq) {
					if (req.getCategory().equals(lowCategory)) {

						req = jJRequirementService
								.findJJRequirement(req.getId());
						req.getRequirementLinkUp().remove(requirement);
						req = updateJJRequirement(req);

					}
				}
				for (JJRequirement req : selectedLowRequirementsList) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(requirement);
					req = updateJJRequirement(req);
				}

				requirement = updateJJRequirement(requirement);
				updateDataTable(requirement, requirement.getCategory(), u);

				for (JJRequirement req : listReq)
					if (!selectedLowRequirementsList.contains(req))
						updateDataTable(req, req.getCategory(),
								UPDATE_OPERATION);

				for (JJRequirement req : selectedLowRequirementsList) {
					req = updateJJRequirement(req);
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);
				}

			} else if (requirement.getCategory().equals(highCategory)) {
				List<JJRequirement> listReq = new ArrayList<JJRequirement>(
						requirement.getRequirementLinkDown());
				for (JJRequirement req : listReq) {
					if (req.getCategory().equals(mediumCategory)
							|| req.getCategory().equals(lowCategory)) {

						req = jJRequirementService
								.findJJRequirement(req.getId());
						req.getRequirementLinkUp().remove(requirement);
						req = updateJJRequirement(req);
					}
				}

				for (JJRequirement req : selectedLowRequirementsList) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(requirement);
					req = updateJJRequirement(req);
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);
				}

				for (JJRequirement req : selectedMediumRequirementsList) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(requirement);
					req = updateJJRequirement(req);
					updateDataTable(req, req.getCategory(), UPDATE_OPERATION);
				}

				for (JJRequirement req : listReq)
					if (!selectedLowRequirementsList.contains(req)
							&& !selectedMediumRequirementsList.contains(req))
						updateDataTable(req, req.getCategory(),
								UPDATE_OPERATION);

				requirement = updateJJRequirement(requirement);
				updateDataTable(requirement, requirement.getCategory(), u);
			}
		}
		// requirement = updateJJRequirement(requirement);
		// updateDataTable(requirement, u);
		getWarningList(
				jJRequirementService.findJJRequirement(requirement.getId()));

		RequestContext context = RequestContext.getCurrentInstance();

		if (requirementState) {
			boolean r = getRequirementDialogConfiguration();
			if (r) {
				context.execute("PF('requirementDialogWidget').hide()");
				RequestContext.getCurrentInstance().update("growlForm");
				reset();

				if (!specPage) {
					RequestContext.getCurrentInstance().execute("updateTree()");
				}

			} else {

				editRequirement();
				if (!specPage)
					RequestContext.getCurrentInstance().execute("updateTree()");
			}

		} else {
			context.execute("PF('requirementDialogWidget').hide()");
			RequestContext.getCurrentInstance().update("growlForm");
			reset();

			if (!specPage) {
				RequestContext.getCurrentInstance().execute("updateTree()");
			}
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	public void importRequirement() {
		long t = System.currentTimeMillis();
		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Integer> chapters = new TreeMap<Integer, Integer>();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		JJTestcaseBean testcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		JJTeststepBean testStepBean = (JJTeststepBean) session
				.getAttribute("jJTeststepBean");
		if (testcaseBean == null)
			testcaseBean = new JJTestcaseBean();

		if (testStepBean == null)
			testStepBean = new JJTeststepBean();

		for (ImportFormat format : selectedImportFormat) {

			JJStatus status = jJStatusService.getOneStatus("NEW", "Requirement",
					true);

			JJRequirement req = format.getRequirement();

			JJRequirement requirement = jJRequirementService
					.findJJRequirement(req.getId());

			JJRequirement importRequirement = new JJRequirement();

			importRequirement.setName(requirement.getName() + "(i)");
			importRequirement.setDescription(requirement.getDescription());
			importRequirement.setNumero(requirement.getNumero());
			importRequirement.setProject(
					project != null ? project : requirement.getProject());
			importRequirement.setProduct(importProduct != null
					? importProduct
					: requirement.getProduct());
			importRequirement.setVersioning(importVersion != null
					? importVersion
					: requirement.getVersioning());
			importRequirement.setEnabled(true);
			importRequirement.setCategory(requirement.getCategory());
			importRequirement.setNote(requirement.getNote());
			importRequirement.setStatus(status);

			JJChapter chapter = requirement.getChapter();

			if (chapter == null) {
				importRequirement.setChapter(chapter);
				importRequirement.setOrdering(null);
			} else {

				if (format.getCopyChapter()) {
					JJChapter importChapter = null;

					if (chapters.containsKey(
							Integer.valueOf(chapter.getId().intValue()))) {

						long id = chapters
								.get(Integer
										.valueOf(chapter.getId().intValue()))
								.longValue();

						importChapter = jJChapterService.findJJChapter(id);

					} else {

						importChapter = new JJChapter();
						importChapter.setName(chapter.getName() + "(i)");
						importChapter.setDescription(chapter.getDescription());
						importChapter.setEnabled(true);
						importChapter.setCategory(chapter.getCategory());
						importChapter.setProject(project);
						importChapter.setParent(null);

						elements = getSortedElements(null,
								requirement.getProject(),
								requirement.getCategory(), false,
								jJChapterService, jJRequirementService);
						if (elements.isEmpty()) {
							importChapter.setOrdering(0);
						} else {
							importChapter.setOrdering(elements.lastKey() + 1);
						}

						((JJChapterBean) LoginBean.findBean("jJChapterBean"))
								.saveJJChapter(importChapter);

						chapters.put(
								Integer.valueOf(chapter.getId().intValue()),
								Integer.valueOf(
										importChapter.getId().intValue()));

					}

					importRequirement.setChapter(importChapter);
					elements = getSortedElements(importChapter,
							requirement.getProject(), requirement.getCategory(),
							false, jJChapterService, jJRequirementService);
					if (elements.isEmpty()) {
						importRequirement.setOrdering(0);
					} else {
						importRequirement.setOrdering(elements.lastKey() + 1);
					}

				} else {
					importRequirement.setChapter(chapter);
					elements = getSortedElements(chapter,
							requirement.getProject(), requirement.getCategory(),
							false, jJChapterService, jJRequirementService);
					if (elements.isEmpty()) {
						importRequirement.setOrdering(0);
					} else {
						importRequirement.setOrdering(elements.lastKey() + 1);
					}
				}
			}

			saveJJRequirement(importRequirement);
			updateDataTable(importRequirement, importRequirement.getCategory(),
					ADD_OPERATION);
			reset();

			if (format.getCopyTestcase()) {

				Set<JJTestcase> testcases = requirement.getTestcases();

				if (testcases != null && !testcases.isEmpty()) {

					for (JJTestcase tc : testcases) {

						JJRequirement req1 = jJRequirementService
								.findJJRequirement(importRequirement.getId());

						JJTestcase importTestcase = new JJTestcase();

						JJTestcase testcase = jJTestcaseService
								.findJJTestcase(tc.getId());

						SortedMap<Integer, JJTestcase> testcaseElements = manageTestcaseOrder(
								req1.getChapter());

						if (testcaseElements.isEmpty()) {

							importTestcase.setOrdering(0);
						} else {
							importTestcase.setOrdering(
									testcaseElements.lastKey() + 1);
						}

						importTestcase.setRequirement(req1);
						req1.getTestcases().add(importTestcase);

						importTestcase.setName(testcase.getName() + " (i)");
						importTestcase
								.setDescription(testcase.getDescription());
						importTestcase.setAutomatic(testcase.getAutomatic());
						importTestcase.setEnabled(true);

						testcaseBean.saveJJTestcase(importTestcase);

						Set<JJTeststep> teststeps = testcase.getTeststeps();

						for (JJTeststep teststep : teststeps) {

							JJTestcase tc1 = jJTestcaseService
									.findJJTestcase(importTestcase.getId());

							JJTeststep importTeststep = new JJTeststep();

							importTeststep
									.setOrdering(teststep.getOrdering() != null
											? teststep.getOrdering()
											: 0);

							importTeststep.setTestcase(tc1);
							tc1.getTeststeps().add(importTeststep);

							importTeststep.setName(teststep.getName() + " (i)");
							importTeststep
									.setDescription(teststep.getDescription());
							importTeststep.setActionstep(
									teststep.getActionstep() + " (i)");
							importTeststep.setResultstep(
									teststep.getResultstep() + " (i)");
							importTeststep.setEnabled(true);

							testStepBean.saveJJTeststep(importTeststep);

						}

					}

				}

			}

		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('requirementImportDialogWidget').hide()");
		reset();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private SortedMap<Integer, JJTestcase> manageTestcaseOrder(
			JJChapter chapter) {
		long t = System.currentTimeMillis();
		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(null,
				chapter, LoginBean.getVersion(), null, false, false, false);

		for (JJTestcase testcase : testcases) {
			elements.put(
					testcase.getOrdering() != null ? testcase.getOrdering() : 0,
					testcase);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return elements;
	}

	public void loadTask() {
		long t = System.currentTimeMillis();
		disabledTask = !initiateTask;

		if (initiateTask) {
			task = new JJTask();
			task.setEnabled(true);
			task.setStartDatePlanned(new Date());
			task.setWorkloadPlanned(8);
		} else {
			task = new JJTask();
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void checkCompleteTask() {

	}

	public List<ImportFormat> getImportFormats() {
		return importFormats;
	}

	public void setImportFormats(List<ImportFormat> importFormats) {
		this.importFormats = importFormats;
	}

	public List<ImportFormat> getSelectedImportFormat() {
		return selectedImportFormat;
	}

	public void setSelectedImportFormat(
			List<ImportFormat> selectedImportFormat) {
		this.selectedImportFormat = selectedImportFormat;
	}

	public void fillTableImport() {
		long t = System.currentTimeMillis();
		importFormats = new ArrayList<ImportFormat>();
		selectedImportFormat = new ArrayList<ImportFormat>();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");

		int i = 0;
		for (JJRequirement requirement : jJRequirementService.getRequirements(
				LoginBean.getCompany(), importCategory,
				loginBean.getAuthorizedMap("Requirement", importProject,
						importProduct),
				importVersion, importStatus, null, false, false, false, true,
				false, null)) {
			importFormats.add(new ImportFormat(String.valueOf(i), requirement));
			i++;
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void loadImportFormat() {
		long t = System.currentTimeMillis();

		importProject = project;
		importProduct = product;
		importVersion = version;

		if (importProduct == null) {
			disableImportVersion = true;
			importVersion = null;
		} else {
			disableImportVersion = false;
		}

		fillTableImport();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void handelSelectImportProject() {
		fillTableImport();

	}

	public void handelSelectImportProduct() {
		long t = System.currentTimeMillis();
		importVersion = null;
		if (importProduct == null) {
			disableImportVersion = true;

		} else {
			disableImportVersion = false;

		}
		fillTableImport();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void handelSelectImportVersion() {
		fillTableImport();

	}

	public void handelSelectImportCategory() {
		fillTableImport();

	}

	public void handelSelectImportStatus() {
		fillTableImport();

	}

	public void closeDialog(boolean save, boolean redirection) {

		long t = System.currentTimeMillis();
		System.out.println("close dialog");

		colspan = 0;
		message = null;
		namesList = null;
		lowCategoryName = null;
		mediumCategoryName = null;
		highCategoryName = null;
		if (!save)
			requirement = null;
		requirementChapter = null;
		requirementChapterList = null;
		requirementProduct = null;
		requirementProductList = null;
		requirementProject = null;
		requirementProjectList = null;
		requirementVersion = null;
		requirementVersionList = null;
		requirementStatus = null;
		requirementStatusList = null;
		requirementCategory = null;
		lowRequirementsList = null;
		mediumRequirementsList = null;
		highRequirementsList = null;
		selectedLowRequirementsList = null;
		selectedMediumRequirementsList = null;
		selectedHighRequirementsList = null;
		disabledLowRequirements = true;
		disabledMediumRequirements = true;
		disabledHighRequirements = true;
		task = null;

		reqtestCases = null;
		reqSelectedtestCases = reqtestCases;
		testCaseName = null;

		storeMapUp = null;
		storeMapDown = null;

		requirementState = true;

		System.out.println("fin");

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		if (redirection)
			RequestContext.getCurrentInstance().execute("updateDataTable()");

	}

	public void closeDialogImport() {
		long t = System.currentTimeMillis();
		message = null;
		importFormats = null;
		selectedImportFormat = null;
		importCategory = null;
		importProduct = null;
		importProject = null;
		importVersion = null;
		importStatus = null;
		importCategoryList = null;
		importProductList = null;
		importProjectList = null;
		importVersionList = null;
		importStatusList = null;

		// loadData();
		// LoginBean.redirectPage();

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	public boolean getDisabledEdit(JJRequirement requirement) {
		// if (requirement.getStatus() != null) {
		// if (requirement.getStatus().getName().equalsIgnoreCase("RELEASED")) {
		// return true;
		// } else {
		// return false;
		// }
		// } else
		// return false;
		return false;

	}

	public void handleSelectProject() {
		long t = System.currentTimeMillis();
		getRequirementChapterList();
		requirementChapter = null;
		fullRequirementsList();

		selectedLowRequirementsList = new ArrayList<JJRequirement>();
		selectedMediumRequirementsList = new ArrayList<JJRequirement>();
		selectedHighRequirementsList = new ArrayList<JJRequirement>();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void handleSelectProduct() {
		long t = System.currentTimeMillis();
		if (requirementProduct != null) {
			disabledVersion = false;
			requirementVersion = null;
		} else {
			disabledVersion = true;
			requirementVersion = null;
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void handleSelectVersion() {

	}

	public void handleSelectChapter() {

	}

	public void handleSelectStatus() {
		if (requirementStatus != null) {
			if (requirementStatus.getName().equalsIgnoreCase("CANCELED")
					|| requirementStatus.getName()
							.equalsIgnoreCase("DELETED")) {
				requirement.setEnabled(false);
			} else {
				requirement.setEnabled(true);
			}
		}
	}

	private String warnMessage;

	public String getWarnMessage() {
		return warnMessage;
	}

	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	private boolean disabledExport;

	public boolean getDisabledExport() {
		return disabledExport;
	}

	public void setDisabledExport(boolean disabledExport) {
		this.disabledExport = disabledExport;
	}

	private void loadParameter() {
		long t = System.currentTimeMillis();

		project = LoginBean.getProject();
		product = LoginBean.getProduct();
		version = LoginBean.getVersion();

		JJChapterBean jJChapterBean = (JJChapterBean) LoginBean
				.findBean("jJChapterBean");
		if (jJChapterBean == null) {
			jJChapterBean = new JJChapterBean();
		}
		if (project == null) {
			warnMessage = MessageFactory
					.getMessage("specification_req_warnMessage", "")
					.getDetail();
			disabledExport = true;
			disabledRequirement = true;
			jJChapterBean.setWarnMessage(MessageFactory
					.getMessage("specification_chap_warnMessage", "")
					.getDetail());
			jJChapterBean.setDisabledChapter(true);
		} else {
			warnMessage = MessageFactory
					.getMessage("specification_req_exportTo", "").getDetail()
					+ " PDF";
			disabledExport = false;
			disabledRequirement = false;
			jJChapterBean.setWarnMessage(MessageFactory
					.getMessage("specification_req_managedocument", "")
					.getDetail());
			jJChapterBean.setDisabledChapter(false);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void addRowToCategoryDataModel() {
		long t = System.currentTimeMillis();
		int index = 0;
		long id = requirement.getCategory().getId();

		if (id == lowCategory.getId()) {
			index = 0;
		} else if (id == mediumCategory.getId()) {
			index = 1;
		} else if (id == highCategory.getId()) {
			index = 2;
		}

		for (int i = 0; i < tableDataModelList.size(); i++) {
			CategoryDataModel categoryDataModel = tableDataModelList.get(i);
			if (i == index) {
				List<JJRequirement> requirements = categoryDataModel
						.getAllRequirements();
				requirements.add(0,
						getRowState(requirement, jJRequirementService));
			}
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void editRowFromCategoryDataModel() {
		long t = System.currentTimeMillis();
		int index = 0;
		long id = requirement.getCategory().getId();

		if (id == lowCategory.getId()) {
			index = 0;
		} else if (id == mediumCategory.getId()) {
			index = 1;
		} else if (id == highCategory.getId()) {
			index = 2;
		}

		for (int i = 0; i < tableDataModelList.size(); i++) {
			CategoryDataModel categoryDataModel = tableDataModelList.get(i);
			if (i == index && !requirement.getEnabled()) {
				List<JJRequirement> requirements = categoryDataModel
						.getAllRequirements();
				requirements.remove(requirement);
			}
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void loadData() {

		System.out.println("debut load");

		long t = System.currentTimeMillis();

		if (tableDataModelList == null) {
			templateHeader = "";
			fullTableDataModelList();
		}

		loadParameter();

		// String[] results = templateHeader.split("-");
		//
		// for (int i = 0; i < results.length; i++) {
		//
		// CategoryDataModel categoryDataModel = tableDataModelList.get(i);
		// long categoryId = categoryDataModel.getCategoryId();
		//
		// if (categoryId != 0) {
		// JJCategory category = jJCategoryService
		// .findJJCategory(categoryId);
		//
		// List<JJRequirement> requirements = getRequirementsList(
		// category, product, version, project, true);
		//
		// categoryDataModel = new CategoryDataModel(
		// getListOfRequiremntUtils(requirements),
		// category.getId(), category.getName(), true,
		// jJRequirementService, jJCategoryService,
		// jJChapterService, jJTaskService);
		//
		// tableDataModelList.set(i, categoryDataModel);
		// }
		// }

		System.out.println("fin load");
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	// public void handleChangeLow() {
	// changeLow = true;
	// }
	//
	// public void handleChangeMedium() {
	// changeMedium = true;
	// }
	//
	// public void handleChangeHigh() {
	// changeHigh = true;
	// }

	public void updateTemplate(long id, boolean rendered, boolean add) {

		long t = System.currentTimeMillis();
		if (add) {

			JJCategory category = jJCategoryService.findJJCategory(id);

			if (!templateHeader.isEmpty()) {
				String[] categories = templateHeader.split("-");

				switch (categories.length) {
					case 1 :
						if (category.getStage() > lowCategory.getStage()) {
							mediumCategory = category;
							templateHeader += String
									.valueOf(mediumCategory.getId()) + "-";
						} else {
							mediumCategory = lowCategory;
							tableDataModelList.set(1,
									tableDataModelList.get(0));
							tableDataModelList.set(0,
									new CategoryDataModel(
											new ArrayList<JJRequirement>(), 0,
											"", false, jJRequirementService,
											jJCategoryService, jJChapterService,
											jJTaskService));
							lowCategory = category;
							templateHeader = String.valueOf(lowCategory.getId())
									+ "-"
									+ String.valueOf(mediumCategory.getId())
									+ "-";
						}

						break;

					case 2 :
						if (category.getStage() > mediumCategory.getStage()) {
							highCategory = category;
							templateHeader += String
									.valueOf(highCategory.getId());
						} else if (category.getStage() > lowCategory
								.getStage()) {
							highCategory = mediumCategory;
							mediumCategory = category;
							tableDataModelList.set(2,
									tableDataModelList.get(1));
							tableDataModelList.set(1,
									new CategoryDataModel(
											new ArrayList<JJRequirement>(), 0,
											"", false, jJRequirementService,
											jJCategoryService, jJChapterService,
											jJTaskService));
							templateHeader = String.valueOf(lowCategory.getId())
									+ "-"
									+ String.valueOf(mediumCategory.getId())
									+ "-";
							templateHeader += String
									.valueOf(highCategory.getId());
						} else {
							highCategory = mediumCategory;
							mediumCategory = lowCategory;
							tableDataModelList.set(2,
									tableDataModelList.get(1));
							tableDataModelList.set(1,
									tableDataModelList.get(0));
							tableDataModelList.set(0,
									new CategoryDataModel(
											new ArrayList<JJRequirement>(), 0,
											"", false, jJRequirementService,
											jJCategoryService, jJChapterService,
											jJTaskService));
							lowCategory = category;
							templateHeader = String.valueOf(lowCategory.getId())
									+ "-"
									+ String.valueOf(mediumCategory.getId())
									+ "-";
							templateHeader += String
									.valueOf(highCategory.getId());
						}
						break;

					case 3 :
						if (category.getStage() > mediumCategory.getStage()) {
							highCategory = category;
						} else if (category.getStage() > lowCategory
								.getStage()) {
							mediumCategory = category;
						} else {
							lowCategory = category;
						}

						templateHeader = String.valueOf(lowCategory.getId())
								+ "-" + String.valueOf(mediumCategory.getId())
								+ "-" + String.valueOf(highCategory.getId());
						break;
				}
			} else {
				lowCategory = category;
				templateHeader = String.valueOf(lowCategory.getId()) + "-";
			}

			editTableDataModelList(id, rendered);
		} else {
			int i = 0;
			while (i < tableDataModelList.size()
					&& tableDataModelList.get(i).getCategoryId() != id)
				i++;

			if (i < tableDataModelList.size()
					&& tableDataModelList.get(i).getCategoryId() == id) {
				int j = i + 1;
				while (j < tableDataModelList.size()) {
					tableDataModelList.set(j - 1, tableDataModelList.get(j));
					j++;
				}
				tableDataModelList.set(tableDataModelList.size() - 1,
						new CategoryDataModel(new ArrayList<JJRequirement>(), 0,
								"", false, jJRequirementService,
								jJCategoryService, jJChapterService,
								jJTaskService));
			}

			JJCategory category = jJCategoryService.findJJCategory(id);

			if (!templateHeader.isEmpty()) {
				String[] categories = templateHeader.split("-");

				switch (categories.length) {
					case 1 :
						lowCategory = null;
						templateHeader = "";
						break;

					case 2 :
						if (category.equals(mediumCategory)) {
							mediumCategory = null;
							templateHeader = String.valueOf(lowCategory.getId())
									+ "-";
						} else if (category.equals(lowCategory)) {
							lowCategory = mediumCategory;
							mediumCategory = null;
							templateHeader = String.valueOf(lowCategory.getId())
									+ "-";
						}
						break;

					case 3 :
						if (category.equals(mediumCategory)) {
							mediumCategory = highCategory;
							highCategory = null;

						} else if (category.equals(lowCategory)) {
							lowCategory = mediumCategory;
							mediumCategory = highCategory;
							highCategory = null;

						} else if (category.equals(highCategory)) {
							highCategory = null;
						}

						templateHeader = String.valueOf(lowCategory.getId())
								+ "-" + String.valueOf(mediumCategory.getId())
								+ "-";
						break;
				}
			}

		}
		categoryList = null;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void fullTableDataModelList() {
		long t = System.currentTimeMillis();
		tableDataModelList = new ArrayList<CategoryDataModel>();

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		List<JJCategory> categories = new ArrayList<JJCategory>(jJContactService
				.findJJContact(contact.getId()).getCategories());
		Collections.sort(categories, new Comparator<JJCategory>() {
			@Override
			public int compare(JJCategory o1, JJCategory o2) {
				return o1.getStage().compareTo(o2.getStage());

			}
		});
		if (!((LoginBean) LoginBean.findBean("loginBean")).isMobile()) {

			for (int i = 0; i < 3; i++) {
				CategoryDataModel requirementDataModel = new CategoryDataModel(
						new ArrayList<JJRequirement>(), 0, "", false,
						jJRequirementService, jJCategoryService,
						jJChapterService, jJTaskService);
				tableDataModelList.add(requirementDataModel);
			}

			for (JJCategory cat : categories) {
				if (cat.getEnabled())
					updateTemplate(cat.getId(), true, true);
			}
			logger.info("REQ-Tracker=" + (System.currentTimeMillis() - t));
		} else {

			for (int i = 0; i < 3; i++) {
				CategoryDataModel requirementDataModel = new CategoryDataModel(
						new ArrayList<JJRequirement>(), 0, "", false,
						jJRequirementService, jJCategoryService,
						jJChapterService, jJTaskService);
				tableDataModelList.add(requirementDataModel);
			}

			for (JJCategory cat : categories) {
				if (cat.getEnabled())
					updateTemplate(cat.getId(), false, true);
			}
			updateTemplate(
					((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorisationService().getCategory().getId(),
					true, true);
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

		}

	}

	public void editOneColumn(JJCategory category) {
		long t = System.currentTimeMillis();
		int i = 0;

		if (category.equals(lowCategory)) {
			i = 0;
		} else if (category.equals(mediumCategory)) {
			i = 1;
		} else if (category.equals(highCategory)) {
			i = 2;
		}

		if (tableDataModelList.get(i).getRendered()) {

			List<JJRequirement> requirements = getRequirementsList(category,
					product, version, project, true);
			CategoryDataModel categoryDataModel = new CategoryDataModel(
					getListOfRequiremntUtils(requirements), category.getId(),
					category.getName(), tableDataModelList.get(i).getRendered(),
					jJRequirementService, jJCategoryService, jJChapterService,
					jJTaskService);
			categoryDataModel
					.setExpanded(tableDataModelList.get(i).isExpanded());

			tableDataModelList.set(i, categoryDataModel);
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		}

	}

	public boolean getDisabledRequirement() {
		return disabledRequirement;
	}

	public void setDisabledRequirement(boolean disabledRequirement) {
		this.disabledRequirement = disabledRequirement;
	}

	private void editTableDataModelList(long id, boolean rendered) {
		long t = System.currentTimeMillis();
		String[] results = templateHeader.split("-");

		for (int i = 0; i < results.length; i++) {
			String result = results[i];
			JJCategory category = null;
			if (i == 0) {
				category = lowCategory;
			} else if (i == 1) {
				category = mediumCategory;
			} else if (i == 2) {
				category = highCategory;
			}

			CategoryDataModel categoryDataModel = tableDataModelList.get(i);

			long categoryId = categoryDataModel.getCategoryId();

			if (!((LoginBean) LoginBean.findBean("loginBean")).isMobile()) {
				if (categoryId != Long.parseLong(result)) {

					List<JJRequirement> requirements = getRequirementsList(
							category, product, version, project, true);

					categoryDataModel = new CategoryDataModel(
							getListOfRequiremntUtils(requirements),
							category.getId(), category.getName(), true,
							jJRequirementService, jJCategoryService,
							jJChapterService, jJTaskService);
					categoryDataModel.setActiveIndex(-1);

					tableDataModelList.set(i, categoryDataModel);
				}
			} else {

				List<JJRequirement> requirements = null;

				if (id == Long.parseLong(result))
					requirements = getRequirementsList(category, product,
							version, project, true);

				categoryDataModel = new CategoryDataModel(
						getListOfRequiremntUtils(requirements),
						category.getId(), category.getName(),
						requirements != null, jJRequirementService,
						jJCategoryService, jJChapterService, jJTaskService);
				tableDataModelList.set(i, categoryDataModel);

			}

		}

		logger.info("edit data");
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	@PostConstruct
	public void init() {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		if (contact != null) {
			long t = System.currentTimeMillis();
			// project = LoginBean.getProject();
			// product = LoginBean.getProduct();
			// version = LoginBean.getVersion();

			loadParameter();
			templateHeader = "";
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		} else {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			session.setAttribute("jJRequirementBean", null);
		}

	}

	private void fullRequirementsList() {
		long t = System.currentTimeMillis();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		if (lowCategory != null
				&& requirementCategory.getId().equals(lowCategory.getId())) {
			disabledLowRequirements = true;
			disabledMediumRequirements = false;
			disabledHighRequirements = false;

		} else if (mediumCategory != null
				&& requirementCategory.getId().equals(mediumCategory.getId())) {
			disabledLowRequirements = false;
			disabledMediumRequirements = true;
			disabledHighRequirements = false;

		} else if (highCategory != null
				&& requirementCategory.getId().equals(highCategory.getId())) {
			disabledLowRequirements = false;
			disabledMediumRequirements = false;
			disabledHighRequirements = true;
		}

		if (lowCategory == null) {
			lowCategoryName = "Low Category :";
			disabledLowRequirements = true;
		} else {
			lowCategoryName = loginBean.checkMessage(lowCategory) + " :";
			lowRequirementsList = getRequirementsList(lowCategory, product,
					null, requirementProject, true);
		}

		if (mediumCategory == null) {
			mediumCategoryName = "Medium Category :";
			disabledMediumRequirements = true;
		} else {
			mediumCategoryName = loginBean.checkMessage(mediumCategory) + " :";
			mediumRequirementsList = getRequirementsList(mediumCategory,
					product, null, requirementProject, true);
		}

		if (highCategory == null) {
			highCategoryName = "High Category :";
			disabledHighRequirements = true;
		} else {
			highCategoryName = loginBean.checkMessage(highCategory) + " :";
			highRequirementsList = getRequirementsList(highCategory, product,
					null, requirementProject, true);
		}

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private void fullSelectedRequirementsList() {
		long t = System.currentTimeMillis();

		Set<JJRequirement> list;
		storeMapUp = new ArrayList<JJRequirement>();
		storeMapDown = new ArrayList<JJRequirement>();

		selectedLowRequirementsList = new ArrayList<JJRequirement>();
		selectedMediumRequirementsList = new ArrayList<JJRequirement>();
		selectedHighRequirementsList = new ArrayList<JJRequirement>();

		if (lowCategory != null
				&& requirementCategory.getId().equals(lowCategory.getId())) {
			list = requirement.getRequirementLinkUp();

			if (mediumCategory != null) {
				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId()
							.equals(mediumCategory.getId())
							&& requirement.getEnabled()) {
						storeMapUp.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedMediumRequirementsList = tempList;
			}

			if (highCategory != null) {

				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId().equals(
							highCategory.getId()) && requirement.getEnabled()) {
						storeMapUp.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedHighRequirementsList = tempList;
			}
		}

		if (mediumCategory != null
				&& requirementCategory.getId().equals(mediumCategory.getId())) {

			if (lowCategory != null) {
				list = requirement.getRequirementLinkDown();
				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId().equals(
							lowCategory.getId()) && requirement.getEnabled()) {
						storeMapDown.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedLowRequirementsList = tempList;

			}

			if (highCategory != null) {
				list = requirement.getRequirementLinkUp();
				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId().equals(
							highCategory.getId()) && requirement.getEnabled()) {
						storeMapUp.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedHighRequirementsList = tempList;
			}

		}

		if (highCategory != null
				&& requirementCategory.getId().equals(highCategory.getId())) {

			list = requirement.getRequirementLinkDown();

			if (lowCategory != null) {

				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId().equals(
							lowCategory.getId()) && requirement.getEnabled()) {
						storeMapDown.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedLowRequirementsList = tempList;
			}

			if (mediumCategory != null) {
				List<JJRequirement> tempList = new ArrayList<JJRequirement>();
				for (JJRequirement requirement : list) {
					if (requirement.getCategory().getId()
							.equals(mediumCategory.getId())
							&& requirement.getEnabled()) {
						storeMapDown.add(requirement);
						tempList.add(requirement);
					}
				}
				selectedMediumRequirementsList = tempList;
			}
		}
		list = null;

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private List<JJRequirement> getRequirementsList(JJCategory category,
			JJProduct product, JJVersion version, JJProject project,
			boolean withProject) {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		long t = System.currentTimeMillis();
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		if (category != null && withProject) {
			list = jJRequirementService.getRequirements(LoginBean.getCompany(),
					category,
					loginBean.getAuthorizedMap("Requirement", project, product),
					version, null, null, false, false, true, true, false, null);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return list;
	}

	public void getRequirementOrder(JJRequirement requirementToOrder) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseBean testcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		if (testcaseBean == null) {
			testcaseBean = new JJTestcaseBean();
		}

		if (jJTaskBean == null) {
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
			if (jJTaskBean == null) {
				jJTaskBean = new JJTaskBean();
			}
		}

		long t = System.currentTimeMillis();
		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Object> subElements = null;
		SortedMap<Integer, JJTestcase> testcases = null;
		SortedMap<Integer, JJTestcase> subTestcases = null;

		if (requirementToOrder.getId() == null) {
			// Mode New Requirement
			requirementToOrder.setChapter(requirementChapter);
			if (requirementChapter != null) {
				elements = getSortedElements(requirementChapter,
						requirementToOrder.getProject(),
						requirementToOrder.getCategory(), false,
						jJChapterService, jJRequirementService);

				if (elements.isEmpty()) {
					requirementToOrder.setOrdering(0);
				} else {
					requirementToOrder.setOrdering(elements.lastKey() + 1);
				}
			} else {
				requirementToOrder.setOrdering(null);
			}
		} else {
			subTestcases = getSortedTestcases(jJRequirementService
					.findJJRequirement(requirementToOrder.getId()), null);
			testcases = getSortedTestcases(null,
					jJRequirementService
							.findJJRequirement(requirementToOrder.getId())
							.getChapter());

			// Mode Edit Requirement

			if (requirementChapter != null) {

				if (requirementToOrder.getChapter() != null) {

					// Drag R and Drop to C

					if (requirementToOrder.getChapter()
							.getId() != requirementChapter.getId()) {

						SortedMap<Integer, JJTestcase> testcases1 = getSortedTestcases(
								null, requirementChapter);

						int requirementOrder = requirementToOrder.getOrdering();

						elements = getSortedElements(
								requirementToOrder.getChapter(),
								requirementToOrder.getProject(),
								requirementToOrder.getCategory(), false,
								jJChapterService, jJRequirementService);

						subElements = elements.tailMap(requirementOrder);

						requirementToOrder.setChapter(requirementChapter);

						elements = getSortedElements(requirementChapter,
								requirementToOrder.getProject(),
								requirementToOrder.getCategory(), false,
								jJChapterService, jJRequirementService);

						if (elements.isEmpty()) {
							requirementToOrder.setOrdering(0);

						} else {
							requirementToOrder
									.setOrdering(elements.lastKey() + 1);
						}

						requirementToOrder = updateJJRequirement(
								requirementToOrder);

						subElements.remove(requirementOrder);

						for (Map.Entry<Integer, Object> entry : subElements
								.entrySet()) {

							// String className = entry.getValue().getClass()
							// .getSimpleName();
							if (entry.getValue() instanceof JJChapter) {

								JJChapter chapter = (JJChapter) entry
										.getValue();

								int lastOrder = chapter.getOrdering();
								chapter.setOrdering(lastOrder - 1);
								((JJChapterBean) LoginBean
										.findBean("jJChapterBean"))
												.updateJJChapter(chapter);

							} else if (entry
									.getValue() instanceof JJRequirement) {

								JJRequirement r = (JJRequirement) entry
										.getValue();

								int lastOrder = r.getOrdering();
								r.setOrdering(lastOrder - 1);

								r = updateJJRequirement(r);
								reset();
							}

						}

						if (!subTestcases.isEmpty()) {

							SortedMap<Integer, JJTestcase> tempTestcases;

							for (Map.Entry<Integer, JJTestcase> entry : subTestcases
									.entrySet()) {

								int testcaseOrder = entry.getKey();

								JJTestcase testcase = entry.getValue();

								tempTestcases = new TreeMap<Integer, JJTestcase>();
								tempTestcases = testcases
										.tailMap(testcaseOrder);
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

					} else {

						requirementToOrder = updateJJRequirement(
								requirementToOrder);
						reset();

					}
				} else {

					SortedMap<Integer, JJTestcase> testcases1 = getSortedTestcases(
							null, requirementChapter);

					requirementToOrder.setChapter(requirementChapter);

					elements = getSortedElements(requirementChapter,
							requirementToOrder.getProject(),
							requirementToOrder.getCategory(), false,
							jJChapterService, jJRequirementService);
					if (elements.isEmpty()) {
						requirementToOrder.setOrdering(0);
					} else {
						requirementToOrder.setOrdering(elements.lastKey() + 1);
					}

					requirementToOrder = updateJJRequirement(
							requirementToOrder);
					reset();

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

				}

			} else {

				// Drag R and Drop to Left

				if (requirementToOrder.getChapter() != null
						&& requirementToOrder.getOrdering() != null) {

					int requirementOrder = requirementToOrder.getOrdering();

					elements = getSortedElements(
							requirementToOrder.getChapter(),
							requirementToOrder.getProject(),
							requirementToOrder.getCategory(), false,
							jJChapterService, jJRequirementService);

					subElements = elements.tailMap(requirementOrder);

					requirementToOrder.setChapter(null);
					requirementToOrder.setOrdering(null);

					requirementToOrder = updateJJRequirement(
							requirementToOrder);
					reset();

					subElements.remove(requirementOrder);

					for (Map.Entry<Integer, Object> entry : subElements
							.entrySet()) {

						// String className = entry.getValue().getClass()
						// .getSimpleName();
						if (entry.getValue() instanceof JJChapter) {

							JJChapter chapter = (JJChapter) entry.getValue();

							int lastOrder = chapter.getOrdering();
							chapter.setOrdering(lastOrder - 1);

							((JJChapterBean) LoginBean
									.findBean("jJChapterBean"))
											.updateJJChapter(chapter);

						} else if (entry.getValue() instanceof JJRequirement) {

							JJRequirement r = (JJRequirement) entry.getValue();

							int lastOrder = r.getOrdering();
							r.setOrdering(lastOrder - 1);

							r = updateJJRequirement(r);
							reset();
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

				} else {
					requirementToOrder = updateJJRequirement(
							requirementToOrder);
					reset();
				}

			}

			Set<JJTask> tasks = null;
			Set<JJTestcase> testcaseList = null;

			JJRequirement req = jJRequirementService
					.findJJRequirement(requirementToOrder.getId());

			if (requirementStatus != null
					&& (requirementStatus.getName().equalsIgnoreCase("CANCELED")
							|| requirementStatus.getName()
									.equalsIgnoreCase("DELETED"))) {

				tasks = req.getTasks();
				for (JJTask task : tasks) {
					task.setEnabled(false);
					jJTaskBean.saveJJTask(task, true, new MutableInt(0));
				}

				testcaseList = req.getTestcases();
				for (JJTestcase testcase : testcaseList) {
					testcase.setEnabled(false);
					testcaseBean.updateJJTestcase(testcase);
				}

				if (requirementStatus.getName().equalsIgnoreCase("DELETED")) {
					req.setEnabled(false);
					req = updateJJRequirement(req);
					reset();
				}

			} else if (requirementStatus != null && (requirementStatus.getName()
					.equalsIgnoreCase("MODIFIED"))) {
				tasks = req.getTasks();
				for (JJTask task : tasks) {
					task.setEnabled(true);
					jJTaskBean.saveJJTask(task, true, new MutableInt(0));
				}

				testcaseList = req.getTestcases();
				for (JJTestcase testcase : testcaseList) {
					testcase.setEnabled(true);
					testcaseBean.updateJJTestcase(testcase);
				}
			}

		}
		elements = null;
		subElements = null;

		testcases = null;
		subTestcases = null;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public static SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			JJProject project, JJCategory category, boolean onlyActif,
			JJChapterService jJChapterService,
			JJRequirementService jJRequirementService) {

		long t = System.currentTimeMillis();
		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getChildrenOfParentChapter(parent, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering() != null
						? chapter.getOrdering()
						: 0, chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(
							LoginBean.getCompany(), parent,
							LoginBean.getProduct(), LoginBean.getVersion(),
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering() != null
						? requirement.getOrdering()
						: 0, requirement);

			}
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					LoginBean.getCompany(), project, category, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering() != null
						? chapter.getOrdering()
						: 0, chapter);
			}
		}

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return elements;
	}

	private SortedMap<Integer, JJTestcase> getSortedTestcases(
			JJRequirement requirement, JJChapter chapter) {

		long t = System.currentTimeMillis();
		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(requirement,
				chapter, null, null, false, true, false);

		for (JJTestcase testcase : testcases) {

			elements.put(
					testcase.getOrdering() != null ? testcase.getOrdering() : 0,
					testcase);

		}

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return elements;
	}

	// public void copyTestcase() {
	// long t = System.currentTimeMillis();
	// boolean copyAll = true;
	// for (ImportFormat importFormat : importFormats) {
	//
	// if (!importFormat.getCopyTestcase()) {
	// copyAll = false;
	// break;
	// }
	//
	// }
	//
	// logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	// }
	//
	//
	// public void copyChapter() {
	// long t = System.currentTimeMillis();
	// boolean copyAll = true;
	// for (ImportFormat importFormat : importFormats) {
	// if (!importFormat.getCopyChapter()) {
	// copyAll = false;
	// break;
	// }
	// }
	// copyChapters = copyAll;
	// logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	// }

	// public void copyRequirementsListener() {
	// long t = System.currentTimeMillis();
	//
	// if (copyRequirements == oldCopyRequirements)
	// copyRequirements = !copyRequirements;
	//
	// for (ImportFormat importFormat : importFormats) {
	// importFormat.setCopyRequirement(copyRequirements);
	// }
	//
	// oldCopyRequirements = copyRequirements;
	// disableImportButton = !copyRequirements;
	// logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	// }

	// public void copyRequirementListener() {
	// long t = System.currentTimeMillis();
	// boolean copyAll = true;
	// for (ImportFormat importFormat : importFormats) {
	// if (!importFormat.getCopyRequirement()) {
	// copyAll = false;
	// break;
	// }
	// }
	//
	// copyRequirements = copyAll;
	// oldCopyRequirements = copyAll;
	//
	// for (ImportFormat importFormat : importFormats) {
	// if (importFormat.getCopyRequirement()) {
	// disableImportButton = false;
	// break;
	// } else {
	// disableImportButton = true;
	// }
	//
	// }
	// logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	// }

	public void onRowDblClckSelect(SelectEvent event) {

		if (event.getObject() != null
				&& event.getObject() instanceof JJRequirement) {
			requirement = ((JJRequirement) event.getObject());
			editRequirement();
		}

	}
	public void handleImportCSV(FileUploadEvent event) {

		try {
			List<JJRequirement> requirements = ReadXMLFile
					.getRequirementsFromCSV(event.getFile().getInputstream(),
							LoginBean.getProject(), LoginBean.getProduct(),
							LoginBean.getVersion(),
							jJStatusService.getOneStatus("NEW", "Requirement",
									true),
							jJCategoryService.getCategory("FUNCTIONAL",
									LoginBean.getCompany(), true));

			if (requirements.isEmpty()) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("Pas d'Exigence trouvé dans ce Fichier",
								FacesMessage.SEVERITY_WARN,
								MessageFactory
										.getMessage("label_requirement", "")
										.getDetail());
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);
			} else {
				for (JJRequirement r : requirements) {
					saveJJRequirement(r);
					updateDataTable(r, r.getCategory(), ADD_OPERATION);
				}

				FacesMessage facesMessage = MessageFactory.getMessage(
						requirements.size() + " Exigences Ajoutées avec succés",
						FacesMessage.SEVERITY_INFO,
						MessageFactory.getMessage("label_requirement", "")
								.getDetail());
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);

			}
		} catch (IOException e) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					"Erreur Fichier", FacesMessage.SEVERITY_WARN, MessageFactory
							.getMessage("label_requirement", "").getDetail());
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void handleImportXML(FileUploadEvent event) throws IOException {

		try {
			List<JJRequirement> requirements = ReadXMLFile
					.getRequirementsFromXml(event.getFile().getInputstream(),
							jJCategoryService, jJRequirementService,
							jJChapterService, LoginBean.getProject(), null,
							LoginBean.getProduct(), LoginBean.getVersion(),
							jJStatusService.getOneStatus("NEW", "Requirement",
									true));
			if (requirements.isEmpty()) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("Pas d'Exigence trouvé dans ce Fichier",
								FacesMessage.SEVERITY_WARN,
								MessageFactory
										.getMessage("label_requirement", "")
										.getDetail());
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);
			} else {
				for (JJRequirement r : requirements) {
					saveJJRequirement(r);
					updateDataTable(r, r.getCategory(), ADD_OPERATION);
				}

				FacesMessage facesMessage = MessageFactory.getMessage(
						requirements.size() + " Exigences Ajoutées avec succés",
						FacesMessage.SEVERITY_INFO,
						MessageFactory.getMessage("label_requirement", "")
								.getDetail());
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);

			}
		} catch (SAXParseException e) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					"Erreur Fichier", FacesMessage.SEVERITY_WARN, MessageFactory
							.getMessage("label_requirement", "").getDetail());
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void viewLinks() {

		if (viewLinkRequirement != null && (filterButton == null || filterButton
				.equalsIgnoreCase("specification_filter_button"))) {
			filterButton = "specification_showAll_button";
			viewLinkRequirement = jJRequirementService
					.findJJRequirement(viewLinkRequirement.getId());

		} else {

			filterButton = null;
			viewLinkRequirement = null;

		}

		filterTable();

	}

	public class ImportFormat {

		private String id;
		private JJRequirement requirement;
		private boolean copyTestcase;
		private boolean copyChapter;

		public ImportFormat() {
			super();
		}

		public ImportFormat(String id, JJRequirement requirement) {

			super();
			long t = System.currentTimeMillis();
			this.id = id;
			this.requirement = requirement;
			this.copyTestcase = false;
			this.copyChapter = false;
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public JJRequirement getRequirement() {
			return requirement;
		}

		public void setRequirement(JJRequirement requirement) {
			this.requirement = requirement;
		}

		public boolean getCopyTestcase() {
			return copyTestcase;
		}

		public void setCopyTestcase(boolean copyTestcase) {
			this.copyTestcase = copyTestcase;
		}

		public boolean getCopyChapter() {
			return copyChapter;
		}

		public void setCopyChapter(boolean copyChapter) {
			this.copyChapter = copyChapter;
		}

	}

	public List<JJRequirement> getRequirements(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJChapter chapter, boolean withChapter,
			boolean onlyActif, boolean orderByCreationdate) {
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		return jJRequirementService.getRequirements(LoginBean.getCompany(),
				category,
				loginBean.getAuthorizedMap("Requirement", project, product),
				version, status, chapter, false, withChapter, onlyActif,
				orderByCreationdate, false, null);
	}

	public List<JJRequirement> getRequirements(JJProject project,
			JJProduct product, JJVersion version) {
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		return jJRequirementService.getRequirements(LoginBean.getCompany(),
				loginBean.getAuthorizedMap("Requirement", project, product),
				version, null);
	}

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif) {

		return jJRequirementService
				.getRequirementChildrenWithChapterSortedByOrder(
						LoginBean.getCompany(), chapter, LoginBean.getProduct(),
						LoginBean.getVersion(), onlyActif);
	}

	public void reset() {
		long t = System.currentTimeMillis();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJStatusBean jJStatusBean = (JJStatusBean) session
				.getAttribute("jJStatusBean");

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		if (jJTestcaseBean != null)
			jJTestcaseBean.setProject(null);
		if (jJStatusBean != null) {
			jJStatusBean.setStatusPieChart(null);
			jJStatusBean.setStates(null);
			jJStatusBean.setTaskStatues(null);
			jJStatusBean.setContacts(null);
			jJStatusBean.setCategoryPieChart(null);
			jJStatusBean.setProductPieChart(null);
			jJStatusBean.setProjectPieChart(null);
		}

		setJJRequirement_(null);
		setSelectedTasks(null);
		setSelectedRequirementLinkDown(null);
		setSelectedRequirementLinkUp(null);
		setSelectedTestcases(null);
		setCreateDialogVisible(false);
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private boolean getRequirementDialogConfiguration() {

		Boolean val = jJConfigurationService.getDialogConfig(
				"RequirementDialog", "specs.requirement.create.saveandclose");
		if (val == null) {
			JJConfiguration configuration = new JJConfiguration();
			configuration.setName("RequirementDialog");
			configuration.setDescription(
					"specify action after submit in specs dialog");
			configuration.setCreatedBy(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
			configuration.setCreationDate(new Date());
			configuration.setEnabled(true);
			configuration.setParam("specs.requirement.create.saveandclose");
			configuration.setVal("true");
			jJConfigurationService.saveJJConfiguration(configuration);

			val = jJConfigurationService.getDialogConfig("RequirementDialog",
					"specs.requirement.create.saveandclose");
		}
		return val;
	}

	public void replaceInDataModelList(JJRequirement req) {
		boolean repl = false;
		int i = 0;

		while (i < tableDataModelList.size() && !repl) {

			repl = tableDataModelList.get(i).getCategoryId() == req
					.getCategory().getId();
			if (!repl)
				i++;
		}

		List<JJRequirement> requirements = getRequirementsList(
				req.getCategory(), product, version, project, true);

		CategoryDataModel categoryDataModel = new CategoryDataModel(
				getListOfRequiremntUtils(requirements),
				req.getCategory().getId(), req.getCategory().getName(), true,
				jJRequirementService, jJCategoryService, jJChapterService,
				jJTaskService);

		tableDataModelList.set(i, categoryDataModel);

	}

	public boolean listContain(JJRequirement r, List<JJRequirement> list) {
		boolean exist = false;
		int i = 0;
		while (i < list.size() && !exist) {

			exist = list.get(i).equals(r);
			i++;
		}
		return exist;

	}

	// mindMap part

	public MindmapNode getReqRoot() {
		return reqRoot;
	}

	public void setReqRoot(MindmapNode reqRoot) {
		this.reqRoot = reqRoot;
	}

	public void initReqMindMap() {

		this.requirement = jJRequirementService
				.findJJRequirement(requirement.getId());
		JJVersion version = null;
		JJProduct product = null;
		version = LoginBean.getVersion();
		product = LoginBean.getProduct();

		this.reqRoot = new DefaultMindmapNode(
				requirement.getCategory().getName().toUpperCase() + " : "
						+ requirement.getName(),
				requirement, "FFCC00", true);
		for (JJRequirement r : requirement.getRequirementLinkDown()) {

			boolean add = true;
			if (product != null) {
				add = product.equals(r.getProduct());
				if (add && version != null)
					add = version.equals(r.getVersioning());
			}
			if (add) {
				MindmapNode linkDownNode = new DefaultMindmapNode(
						r.getCategory().getName().toUpperCase() + " : "
								+ r.getName(),
						r, "#00A8E8", true);
				System.out.println("linkDownNode " + linkDownNode.getLabel());
				if (r.getEnabled())
					reqRoot.addNode(linkDownNode);
			}

		}

		for (JJRequirement r : requirement.getRequirementLinkUp()) {

			boolean add = true;
			if (product != null) {
				add = product.equals(r.getProduct());
				if (add && version != null)
					add = version.equals(r.getVersioning());
			}
			if (add) {
				MindmapNode linkUpNode = new DefaultMindmapNode(
						r.getCategory().getName().toUpperCase() + " : "
								+ r.getName(),
						r, "#6e9ebf", true);
				System.out.println("linkUpNode " + linkUpNode.getLabel());
				if (r.getEnabled())
					reqRoot.addNode(linkUpNode);
			}

		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('requirementMindMap').show()");

	}

	public void onNodeDblselect(SelectEvent event) {
		this.requirement = (JJRequirement) ((MindmapNode) event.getObject())
				.getData();
		this.requirement = jJRequirementService
				.findJJRequirement(requirement.getId());

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('requirementMindMap').hide()");
		editRequirement();
		context.execute("PF('requirementDialogWidget').show()");
	}

	public void onNodeSelect(SelectEvent event) {

		this.requirement = (JJRequirement) ((MindmapNode) event.getObject())
				.getData();
		this.requirement = jJRequirementService
				.findJJRequirement(requirement.getId());
		MindmapNode node = (MindmapNode) event.getObject();

		if (node.getChildren().isEmpty()) {

			for (JJRequirement r : requirement.getRequirementLinkDown()) {
				MindmapNode linkDownNode = new DefaultMindmapNode(
						r.getCategory().getName().toUpperCase() + " : "
								+ r.getName(),
						r, "#00A8E8", true);
				System.out.println("linkDownNode " + linkDownNode.getLabel());
				if (r.getEnabled())
					node.addNode(linkDownNode);

			}

			for (JJRequirement r : requirement.getRequirementLinkUp()) {
				MindmapNode linkUpNode = new DefaultMindmapNode(
						r.getCategory().getName().toUpperCase() + " : "
								+ r.getName(),
						r, "#6e9ebf", true);
				System.out.println("linkUpNode " + linkUpNode.getLabel());
				if (r.getEnabled())
					node.addNode(linkUpNode);

			}

		}
	}

	public void calculateCovCom(long id) {
		int i = 0;
		int j = -1;
		while (i < tableDataModelList.size()) {
			if (tableDataModelList.get(i).getCategoryId() == id) {
				j = i;
				i = tableDataModelList.size();
			}
			i++;
		}

		if (j != -1) {
			tableDataModelList.get(j).setActiveIndex(0);
			if (tableDataModelList.get(j).getCoverageProgress() == -1) {
				tableDataModelList.get(j).calculCompletionProgress();
				tableDataModelList.get(j).calculCoverageProgress();
			}

		}

	}

	public void onTabClose(long id) {
		int i = 0;
		int j = -1;
		while (i < tableDataModelList.size()) {
			if (tableDataModelList.get(i).getCategoryId() == id) {
				j = i;
				i = tableDataModelList.size();
			}
			i++;
		}

		if (j != -1) {
			tableDataModelList.get(j).setActiveIndex(-1);
		}
	}

	public void reloadPage() {

		mine = false;
		filterValue = null;
		filterButton = null;
		viewLinkRequirement = null;
		tableDataModelList = null;
		loadData();
		closeDialog(false, true);
	}

	public boolean checkIfFinished(JJRequirement req) {

		long t = System.currentTimeMillis();
		boolean FINIS = true;

		FINIS = jJRequirementService.haveLinkUp(req) || jJCategoryService
				.isHighLevel(req.getCategory(), LoginBean.getCompany());

		if (FINIS)
			FINIS = jJRequirementService.haveLinkDown(req) || jJCategoryService
					.isLowLevel(req.getCategory(), LoginBean.getCompany());

		if (FINIS) {
			List<JJTask> tasks = jJTaskService.getTasks(null, null, null, null,
					null, null, null, false, req, null, null, null, true, false,
					false, null);
			FINIS = !tasks.isEmpty();

			for (JJTask task : tasks) {
				if (!(task.getEndDateReal() != null
						|| (task.getStatus() != null && task.getStatus()
								.getName().equalsIgnoreCase("DONE")))) {
					FINIS = false;
					break;
				}
			}

			if (FINIS) {
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(req,
						null, null, null, true, false, false);

				for (JJTestcase testcase : testcases) {

					List<JJTestcaseexecution> testcaseExecutions = jJTestcaseexecutionService
							.getTestcaseexecutions(testcase, null, true, true,
									false);

					if (testcaseExecutions.isEmpty()) {
						FINIS = false;
						break;
					} else if ((testcaseExecutions.get(0).getPassed() == null)
							|| (testcaseExecutions.get(0).getPassed() != null
									&& !testcaseExecutions.get(0)
											.getPassed())) {
						FINIS = false;
						break;

					}

				}
			}
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return FINIS;

	}

	public static JJRequirement getRowState(JJRequirement req,
			JJRequirementService jJRequirementService) {
		if (req.getState() == null) {
			long t = System.currentTimeMillis();
			if (req.getId() != null)
				req = jJRequirementService.findJJRequirement(req.getId());
			req.setState(jJRequirementService.getRequirementState(req,
					LoginBean.getCompany()));
			req = jJRequirementService.updateJJRequirement(req);
			logger.error("getRowState =" + (System.currentTimeMillis() - t));
		}
		return req;
	}

	public static void updateRowState(JJRequirement req,
			JJRequirementService jJRequirementService, Object object) {
		if (req != null) {
			long t = System.currentTimeMillis();
			req.setState(null);
			req = jJRequirementService.updateJJRequirement(req);

			if (((JJRequirementBean) LoginBean
					.findBean("jJRequirementBean")) != null && req != null) {
				((JJRequirementBean) LoginBean.findBean("jJRequirementBean"))
						.updateDataTable(req, req.getCategory(),
								JJRequirementBean.UPDATE_OPERATION);
			}

			if (object instanceof JJTask || object instanceof JJRequirement) {

				List<JJRequirement> listReq = new ArrayList<JJRequirement>(
						req.getRequirementLinkUp());

				for (JJRequirement rr : listReq) {
					if (rr.getEnabled() && rr.getState() != null) {

						boolean updateLinkUp = false;
						updateLinkUp = rr.getState().getName()
								.equalsIgnoreCase(jJRequirement_UnLinked);
						if (updateLinkUp && !rr.equals(object))
							updateRowState(rr, jJRequirementService, req);
					}
				}

				listReq = new ArrayList<JJRequirement>(
						req.getRequirementLinkDown());
				for (JJRequirement rr : listReq) {
					if (rr.getEnabled() && rr.getState() != null
							&& !rr.equals(object)) {
						updateRowState(rr, jJRequirementService, req);
					}
				}

			}
			JJTestcaseBean.resetTestcaseBean();
			logger.error("updateRowState =" + (System.currentTimeMillis() - t));
		}
	}

	public void saveJJRequirement(JJRequirement b) {
		((LoginBean) LoginBean.findBean("loginBean")).setNoCouvretReq(null);
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJRequirementService.saveJJRequirement(b);
		b = jJRequirementService.findJJRequirement(b.getId());
		updateRowState(b, jJRequirementService, b);
	}

	public JJRequirement updateJJRequirement(JJRequirement b) {
		((LoginBean) LoginBean.findBean("loginBean")).setNoCouvretReq(null);
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		// b = jJRequirementService.updateJJRequirement(b);
		updateRowState(b, jJRequirementService, b);
		return jJRequirementService.findJJRequirement(b.getId());
	}

	public List<JJRequirement> getListOfRequiremntUtils(
			List<JJRequirement> requirments) {

		long t = System.currentTimeMillis();
		if (requirments != null) {
			List<JJRequirement> requirementUtils = new ArrayList<JJRequirement>();
			for (JJRequirement req : requirments) {
				requirementUtils.add(getRowState(req, jJRequirementService));

			}
			logger.error("getListOfRequiremntUtils ="
					+ (System.currentTimeMillis() - t));
			return requirementUtils;
		} else
			return null;

	}

	public void onrate(JJRequirement req) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (!contact.getRequirements().contains(req)) {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getRequirements()
					.add(jJRequirementService.findJJRequirement(req.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(RequirementBean.REQUIREMENT_SUBSCRIPTION_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getRequirements().remove(
					jJRequirementService.findJJRequirement(req.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory.getMessage(
					RequirementBean.REQUIREMENT_SUBSCRIPTION_CANCEL_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void onNodeSelect(NodeSelectEvent event) throws IOException {

		int i = -1;

		if (tableDataModelList != null) {
			int j = 0;
			while (j < tableDataModelList.size()) {
				if (tableDataModelList.get(j).isExpanded()) {
					i = j;
					j = tableDataModelList.size();

				}
				j++;
			}
		}

		if (i != -1) {
			if (event.getTreeNode().getData() instanceof JJChapter) {
				tableDataModelList.get(i).setSelectedChapter(
						(JJChapter) event.getTreeNode().getData());
				tableDataModelList.get(i).setFilterChapter(true);
			} else if (event.getTreeNode().getData() instanceof String
					&& (((String) event.getTreeNode().getData())
							.equalsIgnoreCase("withOutChapter"))) {
				tableDataModelList.get(i).setSelectedChapter(null);
				tableDataModelList.get(i).setFilterChapter(true);
			} else {
				tableDataModelList.get(i).setSelectedChapter(null);
				tableDataModelList.get(i).setFilterChapter(false);
			}
		}

	}

	public void getWarningList(JJRequirement req) {
		boolean warn = false;

		if (requirementChapterList == null
				|| requirementChapterList.isEmpty()) {
			warn = true;
			FacesMessage facesMessage = MessageFactory.getMessage(
					SPECIFICATION_ERROR_NOCATEGORYCHAPTER,
					((LoginBean) LoginBean.findBean("loginBean"))
							.checkMessage(req.getCategory()));
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} else if (req.getChapter() == null) {
			warn = true;
			FacesMessage facesMessage = MessageFactory
					.getMessage(SPECIFICATION_WARNING_NOCHAPTER, MessageFactory
							.getMessage("label_requirement", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		if (!jJRequirementService.haveLinkDown(req) && !jJCategoryService
				.isLowLevel(req.getCategory(), LoginBean.getCompany())) {
			warn = true;
			FacesMessage facesMessage = MessageFactory
					.getMessage(SPECIFICATION_WARNING_LINKDOWN, MessageFactory
							.getMessage("label_requirement", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		if (!jJRequirementService.haveLinkUp(req) && !jJCategoryService
				.isHighLevel(req.getCategory(), LoginBean.getCompany())) {
			warn = true;
			FacesMessage facesMessage = MessageFactory
					.getMessage(SPECIFICATION_WARNING_LINKUP, MessageFactory
							.getMessage("label_requirement", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance()
					.addMessage(MessageFactory
							.getMessage("label_requirement", "").getDetail(),
							facesMessage);
		}

		if (!warn) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					MESSAGE_SUCCESSFULLY_UPDATED, MessageFactory
							.getMessage("label_requirement", "").getDetail(),
					"e");
			facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance()
					.addMessage(MessageFactory
							.getMessage("label_requirement", "").getDetail(),
							facesMessage);
		}

	}

	public List<JJRequirement> getInfinshedRequirement(JJVersion jJversion) {
		List<JJRequirement> infinshedRequirement = new ArrayList<JJRequirement>();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		for (JJRequirement req : jJRequirementService.getRequirements(null,
				loginBean.getAuthorizedMap("Requirement",
						LoginBean.getProject(), LoginBean.getProduct()),
				jJversion, null)) {
			// if (!jJTaskService.haveTask(req, true, true, true)
			// && jJversion == req.getVersioning())
			// infinshedRequirement.add(req);

			if (!getRowState(req, jJRequirementService).getState().getName()
					.equalsIgnoreCase(jJRequirement_Finished)
					&& jJversion == req.getVersioning())
				infinshedRequirement.add(req);
		}

		return infinshedRequirement;
	}

	public void initDataTabels(ComponentSystemEvent e) {

		if (tableDataModelList == null)
			fullTableDataModelList();

		if (tableDataModelList != null) {

			RequestContext.getCurrentInstance().execute("updateDataTable();");
		}

	}

	// kanban View
	private List<FlowStepUtil> flowStepUtils;
	private TreeNode linkedData;

	public List<FlowStepUtil> getFlowStepUtils() {
		JJSprintBean jJSprintBean = (JJSprintBean) LoginBean
				.findBean("jJSprintBean");
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		if (jJSprintBean == null)
			jJSprintBean = new JJSprintBean();
		if (flowStepUtils == null && loginBean.getPlanningConfiguration()
				.getKanban_Tab().equals(jJSprintBean.getActiveTabGantIndex()))
			flowStepUtils = FlowStepUtil.getFlowStepUtils(jJRequirementService,
					jJFlowStepService);
		return flowStepUtils;
	}

	public void setFlowStepUtils(List<FlowStepUtil> flowStepUtils) {
		this.flowStepUtils = flowStepUtils;
	}

	public TreeNode getLinkedData() {
		return linkedData;
	}

	public void setLinkedData(TreeNode linkedData) {
		this.linkedData = linkedData;
	}

	public void loadLinkedData(JJRequirement r, TreeNode principal) {

		r = jJRequirementService.findJJRequirement(r.getId());
		TreeNode father = new DefaultTreeNode("JJRequirement", getRowState(r, jJRequirementService), principal);

		for (JJRequirement req : r.getRequirementLinkUp()) {
			if (req.getEnabled())
				loadLinkedData(req, father);
		}

		for (JJTestcase test : jJTestcaseService.getJJtestCases(r))
			new DefaultTreeNode("JJTestcase", test, father);

		for (JJBug bug : jJBugService.getRequirementBugs(r,
				LoginBean.getCompany(), LoginBean.getProject(),
				LoginBean.getProduct(), LoginBean.getVersion()))
			new DefaultTreeNode("JJBug", bug, father);

		for (JJTask task_ : jJTaskService.getImportTasks(null, r, null, true))
			new DefaultTreeNode("JJTask", task_, father);

	}

	public void loadLinkedData(JJRequirement req) {

		linkedData = new DefaultTreeNode("Root", null);
		req = jJRequirementService.findJJRequirement(req.getId());
		TreeNode principal = new DefaultTreeNode("JJRequirement", getRowState(req, jJRequirementService),
				linkedData);
		for (JJRequirement r : req.getRequirementLinkUp()) {
			if (r.getEnabled())
				loadLinkedData(r, principal);
		}

		for (JJTestcase test : jJTestcaseService.getJJtestCases(req))
			new DefaultTreeNode("JJTestcase", test, principal);

		for (JJBug bug : jJBugService.getRequirementBugs(req,
				LoginBean.getCompany(), LoginBean.getProject(),
				LoginBean.getProduct(), LoginBean.getVersion()))
			new DefaultTreeNode("JJBug", bug, principal);

		for (JJTask task_ : jJTaskService.getImportTasks(null, req, null, true))
			new DefaultTreeNode("JJTask", task_, principal);

		principal.setExpanded(true);

	}

	public void addReqToPreviousFlowStep(DragDropEvent ddevent) {
		try {
			String id = ddevent.getDragId()
					.split(":")[ddevent.getDragId().split(":").length - 1];

			String flowStepsUtilIndex = id.substring(
					id.indexOf("dragReq_") + "dragReq_".length(), id.indexOf(
							"_", id.indexOf("dragReq_") + "dragReq_".length()));

			String reqIndex = id.replace("dragReq_" + flowStepsUtilIndex + "_",
					"");

			System.err.println(id + "  " + flowStepsUtilIndex + " " + reqIndex);

			int i = Integer.parseInt(flowStepsUtilIndex);
			int j = Integer.parseInt(reqIndex);

			JJRequirement dropedReq = flowStepUtils.get(i).getRequirements()
					.get(j);

			System.err.println(dropedReq.getName());

			dropedReq.setFlowStep(flowStepUtils.get(i).getPreviousFlowStep());

			((LoginBean) LoginBean.findBean("loginBean")).setNoCouvretReq(null);
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			dropedReq.setUpdatedBy(contact);
			dropedReq.setUpdatedDate(new Date());

			dropedReq = jJRequirementService.updateJJRequirement(dropedReq);
			updateDataTable(dropedReq, dropedReq.getCategory(),
					UPDATE_OPERATION);

			flowStepUtils.get(Integer.parseInt(flowStepsUtilIndex))
					.getRequirements().remove(j);
			i = i - 1;
			if (i <= -1)
				i = flowStepUtils.size() - 1;

			flowStepUtils.get(i).getRequirements().add(dropedReq);

			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_requirement", "")
							.getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} catch (Exception e) {

			String message = "message_unsuccessfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					FacesMessage.SEVERITY_ERROR, MessageFactory
							.getMessage("label_requirement", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public String getKanbanStyleClass() {

		if (flowStepUtils != null) {
			if (flowStepUtils.size() >= 4)
				return "Container25";
			else
				return "Container20";
		} else
			return "";
	}

	public String getContainerWidth() {
		if (flowStepUtils != null) {
			return flowStepUtils.size() * 320 + "";
		} else
			return "0";
	}

	public void addReqToNextFlowStep(DragDropEvent ddevent) {

		try {
			String id = ddevent.getDragId()
					.split(":")[ddevent.getDragId().split(":").length - 1];

			String flowStepsUtilIndex = id.substring(
					id.indexOf("dragReq_") + "dragReq_".length(), id.indexOf(
							"_", id.indexOf("dragReq_") + "dragReq_".length()));

			String reqIndex = id.replace("dragReq_" + flowStepsUtilIndex + "_",
					"");

			System.err.println(id + "  " + flowStepsUtilIndex + " " + reqIndex);

			int i = Integer.parseInt(flowStepsUtilIndex);
			int j = Integer.parseInt(reqIndex);

			JJRequirement dropedReq = flowStepUtils.get(i).getRequirements()
					.get(j);

			System.err.println(dropedReq.getName());

			dropedReq.setFlowStep(flowStepUtils.get(i).getNextFlowStep());

			((LoginBean) LoginBean.findBean("loginBean")).setNoCouvretReq(null);
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			dropedReq.setUpdatedBy(contact);
			dropedReq.setUpdatedDate(new Date());

			dropedReq = jJRequirementService.updateJJRequirement(dropedReq);
			updateDataTable(dropedReq, dropedReq.getCategory(),
					UPDATE_OPERATION);

			flowStepUtils.get(Integer.parseInt(flowStepsUtilIndex))
					.getRequirements().remove(j);
			i = i + 1;
			if (i >= flowStepUtils.size())
				i = 0;

			flowStepUtils.get(i).getRequirements().add(dropedReq);

			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_requirement", "")
							.getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} catch (Exception e) {

			String message = "message_unsuccessfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					FacesMessage.SEVERITY_ERROR, MessageFactory
							.getMessage("label_requirement", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void closeLinkDialog() {
		linkedData = null;
	}

}
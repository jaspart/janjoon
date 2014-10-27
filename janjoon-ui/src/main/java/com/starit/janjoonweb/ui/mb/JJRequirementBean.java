package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.RequirementUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean {

	static Logger logger = Logger.getLogger(JJRequirementBean.class);

	@Autowired
	public JJConfigurationService jJConfigurationService;

	private JJTaskBean jJTaskBean;

	public void setjJTaskBean(JJTaskBean jJTaskBean) {
		this.jJTaskBean = jJTaskBean;
	}

	@Autowired
	public JJPermissionService jJPermissionService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@Autowired
	JJTestcaseService jJTestcaseService;

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	@Autowired
	JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	@Autowired
	JJTeststepService jJTeststepService;

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	private JJCategory lowCategory;
	private JJCategory mediumCategory;
	private JJCategory highCategory;
	private JJCategory requirementCategory;

	private List<JJCategory> categoryList;

	private List<CategoryDataModel> tableDataModelList;

	private JJRequirement requirement;

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
	private List<JJRequirement> noCouvretReq;

	private JJStatus requirementStatus;

	private List<JJStatus> requirementStatusList;

	private String templateHeader;

	private String message;
	private String lowCategoryName;
	private String mediumCategoryName;
	private String highCategoryName;

	private List<JJRequirement> lowRequirementsList;
	private List<JJRequirement> mediumRequirementsList;
	private List<JJRequirement> highRequirementsList;
	private List<JJRequirement> selectedLowRequirementsList;
	private List<JJRequirement> selectedMediumRequirementsList;
	private List<JJRequirement> selectedHighRequirementsList;

	private boolean disabledLowRequirements;
	private boolean haveSpecPermission;
	private boolean disabledMediumRequirements;
	private boolean disabledHighRequirements;
	private boolean disabledVersion;
	private boolean disabledStatus;
	private boolean disabledTask;
	private boolean disabledProject;

	private boolean changeLow;
	private boolean changeMedium;
	private boolean changeHigh;

	private boolean initiateTask;
	private boolean disabledInitTask;

	private JJTask task;

	private List<JJRequirement> storeMapUp;
	private List<JJRequirement> storeMapDown;
	private List<String> namesList;

	private long categoryId;

	private boolean requirementState;

	public List<JJRequirement> getNoCouvretReq() {
		return noCouvretReq;
	}

	public void setNoCouvretReq(List<JJRequirement> noCouvretReq) {
		this.noCouvretReq = noCouvretReq;
	}

	public boolean isHaveSpecPermission() {
		return haveSpecPermission;
	}

	public void setHaveSpecPermission(boolean haveSpecPermission) {
		this.haveSpecPermission = haveSpecPermission;
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

	public List<JJCategory> getCategoryList() {
		categoryList = jJCategoryService.getCategories(null, false, true, true);
		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public List<CategoryDataModel> getTableDataModelList() {
		return tableDataModelList;
	}

	public void setTableDataModelList(List<CategoryDataModel> tableDataModelList) {
		this.tableDataModelList = tableDataModelList;
	}

	public String getTableDataModelSizePct() {
		int nbOpenedTables = 0;
		for (int i = 0; i < tableDataModelList.size(); i++) {
			if (tableDataModelList.get(i).getCategoryId() != 0) {
				nbOpenedTables++;
			}
		}
		double tableDataModelSizePct = 0;
		if (nbOpenedTables > 0) {
			tableDataModelSizePct = 100 / nbOpenedTables;
		}
		return tableDataModelSizePct + "%";
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
		requirementProjectList = jJProjectService.getProjects(true);
		return requirementProjectList;
	}

	public void setRequirementProjectList(List<JJProject> requirementProjectList) {
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
		requirementProductList = jJProductService.getProducts(true);
		return requirementProductList;
	}

	public void setRequirementProductList(List<JJProduct> requirementProductList) {
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
				requirementProduct);
		return requirementVersionList;
	}

	public void setRequirementVersionList(List<JJVersion> requirementVersionList) {
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
					requirementProject, requirementCategory, true,
					new ArrayList<String>());
			return requirementChapterList;
		} else
			return null;

	}

	public void setRequirementChapterList(List<JJChapter> requirementChapterList) {
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

	public void setLowRequirementsList(List<JJRequirement> lowRequirementsList) {
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

	public void setHighRequirementsList(List<JJRequirement> highRequirementsList) {
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

	public void setDisabledMediumRequirements(boolean disabledMediumRequirements) {
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

	public JJProject getImportProject() {
		return importProject;
	}

	public void setImportProject(JJProject importProject) {
		this.importProject = importProject;
	}

	public List<JJProject> getImportProjectList() {
		importProjectList = jJProjectService.getProjects(true);
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

		importProductList = jJProductService.getProducts(true);
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
				importProduct);

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
				true);

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

		importStatusList = jJStatusService.getStatus("JJRequirement", true,
				names, true);

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

		requirement = new JJRequirement();
		requirement.setCreationDate(new Date());
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

		requirementStatusList = jJStatusService.getStatus("JJRequirement",
				true, null, true);

		requirementStatus = jJStatusService.getOneStatus("NEW",
				"JJRequirement", true);

		requirement.setStatus(requirementStatus);

		disabledStatus = true;

		fullRequirementsList();

		selectedLowRequirementsList = new ArrayList<JJRequirement>();
		selectedMediumRequirementsList = new ArrayList<JJRequirement>();
		selectedHighRequirementsList = new ArrayList<JJRequirement>();

		task = new JJTask();
		disabledInitTask = false;
		initiateTask = false;
		disabledTask = true;

		requirement.setNumero(0);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		requirement.setCreatedBy(contact);

		changeLow = false;
		changeMedium = false;
		changeHigh = false;

		requirementState = true;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void editRequirement() {
		long t = System.currentTimeMillis();
		message = "specification_edit_header";

		requirement = jJRequirementService.findJJRequirement(requirement
				.getId());
		requirementCategory = requirement.getCategory();
		requirement.setUpdatedDate(new Date());

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

		requirementStatusList = jJStatusService.getStatus("JJRequirement",
				true, namesList, true);

		if (requirement.getStatus().getName().equalsIgnoreCase("NEW")) {
			requirementStatus = jJStatusService.getOneStatus("MODIFIED",
					"JJRequirement", true);
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

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");

		int numero = requirement.getNumero() + 1;
		requirement.setNumero(numero);
		requirement.setUpdatedBy(contact);
		requirement.setUpdatedDate(new Date());

		changeLow = false;
		changeMedium = false;
		changeHigh = false;

		requirementState = false;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private void deleteTasksAndTestcase(JJRequirement requirement) {

		if (jJTaskBean == null) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		}

		long t = System.currentTimeMillis();
		JJRequirement req = jJRequirementService.findJJRequirement(requirement
				.getId());
		for (JJTestcase testcase : req.getTestcases()) {

			JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());
			for (JJTask task : tc.getTasks()) {
				task.setEnabled(false);
				jJTaskBean.saveJJTask(task,true);
			}
			tc.setEnabled(false);
			jJTestcaseService.updateJJTestcase(tc);
		}

		for (JJTask task : req.getTasks()) {
			task.setEnabled(false);
			jJTaskBean.saveJJTask(task,true);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void deleteRequirement() {
		long t = System.currentTimeMillis();
		requirement = jJRequirementService.findJJRequirement(requirement
				.getId());
		requirement.setEnabled(false);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");

		int numero = requirement.getNumero() + 1;
		requirement.setNumero(numero);
		requirement.setUpdatedBy(contact);
		requirement.setUpdatedDate(new Date());
		requirementStatus = jJStatusService.getOneStatus("DELETED",
				"JJRequirement", true);
		requirement.setStatus(requirementStatus);

		jJRequirementService.updateJJRequirement(requirement);

		deleteTasksAndTestcase(requirement);

		closeDialog(false);

		reset();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void preReleaseRequirement(long id) {
		categoryId = id;
	}

	@SuppressWarnings("unchecked")
	public void releaseRequirement() {
		long t = System.currentTimeMillis();
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		for (CategoryDataModel categoryDataModel : tableDataModelList) {
			if (categoryDataModel.getCategoryId() == categoryId) {

				for (RequirementUtil r : (List<RequirementUtil>) categoryDataModel
						.getWrappedData()) {
					list.add(r.getRequirement());
				}
				break;
			}

		}

		JJStatus status = jJStatusService.getOneStatus("RELEASED",
				"JJRequirement", true);

		for (JJRequirement req : list) {
			req = jJRequirementService.findJJRequirement(req.getId());
			if (req.getStatus() != status) {
				req.setStatus(status);

				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext
						.getExternalContext().getSession(false);
				JJContact contact = (JJContact) session
						.getAttribute("JJContact");

				int numero = req.getNumero() + 1;
				req.setNumero(numero);
				req.setUpdatedBy(contact);

				jJRequirementService.updateJJRequirement(req);

			}
		}
		reset();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void save() {
		long t = System.currentTimeMillis();
		requirement.setProduct(requirementProduct);
		requirement.setVersioning(requirementVersion);
		requirement.setStatus(requirementStatus);

		getRequirementsListUP();

		getRequirementOrder();

		if (!requirementCategory.getId().equals(lowCategory.getId())) {
			getRequirementsListDOWN();
		}

		if (requirement.getId() == null) {

			requirement.setProject(requirementProject);

			// if (initiateTask) {
			// task.setName(requirement.getName());
			// task.setDescription("This Task: " + task.getName());
			// task.setEndDatePlanned(new Date(task.getStartDatePlanned()
			// .getTime() + task.getWorkloadPlanned() * 60 * 60 * 1000));
			//
			// requirement.getTasks().add(task);
			// task.setRequirement(requirement);
			// jJRequirementService.saveJJRequirement(requirement);
			// jJTaskService.saveJJTask(task);
			// } else {
			// jJRequirementService.saveJJRequirement(requirement);
			// }

			jJRequirementService.saveJJRequirement(requirement);

		} else {

			// JJRequirement req = jJRequirementService
			// .findJJRequirement(requirement.getId());
			if (!requirement.getEnabled()) {
				deleteTasksAndTestcase(requirement);
			}

		}

		RequestContext context = RequestContext.getCurrentInstance();

		if (requirementState) {

			// addRowToCategoryDataModel();

			if (getRequirementDialogConfiguration()) {
				context.execute("requirementDialogWidget.hide()");
			} else {
				newRequirement(requirementCategory.getId());
			}

		} else {

			// System.out.println("GDGDFG");
			//
			//
			//
			// System.out.println("FEFEFE");

			context.execute("requirementDialogWidget.hide()");
			// closeDialog();

		}

		reset();
		System.out.println("replaceInDataModelList");
		replaceInDataModelList(requirement);
		System.out.println("replaceInDataModelList2");
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		redirectPage();

	}

	public void importRequirement() {
		long t = System.currentTimeMillis();
		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Integer> chapters = new TreeMap<Integer, Integer>();

		for (ImportFormat format : importFormats) {

			if (format.getCopyRequirement()) {

				JJStatus status = jJStatusService.getOneStatus("NEW",
						"JJRequirement", true);

				JJRequirement req = format.getRequirement();

				JJRequirement requirement = jJRequirementService
						.findJJRequirement(req.getId());

				JJRequirement importRequirement = new JJRequirement();

				importRequirement.setName(requirement.getName() + "(i)");
				importRequirement.setDescription(requirement.getDescription());
				importRequirement
						.setCreationDate(requirement.getCreationDate());
				importRequirement.setCreatedBy(requirement.getCreatedBy());

				importRequirement.setUpdatedDate(new Date());

				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext
						.getExternalContext().getSession(false);
				JJContact contact = (JJContact) session
						.getAttribute("JJContact");

				importRequirement.setNumero(requirement.getNumero());
				importRequirement.setUpdatedBy(contact);

				importRequirement.setProject(project);
				importRequirement.setProduct(importProduct);
				importRequirement.setVersioning(importVersion);
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

						if (chapters.containsKey(Integer.valueOf(chapter
								.getId().intValue()))) {

							long id = chapters
									.get(Integer.valueOf(chapter.getId()
											.intValue())).longValue();

							importChapter = jJChapterService.findJJChapter(id);

						} else {

							importChapter = new JJChapter();
							importChapter.setName(chapter.getName() + "(i)");
							importChapter.setDescription(chapter
									.getDescription());
							importChapter.setCreationDate(chapter
									.getCreationDate());
							importChapter.setEnabled(true);
							importChapter.setCategory(chapter.getCategory());
							importChapter.setProject(project);
							importChapter.setUpdatedDate(new Date());
							importChapter.setParent(null);

							elements = getSortedElements(null,
									requirement.getProject(),
									requirement.getCategory(), false);
							if (elements.isEmpty()) {
								importChapter.setOrdering(0);
							} else {
								importChapter
										.setOrdering(elements.lastKey() + 1);
							}

							jJChapterService.saveJJChapter(importChapter);

							chapters.put(Integer.valueOf(chapter.getId()
									.intValue()), Integer.valueOf(importChapter
									.getId().intValue()));

						}

						importRequirement.setChapter(importChapter);
						elements = getSortedElements(importChapter,
								requirement.getProject(),
								requirement.getCategory(), false);
						if (elements.isEmpty()) {
							importRequirement.setOrdering(0);
						} else {
							importRequirement
									.setOrdering(elements.lastKey() + 1);
						}

					} else {
						importRequirement.setChapter(chapter);
						elements = getSortedElements(chapter,
								requirement.getProject(),
								requirement.getCategory(), false);
						if (elements.isEmpty()) {
							importRequirement.setOrdering(0);
						} else {
							importRequirement
									.setOrdering(elements.lastKey() + 1);
						}
					}
				}

				jJRequirementService.saveJJRequirement(importRequirement);
				reset();

				if (format.getCopyTestcase()) {

					Set<JJTestcase> testcases = requirement.getTestcases();

					if (testcases != null && !testcases.isEmpty()) {

						for (JJTestcase tc : testcases) {

							JJRequirement req1 = jJRequirementService
									.findJJRequirement(importRequirement
											.getId());

							JJTestcase importTestcase = new JJTestcase();

							JJTestcase testcase = jJTestcaseService
									.findJJTestcase(tc.getId());

							SortedMap<Integer, JJTestcase> testcaseElements = manageTestcaseOrder(req1
									.getChapter());

							if (testcaseElements.isEmpty()) {

								importTestcase.setOrdering(0);
							} else {
								importTestcase.setOrdering(testcaseElements
										.lastKey() + 1);
							}

							importTestcase.setRequirement(req1);
							req1.getTestcases().add(importTestcase);

							importTestcase.setName(testcase.getName() + " (i)");
							importTestcase.setDescription(testcase
									.getDescription());
							importTestcase.setCreationDate(testcase
									.getCreationDate());
							importTestcase.setUpdatedDate(new Date());
							importTestcase
									.setAutomatic(testcase.getAutomatic());
							importTestcase.setEnabled(true);

							jJTestcaseService.saveJJTestcase(importTestcase);

							Set<JJTeststep> teststeps = testcase.getTeststeps();

							for (JJTeststep teststep : teststeps) {

								JJTestcase tc1 = jJTestcaseService
										.findJJTestcase(importTestcase.getId());

								JJTeststep importTeststep = new JJTeststep();

								importTeststep.setOrdering(teststep
										.getOrdering());

								importTeststep.setTestcase(tc1);
								tc1.getTeststeps().add(importTeststep);

								importTeststep.setName(teststep.getName()
										+ " (i)");
								importTeststep.setDescription(teststep
										.getDescription());
								importTeststep.setCreationDate(teststep
										.getCreationDate());
								importTeststep.setUpdatedDate(new Date());
								importTeststep.setActionstep(teststep
										.getActionstep() + " (i)");
								importTeststep.setResultstep(teststep
										.getResultstep() + " (i)");
								importTeststep.setEnabled(true);

								jJTeststepService
										.saveJJTeststep(importTeststep);

							}

						}

					}

				}

			}
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("requirementImportDialogWidget.hide()");
		reset();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private SortedMap<Integer, JJTestcase> manageTestcaseOrder(JJChapter chapter) {
		long t = System.currentTimeMillis();
		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(null,
				chapter, false, false, false);

		for (JJTestcase testcase : testcases) {
			elements.put(testcase.getOrdering(), testcase);
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
			task.setCreationDate(new Date());
			task.setStartDatePlanned(new Date());
			task.setWorkloadPlanned(8);
		} else {
			task = new JJTask();
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void checkCompleteTask() {

	}

	private List<ImportFormat> importFormats;

	public List<ImportFormat> getImportFormats() {
		return importFormats;
	}

	public void setImportFormats(List<ImportFormat> importFormats) {
		this.importFormats = importFormats;
	}

	public void fillTableImport() {
		long t = System.currentTimeMillis();
		importFormats = new ArrayList<ImportFormat>();

		copyTestcases = false;
		copyChapters = false;
		copyRequirements = false;
		disableImportButton = true;

		int i = 0;
		for (JJRequirement requirement : jJRequirementService.getRequirements(
				importCategory, importProject, importProduct, importVersion,
				importStatus, null, false, false, true)) {
			importFormats.add(new ImportFormat(String.valueOf(i), requirement,
					copyRequirements, copyTestcases, copyChapters));
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

	public void closeDialog(boolean save) {

		long t = System.currentTimeMillis();
		System.out.println("close dialog");

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

		storeMapUp = null;
		storeMapDown = null;

		requirementState = true;

		// loadData();
		// redirectPage();

		System.out.println("fin");

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		redirectPage();

	}

	public void closeDialogImport() {
		long t = System.currentTimeMillis();
		message = null;
		importFormats = null;
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

		loadData();
		redirectPage();

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	public boolean getDisabledEdit(JJRequirement requirement) {
		if (requirement.getStatus().getName().equalsIgnoreCase("RELEASED")) {
			return true;
		} else {
			return false;
		}
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
					|| requirementStatus.getName().equalsIgnoreCase("DELETED")) {
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
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");

		project = jJProjectBean.getProject();

		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		product = jJProductBean.getProduct();

		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		version = jJVersionBean.getVersion();

		JJChapterBean jJChapterBean = (JJChapterBean) session
				.getAttribute("jJChapterBean");
		if (jJChapterBean == null)
			jJChapterBean = new JJChapterBean();

		if (project == null) {

			warnMessage = "Select a project to export PDF";
			disabledExport = true;

			disabledRequirement = true;
			haveSpecPermission = true;
			title = "Select a project to create requirement";

			jJChapterBean.setWarnMessage("Select a project to manage document");
			jJChapterBean.setDisabledChapter(true);

		} else {

			warnMessage = "Export to PDF";
			disabledExport = false;

			disabledRequirement = false;
			haveSpecPermission = jJPermissionService.isAuthorized(
					(JJContact) session.getAttribute("JJContact"), project,
					product, "JJRequirement");
			if (!haveSpecPermission)
				title = "You Have no permisson to do this action";
			else
				title = "New Requirement";

			System.err.println(title + haveSpecPermission);

			jJChapterBean.setWarnMessage("Manage document");
			jJChapterBean.setDisabledChapter(false);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	@SuppressWarnings("unchecked")
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
				List<RequirementUtil> requirements = (List<RequirementUtil>) categoryDataModel
						.getWrappedData();
				requirements.add(0, new RequirementUtil(requirement,
						getRowStyleClass(requirement)));
			}
			// categoryDataModel.calculCompletionProgress();
			// categoryDataModel.calculCoverageProgress();
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	@SuppressWarnings("unchecked")
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
				List<RequirementUtil> requirements = (List<RequirementUtil>) categoryDataModel
						.getWrappedData();
				requirements.remove(new RequirementUtil(requirement, null));
			}
			// categoryDataModel.calculCompletionProgress();
			// categoryDataModel.calculCoverageProgress();
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void loadData() {

		// Utiliser au moment du changement des couples (product/project)
		// long t = System.currentTimeMillis();

		System.out.println("debut load");

		long t = System.currentTimeMillis();

		if (tableDataModelList == null) {

			templateHeader = "";

			fullTableDataModelList();
		}

		loadParameter();

		String[] results = templateHeader.split("-");

		for (int i = 0; i < results.length; i++) {

			CategoryDataModel categoryDataModel = tableDataModelList.get(i);
			long categoryId = categoryDataModel.getCategoryId();

			if (categoryId != 0) {
				JJCategory category = jJCategoryService
						.findJJCategory(categoryId);

				List<JJRequirement> requirements = getRequirementsList(
						category, product, version, project, true);

				categoryDataModel = new CategoryDataModel(
						getListOfRequiremntUtils(requirements),
						category.getId(), category.getName(), true);

				// categoryDataModel.calculCompletionProgress();
				// categoryDataModel.calculCoverageProgress();

				tableDataModelList.set(i, categoryDataModel);

			}

		}

		System.out.println("fin load");
		// logger.info("loadData");
		// logger.info(System.currentTimeMillis() - t);

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	public void redirectPage() {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleChangeLow() {
		changeLow = true;
	}

	public void handleChangeMedium() {
		changeMedium = true;
	}

	public void handleChangeHigh() {
		changeHigh = true;
	}

	public void updateTemplate(long id) {
		long t = System.currentTimeMillis();
		JJCategory category = jJCategoryService.findJJCategory(id);

		if (!templateHeader.isEmpty()) {
			String[] categories = templateHeader.split("-");

			switch (categories.length) {
			case 1:
				if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader += String.valueOf(mediumCategory.getId())
							+ "-";

				} else if (category.getStage() < lowCategory.getStage()) {
					mediumCategory = lowCategory;
					lowCategory = category;
					templateHeader = String.valueOf(lowCategory.getId()) + "-"
							+ String.valueOf(mediumCategory.getId()) + "-";

				} else {
					lowCategory = category;
					templateHeader = String.valueOf(lowCategory.getId()) + "-";
				}

				break;

			case 2:

				if (category.getStage() > mediumCategory.getStage()) {

					highCategory = category;
					templateHeader += String.valueOf(highCategory.getId());

				} else if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader = String.valueOf(lowCategory.getId()) + "-"
							+ String.valueOf(mediumCategory.getId()) + "-";

				} else {
					lowCategory = category;
					templateHeader = String.valueOf(lowCategory.getId()) + "-"
							+ String.valueOf(mediumCategory.getId()) + "-";
				}

				break;
			case 3:
				if (category.getStage() > mediumCategory.getStage()) {
					highCategory = category;

				} else if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;

				} else {
					lowCategory = category;

				}

				templateHeader = String.valueOf(lowCategory.getId()) + "-"
						+ String.valueOf(mediumCategory.getId()) + "-"
						+ String.valueOf(highCategory.getId());
				break;
			}
		} else {
			lowCategory = category;
			templateHeader = String.valueOf(lowCategory.getId()) + "-";
		}

		editTableDataModelList();
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private void fullTableDataModelList() {
		long t = System.currentTimeMillis();
		tableDataModelList = new ArrayList<CategoryDataModel>();
		for (int i = 0; i < 3; i++) {
			CategoryDataModel requirementDataModel = new CategoryDataModel(
					new ArrayList<RequirementUtil>(), 0, "", false);

			tableDataModelList.add(requirementDataModel);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
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

		CategoryDataModel categoryDataModel = tableDataModelList.get(i);
		List<JJRequirement> requirements = getRequirementsList(category,
				product, version, project, true);

		categoryDataModel
				.setWrappedData(getListOfRequiremntUtils(requirements));
		// categoryDataModel.calculCompletionProgress();
		// categoryDataModel.calculCoverageProgress();

		tableDataModelList.set(i, categoryDataModel);

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private boolean disabledRequirement;

	public boolean getDisabledRequirement() {
		return disabledRequirement;
	}

	public void setDisabledRequirement(boolean disabledRequirement) {
		this.disabledRequirement = disabledRequirement;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void editTableDataModelList() {

		long t = System.currentTimeMillis();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

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

			if (categoryId != Long.parseLong(result)) {
				List<JJRequirement> requirements = getRequirementsList(
						category, product, version, project, true);

				categoryDataModel = new CategoryDataModel(
						getListOfRequiremntUtils(requirements),
						category.getId(), category.getName(), true);

				// categoryDataModel.calculCompletionProgress();
				// categoryDataModel.calculCoverageProgress();

				tableDataModelList.set(i, categoryDataModel);

			}

		}

		logger.info("edit data");

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	@PostConstruct
	public void init() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJContact contact = (JJContact) session.getAttribute("JJContact");

		if (contact != null) {
			long t = System.currentTimeMillis();
			JJProjectBean jJProjectBean = (JJProjectBean) session
					.getAttribute("jJProjectBean");
			project = jJProjectBean.getProject();

			JJProductBean jJProductBean = (JJProductBean) session
					.getAttribute("jJProductBean");
			product = jJProductBean.getProduct();

			JJVersionBean jJVersionBean = (JJVersionBean) session
					.getAttribute("jJVersionBean");
			version = jJVersionBean.getVersion();

			loadParameter();
			templateHeader = "";

			fullTableDataModelList();
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		}
	}

	private void fullRequirementsList() {
		long t = System.currentTimeMillis();

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
			lowCategoryName = lowCategory.getName() + " :";
			lowRequirementsList = getRequirementsList(lowCategory, null, null,
					requirementProject, true);

		}
		if (mediumCategory == null) {
			mediumCategoryName = "Medium Category :";
			disabledMediumRequirements = true;
		} else {
			mediumCategoryName = mediumCategory.getName() + " :";
			mediumRequirementsList = getRequirementsList(mediumCategory, null,
					null, requirementProject, true);

		}

		if (highCategory == null) {
			highCategoryName = "High Category :";
			disabledHighRequirements = true;
		} else {
			highCategoryName = highCategory.getName() + " :";
			highRequirementsList = getRequirementsList(highCategory, null,
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
					if (requirement.getCategory().getId()
							.equals(highCategory.getId())
							&& requirement.getEnabled()) {
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
					if (requirement.getCategory().getId()
							.equals(lowCategory.getId())
							&& requirement.getEnabled()) {
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
					if (requirement.getCategory().getId()
							.equals(highCategory.getId())
							&& requirement.getEnabled()) {
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
					if (requirement.getCategory().getId()
							.equals(lowCategory.getId())
							&& requirement.getEnabled()) {
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

	private void getRequirementsListUP() {
		long t = System.currentTimeMillis();

		List<JJRequirement> listUP = new ArrayList<JJRequirement>();

		/**
		 * Low Category
		 */
		if (requirementCategory.getId().equals(lowCategory.getId())) {

			// List UP contains M & H

			if (changeMedium) {

				for (JJRequirement entry : selectedMediumRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());
					if (requirement.getId() == null) {
						req.getRequirementLinkDown().add(requirement);
					}
					listUP.add(req);
				}

			}

			if (changeHigh) {
				for (JJRequirement entry : selectedHighRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());
					if (requirement.getId() == null) {
						req.getRequirementLinkDown().add(requirement);
					}
					listUP.add(req);

				}
			}
		}

		/**
		 * Medium Category
		 */
		else if (requirementCategory.getId().equals(mediumCategory.getId())) {

			// List UP contains H

			if (changeHigh) {

				for (JJRequirement entry : selectedHighRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());
					if (requirement.getId() == null) {
						req.getRequirementLinkDown().add(requirement);
					}
					listUP.add(req);
				}
			}

		}

		if (requirement.getId() == null) {
			requirement.getRequirementLinkUp().addAll(listUP);
		} else {

			if (!listUP.isEmpty() && !storeMapUp.isEmpty()) {
				// Traitement elm par elem

				List<String> idListUP = new ArrayList<String>();
				List<String> idStoreMapUp = new ArrayList<String>();

				for (JJRequirement req : listUP) {
					idListUP.add(String.valueOf(req.getId()));
				}

				for (JJRequirement req : storeMapUp) {
					idStoreMapUp.add(String.valueOf(req.getId()));
				}

				if (!idStoreMapUp.equals(idListUP)) {

					List<JJRequirement> removeList = new ArrayList<JJRequirement>();
					List<JJRequirement> addList = new ArrayList<JJRequirement>();

					for (String key : idStoreMapUp) {
						if (!idListUP.contains(key)) {
							for (JJRequirement req : storeMapUp) {
								if (req.getId().equals(Long.parseLong(key))) {
									req.getRequirementLinkDown().remove(
											requirement);
									removeList.add(req);
									break;
								}
							}
						}
					}

					for (String key : idListUP) {
						if (!idStoreMapUp.contains(key)) {
							for (JJRequirement req : listUP) {
								if (req.getId().equals(Long.parseLong(key))) {
									req.getRequirementLinkDown().add(
											requirement);
									addList.add(req);
									break;
								}
							}
						}
					}

					if (!removeList.isEmpty()) {
						requirement.getRequirementLinkUp()
								.removeAll(removeList);
					}

					if (!addList.isEmpty()) {
						requirement.getRequirementLinkUp().addAll(addList);
					}

				}

			}

			else if (listUP.isEmpty() && !storeMapUp.isEmpty()) {
				// Supprimer les storeMapUp

				for (JJRequirement req : storeMapUp) {
					req.getRequirementLinkDown().remove(requirement);
				}
				requirement.getRequirementLinkUp().removeAll(storeMapUp);

			} else if (!listUP.isEmpty() && storeMapUp.isEmpty()) {
				// Ajouter list Up

				for (JJRequirement req : listUP) {
					req.getRequirementLinkDown().add(requirement);
				}

				requirement.getRequirementLinkUp().addAll(listUP);
			}

		}

		// editRowsFromCategoryDataModel(listUP);

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	private void getRequirementsListDOWN() {
		long t = System.currentTimeMillis();

		List<JJRequirement> listDOWN = new ArrayList<JJRequirement>();

		/**
		 * Medium Category
		 */
		if (requirementCategory.getId().equals(mediumCategory.getId())) {

			// List DOWN contains L

			if (changeLow) {
				for (JJRequirement entry : selectedLowRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());
					listDOWN.add(req);
				}
			}
		}
		/**
		 * High Category
		 */
		else if (requirementCategory.getId().equals(highCategory.getId())) {

			// List DOWN contains L & M

			if (changeLow) {

				for (JJRequirement entry : selectedLowRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());
					listDOWN.add(req);
				}
			}

			if (changeMedium) {

				for (JJRequirement entry : selectedMediumRequirementsList) {

					JJRequirement req = jJRequirementService
							.findJJRequirement(entry.getId());

					listDOWN.add(req);
				}
			}
		}

		if (requirement.getId() == null) {

			for (JJRequirement req : listDOWN) {
				req.getRequirementLinkUp().add(requirement);
			}

			requirement.getRequirementLinkDown().addAll(listDOWN);

		} else {
			if (!listDOWN.isEmpty() && !storeMapDown.isEmpty()) {
				// Traitement elm par elem

				List<String> idListDown = new ArrayList<String>();
				List<String> idStoreMapDown = new ArrayList<String>();

				for (JJRequirement req : listDOWN) {
					idListDown.add(String.valueOf(req.getId()));
				}

				for (JJRequirement req : storeMapDown) {
					idStoreMapDown.add(String.valueOf(req.getId()));
				}

				if (!idStoreMapDown.equals(idListDown)) {

					List<JJRequirement> removeList = new ArrayList<JJRequirement>();
					List<JJRequirement> addList = new ArrayList<JJRequirement>();

					for (String key : idStoreMapDown) {
						if (!idListDown.contains(key)) {
							for (JJRequirement req : storeMapDown) {
								if (req.getId().equals(Long.parseLong(key))) {
									removeList.add(req);
									break;
								}
							}
						}
					}

					for (String key : idListDown) {
						if (!idStoreMapDown.contains(key)) {
							for (JJRequirement req : listDOWN) {
								if (req.getId().equals(Long.parseLong(key))) {
									addList.add(req);
									break;
								}
							}
						}
					}

					if (!removeList.isEmpty()) {

						for (JJRequirement req : removeList) {
							req.getRequirementLinkUp().remove(requirement);
							jJRequirementService.updateJJRequirement(req);
						}

						requirement.getRequirementLinkDown().removeAll(
								removeList);
						reset();
					}

					if (!addList.isEmpty()) {

						for (JJRequirement req : addList) {
							JJRequirement req1 = jJRequirementService
									.findJJRequirement(req.getId());
							JJRequirement req2 = jJRequirementService
									.findJJRequirement(requirement.getId());

							req1.getRequirementLinkUp().add(req2);
							req2.getRequirementLinkDown().add(req1);
							jJRequirementService.updateJJRequirement(req1);

						}
						reset();

					}

				}

			}

			else if (listDOWN.isEmpty() && !storeMapDown.isEmpty()) {
				// Supprimer les storeMapDown
				for (JJRequirement req : storeMapDown) {
					req.getRequirementLinkUp().remove(requirement);
					jJRequirementService.updateJJRequirement(req);
				}
				reset();
				requirement.getRequirementLinkDown().removeAll(storeMapDown);

			} else if (!listDOWN.isEmpty() && storeMapDown.isEmpty()) {

				// Ajouter list Down

				for (JJRequirement req : listDOWN) {

					JJRequirement req1 = jJRequirementService
							.findJJRequirement(req.getId());
					JJRequirement req2 = jJRequirementService
							.findJJRequirement(requirement.getId());

					req1.getRequirementLinkUp().add(req2);
					req2.getRequirementLinkDown().add(req1);

					jJRequirementService.updateJJRequirement(req1);
				}

			}
			reset();

		}
		// editRowsFromCategoryDataModel(listDOWN);
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private List<JJRequirement> getRequirementsList(JJCategory category,
			JJProduct product, JJVersion version, JJProject project,
			boolean withProject) {

		long t = System.currentTimeMillis();
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		if (category != null && withProject) {
			list = jJRequirementService.getRequirements(category, project,
					product, version, null, null, false, true, true);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return list;
	}

	private void getRequirementOrder() {
		
		if (jJTaskBean == null) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		}


		long t = System.currentTimeMillis();
		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Object> subElements = null;

		SortedMap<Integer, JJTestcase> testcases = null;
		SortedMap<Integer, JJTestcase> subTestcases = null;

		if (requirement.getId() == null) {

			// Mode New Requirement

			requirement.setChapter(requirementChapter);

			if (requirementChapter != null) {
				elements = getSortedElements(requirementChapter,
						requirement.getProject(), requirement.getCategory(),
						false);

				if (elements.isEmpty()) {
					requirement.setOrdering(0);
				} else {
					requirement.setOrdering(elements.lastKey() + 1);
				}
			} else {
				requirement.setOrdering(null);
			}

		} else {

			subTestcases = getSortedTestcases(
					jJRequirementService.findJJRequirement(requirement.getId()),
					null);
			testcases = getSortedTestcases(null, jJRequirementService
					.findJJRequirement(requirement.getId()).getChapter());

			// Mode Edit Requirement

			if (requirementChapter != null) {

				if (requirement.getChapter() != null) {

					// Drag R and Drop to C

					if (requirement.getChapter().getId() != requirementChapter
							.getId()) {

						SortedMap<Integer, JJTestcase> testcases1 = getSortedTestcases(
								null, requirementChapter);

						int requirementOrder = requirement.getOrdering();

						elements = getSortedElements(requirement.getChapter(),
								requirement.getProject(),
								requirement.getCategory(), false);

						subElements = elements.tailMap(requirementOrder);

						requirement.setChapter(requirementChapter);

						elements = getSortedElements(requirementChapter,
								requirement.getProject(),
								requirement.getCategory(), false);

						if (elements.isEmpty()) {
							requirement.setOrdering(0);

						} else {
							requirement.setOrdering(elements.lastKey() + 1);
						}

						jJRequirementService.updateJJRequirement(requirement);

						subElements.remove(requirementOrder);

						for (Map.Entry<Integer, Object> entry : subElements
								.entrySet()) {

							String className = entry.getValue().getClass()
									.getSimpleName();
							if (className.equalsIgnoreCase("JJChapter")) {

								JJChapter chapter = (JJChapter) entry
										.getValue();

								int lastOrder = chapter.getOrdering();
								chapter.setOrdering(lastOrder - 1);
								chapter.setUpdatedDate(new Date());

								jJChapterService.updateJJChapter(chapter);

							} else if (className
									.equalsIgnoreCase("JJRequirement")) {

								JJRequirement r = (JJRequirement) entry
										.getValue();

								int lastOrder = r.getOrdering();
								r.setOrdering(lastOrder - 1);
								r.setUpdatedDate(new Date());

								jJRequirementService.updateJJRequirement(r);
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
								testcase.setUpdatedDate(new Date());
								jJTestcaseService.updateJJTestcase(testcase);

								testcase = null;

								for (Map.Entry<Integer, JJTestcase> tmpEntry : tempTestcases
										.entrySet()) {

									testcase = tmpEntry.getValue();
									int lastOrder = testcase.getOrdering();

									testcase.setOrdering(lastOrder - 1);
									testcase.setUpdatedDate(new Date());
									jJTestcaseService
											.updateJJTestcase(testcase);

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
								testcase.setUpdatedDate(new Date());
								jJTestcaseService.updateJJTestcase(testcase);

								i++;

								testcase = null;
							}

						}

					} else {

						jJRequirementService.updateJJRequirement(requirement);
						reset();

					}
				} else {

					SortedMap<Integer, JJTestcase> testcases1 = getSortedTestcases(
							null, requirementChapter);

					requirement.setChapter(requirementChapter);

					elements = getSortedElements(requirementChapter,
							requirement.getProject(),
							requirement.getCategory(), false);
					if (elements.isEmpty()) {
						requirement.setOrdering(0);
					} else {
						requirement.setOrdering(elements.lastKey() + 1);
					}

					jJRequirementService.updateJJRequirement(requirement);
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
							testcase.setUpdatedDate(new Date());
							jJTestcaseService.updateJJTestcase(testcase);

							i++;

							testcase = null;
						}
					}

				}

			} else {

				// Drag R and Drop to Left

				if (requirement.getChapter() != null) {

					int requirementOrder = requirement.getOrdering();

					elements = getSortedElements(requirement.getChapter(),
							requirement.getProject(),
							requirement.getCategory(), false);

					subElements = elements.tailMap(requirementOrder);

					requirement.setChapter(null);
					requirement.setOrdering(null);

					jJRequirementService.updateJJRequirement(requirement);
					reset();

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
									testcase.setUpdatedDate(new Date());
									jJTestcaseService
											.updateJJTestcase(testcase);
								}

								testcase = null;

							}

						}

						tempTestcases = null;
					}

				} else {
					jJRequirementService.updateJJRequirement(requirement);
					reset();
				}

			}

			Set<JJTask> tasks = null;
			Set<JJTestcase> testcaseList = null;

			JJRequirement req = jJRequirementService
					.findJJRequirement(requirement.getId());

			if (requirementStatus.getName().equalsIgnoreCase("CANCELED")
					|| requirementStatus.getName().equalsIgnoreCase("DELETED")) {

				tasks = req.getTasks();
				for (JJTask task : tasks) {
					task.setEnabled(false);
					task.setUpdatedDate(new Date());
					jJTaskBean.saveJJTask(task,true);
				}

				testcaseList = req.getTestcases();
				for (JJTestcase testcase : testcaseList) {
					testcase.setEnabled(false);
					testcase.setUpdatedDate(new Date());
					jJTestcaseService.updateJJTestcase(testcase);
				}

				if (requirementStatus.getName().equalsIgnoreCase("DELETED")) {
					req.setEnabled(false);
					req.setUpdatedDate(new Date());
					jJRequirementService.updateJJRequirement(req);
					reset();
				}

			} else if (requirementStatus.getName().equalsIgnoreCase("MODIFIED")) {
				tasks = req.getTasks();
				for (JJTask task : tasks) {
					task.setEnabled(true);
					task.setUpdatedDate(new Date());
					jJTaskBean.saveJJTask(task,true);
				}

				testcaseList = req.getTestcases();
				for (JJTestcase testcase : testcaseList) {
					testcase.setEnabled(true);
					testcase.setUpdatedDate(new Date());
					jJTestcaseService.updateJJTestcase(testcase);
				}
			}

		}
		elements = null;
		subElements = null;

		testcases = null;
		subTestcases = null;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			JJProject project, JJCategory category, boolean onlyActif) {

		long t = System.currentTimeMillis();
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

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return elements;
	}

	private SortedMap<Integer, JJTestcase> getSortedTestcases(
			JJRequirement requirement, JJChapter chapter) {

		long t = System.currentTimeMillis();
		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(
				requirement, chapter, false, true, false);

		for (JJTestcase testcase : testcases) {
			if (testcase.getOrdering() != null)
				elements.put(testcase.getOrdering(), testcase);
			else
				elements.put(0, testcase);

		}

		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return elements;
	}

	private boolean copyTestcases;
	private boolean copyChapters;
	private boolean copyRequirements;
	private boolean disableImportButton;

	public boolean getCopyTestcases() {
		return copyTestcases;
	}

	public void setCopyTestcases(boolean copyTestcases) {
		this.copyTestcases = copyTestcases;
	}

	public boolean getCopyChapters() {
		return copyChapters;
	}

	public void setCopyChapters(boolean copyChapters) {
		this.copyChapters = copyChapters;
	}

	public boolean getCopyRequirements() {
		return copyRequirements;
	}

	public void setCopyRequirements(boolean copyRequirements) {
		this.copyRequirements = copyRequirements;
	}

	public boolean getDisableImportButton() {
		return disableImportButton;
	}

	public void setDisableImportButton(boolean disableImportButton) {
		this.disableImportButton = disableImportButton;
	}

	public void copyTestcases() {

		for (ImportFormat importFormat : importFormats) {
			importFormat.setCopyTestcase(copyTestcases);
		}
	}

	public void copyTestcase() {
		long t = System.currentTimeMillis();
		boolean copyAll = true;
		for (ImportFormat importFormat : importFormats) {

			if (!importFormat.getCopyTestcase()) {
				copyAll = false;
				break;
			}

		}
		copyTestcases = copyAll;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void copyChapters() {
		long t = System.currentTimeMillis();
		for (ImportFormat importFormat : importFormats) {
			importFormat.setCopyChapter(copyChapters);
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void copyChapter() {
		long t = System.currentTimeMillis();
		boolean copyAll = true;
		for (ImportFormat importFormat : importFormats) {
			if (!importFormat.getCopyChapter()) {
				copyAll = false;
				break;
			}
		}
		copyChapters = copyAll;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void copyRequirements() {
		long t = System.currentTimeMillis();
		for (ImportFormat importFormat : importFormats) {
			importFormat.setCopyRequirement(copyRequirements);
		}

		disableImportButton = !copyRequirements;
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void copyRequirement() {
		long t = System.currentTimeMillis();
		boolean copyAll = true;
		for (ImportFormat importFormat : importFormats) {
			if (!importFormat.getCopyRequirement()) {
				copyAll = false;
				break;
			}
		}

		copyRequirements = copyAll;

		for (ImportFormat importFormat : importFormats) {
			if (importFormat.getCopyRequirement()) {
				disableImportButton = false;
				break;
			} else {
				disableImportButton = true;
			}

		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	public void onRowDblClckSelect(SelectEvent event) {

		if (event.getObject() != null
				&& event.getObject() instanceof RequirementUtil) {
			requirement = ((RequirementUtil) event.getObject())
					.getRequirement();
			editRequirement();
		}

	}

	public void viewLinks() {

		requirement = jJRequirementService.findJJRequirement(requirement
				.getId());

		Set<JJRequirement> downs = requirement.getRequirementLinkDown();
		Set<JJRequirement> ups = requirement.getRequirementLinkUp();
		Set<JJRequirement> links = new HashSet<JJRequirement>();

		if (ups != null)
			links.addAll(ups);
		if (downs != null)
			links.addAll(downs);

		String[] results = templateHeader.split("-");

		for (int i = 0; i < results.length; i++) {

			CategoryDataModel categoryDataModel = tableDataModelList.get(i);
			long categoryId = categoryDataModel.getCategoryId();

			if (categoryId != 0) {

				JJCategory category = jJCategoryService
						.findJJCategory(categoryId);

				List<JJRequirement> requirements = new ArrayList<JJRequirement>();

				if (category.equals(requirement.getCategory())
						&& !listContain(requirement, requirements)) {

					requirements.add(requirement);
				} else {

					for (JJRequirement req : links) {

						Set<JJRequirement> downsREQ = req
								.getRequirementLinkDown();
						Set<JJRequirement> upsREQ = req.getRequirementLinkUp();
						Set<JJRequirement> linksREQ = new HashSet<JJRequirement>();

						if (upsREQ != null)
							linksREQ.addAll(upsREQ);
						if (downs != null)
							linksREQ.addAll(downsREQ);

						for (JJRequirement r : linksREQ) {
							if (r.getCategory().equals(category)
									&& !listContain(r, requirements))
								requirements.add(r);
						}

						if (req.getCategory().equals(category)
								&& !listContain(req, requirements))
							requirements.add(req);
					}

				}

				categoryDataModel = new CategoryDataModel(
						getListOfRequiremntUtils(requirements),
						category.getId(), category.getName(), true);

				// categoryDataModel.calculCompletionProgress();
				// categoryDataModel.calculCoverageProgress();

				tableDataModelList.set(i, categoryDataModel);

			}

		}

	}

	public class CategoryDataModel extends ListDataModel<RequirementUtil>
			implements SelectableDataModel<RequirementUtil> {

		private String nameDataModel;
		private long categoryId;
		private int activeIndex;
		private float coverageProgress = 0;
		private float completionProgress = 0;

		private List<RequirementUtil> filtredRequirements;

		private boolean rendered;

		boolean mine;

		public boolean isMine() {
			return mine;
		}

		public void setMine(boolean mine) {
			this.mine = mine;
		}

		public int getActiveIndex() {
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

		public List<RequirementUtil> getFiltredRequirements() {
			return filtredRequirements;
		}

		public void setFiltredRequirements(
				List<RequirementUtil> filtredRequirements) {
			this.filtredRequirements = filtredRequirements;
		}

		public boolean getRendered() {
			return rendered;
		}

		public void setRendered(boolean rendered) {
			this.rendered = rendered;
		}

		public CategoryDataModel(List<RequirementUtil> data, long categoryId,
				String nameDataModel, boolean rendered) {
			super(data);
			this.categoryId = categoryId;
			this.nameDataModel = nameDataModel;
			this.rendered = rendered;
			this.mine = false;
			this.activeIndex = -1;

		}

		@SuppressWarnings("unchecked")
		private void calculCoverageProgress() {
			long t = System.currentTimeMillis();
			if (categoryId != 0) {

				float compteur = 0;

				List<JJCategory> categoryList = jJCategoryService
						.getCategories(null, false, true, true);

				JJCategory category = jJCategoryService
						.findJJCategory(categoryId);

				boolean sizeIsOne = false;

				List<JJRequirement> requirements = new ArrayList<JJRequirement>();
				for (RequirementUtil r : (List<RequirementUtil>) getWrappedData()) {
					requirements.add(r.getRequirement());
				}

				if (category.getStage() == categoryList.get(0).getStage()) {

					for (JJRequirement requirement : requirements) {

						requirement = jJRequirementService
								.findJJRequirement(requirement.getId());
						for (JJRequirement req : requirement
								.getRequirementLinkUp()) {
							if (req.getEnabled()) {
								compteur++;
								break;
							}
						}

					}

					sizeIsOne = true;
				} else if (category.getStage() == categoryList.get(
						categoryList.size() - 1).getStage()
						&& !sizeIsOne) {

					for (JJRequirement requirement : requirements) {
						boolean linkUp = false;
						boolean linkDown = false;
						requirement = jJRequirementService
								.findJJRequirement(requirement.getId());
						for (JJRequirement req : requirement
								.getRequirementLinkDown()) {
							if (req.getEnabled()) {
								linkDown = true;
								break;
							}
						}

						for (JJTask task : requirement.getTasks()) {
							if (task.getEnabled()) {
								linkUp = true;
								break;
							}
						}

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

						for (JJRequirement req : requirement
								.getRequirementLinkUp()) {
							if (req.getEnabled()) {
								linkUp = true;
								break;
							}
						}

						for (JJRequirement req : requirement
								.getRequirementLinkDown()) {
							if (req.getEnabled()) {
								linkDown = true;
								break;
							}
						}

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
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		}

		@SuppressWarnings("unchecked")
		private void calculCompletionProgress() {
			long t = System.currentTimeMillis();
			if (categoryId != 0) {

				float compteur = 0;
				List<JJRequirement> requirements = new ArrayList<JJRequirement>();

				for (RequirementUtil r : (List<RequirementUtil>) getWrappedData()) {
					requirements.add(r.getRequirement());
				}
				for (JJRequirement requirement : requirements) {
					compteur = compteur + calculCompletion(requirement);

				}

				if (requirements.isEmpty()) {
					completionProgress = 0;
				} else {
					completionProgress = compteur / requirements.size();
				}

				completionProgress = completionProgress * 100;
			}
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		}

		private float calculCompletion(JJRequirement requirement) {
			long t = System.currentTimeMillis();
			float compteur = 0;
			int size = 0;
			requirement = jJRequirementService.findJJRequirement(requirement
					.getId());
			Set<JJRequirement> linksUp = requirement.getRequirementLinkUp();
			for (JJRequirement req : linksUp) {

				if (req.getEnabled()) {
					compteur = compteur + calculCompletion(req);
					size++;
				}

			}

			Set<JJTask> tasks = requirement.getTasks();
			int hasTaskCompleted = 0;
			if (!tasks.isEmpty()) {
				boolean completed = false;
				for (JJTask task : tasks) {
					if (task.getEnabled()) {
						if (task.getEndDateReal() != null) {
							completed = true;
						} else {
							completed = false;
							break;
						}

					}
				}
				if (completed) {
					compteur++;
					hasTaskCompleted = 1;
				}
			}
			if (size > 0) {
				compteur = compteur / (size + hasTaskCompleted);
			}
			logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
			return compteur;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RequirementUtil getRowData(String rowKey) {

			List<RequirementUtil> requirements = (List<RequirementUtil>) getWrappedData();

			for (RequirementUtil req : requirements) {
				if (req.getRequirement().getId().toString().equals(rowKey))
					return req;
			}

			return null;
		}

		@Override
		public Object getRowKey(RequirementUtil req) {
			return req.getRequirement().getId().toString();
		}

	}

	public class ImportFormat {

		private String id;
		private JJRequirement requirement;
		private boolean copyRequirement;
		private boolean copyTestcase;
		private boolean copyChapter;

		public ImportFormat() {
			super();
		}

		public ImportFormat(String id, JJRequirement requirement,
				boolean copyRequirement, boolean copyTestcase,
				boolean copyChapter) {

			super();
			long t = System.currentTimeMillis();
			this.id = id;
			this.requirement = requirement;
			this.copyRequirement = copyRequirement;
			this.copyTestcase = copyTestcase;
			this.copyChapter = copyChapter;
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

		public boolean getCopyRequirement() {
			return copyRequirement;
		}

		public void setCopyRequirement(boolean copyRequirement) {
			this.copyRequirement = copyRequirement;
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
		return jJRequirementService.getRequirements(category, project, product,
				version, status, chapter, withChapter, onlyActif,
				orderByCreationdate);
	}

	public List<JJRequirement> getRequirements(JJProject project,
			JJProduct product, JJVersion version) {
		return jJRequirementService.getRequirements(project, product, version);
	}

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif) {
		return jJRequirementService
				.getRequirementChildrenWithChapterSortedByOrder(chapter,
						onlyActif);
	}

	public void reset() {
		long t = System.currentTimeMillis();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJStatusBean jJStatusBean = (JJStatusBean) session
				.getAttribute("jJStatusBean");
		if (jJStatusBean != null)
			jJStatusBean.setPieChart(null);

		setJJRequirement_(null);
		setSelectedBugs(null);
		setSelectedTasks(null);
		setSelectedMessages(null);
		setSelectedRequirementLinkDown(null);
		setSelectedRequirementLinkUp(null);
		setSelectedTestcases(null);
		setCreateDialogVisible(false);
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
	}

	private boolean getRequirementDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("RequirementDialog",
				"specs.requirement.create.saveandclose");
	}

	// la partie mine

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
				getListOfRequiremntUtils(requirements), req.getCategory()
						.getId(), req.getCategory().getName(), true);

		// categoryDataModel.calculCompletionProgress();
		// categoryDataModel.calculCoverageProgress();

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

	public void mineChangeEvent(CategoryDataModel tableDataModel) {

		System.out.println(tableDataModel.isMine());
		if (tableDataModel.isMine()) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			tableDataModel.setMine(true);
			tableDataModel
					.setFiltredRequirements(getListOfRequiremntUtils(jJRequirementService
							.getMineRequirements((JJContact) session
									.getAttribute("JJContact"), product,
									project, jJCategoryService
											.findJJCategory(tableDataModel
													.getCategoryId()),
									importVersion, true, true)));

		} else {
			JJCategory category = jJCategoryService
					.findJJCategory(tableDataModel.getCategoryId());
			List<JJRequirement> requirements = getRequirementsList(category,
					product, version, project, true);

			CategoryDataModel categoryDataModel = new CategoryDataModel(
					getListOfRequiremntUtils(requirements), category.getId(),
					category.getName(), true);

			// categoryDataModel.calculCompletionProgress();
			// categoryDataModel.calculCoverageProgress();

			tableDataModelList.set(tableDataModelList.indexOf(tableDataModel),
					categoryDataModel);

		}
	}

	// mindMap part

	private MindmapNode reqRoot;

	public MindmapNode getReqRoot() {
		return reqRoot;
	}

	public void setReqRoot(MindmapNode reqRoot) {
		this.reqRoot = reqRoot;
	}

	public void initReqMindMap() {

		this.requirement = jJRequirementService.findJJRequirement(requirement
				.getId());
		this.reqRoot = new DefaultMindmapNode(requirement.getCategory()
				.getName().toUpperCase()
				+ " : " + requirement.getName(), requirement, "FFCC00", true);
		for (JJRequirement r : requirement.getRequirementLinkDown()) {
			MindmapNode linkDownNode = new DefaultMindmapNode(r.getCategory()
					.getName().toUpperCase()
					+ " : " + r.getName(), r, "#00A8E8", true);
			System.out.println("linkDownNode " + linkDownNode.getLabel());
			if (r.getEnabled())
				reqRoot.addNode(linkDownNode);

		}

		for (JJRequirement r : requirement.getRequirementLinkUp()) {
			MindmapNode linkUpNode = new DefaultMindmapNode(r.getCategory()
					.getName().toUpperCase()
					+ " : " + r.getName(), r, "#6e9ebf", true);
			System.out.println("linkUpNode " + linkUpNode.getLabel());
			if (r.getEnabled())
				reqRoot.addNode(linkUpNode);

		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("requirementMindMap.show()");

	}

	public void onNodeDblselect(SelectEvent event) {
		this.requirement = (JJRequirement) ((MindmapNode) event.getObject())
				.getData();
		this.requirement = jJRequirementService.findJJRequirement(requirement
				.getId());

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("requirementMindMap.hide()");
		editRequirement();
		context.execute("requirementDialogWidget.show()");
	}

	public void onNodeSelect(SelectEvent event) {

		this.requirement = (JJRequirement) ((MindmapNode) event.getObject())
				.getData();
		this.requirement = jJRequirementService.findJJRequirement(requirement
				.getId());
		MindmapNode node = (MindmapNode) event.getObject();

		if (node.getChildren().isEmpty()) {

			for (JJRequirement r : requirement.getRequirementLinkDown()) {
				MindmapNode linkDownNode = new DefaultMindmapNode(r
						.getCategory().getName().toUpperCase()
						+ " : " + r.getName(), r, "#00A8E8", true);
				System.out.println("linkDownNode " + linkDownNode.getLabel());
				if (r.getEnabled())
					node.addNode(linkDownNode);

			}

			for (JJRequirement r : requirement.getRequirementLinkUp()) {
				MindmapNode linkUpNode = new DefaultMindmapNode(r.getCategory()
						.getName().toUpperCase()
						+ " : " + r.getName(), r, "#6e9ebf", true);
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
			if (tableDataModelList.get(j).getCoverageProgress() == 0) {
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
		loadData();
		closeDialog(false);
		// oncomplete="PF('blockUIWidget').unblock();"
	}

	public String getRowStyleClass(JJRequirement requirement) {

		long t = System.currentTimeMillis();
		List<JJCategory> categoryList = jJCategoryService.getCategories(null,
				false, true, true);

		JJCategory category = jJCategoryService.findJJCategory(requirement
				.getCategory().getId());

		boolean sizeIsOne = false;

		boolean UP = false;
		boolean DOWN = false;
		boolean TASK = true;
		boolean ENCOURS = false;
		boolean FINIS = true;
		requirement = jJRequirementService.findJJRequirement(requirement
				.getId());
		if (category.getStage() == categoryList.get(0).getStage()) {
			DOWN = true;

			for (JJRequirement req : requirement.getRequirementLinkUp()) {
				if (req.getEnabled()) {
					UP = true;
					break;
				}
			}

			sizeIsOne = true;
		} else if (category.getStage() == categoryList.get(
				categoryList.size() - 1).getStage()
				&& !sizeIsOne) {

			UP = true;

			for (JJRequirement req : requirement.getRequirementLinkDown()) {
				if (req.getEnabled()) {
					DOWN = true;
					break;
				}
			}

		} else {

			for (JJRequirement req : requirement.getRequirementLinkUp()) {
				if (req.getEnabled()) {
					UP = true;
					break;
				}
			}

			for (JJRequirement req : requirement.getRequirementLinkDown()) {
				if (req.getEnabled()) {
					DOWN = true;
					break;
				}
			}

		}

		List<JJTask> tasks = jJTaskService.getTasks(null, null, null, null,
				null, requirement, null, null, true, false, false, null);
		if (tasks.isEmpty()) {
			TASK = false;
		}
		for (JJTask task : tasks) {
			if (task.getEndDateReal() == null) {
				ENCOURS = true;
				FINIS = false;
				break;
			}
		}

		String rowStyleClass = "";

		if (UP && DOWN && TASK) {

			if (ENCOURS && !FINIS) {
				rowStyleClass = "RequirementInProcess";
			} else if (!ENCOURS && FINIS) {

				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, true, false, false);
				boolean SUCCESS = true;

				for (JJTestcase testcase : testcases) {

					List<JJTestcaseexecution> testcaseExecutions = jJTestcaseexecutionService
							.getTestcaseexecutions(testcase, null, true, true,
									false);

					if (testcaseExecutions.isEmpty()) {
						SUCCESS = false;
					} else {

						if ((testcaseExecutions.get(0).getPassed() == null)
								|| (testcaseExecutions.get(0).getPassed() != null && !testcaseExecutions
										.get(0).getPassed())) {
							SUCCESS = false;
							break;

						}
					}

				}

				if (SUCCESS) {
					rowStyleClass = "RequirementFinishedSuccessed";
				} else {
					rowStyleClass = "RequirementFinishedTestProcess";
				}

			}

		} else if (UP && DOWN && !TASK) {
			rowStyleClass = "RequirementNoTask";
		} else {
			rowStyleClass = "RequirementNotLinked";
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));
		return rowStyleClass;
	}

	public void initNonCouvretSpec(ComponentSystemEvent e) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean login = (LoginBean) session.getAttribute("loginBean");

		if (noCouvretReq == null && login.isEnable()) {
			noCouvretReq = new ArrayList<JJRequirement>();
			for (JJRequirement req : jJRequirementService
					.findAllJJRequirements()) {
				if (req.getEnabled() && req.getCategory() != null) {
					if (isRequirementNotLinked(req))
						noCouvretReq.add(req);
				}
			}
		}

	}

	public boolean isRequirementNotLinked(JJRequirement req) {
		List<JJCategory> categoryList = jJCategoryService.getCategories(null,
				false, true, true);

		JJCategory category = jJCategoryService.findJJCategory(req
				.getCategory().getId());

		boolean sizeIsOne = false;

		boolean UP = false;
		boolean DOWN = false;
		boolean TASK = true;

		req = jJRequirementService.findJJRequirement(req.getId());

		if (jJTaskService.getTasks(null, null, null, null, null, req, null,
				null, true, false, false, null).isEmpty()) {
			TASK = false;
		}
		if (!TASK) {

			if (category.getStage() == categoryList.get(0).getStage()) {
				DOWN = true;

				for (JJRequirement r : req.getRequirementLinkUp()) {
					if (r.getEnabled()) {
						UP = true;
						break;
					}
				}

				sizeIsOne = true;
			} else if (category.getStage() == categoryList.get(
					categoryList.size() - 1).getStage()
					&& !sizeIsOne) {

				UP = true;

				for (JJRequirement r : req.getRequirementLinkDown()) {
					if (r.getEnabled()) {
						DOWN = true;
						break;
					}
				}

			} else {

				for (JJRequirement r : req.getRequirementLinkUp()) {
					if (r.getEnabled()) {
						UP = true;
						break;
					}
				}

				if (UP) {
					for (JJRequirement r : req.getRequirementLinkDown()) {
						if (r.getEnabled()) {
							DOWN = true;
							break;
						}
					}
				}

			}
		}

		return UP && DOWN && !TASK;
	}

	public List<RequirementUtil> getListOfRequiremntUtils(
			List<JJRequirement> requirments) {
		List<RequirementUtil> requirementUtils = new ArrayList<RequirementUtil>();
		for (JJRequirement req : requirments) {
			requirementUtils
					.add(new RequirementUtil(req, getRowStyleClass(req)));
		}
		return requirementUtils;
	}

}

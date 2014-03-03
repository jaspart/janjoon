package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import org.primefaces.context.RequestContext;
import org.primefaces.model.SelectableDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTaskService;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean {

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	private JJCategory lowCategory;
	private JJCategory mediumCategory;
	private JJCategory highCategory;
	private JJCategory requirementCategory;

	private List<JJCategory> categoryList;

	private List<RequirementDataModel> tableDataModelList;

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

	private JJStatus requirementStatus;

	private List<JJStatus> requirementStatusList;

	private String templateHeader;
	private String outputTemplateHeader;
	private String message;
	private String lowCategoryName;
	private String mediumCategoryName;
	private String highCategoryName;

	private List<String> lowRequirementsList;
	private List<String> mediumRequirementsList;
	private List<String> highRequirementsList;
	private List<String> selectedLowRequirementsList;
	private List<String> selectedMediumRequirementsList;
	private List<String> selectedHighRequirementsList;

	private boolean disabledLowRequirements;
	private boolean disabledMediumRequirements;
	private boolean disabledHighRequirements;
	private boolean disabledVersion;
	private boolean disabledStatus;
	private boolean disabledTask;

	private boolean initiateTask;
	private boolean disabledInitTask;

	private JJTask task;

	private List<JJRequirement> storeMapUp;
	private List<JJRequirement> storeMapDown;
	private List<String> namesList;

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
		categoryList = jJCategoryService.getCategories(true, true);
		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public List<RequirementDataModel> getTableDataModelList() {
		return tableDataModelList;
	}

	public void setTableDataModelList(
			List<RequirementDataModel> tableDataModelList) {
		this.tableDataModelList = tableDataModelList;
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

	public JJVersion geVersion() {
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
		requirementVersionList = jJVersionService.getVersions(true,
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
		requirementChapterList = jJChapterService.getChapters(
				requirementProject, requirementProduct, requirementCategory,
				true);
		return requirementChapterList;
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

		requirementStatusList = jJStatusService
				.getStatus(true, namesList, true);
		return requirementStatusList;
	}

	public void setRequirementStatusList(List<JJStatus> requirementStatusList) {

		this.requirementStatusList = requirementStatusList;
	}

	public String getTemplateHeader() {
		return templateHeader;
	}

	public void setTemplateHeader(String templateHeader) {
		this.templateHeader = templateHeader;
	}

	public String getOutputTemplateHeader() {
		if (templateHeader != null) {
			outputTemplateHeader = templateHeader.replace("/", " ");
		}
		return outputTemplateHeader;
	}

	public void setOutputTemplateHeader(String outputTemplateHeader) {
		this.outputTemplateHeader = outputTemplateHeader;
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

	public List<String> getLowRequirementsList() {
		return lowRequirementsList;
	}

	public void setLowRequirementsList(List<String> lowRequirementsList) {
		this.lowRequirementsList = lowRequirementsList;
	}

	public List<String> getMediumRequirementsList() {
		return mediumRequirementsList;
	}

	public void setMediumRequirementsList(List<String> mediumRequirementsList) {
		this.mediumRequirementsList = mediumRequirementsList;
	}

	public List<String> getHighRequirementsList() {
		return highRequirementsList;
	}

	public void setHighRequirementsList(List<String> highRequirementsList) {
		this.highRequirementsList = highRequirementsList;
	}

	public List<String> getSelectedLowRequirementsList() {
		return selectedLowRequirementsList;
	}

	public void setSelectedLowRequirementsList(
			List<String> selectedLowRequirementsList) {
		this.selectedLowRequirementsList = selectedLowRequirementsList;
	}

	public List<String> getSelectedMediumRequirementsList() {
		return selectedMediumRequirementsList;
	}

	public void setSelectedMediumRequirementsList(
			List<String> selectedMediumRequirementsList) {
		this.selectedMediumRequirementsList = selectedMediumRequirementsList;
	}

	public List<String> getSelectedHighRequirementsList() {
		return selectedHighRequirementsList;
	}

	public void setSelectedHighRequirementsList(
			List<String> selectedHighRequirementsList) {
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

	public void newRequirement(long id) {
		System.out.println("New Requirement");

		message = "New Requirement";

		requirementCategory = jJCategoryService.findJJCategory(id);

		requirement = new JJRequirement();
		requirement.setCreationDate(new Date());
		requirement.setEnabled(true);
		requirement.setDescription(null);

		requirement.setCategory(requirementCategory);

		requirementProject = project;
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

		namesList = new ArrayList<String>();
		requirementStatus = jJStatusService.getJJStatusWithName("NEW");
		disabledStatus = true;

		fullRequirementsList();

		selectedLowRequirementsList = new ArrayList<String>();
		selectedMediumRequirementsList = new ArrayList<String>();
		selectedHighRequirementsList = new ArrayList<String>();

		task = new JJTask();
		disabledInitTask = false;
		initiateTask = false;
		disabledTask = true;

	}

	public void editRequirement() {
		System.out.println("Edit Requirement");

		message = "Edit Requirement";

		requirementCategory = requirement.getCategory();
		requirement.setUpdatedDate(new Date());

		requirementProject = requirement.getProject();

		requirementProduct = requirement.getProduct();

		if (requirementProduct != null) {
			disabledVersion = false;
		} else {
			disabledVersion = true;
		}

		requirementVersion = requirement.getVersioning();

		requirementChapter = requirement.getChapter();

		namesList = new ArrayList<String>();
		namesList.add("DELETED");
		namesList.add("NEW");

		if (requirement.getStatus().getName().equalsIgnoreCase("NEW")) {
			requirementStatus = jJStatusService.getJJStatusWithName("MODIFIED");
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
	}

	public void deleteRequirement() {

		System.out.println("DELETE Requirement ...");
		requirement.setEnabled(false);
		jJRequirementService.updateJJRequirement(requirement);
		requirement = null;
	}

	@SuppressWarnings("unchecked")
	public void releaseRequirement(long id) {

		System.out.println("RELEASE Requirements ...");
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		for (RequirementDataModel requirementDataModel : tableDataModelList) {
			if (requirementDataModel.getCategoryId() == id) {
				List<JJRequirement> temp = (List<JJRequirement>) requirementDataModel
						.getWrappedData();
				list.addAll(temp);
				break;
			}

		}

		JJStatus status = jJStatusService.getJJStatusWithName("RELEASED");

		for (JJRequirement requirement : list) {
			requirement.setStatus(status);
			jJRequirementService.updateJJRequirement(requirement);
		}

	}

	public void save(JJChapterBean jJChapterBean) {

		String message = "";

		requirement.setProject(requirementProject);
		requirement.setProduct(requirementProduct);
		requirement.setVersioning(requirementVersion);
		requirement.setStatus(requirementStatus);

		getRequirementsListUP();

		if (!requirementCategory.getId().equals(lowCategory.getId())) {
			getRequirementsListDOWN();
		}

		getRequirementOrder(jJChapterBean);

		if (requirement.getId() == null) {
			System.out.println("SAVING new Requirement...");

			if (initiateTask) {
				task.setName(requirement.getName());
				task.setDescription("This Task: " + task.getName());
				requirement.getTasks().add(task);
				task.setRequirement(requirement);
				jJRequirementService.saveJJRequirement(requirement);
				jJTaskService.saveJJTask(task);
			} else {
				jJRequirementService.saveJJRequirement(requirement);
			}

			message = "message_successfully_created";

			newRequirement(requirementCategory.getId());

		} else {
			System.out.println("UPDATING Requirement...");

			// jJRequirementService.updateJJRequirement(requirement);

			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("requirementDialogWidget.hide()");
			closeDialog();
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJRequirement");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void initiateTask() {

		System.out.println("initiateTask " + initiateTask);

		disabledTask = !initiateTask;

		if (initiateTask) {
			task = new JJTask();
			task.setEnabled(true);
			task.setCreationDate(new Date());
			task.setWorkloadPlanned(8);
		} else {
			task = new JJTask();
		}

	}

	public void completeTask() {
		System.out.println("task.getCompletion() " + task.getCompletion());
	}

	public void closeDialog() {
		System.out.println("close Dialog");
		message = null;
		namesList = null;
		lowCategoryName = null;
		mediumCategoryName = null;
		highCategoryName = null;
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
	}

	public boolean getDisabledEdit(JJRequirement requirement) {
		if (requirement.getStatus().getName().equalsIgnoreCase("RELEASED")) {
			return true;
		} else {
			return false;
		}
	}

	public void handleSelectProject() {

	}

	public void handleSelectProduct() {

		if (requirementProduct != null) {
			disabledVersion = false;
			requirementVersion = null;
			requirementChapter = null;
		} else {
			disabledVersion = true;
			requirementVersion = null;
			requirementChapter = null;
		}

	}

	public void handleSelectVersion() {

	}

	public void handleSelectChapter() {

	}

	public void handleSelectStatus() {
		System.out.println(requirementStatus.getName());
	}

	public void loadData(JJProjectBean jJProjectBean,
			JJProductBean jJProductBean, JJVersionBean jJVersionBean,
			JJChapterBean jJChapterBean) {

		project = jJProjectBean.getProject();
		product = jJProductBean.getProduct();
		version = jJVersionBean.getVersion();

		jJChapterBean.setProject(project);
		jJChapterBean.setProduct(product);
		jJChapterBean.setVersion(version);

		fullTableDataModelList();
	}

	public void updateTemplate(long id) {

		JJCategory category = jJCategoryService.findJJCategory(id);

		if (templateHeader != null) {
			String[] categories = templateHeader.split("/");

			switch (categories.length) {
			case 1:
				if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader += mediumCategory.getName() + "/";

				} else if (category.getStage() < lowCategory.getStage()) {
					mediumCategory = lowCategory;
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";

				} else {
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/";
				}

				break;

			case 2:

				if (category.getStage() > mediumCategory.getStage()) {

					highCategory = category;
					templateHeader += highCategory.getName();

				} else if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";

				} else {
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";
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

				templateHeader = lowCategory.getName() + "/"
						+ mediumCategory.getName() + "/"
						+ highCategory.getName();

				break;

			}

		} else {
			lowCategory = category;
			templateHeader = lowCategory.getName() + "/";
		}

		System.out.println("\n" + templateHeader);
	}

	private void fullTableDataModelList() {
		Map<String, List<JJRequirement>> mapTable = new LinkedHashMap<String, List<JJRequirement>>();

		if (lowCategory != null) {
			mapTable.put(String.valueOf(lowCategory.getId()),
					getRequirementsList(lowCategory, product, version));
		}

		if (mediumCategory != null) {
			mapTable.put(String.valueOf(mediumCategory.getId()),
					getRequirementsList(mediumCategory, product, version));
		}

		if (highCategory != null) {
			mapTable.put(String.valueOf(highCategory.getId()),
					getRequirementsList(highCategory, product, version));
		}

		tableDataModelList = new ArrayList<RequirementDataModel>();

		for (Map.Entry<String, List<JJRequirement>> entry : mapTable.entrySet()) {

			List<JJRequirement> value = entry.getValue();

			String key = entry.getKey();

			long categoryId = Long.parseLong(key);

			JJCategory category = jJCategoryService.findJJCategory(categoryId);
			String nameDataModel = category.getName();

			RequirementDataModel requirementDataModel = new RequirementDataModel(
					value, categoryId, nameDataModel);

			tableDataModelList.add(requirementDataModel);
		}
	}

	private void fullRequirementsList() {

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

		List<JJRequirement> list;

		if (lowCategory == null) {
			lowCategoryName = "Low Category :";
			disabledLowRequirements = true;
		} else {
			lowCategoryName = lowCategory.getName() + " :";
			list = getRequirementsList(lowCategory, null, null);
			lowRequirementsList = convertRequirementListToStringList(list);
		}
		if (mediumCategory == null) {
			mediumCategoryName = "Medium Category :";
			disabledMediumRequirements = true;
		} else {
			mediumCategoryName = mediumCategory.getName() + " :";
			list = getRequirementsList(mediumCategory, null, null);
			mediumRequirementsList = convertRequirementListToStringList(list);
		}

		if (highCategory == null) {
			highCategoryName = "High Category :";
			disabledHighRequirements = true;
		} else {
			highCategoryName = highCategory.getName() + " :";
			list = getRequirementsList(highCategory, null, null);
			highRequirementsList = convertRequirementListToStringList(list);
		}

		list = null;
	}

	private void fullSelectedRequirementsList() {

		Set<JJRequirement> list;
		storeMapUp = new ArrayList<JJRequirement>();
		storeMapDown = new ArrayList<JJRequirement>();

		selectedLowRequirementsList = new ArrayList<String>();
		selectedMediumRequirementsList = new ArrayList<String>();
		selectedHighRequirementsList = new ArrayList<String>();

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
				selectedMediumRequirementsList = convertRequirementListToStringList(tempList);
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
				selectedHighRequirementsList = convertRequirementListToStringList(tempList);

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
				selectedLowRequirementsList = convertRequirementListToStringList(tempList);

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
				selectedHighRequirementsList = convertRequirementListToStringList(tempList);

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
				selectedLowRequirementsList = convertRequirementListToStringList(tempList);

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
				selectedMediumRequirementsList = convertRequirementListToStringList(tempList);
			}

		}

		System.out.println("storeMapUp.size() " + storeMapUp.size());
		System.out.println("storeMapDown.size() " + storeMapDown.size());
		list = null;

	}

	private void getRequirementsListUP() {

		List<JJRequirement> listUP = new ArrayList<JJRequirement>();

		/**
		 * Low Category
		 */
		if (requirementCategory.getId().equals(lowCategory.getId())) {

			// List UP contains M & H

			for (String entry : selectedMediumRequirementsList) {

				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));
				if (requirement.getId() == null) {
					req.getRequirementLinkDown().add(requirement);
				}
				listUP.add(req);
			}

			for (String entry : selectedHighRequirementsList) {

				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));
				if (requirement.getId() == null) {
					req.getRequirementLinkDown().add(requirement);
				}
				listUP.add(req);

			}
		}

		/**
		 * Medium Category
		 */
		else if (requirementCategory.getId().equals(mediumCategory.getId())) {

			// List UP contains H

			for (String entry : selectedHighRequirementsList) {

				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));
				if (requirement.getId() == null) {
					req.getRequirementLinkDown().add(requirement);
				}
				listUP.add(req);
			}

		}

		if (requirement.getId() == null) {
			requirement.getRequirementLinkUp().addAll(listUP);
		} else {

			if (!listUP.isEmpty() && !storeMapUp.isEmpty()) {
				// Traitement elm par elem

				System.out
						.println("!listUP.isEmpty() && !storeMapUp.isEmpty()");

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

				System.out.println("listUP.isEmpty() && !storeMapUp.isEmpty()");

				for (JJRequirement req : storeMapUp) {
					req.getRequirementLinkDown().remove(requirement);
				}
				requirement.getRequirementLinkUp().removeAll(storeMapUp);

			} else if (!listUP.isEmpty() && storeMapUp.isEmpty()) {
				// Ajouter list Up

				System.out.println("!listUP.isEmpty() && storeMapUp.isEmpty()");

				for (JJRequirement req : listUP) {
					req.getRequirementLinkDown().add(requirement);
				}

				requirement.getRequirementLinkUp().addAll(listUP);
			}

		}

		System.out
				.println("Fin Up" + requirement.getRequirementLinkUp().size());

	}

	private void getRequirementsListDOWN() {

		List<JJRequirement> listDOWN = new ArrayList<JJRequirement>();

		/**
		 * Medium Category
		 */
		if (requirementCategory.getId().equals(mediumCategory.getId())) {

			// List DOWN contains L

			for (String entry : selectedLowRequirementsList) {

				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));
				listDOWN.add(req);
			}

		}
		/**
		 * High Category
		 */
		else if (requirementCategory.getId().equals(highCategory.getId())) {

			// List DOWN contains L & M

			for (String entry : selectedLowRequirementsList) {
				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));
				listDOWN.add(req);
			}

			for (String entry : selectedMediumRequirementsList) {

				String key = splitString(entry, "-", 0);

				JJRequirement req = jJRequirementService.findJJRequirement(Long
						.parseLong(key));

				listDOWN.add(req);
			}

		}

		if (requirement.getId() == null) {

			for (JJRequirement req : listDOWN) {
				req.getRequirementLinkUp().add(requirement);
			}

			requirement.getRequirementLinkDown().addAll(listDOWN);

		} else {
			System.out.println("requirement.getId() != null");
			if (!listDOWN.isEmpty() && !storeMapDown.isEmpty()) {
				// Traitement elm par elem

				System.out
						.println("!listDOWN.isEmpty() && !storeMapDown.isEmpty()");

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
					}

					if (!addList.isEmpty()) {

						for (JJRequirement req : addList) {
							req.getRequirementLinkUp().add(requirement);
						}

						requirement.getRequirementLinkDown().addAll(addList);
					}

				}

			}

			else if (listDOWN.isEmpty() && !storeMapDown.isEmpty()) {
				// Supprimer les storeMapDown

				System.out
						.println("listDOWN.isEmpty() && !storeMapDown.isEmpty()");

				for (JJRequirement req : storeMapDown) {
					req.getRequirementLinkUp().remove(requirement);
					jJRequirementService.updateJJRequirement(req);
				}

				requirement.getRequirementLinkDown().removeAll(storeMapDown);

			} else if (!listDOWN.isEmpty() && storeMapDown.isEmpty()) {
				System.out
						.println("!listDOWN.isEmpty() && storeMapDown.isEmpty()");
				// Ajouter list Down

				for (JJRequirement req : listDOWN) {
					req.getRequirementLinkUp().add(requirement);
				}
				requirement.getRequirementLinkDown().addAll(listDOWN);
			}

		}

		System.out
				.println("Fin Down requirement.getRequirementLinkDown().size() "
						+ requirement.getRequirementLinkDown().size());

		System.out.println("Quit");

	}

	private List<JJRequirement> getRequirementsList(JJCategory category,
			JJProduct product, JJVersion version) {
		List<JJRequirement> list = new ArrayList<JJRequirement>();
		if (category != null) {
			list = jJRequirementService.getRequirements(category, project,
					product, version, null, false, true, true);
		}
		return list;
	}

	private List<String> convertRequirementListToStringList(
			List<JJRequirement> requirementsList) {
		List<String> list = new ArrayList<String>();
		for (JJRequirement requirement : requirementsList) {
			String entry = requirement.getId() + "-" + requirement.getName();
			list.add(entry);
		}

		return list;
	}

	private String splitString(String s, String regex, int index) {
		String[] result = s.split(regex);
		return result[index];
	}

	private void getRequirementOrder(JJChapterBean jJChapterBean) {
		SortedMap<Integer, Object> elements = null;
		SortedMap<Integer, Object> subElements = null;
		if (requirement.getId() == null) {
			requirement.setChapter(requirementChapter);

			if (requirementChapter != null) {
				elements = jJChapterBean.getSortedElements(requirementChapter,
						null, false);
				if (elements.isEmpty()) {
					requirement.setOrdering(0);
				} else {
					requirement.setOrdering(elements.lastKey() + 1);
				}
			} else {
				requirement.setOrdering(null);
			}
		} else {

			if (requirementChapter != null) {

				if (requirement.getChapter() != null) {

					if (requirement.getChapter().getId() != requirementChapter
							.getId()) {

						int requirementOrder = requirement.getOrdering();
						elements = jJChapterBean.getSortedElements(
								requirement.getChapter(), null, false);

						subElements = elements.tailMap(requirementOrder);

						requirement.setChapter(requirementChapter);
						elements = jJChapterBean.getSortedElements(
								requirementChapter, null, false);
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

								JJRequirement requirement = (JJRequirement) entry
										.getValue();

								int lastOrder = requirement.getOrdering();
								requirement.setOrdering(lastOrder - 1);
								requirement.setUpdatedDate(new Date());

								jJRequirementService
										.updateJJRequirement(requirement);
							}

						}

					} else {

						jJRequirementService.updateJJRequirement(requirement);

					}
				} else {
					requirement.setChapter(requirementChapter);
					elements = jJChapterBean.getSortedElements(
							requirementChapter, null, false);
					if (elements.isEmpty()) {
						requirement.setOrdering(0);
					} else {
						requirement.setOrdering(elements.lastKey() + 1);
					}

					jJRequirementService.updateJJRequirement(requirement);
				}

			} else {

				if (requirement.getChapter() != null) {
					int requirementOrder = requirement.getOrdering();
					elements = jJChapterBean.getSortedElements(
							requirement.getChapter(), null, false);

					subElements = elements.tailMap(requirementOrder);

					requirement.setChapter(null);
					requirement.setOrdering(null);

					jJRequirementService.updateJJRequirement(requirement);

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

				} else {
					jJRequirementService.updateJJRequirement(requirement);
				}

			}
		}
		elements = null;
		subElements = null;
	}

	@SuppressWarnings("unchecked")
	public class RequirementDataModel extends ListDataModel<JJRequirement>
			implements SelectableDataModel<JJRequirement> {

		private String nameDataModel;
		private long categoryId;

		private float coverageProgress = 0;
		private int completionProgress = 0;

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
			System.out.println("categoryId " + categoryId);

			float compteur = 0;
			List<JJRequirement> dataList = (List<JJRequirement>) getWrappedData();
			System.out.println("dataList.size() " + dataList.size());

			List<JJCategory> categoryList = jJCategoryService.getCategories(
					true, true);

			boolean sizeIsOne = false;

			if (categoryId == categoryList.get(0).getId()) {
				System.out.println("categoryId == categoryList.get(0).getId()");
				for (JJRequirement requirement : dataList) {
					if (!requirement.getRequirementLinkUp().isEmpty()) {
						compteur++;
					}
				}

				sizeIsOne = true;
			} else if (categoryId == categoryList.get(categoryList.size() - 1)
					.getId() && !sizeIsOne) {
				System.out
						.println("categoryId == categoryList.get(categoryList.size() - 1)"
								+ ".getId() && !sizeIsOne");
				for (JJRequirement requirement : dataList) {
					if (!requirement.getRequirementLinkDown().isEmpty()
							&& !requirement.getTasks().isEmpty()) {
						compteur++;
					} else if (!requirement.getRequirementLinkDown().isEmpty()
							|| !requirement.getTasks().isEmpty()) {
						compteur += 0.5;
					}
				}
			} else {
				System.out.println("else");
				for (JJRequirement requirement : dataList) {

					if (!requirement.getRequirementLinkDown().isEmpty()
							&& !requirement.getRequirementLinkUp().isEmpty()) {
						compteur++;
					} else if (!requirement.getRequirementLinkDown().isEmpty()
							|| !requirement.getRequirementLinkUp().isEmpty()) {
						compteur += 0.5;
					}
				}
			}
			System.out.println("compteur " + compteur);

			if (dataList.isEmpty()) {
				coverageProgress = 0;
			} else {
				coverageProgress = (compteur * 100) / dataList.size();
			}

			System.out.println("coverageProgress " + coverageProgress);

			return coverageProgress;
		}

		public void setCoverageProgress(float coverageProgress) {
			this.coverageProgress = coverageProgress;
		}

		public int getCompletionProgress() {
			return completionProgress;
		}

		public void setCompletionProgress(int completionProgress) {
			this.completionProgress = completionProgress;
		}

		public RequirementDataModel(List<JJRequirement> data, long categoryId,
				String nameDataModel) {
			super(data);
			this.categoryId = categoryId;
			this.nameDataModel = nameDataModel;

		}

		@Override
		public JJRequirement getRowData(String rowKey) {

			List<JJRequirement> requirements = (List<JJRequirement>) getWrappedData();

			for (JJRequirement requirement : requirements) {
				if (requirement.getId().equals(rowKey))
					return requirement;
			}

			return null;
		}

		@Override
		public Object getRowKey(JJRequirement requirement) {
			return requirement.getId();
		}
	}
}
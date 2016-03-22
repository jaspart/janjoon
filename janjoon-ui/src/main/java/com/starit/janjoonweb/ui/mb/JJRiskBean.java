package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJRisk;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyRiskDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJRisk.class, beanName = "jJRiskBean")
public class JJRiskBean {

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJBugService jJBugService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	private JJRisk JJRisk_;
	private LazyRiskDataModel riskDataModel;
	private List<JJRisk> selectedRiskList;
	private SelectItem[] statusOptions;
	private SelectItem[] productOptions;
	private List<JJRequirement> allRequirements;
	private List<JJBug> allBugs;
	private List<JJTestcase> allTestCases;

	public JJRisk getJJRisk_() {
		if (JJRisk_ == null) {
			JJRisk_ = new JJRisk();
			JJRisk_.setProject(LoginBean.getProject());
			JJRisk_.setProduct(LoginBean.getProduct());
		}
		return JJRisk_;
	}

	public void setJJRisk_(JJRisk JJRisk_) {
		this.JJRisk_ = JJRisk_;
	}

	public LazyRiskDataModel getRiskDataModel() {
		if (riskDataModel == null)
			riskDataModel = new LazyRiskDataModel(jJRiskService,
					LoginBean.getProject(), LoginBean.getProduct());
		return riskDataModel;
	}

	public void setRiskDataModel(LazyRiskDataModel riskDataModel) {
		this.riskDataModel = riskDataModel;
	}

	public List<JJRisk> getSelectedRiskList() {
		return selectedRiskList;
	}

	public void setSelectedRiskList(List<JJRisk> selectedRiskList) {
		this.selectedRiskList = selectedRiskList;
	}

	public SelectItem[] getStatusOptions() {
		if (statusOptions == null)
			;
		statusOptions = createFilterOptions(
				jJStatusService.getStatus("risk", true, null, true));
		return statusOptions;
	}

	public void setStatusOptions(SelectItem[] statusOptions) {
		this.statusOptions = statusOptions;
	}

	public SelectItem[] getProductOptions() {
		if (productOptions == null) {
			if (LoginBean.getProduct() == null)
				productOptions = createFilterOptions(
						((JJProductBean) LoginBean.findBean("jJProductBean"))
								.getProductList());
			else {
				List<JJProduct> prods = new ArrayList<JJProduct>();
				prods.add(LoginBean.getProduct());
				productOptions = createFilterOptions(prods);
			}
		}

		return productOptions;
	}

	public void setProductOptions(SelectItem[] productOptions) {
		this.productOptions = productOptions;
	}

	public List<JJRequirement> getAllRequirements() {
		if (allRequirements == null)
			allRequirements = jJRequirementService.getRequirements(
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorizedMap("Requirement",
									getJJRisk_().getProject(),
									getJJRisk_().getProduct()),
					null, null);
		return allRequirements;
	}

	public void setAllRequirements(List<JJRequirement> allRequirements) {
		this.allRequirements = allRequirements;
	}

	public List<JJBug> getAllBugs() {
		if (allBugs == null)
			allBugs = jJBugService.getBugs(LoginBean.getCompany(),
					getJJRisk_().getProject(), getJJRisk_().getProduct(), null);
		return allBugs;
	}

	public void setAllBugs(List<JJBug> allBugs) {
		this.allBugs = allBugs;
	}

	public List<JJTestcase> getAllTestCases() {
		if (allTestCases == null)
			allTestCases = jJTestcaseService.getImportTestcases(null,
					getJJRisk_().getProject(), getJJRisk_().getProduct(), null,
					null, true, false);
		return allTestCases;
	}

	public void setAllTestCases(List<JJTestcase> allTestCases) {
		this.allTestCases = allTestCases;
	}

	public String getMessage() {

		return (getJJRisk_() == null || getJJRisk_().getId() == null)
				? "risk_create_title"
				: "risk_edit_title";
	}

	public void changeEvent() {
		allRequirements = null;
		allBugs = null;
		allTestCases = null;
	}

	private SelectItem[] createFilterOptions(Object objet) {

		@SuppressWarnings("unchecked")
		List<Object> data = (List<Object>) objet;

		SelectItem[] options = new SelectItem[data.size() + 1];

		options[0] = new SelectItem("",
				MessageFactory.getMessage("label_all").getDetail());
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i) instanceof JJCriticity) {
				JJCriticity criticity = (JJCriticity) data.get(i);
				options[i + 1] = new SelectItem(criticity.getName(),
						criticity.getName());
			} else if (data.get(i) instanceof JJStatus) {
				JJStatus status = (JJStatus) data.get(i);
				options[i + 1] = new SelectItem(status.getName(),
						MessageFactory
								.getMessage("status_" + status.getName(), "")
								.getDetail());

			} else if (data.get(i) instanceof JJImportance) {
				JJImportance importance = (JJImportance) data.get(i);
				options[i + 1] = new SelectItem(importance.getName(),
						importance.getName());
			} else if (data.get(i) instanceof JJProduct) {
				JJProduct product = (JJProduct) data.get(i);
				options[i + 1] = new SelectItem(product.getName(),
						product.getName());
			}

		}
		return options;

	}

	public List<JJProduct> completeProduct(String query) {
		List<JJProduct> suggestions = new ArrayList<JJProduct>();
		suggestions.add(null);
		for (JJProduct jJProduct : jJProductService.getProducts(
				LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false)) {
			String jJProductStr = String.valueOf(
					jJProduct.getName() + " " + jJProduct.getDescription() + " "
							+ jJProduct.getCreationDate() + " "
							+ jJProduct.getUpdatedDate());
			if (jJProductStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJProduct);
			}
		}
		return suggestions;
	}

	public List<JJStatus> completeStatus(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		for (JJStatus jJStatus : jJStatusService.getStatus("Risk", true, null,
				true)) {
			String jJStatusStr = String.valueOf(
					jJStatus.getName() + " " + jJStatus.getDescription() + " "
							+ jJStatus.getCreationDate() + " "
							+ jJStatus.getUpdatedDate());
			if (jJStatusStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}
	public List<JJProject> completeProject(String query) {
		List<JJProject> suggestions = new ArrayList<JJProject>();
		suggestions.add(null);
		for (JJProject jJProject : jJProjectService.getProjects(
				LoginBean.getCompany(),
				((LoginBean) LoginBean.findBean("loginBean")).getContact(),
				true, false)) {
			String jJProjectStr = String.valueOf(
					jJProject.getName() + " " + jJProject.getDescription() + " "
							+ jJProject.getCreationDate() + " "
							+ jJProject.getUpdatedDate());
			if (jJProjectStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJProject);
			}
		}
		return suggestions;
	}

	public void persistRisk() {

		if (LoginBean.getProject() != null) {
			getJJRisk_().setProject(LoginBean.getProject());

		}

		if (LoginBean.getProduct() != null) {

			getJJRisk_().setProduct(LoginBean.getProduct());
		}

		if (getJJRisk_().getId() == null) {
			getJJRisk_().setEnabled(true);
		}

		String message = "";
		if (getJJRisk_().getId() != null) {
			// Hibernate.initialize(this.JJRisk_.getRequirements());
			updateJJRisk(getJJRisk_());
			message = "message_successfully_updated";
		} else {
			saveJJRisk(getJJRisk_());
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				MessageFactory.getMessage("label_risk").getDetail(), "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();

		RequestContext.getCurrentInstance()
				.execute("PF('createDialogWidget').hide()");

		RequestContext.getCurrentInstance().update("growlForm");

	}
	public void deleteRisks() {
		for (JJRisk b : selectedRiskList) {
			b.setEnabled(false);
			updateJJRisk(b);
		}
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted",
				MessageFactory.getMessage("label_risk").getDetail(), "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		selectedRiskList = null;
		reset();
	}

	public void deleteRisk() {

		getJJRisk_().setEnabled(false);
		updateJJRisk(getJJRisk_());
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted",
				MessageFactory.getMessage("label_risk").getDetail(), "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void saveJJRisk(JJRisk b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJRiskService.saveJJRisk(b);
	}

	public void updateJJRisk(JJRisk b) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJRiskService.updateJJRisk(b);
	}

}

package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyCategoryDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCategory.class, beanName = "jJCategoryBean")
public class JJCategoryBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	private JJCategory categoryAdmin;
	private LazyCategoryDataTable categoryListTable;
	private boolean allCompany = false;

	private String message;

	private boolean categoryState;

	public JJCategory getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(JJCategory categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	public LazyCategoryDataTable getCategoryListTable() {
		if (categoryListTable == null)
			categoryListTable = new LazyCategoryDataTable(jJCategoryService,
					LoginBean.getCompany());
		return categoryListTable;
	}

	public void setCategoryListTable(LazyCategoryDataTable categoryListTable) {
		this.categoryListTable = categoryListTable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAllCompany() {
		return allCompany;
	}

	public void setAllCompany(boolean allCompany) {
		this.allCompany = allCompany;
	}

	public void newCategory() {

		message = "admin_category_new_title";
		categoryAdmin = new JJCategory();
		categoryAdmin.setEnabled(true);
		categoryAdmin.setStage(0);
		categoryState = true;
	}

	public void editCategory() {
		message = "admin_category_edit_title";
		categoryState = false;
	}

	public void deleteCategory() {
		// message = "Edit Contact";
		if (categoryAdmin != null) {
			categoryAdmin.setEnabled(false);
			updateJJCategory(categoryAdmin);
			categoryListTable = null;

			String message = "message_successfully_deleted";
			FacesMessage facesMessage = MessageFactory
					.getMessage(message,
							MessageFactory.getMessage("label_category", "")
									.getDetail(), "e");

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void save() {

		String message = "";
		FacesMessage facesMessage = null;
		String name = categoryAdmin.getName().trim().toUpperCase();

		if (!jJCategoryService.nameExist(name, categoryAdmin.getId(),
				LoginBean.getCompany())) {
			categoryAdmin.setName(name);
			if (categoryAdmin.getId() == null) {

				if (!allCompany
						|| !((LoginBean) LoginBean.findBean("loginBean"))
								.getAuthorisationService().isSuperAdmin())
					categoryAdmin.setCompany(LoginBean.getCompany());

				categoryAdmin.setDescription("This is category "
						+ categoryAdmin.getName());
				saveJJCategory(categoryAdmin);
				message = "message_successfully_created";

			} else {

				categoryAdmin.setUpdatedDate(new Date());

				updateJJCategory(categoryAdmin);
				message = "message_successfully_updated";

				// closeDialog();
			}
			categoryListTable = null;
			allCompany = false;

			facesMessage = MessageFactory.getMessage(message, MessageFactory
					.getMessage("label_category", "").getDetail(), "e");
			RequestContext context = RequestContext.getCurrentInstance();

			if (categoryState) {
				if (getCategoryDialogConfiguration()) {
					context.execute("PF('categoryDialogWidget').hide()");
					RequestContext.getCurrentInstance().update("growlForm");
				} else {

					newCategory();
				}

			} else {
				context.execute("PF('categoryDialogWidget').hide()");
				RequestContext.getCurrentInstance().update("growlForm");
			}
		} else {
			message = "validator_buildVersion_nameExist";

			facesMessage = MessageFactory.getMessage(message, MessageFactory
					.getMessage("label_category", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);

		}
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void closeDialog() {
		categoryAdmin = null;
		categoryState = true;
	}

	private boolean getCategoryDialogConfiguration() {
		return jJConfigurationService.getDialogConfig("CategoryDialog",
				"category.create.saveandclose");
	}

	public void updateBeans(JJCategory category) {

		JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
				.findBean("jJRequirementBean");
		JJStatusBean jJStatusBean = (JJStatusBean) LoginBean
				.findBean("jJStatusBean");

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) LoginBean
				.findBean("jJTestcaseBean");

		if (jJStatusBean != null) {
			jJStatusBean.setCategoryDataModel(null);
		}

		if (jJTestcaseBean != null) {
			jJTestcaseBean.setCategoryList(null);
			jJTestcaseBean.setCategory(null);
		}

		if (jJRequirementBean != null
				&& jJRequirementBean.getTableDataModelList() != null) {
			jJRequirementBean.setCategoryList(null);
			jJRequirementBean.setCategoryDataModel(null);
			if (!category.getEnabled())
				jJRequirementBean.updateTemplate(category.getId(), null, true,
						false);

		}

	}

	public void saveJJCategory(JJCategory b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("loginBean")).getContact();
		b.setCreatedBy(contact);
		jJCategoryService.saveJJCategory(b);
		updateBeans(b);
	}

	public void updateJJCategory(JJCategory b) {
		JJContact contact = ((LoginBean) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCategoryService.updateJJCategory(b);
		updateBeans(b);
	}
}

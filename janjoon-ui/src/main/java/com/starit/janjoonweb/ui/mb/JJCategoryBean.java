package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCategory.class, beanName = "jJCategoryBean")
public class JJCategoryBean {

	private JJCategory categoryAdmin;
	private List<JJCategory> categoryListTable;

	private String message;
	private boolean disableLevel;

	private boolean categoryState;

	public JJCategory getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(JJCategory categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	public List<JJCategory> getCategoryListTable() {
		categoryListTable = jJCategoryService.getCategories(null, false, true,
				true);
		return categoryListTable;
	}

	public void setCategoryListTable(List<JJCategory> categoryListTable) {
		this.categoryListTable = categoryListTable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getDisableLevel() {
		return disableLevel;
	}

	public void setDisableLevel(boolean disableLevel) {
		this.disableLevel = disableLevel;
	}

	public void newCategory() {

		message = "New Category";

		categoryAdmin = new JJCategory();
		categoryAdmin.setEnabled(true);
		categoryAdmin.setCreationDate(new Date());
		categoryAdmin.setStage(0);
		disableLevel = false;

		categoryState = true;
	}

	public void editCategory() {

		message = "Edit Category";
		disableLevel = true;

		categoryState = false;
	}

	public void deleteCategory() {
		// message = "Edit Contact";

		if (categoryAdmin != null) {

			categoryAdmin.setEnabled(false);
			jJCategoryService.updateJJCategory(categoryAdmin);

		}
	}

	public void save() {

		String message = "";
		FacesMessage facesMessage = null;
		String name = categoryAdmin.getName().trim().toUpperCase();
		List<JJCategory> categories = jJCategoryService.getCategories(name,
				true, false, false);

		if (categories.isEmpty()) {
			categoryAdmin.setName(name);
			if (categoryAdmin.getId() == null) {

				categoryAdmin.setDescription("This is category "
						+ categoryAdmin.getName());
				jJCategoryService.saveJJCategory(categoryAdmin);
				message = "message_successfully_created";

			} else {

				categoryAdmin.setUpdatedDate(new Date());

				jJCategoryService.updateJJCategory(categoryAdmin);
				message = "message_successfully_updated";

				// closeDialog();
			}

			facesMessage = MessageFactory.getMessage(message, "JJCategory");

			RequestContext context = RequestContext.getCurrentInstance();

			if (categoryState) {
				if (getCategoryDialogConfiguration()) {
					context.execute("categoryDialogWidget.hide()");
				} else {

					newCategory();
				}

			} else {
				context.execute("categoryDialogWidget.hide()");
			}

		} else {
			message = "This category is already exist into the database, try to use another name";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "JJCategory");
		}

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void closeDialog() {

		categoryAdmin = null;

		categoryState = true;
	}

	private boolean getCategoryDialogConfiguration() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJConfigurationBean jJConfigurationBean = (JJConfigurationBean) session
				.getAttribute("jJConfigurationBean");
		return jJConfigurationBean.getDialogConfig("CategoryDialog",
				"category.create.saveandclose");
	}

}

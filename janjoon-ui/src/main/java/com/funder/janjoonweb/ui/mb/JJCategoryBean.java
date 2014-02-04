package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCategory.class, beanName = "jJCategoryBean")
public class JJCategoryBean {

	private JJCategory categoryAdmin;
	private List<JJCategory> categoryListTable;

	private String message;

	public JJCategory getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(JJCategory categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	public List<JJCategory> getCategoryListTable() {
		categoryListTable = jJCategoryService.getAllJJCategories();
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

	public void newCategory() {
		System.out.println("Initial bean category");
		message = "New Category";
		categoryAdmin = new JJCategory();
		categoryAdmin.setEnabled(true);
		categoryAdmin.setCreationDate(new Date());
		categoryAdmin.setStage(0);
	}

	public void save() {
		System.out.println("SAVING Category...");
		String message = "";

		if (categoryAdmin.getId() == null) {
			System.out.println("IS a new JJCategory");

			categoryAdmin.setDescription("This is category "
					+ categoryAdmin.getName());
			jJCategoryService.saveJJCategory(categoryAdmin);
			message = "message_successfully_created";

			newCategory();

		} else {
			jJCategoryService.updateJJCategory(categoryAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("categoryDialogWidget.hide()");
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJCategory");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void closeDialog(CloseEvent event) {
		System.out.println("close dialog");
		categoryAdmin = null;
	}

}

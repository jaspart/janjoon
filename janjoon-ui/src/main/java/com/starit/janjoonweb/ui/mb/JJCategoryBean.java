package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyCategoryDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCategory.class, beanName = "jJCategoryBean")
public class JJCategoryBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	private JJCategory categoryAdmin;
	private LazyCategoryDataTable categoryListTable;

	private String message;

	private boolean categoryState;

	public JJCategory getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(JJCategory categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	public LazyCategoryDataTable getCategoryListTable() {
		if(categoryListTable == null)
		categoryListTable = new LazyCategoryDataTable(jJCategoryService);
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
			categoryListTable=null;
			
			String message = "message_successfully_deleted";
			FacesMessage facesMessage = MessageFactory.getMessage(message, "Category");		

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void save() {

		String message = "";
		FacesMessage facesMessage = null;
		String name = categoryAdmin.getName().trim().toUpperCase();
		List<JJCategory> categories = jJCategoryService.getCategories(name,
				true, false, false);
		if (categoryAdmin.getId() != null && !categories.isEmpty())
		{
			if(categories.size() == 1)
			{
				if(categories.get(0).equals(categoryAdmin))
					categories.remove(0);
			}
		}
		
		if (categories.isEmpty()) {
			categoryAdmin.setName(name);
			if (categoryAdmin.getId() == null) {

				categoryAdmin.setDescription("This is category "
						+ categoryAdmin.getName());
				saveJJCategory(categoryAdmin);
				message = "message_successfully_created";

			}
			else {

				categoryAdmin.setUpdatedDate(new Date());

				updateJJCategory(categoryAdmin);
				message = "message_successfully_updated";

				// closeDialog();
			}
			categoryListTable=null;

			facesMessage = MessageFactory.getMessage(message, "Category");
			RequestContext context = RequestContext.getCurrentInstance();

			if (categoryState) {
				if (getCategoryDialogConfiguration()) {
					context.execute("PF('categoryDialogWidget').hide()");
				}
				else {

					newCategory();
				}

			}
			else {
				context.execute("PF('categoryDialogWidget').hide()");
			}
		}
		else {
			message = "This category is already exist into the database, try to use another name";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "Category");
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
	
	public void saveJJCategory(JJCategory b)
	{
		JJContact contact=(JJContact) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("JJContact");
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJCategoryService.saveJJCategory(b);
	}
	
	public void updateJJCategory(JJCategory b)
	{
		JJContact contact=(JJContact) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("JJContact");
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCategoryService.updateJJCategory(b);
	}
}

package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJRight;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJRight.class, beanName = "jJRightBean")
public class JJRightBean {

	private JJRight rightAdmin;
	private List<JJRight> rightListTable;

	private JJCategory category;
	private List<JJCategory> categoryList;

	private String object;
	private List<String> objectList;

	public JJRight getRightAdmin() {
		return rightAdmin;
	}

	public void setRightAdmin(JJRight rightAdmin) {
		this.rightAdmin = rightAdmin;
	}

	public List<JJRight> getRightListTable() {
		rightListTable = jJRightService.getRightsWithoutProfile();
		return rightListTable;
	}

	public void setRightListTable(List<JJRight> rightListTable) {
		this.rightListTable = rightListTable;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public List<JJCategory> getCategoryList() {
		categoryList = jJCategoryService.getCategories(null, false, true, true);

		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public List<String> getObjectList() {
		objectList = jJRightService.getTablesName();
		return objectList;
	}

	public void setObjectList(List<String> objectList) {
		this.objectList = objectList;
	}

	public void newRight() {
		System.out.println("Initial bean right");
		rightAdmin = new JJRight();
		rightAdmin.setR(false);
		rightAdmin.setW(false);
		rightAdmin.setX(false);

		category = null;
		object = null;

	}

	public void save() {
		System.out.println("SAVING Right...");
		String message = "";

		if (rightAdmin.getId() == null) {
			System.out.println("IS a new JJRight");
			rightAdmin.setCategory(category);
			rightAdmin.setObjet(object);
			jJRightService.saveJJRight(rightAdmin);

			message = "message_successfully_created";

			newRight();

		} else {
			jJRightService.updateJJRight(rightAdmin);

			message = "message_successfully_updated";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJRight");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		// System.out.println("rightAdmin.getR() " + rightAdmin.getR());
		// System.out.println("rightAdmin.getW() " + rightAdmin.getW());
		// System.out.println("rightAdmin.getX() " + rightAdmin.getX());
	}

	public void handleSelectCategory() {
		// System.out.println(category.getName());
	}

	public void handleSelectObject() {
		// System.out.println(object);
	}

}
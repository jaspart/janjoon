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
		categoryList = jJCategoryService.getAllJJCategories();

		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public void newRight() {
		System.out.println("Initial bean right");
		rightAdmin = new JJRight();
		rightAdmin.setR(false);
		rightAdmin.setW(false);
		rightAdmin.setX(false);
		rightListTable = null;
		category = null;

	}

	public void save() {
		System.out.println("SAVING Right...");
		String message = "";

		if (rightAdmin.getId() == null) {
			System.out.println("IS a new JJRight");
			rightAdmin.setCategory(category);
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
//		System.out.println("rightAdmin.getR() " + rightAdmin.getR());
//		System.out.println("rightAdmin.getW() " + rightAdmin.getW());
//		System.out.println("rightAdmin.getX() " + rightAdmin.getX());
	}

	public void handleSelectCategory() {
//		System.out.println(category.getName());
	}

}
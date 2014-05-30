package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJRight;

@RooSerializable
@RooJsfManagedBean(entity = JJRight.class, beanName = "jJRightBean")
public class JJRightBean {

	private JJRight rightAdmin;
	private List<RightDataModel> rightDataModel;

	private JJCategory category;
	private List<JJCategory> categories;

	private String object;
	private List<String> objects;

	private boolean checkRight;
	private boolean checkRights;
	private boolean disabledCheckRight;

	public JJRight getRightAdmin() {
		return rightAdmin;
	}

	public void setRightAdmin(JJRight rightAdmin) {
		this.rightAdmin = rightAdmin;
	}

	public List<RightDataModel> getRightDataModel() {
		return rightDataModel;
	}

	public void setRightDataModel(List<RightDataModel> rightDataModel) {
		this.rightDataModel = rightDataModel;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public List<JJCategory> getCategories() {
		return categories = jJCategoryService.getCategories(null, false, true,
				true);
	}

	public void setCategories(List<JJCategory> categories) {
		this.categories = categories;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public List<String> getObjects() {
		return objects = jJRightService.getTablesName();
	}

	public void setObjects(List<String> objects) {
		this.objects = objects;
	}

	public boolean getCheckRight() {
		return checkRight;
	}

	public void setCheckRight(boolean checkRight) {
		this.checkRight = checkRight;
	}

	public boolean getCheckRights() {
		return checkRights;
	}

	public void setCheckRights(boolean checkRights) {
		this.checkRights = checkRights;
	}

	public boolean getDisabledCheckRight() {
		return disabledCheckRight;
	}

	public void setDisabledCheckRight(boolean disabledCheckRight) {
		this.disabledCheckRight = disabledCheckRight;
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

	public void newRight() {

		rightAdmin = new JJRight();
		rightAdmin.setR(false);
		rightAdmin.setW(false);
		rightAdmin.setX(false);
		rightAdmin.setEnabled(true);

		category = null;
		object = null;
	}

	public void addRight() {

		if (rightDataModel == null) {
			rightDataModel = new ArrayList<RightDataModel>();
		}

		rightAdmin.setCategory(category);
		rightAdmin.setObjet(object);

		rightDataModel.add(new RightDataModel(rightAdmin, true, false));
		newRight();
	}

	public void fillRightTable(JJProfile profile) {
		rightDataModel = new ArrayList<RightDataModel>();

		if (profile == null) {
			System.out.println("null");
		} else {
			System.out.println("   sdfsdfsd " + profile.getId());
		}

		List<JJRight> rights = jJRightService.getRights(profile, true);

		System.out.println("tttt " + rights.size());

		for (JJRight right : rights) {
			rightDataModel.add(new RightDataModel(right, true, true));
		}

		checkRights = true;
	}

	public void checkRights() {

		for (RightDataModel rightModel : rightDataModel) {
			rightModel.setCheckRight(checkRights);
		}

	}

	public void checkRight() {

		boolean checkAll = true;
		for (RightDataModel rightModel : rightDataModel) {

			if (!rightModel.getCheckRight()) {
				checkAll = false;
				break;
			}

		}
		checkRights = checkAll;
	}

	public class RightDataModel {

		private JJRight right;
		private boolean checkRight;
		private boolean old;

		public RightDataModel(JJRight right, boolean checkRight, boolean old) {
			super();
			this.right = right;
			this.checkRight = checkRight;
			this.old = old;
		}

		public JJRight getRight() {
			return right;
		}

		public void setRight(JJRight right) {
			this.right = right;
		}

		public boolean getCheckRight() {
			return checkRight;
		}

		public void setCheckRight(boolean checkRight) {
			this.checkRight = checkRight;
		}

		public boolean isOld() {
			return old;
		}

		public void setOld(boolean old) {
			this.old = old;
		}

	}

}
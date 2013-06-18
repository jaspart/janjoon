package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;

@RooSerializable
@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean {

	private JJRequirement myJJRequirement;

	public JJRequirement getMyJJRequirement() {
		System.out.println("get invoked");
		return myJJRequirement;

	}

	public void setMyJJRequirement(JJRequirement myJJRequirement) {
		this.myJJRequirement = myJJRequirement;
	}

	public void createNewJJRequirement() {
		System.out.println("bean created");
		myJJRequirement = new JJRequirement();
		myJJRequirement.setCreationDate(new Date());
		myJJRequirement.setEnabled(true);
	}

	public void createNewBusinessJJRequirement() {
		createNewJJRequirement();
		List<JJCategory> JJCategoryList = jJCategoryService.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("BUSINESS")) {
					System.out.println("A JJCategory as name BUSINESS is founded");
					myJJCategory = jjCategory;
					break;
				}

			}
			if (myJJCategory == null)
				myJJCategory = createANDsaveJJCategory("BUSINESS");
		} else {
			System.out.println("The table JJCategory is empty");
			myJJCategory = createANDsaveJJCategory("BUSINESS");
		}
		myJJRequirement.setCategory(myJJCategory);
	}

	public void createNewFunctionalJJRequirement() {
		createNewJJRequirement();
		List<JJCategory> JJCategoryList = jJCategoryService.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("FUNCTIONAL")) {
					System.out.println("A JJCategory as name FUNCTIONAL is founded");
					myJJCategory = jjCategory;
					break;
				}

			}
			if (myJJCategory == null)
				myJJCategory = createANDsaveJJCategory("FUNCTIONAL");
		} else {
			System.out.println("The table JJCategory is empty");
			myJJCategory = createANDsaveJJCategory("FUNCTIONAL");
		}
		myJJRequirement.setCategory(myJJCategory);
	}

	public void createNewTechnicalJJRequirement() {
		createNewJJRequirement();
		List<JJCategory> JJCategoryList = jJCategoryService.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("TECHNICAL")) {
					System.out.println("A JJCategory as name TECHNICAL is founded");
					myJJCategory = jjCategory;
					break;
				}

			}
			if (myJJCategory == null)
				myJJCategory = createANDsaveJJCategory("TECHNICAL");
		} else {
			System.out.println("The table JJCategory is empty");
			myJJCategory = createANDsaveJJCategory("TECHNICAL");
		}
		myJJRequirement.setCategory(myJJCategory);
	}

	public void myPersist() {
		List<JJStatus> JJStatusList = jJStatusService.findAllJJStatuses();
		JJStatus myJJStatus = null;

		if (!JJStatusList.isEmpty()) {
			System.out.println("The table JJStatus is not empty");
			for (JJStatus jjStatus : JJStatusList) {
				if (jjStatus.getName().equalsIgnoreCase("NEW")) {
					System.out.println("A JJStatus as name NEW is founded");
					myJJStatus = jjStatus;
					break;
				}

			}
			if (myJJStatus == null)
				myJJStatus = createANDsaveJJStatus();
		} else {
			System.out.println("The table JJStatus is empty");
			myJJStatus = createANDsaveJJStatus();
		}

		myJJRequirement.setStatus(myJJStatus);
		jJRequirementService.saveJJRequirement(myJJRequirement);

		System.out.println("save done");
		findAllJJRequirements();
	}

	private JJStatus createANDsaveJJStatus() {
		JJStatus newJJStatus = new JJStatus();
		newJJStatus.setName("NEW");
		newJJStatus.setCreationDate(new Date());
		newJJStatus.setDescription("A Status defined as NEW");
		jJStatusService.saveJJStatus(newJJStatus);
		System.out.println("The Status is saved into the database");
		return newJJStatus;
	}
	
	private JJCategory createANDsaveJJCategory(String name){
		JJCategory newJJCategory = new JJCategory();
		newJJCategory.setName(name);
		newJJCategory.setCreationDate(new Date());
		newJJCategory.setDescription("A Category defined as "+name);
		jJCategoryService.saveJJCategory(newJJCategory);
		System.out.println("The category is saved into the database");
		return newJJCategory;
	}
	

}

package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.ui.mb.converter.JJChapterConverter;
import com.funder.janjoonweb.ui.mb.converter.JJProjectConverter;

@RooSerializable
@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean {
	
	

	private JJRequirement myBusinessJJRequirement;
	private JJRequirement myFunctionalJJRequirement;
	private JJRequirement myTechnicalJJRequirement;

	private List<JJRequirement> myBusinessJJRequirements;
	private List<JJRequirement> myFunctionalJJRequirements;
	private List<JJRequirement> myTechnicalJJRequirements;
	
	private JJProjectConverter projectConverter = new JJProjectConverter();
	private JJChapterConverter chapterConverter = new JJChapterConverter();
	
	
	public JJRequirement getMyBusinessJJRequirement() {
		System.out.println("get business req invoked");
		return myBusinessJJRequirement;
	}

	public void setMyBusinessJJRequirement(JJRequirement myBusinessJJRequirement) {
		this.myBusinessJJRequirement = myBusinessJJRequirement;
	}

	public JJRequirement getMyFunctionalJJRequirement() {
		System.out.println("get functional req invoked");
		return myFunctionalJJRequirement;
	}

	public void setMyFunctionalJJRequirement(
			JJRequirement myFunctionalJJRequirement) {
		this.myFunctionalJJRequirement = myFunctionalJJRequirement;
	}

	public JJRequirement getMyTechnicalJJRequirement() {
		System.out.println("get technical req invoked");
		return myTechnicalJJRequirement;
	}

	public void setMyTechnicalJJRequirement(
			JJRequirement myTechnicalJJRequirement) {
		this.myTechnicalJJRequirement = myTechnicalJJRequirement;
	}

	public List<JJRequirement> getMyBusinessJJRequirements() {
		return myBusinessJJRequirements;
	}

	public void setMyBusinessJJRequirements(
			List<JJRequirement> myBusinessJJRequirements) {
		this.myBusinessJJRequirements = myBusinessJJRequirements;
	}

	public List<JJRequirement> getMyFunctionalJJRequirements() {
		return myFunctionalJJRequirements;
	}

	public void setMyFunctionalJJRequirements(
			List<JJRequirement> myFunctionalJJRequirements) {
		this.myFunctionalJJRequirements = myFunctionalJJRequirements;
	}

	public List<JJRequirement> getMyTechnicalJJRequirements() {
		return myTechnicalJJRequirements;
	}

	public void setMyTechnicalJJRequirements(
			List<JJRequirement> myTechnicalJJRequirements) {
		this.myTechnicalJJRequirements = myTechnicalJJRequirements;
	}

	public JJProjectConverter getProjectConverter() {
		return projectConverter;
	}

	public void setProjectConverter(JJProjectConverter projectConverter) {
		this.projectConverter = projectConverter;
	}

	public JJChapterConverter getChapterConverter() {
		return chapterConverter;
	}

	public void setChapterConverter(JJChapterConverter chapterConverter) {
		this.chapterConverter = chapterConverter;
	}
	
	public void createNewBusinessJJRequirement() {
		
		
		JJProject project;
		for (int i = 0; i < 3; i++) {
			project = new JJProject();
			project.setName("ProjectName " + i);
			project.setDescription("ProjectDescription " + i);
			project.setCreationDate(new Date());
			jJProjectService.saveJJProject(project);
		}
		
		JJChapter chapter;
		for (int i = 0; i < 3; i++) {
			chapter = new JJChapter();
			chapter.setName("ChapterName " + i);
			chapter.setDescription("ChapterDescription " + i);
			chapter.setCreationDate(new Date());
			jJChapterService.saveJJChapter(chapter);
		}
		
		System.out.println("business req bean created");
		myBusinessJJRequirement = new JJRequirement();
		myBusinessJJRequirement.setCreationDate(new Date());
		myBusinessJJRequirement.setEnabled(true);

		List<JJCategory> JJCategoryList = jJCategoryService
				.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("BUSINESS")) {
					System.out
							.println("A JJCategory as name BUSINESS is founded");
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
		myBusinessJJRequirement.setCategory(myJJCategory);
	}

	public void createNewFunctionalJJRequirement() {
		System.out.println("functional req bean created");
		myFunctionalJJRequirement = new JJRequirement();
		myFunctionalJJRequirement.setCreationDate(new Date());
		myFunctionalJJRequirement.setEnabled(true);

		List<JJCategory> JJCategoryList = jJCategoryService
				.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("FUNCTIONAL")) {
					System.out
							.println("A JJCategory as name FUNCTIONAL is founded");
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
		myFunctionalJJRequirement.setCategory(myJJCategory);
	}

	public void createNewTechnicalJJRequirement() {
		System.out.println("technical req bean created");
		myTechnicalJJRequirement = new JJRequirement();
		myTechnicalJJRequirement.setCreationDate(new Date());
		myTechnicalJJRequirement.setEnabled(true);

		List<JJCategory> JJCategoryList = jJCategoryService
				.findAllJJCategorys();
		JJCategory myJJCategory = null;
		if (!JJCategoryList.isEmpty()) {
			System.out.println("The table JJCategory is not empty");
			for (JJCategory jjCategory : JJCategoryList) {
				if (jjCategory.getName().equalsIgnoreCase("TECHNICAL")) {
					System.out
							.println("A JJCategory as name TECHNICAL is founded");
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
		myTechnicalJJRequirement.setCategory(myJJCategory);
	}

	public void persistNewBusinessJJRequirement() {
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

		myBusinessJJRequirement.setStatus(myJJStatus);
		jJRequirementService.saveJJRequirement(myBusinessJJRequirement);

		System.out.println("save done");
		findAllBusinessJJRequirements();
	}

	public void persistNewFunctionalJJRequirement() {
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

		myFunctionalJJRequirement.setStatus(myJJStatus);
		jJRequirementService.saveJJRequirement(myFunctionalJJRequirement);

		System.out.println("save done");
		findAllFunctionalJJRequirements();
	}

	public void persistNewTechnicalJJRequirement() {
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

		myTechnicalJJRequirement.setStatus(myJJStatus);
		jJRequirementService.saveJJRequirement(myTechnicalJJRequirement);

		System.out.println("save done");
		findAllTechnicalJJRequirements();
	}

	public void findAllBusinessJJRequirements() {
		System.out.println("findAllBusinessJJRequirements is invoked");
		myBusinessJJRequirements = jJRequirementService
				.getAllSpecifiedJJRequirements("BUSINESS");
	}

	public void findAllFunctionalJJRequirements() {
		System.out.println("findAllFunctionalJJRequirements is invoked");
		myFunctionalJJRequirements = jJRequirementService
				.getAllSpecifiedJJRequirements("FUNCTIONAL");
	}

	public void findAllTechnicalJJRequirements() {
		System.out.println("findAllTechnicalJJRequirements is invoked");
		myTechnicalJJRequirements = jJRequirementService
				.getAllSpecifiedJJRequirements("TECHNICAL");
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

	private JJCategory createANDsaveJJCategory(String name) {
		JJCategory newJJCategory = new JJCategory();
		newJJCategory.setName(name);
		newJJCategory.setCreationDate(new Date());
		newJJCategory.setDescription("A Category defined as " + name);
		jJCategoryService.saveJJCategory(newJJCategory);
		System.out.println("The category is saved into the database");
		return newJJCategory;
	}

}

package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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

	private JJRequirement myJJRequirement;
	private JJRequirement myEditedJJRequirement;
	private JJRequirement mySelectedBusinessJJRequirement;
	private JJRequirement mySelectedFunctionalJJRequirement;
	private JJRequirement mySelectedTechnicalJJRequirement;

	private int creationColumnNumber;
	private int editionColumnNumber;
	private int deleteColumnNumber;

	private List<JJRequirement> myBusinessJJRequirements;
	private List<JJRequirement> myFunctionalJJRequirements;
	private List<JJRequirement> myTechnicalJJRequirements;

	private JJProjectConverter projectConverter = new JJProjectConverter();
	private JJChapterConverter chapterConverter = new JJChapterConverter();

	public JJRequirement getMyJJRequirement() {
		System.out.println("get req invoked");
		return myJJRequirement;
	}

	public void setMyJJRequirement(JJRequirement myJJRequirement) {
		this.myJJRequirement = myJJRequirement;
	}

	public JJRequirement getMyEditedJJRequirement() {
		return myEditedJJRequirement;
	}

	public void setMyEditedJJRequirement(JJRequirement myEditedJJRequirement) {
		this.myEditedJJRequirement = myEditedJJRequirement;
	}

	public JJRequirement getMySelectedBusinessJJRequirement() {
		return mySelectedBusinessJJRequirement;
	}

	public void setMySelectedBusinessJJRequirement(
			JJRequirement mySelectedBusinessJJRequirement) {
		this.mySelectedBusinessJJRequirement = mySelectedBusinessJJRequirement;
	}

	public JJRequirement getMySelectedFunctionalJJRequirement() {
		return mySelectedFunctionalJJRequirement;
	}

	public void setMySelectedFunctionalJJRequirement(
			JJRequirement mySelectedFunctionalJJRequirement) {
		this.mySelectedFunctionalJJRequirement = mySelectedFunctionalJJRequirement;
	}

	public JJRequirement getMySelectedTechnicalJJRequirement() {
		return mySelectedTechnicalJJRequirement;
	}

	public void setMySelectedTechnicalJJRequirement(
			JJRequirement mySelectedTechnicalJJRequirement) {
		this.mySelectedTechnicalJJRequirement = mySelectedTechnicalJJRequirement;
	}

	public int getCreationColumnNumber() {
		return creationColumnNumber;
	}

	public void setCreationColumnNumber(int creationColumnNumber) {
		this.creationColumnNumber = creationColumnNumber;
	}

	public int getEditionColumnNumber() {
		return editionColumnNumber;
	}

	public void setEditionColumnNumber(int editionColumnNumber) {
		this.editionColumnNumber = editionColumnNumber;
	}

	public int getDeleteColumnNumber() {
		return deleteColumnNumber;
	}

	public void setDeleteColumnNumber(int deleteColumnNumber) {
		this.deleteColumnNumber = deleteColumnNumber;
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

	public void createJJRequirement(int number) {
		List<JJProject> JJProjectList = jJProjectService.findAllJJProjects();
		if (JJProjectList.isEmpty()) {
			JJProject project;
			for (int i = 0; i < 3; i++) {
				project = new JJProject();
				project.setName("ProjectName " + i);
				project.setDescription("ProjectDescription " + i);
				project.setCreationDate(new Date());
				jJProjectService.saveJJProject(project);
			}
		}

		List<JJChapter> JJChapterList = jJChapterService.findAllJJChapters();
		if (JJChapterList.isEmpty()) {
			JJChapter chapter;
			for (int i = 0; i < 3; i++) {
				chapter = new JJChapter();
				chapter.setName("ChapterName " + i);
				chapter.setDescription("ChapterDescription " + i);
				chapter.setCreationDate(new Date());
				jJChapterService.saveJJChapter(chapter);
			}
		}

		System.out.println("JJRequirement bean created");
		myJJRequirement = new JJRequirement();
		myJJRequirement.setCreationDate(new Date());
		myJJRequirement.setEnabled(true);

		this.creationColumnNumber = number;
		String category = null;
		switch (number) {
		case 1:
			category = "BUSINESS";
			break;
		case 2:
			category = "FUNCTIONAL";
			break;
		case 3:
			category = "TECHNICAL";
			break;

		default:
			break;
		}

		JJCategory myJJCategory = null;

		List<JJCategory> JJCategoryList = jJCategoryService
				.getJJCategoryWithName(category);

		if (!JJCategoryList.isEmpty()) {
			for (JJCategory jjCategory : JJCategoryList) {
				myJJCategory = jjCategory;
				break;
			}

		} else
			myJJCategory = createANDpersistJJCategory(category);

		myJJRequirement.setCategory(myJJCategory);

		JJStatus myJJStatus = null;

		List<JJStatus> JJStatusList = jJStatusService
				.getJJStatusWithName("NEW");

		if (!JJStatusList.isEmpty()) {
			for (JJStatus jjStatus : JJStatusList) {
				myJJStatus = jjStatus;
				break;
			}

		} else
			myJJStatus = createANDpersistJJStatus("NEW");

		myJJRequirement.setStatus(myJJStatus);
		myJJRequirement.setNumero(new Random().nextInt(1000) + 1);

	}

	public void editJJRequirement(int number) {
		this.editionColumnNumber = number;
		JJRequirement req = new JJRequirement();
		switch (number) {
		case 1:
			req = mySelectedBusinessJJRequirement;
			break;
		case 2:
			req = mySelectedFunctionalJJRequirement;
			break;

		case 3:
			req = mySelectedTechnicalJJRequirement;
			break;

		default:
			break;
		}
		System.out.println("JJRequiremnt selected is :" + req.getId());

		System.out.println("EditJJRequirement bean created");

		myEditedJJRequirement = new JJRequirement();
		myEditedJJRequirement.setCreationDate(req.getCreationDate());
		myEditedJJRequirement.setUpdatedDate(new Date());
		myEditedJJRequirement.setEnabled(true);
		myEditedJJRequirement.setCategory(req.getCategory());

		JJStatus myJJStatus = null;

		List<JJStatus> JJStatusList = jJStatusService
				.getJJStatusWithName("MODIFY");

		if (!JJStatusList.isEmpty()) {
			for (JJStatus jjStatus : JJStatusList) {
				myJJStatus = jjStatus;
				break;
			}

		} else
			myJJStatus = createANDpersistJJStatus("MODIFY");

		myEditedJJRequirement.setStatus(myJJStatus);
		myEditedJJRequirement.setNumero(req.getNumero());
		myEditedJJRequirement.setProject(req.getProject());
		myEditedJJRequirement.setChapter(req.getChapter());
		myEditedJJRequirement.setDescription(req.getDescription());
		myEditedJJRequirement.setName(req.getName());
		myEditedJJRequirement.setNote(req.getNote());

	}

	public void persistJJRequirement(int index) {

		if (index == 1) {
			jJRequirementService.saveJJRequirement(myJJRequirement);
			findAllJJRequirementsWithCategory(creationColumnNumber);
		} else if (index == 2) {
			switch (editionColumnNumber) {
			case 1:
				mySelectedBusinessJJRequirement.setEnabled(false);
				jJRequirementService
						.updateJJRequirement(mySelectedBusinessJJRequirement);
				break;
			case 2:
				mySelectedFunctionalJJRequirement.setEnabled(false);
				jJRequirementService
						.updateJJRequirement(mySelectedFunctionalJJRequirement);
				break;
			case 3:
				mySelectedTechnicalJJRequirement.setEnabled(false);
				jJRequirementService
						.updateJJRequirement(mySelectedTechnicalJJRequirement);
				break;

			default:
				break;
			}

			jJRequirementService.saveJJRequirement(myEditedJJRequirement);
			findAllJJRequirementsWithCategory(editionColumnNumber);
		} else {
			switch (deleteColumnNumber) {
			case 1:

				jJRequirementService
						.updateJJRequirement(mySelectedBusinessJJRequirement);
				break;
			case 2:

				jJRequirementService
						.updateJJRequirement(mySelectedFunctionalJJRequirement);
				break;
			case 3:

				jJRequirementService
						.updateJJRequirement(mySelectedTechnicalJJRequirement);
				break;

			default:
				break;
			}

			findAllJJRequirementsWithCategory(deleteColumnNumber);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"This JJRequirement is deleted.", "Delete Status");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void deleteJJRequirement(int number) {
		this.deleteColumnNumber = number;

		JJStatus myJJStatus = null;

		List<JJStatus> JJStatusList = jJStatusService
				.getJJStatusWithName("DELETED");

		if (!JJStatusList.isEmpty()) {
			for (JJStatus jjStatus : JJStatusList) {
				myJJStatus = jjStatus;
				break;
			}

		} else
			myJJStatus = createANDpersistJJStatus("DELETED");

		switch (number) {
		case 1:
			mySelectedBusinessJJRequirement.setEnabled(false);
			mySelectedBusinessJJRequirement.setStatus(myJJStatus);
			break;
		case 2:
			mySelectedFunctionalJJRequirement.setEnabled(false);
			mySelectedFunctionalJJRequirement.setStatus(myJJStatus);
			break;

		case 3:
			mySelectedTechnicalJJRequirement.setEnabled(false);
			mySelectedTechnicalJJRequirement.setStatus(myJJStatus);
			break;

		default:
			break;
		}
	}

	public void deleteJJRequirementMessage(ActionEvent actionEvent) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Delete Status", "This JJRequirement is deleted.");

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void findAllJJRequirementsWithCategory(int number) {
		System.out.println("findAllJJRequirementsWithCategory is invoked");
		switch (number) {
		case 1:
			myBusinessJJRequirements = jJRequirementService
					.getAllJJRequirementsWithCategory("BUSINESS");
			break;
		case 2:
			myFunctionalJJRequirements = jJRequirementService
					.getAllJJRequirementsWithCategory("FUNCTIONAL");
			break;
		case 3:
			myTechnicalJJRequirements = jJRequirementService
					.getAllJJRequirementsWithCategory("TECHNICAL");
			break;
		default:
			break;
		}

	}

	private JJStatus createANDpersistJJStatus(String name) {
		JJStatus newJJStatus = new JJStatus();
		newJJStatus.setName(name);
		newJJStatus.setCreationDate(new Date());
		newJJStatus.setDescription("A JJStatus defined as " + name);
		newJJStatus.setEnabled(true);
		jJStatusService.saveJJStatus(newJJStatus);
		return newJJStatus;
	}

	private JJCategory createANDpersistJJCategory(String name) {
		JJCategory newJJCategory = new JJCategory();
		newJJCategory.setName(name);
		newJJCategory.setCreationDate(new Date());
		newJJCategory.setDescription("A JJCategory defined as " + name);
		newJJCategory.setEnabled(true);
		jJCategoryService.saveJJCategory(newJJCategory);
		return newJJCategory;
	}

}
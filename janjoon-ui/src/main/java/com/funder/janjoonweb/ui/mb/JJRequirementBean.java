package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;

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
	private int releaseColumnNumber;
	private int discardColumnNumber;

	/**
	 * Tables's Lists
	 */
	private List<JJRequirement> myBusinessJJRequirements;
	private List<JJRequirement> myFunctionalJJRequirements;
	private List<JJRequirement> myTechnicalJJRequirements;

	/**
	 * Menus's Lists
	 */
	private List<JJRequirement> businessJJRequirementsList;
	private List<JJRequirement> selectedBusinessJJRequirements;

	private List<JJRequirement> functionalJJRequirementsList;
	private List<JJRequirement> selectedFunctionalJJRequirements;

	private List<JJRequirement> technicalJJRequirementsList;
	private List<JJRequirement> selectedTechnicalJJRequirements;

	private boolean disabled;
	private String messageRelease;
	private String messageDiscard;

	private JJProject currentProject;

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

	public int getReleaseColumnNumber() {
		return releaseColumnNumber;
	}

	public void setReleaseColumnNumber(int releaseColumnNumber) {
		this.releaseColumnNumber = releaseColumnNumber;
	}

	public int getDiscardColumnNumber() {
		return discardColumnNumber;
	}

	public void setDiscardColumnNumber(int discardColumnNumber) {
		this.discardColumnNumber = discardColumnNumber;
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

	public List<JJRequirement> getBusinessJJRequirementsList() {
		businessJJRequirementsList = jJRequirementService
				.getAllJJRequirementsWithCategory("BUSINESS");
		return businessJJRequirementsList;
	}

	public void setBusinessJJRequirementsList(
			List<JJRequirement> businessJJRequirementsList) {
		this.businessJJRequirementsList = businessJJRequirementsList;
	}

	public List<JJRequirement> getSelectedBusinessJJRequirements() {
		return selectedBusinessJJRequirements;
	}

	public void setSelectedBusinessJJRequirements(
			List<JJRequirement> selectedBusinessJJRequirements) {
		this.selectedBusinessJJRequirements = selectedBusinessJJRequirements;
	}

	public List<JJRequirement> getFunctionalJJRequirementsList() {
		functionalJJRequirementsList = jJRequirementService
				.getAllJJRequirementsWithCategory("FUNCTIONAL");
		return functionalJJRequirementsList;
	}

	public void setFunctionalJJRequirementsList(
			List<JJRequirement> functionalJJRequirementsList) {
		this.functionalJJRequirementsList = functionalJJRequirementsList;
	}

	public List<JJRequirement> getSelectedFunctionalJJRequirements() {
		return selectedFunctionalJJRequirements;
	}

	public void setSelectedFunctionalJJRequirements(
			List<JJRequirement> selectedFunctionalJJRequirements) {
		this.selectedFunctionalJJRequirements = selectedFunctionalJJRequirements;
	}

	public List<JJRequirement> getTechnicalJJRequirementsList() {
		technicalJJRequirementsList = jJRequirementService
				.getAllJJRequirementsWithCategory("TECHNICAL");
		return technicalJJRequirementsList;
	}

	public void setTechnicalJJRequirementsList(
			List<JJRequirement> technicalJJRequirementsList) {
		this.technicalJJRequirementsList = technicalJJRequirementsList;
	}

	public List<JJRequirement> getSelectedTechnicalJJRequirements() {
		return selectedTechnicalJJRequirements;
	}

	public void setSelectedTechnicalJJRequirements(
			List<JJRequirement> selectedTechnicalJJRequirements) {
		this.selectedTechnicalJJRequirements = selectedTechnicalJJRequirements;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getMessageRelease() {
		return messageRelease;
	}

	public void setMessageRelease(String messageRelease) {
		this.messageRelease = messageRelease;
	}

	public String getMessageDiscard() {
		return messageDiscard;
	}

	public void setMessageDiscard(String messageDiscard) {
		this.messageDiscard = messageDiscard;
	}

	public JJProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(JJProject currentProject) {
		this.currentProject = currentProject;
	}

	public void createJJRequirement(int number) {
		System.out.println("JJRequirement bean created");
		// System.out.println("businessJJRequirementsList.size() "
		// + businessJJRequirementsList.size());
		// functionalJJRequirementsList;
		// technicalJJRequirementsList;
		//
		// if (currentProject != null)
		// System.out.println("Current project " + currentProject.getId());
		//
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

			Set<JJRequirement> requirementsLink = new HashSet<JJRequirement>();
			List<JJRequirement> tempList = new ArrayList<JJRequirement>();

			if (selectedBusinessJJRequirements.size() > 0) {
				for (JJRequirement req : selectedBusinessJJRequirements)
					req.setRequirementLink(myJJRequirement);
				tempList.addAll(tempList.size(), selectedBusinessJJRequirements);
			}

			if (selectedFunctionalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedFunctionalJJRequirements)
					req.setRequirementLink(myJJRequirement);
				tempList.addAll(tempList.size(),
						selectedFunctionalJJRequirements);
			}

			if (selectedTechnicalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedTechnicalJJRequirements)
					req.setRequirementLink(myJJRequirement);
				tempList.addAll(tempList.size(),
						selectedTechnicalJJRequirements);
			}

			requirementsLink.addAll(tempList);
			myJJRequirement.setRequirementsLink(requirementsLink);

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
			System.out.println("******************************* "
					+ deleteColumnNumber);
			switch (deleteColumnNumber) {
			case 1:
				System.out.println(";;;;;;;;;;;;;;;;;;;;;;;");
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

		System.out.println("$$$$$$$$$$$$$$$$$$$number " + number);
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

	public void releaseJJRequirement() {
		List<JJRequirement> reqList = null;

		JJStatus myJJStatus = null;

		List<JJStatus> JJStatusList = jJStatusService
				.getJJStatusWithName("RELEASE");

		if (!JJStatusList.isEmpty()) {
			for (JJStatus jjStatus : JJStatusList) {
				myJJStatus = jjStatus;
				break;
			}

		} else
			myJJStatus = createANDpersistJJStatus("RELEASE");

		switch (releaseColumnNumber) {
		case 1:
			reqList = myBusinessJJRequirements;
			break;

		case 2:
			reqList = myFunctionalJJRequirements;
			break;

		case 3:
			reqList = myTechnicalJJRequirements;
			break;

		default:
			break;
		}

		for (JJRequirement jjRequirement : reqList) {
			if (!jjRequirement.getStatus().getName()
					.equalsIgnoreCase("RELEASE")) {
				jjRequirement.setStatus(myJJStatus);
				jJRequirementService.updateJJRequirement(jjRequirement);
			}

		}
		findAllJJRequirementsWithCategory(releaseColumnNumber);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"All JJRequirements are released.", "Release Status");

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void discardJJRequirement() {

		JJStatus myJJStatus = null;

		List<JJStatus> JJStatusList = jJStatusService
				.getJJStatusWithName("DISCARD");

		if (!JJStatusList.isEmpty()) {
			for (JJStatus jjStatus : JJStatusList) {
				myJJStatus = jjStatus;
				break;
			}

		} else
			myJJStatus = createANDpersistJJStatus("DISCARD");

		JJRequirement req = null;

		switch (discardColumnNumber) {
		case 1:

			mySelectedBusinessJJRequirement.setEnabled(false);
			mySelectedBusinessJJRequirement.setStatus(myJJStatus);
			req = mySelectedBusinessJJRequirement;
			break;
		case 2:
			mySelectedFunctionalJJRequirement.setEnabled(false);
			mySelectedFunctionalJJRequirement.setStatus(myJJStatus);
			req = mySelectedFunctionalJJRequirement;
			break;

		case 3:
			mySelectedTechnicalJJRequirement.setEnabled(false);
			mySelectedTechnicalJJRequirement.setStatus(myJJStatus);
			req = mySelectedTechnicalJJRequirement;
			break;

		default:
			break;
		}

		jJRequirementService.updateJJRequirement(req);

		findAllJJRequirementsWithCategory(discardColumnNumber);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"This requirement is discarded.", "Discard Status");

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void onRowSelect(SelectEvent event) {
		JJRequirement req = (JJRequirement) event.getObject();

		FacesMessage msg = new FacesMessage("JJRequirement Selected "
				+ req.getName(), req.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);

		System.out.println("\n" + req.getStatus().getName());
		if (req.getStatus().getName().equalsIgnoreCase("RELEASE"))
			disabled = true;
		else
			disabled = false;
	}

	public void updateMessageRelease(int number) {
		this.releaseColumnNumber = number;
		switch (number) {
		case 1:
			messageRelease = "Are you sure you want to release all Business JJRequirements";
			break;

		case 2:
			messageRelease = "Are you sure you want to release all Functional JJRequirements";
			break;
		case 3:
			messageRelease = "Are you sure you want to release all Technical JJRequirements";
			break;
		default:
			break;
		}
	}

	public void updateMessageDiscard(int number) {
		this.discardColumnNumber = number;
		messageDiscard = "Are you sure you want to discard this requirement";
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
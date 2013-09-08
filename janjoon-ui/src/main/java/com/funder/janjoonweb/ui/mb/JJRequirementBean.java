package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import java.io.*;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTaskService;
import com.funder.janjoonweb.domain.JJVersion;

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

	private List<JJTask> tasksList;
	private List<JJTask> selectedTasksList;

	private boolean disabled;

	private String messageRelease;
	private String messageDiscard;

	private JJProject currentProject;
	private JJProduct currentProduct;
	private JJVersion currentVersion;

	private int progressLeft = 0;
	private int progressMiddle = 0;
	private int progressRight = 0;

	private int coverageProgressLeft = 0;
	private int coverageProgressMiddle = 0;
	private int coverageProgressRight = 0;

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public JJRequirement getMyJJRequirement() {

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
		if (currentProject != null)
			if (currentProduct != null)
				if (currentVersion != null)

					businessJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"BUSINESS", currentProject, currentProduct,
									currentVersion);
				else

					businessJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"BUSINESS", currentProject, currentProduct);
			else
				businessJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithProject("BUSINESS",
								currentProject);
		else
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

		if (currentProject != null)
			if (currentProduct != null)
				if (currentVersion != null)

					functionalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"FUNCTIONAL", currentProject,
									currentProduct, currentVersion);

				else

					functionalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"FUNCTIONAL", currentProject,
									currentProduct);

			else
				functionalJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithProject("FUNCTIONAL",
								currentProject);
		else

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

		if (currentProject != null)
			if (currentProduct != null)
				if (currentVersion != null)

					technicalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"TECHNICAL", currentProject,
									currentProduct, currentVersion);

				else

					technicalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"TECHNICAL", currentProject, currentProduct);
			else
				technicalJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithProject("TECHNICAL",
								currentProject);
		else

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

	public List<JJTask> getTasksList() {
		tasksList = jJTaskService.findAllJJTasks();
		return tasksList;
	}

	public void setTasksList(List<JJTask> tasksList) {
		this.tasksList = tasksList;
	}

	public List<JJTask> getSelectedTasksList() {
		return selectedTasksList;
	}

	public void setSelectedTasksList(List<JJTask> selectedTasksList) {
		this.selectedTasksList = selectedTasksList;
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

	public JJProduct getCurrentProduct() {
		return currentProduct;
	}

	public void setCurrentProduct(JJProduct currentProduct) {
		this.currentProduct = currentProduct;
	}

	public JJVersion getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(JJVersion currentVersion) {
		this.currentVersion = currentVersion;
	}

	public int getProgressLeft() {

		if (myBusinessJJRequirements != null) {

			if (myBusinessJJRequirements.size() > 0) {
				int numberCompleted = 0;
				for (JJRequirement req : myBusinessJJRequirements) {

					req = jJRequirementService.findJJRequirement(req.getId());
					Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
					requirementsDown = req.getRequirementLinkDown();
					List<JJRequirement> tempList = new ArrayList<JJRequirement>();
					for (JJRequirement jjRequirement : requirementsDown) {
						if (jjRequirement.getCategory().getName()
								.equalsIgnoreCase("FUNCTIONAL"))
							tempList.add(jjRequirement);
					}

					if (tempList.size() > 0) {
						int numberFuncCompleted = 0;
						for (JJRequirement jjreq : tempList) {

							jjreq = jJRequirementService
									.findJJRequirement(jjreq.getId());
							Set<JJRequirement> requirements1Down = new HashSet<JJRequirement>();
							requirements1Down = jjreq.getRequirementLinkDown();
							List<JJRequirement> tempList2 = new ArrayList<JJRequirement>();
							for (JJRequirement jjRequirement : requirements1Down) {
								if (jjRequirement.getCategory().getName()
										.equalsIgnoreCase("TECHNICAL"))
									tempList2.add(jjRequirement);
							}

							if (tempList2.size() > 0) {
								int numberTechCompleted = 0;
								for (JJRequirement jjRequirement : tempList2) {
									if (jjRequirement.getIsCompleted().equals(
											true)) {
										numberTechCompleted++;
									}
								}

								if (numberTechCompleted == tempList2.size())
									numberFuncCompleted++;
							}

						}
						if (numberFuncCompleted == tempList.size())
							numberCompleted++;
					}

				}

				progressLeft = (numberCompleted * 100)
						/ myBusinessJJRequirements.size();

				return progressLeft;

			} else
				return 0;
		} else
			return 0;
	}

	public void setProgressLeft(int progressLeft) {
		this.progressLeft = progressLeft;
	}

	public int getProgressMiddle() {
		if (myFunctionalJJRequirements != null) {

			if (myFunctionalJJRequirements.size() > 0) {
				int numberCompleted = 0;
				for (JJRequirement req : myFunctionalJJRequirements) {

					req = jJRequirementService.findJJRequirement(req.getId());
					Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
					requirementsDown = req.getRequirementLinkDown();
					List<JJRequirement> tempList = new ArrayList<JJRequirement>();
					for (JJRequirement jjRequirement : requirementsDown) {
						if (jjRequirement.getCategory().getName()
								.equalsIgnoreCase("TECHNICAL"))
							tempList.add(jjRequirement);
					}

					if (tempList.size() > 0) {
						int numberTechCompleted = 0;
						for (JJRequirement jjreq : tempList) {
							if (jjreq.getIsCompleted().equals(true)) {
								numberTechCompleted++;
							}

						}
						if (numberTechCompleted == tempList.size())
							numberCompleted++;
					}

				}

				progressMiddle = (numberCompleted * 100)
						/ myFunctionalJJRequirements.size();

				return progressMiddle;

			} else
				return 0;
		} else
			return 0;
	}

	public void setProgressMiddle(int progressMiddle) {
		this.progressMiddle = progressMiddle;
	}

	public int getProgressRight() {
		if (myTechnicalJJRequirements != null) {
			if (myTechnicalJJRequirements.size() > 0) {
				int numberCompleted = 0;

				for (JJRequirement jjreq : myTechnicalJJRequirements) {
					if (jjreq.getIsCompleted().equals(true)) {
						numberCompleted++;
					}
				}

				progressRight = (numberCompleted * 100)
						/ myTechnicalJJRequirements.size();

				return progressRight;
			} else
				return 0;
		} else
			return 0;

	}

	public void setProgressRight(int progressRight) {
		this.progressRight = progressRight;
	}

	public int getCoverageProgressLeft() {

		if (myBusinessJJRequirements != null) {
			if (myBusinessJJRequirements.size() > 0) {
				int numberCompleted = 0;

				for (JJRequirement jjreq : myBusinessJJRequirements) {
					jjreq = jJRequirementService.findJJRequirement(jjreq
							.getId());
					Set<JJRequirement> listLinkDown = jjreq
							.getRequirementLinkDown();
					for (JJRequirement jjRequirement : listLinkDown) {
						if (jjRequirement.getCategory().getName()
								.equalsIgnoreCase("FUNCTIONAL")) {
							numberCompleted++;
							break;
						}

					}
				}

				coverageProgressLeft = (numberCompleted * 100)
						/ myBusinessJJRequirements.size();

				return coverageProgressLeft;
			} else
				return 0;
		} else
			return 0;
	}

	public void setCoverageProgressLeft(int coverageProgressLeft) {
		this.coverageProgressLeft = coverageProgressLeft;
	}

	public int getCoverageProgressMiddle() {

		if (myFunctionalJJRequirements != null) {
			if (myFunctionalJJRequirements.size() > 0) {
				int numberCompleted = 0;

				for (JJRequirement jjreq : myFunctionalJJRequirements) {
					jjreq = jJRequirementService.findJJRequirement(jjreq
							.getId());
					Set<JJRequirement> listLinkDown = jjreq
							.getRequirementLinkDown();
					for (JJRequirement jjRequirement : listLinkDown) {
						if (jjRequirement.getCategory().getName()
								.equalsIgnoreCase("TECHNICAL")) {
							numberCompleted++;
							break;
						}

					}
				}

				coverageProgressMiddle = (numberCompleted * 100)
						/ myFunctionalJJRequirements.size();

				return coverageProgressMiddle;
			} else
				return 0;
		} else
			return 0;

	}

	public void setCoverageProgressMiddle(int coverageProgressMiddle) {
		this.coverageProgressMiddle = coverageProgressMiddle;
	}

	public int getCoverageProgressRight() {

		if (myTechnicalJJRequirements != null) {
			if (myTechnicalJJRequirements.size() > 0) {
				int numberCompleted = 0;

				for (JJRequirement jjreq : myTechnicalJJRequirements) {
					jjreq = jJRequirementService.findJJRequirement(jjreq
							.getId());
					Set<JJTask> tasks = jjreq.getTasks();
					if (!tasks.isEmpty())
						numberCompleted++;
				}

				coverageProgressRight = (numberCompleted * 100)
						/ myTechnicalJJRequirements.size();

				return coverageProgressRight;
			} else
				return 0;
		} else
			return 0;

	}

	public void setCoverageProgressRight(int coverageProgressRight) {
		this.coverageProgressRight = coverageProgressRight;
	}

	public void createJJRequirement(int number) {

		myJJRequirement = new JJRequirement();
		myJJRequirement.setCreationDate(new Date());
		myJJRequirement.setEnabled(true);
		myJJRequirement.setIsCompleted(false);

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

		JJCategory jjCategory = jJCategoryService.getJJCategoryWithName(category);

		if (jjCategory != null)
			myJJCategory = jjCategory;
		else
			myJJCategory = createANDpersistJJCategory(category);

		myJJRequirement.setCategory(myJJCategory);

		JJStatus myJJStatus = null;

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("NEW");

		if (jjStatus != null)
			myJJStatus = jjStatus;
		else
			myJJStatus = createANDpersistJJStatus("NEW");

		myJJRequirement.setStatus(myJJStatus);
		myJJRequirement.setNumero(new Random().nextInt(1000) + 1);
		if (currentProject != null)
			myJJRequirement.setProject(currentProject);
		if (currentProduct != null)
			myJJRequirement.setProduct(currentProduct);

		myJJRequirement.setChapter(null);

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

		myEditedJJRequirement = new JJRequirement();
		myEditedJJRequirement.setCreationDate(req.getCreationDate());
		myEditedJJRequirement.setUpdatedDate(new Date());
		myEditedJJRequirement.setEnabled(true);
		myEditedJJRequirement.setCategory(req.getCategory());

		JJStatus myJJStatus = null;

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("MODIFY");

		if (jjStatus != null)
			myJJStatus = jjStatus;
		else
			myJJStatus = createANDpersistJJStatus("MODIFY");

		myEditedJJRequirement.setStatus(myJJStatus);
		myEditedJJRequirement.setNumero(req.getNumero());
		myEditedJJRequirement.setProject(req.getProject());
		myEditedJJRequirement.setChapter(req.getChapter());
		myEditedJJRequirement.setProduct(req.getProduct());
		myEditedJJRequirement.setDescription(req.getDescription());
		myEditedJJRequirement.setName(req.getName());
		myEditedJJRequirement.setNote(req.getNote());

		req = jJRequirementService.findJJRequirement(req.getId());

		Set<JJRequirement> requirementsDown = req.getRequirementLinkDown();
		System.out.println("AJA LOG == requirementsDown == ");
		for (JJRequirement jjRequirement : requirementsDown) {
			System.out.println("AJA LOG requirementsDown : "+jjRequirement.getName());
			if (jjRequirement.getCategory().getName().equalsIgnoreCase("BUSINESS")){
				selectedBusinessJJRequirements.add(jjRequirement);
			}
			else if (jjRequirement.getCategory().getName().equalsIgnoreCase("FUNCTIONAL")){
				selectedFunctionalJJRequirements.add(jjRequirement);
			}
			else if (jjRequirement.getCategory().getName().equalsIgnoreCase("TECHNICAL")){
				selectedTechnicalJJRequirements.add(jjRequirement);
			}
		}
	}

	public void persistJJRequirement(int index) throws Exception {
		Set<JJRequirement> tempList1 = new HashSet<JJRequirement>();
		Set<JJRequirement> tempList2 = new HashSet<JJRequirement>();
		System.out.println("AJA LOG persistJJRequirement == ");
		if(selectedBusinessJJRequirements != null){
			if (selectedBusinessJJRequirements.size() > 0) {
				for (JJRequirement req : selectedBusinessJJRequirements) {
					System.out.println("AJA LOG selectedBusinessJJRequirements : "+req.getName());
					//req.getRequirementLinkUp().add(myJJRequirement);
					//myJJRequirement.getRequirementLinkUp().add(req);

				//	req.getRequirementLinkUp().add(myJJRequirement);
				//	myJJRequirement.getRequirementLinkUp().add(req);

					tempList1.add(req);
					System.out.println("AJA LOG tempList1 : "+tempList1.contains(req));
					//myJJRequirement.getRequirementLinkDown().add(req);
				}
				myJJRequirement.setRequirementLinkDown(tempList1);
				for (JJRequirement req : myJJRequirement.getRequirementLinkDown()) {
					System.out.println("AJA LOG getreq : "+req.getName());
				}
				//myJJRequirement.setRequirementLinkUp(tempList1);
			//	req.getSelectedBusinessJJRequirements().add(myJJRequirement);
			//	myJJRequirement.getSelectedBusinessJJRequirements().add(req);
			}
			else{
				System.out.println("No selectedBusinessJJRequirements! isEmpty pointer.");
			}
		}
		else{
			System.out.println("No selectedBusinessJJRequirements! null pointer.");
		}
		System.out.println("Saving Requirement. (if no message after this one, then it means that the saving crashed!)");		
		jJRequirementService.saveJJRequirement(myJJRequirement);
		System.out.println("Requirement Saving process. (if no message after this one, then it means that the findall crashed!)");
		findAllJJRequirementsWithCategory(creationColumnNumber);
		System.out.println("Requirement Saved.");		

		System.out.println("AJA saveJJRequirement == ");
		for(JJRequirement req : jJRequirementService.getAllJJRequirementsWithCategory("BUSINESS")){
			for (JJRequirement down : req.getRequirementLinkDown()) {
				System.out.println("AJA down : "+down.getName());
			}
			for (JJRequirement up : req.getRequirementLinkUp()) {
				System.out.println("AJA up : "+up.getName());
			}
		}

		findAllJJRequirementsWithCategory(creationColumnNumber);



//		if (index == 1) {

/*			Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
			List<JJRequirement> tempList = new ArrayList<JJRequirement>();

			if(selectedBusinessJJRequirements != null)
			if (selectedBusinessJJRequirements.size() > 0) {
				for (JJRequirement req : selectedBusinessJJRequirements) {
					System.out.println("AJA LOG getreq : "+req.getName());
					req = jJRequirementService.findJJRequirement(req.getId());
					System.out.println("AJA LOG getreq : "+req.getName());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedBusinessJJRequirements);
			}

			if(selectedFunctionalJJRequirements != null)
			if (selectedFunctionalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedFunctionalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedFunctionalJJRequirements);
			}

			if(selectedTechnicalJJRequirements != null)
			if (selectedTechnicalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedTechnicalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedTechnicalJJRequirements);
			}

			requirementsDown.addAll(tempList);

			myJJRequirement.setRequirementLinkDown(requirementsDown);

			if (creationColumnNumber == 3) {
				if (selectedTasksList.size() > 0) {
//					for (JJTask task : selectedTasksList) {
//						task.setRequirement(myJJRequirement);
//						task.setStartDate(new Date());
//						jJTaskService.updateJJTask(task);
//					}
					Set<JJTask> tasks = new HashSet<JJTask>();
					tasks.addAll(selectedTasksList);
					myJJRequirement.setTasks(tasks);
				}
			}

			//jJRequirementService.saveJJRequirement(myJJRequirement);
			if (creationColumnNumber == 3) {
				if (selectedTasksList.size() > 0) {
					for (JJTask task : selectedTasksList) {
						task.setRequirement(myJJRequirement);
						task.setStartDate(new Date());
						jJTaskService.updateJJTask(task);
					}
				}
			}

			jJRequirementService.saveJJRequirement(myJJRequirement);

			System.out.println("AJA saveJJRequirement == ");
			for(JJRequirement req : jJRequirementService.getAllJJRequirementsWithCategory("BUSINESS")){
				for (JJRequirement down : req.getRequirementLinkDown()) {
					System.out.println("AJA down : "+down.getName());
				}
				for (JJRequirement up : req.getRequirementLinkUp()) {
					System.out.println("AJA up : "+up.getName());
				}
			}

			findAllJJRequirementsWithCategory(creationColumnNumber);
*//*		} else if (index == 2) {

			switch (editionColumnNumber) {
			case 1:
				mySelectedBusinessJJRequirement.setEnabled(false);
				jJRequirementService.updateJJRequirement(mySelectedBusinessJJRequirement);
				break;
			case 2:
				mySelectedFunctionalJJRequirement.setEnabled(false);
				jJRequirementService.updateJJRequirement(mySelectedFunctionalJJRequirement);
				break;
			case 3:
				mySelectedTechnicalJJRequirement.setEnabled(false);
				jJRequirementService.updateJJRequirement(mySelectedTechnicalJJRequirement);
				break;

			default:
				break;
			}

			Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
			List<JJRequirement> tempList = new ArrayList<JJRequirement>();

			if(selectedBusinessJJRequirements != null)
			if (selectedBusinessJJRequirements.size() > 0) {
				System.out.println("AJA selectedBusinessJJRequirements.size="+selectedBusinessJJRequirements.size());
				for (JJRequirement req : selectedBusinessJJRequirements) {
					System.out.println("AJA req.getId()="+req.getId());
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedBusinessJJRequirements);
			}

			if(selectedFunctionalJJRequirements != null)
			if (selectedFunctionalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedFunctionalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedFunctionalJJRequirements);
			}

			if(selectedTechnicalJJRequirements != null)
			if (selectedTechnicalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedTechnicalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedTechnicalJJRequirements);
			}

			requirementsDown.addAll(tempList);
			myEditedJJRequirement.setRequirementLinkDown(requirementsDown);

			jJRequirementService.saveJJRequirement(myEditedJJRequirement);
			findAllJJRequirementsWithCategory(editionColumnNumber);
		} else {

			switch (deleteColumnNumber) {
			case 1:
				jJRequirementService.updateJJRequirement(mySelectedBusinessJJRequirement);
				break;
			case 2:
				jJRequirementService.updateJJRequirement(mySelectedFunctionalJJRequirement);
				break;
			case 3:
				jJRequirementService.updateJJRequirement(mySelectedTechnicalJJRequirement);
				break;

			default:
				break;
			}

			findAllJJRequirementsWithCategory(deleteColumnNumber);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"This JJRequirement is deleted.", "Delete Status");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
*/
	}

	public void deleteJJRequirement(int number) {

		this.deleteColumnNumber = number;

		JJStatus myJJStatus = null;

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("DELETED");

		if (jjStatus != null)
			myJJStatus = jjStatus;
		else
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

		switch (number) {
		case 1:
			if (currentProject != null) {
				if (currentProduct != null) {
					if (currentVersion != null) {
						myBusinessJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"BUSINESS", currentProject, currentProduct, currentVersion);
					}
					else {
						myBusinessJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"BUSINESS", currentProject, currentProduct);
					}
				}
				else {
					myBusinessJJRequirements = jJRequirementService
							.getAllJJRequirementsWithProject("BUSINESS", currentProject);
				}
			}
			else {
				myBusinessJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("BUSINESS");
			}
			break;
		case 2:
			if (currentProject != null)
				if (currentProduct != null) {
					if (currentVersion != null) {
						myFunctionalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"FUNCTIONAL", currentProject, currentProduct, currentVersion);
					}
					else {
						myFunctionalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"FUNCTIONAL", currentProject, currentProduct);
					}
				}
				else {
					myFunctionalJJRequirements = jJRequirementService
							.getAllJJRequirementsWithProject("FUNCTIONAL", currentProject);
				}
			else {
				myFunctionalJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("FUNCTIONAL");
			}
			break;
		case 3:
			if (currentProject != null) {
				if (currentProduct != null) {
					if (currentVersion != null) {
						myTechnicalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"TECHNICAL", currentProject, currentProduct, currentVersion);
					}
					else {
						myTechnicalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"TECHNICAL", currentProject, currentProduct);
					}
				}
				else {
					myTechnicalJJRequirements = jJRequirementService
							.getAllJJRequirementsWithProject("TECHNICAL", currentProject);
				}
			}
			else {
				myTechnicalJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("TECHNICAL");
			}
			break;
		default:
			break;
		}
	}

	public void releaseJJRequirement() {
		List<JJRequirement> reqList = null;

		JJStatus myJJStatus = null;

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("RELEASE");

		if (jjStatus != null)
			myJJStatus = jjStatus;
		else
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

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("DISCARD");

		if (jjStatus != null)
			myJJStatus = jjStatus;
		else
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

	public void selectRequirement(int columnNumber) {

		List<JJRequirement> businessRequirementList = new ArrayList<JJRequirement>();
		List<JJRequirement> functionalRequirementList = new ArrayList<JJRequirement>();
		List<JJRequirement> technicalRequirementList = new ArrayList<JJRequirement>();

		JJRequirement req = new JJRequirement();

		Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();

		// A voir tous le traitements dans une requête
		switch (columnNumber) {
		case 1:

			req = jJRequirementService
					.findJJRequirement(mySelectedBusinessJJRequirement.getId());
			businessRequirementList.add(req);
			myBusinessJJRequirements = businessRequirementList;

			requirementsDown = req.getRequirementLinkDown();

			for (JJRequirement jjRequirement : requirementsDown) {
				if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("FUNCTIONAL"))
					functionalRequirementList.add(jjRequirement);
				else if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("TECHNICAL"))
					technicalRequirementList.add(jjRequirement);
			}
			myFunctionalJJRequirements = functionalRequirementList;
			myTechnicalJJRequirements = technicalRequirementList;
			break;
		case 2:
			req = jJRequirementService
					.findJJRequirement(mySelectedFunctionalJJRequirement
							.getId());
			functionalRequirementList.add(req);
			myFunctionalJJRequirements = functionalRequirementList;

			requirementsDown = req.getRequirementLinkDown();

			for (JJRequirement jjRequirement : requirementsDown) {
				if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("BUSINESS"))
					businessRequirementList.add(jjRequirement);
				else if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("TECHNICAL"))
					technicalRequirementList.add(jjRequirement);
			}
			myBusinessJJRequirements = businessRequirementList;
			myTechnicalJJRequirements = technicalRequirementList;

			break;
		case 3:
			req = jJRequirementService
					.findJJRequirement(mySelectedTechnicalJJRequirement.getId());
			technicalRequirementList.add(req);
			myTechnicalJJRequirements = technicalRequirementList;

			requirementsDown = req.getRequirementLinkDown();

			for (JJRequirement jjRequirement : requirementsDown) {
				if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("BUSINESS"))
					businessRequirementList.add(jjRequirement);
				else if (jjRequirement.getCategory().getName()
						.equalsIgnoreCase("FUNCTIONAL"))
					functionalRequirementList.add(jjRequirement);
			}
			myBusinessJJRequirements = businessRequirementList;
			myFunctionalJJRequirements = functionalRequirementList;
			break;

		default:
			break;
		}

	}

	public void onRowSelect(SelectEvent event) {
		JJRequirement req = (JJRequirement) event.getObject();

		FacesMessage msg = new FacesMessage("JJRequirement Selected "
				+ req.getName(), req.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);

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

	public void completeRequirement() {
		mySelectedTechnicalJJRequirement.setIsCompleted(true);
		jJRequirementService
				.updateJJRequirement(mySelectedTechnicalJJRequirement);

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
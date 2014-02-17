package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
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

	private List<String> myBusinessJJRequirementList;
	private List<String> selectedBusinessJJRequirementList;

	private List<String> myFunctionalJJRequirementList;
	private List<String> selectedFunctionalJJRequirementList;

	private List<String> myTechnicalJJRequirementList;
	private List<String> selectedTechnicalJJRequirementList;

	private HashMap<String, String> referencedMap;

	private List<JJTask> tasksList;
	private List<JJTask> selectedTasksList;

	private boolean disabled;

	private String messageRelease;
	private String messageDiscard;

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
		if (project != null)
			if (product != null)
				if (version != null)

					businessJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"BUSINESS", project, product, version);
				else

					businessJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"BUSINESS", project, product);
			else
				businessJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject("BUSINESS",
								project);
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

		if (project != null)
			if (product != null)
				if (version != null)

					functionalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"FUNCTIONAL", project, product, version);

				else

					functionalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"FUNCTIONAL", project, product);

			else
				functionalJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject(
								"FUNCTIONAL", project);
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

		if (project != null)
			if (product != null)
				if (version != null)

					technicalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"TECHNICAL", project, product, version);

				else

					technicalJJRequirementsList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"TECHNICAL", project, product);
			else
				technicalJJRequirementsList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject(
								"TECHNICAL", project);
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

	public List<String> getMyBusinessJJRequirementList() {
		List<JJRequirement> tmpList = new ArrayList<JJRequirement>();
		myBusinessJJRequirementList = new ArrayList<String>();
		if (project != null)
			if (product != null)
				if (version != null)

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"BUSINESS", project, product, version);
				else

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"BUSINESS", project, product);
			else
				tmpList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject("BUSINESS",
								project);
		else
			tmpList = jJRequirementService
					.getAllJJRequirementsWithCategory("BUSINESS");

		for (JJRequirement jjRequirement : tmpList) {
			myBusinessJJRequirementList.add(formatString(jjRequirement));
		}

		return myBusinessJJRequirementList;
	}

	public void setMyBusinessJJRequirementList(
			List<String> myBusinessJJRequirementList) {
		this.myBusinessJJRequirementList = myBusinessJJRequirementList;
	}

	public List<String> getSelectedBusinessJJRequirementList() {
		return selectedBusinessJJRequirementList;
	}

	public void setSelectedBusinessJJRequirementList(
			List<String> selectedBusinessJJRequirementList) {
		this.selectedBusinessJJRequirementList = selectedBusinessJJRequirementList;
	}

	public List<String> getMyFunctionalJJRequirementList() {
		List<JJRequirement> tmpList = new ArrayList<JJRequirement>();
		myFunctionalJJRequirementList = new ArrayList<String>();
		if (project != null)
			if (product != null)
				if (version != null)

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"FUNCTIONAL", project, product, version);
				else

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"FUNCTIONAL", project, product);
			else
				tmpList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject(
								"FUNCTIONAL", project);
		else
			tmpList = jJRequirementService
					.getAllJJRequirementsWithCategory("FUNCTIONAL");

		for (JJRequirement jjRequirement : tmpList) {
			myFunctionalJJRequirementList.add(formatString(jjRequirement));
		}

		return myFunctionalJJRequirementList;
	}

	public void setMyFunctionalJJRequirementList(
			List<String> myFunctionalJJRequirementList) {
		this.myFunctionalJJRequirementList = myFunctionalJJRequirementList;
	}

	public List<String> getSelectedFunctionalJJRequirementList() {
		return selectedFunctionalJJRequirementList;
	}

	public void setSelectedFunctionalJJRequirementList(
			List<String> selectedFunctionalJJRequirementList) {
		this.selectedFunctionalJJRequirementList = selectedFunctionalJJRequirementList;
	}

	public List<String> getMyTechnicalJJRequirementList() {
		List<JJRequirement> tmpList = new ArrayList<JJRequirement>();
		myTechnicalJJRequirementList = new ArrayList<String>();
		if (project != null)
			if (product != null)
				if (version != null)

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProductAndVersion(
									"TECHNICAL", project, product, version);
				else

					tmpList = jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"TECHNICAL", project, product);
			else
				tmpList = jJRequirementService
						.getAllJJRequirementsWithCategoryAndProject(
								"TECHNICAL", project);
		else
			tmpList = jJRequirementService
					.getAllJJRequirementsWithCategory("TECHNICAL");

		for (JJRequirement jjRequirement : tmpList) {
			myTechnicalJJRequirementList.add(formatString(jjRequirement));
		}
		return myTechnicalJJRequirementList;
	}

	public void setMyTechnicalJJRequirementList(
			List<String> myTechnicalJJRequirementList) {
		this.myTechnicalJJRequirementList = myTechnicalJJRequirementList;
	}

	public List<String> getSelectedTechnicalJJRequirementList() {
		return selectedTechnicalJJRequirementList;
	}

	public void setSelectedTechnicalJJRequirementList(
			List<String> selectedTechnicalJJRequirementList) {
		this.selectedTechnicalJJRequirementList = selectedTechnicalJJRequirementList;
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

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion geVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
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
		selectedChapter = null;
		myJJRequirement = new JJRequirement();
		myJJRequirement.setCreationDate(new Date());
		myJJRequirement.setEnabled(true);
		myJJRequirement.setIsCompleted(false);

		this.creationColumnNumber = number;
		String categoryName = null;
		switch (number) {
		case 1:
			categoryName = "BUSINESS";
			break;
		case 2:
			categoryName = "FUNCTIONAL";
			break;
		case 3:
			categoryName = "TECHNICAL";
			break;

		default:
			break;
		}

		category = jJCategoryService.getJJCategoryWithName(categoryName);

		myJJRequirement.setCategory(category);

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("NEW");

		myJJRequirement.setStatus(jjStatus);
		myJJRequirement.setNumero(new Random().nextInt(1000) + 1);
		if (project != null)
			myJJRequirement.setProject(project);
		if (product != null)
			myJJRequirement.setProduct(product);

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

		category = req.getCategory();

		selectedChapterEdit = req.getChapter();

		myEditedJJRequirement = new JJRequirement();
		myEditedJJRequirement.setCreationDate(req.getCreationDate());
		myEditedJJRequirement.setUpdatedDate(new Date());
		myEditedJJRequirement.setEnabled(true);
		myEditedJJRequirement.setCategory(category);

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("MODIFIED");

		myEditedJJRequirement.setStatus(jjStatus);
		myEditedJJRequirement.setNumero(req.getNumero());
		myEditedJJRequirement.setProject(req.getProject());
		myEditedJJRequirement.setChapter(req.getChapter());

		SortedMap<Integer, Object> elements = getSortedElements(
				selectedChapterEdit, false);

		/**
		 * Attribute order to chapter
		 */
		if (elements.isEmpty()) {
			myEditedJJRequirement.setOrdering(0);
		} else {
			myEditedJJRequirement.setOrdering(elements.lastKey() + 1);
		}

		myEditedJJRequirement.setProduct(req.getProduct());
		myEditedJJRequirement.setDescription(req.getDescription());
		myEditedJJRequirement.setName(req.getName());
		myEditedJJRequirement.setNote(req.getNote());

		req = jJRequirementService.findJJRequirement(req.getId());

		System.out.println("req.getRequirementLinkDown().size() "
				+ req.getRequirementLinkDown().size());

		System.out.println("req.getRequirementLinkUp().size() "
				+ req.getRequirementLinkUp().size());

		Set<JJRequirement> requirementsDown = req.getRequirementLinkDown();
		Set<JJRequirement> requirementsUp = req.getRequirementLinkUp();

		referencedMap = new HashMap<String, String>();

		selectedBusinessJJRequirementList = new ArrayList<String>();
		selectedFunctionalJJRequirementList = new ArrayList<String>();
		selectedTechnicalJJRequirementList = new ArrayList<String>();

		for (JJRequirement jjRequirement : requirementsDown) {
			if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("BUSINESS")) {
				selectedBusinessJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "D");
			} else if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("FUNCTIONAL")) {
				selectedFunctionalJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "D");
			} else if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("TECHNICAL")) {
				selectedTechnicalJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "D");
			}
		}

		for (JJRequirement jjRequirement : requirementsUp) {
			if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("BUSINESS")) {
				selectedBusinessJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "U");
			} else if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("FUNCTIONAL")) {
				selectedFunctionalJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "U");
			} else if (jjRequirement.getCategory().getName()
					.equalsIgnoreCase("TECHNICAL")) {
				selectedTechnicalJJRequirementList
						.add(formatString(jjRequirement));
				referencedMap.put(jjRequirement.getId().toString(), "U");
			}
		}

		for (Map.Entry<String, String> entree : referencedMap.entrySet()) {
			System.out.println("entree.getKey() + + entree.getValue()"
					+ entree.getKey() + "    " + entree.getValue());
		}

	}

	public void persistJJRequirement(int index) {

		if (index == 1) {

			Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
			List<JJRequirement> tempList = new ArrayList<JJRequirement>();

			if (selectedBusinessJJRequirements.size() > 0) {
				for (JJRequirement req : selectedBusinessJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}
				tempList.addAll(tempList.size(), selectedBusinessJJRequirements);
			}

			if (selectedFunctionalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedFunctionalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}

				tempList.addAll(tempList.size(),
						selectedFunctionalJJRequirements);
			}

			if (selectedTechnicalJJRequirements.size() > 0) {
				for (JJRequirement req : selectedTechnicalJJRequirements) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(myJJRequirement);
				}

				tempList.addAll(tempList.size(),
						selectedTechnicalJJRequirements);
			}

			requirementsDown.addAll(tempList);

			myJJRequirement.setRequirementLinkDown(requirementsDown);

			if (creationColumnNumber == 3) {

				if (selectedTasksList.size() > 0) {
					// for (JJTask task : selectedTasksList) {
					// task.setRequirement(myJJRequirement);
					// task.setStartDate(new Date());
					// jJTaskService.updateJJTask(task);
					// }

					Set<JJTask> tasks = new HashSet<JJTask>();
					tasks.addAll(selectedTasksList);
					myJJRequirement.setTasks(tasks);

				}

			}

			jJRequirementService.saveJJRequirement(myJJRequirement);
			if (creationColumnNumber == 3) {

				if (selectedTasksList.size() > 0) {
					for (JJTask task : selectedTasksList) {
						task.setRequirement(myJJRequirement);
						task.setStartDateReal(new Date());
						jJTaskService.updateJJTask(task);
					}

				}

			}

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

			Set<JJRequirement> requirementsDown = new HashSet<JJRequirement>();
			List<JJRequirement> tempList = new ArrayList<JJRequirement>();

			if (selectedBusinessJJRequirementList.size() > 0) {
				for (String s : selectedBusinessJJRequirementList) {
					s = getIdFromString(s);
					JJRequirement req = jJRequirementService
							.findJJRequirement(Long.parseLong(s));
					req.getRequirementLinkUp().add(myEditedJJRequirement);
					tempList.add(req);
				}
			}

			if (selectedFunctionalJJRequirementList.size() > 0) {
				for (String s : selectedFunctionalJJRequirementList) {
					s = getIdFromString(s);
					JJRequirement req = jJRequirementService
							.findJJRequirement(Long.parseLong(s));
					req.getRequirementLinkUp().add(myEditedJJRequirement);
					tempList.add(req);
				}
			}

			if (selectedTechnicalJJRequirementList.size() > 0) {
				for (String s : selectedTechnicalJJRequirementList) {
					s = getIdFromString(s);
					JJRequirement req = jJRequirementService
							.findJJRequirement(Long.parseLong(s));
					req.getRequirementLinkUp().add(myEditedJJRequirement);
					tempList.add(req);
				}
			}

			requirementsDown.addAll(tempList);
			myEditedJJRequirement.setRequirementLinkDown(requirementsDown);

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

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("DELETED");

		switch (number) {
		case 1:
			mySelectedBusinessJJRequirement.setEnabled(false);
			mySelectedBusinessJJRequirement.setStatus(jjStatus);
			break;
		case 2:
			mySelectedFunctionalJJRequirement.setEnabled(false);
			mySelectedFunctionalJJRequirement.setStatus(jjStatus);
			break;

		case 3:
			mySelectedTechnicalJJRequirement.setEnabled(false);
			mySelectedTechnicalJJRequirement.setStatus(jjStatus);
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
			if (project != null)
				if (product != null)

					if (version != null)

						myBusinessJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"BUSINESS", project, product, version);
					else

						myBusinessJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"BUSINESS", project, product);
				else
					myBusinessJJRequirements = jJRequirementService
							.getAllJJRequirementsWithCategoryAndProject(
									"BUSINESS", project);
			else
				myBusinessJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("BUSINESS");
			break;
		case 2:

			if (project != null)
				if (product != null)

					if (version != null)

						myFunctionalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"FUNCTIONAL", project, product, version);

					else

						myFunctionalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"FUNCTIONAL", project, product);

				else
					myFunctionalJJRequirements = jJRequirementService
							.getAllJJRequirementsWithCategoryAndProject(
									"FUNCTIONAL", project);
			else

				myFunctionalJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("FUNCTIONAL");
			break;
		case 3:
			if (project != null)
				if (product != null)
					if (version != null)

						myTechnicalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"TECHNICAL", project, product, version);

					else

						myTechnicalJJRequirements = jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"TECHNICAL", project, product);
				else
					myTechnicalJJRequirements = jJRequirementService
							.getAllJJRequirementsWithCategoryAndProject(
									"TECHNICAL", project);
			else

				myTechnicalJJRequirements = jJRequirementService
						.getAllJJRequirementsWithCategory("TECHNICAL");
			break;
		default:
			break;
		}

	}

	public void releaseJJRequirement() {
		List<JJRequirement> reqList = null;

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("RELEASED");

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
				jjRequirement.setStatus(jjStatus);
				jJRequirementService.updateJJRequirement(jjRequirement);
			}

		}
		findAllJJRequirementsWithCategory(releaseColumnNumber);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"All JJRequirements are released.", "Release Status");

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void discardJJRequirement() {

		JJStatus jjStatus = jJStatusService.getJJStatusWithName("DISCARDED");

		JJRequirement req = null;

		switch (discardColumnNumber) {
		case 1:

			mySelectedBusinessJJRequirement.setEnabled(false);
			mySelectedBusinessJJRequirement.setStatus(jjStatus);
			req = mySelectedBusinessJJRequirement;
			break;
		case 2:
			mySelectedFunctionalJJRequirement.setEnabled(false);
			mySelectedFunctionalJJRequirement.setStatus(jjStatus);
			req = mySelectedFunctionalJJRequirement;
			break;

		case 3:
			mySelectedTechnicalJJRequirement.setEnabled(false);
			mySelectedTechnicalJJRequirement.setStatus(jjStatus);
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

		// FacesMessage msg = new FacesMessage("JJRequirement Selected "
		// + req.getName(), req.getName());
		// FacesContext.getCurrentInstance().addMessage(null, msg);

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

	private String getIdFromString(String s) {
		String[] temp = s.split("-");
		return temp[0];
	}

	private String formatString(JJRequirement req) {
		return req.getId() + "- " + req.getName();

	}

	// public List<JJChapter> completeChapters(String query) {
	// List<JJChapter> suggestions = new ArrayList<JJChapter>();
	//
	// for (JJChapter jJChapter : jJChapterService
	// .getAllJJChaptersWithProjectAndCategory(project,
	// currentJJCategory)) {
	// String jJChapterStr = String.valueOf(jJChapter.getName() + " "
	// + jJChapter.getDescription() + " "
	// + jJChapter.getCreationDate() + " "
	// + jJChapter.getUpdatedDate());
	// if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
	// suggestions.add(jJChapter);
	// }
	// }
	// return suggestions;
	// }

	public void load(JJProjectBean jJProjectBean, JJChapterBean jJChapterBean,
			JJProductBean jJProductBean,JJVersionBean jJVersionBean) {
		
		project = jJProjectBean.getProject();
		product = jJProductBean.getProduct();
		version = jJVersionBean.getVersion();
		
		if (project == null) {
			project = jJProjectBean.getProject();
			jJChapterBean.setProject(project);
			jJProductBean.setProject(project);
		}

	}

	public void handleSelectChapter() {

		SortedMap<Integer, Object> elements = getSortedElements(
				selectedChapter, false);

		/**
		 * Attribute order to chapter
		 */
		if (elements.isEmpty()) {
			myJJRequirement.setOrdering(0);
		} else {
			myJJRequirement.setOrdering(elements.lastKey() + 1);
		}

		System.out.println("myJJRequirement.getOrdering() "
				+ myJJRequirement.getOrdering());
		myJJRequirement.setChapter(selectedChapter);
	}

	public void handleSelectChapterEdit() {

		SortedMap<Integer, Object> elements = getSortedElements(
				selectedChapter, false);

		/**
		 * Attribute order to chapter
		 */
		if (elements.isEmpty()) {
			myEditedJJRequirement.setOrdering(0);
		} else {
			myEditedJJRequirement.setOrdering(elements.lastKey() + 1);
		}

		System.out.println("myEditedJJRequirement.getOrdering() "
				+ myEditedJJRequirement.getOrdering());
		myEditedJJRequirement.setChapter(selectedChapterEdit);
	}

	private JJCategory category;

	private JJChapter selectedChapter;

	public JJChapter getSelectedChapter() {
		return selectedChapter;
	}

	public void setSelectedChapter(JJChapter selectedChapter) {
		this.selectedChapter = selectedChapter;
	}

	private List<JJChapter> chapterList;

	public List<JJChapter> getChapterList() {
		chapterList = jJChapterService.getAllJJChapters(project, product,
				category);
		return chapterList;
	}

	public void setChapterList(List<JJChapter> chapterList) {
		this.chapterList = chapterList;
	}

	private JJChapter selectedChapterEdit;

	public JJChapter getSelectedChapterEdit() {
		return selectedChapterEdit;
	}

	public void setSelectedChapterEdit(JJChapter selectedChapterEdit) {
		this.selectedChapterEdit = selectedChapterEdit;
	}

	private List<JJChapter> chapterListEdit;

	public List<JJChapter> getChapterListEdit() {
		chapterListEdit = jJChapterService.getAllJJChapters(project, product,
				category);
		return chapterListEdit;
	}

	public void setChapterListEdit(List<JJChapter> chapterListEdit) {
		this.chapterListEdit = chapterListEdit;
	}

	public SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			boolean onlyActif) {

		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getAllChildsJJChapterWithParentSortedByOrder(parent,
							onlyActif);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering(), requirement);
			}

		} else {
			List<JJChapter> chapters = jJChapterService
					.getAllParentsJJChapterSortedByOrder(project, product,
							category, onlyActif);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}
		}

		return elements;

	}

	public void closeDialog(CloseEvent event) {
		myJJRequirement = null;
		myEditedJJRequirement = null;
	}

	// /////////////////////////////////////////////////////////////
	// Transformation

	private JJCategory lowCategory;

	public JJCategory getLowCategory() {
		return lowCategory;
	}

	public void setLowCategory(JJCategory lowCategory) {
		this.lowCategory = lowCategory;
	}

	private JJCategory mediumCategory;

	public JJCategory getMediumCategory() {
		return mediumCategory;
	}

	public void setMediumCategory(JJCategory mediumCategory) {
		this.mediumCategory = mediumCategory;
	}

	private JJCategory highCategory;

	public JJCategory getHighCategory() {
		return highCategory;
	}

	public void setHighCategory(JJCategory highCategory) {
		this.highCategory = highCategory;
	}

	private List<JJCategory> categoryList;

	public List<JJCategory> getCategoryList() {
		categoryList = jJCategoryService.getCategories(true, true);
		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	private String templateHeader = null;

	public String getTemplateHeader() {
		return templateHeader;
	}

	public void setTemplateHeader(String templateHeader) {
		this.templateHeader = templateHeader;
	}

	private List<JJRequirement> lowRequirementList;

	public List<JJRequirement> getLowRequirementList() {
		if (lowCategory != null) {
			lowRequirementList = jJRequirementService.getRequirements(
					lowCategory, project, product, version, null, false, true,
					true);
		} else {
			lowRequirementList = null;
		}
		return lowRequirementList;
	}

	public void setLowRequirementList(List<JJRequirement> lowRequirementList) {
		this.lowRequirementList = lowRequirementList;
	}

	private List<JJRequirement> mediumRequirementList;

	public List<JJRequirement> getMediumRequirementList() {
		if (mediumCategory != null) {
			mediumRequirementList = jJRequirementService.getRequirements(
					mediumCategory, project, product, version, null, false,
					true, true);
		} else {
			mediumRequirementList = null;
		}
		return mediumRequirementList;
	}

	public void setMediumRequirementList(
			List<JJRequirement> mediumRequirementList) {
		this.mediumRequirementList = mediumRequirementList;
	}

	private List<JJRequirement> highRequirementList;

	public List<JJRequirement> getHighRequirementList() {
		if (highCategory != null) {
			highRequirementList = jJRequirementService.getRequirements(
					highCategory, project, product, version, null, false, true,
					true);
		} else {
			highRequirementList = null;
		}

		return highRequirementList;
	}

	public void setHighRequirementList(List<JJRequirement> highRequirementList) {
		this.highRequirementList = highRequirementList;
	}

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	public void updateTemplate(long id) {
		System.out.println("A voir avec Arnaud");

		JJCategory category = jJCategoryService.findJJCategory(id);

		if (templateHeader != null) {
			String[] categories = templateHeader.split("/");
			int length = categories.length;

			System.out.println(length);

			switch (length) {
			case 1:
				if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader += mediumCategory.getName() + "/";
				} else if (category.getStage() < lowCategory.getStage()) {
					mediumCategory = lowCategory;
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";
				} else {
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/";
				}

				break;

			case 2:

				if (category.getStage() > mediumCategory.getStage()) {
					highCategory = category;
					templateHeader += highCategory.getName();
				} else if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";
				} else {
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/";
				}

				break;
			case 3:
				if (category.getStage() > mediumCategory.getStage()) {
					highCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/"
							+ highCategory.getName();
				} else if (category.getStage() > lowCategory.getStage()) {
					mediumCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/"
							+ highCategory.getName();
				} else {
					lowCategory = category;
					templateHeader = lowCategory.getName() + "/"
							+ mediumCategory.getName() + "/"
							+ highCategory.getName();
				}

				break;

			}

		} else {
			lowCategory = category;
			templateHeader = category.getName() + "/";
		}

		System.out.println("\n" + templateHeader);
	}

}
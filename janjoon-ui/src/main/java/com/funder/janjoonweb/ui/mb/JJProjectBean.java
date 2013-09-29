package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJChapterService;
import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJCategoryService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProductService;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTaskService;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.domain.JJVersionService;
import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJPermissionService;
import com.funder.janjoonweb.domain.JJRight;
import com.funder.janjoonweb.domain.JJRightService;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	private JJProject myJJProject;
	private List<JJProject> myJJProjectList;

	@Autowired
	JJChapterService jJChapterService;

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	@Autowired
	JJProductService jJProductService;

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	@Autowired
	JJVersionService jJVersionService;

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	@Autowired
	JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	@Autowired
	JJContactService localJJContactService;

	public void setjJContactService(
			JJContactService localJJContactService) {
		this.localJJContactService = localJJContactService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Autowired
	JJRightService jJRightService;

	public void setjJRightService(
			JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public JJProject getMyJJProject() {

		return myJJProject;
	}

	public void setMyJJProject(JJProject myJJProject) {

		this.myJJProject = myJJProject;
	}

	public List<JJProject> getMyJJProjectList() {
		/*** Begin Temporary ***/

		// if (jJChapterService.getAllJJChapter().isEmpty()) {
		// JJChapter chapter;
		// for (int i = 0; i < 4; i++) {
		// chapter = new JJChapter();
		// chapter.setName("ChapterName " + i);
		// chapter.setDescription("ChapterDescription " + i);
		// chapter.setCreationDate(new Date());
		// chapter.setEnabled(true);
		// jJChapterService.saveJJChapter(chapter);
		// }
		// }

		if (jJTaskService.findAllJJTasks().isEmpty()) {
			JJTask task;
			for (int i = 0; i < 4; i++) {
				task = new JJTask();
				task.setName("TaskName " + i);
				task.setDescription("TaskDescription " + i);
				task.setCreationDate(new Date());
				task.setEnabled(true);
				jJTaskService.saveJJTask(task);
			}
		}

		if (jJVersionService.getAllJJVersion().isEmpty()) {
			JJVersion version;
			for (int i = 0; i < 4; i++) {
				version = new JJVersion();
				version.setName("VersionName " + i);
				version.setDescription("VersionDescription " + i);
				version.setCreationDate(new Date());
				version.setEnabled(true);
				jJVersionService.saveJJVersion(version);
			}
		}


		if (jJProductService.getAllJJProduct().isEmpty()) {
			JJProduct product;
			for (int i = 0; i < 2; i++) {
				product = new JJProduct();
				product.setName("ProductName " + i);
				product.setDescription("ProductDescription " + i);
				product.setCreationDate(new Date());
				product.setEnabled(true);
				product.setExtname("ProductExtName " + i);

				List<JJVersion> jJVersionList = jJVersionService
						.getAllJJVersion();

				Set<JJVersion> versions = new HashSet<JJVersion>();

				if (i == 0) {
					jJVersionList.get(i).setProduct(product);
					versions.add(jJVersionList.get(i));
					jJVersionList.get(i + 1).setProduct(product);
					versions.add(jJVersionList.get(i + 1));
				} else {
					jJVersionList.get(i + 1).setProduct(product);
					versions.add(jJVersionList.get(i + 1));
					jJVersionList.get(i + 2).setProduct(product);
					versions.add(jJVersionList.get(i + 2));
				}

				product.setJjversions(versions);

				jJProductService.saveJJProduct(product);
			}
		}

		if (jJProjectService.findAllJJProjects().isEmpty()) {
			for (int i = 0; i < 2; i++) {
				JJProject project;
				project = new JJProject();
				project.setName("ProjectName " + i);
				project.setDescription("ProjectDescription " + i);
				project.setCreationDate(new Date());
				project.setEnabled(true);

				jJProjectService.saveJJProject(project);
			}
		}

		JJCategory CategoryBusiness;
		JJCategory CategoryFunctional;
		JJCategory CategoryTechnical;
		JJCategory CategoryArchitecture;
		JJCategory CategorySecurity;
		if (jJCategoryService.getAllJJCategory().isEmpty()) {
			String name = "BUSINESS";
			CategoryBusiness = new JJCategory();
			CategoryBusiness.setName(name);
			CategoryBusiness.setCreationDate(new Date());
			CategoryBusiness.setDescription("A JJCategory defined as " + name);
			CategoryBusiness.setEnabled(true);
			CategoryBusiness.setStage(1);
			jJCategoryService.saveJJCategory(CategoryBusiness);

			name = "FUNCTIONAL";
			CategoryFunctional = new JJCategory();
			CategoryFunctional.setName(name);
			CategoryFunctional.setCreationDate(new Date());
			CategoryFunctional.setDescription("A JJCategory defined as " + name);
			CategoryFunctional.setEnabled(true);
			CategoryFunctional.setStage(2);
			jJCategoryService.saveJJCategory(CategoryFunctional);

			name = "TECHNICAL";
			CategoryTechnical = new JJCategory();
			CategoryTechnical.setName(name);
			CategoryTechnical.setCreationDate(new Date());
			CategoryTechnical.setDescription("A JJCategory defined as " + name);
			CategoryTechnical.setEnabled(true);
			CategoryTechnical.setStage(3);
			jJCategoryService.saveJJCategory(CategoryTechnical);

			name = "ARCHITECTURE";
			CategoryArchitecture = new JJCategory();
			CategoryArchitecture.setName(name);
			CategoryArchitecture.setCreationDate(new Date());
			CategoryArchitecture.setDescription("A JJCategory defined as " + name);
			CategoryArchitecture.setEnabled(true);
			CategoryArchitecture.setStage(2);
			jJCategoryService.saveJJCategory(CategoryArchitecture);

			name = "SECURITY";
			CategorySecurity = new JJCategory();
			CategorySecurity.setName(name);
			CategorySecurity.setCreationDate(new Date());
			CategorySecurity.setDescription("A JJCategory defined as " + name);
			CategorySecurity.setEnabled(true);
			CategorySecurity.setStage(2);
			jJCategoryService.saveJJCategory(CategorySecurity);
		}

		JJPermission newJJPermission;
		if (jJPermissionService.getAllJJPermission().isEmpty()) {
			newJJPermission = new JJPermission();
			newJJPermission.setPermission("general");
			jJPermissionService.saveJJPermission(newJJPermission);
		}

		if (jJRightService.getAllJJRight().isEmpty()) {
			JJRight newJJRight = new JJRight();
			newJJRight.setProject(jJProjectService.getAllJJProject().get(0));
			newJJRight.setProduct(jJProductService.getAllJJProduct().get(0));
			newJJRight.setPermission(jJPermissionService.getAllJJPermission().get(0));
			newJJRight.setCategory(jJCategoryService.getAllJJCategory().get(0));
			newJJRight.setR(true);
			newJJRight.setW(true);
			newJJRight.setX(true);
			jJRightService.saveJJRight(newJJRight);
		}
/*
		if (localJJContactService.getAllJJContact().isEmpty()) {
			JJContact newJJContact = new JJContact();
			newJJContact.setPassword("starit");
			newJJContact.setEmail("starit@gmail.com");
			newJJContact.setLdap(123456);
			newJJContact.setFirstname("janjoon");
			newJJContact.setLastname("admin");
			newJJContact.setDateofbirth(new Date());
			newJJContact.setEnabled(true);
			newJJContact.setAccountNonExpired(true);
			newJJContact.setCredentialsNonExpired(true);
			newJJContact.setAccountNonLocked(true);

			//newJJContact.setJjright(rights);
			localJJContactService.saveJJContact(newJJContact);
		}
*/

		/*** End Temporary ***/

		myJJProjectList = jJProjectService.getAllJJProject();
		return myJJProjectList;
	}

	public void setMyJJProjectList(List<JJProject> myJJProjectList) {
		this.myJJProjectList = myJJProjectList;
	}

	public void handleSelectProject(JJProductBean jJProductBean,
			JJVersionBean jJVersionBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {
		if (myJJProject != null) {

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Project selected: " + myJJProject.getName(),
					"Selection info");

			FacesContext.getCurrentInstance().addMessage(null, message);

			if (jJProductBean != null) {
				jJProductBean.setDisabled(false);
				jJProductBean.setMyJJProduct(null);
				jJProductBean.setProject(myJJProject);
			}

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setMyJJVersion(null);
			}

			if (jJRequirementBean != null) {

				jJRequirementBean.setCurrentProject(myJJProject);
				jJChapterBean.setCurrentProject(myJJProject);

				jJRequirementBean
						.setMyBusinessJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProject("BUSINESS", myJJProject));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProject("FUNCTIONAL", myJJProject));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProject("TECHNICAL", myJJProject));

			}

			if (jJChapterBean != null)
				jJChapterBean.setCurrentProject(myJJProject);

		} else {

			if (jJProductBean != null) {
				jJProductBean.setDisabled(true);
				jJProductBean.setMyJJProduct(null);
			}
			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setMyJJVersion(null);
			}

			// IF PROJECT IS NULL GET ALL JJREQUIRMENTS

			jJRequirementBean.setCurrentProject(myJJProject);
			jJChapterBean.setCurrentProject(myJJProject);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategory("BUSINESS"));

			jJRequirementBean
					.setMyFunctionalJJRequirements(jJRequirementService
							.getAllJJRequirementsWithCategory("FUNCTIONAL"));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategory("TECHNICAL"));

		}

	}

}
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
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProductService;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTaskService;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.domain.JJVersionService;

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
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
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
			for (int i = 0; i < 6; i++) {
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

		// List<JJProduct> jJProductList = jJProductService.getAllJJProduct();
		// if (!jJProductList.isEmpty()) {
		// for (int i = 0; i < jJProductList.size(); i++) {
		//
		// List<JJChapter> jJChapterList = jJChapterService
		// .getAllJJChapter();
		//
		// Set<JJChapter> chapters = new HashSet<JJChapter>();
		//
		// if (i == 0) {
		// jJChapterList.get(i).setProduct(jJProductList.get(i));
		// chapters.add(jJChapterList.get(i));
		// jJChapterList.get(i + 1).setProduct(jJProductList.get(i));
		// chapters.add(jJChapterList.get(i + 1));
		// } else {
		// jJChapterList.get(i + 1).setProduct(jJProductList.get(i));
		// chapters.add(jJChapterList.get(i + 1));
		// jJChapterList.get(i + 2).setProduct(jJProductList.get(i));
		// chapters.add(jJChapterList.get(i + 2));
		// }
		//
		// jJProductList.get(i).setChapters(chapters);
		//
		// jJProductService.updateJJProduct(jJProductList.get(i));
		// }
		// }

		if (jJProjectService.findAllJJProjects().isEmpty()) {
			JJProject project;
			for (int i = 0; i < 2; i++) {
				project = new JJProject();
				project.setName("ProjectName " + i);
				project.setDescription("ProjectDescription " + i);
				project.setCreationDate(new Date());
				project.setEnabled(true);

				// Set<JJChapter> chapters = new HashSet<JJChapter>();
				// List<JJChapter> jJChapterList = jJChapterService
				// .getAllJJChapter();
				//
				// if (i == 0) {
				// jJChapterList.get(i).setProject(project);
				// chapters.add(jJChapterList.get(i));
				// jJChapterList.get(i + 1).setProject(project);
				// chapters.add(jJChapterList.get(i + 1));
				// } else {
				// jJChapterList.get(i + 1).setProject(project);
				// chapters.add(jJChapterList.get(i + 1));
				// jJChapterList.get(i + 2).setProject(project);
				// chapters.add(jJChapterList.get(i + 2));
				// }
				//
				// project.setChapters(chapters);

				jJProjectService.saveJJProject(project);
			}
		}
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
								.getAllJJRequirementsWithProject("BUSINESS",
										myJJProject));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProject("FUNCTIONAL",
										myJJProject));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProject("TECHNICAL",
										myJJProject));

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
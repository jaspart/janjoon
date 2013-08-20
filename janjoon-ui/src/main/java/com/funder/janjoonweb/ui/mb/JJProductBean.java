package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJChapterService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

	private JJProduct myJJProduct;
	private List<JJProduct> myJJProductList;
	private boolean disabled = true;
	private JJProject project;

	@Autowired
	JJChapterService jJChapterService;

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public JJProduct getMyJJProduct() {
		return myJJProduct;
	}

	public void setMyJJProduct(JJProduct myJJProduct) {
		this.myJJProduct = myJJProduct;
	}

	public List<JJProduct> getMyJJProductList() {

		// myJJProductList = jJChapterService
		// .getAllJJProductInJJChapterWithJJProject(project);
		myJJProductList = jJProductService.getAllJJProduct();

		JJProduct product = new JJProduct();
		product.setId((long) 1234567890);
		product.setName("Select All");
		myJJProductList.add(0, product);
		return myJJProductList;
	}

	public void setMyJJProductList(List<JJProduct> myJJProductList) {
		this.myJJProductList = myJJProductList;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void handleSelectProduct(JJVersionBean jJVersionBean,
			JJProjectBean jJProjectBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {

		if (myJJProduct != null) {

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Product selected: " + myJJProduct.getName(),
					"Selection info");
			FacesContext.getCurrentInstance().addMessage(null, message);

			// Requete getReqwithProject&Product

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(false);
				jJVersionBean.setMyJJVersion(null);
				jJVersionBean.setProduct(myJJProduct);
				jJVersionBean.setProject(project);
			}

			if (jJRequirementBean != null) {

				jJRequirementBean.setCurrentProduct(myJJProduct);

				jJRequirementBean
						.setMyBusinessJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"BUSINESS", project, myJJProduct));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"FUNCTIONAL", project, myJJProduct));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"TECHNICAL", project, myJJProduct));

			}
			if (jJChapterBean != null)
				jJChapterBean.setCurrentProduct(myJJProduct);

		} else {
			// IF PRODUCT IS NULL GET ALL JJREQUIRMENTS WITH PROJECT

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setMyJJVersion(null);
			}

			jJRequirementBean.setCurrentProduct(myJJProduct);
			jJChapterBean.setCurrentProduct(myJJProduct);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProject("BUSINESS", project));

			jJRequirementBean
					.setMyFunctionalJJRequirements(jJRequirementService
							.getAllJJRequirementsWithProject("FUNCTIONAL",
									project));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProject("TECHNICAL", project));

		}

	}
}

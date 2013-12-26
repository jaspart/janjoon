package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJChapterService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJRight;
import com.funder.janjoonweb.domain.JJRightService;
import com.funder.janjoonweb.domain.JJRightServiceImpl;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJPermissionService;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

	private JJProduct product;
	private List<JJProduct> productList;
	private List<JJContact> productManagerList;
	private JJContact productManager;
	private boolean disabled = true;
	private JJProject project;
	private String message;

	@Autowired
	JJChapterService jJChapterService;
	JJRightService jJRightService;

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

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public List<JJProduct> getProductList() {

		// productList = jJChapterService
		// .getAllJJProductInJJChapterWithJJProject(project);
		productList = jJProductService.getAllJJProducts();

		JJProduct product = new JJProduct();
		product.setId((long) 1234567890);
		product.setName("Select All");
		productList.add(0, product);
		return productList;
	}

	public void setProductList(List<JJProduct> productList) {
		this.productList = productList;
	}

	public List<JJContact> getProductManagerList() {
		productManagerList = new ArrayList<JJContact>();
		
		for(JJRight right : jJRightService.getObjectWriterList("Product",true)){
			//recuperer les permission avec les profiles de ces droits right.getProfile()
			List<JJPermission> permissions = JJPermissionService.getProductManagerPermissions(right.getProfile());
			for(JJPermission permission : permissions){
				productManagerList.add(permission.getContact());
			}
		}
		return productManagerList;
	}

	public void setProductManagerList(List<JJContact> productManagerList) {
		this.productManagerList = productManagerList;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void newProduct() {
		message = "New Product";
		product = new JJProduct();
		product.setEnabled(true);
		product.setCreationDate(new Date());
		product.setDescription("Defined as a Product");
		product.setName("TEMP Value");
//		product.setManager("TEMP Value");
	}
	
	public void editProduct() {
		message = "Edit Product";
	}

	public void save() {
		System.out.println("SAVING ...");
		String message = "";

		if (product.getId() == null) {
			System.out.println("IS a new Product");
			jJProductService.saveJJProduct(product);
			message = "Product_successfully_created";
		} else {
			jJProductService.updateJJProduct(product);
			message = "Product_successfully_updated";
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("productDialogWidget.hide()");
	
		FacesMessage facesMessage = MessageFactory.getMessage(message, "JJProduct");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	
	public void handleSelectProduct(JJVersionBean jJVersionBean,
			JJProjectBean jJProjectBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {

		if (product != null) {

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Product selected: " + product.getName(), "Selection info");
			FacesContext.getCurrentInstance().addMessage(null, message);

			// Requete getReqwithProject&Product
			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(false);
				jJVersionBean.setProduct(null);
				jJVersionBean.setProduct(product);
				jJVersionBean.setProject(project);
			}

			if (jJRequirementBean != null) {

				jJRequirementBean.setCurrentProduct(product);

				jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProduct("BUSINESS", project, product));

				jJRequirementBean.setMyFunctionalJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProduct("FUNCTIONAL", project, product));

				jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProduct("TECHNICAL", project, product));

			}
			if (jJChapterBean != null)
				jJChapterBean.setCurrentProduct(product);

		} else {
			// IF PRODUCT IS NULL GET ALL JJREQUIRMENTS WITH PROJECT

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setProduct(null);
			}

			jJRequirementBean.setCurrentProduct(product);
			jJChapterBean.setCurrentProduct(product);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("BUSINESS",	project));

			jJRequirementBean.setMyFunctionalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("FUNCTIONAL", project));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("TECHNICAL", project));

		}
	}
}

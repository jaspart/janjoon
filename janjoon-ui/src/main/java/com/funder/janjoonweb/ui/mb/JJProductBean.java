package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJChapterService;
import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJPermissionService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJRight;
import com.funder.janjoonweb.domain.JJRightService;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

	private JJProduct product;
	private List<JJProduct> productList;
	
	private List<JJProduct> productListTable;

	private JJContact productManager;
	private List<JJContact> productManagerList;

	private boolean disabled = true;
	private JJProject project;
	private String message;

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

	@Autowired
	JJRightService jJRightService;

	public void setjJRightService(JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
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

	public List<JJProduct> getProductListTable() {
		productListTable = jJProductService.getAllJJProducts();
		return productListTable;
	}

	public void setProductListTable(List<JJProduct> productListTable) {
		this.productListTable = productListTable;
	}

	public JJContact getProductManager() {
		return productManager;
	}

	public void setProductManager(JJContact productManager) {
		this.productManager = productManager;
	}

	public List<JJContact> getProductManagerList() {

		productManagerList = new ArrayList<JJContact>();

		List<JJRight> rights = jJRightService.getObjectWriterList("Product");
		System.out.println("rights "+rights.size());
		for (JJRight right : rights ) {
			List<JJPermission> permissions = jJPermissionService
					.getProductManagerPermissions(right.getProfile());
			for (JJPermission permission : permissions) {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void newProduct() {
		message = "New Product";
		product = new JJProduct();
		product.setEnabled(true);
		product.setCreationDate(new Date());
		product.setDescription("Defined as a Product");
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
			message = "message_successfully_created";
		} else {
			jJProductService.updateJJProduct(product);
			message = "message_successfully_updated";
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("productDialogWidget.hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProduct");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	
	public void dialogClose(CloseEvent event) {
		newProduct();
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

				jJRequirementBean
						.setMyBusinessJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"BUSINESS", project, product));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"FUNCTIONAL", project, product));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"TECHNICAL", project, product));

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
					.getAllJJRequirementsWithCategoryAndProject("BUSINESS",
							project));

			jJRequirementBean
					.setMyFunctionalJJRequirements(jJRequirementService
							.getAllJJRequirementsWithCategoryAndProject(
									"FUNCTIONAL", project));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("TECHNICAL",
							project));

		}
	}
}

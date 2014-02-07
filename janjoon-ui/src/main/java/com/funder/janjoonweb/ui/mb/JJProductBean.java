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
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

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

	private JJProduct product;
	private List<JJProduct> productList;

	private JJProduct productAdmin;
	private List<JJProduct> productListTable;

	private JJContact productManager;
	private List<JJContact> productManagerList;

	private boolean disabled = true;
	private JJProject project;
	private String message;

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

	public JJProduct getProductAdmin() {
		return productAdmin;
	}

	public void setProductAdmin(JJProduct productAdmin) {
		this.productAdmin = productAdmin;
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

		List<JJRight> rights = jJRightService.getObjectWriterList("JJProduct");
		System.out.println("rights " + rights.size());
		for (JJRight right : rights) {
			List<JJPermission> permissions = jJPermissionService
					.getManagerPermissions(right.getProfile());
			for (JJPermission permission : permissions) {
				productManagerList.add(permission.getContact());
			}
		}
		return productManagerList;
	}

	public void setProductManagerList(List<JJContact> productManagerList) {
		this.productManagerList = productManagerList;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
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

	public void handleSelectProduct(JJVersionBean jJVersionBean,
			JJProjectBean jJProjectBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {

		if (product != null) {

			// FacesMessage message = new
			// FacesMessage(FacesMessage.SEVERITY_INFO,
			// "Product selected: " + product.getName(), "Selection info");
			// FacesContext.getCurrentInstance().addMessage(null, message);

			// Requete getReqwithProject&Product
			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(false);
				jJVersionBean.setProduct(null);
				jJVersionBean.setProduct(product);
				jJVersionBean.setProject(project);
			}

			if (jJRequirementBean != null) {

				jJRequirementBean.setProduct(product);

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
				jJChapterBean.setProduct(product);

		} else {
			// IF PRODUCT IS NULL GET ALL JJREQUIRMENTS WITH PROJECT

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setProduct(null);
			}

			jJRequirementBean.setProduct(product);
			jJChapterBean.setProduct(product);

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

	public void newProduct(JJVersionBean jJVersionBean) {
		System.out.println("Initial bean product");
		message = "New Product";
		productAdmin = new JJProduct();
		productAdmin.setEnabled(true);
		productAdmin.setCreationDate(new Date());
		productAdmin.setDescription("Defined as a Product");
		productManager = null;
		jJVersionBean.newVersion();

	}

	public void save(JJVersionBean jJVersionBean) {
		System.out.println("SAVING Product...");
		String message = "";

		if (productAdmin.getId() == null) {
			System.out.println("IS a new JJProduct");
			productAdmin.setManager(productManager);
			jJProductService.saveJJProduct(productAdmin);
			message = "message_successfully_created";

			List<JJVersion> versions = jJVersionBean.getVersionListTable();

			if (versions != null && !versions.isEmpty()) {
				JJProduct product = jJProductService.findJJProduct(productAdmin
						.getId());
				product.getVersions().addAll(versions);
				for (JJVersion jjVersion : versions) {
					// System.out.println(jjVersion.getName());
					jjVersion.setProduct(product);
				}
				jJProductService.updateJJProduct(product);
			}

			// //
			// product = jJProductService.findJJProduct(product.getId());
			// System.out.println("Seconf round");
			// for (JJVersion jjVersion : product.getVersions()) {
			//
			//
			// System.out.println(jjVersion.getName());
			//
			// }
			// //
			newProduct(jJVersionBean);

		} else {
			jJProductService.updateJJProduct(productAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("productDialogWidget.hide()");
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProduct");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		String summary = productAdmin.getEnabled() ? "Enabled Product"
				: "Disabled Product";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog(JJVersionBean jJVersionBean) {
		System.out.println("close dialog");
		productAdmin = null;
		productManager = null;
		productManagerList = null;
		jJVersionBean.setVersionAdmin(null);
		jJVersionBean.setVersionListTable(null);
		jJVersionBean.setVersionDataModel(null);
		
	}

	public void handleSelectProductManager() {
		System.out.println(productManager.getFirstname() + " "
				+ productManager.getName());
	}

}

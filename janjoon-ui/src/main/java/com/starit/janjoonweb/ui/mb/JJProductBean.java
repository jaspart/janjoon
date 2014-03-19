package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

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

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
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
	private Set<JJContact> productManagerList;

	private String message;

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {

		this.product = product;

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		jJVersionBean.setVersion(null);
		if (product != null) {
			jJVersionBean.setDisabled(false);
		} else {
			jJVersionBean.setDisabled(true);
		}
	}

	public List<JJProduct> getProductList() {
		productList = jJProductService.getProducts(true);

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
		productListTable = jJProductService.getProducts(true);
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

	public Set<JJContact> getProductManagerList() {

		productManagerList = jJPermissionService.getManagers("JJProduct");

		return productManagerList;
	}

	public void setProductManagerList(Set<JJContact> productManagerList) {
		this.productManagerList = productManagerList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public void editProduct(JJVersionBean jJVersionBean) {
		System.out.println("Update bean product");
		message = "Edit Product";
		productManager = productAdmin.getManager();
		jJVersionBean.newVersion();
	}

	public void deleteProduct() {

		if (productAdmin != null) {
			System.out.println(productAdmin.getName());

			productAdmin.setEnabled(false);
			jJProductService.updateJJProduct(productAdmin);

		}
	}

	public void save(JJVersionBean jJVersionBean) {
		System.out.println("SAVING Product...");
		String message = "";

		List<JJVersion> versions = jJVersionBean.getVersionListTable();

		if (!versions.isEmpty()) {

			productAdmin.getVersions().addAll(versions);
			for (JJVersion version : versions) {
				version.setProduct(productAdmin);
			}
		}

		if (productAdmin.getId() == null) {
			System.out.println("IS a new JJProduct");

			productAdmin.setManager(productManager);
			jJProductService.saveJJProduct(productAdmin);
			message = "message_successfully_created";

			newProduct(jJVersionBean);

		} else {

			productAdmin.setUpdatedDate(new Date());

			jJProductService.updateJJProduct(productAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("productDialogWidget.hide()");
			closeDialog(jJVersionBean);
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

	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project) {
		return jJTaskService.getTasks(project, product, null, true);
	}

	public void handleSelectProductManager() {

		if (productManager != null) {
			System.out.println(productManager.getFirstname() + " "
					+ productManager.getName());
		}
	}

}

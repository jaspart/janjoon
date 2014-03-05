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
	private JJProduct productTemp;
	private List<JJProduct> productList;

	private JJProduct productAdmin;
	private List<JJProduct> productListTable;

	private JJContact productManager;
	private List<JJContact> productManagerList;

	private JJProject project;

	private String message;

	public JJProduct getProduct() {
		if (product == null) {
			product = productTemp;
		}

		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public List<JJProduct> getProductList() {
		productList = jJProductService.getProducts(true);

		productTemp = new JJProduct();
		productTemp.setId(Long.valueOf("0"));
		productTemp.setName("Select All");
		productList.add(0, productTemp);
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

	public List<JJContact> getProductManagerList() {

		productManagerList = new ArrayList<JJContact>();

		List<JJRight> rights = jJRightService.getObjectWriterList("JJProduct");
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
			JJRequirementBean jJRequirementBean, JJChapterBean jJChapterBean) {

		jJRequirementBean.setProduct(product);

		jJChapterBean.setProduct(product);

		jJChapterBean.setVersion(null);

		if (product != null) {

			jJVersionBean.setDisabled(false);
			jJVersionBean.setProduct(product);
			jJVersionBean.setVersion(null);

		} else {

			jJVersionBean.setDisabled(true);
			jJVersionBean.setProduct(null);
			jJVersionBean.setVersion(null);

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

	public void deleteProduct() {
		// message = "Edit Contact";

		if (productAdmin != null) {
			System.out.println(productAdmin.getName());

			productAdmin.setEnabled(false);
			jJProductService.updateJJProduct(productAdmin);

		}
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

		if (productManager != null) {
			System.out.println(productManager.getFirstname() + " "
					+ productManager.getName());
		}
	}

}

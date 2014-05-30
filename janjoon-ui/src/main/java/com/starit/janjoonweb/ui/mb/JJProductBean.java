package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
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

import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;
import com.starit.janjoonweb.ui.mb.JJVersionBean.VersionDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	@Autowired
	JJVersionService jJVersionService;

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
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

	private boolean disabledProductMode;
	private boolean disabledVersionMode;

	private boolean productState;

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

	public boolean getDisabledProductMode() {
		return disabledProductMode;
	}

	public void setDisabledProductMode(boolean disabledProductMode) {
		this.disabledProductMode = disabledProductMode;
	}

	public boolean getDisabledVersionMode() {
		return disabledVersionMode;
	}

	public void setDisabledVersionMode(boolean disabledVersionMode) {
		this.disabledVersionMode = disabledVersionMode;
	}

	public void newProduct(JJVersionBean jJVersionBean) {

		message = "New Product";
		productAdmin = new JJProduct();
		productAdmin.setEnabled(true);
		productAdmin.setCreationDate(new Date());
		productAdmin.setDescription("Defined as a Product");
		productManager = null;

		jJVersionBean.setVersionDataModel(null);
		jJVersionBean.setDisabledCheckVersion(true);
		jJVersionBean.newVersion();

		disabledProductMode = false;
		disabledVersionMode = true;

		productState = true;

	}

	public void editProduct(JJVersionBean jJVersionBean) {

		message = "Edit Product";

		getProductManagerList();

		if (productManagerList.isEmpty()) {
			productManager = null;

		} else {
			if (productManagerList.contains(productAdmin.getManager())) {
				productManager = productAdmin.getManager();
			} else {
				productManager = null;
			}
		}

		System.out.println("vovo");

		jJVersionBean.setDisabledCheckVersion(false);
		jJVersionBean.newVersion();
		jJVersionBean.fillVersionTable(productAdmin);

		disabledProductMode = false;
		disabledVersionMode = false;

		productState = false;
	}

	public void deleteProduct() {

		if (productAdmin != null) {
			productAdmin.setEnabled(false);
			jJProductService.updateJJProduct(productAdmin);
		}
	}

	public void addProduct(JJVersionBean jJVersionBean) {

		productAdmin.setManager(productManager);
		if (productAdmin.getId() == null) {

			jJProductService.saveJJProduct(productAdmin);

			disabledProductMode = true;
			disabledVersionMode = false;

			jJVersionBean
					.setVersionDataModel(new ArrayList<VersionDataModel>());

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							"JJProduct"));

		}
	}

	public void save(JJVersionBean jJVersionBean) {

		System.out.println("in save version");

		productAdmin.setManager(productManager);
		productAdmin.setUpdatedDate(new Date());

		jJProductService.updateJJProduct(productAdmin);

		System.out.println("pppp");

		List<JJVersion> versions = jJVersionService.getVersions(true, true,
				productAdmin);

		List<VersionDataModel> versionDataModels = jJVersionBean
				.getVersionDataModel();
		List<JJVersion> selectedVersions = new ArrayList<JJVersion>();
		for (VersionDataModel versionDataModel : versionDataModels) {
			if (versionDataModel.getCheckVersion()) {
				selectedVersions.add(versionDataModel.getVersion());

			}
		}

		if (!selectedVersions.isEmpty() && !versions.isEmpty()) {

			for (JJVersion version : selectedVersions) {
				if (version.getId() == null) {
					version.setProduct(productAdmin);

					productAdmin.getVersions().add(version);
					jJVersionService.saveJJVersion(version);
				}
			}

			for (JJVersion version : versions) {
				if (!selectedVersions.contains(version)) {
					version.setEnabled(false);
					version.setUpdatedDate(new Date());
					jJVersionService.updateJJVersion(version);
				}
			}

		} else if (selectedVersions.isEmpty() && !versions.isEmpty()) {

			for (JJVersion version : versions) {
				version.setEnabled(false);
				version.setUpdatedDate(new Date());
				jJVersionService.updateJJVersion(version);
			}

		} else if (!selectedVersions.isEmpty() && versions.isEmpty()) {

			for (JJVersion version : selectedVersions) {
				version.setProduct(productAdmin);

				productAdmin.getVersions().add(version);
				jJVersionService.saveJJVersion(version);
			}

		}

		System.out.println("herer");

		FacesContext.getCurrentInstance().addMessage(
				null,
				MessageFactory.getMessage("message_successfully_updated",
						"JJProduct"));

		RequestContext context = RequestContext.getCurrentInstance();

		if (productState) {
			if (getProductDialogConfiguration()) {
				context.execute("productDialogWidget.hide()");
			} else {
				newProduct(jJVersionBean);
			}
		} else {
			context.execute("productDialogWidget.hide()");
		}

		System.out.println("dfgdfgf");
	}

	public void addMessage() {
		String summary = productAdmin.getEnabled() ? "Enabled Product"
				: "Disabled Product";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog(JJVersionBean jJVersionBean) {

		productAdmin = null;
		productManager = null;
		productManagerList = null;
		jJVersionBean.setVersionAdmin(null);
		jJVersionBean.setVersionDataModel(null);

		productState = true;
	}

	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project) {
		return jJTaskService.getTasksByProduct(product, project);
	}

	private boolean getProductDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProductDialog",
				"product.create.saveandclose");
	}

}

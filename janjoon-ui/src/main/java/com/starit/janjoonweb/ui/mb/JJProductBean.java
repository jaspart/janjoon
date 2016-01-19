package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.JJVersionBean.VersionDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyProductDataModel;
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
	private JJProduct productAdmin;
	private LazyProductDataModel productListTable;
	private JJContact productManager;
	private List<JJContact> productManagerList;
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
		jJVersionBean.setVersionList(null);
		jJVersionBean.setVersionDataModelList(null);
		if (product != null) {
			jJVersionBean.setDisabled(false);
		} else {
			jJVersionBean.setDisabled(true);
		}
	}

	public List<JJProduct> getProductList() {

		if (((LoginBean) LoginBean.findBean("loginBean")).isEnable())
			return jJProductService.getProducts(LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					true, false);
		else
			return new ArrayList<JJProduct>();
	}

	public JJProduct getProductAdmin() {
		return productAdmin;
	}

	public void setProductAdmin(JJProduct productAdmin) {
		this.productAdmin = productAdmin;
	}

	public LazyProductDataModel getProductListTable() {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany())
			company = LoginBean.getCompany();

		if (productListTable == null)
			productListTable = new LazyProductDataModel(jJProductService,
					company);
		return productListTable;
	}

	public void setProductListTable(LazyProductDataModel productListTable) {
		this.productListTable = productListTable;
	}

	public JJContact getProductManager() {
		return productManager;
	}

	public void setProductManager(JJContact productManager) {
		this.productManager = productManager;
	}

	public List<JJContact> getProductManagerList() {

		if (productAdmin.getId() == null)
			productManagerList = jJPermissionService.getManagers(
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					"Product");
		else
			productManagerList = jJPermissionService.getManagers(productAdmin
					.getManager().getCompany(), ((LoginBean) LoginBean
					.findBean("loginBean")).getContact(), "Product");

		return productManagerList;
	}

	public void setProductManagerList(List<JJContact> productManagerList) {
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
		message = "admin_product_new_title";
		productAdmin = new JJProduct();
		productAdmin.setEnabled(true);
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
		message = "admin_product_edit_title";
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
			updateJJProduct(productAdmin);
			resetVersionProductList();
		}
	}

	public void addProduct(JJVersionBean jJVersionBean) {
		productAdmin.setManager(productManager);
		if (productAdmin.getId() == null) {

			saveJJProduct(productAdmin);
			resetVersionProductList();
			disabledProductMode = true;
			disabledVersionMode = false;

			jJVersionBean
					.setVersionDataModel(new ArrayList<VersionDataModel>());

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							MessageFactory.getMessage("label_product", "")
									.getDetail(), ""));
		}
	}

	public void save(JJVersionBean jJVersionBean) {
		System.out.println("in save version");

		productAdmin.setManager(productManager);

		updateJJProduct(productAdmin);
		resetVersionProductList();

		List<JJVersion> versions = jJVersionService.getVersions(true, true,
				productAdmin, LoginBean.getCompany(), true);

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
					jJVersionBean.saveJJVersion(version);
				}
			}

			for (JJVersion version : versions) {
				if (!selectedVersions.contains(version)) {
					version.setEnabled(false);
					jJVersionBean.updateJJVersion(version);
				}
			}

		} else if (selectedVersions.isEmpty() && !versions.isEmpty()) {
			for (JJVersion version : versions) {
				version.setEnabled(false);
				jJVersionBean.updateJJVersion(version);
			}
		} else if (!selectedVersions.isEmpty() && versions.isEmpty()) {
			for (JJVersion version : selectedVersions) {
				version.setProduct(productAdmin);
				jJVersionBean.saveJJVersion(version);
			}
		}

		FacesContext.getCurrentInstance().addMessage(
				null,
				MessageFactory.getMessage("message_successfully_updated",
						MessageFactory.getMessage("label_product", "")
								.getDetail(), ""));

		RequestContext context = RequestContext.getCurrentInstance();

		if (productState) {
			if (getProductDialogConfiguration()) {
				context.execute("PF('productDialogWidget').hide()");
			} else {
				newProduct(jJVersionBean);
			}
		} else {
			context.execute("PF('productDialogWidget').hide()");
		}
	}

	public void addMessage() {
		String summary = productAdmin.getEnabled() ? "Enabled Product"
				: "Disabled Product";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog(JJVersionBean jJVersionBean, JJBuildBean jJBuildBean) {

		productAdmin = null;
		productManager = null;
		productManagerList = null;
		jJBuildBean.setBuildUtils(null);
		jJBuildBean.setIndex(-1);
		jJVersionBean.setVersionAdmin(null);
		jJVersionBean.setVersionDataModel(null);

		productState = true;
	}

	public void resetVersionProductList() {

		productListTable = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		jJVersionBean.setVersionList(null);

	}

	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project) {
		return jJTaskService.getTasksByProduct(product, project);
	}

	private boolean getProductDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProductDialog",
				"product.create.saveandclose");
	}

	public void saveJJProduct(JJProduct b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJProductService.saveJJProduct(b);
	}

	public void updateJJProduct(JJProduct b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJProductService.updateJJProduct(b);
	}

}

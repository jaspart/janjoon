package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
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
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyProductDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProduct.class, beanName = "jJProductBean")
public class JJProductBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	@Autowired
	JJVersionService jJVersionService;

	@Autowired
	JJTaskService jJTaskService;

	@Autowired
	JJPermissionService jJPermissionService;

	private JJProduct product;

	private JJProduct productAdmin;

	private LazyProductDataModel productListTable;

	private JJContact productManager;

	private List<JJContact> productManagerList;
	private String message;
	private boolean disabledProductMode;
	private boolean disabledVersionMode;
	private boolean productState;

	public void addMessage() {
		final String summary = productAdmin.getEnabled()
				? "Enabled Product"
				: "Disabled Product";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void addProduct(final JJVersionBean jJVersionBean) {
		productAdmin.setManager(productManager);
		if (productAdmin.getId() == null) {

			saveJJProduct(productAdmin);
			resetVersionProductList();
			disabledProductMode = true;
			disabledVersionMode = false;

			jJVersionBean
					.setVersionDataModel(new ArrayList<VersionDataModel>());

			FacesContext.getCurrentInstance().addMessage(null,
					MessageFactory.getMessage("message_successfully_created",
							MessageFactory.getMessage("label_product", "")
									.getDetail(),
							""));
		}
	}

	public void closeDialog(final JJVersionBean jJVersionBean,
			final JJBuildBean jJBuildBean) {

		productAdmin = null;
		productManager = null;
		productManagerList = null;
		jJBuildBean.setBuildUtils(null);
		jJBuildBean.setIndex(-1);
		jJVersionBean.setVersionAdmin(null);
		jJVersionBean.setVersionDataModel(null);

		productState = true;
	}

	public void deleteProduct() {
		if (productAdmin != null) {
			productAdmin.setEnabled(false);
			updateJJProduct(productAdmin);
			resetVersionProductList();
		}
	}

	public void editProduct(final JJVersionBean jJVersionBean) {
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

	public boolean getDisabledProductMode() {
		return disabledProductMode;
	}

	public boolean getDisabledVersionMode() {
		return disabledVersionMode;
	}

	public String getMessage() {
		return message;
	}

	public JJProduct getProduct() {
		return product;
	}

	public JJProduct getProductAdmin() {
		return productAdmin;
	}

	private boolean getProductDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProductDialog",
				"product.create.saveandclose");
	}

	public List<JJProduct> getProductList() {

		if (((LoginBean) LoginBean.findBean("loginBean")).isEnable()) {
			return jJProductService.getProducts(LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					true, false);
		} else {
			return new ArrayList<JJProduct>();
		}
	}

	public LazyProductDataModel getProductListTable() {

		final LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany()) {
			company = LoginBean.getCompany();
		}

		if (productListTable == null) {
			productListTable = new LazyProductDataModel(jJProductService,
					company);
		}
		return productListTable;
	}

	public JJContact getProductManager() {
		return productManager;
	}

	public List<JJContact> getProductManagerList() {

		if (productAdmin.getId() == null) {
			productManagerList = jJPermissionService.getManagers(
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					"Product");
		} else {
			productManagerList = jJPermissionService.getManagers(
					productAdmin.getManager().getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					"Product");
		}

		return productManagerList;
	}

	public List<JJTask> getTasksByProduct(final JJProduct product,
			final JJProject project) {
		return jJTaskService.getTasksByProduct(product, project);
	}

	public void newProduct(final JJVersionBean jJVersionBean) {
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

	public void resetVersionProductList() {

		productListTable = null;
		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		jJVersionBean.setVersionList(null);

	}

	public void save(final JJVersionBean jJVersionBean) {
		System.out.println("in save version");

		productAdmin.setManager(productManager);

		updateJJProduct(productAdmin);
		resetVersionProductList();

		final List<JJVersion> versions = jJVersionService.getVersions(true,
				true, productAdmin, LoginBean.getCompany(), true);

		final List<VersionDataModel> versionDataModels = jJVersionBean
				.getVersionDataModel();
		final List<JJVersion> selectedVersions = new ArrayList<JJVersion>();
		for (final VersionDataModel versionDataModel : versionDataModels) {
			if (versionDataModel.getCheckVersion()) {
				selectedVersions.add(versionDataModel.getVersion());

			}
		}

		if (!selectedVersions.isEmpty() && !versions.isEmpty()) {

			for (final JJVersion version : selectedVersions) {
				if (version.getId() == null) {
					version.setProduct(productAdmin);
					jJVersionBean.saveJJVersion(version);
				}
			}

			for (final JJVersion version : versions) {
				if (!selectedVersions.contains(version)) {
					version.setEnabled(false);
					jJVersionBean.updateJJVersion(version);
				}
			}

		} else if (selectedVersions.isEmpty() && !versions.isEmpty()) {
			for (final JJVersion version : versions) {
				version.setEnabled(false);
				jJVersionBean.updateJJVersion(version);
			}
		} else if (!selectedVersions.isEmpty() && versions.isEmpty()) {
			for (final JJVersion version : selectedVersions) {
				version.setProduct(productAdmin);
				jJVersionBean.saveJJVersion(version);
			}
		}

		FacesContext.getCurrentInstance().addMessage(null,
				MessageFactory.getMessage(
						"message_successfully_updated", MessageFactory
								.getMessage("label_product", "").getDetail(),
						""));

		final RequestContext context = RequestContext.getCurrentInstance();

		if (productState) {
			if (getProductDialogConfiguration()) {
				context.execute("PF('productDialogWidget').hide()");
				RequestContext.getCurrentInstance().update("growlForm");
			} else {
				newProduct(jJVersionBean);
			}
		} else {
			context.execute("PF('productDialogWidget').hide()");
			RequestContext.getCurrentInstance().update("growlForm");
		}
	}

	public void saveJJProduct(final JJProduct b) {

		b.setCreationDate(new Date());
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJProductService.saveJJProduct(b);
	}

	public void setDisabledProductMode(final boolean disabledProductMode) {
		this.disabledProductMode = disabledProductMode;
	}

	public void setDisabledVersionMode(final boolean disabledVersionMode) {
		this.disabledVersionMode = disabledVersionMode;
	}

	public void setjJConfigurationService(
			final JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(
			final JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJTaskService(final JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJVersionService(final JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setProduct(final JJProduct product) {

		this.product = product;

		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final JJVersionBean jJVersionBean = (JJVersionBean) session
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

	public void setProductAdmin(final JJProduct productAdmin) {
		this.productAdmin = productAdmin;
	}

	public void setProductListTable(
			final LazyProductDataModel productListTable) {
		this.productListTable = productListTable;
	}

	public void setProductManager(final JJContact productManager) {
		this.productManager = productManager;
	}

	public void setProductManagerList(
			final List<JJContact> productManagerList) {
		this.productManagerList = productManagerList;
	}

	public void updateJJProduct(final JJProduct b) {
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJProductService.updateJJProduct(b);
	}

	public HtmlPanelGrid populateCreatePanel() {
		return null;
	}

	public HtmlPanelGrid populateEditPanel() {
		return null;
	}

	public HtmlPanelGrid populateViewPanel() {
		return null;
	}

}

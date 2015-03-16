package com.starit.janjoonweb.ui.security;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.*;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

public class AuthorisationService implements Serializable {

	private static final long serialVersionUID = 1L;

	private HttpSession session;
	private JJCategory category;

	public void setSession(HttpSession session) {

		this.session = session;
		initFields();

		JJSprintBean jJSprintBean = (JJSprintBean) this.session.getAttribute("jJSprintBean");
		JJProjectBean jjProjectBean = (JJProjectBean) this.session.getAttribute("jJProjectBean");
		JJProductBean jjProductBean = (JJProductBean) this.session.getAttribute("jJProductBean");
		JJContactBean jjContactBean = (JJContactBean) this.session.getAttribute("jJContactBean");
		LoginBean loginBean = (LoginBean) this.session.getAttribute("loginBean");
		loginBean.setContact(null);
		loginBean.setMessageCount(null);
		jjContactBean.setCalendarUtil(null);
		jjContactBean.setContactsLazyModel(null);
		jjContactBean.setContactUtil(null);	
		jjProductBean.setProductListTable(null);
		jjProjectBean.setProjectListTable(null);
		jjContactBean.setCategories(null);
		jjContactBean.setVersionList(null);
		jjContactBean.setLoggedContactCategories(null);

		if (jJSprintBean != null) jJSprintBean.setContacts(null);
		
		RequestContext.getCurrentInstance().execute("updateAdmin()");
	}

	private JJContact contact;
	private boolean adminContact;
	private String adminContactMSG;
	private boolean adminProfil;
	private String adminProfilMSG;
	private boolean adminCompany;
	private String adminCompanyMSG;
	private boolean adminProject;
	private String adminProjectMSG;
	private boolean adminProduct;
	private String adminProductMSG;
	private boolean adminWorkflow;
	private String adminWorkflowMSG;
	private boolean adminCategory;
	private String adminCategoryMSG;
	private boolean adminConfiguration;
	private String adminConfigurationMSG;
	private boolean adminStatus;
	private String adminStatusMSG;
	private boolean rRequiement;
	private String rRequiementMSG;
	private boolean rTest;
	private String rTestMSG;
	private boolean wTest;
	private boolean xTest;
	private boolean rwDev;
	private String rwDevMSG;
	private boolean rProject;
	private String rProjectMSG;
	private boolean wProject;	
	private boolean rBuild;
	private String rBuildMSG;
	private boolean wRequiement;
	private String wRequiementMSG;
	private boolean rBug;
	private String rBugMSG;
	private boolean rContact;
	private String rContactMSG;
	private boolean rCompany;
	private String rCompanyMSG;
	private boolean renderAdmin;

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public boolean isAdminContact() {
		return adminContact;
	}

	public void setAdminContact(boolean adminContactProfil) {
		this.adminContact = adminContactProfil;
	}

	public String getAdminContactMSG() {
		return adminContactMSG;
	}

	public void setAdminContactMSG(String adminContactProfilMSG) {
		this.adminContactMSG = adminContactProfilMSG;
	}

	public boolean isAdminCompany() {
		return adminCompany;
	}

	public void setAdminCompany(boolean adminCompany) {
		this.adminCompany = adminCompany;
	}

	public String getAdminCompanyMSG() {
		return adminCompanyMSG;
	}

	public void setAdminCompanyMSG(String adminCompanyMSG) {
		this.adminCompanyMSG = adminCompanyMSG;
	}

	public boolean isAdminProject() {
		return adminProject;
	}

	public void setAdminProject(boolean adminProject) {
		this.adminProject = adminProject;
	}

	public String getAdminProjectMSG() {
		return adminProjectMSG;
	}

	public void setAdminProjectMSG(String adminProjectMSG) {
		this.adminProjectMSG = adminProjectMSG;
	}

	public boolean isAdminProduct() {
		return adminProduct;
	}

	public void setAdminProduct(boolean adminProduct) {
		this.adminProduct = adminProduct;
	}

	public String getAdminProductMSG() {
		return adminProductMSG;
	}

	public void setAdminProductMSG(String adminProductMSG) {
		this.adminProductMSG = adminProductMSG;
	}

	public boolean isAdminProfil() {
		return adminProfil;
	}

	public void setAdminProfil(boolean adminProfil) {
		this.adminProfil = adminProfil;
	}

	public String getAdminProfilMSG() {
		return adminProfilMSG;
	}

	public void setAdminProfilMSG(String adminProfilMSG) {
		this.adminProfilMSG = adminProfilMSG;
	}

	public boolean isAdminWorkflow() {
		return adminWorkflow;
	}

	public void setAdminWorkflow(boolean adminWorkflow) {
		this.adminWorkflow = adminWorkflow;
	}

	public String getAdminWorkflowMSG() {
		return adminWorkflowMSG;
	}

	public void setAdminWorkflowMSG(String adminWorkflowMSG) {
		this.adminWorkflowMSG = adminWorkflowMSG;
	}

	public boolean isAdminCategory() {
		return adminCategory;
	}

	public void setAdminCategory(boolean adminCategory) {
		this.adminCategory = adminCategory;
	}

	public String getAdminCategoryMSG() {
		return adminCategoryMSG;
	}

	public void setAdminCategoryMSG(String adminCategoryMSG) {
		this.adminCategoryMSG = adminCategoryMSG;
	}

	public boolean isAdminConfiguration() {
		return adminConfiguration;
	}

	public void setAdminConfiguration(boolean adminConfiguration) {
		this.adminConfiguration = adminConfiguration;
	}

	public String getAdminConfigurationMSG() {
		return adminConfigurationMSG;
	}

	public void setAdminConfigurationMSG(String adminConfigurationMSG) {
		this.adminConfigurationMSG = adminConfigurationMSG;
	}

	public boolean isAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(boolean adminStatus) {
		this.adminStatus = adminStatus;
	}

	public String getAdminStatusMSG() {
		return adminStatusMSG;
	}

	public void setAdminStatusMSG(String adminStatusMSG) {
		this.adminStatusMSG = adminStatusMSG;
	}

	public boolean isrRequiement() {
		return rRequiement;
	}

	public void setrRequiement(boolean rRequiement) {
		this.rRequiement = rRequiement;
	}

	public String getrRequiementMSG() {
		return rRequiementMSG;
	}

	public void setrRequiementMSG(String rRequiementMSG) {
		this.rRequiementMSG = rRequiementMSG;
	}

	public boolean isrTest() {
		return rTest;
	}

	public void setrTest(boolean rTest) {
		this.rTest = rTest;
	}

	public String getrTestMSG() {
		return rTestMSG;
	}

	public void setrTestMSG(String rTestMSG) {
		this.rTestMSG = rTestMSG;
	}

	public boolean iswTest() {
		return wTest;
	}

	public void setwTest(boolean wTest) {
		this.wTest = wTest;
	}

	public boolean isxTest() {
		return xTest;
	}

	public void setxTest(boolean xTest) {
		this.xTest = xTest;
	}

	public HttpSession getSession() {
		return session;
	}

	public boolean isRwDev() {
		return rwDev;
	}

	public void setRwDev(boolean rwDev) {
		this.rwDev = rwDev;
	}

	public String getRwDevMSG() {
		return rwDevMSG;
	}

	public void setRwDevMSG(String rwDevMSG) {
		this.rwDevMSG = rwDevMSG;
	}

	public boolean isrProject() {
		return rProject;
	}

	public void setrProject(boolean rProject) {
		this.rProject = rProject;
	}

	public String getrProjectMSG() {
		return rProjectMSG;
	}

	public void setrProjectMSG(String rProjectMSG) {
		this.rProjectMSG = rProjectMSG;
	}

	public boolean iswProject() {
		return wProject;
	}

	public void setwProject(boolean wProject) {
		this.wProject = wProject;
	}

	public boolean isrBuild() {
		return rBuild;
	}

	public void setrBuild(boolean rBuild) {
		this.rBuild = rBuild;
	}

	public String getrBuildMSG() {
		return rBuildMSG;
	}

	public void setrBuildMSG(String rBuildMSG) {
		this.rBuildMSG = rBuildMSG;
	}

	public boolean iswRequiement() {
		return wRequiement;
	}

	public void setwRequiement(boolean wRequiement) {
		this.wRequiement = wRequiement;
	}

	public String getwRequiementMSG() {
		return wRequiementMSG;
	}

	public void setwRequiementMSG(String wRequiementMSG) {
		this.wRequiementMSG = wRequiementMSG;
	}

	public boolean isrBug() {
		return rBug;
	}

	public void setrBug(boolean rBug) {
		this.rBug = rBug;
	}

	public String getrBugMSG() {
		return rBugMSG;
	}

	public void setrBugMSG(String rBugMSG) {
		this.rBugMSG = rBugMSG;
	}

	public boolean isrContact() {
		return rContact;
	}

	public void setrContact(boolean rContact) {
		this.rContact = rContact;
	}

	public String getrContactMSG() {
		return rContactMSG;
	}

	public void setrContactMSG(String rContactMSG) {
		this.rContactMSG = rContactMSG;
	}

	public String getrCompanyMSG() {
		return rCompanyMSG;
	}

	public void setrCompanyMSG(String rCompanyMSG) {
		this.rCompanyMSG = rCompanyMSG;
	}

	public boolean isrCompany() {
		return rCompany;
	}

	public void setrCompany(boolean rCompany) {
		this.rCompany = rCompany;
	}

	public boolean isRenderAdmin() {
		return renderAdmin;
	}

	public void setRenderAdmin(boolean renderAdmin) {
		this.renderAdmin = renderAdmin;
	}

	public AuthorisationService(HttpSession session, JJContact c) {
		this.session = session;
		this.contact = c;
		initFields();
	}

	public void initFields() {

		JJProjectBean projectBean = (JJProjectBean) session.getAttribute("jJProjectBean");
		JJProductBean productBean = (JJProductBean) session.getAttribute("jJProductBean");
		JJPermissionBean permissionBean = (JJPermissionBean) session.getAttribute("jJPermissionBean");

		if (permissionBean == null) permissionBean = new JJPermissionBean();

		JJPermissionService jJPermissionService = permissionBean.getJJPermissionService();

		if (category == null) category = jJPermissionService.getDefaultCategory(((LoginBean) LoginBean.findBean("loginBean")).getContact());
		if (projectBean == null) projectBean = new JJProjectBean();
		if (productBean == null) productBean = new JJProductBean();

		JJProject project = projectBean.getProject();
		JJProduct product = productBean.getProduct();

		adminContact = jJPermissionService.isAuthorized(contact, null, null, "Contact", null, null, null, true);
		if (!adminContact) {
			adminContactMSG = "Permission Denied";
			adminProfil = false;
			adminProfilMSG = "Permission Denied";
		} else {
			adminContactMSG = "";			
			adminProfil = jJPermissionService.isAuthorized(contact, null, null, "Profile", null, null, null, true);

			if (!adminProfil) {
				adminProfilMSG = "Permission Denied";
			}
			else {
				adminProfilMSG = "";
			}
		}

		adminCompany = jJPermissionService.isAuthorized(contact, null, null, "Company", null, null, null, true);
		renderAdmin=adminCompany||jJPermissionService.isAuthorized(contact, null, null, "Company", null, true, null, null);
		if (!adminCompany) {
			adminCompanyMSG = "Permission Denied";
		} else {			
			adminCompanyMSG = "";
		}

		adminProduct = jJPermissionService.isAuthorized(contact, null, null, "Product", null, null, null, true);
		if (!adminProduct) {
			adminProductMSG = "Permission Denied";
		} else {			
			adminProductMSG = "";
		}

		adminProject = jJPermissionService.isAuthorized(contact, null, null, "Project", null, null, null, true);
		if (!adminProject) {
			adminProjectMSG = "Permission Denied";
		} else {			
			adminProjectMSG = "";
		}

		adminWorkflow = jJPermissionService.isAuthorized(contact, null, null, "Workflow", null, null, null, true);
		if (!adminWorkflow) {
			adminWorkflowMSG = "Permission Denied";
		} else {			
			adminWorkflowMSG = "";
		}

		adminCategory = jJPermissionService.isAuthorized(contact, null, null, "Category", null, null, null, true);
		if (!adminCategory) {
			adminCategoryMSG = "Permission Denied";
		} else {
			adminCategoryMSG = "";
		}

		adminConfiguration = jJPermissionService.isAuthorized(contact, null, null, "Configuration", null, null, null, true);
		if (!adminConfiguration) {
			adminConfigurationMSG = "Permission Denied";
		} else {
			adminConfigurationMSG = "";
		}
		adminStatus = jJPermissionService.isAuthorized(contact, null, null, "Status", null, null, null, true);
		if (!adminStatus) {
			adminStatusMSG = "Permission Denied";
		} else {
			adminStatusMSG = "";
		}

		rRequiement = jJPermissionService.isAuthorized(contact, project, product, "Requirement", null, true, null, null);
		if (!rRequiement) {
			rRequiementMSG = "Permission Denied";
		} else {
			rRequiementMSG = MessageFactory.getMessage("header_spec_menuitem", "").getDetail();
		}
		
		rTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, true, null, null);
		wTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, null, true, null);
		xTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, null, null, true);
		if (!rTest) {
			rTestMSG = "Permission Denied";
		} else {
			rTestMSG = MessageFactory.getMessage("header_test_menuitem", "").getDetail();
		}
		
		rwDev = jJPermissionService.isAuthorized(contact, project, product, "Version", null, true, true, null);
		if (!rwDev) {
			rwDevMSG = "Permission Denied";
		} else {
			rwDevMSG = MessageFactory.getMessage("header_dev_menuitem", "").getDetail();		 
			
		}
		
		rProject = jJPermissionService.isAuthorized(contact, project, product, "Project", null, true, null, null);
		wProject = jJPermissionService.isAuthorized(contact, project, product, "Project", null, null, true, null);
		if (!rProject) {
			rProjectMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rProjectMSG = MessageFactory.getMessage("header_project_menuitem", "").getDetail();
		}
		
		rBuild = jJPermissionService.isAuthorized(contact, project, product, "Build", null, true, null, null);
		if (!rBuild) {
			rBuildMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rBuildMSG = MessageFactory.getMessage("header_delivery_menuitem", "").getDetail();
		}
		
		rBug = jJPermissionService.isAuthorized(contact, project, product, "Bug", null, true, null, null);
		if (!rBug) {
			rBugMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rBugMSG = MessageFactory.getMessage("header_bug_menuitem", "").getDetail();
		}
		
		rContact = jJPermissionService.isAuthorized(contact, null, null, "Contact", null, true, null, null);
		if (!rContact) {
			rContactMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rContactMSG = MessageFactory.getMessage("header_team_menuitem", "").getDetail();
		}

		rCompany = jJPermissionService.isAuthorized(contact, null, null, "Company", null, true, null, null);
		if (!rCompany) {
			rCompanyMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rCompanyMSG = MessageFactory.getMessage("header_admin_menuitem", "").getDetail();
		}
		
		if (project != null) {
			wRequiement = jJPermissionService.isAuthorized(contact, project, product, "Requirement", null, null, true, null);
			if (!wRequiement)
				wRequiementMSG = "You Have no permisson to do this action";
			else
				wRequiementMSG = "New Requirement";
		} else {
			wRequiement = false;
			wRequiementMSG = "Select a project to create requiremen";
		}
	}
}

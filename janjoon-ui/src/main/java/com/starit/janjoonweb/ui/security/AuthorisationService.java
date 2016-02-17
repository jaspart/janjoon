package com.starit.janjoonweb.ui.security;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.JJCompanyBean;
import com.starit.janjoonweb.ui.mb.JJContactBean;
import com.starit.janjoonweb.ui.mb.JJPermissionBean;
import com.starit.janjoonweb.ui.mb.JJProductBean;
import com.starit.janjoonweb.ui.mb.JJProjectBean;
import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.util.Contact;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

public class AuthorisationService implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private HttpSession			session;
	private JJCategory			category;

	private JJContact			contact;

	private boolean				adminContact;
	private String				adminContactMSG;
	private boolean				adminProfil;
	private String				adminProfilMSG;
	private boolean				adminCompany;
	private String				adminCompanyMSG;
	private boolean				adminProject;
	private String				adminProjectMSG;
	private boolean				adminProduct;
	private String				adminProductMSG;
	private boolean				adminWorkflow;
	private String				adminWorkflowMSG;
	private boolean				adminCategory;
	private String				adminCategoryMSG;
	private boolean				adminConfiguration;
	private String				adminConfigurationMSG;
	private boolean				adminStatus;
	private String				adminStatusMSG;
	private boolean				rRequiement;
	private String				rRequiementMSG;
	private boolean				rTest;
	private String				rTestMSG;
	private boolean				wTest;
	private boolean				xTest;
	private boolean				rwDev;
	private String				rwDevMSG;
	private boolean				rProject;
	private String				rProjectMSG;
	private boolean				wProject;
	private boolean				rBuild;
	private String				rBuildMSG;
	private boolean				wRequiement;
	private String				wRequiementMSG;
	private boolean				rBug;
	private String				rBugMSG;
	private boolean				rContact;
	private String				rContactMSG;
	private boolean				rCompany;
	private String				rCompanyMSG;
	private boolean				renderAdmin;
	private boolean				viewTeamImputation;
	private boolean				superAdmin;

	public AuthorisationService(final HttpSession session, final JJContact c) {
		this.session = session;
		contact = c;
		initFields();
	}

	public String getAdminCategoryMSG() {
		return adminCategoryMSG;
	}

	public String getAdminCompanyMSG() {
		return adminCompanyMSG;
	}

	public String getAdminConfigurationMSG() {
		return adminConfigurationMSG;
	}

	public String getAdminContactMSG() {
		return adminContactMSG;
	}

	public String getAdminProductMSG() {
		return adminProductMSG;
	}

	public String getAdminProfilMSG() {
		return adminProfilMSG;
	}

	public String getAdminProjectMSG() {
		return adminProjectMSG;
	}

	public String getAdminStatusMSG() {
		return adminStatusMSG;
	}

	public String getAdminWorkflowMSG() {
		return adminWorkflowMSG;
	}

	public JJCategory getCategory() {
		return category;
	}

	public JJContact getContact() {
		return contact;
	}

	public String getrBugMSG() {
		return rBugMSG;
	}

	public String getrBuildMSG() {
		return rBuildMSG;
	}

	public String getrCompanyMSG() {
		return rCompanyMSG;
	}

	public String getrContactMSG() {
		return rContactMSG;
	}

	public String getrProjectMSG() {
		return rProjectMSG;
	}

	public String getrRequiementMSG() {
		return rRequiementMSG;
	}

	public String getrTestMSG() {
		return rTestMSG;
	}

	public String getRwDevMSG() {
		return rwDevMSG;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getwRequiementMSG() {
		return wRequiementMSG;
	}

	public void initFields() {

		JJProjectBean projectBean = (JJProjectBean) session.getAttribute("jJProjectBean");
		JJProductBean productBean = (JJProductBean) session.getAttribute("jJProductBean");
		JJPermissionBean permissionBean = (JJPermissionBean) session.getAttribute("jJPermissionBean");

		if (permissionBean == null) {
			permissionBean = new JJPermissionBean();
		}

		final JJPermissionService jJPermissionService = permissionBean.getJJPermissionService();

		superAdmin = jJPermissionService.isSuperAdmin(contact);

		if (category == null) {
			category = jJPermissionService
			        .getDefaultCategory(((LoginBean) LoginBean.findBean("loginBean")).getContact());
		}
		if (projectBean == null) {
			projectBean = new JJProjectBean();
		}
		if (productBean == null) {
			productBean = new JJProductBean();
		}

		final JJProject project = projectBean.getProject();
		final JJProduct product = productBean.getProduct();

		adminContact = jJPermissionService.isAdmin(contact);
		if (!adminContact) {
			adminContactMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
			adminProfil = false;
			adminProfilMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminContactMSG = "";
			adminProfil = jJPermissionService.isAuthorized(contact, null, null, "Profile", null, null, null, true);

			if (!adminProfil) {
				adminProfilMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
			} else {
				adminProfilMSG = "";
			}
		}

		adminCompany = jJPermissionService.isAuthorized(contact, null, null, "Company", null, true, true, true);
		renderAdmin = adminCompany || adminContact
		        || jJPermissionService.isAuthorized(contact, null, null, "Company", null, true, null, null);
		if (!adminCompany) {
			adminCompanyMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminCompanyMSG = "";
		}

		adminProduct = jJPermissionService.isAuthorized(contact, null, null, "Product", null, null, null, true);
		if (!adminProduct) {
			adminProductMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminProductMSG = "";
		}

		adminProject = jJPermissionService.isAuthorized(contact, null, null, "Project", null, null, null, true);
		if (!adminProject) {
			adminProjectMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminProjectMSG = "";
		}

		adminWorkflow = jJPermissionService.isAuthorized(contact, null, null, "Workflow", null, null, null, true);
		if (!adminWorkflow) {
			adminWorkflowMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminWorkflowMSG = "";
		}

		adminCategory = jJPermissionService.isAuthorized(contact, null, null, "Category", null, null, null, true);
		if (!adminCategory) {
			adminCategoryMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminCategoryMSG = "";
		}

		adminConfiguration = jJPermissionService.isAuthorized(contact, null, null, "Configuration", null, null, null,
		        true);
		if (!adminConfiguration) {
			adminConfigurationMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminConfigurationMSG = "";
		}
		adminStatus = jJPermissionService.isAuthorized(contact, null, null, "Status", null, null, null, true);
		if (!adminStatus) {
			adminStatusMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			adminStatusMSG = "";
		}

		rRequiement = jJPermissionService.isAuthorized(contact, project, product, "Requirement", null, true, null,
		        null);
		if (!rRequiement) {
			rRequiementMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rRequiementMSG = MessageFactory.getMessage("header_spec_menuitemhelp", "").getDetail();
		}

		rTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, true, null, null);
		wTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, null, true, null);
		xTest = jJPermissionService.isAuthorized(contact, project, product, "Testcase", null, null, null, true);
		if (!rTest) {
			rTestMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rTestMSG = MessageFactory.getMessage("header_test_menuitemhelp", "").getDetail();
		}

		rwDev = jJPermissionService.isAuthorized(contact, project, product, "Version", null, true, true, null);
		if (!rwDev) {
			rwDevMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rwDevMSG = MessageFactory.getMessage("header_dev_menuitemhelp", "").getDetail();

		}

		rProject = jJPermissionService.isAuthorized(contact, project, product, "Project", null, true, null, null);
		wProject = jJPermissionService.isAuthorized(contact, project, product, "Project", null, null, true, null);
		if (!rProject) {
			rProjectMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rProjectMSG = MessageFactory.getMessage("header_project_menuitemhelp", "").getDetail();
		}

		rBuild = jJPermissionService.isAuthorized(contact, project, product, "Build", null, true, null, null);
		if (!rBuild) {
			rBuildMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rBuildMSG = MessageFactory.getMessage("header_delivery_menuitemhelp", "").getDetail();
		}

		rBug = jJPermissionService.isAuthorized(contact, project, product, "Bug", null, true, null, null);
		if (!rBug) {
			rBugMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rBugMSG = MessageFactory.getMessage("header_bug_menuitemhelp", "").getDetail();
		}

		rContact = jJPermissionService.isAuthorized(contact, null, null, "Contact", null, true, null, null);
		if (!rContact) {
			rContactMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rContactMSG = MessageFactory.getMessage("header_team_menuitemhelp", "").getDetail();
		}

		rCompany = jJPermissionService.isAuthorized(contact, null, null, "Company", null, true, null, null);
		if (!rCompany) {
			rCompanyMSG = MessageFactory.getMessage("header_noPermission_menuitem", "").getDetail();
		} else {
			rCompanyMSG = MessageFactory.getMessage("header_admin_menuitemhelp", "").getDetail();
		}

		viewTeamImputation = jJPermissionService.isAuthorized(contact, null, product, "Contact", null, null, null, true)
		        || jJPermissionService.isAuthorized(contact, project, null, "Contact", null, null, null, true);

		if (project != null) {
			wRequiement = jJPermissionService.isAuthorized(contact, project, product, "Requirement", null, null, true,
			        null);
			if (!wRequiement) {
				wRequiementMSG = MessageFactory.getMessage("header_noAction_Permission_menuitem", "").getDetail();
			} else {
				wRequiementMSG = MessageFactory.getMessage("specification_table_requirement_new", "").getDetail();
			}
		} else {
			wRequiement = false;
			wRequiementMSG = MessageFactory.getMessage("specification_warning_lackProject", "").getDetail();
		}
	}

	public boolean isAdminCategory() {
		return adminCategory;
	}

	public boolean isAdminCompany() {
		return adminCompany;
	}

	public boolean isAdminConfiguration() {
		return adminConfiguration;
	}

	public boolean isAdminContact() {
		return adminContact;
	}

	public boolean isAdminProduct() {
		return adminProduct;
	}

	public boolean isAdminProfil() {
		return adminProfil;
	}

	public boolean isAdminProject() {
		return adminProject;
	}

	public boolean isAdminStatus() {
		return adminStatus;
	}

	public boolean isAdminWorkflow() {
		return adminWorkflow;
	}

	public boolean isrBug() {
		return rBug;
	}

	public boolean isrBuild() {
		return rBuild;
	}

	public boolean isrCompany() {
		return rCompany;
	}

	public boolean isrContact() {
		return rContact;
	}

	public boolean isRenderAdmin() {
		return renderAdmin;
	}

	public boolean isRenderCategoryConfig() {

		JJPermissionBean permissionBean = (JJPermissionBean) session.getAttribute("jJPermissionBean");

		if (permissionBean == null) {
			permissionBean = new JJPermissionBean();
		}

		final JJPermissionService jJPermissionService = permissionBean.getJJPermissionService();

		final JJContactBean jjContactBean = (JJContactBean) session.getAttribute("jJContactBean");
		final Contact con = jjContactBean.getContactUtil();

		return jJPermissionService.isAuthorized(contact, con.getLastProject(), con.getLastProduct(), "category", null,
		        true, null, null)
		        && jJPermissionService.isAuthorized(contact, con.getLastProject(), con.getLastProduct(), "requirement",
		                null, true, null, null);

	}

	public boolean isrProject() {
		return rProject;
	}

	public boolean isrRequiement() {
		return rRequiement;
	}

	public boolean isrTest() {
		return rTest;
	}

	public boolean isRwDev() {
		return rwDev;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public boolean isViewTeamImputation() {
		return viewTeamImputation;
	}

	public boolean iswProject() {
		return wProject;
	}

	public boolean iswRequiement() {
		return wRequiement;
	}

	public boolean iswTest() {
		return wTest;
	}

	public boolean isxTest() {
		return xTest;
	}

	public void setAdminCategory(final boolean adminCategory) {
		this.adminCategory = adminCategory;
	}

	public void setAdminCategoryMSG(final String adminCategoryMSG) {
		this.adminCategoryMSG = adminCategoryMSG;
	}

	public void setAdminCompany(final boolean adminCompany) {
		this.adminCompany = adminCompany;
	}

	public void setAdminCompanyMSG(final String adminCompanyMSG) {
		this.adminCompanyMSG = adminCompanyMSG;
	}

	public void setAdminConfiguration(final boolean adminConfiguration) {
		this.adminConfiguration = adminConfiguration;
	}

	public void setAdminConfigurationMSG(final String adminConfigurationMSG) {
		this.adminConfigurationMSG = adminConfigurationMSG;
	}

	public void setAdminContact(final boolean adminContactProfil) {
		adminContact = adminContactProfil;
	}

	public void setAdminContactMSG(final String adminContactProfilMSG) {
		adminContactMSG = adminContactProfilMSG;
	}

	public void setAdminProduct(final boolean adminProduct) {
		this.adminProduct = adminProduct;
	}

	public void setAdminProductMSG(final String adminProductMSG) {
		this.adminProductMSG = adminProductMSG;
	}

	public void setAdminProfil(final boolean adminProfil) {
		this.adminProfil = adminProfil;
	}

	public void setAdminProfilMSG(final String adminProfilMSG) {
		this.adminProfilMSG = adminProfilMSG;
	}

	public void setAdminProject(final boolean adminProject) {
		this.adminProject = adminProject;
	}

	public void setAdminProjectMSG(final String adminProjectMSG) {
		this.adminProjectMSG = adminProjectMSG;
	}

	public void setAdminStatus(final boolean adminStatus) {
		this.adminStatus = adminStatus;
	}

	public void setAdminStatusMSG(final String adminStatusMSG) {
		this.adminStatusMSG = adminStatusMSG;
	}

	public void setAdminWorkflow(final boolean adminWorkflow) {
		this.adminWorkflow = adminWorkflow;
	}

	public void setAdminWorkflowMSG(final String adminWorkflowMSG) {
		this.adminWorkflowMSG = adminWorkflowMSG;
	}

	public void setCategory(final JJCategory category) {
		this.category = category;
	}

	public void setContact(final JJContact contact) {
		this.contact = contact;
	}

	public void setrBug(final boolean rBug) {
		this.rBug = rBug;
	}

	public void setrBugMSG(final String rBugMSG) {
		this.rBugMSG = rBugMSG;
	}

	public void setrBuild(final boolean rBuild) {
		this.rBuild = rBuild;
	}

	public void setrBuildMSG(final String rBuildMSG) {
		this.rBuildMSG = rBuildMSG;
	}

	public void setrCompany(final boolean rCompany) {
		this.rCompany = rCompany;
	}

	public void setrCompanyMSG(final String rCompanyMSG) {
		this.rCompanyMSG = rCompanyMSG;
	}

	public void setrContact(final boolean rContact) {
		this.rContact = rContact;
	}

	public void setrContactMSG(final String rContactMSG) {
		this.rContactMSG = rContactMSG;
	}

	public void setRenderAdmin(final boolean renderAdmin) {
		this.renderAdmin = renderAdmin;
	}

	public void setrProject(final boolean rProject) {
		this.rProject = rProject;
	}

	public void setrProjectMSG(final String rProjectMSG) {
		this.rProjectMSG = rProjectMSG;
	}

	public void setrRequiement(final boolean rRequiement) {
		this.rRequiement = rRequiement;
	}

	public void setrRequiementMSG(final String rRequiementMSG) {
		this.rRequiementMSG = rRequiementMSG;
	}

	public void setrTest(final boolean rTest) {
		this.rTest = rTest;
	}

	public void setrTestMSG(final String rTestMSG) {
		this.rTestMSG = rTestMSG;
	}

	public void setRwDev(final boolean rwDev) {
		this.rwDev = rwDev;
	}

	public void setRwDevMSG(final String rwDevMSG) {
		this.rwDevMSG = rwDevMSG;
	}

	public void setSession(final HttpSession session) {

		this.session = session;
		initFields();

		final JJSprintBean jJSprintBean = (JJSprintBean) this.session.getAttribute("jJSprintBean");
		final JJProjectBean jjProjectBean = (JJProjectBean) this.session.getAttribute("jJProjectBean");
		final JJProductBean jjProductBean = (JJProductBean) this.session.getAttribute("jJProductBean");
		final JJContactBean jjContactBean = (JJContactBean) this.session.getAttribute("jJContactBean");
		final JJCompanyBean jjCompanyBean = (JJCompanyBean) this.session.getAttribute("jJCompanyBean");
		final LoginBean loginBean = (LoginBean) this.session.getAttribute("loginBean");
		loginBean.setContact(null);
		loginBean.setMessageCount(null);
		jjContactBean.setCalendarUtil(null);
		jjContactBean.setContactsLazyModel(null);
		jjContactBean.setContactUtil(null);
		jjProductBean.setProductListTable(null);
		jjProjectBean.setProjectListTable(null);
		jjContactBean.setVersionList(null);
		jjContactBean.setLoggedContactCategories(null);
		if (jjCompanyBean != null) {
			jjCompanyBean.setCompanies(null);
		}

		if (jJSprintBean != null) {
			jJSprintBean.setContacts(null);
		}

		RequestContext.getCurrentInstance().execute("updateAdmin()");
	}

	public void setSuperAdmin(final boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public void setViewTeamImputation(final boolean viewTeamImputation) {
		this.viewTeamImputation = viewTeamImputation;
	}

	public void setwProject(final boolean wProject) {
		this.wProject = wProject;
	}

	public void setwRequiement(final boolean wRequiement) {
		this.wRequiement = wRequiement;
	}

	public void setwRequiementMSG(final String wRequiementMSG) {
		this.wRequiementMSG = wRequiementMSG;
	}

	public void setwTest(final boolean wTest) {
		this.wTest = wTest;
	}

	public void setxTest(final boolean xTest) {
		this.xTest = xTest;
	}
}

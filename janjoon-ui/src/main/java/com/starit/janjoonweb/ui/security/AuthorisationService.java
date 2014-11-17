package com.starit.janjoonweb.ui.security;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.JJPermissionBean;
import com.starit.janjoonweb.ui.mb.JJProductBean;
import com.starit.janjoonweb.ui.mb.JJProjectBean;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

public class AuthorisationService implements Serializable {

	private static final long serialVersionUID = 1L;

	private HttpSession session;

	public void setSession(HttpSession session) {
		this.session = session;
		initFields();
	}

	private JJContact contact;
	private boolean adminContactProfil;
	private String adminContactProfilMSG;
	private boolean adminCompany;
	private String adminCompanyMSG;
	private boolean rRequiement;
	private String rRequiementMSG;
	private boolean rwxTest;
	private String rwxTestMSG;
	private boolean rwDev;
	private String rwDevMSG;
	private boolean rProject;
	private String rProjectMSG;
	private boolean rBuild;
	private String rBuildMSG;
	private boolean wRequiement;
	private String wRequiementMSG;

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public boolean isAdminContactProfil() {
		return adminContactProfil;
	}

	public void setAdminContactProfil(boolean adminContactProfil) {
		this.adminContactProfil = adminContactProfil;
	}

	public String getAdminContactProfilMSG() {
		return adminContactProfilMSG;
	}

	public void setAdminContactProfilMSG(String adminContactProfilMSG) {
		this.adminContactProfilMSG = adminContactProfilMSG;
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

	public boolean isRwxTest() {
		return rwxTest;
	}

	public void setRwxTest(boolean rwxTest) {
		this.rwxTest = rwxTest;
	}

	public String getRwxTestMSG() {
		return rwxTestMSG;
	}

	public void setRwxTestMSG(String rwxTestMSG) {
		this.rwxTestMSG = rwxTestMSG;
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

	public AuthorisationService(HttpSession session, JJContact c) {		
		this.session = session;
		this.contact = c;
		initFields();
	}

	public void initFields() {			
		
		JJProjectBean projectBean=(JJProjectBean) session
				.getAttribute("jJProjectBean");
		
		JJProductBean productBean=(JJProductBean) session
				.getAttribute("jJProductBean");
		
		JJPermissionBean permissionBean=(JJPermissionBean) session.getAttribute("jJPermissionBean");
		if(permissionBean == null)
			permissionBean=new JJPermissionBean();
		
		JJPermissionService jJPermissionService=permissionBean.getJJPermissionService();		
		
		
		if(projectBean==null)
			projectBean=new JJProjectBean();
		if(productBean ==null)
			productBean=new JJProductBean();

		JJProject project = projectBean.getProject();
		JJProduct product = productBean.getProduct();		
		

		adminContactProfil = jJPermissionService.isAuthorized(contact, null,
				null, "Contact", null, true, true, true);

		if (!adminContactProfil)
			adminContactProfilMSG = "Permission Denied";
		else
			adminContactProfilMSG = "";
		
		adminCompany = jJPermissionService.isAuthorized(contact, null, null,
				"Company", null, true, true, true);

		if (!adminCompany)
			adminCompanyMSG = "Permission Denied";
		else
			adminCompanyMSG = "";
		
		rRequiement = jJPermissionService.isAuthorized(contact, project,
				product, "Requirement", null, true, null, null);
		
		if (!rRequiement)
			rRequiementMSG = "Permission Denied";
		else
			rRequiementMSG =MessageFactory.getMessage("header_spec_menuitem", "").getDetail();
		
		rwxTest = jJPermissionService.isAuthorized(contact, project, product,
				"Testcase", null, true, true, true);
		
		if (!rwxTest)
			rwxTestMSG = "Permission Denied";
		else
			rwxTestMSG = MessageFactory.getMessage("header_test_menuitem", "").getDetail();
		rwDev = jJPermissionService.isAuthorized(contact, project, product,
				"Version", null, true, true, null);
		
		if (!rwDev)

			rwDevMSG = "Permission Denied";
		else
			rwDevMSG = MessageFactory.getMessage("header_dev_menuitem", "").getDetail();
		
		rProject = jJPermissionService.isAuthorized(contact, project, null,
				"Task", null, true, null, null);
		
		if (!rProject)
			rProjectMSG = "You Have no permisson to access this Category";
		else
			rProjectMSG =  MessageFactory.getMessage("header_project_menuitem", "").getDetail();
		rBuild = jJPermissionService.isAuthorized(contact, project, product,
				"Build", null, true, null, null);
		if (!rBuild)
			rBuildMSG = "You Have no permisson to access this Category";
		else
			rBuildMSG = MessageFactory.getMessage("header_delivery_menuitem", "").getDetail();
		
		if(project != null)
		{
			wRequiement = jJPermissionService.isAuthorized(contact, project,
					product, "Requirement", null, null, true, null);
			if (!wRequiement)
				wRequiementMSG = "You Have no permisson to do this action";
			else
				wRequiementMSG = "New Requirement";
		}else
		{
			wRequiement=false;
			wRequiementMSG="Select a project to create requiremen";
		}
		

	}

}

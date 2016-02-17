package com.starit.janjoonweb.ui.mb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Configurable;

//@RooJsfApplicationBean
@ManagedBean(name = "applicationBean")
@RequestScoped
@Configurable
public class ApplicationBean {

	public String getColumnName(String column) {
		if (column == null || column.length() == 0) {
			return column;
		}
		final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
		final Matcher m = p.matcher(Character.toUpperCase(column.charAt(0)) + column.substring(1));
		final StringBuilder builder = new StringBuilder();
		while (m.find()) {
			builder.append(m.group()).append(" ");
		}
		return builder.toString().trim();
	}

	private MenuModel menuModel;

	@PostConstruct
	public void init() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		JJWorkflowBean jJWorkflowBean = (JJWorkflowBean) session.getAttribute("jJWorkflowBean");
		if (jJWorkflowBean == null)
			jJWorkflowBean = new JJWorkflowBean();

		menuModel = new DefaultMenuModel();

		DefaultSubMenu submenu;
		DefaultMenuItem item;

		// ///////////
		submenu = new DefaultSubMenu("AuditLog");
		submenu.setId("jJAuditLogSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJAuditLogMenuItem");
		item.setCommand("#{jJAuditLogBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJAuditLogMenuItem");
		item.setCommand("#{jJAuditLogBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////////

		// ///////////
		submenu = new DefaultSubMenu("Bug");
		submenu.setId("jJBugSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJBugMenuItem");
		item.setCommand("#{jJBugBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJBugMenuItem");
		item.setCommand("#{jJBugBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////////
		submenu = new DefaultSubMenu("Build");
		submenu.setId("jJBuildSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJBuildMenuItem");
		item.setCommand("#{jJBuildBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJBuildMenuItem");
		item.setCommand("#{jJBuildBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ////////////
		submenu = new DefaultSubMenu("Category");
		submenu.setId("jJCategorySubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJCategoryMenuItem");
		item.setCommand("#{jJCategoryBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJCategoryMenuItem");
		item.setCommand("#{jJCategoryBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Chapter");
		submenu.setId("jJChapterSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJChapterMenuItem");
		item.setCommand("#{jJChapterBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJChapterMenuItem");
		item.setCommand("#{jJChapterBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Company");
		submenu.setId("jJCompanySubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJCompanyMenuItem");
		item.setCommand("#{jJCompanyBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJCompanyMenuItem");
		item.setCommand("#{jJCompanyBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Configuration");
		submenu.setId("jJConfigurationSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJConfigurationMenuItem");
		item.setCommand("#{jJConfigurationBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJConfigurationMenuItem");
		item.setCommand("#{jJConfigurationBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //////
		submenu = new DefaultSubMenu("Contact");
		submenu.setId("jJContactSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJContactMenuItem");
		item.setCommand("#{jJContactBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJContactMenuItem");
		item.setCommand("#{jJContactBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //////
		submenu = new DefaultSubMenu("Criticity");
		submenu.setId("jJCriticitySubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJCriticityMenuItem");
		item.setCommand("#{jJCriticityBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJCriticityMenuItem");
		item.setCommand("#{jJCriticityBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //////
		submenu = new DefaultSubMenu("Hardware");
		submenu.setId("jJHardwareSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJHardwareMenuItem");
		item.setCommand("#{jJHardwareBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJHardwareMenuItem");
		item.setCommand("#{jJHardwareBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Importance");
		submenu.setId("jJImportanceSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJImportanceMenuItem");
		item.setCommand("#{jJImportanceBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJImportanceMenuItem");
		item.setCommand("#{jJImportanceBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //////
		submenu = new DefaultSubMenu("Imputation");
		submenu.setId("jJImputationSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJImputationMenuItem");
		item.setCommand("#{jJImputationBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJImputationMenuItem");
		item.setCommand("#{jJImputationBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //////
		submenu = new DefaultSubMenu("Job");
		submenu.setId("jJJobSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJJobMenuItem");
		item.setCommand("#{jJJobBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJJobMenuItem");
		item.setCommand("#{jJJobBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// /////
		submenu = new DefaultSubMenu("Message");
		submenu.setId("jJMessageSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJMessageMenuItem");
		item.setCommand("#{jJMessageBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJMessageMenuItem");
		item.setCommand("#{jJMessageBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ////////
		submenu = new DefaultSubMenu("Permission");
		submenu.setId("jJPermissionSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJPermissionMenuItem");
		item.setCommand("#{jJPermissionBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJPermissionMenuItem");
		item.setCommand("#{jJPermissionBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Phase");
		submenu.setId("jJPhaseSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJPhaseMenuItem");
		item.setCommand("#{jJPhaseBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJPhaseMenuItem");
		item.setCommand("#{jJPhaseBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///
		submenu = new DefaultSubMenu("Product");
		submenu.setId("jJProductSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJProductMenuItem");
		item.setCommand("#{jJProductBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJProductMenuItem");
		item.setCommand("#{jJProductBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Profile");
		submenu.setId("jJProfileSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJProfileMenuItem");
		item.setCommand("#{jJProfileBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJProfileMenuItem");
		item.setCommand("#{jJProfileBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///////
		submenu = new DefaultSubMenu("Project");
		submenu.setId("jJProjectSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJProjectMenuItem");
		item.setCommand("#{jJProjectBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJProjectMenuItem");
		item.setCommand("#{jJProjectBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// /////
		submenu = new DefaultSubMenu("Requirement");
		submenu.setId("jJRequirementSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJRequirementMenuItem");
		item.setCommand("#{jJRequirementBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJRequirementMenuItem");
		item.setCommand("#{jJRequirementBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///
		submenu = new DefaultSubMenu("Right");
		submenu.setId("jJRightSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJRightMenuItem");
		item.setCommand("#{jJRightBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJRightMenuItem");
		item.setCommand("#{jJRightBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// /////
		submenu = new DefaultSubMenu("Software");
		submenu.setId("jJSoftwareSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJSoftwareMenuItem");
		item.setCommand("#{jJSoftwareBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJSoftwareMenuItem");
		item.setCommand("#{jJSoftwareBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ////
		submenu = new DefaultSubMenu("Sprint");
		submenu.setId("jJSprintSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJSprintMenuItem");
		item.setCommand("#{jJSprintBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJSprintMenuItem");
		item.setCommand("#{jJSprintBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// /////
		submenu = new DefaultSubMenu("Status");
		submenu.setId("jJStatusSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJStatusMenuItem");
		item.setCommand("#{jJStatusBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJStatusMenuItem");
		item.setCommand("#{jJStatusBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //
		submenu = new DefaultSubMenu("Task");
		submenu.setId("jJTaskSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJTaskMenuItem");
		item.setCommand("#{jJTaskBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJTaskMenuItem");
		item.setCommand("#{jJTaskBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ///
		submenu = new DefaultSubMenu("Testcase");
		submenu.setId("jJTestcaseSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJTestcaseMenuItem");
		item.setCommand("#{jJTestcaseBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJTestcaseMenuItem");
		item.setCommand("#{jJTestcaseBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ////
		submenu = new DefaultSubMenu("TestcaseExecution");
		submenu.setId("jJTestcaseexecutionSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJTestcaseexecutionMenuItem");
		item.setCommand("#{jJTestcaseexecutionBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJTestcaseexecutionMenuItem");
		item.setCommand("#{jJTestcaseexecutionBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //
		submenu = new DefaultSubMenu("Teststep");
		submenu.setId("jJTeststepSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJTeststepMenuItem");
		item.setCommand("#{jJTeststepBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJTeststepMenuItem");
		item.setCommand("#{jJTeststepBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// //
		submenu = new DefaultSubMenu("TeststepExecution");
		submenu.setId("jJTeststepexecutionSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJTeststepexecutionMenuItem");
		item.setCommand("#{jJTeststepexecutionBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJTeststepexecutionMenuItem");
		item.setCommand("#{jJTeststepexecutionBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

		// ////
		submenu = new DefaultSubMenu("Version");
		submenu.setId("jJVersionSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJVersionMenuItem");
		item.setCommand("#{jJVersionBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJVersionMenuItem");
		item.setCommand("#{jJVersionBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);
		menuModel.addElement(submenu);
		// ////
		submenu = new DefaultSubMenu("Workflow");
		submenu.setId("jJWorkflowSubmenu");

		item = new DefaultMenuItem("Create", "ui-icon ui-icon-document");
		item.setId("createJJWorkflowMenuItem");
		item.setCommand("#{jJWorkflowBean.displayCreateDialog}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		item = new DefaultMenuItem("List All", "ui-icon ui-icon-folder-open");
		item.setId("listJJWorkflowMenuItem");
		item.setCommand("#{jJWorkflowBean.displayList}");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");

		submenu.addElement(item);

		menuModel.addElement(submenu);

	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public String getAppName() {
		return "Janjoon";
	}
}
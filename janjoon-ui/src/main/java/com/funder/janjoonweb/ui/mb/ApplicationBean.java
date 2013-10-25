package com.funder.janjoonweb.ui.mb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.submenu.UISubmenu;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Configurable;

//@RooJsfApplicationBean
@ManagedBean
@RequestScoped
@Configurable
public class ApplicationBean {

	public String getColumnName(String column) {
		if (column == null || column.length() == 0) {
			return column;
		}
		final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
		final Matcher m = p.matcher(Character.toUpperCase(column.charAt(0))
				+ column.substring(1));
		final StringBuilder builder = new StringBuilder();
		while (m.find()) {
			builder.append(m.group()).append(" ");
		}
		return builder.toString().trim();
	}

	private MenuModel menuModel;

	@PostConstruct
	public void init() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		menuModel = new DefaultMenuModel();
		menuModel.generateUniqueIds();
		UISubmenu submenu;
		UIMenuItem item;

		submenu = new UISubmenu();
		submenu.setId("jJBugSubmenu");
		submenu.setLabel("JJBug");
		item = new UIMenuItem();
		item.setId("createJJBugMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJBugBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJBugMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJBugBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJBuildSubmenu");
		submenu.setLabel("JJBuild");
		item = new UIMenuItem();
		item.setId("createJJBuildMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJBuildBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJBuildMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJBuildBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJCategorySubmenu");
		submenu.setLabel("JJCategory");
		item = new UIMenuItem();
		item.setId("createJJCategoryMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJCategoryBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJCategoryMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJCategoryBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJChapterSubmenu");
		submenu.setLabel("JJChapter");
		item = new UIMenuItem();
		item.setId("createJJChapterMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJChapterBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJChapterMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJChapterBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJConfigurationSubmenu");
		submenu.setLabel("JJConfiguration");
		item = new UIMenuItem();
		item.setId("createJJConfigurationMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJConfigurationBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJConfigurationMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJConfigurationBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJContactSubmenu");
		submenu.setLabel("JJContact");
		item = new UIMenuItem();
		item.setId("createJJContactMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJContactBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJContactMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJContactBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJCriticitySubmenu");
		submenu.setLabel("JJCriticity");
		item = new UIMenuItem();
		item.setId("createJJCriticityMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJCriticityBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJCriticityMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJCriticityBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJHardwareSubmenu");
		submenu.setLabel("JJHardware");
		item = new UIMenuItem();
		item.setId("createJJHardwareMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJHardwareBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJHardwareMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJHardwareBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJImportanceSubmenu");
		submenu.setLabel("JJImportance");
		item = new UIMenuItem();
		item.setId("createJJImportanceMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJImportanceBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJImportanceMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJImportanceBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJJobSubmenu");
		submenu.setLabel("JJJob");
		item = new UIMenuItem();
		item.setId("createJJJobMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJJobBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJJobMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJJobBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJMessageSubmenu");
		submenu.setLabel("JJMessage");
		item = new UIMenuItem();
		item.setId("createJJMessageMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJMessageBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJMessageMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJMessageBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJPermissionSubmenu");
		submenu.setLabel("JJPermission");
		item = new UIMenuItem();
		item.setId("createJJPermissionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJPermissionBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJPermissionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJPermissionBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJPhaseSubmenu");
		submenu.setLabel("JJPhase");
		item = new UIMenuItem();
		item.setId("createJJPhaseMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJPhaseBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJPhaseMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJPhaseBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJProductSubmenu");
		submenu.setLabel("JJProduct");
		item = new UIMenuItem();
		item.setId("createJJProductMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProductBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJProductMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProductBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJProfileSubmenu");
		submenu.setLabel("JJProfile");
		item = new UIMenuItem();
		item.setId("createJJProfileMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProfileBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJProfileMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProfileBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJProjectSubmenu");
		submenu.setLabel("JJProject");
		item = new UIMenuItem();
		item.setId("createJJProjectMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProjectBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJProjectMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJProjectBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJRequirementSubmenu");
		submenu.setLabel("JJRequirement");
		item = new UIMenuItem();
		item.setId("createJJRequirementMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJRequirementBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJRequirementMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJRequirementBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJRightSubmenu");
		submenu.setLabel("JJRight");
		item = new UIMenuItem();
		item.setId("createJJRightMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJRightBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJRightMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJRightBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJSoftwareSubmenu");
		submenu.setLabel("JJSoftware");
		item = new UIMenuItem();
		item.setId("createJJSoftwareMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJSoftwareBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJSoftwareMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJSoftwareBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJSprintSubmenu");
		submenu.setLabel("JJSprint");
		item = new UIMenuItem();
		item.setId("createJJSprintMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJSprintBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJSprintMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJSprintBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJStatusSubmenu");
		submenu.setLabel("JJStatus");
		item = new UIMenuItem();
		item.setId("createJJStatusMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJStatusBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJStatusMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJStatusBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJTaskSubmenu");
		submenu.setLabel("JJTask");
		item = new UIMenuItem();
		item.setId("createJJTaskMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTaskBean.displayCreateDialog}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJTaskMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTaskBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJTestcaseSubmenu");
		submenu.setLabel("JJTestcase");
		item = new UIMenuItem();
		item.setId("createJJTestcaseMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTestcaseBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJTestcaseMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTestcaseBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJTestcaseexecutionSubmenu");
		submenu.setLabel("JJTestcaseexecution");
		item = new UIMenuItem();
		item.setId("createJJTestcaseexecutionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTestcaseexecutionBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJTestcaseexecutionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTestcaseexecutionBean.displayList}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJTeststepSubmenu");
		submenu.setLabel("JJTeststep");
		item = new UIMenuItem();
		item.setId("createJJTeststepMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTeststepBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJTeststepMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTeststepBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJTeststepexecutionSubmenu");
		submenu.setLabel("JJTeststepexecution");
		item = new UIMenuItem();
		item.setId("createJJTeststepexecutionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTeststepexecutionBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJTeststepexecutionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJTeststepexecutionBean.displayList}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);

		submenu = new UISubmenu();
		submenu.setId("jJVersionSubmenu");
		submenu.setLabel("JJVersion");
		item = new UIMenuItem();
		item.setId("createJJVersionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_create}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJVersionBean.displayCreateDialog}",
				String.class, new Class[0]));
		item.setIcon("ui-icon ui-icon-document");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		item = new UIMenuItem();
		item.setId("listJJVersionMenuItem");
		item.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{messages.label_list}",
						String.class));
		item.setActionExpression(expressionFactory.createMethodExpression(
				elContext, "#{jJVersionBean.displayList}", String.class,
				new Class[0]));
		item.setIcon("ui-icon ui-icon-folder-open");
		item.setAjax(false);
		item.setAsync(false);
		item.setUpdate(":dataForm:data");
		submenu.getChildren().add(item);
		menuModel.addElement(submenu);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public String getAppName() {
		return "Janjoon";
	}
}
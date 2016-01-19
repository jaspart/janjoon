package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.junit.experimental.theories.internal.AllMembersSupplier;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyMessageDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJMessage.class, beanName = "jJMessageBean")
public class JJMessageBean {

	private JJMessage message;
	private JJMessage resolvedJJMessage;
	// private Object field;
	private boolean loadFiltredJJmessage = false;
	private boolean alertOrInfo;
	private SelectItem[] projectOptions;
	private SelectItem[] productOptions;
	private SelectItem[] creatorOptions;
	private SelectItem[] criticityOptions;
	private SelectItem[] statusOptions;
	private List<JJMessage> allJJMessages;
	private List<JJMessage> alertMessages;
	private LazyMessageDataModel mainMessages;
	private List<String> columns;
	private boolean collapsedLayoutPanel = true;
	private HtmlPanelGrid viewPanel;
	private JJMessage viewedMessage;

	public List<JJMessage> getAlertMessages() {

		if (alertMessages == null
				&& ((LoginBean) LoginBean.findBean("loginBean")).isEnable()) {
			alertMessages = jJMessageService.getAlertMessages(
					LoginBean.getProject(), LoginBean.getProduct(),
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
		}
		return alertMessages;
	}

	public void setAlertMessages(List<JJMessage> alertMessages) {
		this.alertMessages = alertMessages;
	}

	@PostConstruct
	public void init() {

		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("message");
		// allJJMessages = jJMessageService.findAllJJMessages();
		setMessage(new JJMessage());
		viewedMessage = new JJMessage();
		mainMessages = null;
		((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);

	}

	public LazyMessageDataModel getMainMessages() {

		return mainMessages;
	}

	public void setMainMessages(LazyMessageDataModel mainMessages) {
		this.mainMessages = mainMessages;
	}

	public List<String> getColumns() {
		return columns;
	}

	public List<JJMessage> getAllJJMessages() {
		if (allJJMessages == null)
			allJJMessages = jJMessageService.findAllJJMessages();
		return allJJMessages;
	}

	public void setAllJJMessages(List<JJMessage> allJJMessages) {
		this.allJJMessages = allJJMessages;
	}

	public JJMessage getMessage() {
		return message;
	}

	public void setMessage(JJMessage message) {
		this.message = message;
	}

	public JJMessage getResolvedJJMessage() {
		return resolvedJJMessage;
	}

	public void setResolvedJJMessage(JJMessage resolvedJJMessage) {
		this.resolvedJJMessage = resolvedJJMessage;
	}

	public HtmlPanelGrid getViewPanel() {
		return populateMessagePanel();
	}

	public void setViewPanel(HtmlPanelGrid viewPanel) {
		this.viewPanel = viewPanel;
	}

	public JJMessage getViewedMessage() {
		if (viewedMessage == null)
			viewedMessage = new JJMessage();
		return viewedMessage;
	}

	public void setViewedMessage(JJMessage viewedMessage) {
		this.viewedMessage = viewedMessage;
	}

	public boolean isLoadFiltredJJmessage() {
		return loadFiltredJJmessage;
	}

	public void setLoadFiltredJJmessage(boolean loadFiltredJJmessage) {
		this.loadFiltredJJmessage = loadFiltredJJmessage;
	}

	public boolean isCollapsedLayoutPanel() {
		return collapsedLayoutPanel;
	}

	public void setCollapsedLayoutPanel(boolean collapsedLayoutPanel) {
		this.collapsedLayoutPanel = collapsedLayoutPanel;
	}

	public boolean isAlertOrInfo() {
		return alertOrInfo;
	}

	public void setAlertOrInfo(boolean alertOrInfo) {
		this.alertOrInfo = alertOrInfo;
	}

	public SelectItem[] getProjectOptions() {

		return projectOptions;

	}

	public SelectItem[] getProductOptions() {
		return productOptions;
	}

	public SelectItem[] getCreatorOptions() {
		return creatorOptions;
	}

	public SelectItem[] getCriticityOptions() {
		return criticityOptions;
	}

	public SelectItem[] getStatusOptions() {
		return statusOptions;
	}

	private SelectItem[] createFilterOptions(Object objet) {
		List<Object> data = (List<Object>) objet;
		SelectItem[] options = new SelectItem[data.size() + 1];
		options[0] = new SelectItem("", "Select");
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) instanceof JJCriticity) {
				JJCriticity criticity = (JJCriticity) data.get(i);
				options[i + 1] = new SelectItem(criticity.getName(),
						criticity.getName());
			} else if (data.get(i) instanceof JJStatus) {
				JJStatus status = (JJStatus) data.get(i);
				options[i + 1] = new SelectItem(status.getName(),
						status.getName());
			} else if (data.get(i) instanceof JJContact) {
				JJContact contact = (JJContact) data.get(i);
				options[i + 1] = new SelectItem(contact.getFirstname() + " "
						+ contact.getName(), contact.getFirstname() + " "
						+ contact.getName());
			} else if (data.get(i) instanceof JJProduct) {
				JJProduct product = (JJProduct) data.get(i);
				options[i + 1] = new SelectItem(product.getName(),
						product.getName());
			} else if (data.get(i) instanceof JJProject) {
				JJProject project = (JJProject) data.get(i);
				options[i + 1] = new SelectItem(project.getName(),
						project.getName());
			}
		}
		return options;
	}

	public void handleLayoutToggle(ToggleEvent event) {
		this.collapsedLayoutPanel = !this.collapsedLayoutPanel;
	}

	public void resolveJJMessage() {
		resolvedJJMessage.setEnabled(false);
		JJStatus status = jJStatusService.getOneStatus("CLOSED", "Message",
				true);
		if (status != null) {
			resolvedJJMessage.setStatus(status);
		}
		updateJJMessage(resolvedJJMessage);
		String message = "message_successfully_disabled";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Message", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void deleteMessage() {
		viewedMessage.setEnabled(false);
		JJStatus status = jJStatusService.getOneStatus("CLOSED", "Message",
				true);
		if (status != null) {
			viewedMessage.setStatus(status);
		}
		updateJJMessage(viewedMessage);
		String message = "message_successfully_disabled";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Message", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();

	}

	public void initJJMessageLayout(ComponentSystemEvent e) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean login = (LoginBean) session.getAttribute("loginBean");

		if (mainMessages == null && login.isEnable()) {

			mainMessages = new LazyMessageDataModel(jJMessageService,
					LoginBean.getProject(), LoginBean.getProduct(),
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
		}
	}

	public void initJJmessageTable(ComponentSystemEvent e) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean login = (LoginBean) session.getAttribute("loginBean");

		if (!loadFiltredJJmessage && login.isEnable()) {
			List<JJCriticity> criticities = new ArrayList<JJCriticity>();
			List<JJStatus> status = new ArrayList<JJStatus>();
			List<JJProduct> productes = new ArrayList<JJProduct>();
			List<JJProject> projectes = new ArrayList<JJProject>();
			List<JJContact> contactes = new ArrayList<JJContact>();
			// enabledJJMessage = jJMessageService.getMessages(true);

			mainMessages = new LazyMessageDataModel(jJMessageService,
					LoginBean.getProject(), LoginBean.getProduct(),
					LoginBean.getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
			// // if (!login.isCollapsedMesPanel()) {
			// RequestContext context = RequestContext.getCurrentInstance();
			// context.update("messagePanel");
			// // }

			for (JJMessage mes : jJMessageService.getActifMessages(
					LoginBean.getProject(), LoginBean.getProduct(),
					LoginBean.getCompany())) {
				if (mes.getCreatedBy() != null
						&& !listContaines(contactes, mes.getCreatedBy()))
					contactes.add(mes.getCreatedBy());
				if (mes.getProduct() != null
						&& !listContaines(productes, mes.getProduct()))
					productes.add(mes.getProduct());
				if (mes.getProject() != null
						&& !listContaines(projectes, mes.getProject()))
					projectes.add(mes.getProject());
				if (mes.getCriticity() != null
						&& !listContaines(criticities, mes.getCriticity()))
					criticities.add(mes.getCriticity());
				if (mes.getStatus() != null
						&& !listContaines(status, mes.getStatus()))
					status.add(mes.getStatus());
			}
			criticityOptions = createFilterOptions(criticities);
			projectOptions = createFilterOptions(projectes);
			productOptions = createFilterOptions(productes);
			creatorOptions = createFilterOptions(contactes);
			statusOptions = createFilterOptions(status);
			loadFiltredJJmessage = true;
		}
	}

	public boolean listContaines(Object objet, Object find) {
		if (find instanceof JJStatus) {
			List<JJStatus> list = (List<JJStatus>) objet;
			int i = 0;
			JJStatus status = (JJStatus) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(status));
				i++;
			}
			return contain;
		} else if (find instanceof JJContact) {
			List<JJContact> list = (List<JJContact>) objet;
			int i = 0;
			JJContact contact = (JJContact) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(contact));
				i++;
			}
			return contain;
		} else if (find instanceof JJCriticity) {
			List<JJCriticity> list = (List<JJCriticity>) objet;
			int i = 0;
			JJCriticity criticity = (JJCriticity) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(criticity));
				i++;
			}
			return contain;
		} else if (find instanceof JJProduct) {
			List<JJProduct> list = (List<JJProduct>) objet;
			int i = 0;
			JJProduct product = (JJProduct) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(product));
				i++;
			}
			return contain;
		} else if (find instanceof JJProject) {
			List<JJProject> list = (List<JJProject>) objet;
			int i = 0;
			JJProject project = (JJProject) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(project));
				i++;
			}
			return contain;
		} else
			return false;

	}

	public void save(JJMessage mes) {
		String message = "";
		if (mes.getId() != null) {
			updateJJMessage(mes);
			message = "message_successfully_updated";
		} else {
			saveJJMessage(mes);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Message", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void createMessage(boolean withField, Object field) {
		if (!alertOrInfo) {

			JJCriticity criticity = jJCriticityService.getCriticityByName(
					"INFO", true);
			if (criticity != null)
				message.setCriticity(criticity);

		} else {
			JJCriticity criticity = jJCriticityService.getCriticityByName(
					"ALERT", true);
			if (criticity != null)
				message.setCriticity(criticity);
		}

		try {
			message.setName(message.getMessage().substring(0, 20));
		} catch (StringIndexOutOfBoundsException c) {
			message.setName(message.getMessage());
		}
		// set attribute
		JJStatus status = jJStatusService.getOneStatus("NEW", "Message", true);

		if (status != null) {
			message.setStatus(status);
		}
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		message.setProduct(LoginBean.getProduct());
		message.setProject(LoginBean.getProject());
		message.setVersioning(LoginBean.getVersion());

		message.setEnabled(true);
		message.setContact(contact);
		message.setDescription(message.getMessage());

		if (withField && field instanceof JJBug)
			message.setBug((JJBug) field);
		else if (withField && field instanceof JJRequirement)
			message.setRequirement((JJRequirement) field);
		else if (withField && field instanceof JJTestcase)
			message.setTestcase((JJTestcase) field);

		save(message);

		message = new JJMessage();
		alertMessages = null;
	}

	public void onRowSelect(SelectEvent event) {
		viewedMessage = (JJMessage) event.getObject();
		// viewPanel = populateMessagePanel();
	}

	public boolean haveObject(JJMessage mes) {
		return mes.getBug() != null || mes.getRequirement() != null
				|| mes.getTestcase() != null;
	}

	public String getObjectName(JJMessage mes) {

		if (mes.getBug() != null)
			return "BUG: " + mes.getBug().getName();
		else if (mes.getRequirement() != null)
			return "SPEC: " + mes.getRequirement().getName();
		else if (mes.getTestcase() != null)
			return "TEST: " + mes.getTestcase().getName();
		else
			return null;
	}

	public void objectInfo(JJMessage mes) throws IOException {

		if (mes.getBug() != null)
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							FacesContext.getCurrentInstance()
									.getExternalContext()
									.getRequestContextPath()
									+ "/pages/bug.jsf?bug="
									+ mes.getBug().getId()
									+ "&faces-redirect=true");

		else if (mes.getRequirement() != null)
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							FacesContext.getCurrentInstance()
									.getExternalContext()
									.getRequestContextPath()
									+ "/pages/requirement.jsf?requirement="
									+ mes.getRequirement().getId()
									+ "&faces-redirect=true");

		else if (mes.getTestcase() != null)
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							FacesContext.getCurrentInstance()
									.getExternalContext()
									.getRequestContextPath()
									+ "/pages/test.jsf?testcase="
									+ mes.getTestcase().getId()
									+ "&faces-redirect=true");

	}

	public void saveJJMessage(JJMessage b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		b.setCompany(contact.getCompany());
		jJMessageService.saveJJMessage(b);
	}

	public void updateJJMessage(JJMessage b) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);

		if (b.getCompany() != null)
			b.setCompany(contact.getCompany());

		b.setUpdatedDate(new Date());
		jJMessageService.updateJJMessage(b);
	}

	public void reset() {
		setAllJJMessages(null);
		RequestContext context = RequestContext.getCurrentInstance();

		context.update(":headerForm:dataTable1");
		context.update("comForm");
		context.update("messageForm");

		// if (FacesContext.getCurrentInstance().getViewRoot().getViewId()
		// .contains("main")) {
		// context.update("contentPanel");
		// }
		setJJMessage_(null);
		setCreateDialogVisible(false);
		loadFiltredJJmessage = false;
		resolvedJJMessage = null;
		viewedMessage = null;
		mainMessages = null;
		alertMessages = null;
		// comMessages = null;
		((LoginBean) LoginBean.findBean("loginBean")).setShowMarquee(null);
		((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
		// initJJmessageTable(null);
	}

	public HtmlPanelGrid populateMessagePanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		if (viewedMessage != null) {
			HtmlOutputText nameLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			nameLabel.setId("nameLabel");
			nameLabel.setValue("Name:");
			htmlPanelGrid.getChildren().add(nameLabel);

			InputTextarea nameValue = (InputTextarea) application
					.createComponent(InputTextarea.COMPONENT_TYPE);
			nameValue.setId("nameValue");
			nameValue.setValueExpression("value",
					expressionFactory
							.createValueExpression(elContext,
									"#{jJMessageBean.viewedMessage.name}",
									String.class));
			nameValue.setReadonly(true);
			nameValue.setDisabled(true);
			htmlPanelGrid.getChildren().add(nameValue);

			HtmlOutputText descriptionLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			descriptionLabel.setId("descriptionLabel");
			descriptionLabel.setValue("Description:");
			htmlPanelGrid.getChildren().add(descriptionLabel);

			InputTextarea descriptionValue = (InputTextarea) application
					.createComponent(InputTextarea.COMPONENT_TYPE);
			descriptionValue.setId("descriptionValue");
			descriptionValue.setValueExpression("value", expressionFactory
					.createValueExpression(elContext,
							"#{jJMessageBean.viewedMessage.description}",
							String.class));
			descriptionValue.setReadonly(true);
			descriptionValue.setDisabled(true);
			htmlPanelGrid.getChildren().add(descriptionValue);

			HtmlOutputText creationDateLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			creationDateLabel.setId("creationDateLabel");
			creationDateLabel.setValue("Creation Date:");
			htmlPanelGrid.getChildren().add(creationDateLabel);

			HtmlOutputText creationDateValue = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			creationDateValue.setValueExpression("value", expressionFactory
					.createValueExpression(elContext,
							"#{jJMessageBean.viewedMessage.creationDate}",
							Date.class));
			DateTimeConverter creationDateValueConverter = (DateTimeConverter) application
					.createConverter(DateTimeConverter.CONVERTER_ID);
			creationDateValueConverter.setPattern("dd/MM/yyyy");
			creationDateValue.setConverter(creationDateValueConverter);
			htmlPanelGrid.getChildren().add(creationDateValue);

			if (viewedMessage.getCreatedBy() != null) {
				HtmlOutputText createdByLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				createdByLabel.setId("createdByLabel");
				createdByLabel.setValue("Created By:");
				htmlPanelGrid.getChildren().add(createdByLabel);

				HtmlOutputText createdByValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				createdByValue
						.setValueExpression(
								"value",
								expressionFactory
										.createValueExpression(
												elContext,
												"#{jJMessageBean.viewedMessage.createdBy.name}",
												String.class));
				htmlPanelGrid.getChildren().add(createdByValue);
			}

			HtmlOutputText updatedDateLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			updatedDateLabel.setId("updatedDateLabel");
			updatedDateLabel.setValue("Updated Date:");
			htmlPanelGrid.getChildren().add(updatedDateLabel);

			HtmlOutputText updatedDateValue = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			updatedDateValue.setValueExpression("value", expressionFactory
					.createValueExpression(elContext,
							"#{jJMessageBean.viewedMessage.updatedDate}",
							Date.class));
			DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application
					.createConverter(DateTimeConverter.CONVERTER_ID);
			updatedDateValueConverter.setPattern("dd/MM/yyyy");
			updatedDateValue.setConverter(updatedDateValueConverter);
			htmlPanelGrid.getChildren().add(updatedDateValue);

			if (viewedMessage.getUpdatedBy() != null) {
				HtmlOutputText updatedByLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				updatedByLabel.setId("updatedByLabel");
				updatedByLabel.setValue("Updated By:");
				htmlPanelGrid.getChildren().add(updatedByLabel);
				HtmlOutputText updatedByValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				updatedByValue
						.setValueExpression(
								"value",
								expressionFactory
										.createValueExpression(
												elContext,
												"#{jJMessageBean.viewedMessage.updatedBy.name}",
												String.class));
				htmlPanelGrid.getChildren().add(updatedByValue);

			}

			HtmlOutputText enabledLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			enabledLabel.setId("enabledLabel");
			enabledLabel.setValue("Enabled:");
			htmlPanelGrid.getChildren().add(enabledLabel);

			HtmlOutputText enabledValue = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			enabledValue.setValueExpression("value", expressionFactory
					.createValueExpression(elContext,
							"#{jJMessageBean.viewedMessage.enabled}",
							String.class));
			htmlPanelGrid.getChildren().add(enabledValue);

			HtmlOutputText messageLabel = (HtmlOutputText) application
					.createComponent(HtmlOutputText.COMPONENT_TYPE);
			messageLabel.setId("messageLabel");
			messageLabel.setValue("Message:");
			htmlPanelGrid.getChildren().add(messageLabel);

			InputTextarea messageValue = (InputTextarea) application
					.createComponent(InputTextarea.COMPONENT_TYPE);
			messageValue.setId("messageValue");
			messageValue.setValueExpression("value", expressionFactory
					.createValueExpression(elContext,
							"#{jJMessageBean.viewedMessage.message}",
							String.class));
			messageValue.setReadonly(true);
			messageValue.setDisabled(true);
			htmlPanelGrid.getChildren().add(messageValue);
			if (viewedMessage.getBug() != null) {
				HtmlOutputText bugLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				bugLabel.setId("bugLabel");
				bugLabel.setValue("Bug:");
				htmlPanelGrid.getChildren().add(bugLabel);

				HtmlOutputText bugValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				bugValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.bug.name}",
								String.class));
				htmlPanelGrid.getChildren().add(bugValue);
			}
			if (viewedMessage.getBuild() != null) {
				HtmlOutputText buildLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				buildLabel.setId("buildLabel");
				buildLabel.setValue("Build:");
				htmlPanelGrid.getChildren().add(buildLabel);

				HtmlOutputText buildValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				buildValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.build.name}",
								String.class));
				htmlPanelGrid.getChildren().add(buildValue);
			}

			if (viewedMessage.getChapter() != null) {
				HtmlOutputText chapterLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				chapterLabel.setId("chapterLabel");
				chapterLabel.setValue("Chapter:");
				htmlPanelGrid.getChildren().add(chapterLabel);

				HtmlOutputText chapterValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				chapterValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.chapter.name}",
								String.class));
				htmlPanelGrid.getChildren().add(chapterValue);
			}
			if (viewedMessage.getContact() != null) {
				HtmlOutputText contactLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				contactLabel.setId("contactLabel");
				contactLabel.setValue("Contact:");
				htmlPanelGrid.getChildren().add(contactLabel);

				HtmlOutputText contactValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				contactValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.contact.name}",
								String.class));
				htmlPanelGrid.getChildren().add(contactValue);
			}
			if (viewedMessage.getCriticity() != null) {
				HtmlOutputText criticityLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				criticityLabel.setId("criticityLabel");
				criticityLabel.setValue("Criticity:");
				htmlPanelGrid.getChildren().add(criticityLabel);

				HtmlOutputText criticityValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				criticityValue
						.setValueExpression(
								"value",
								expressionFactory
										.createValueExpression(
												elContext,
												"#{jJMessageBean.viewedMessage.criticity.name}",
												String.class));
				htmlPanelGrid.getChildren().add(criticityValue);
			}
			if (viewedMessage.getImportance() != null) {
				HtmlOutputText importanceLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				importanceLabel.setId("importanceLabel");
				importanceLabel.setValue("Importance:");
				htmlPanelGrid.getChildren().add(importanceLabel);

				HtmlOutputText importanceValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				importanceValue
						.setValueExpression(
								"value",
								expressionFactory
										.createValueExpression(
												elContext,
												"#{jJMessageBean.viewedMessage.importance.name}",
												String.class));
				htmlPanelGrid.getChildren().add(importanceValue);
			}
			if (viewedMessage.getProduct() != null) {
				HtmlOutputText productLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				productLabel.setId("productLabel");
				productLabel.setValue("Product:");
				htmlPanelGrid.getChildren().add(productLabel);

				HtmlOutputText productValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				productValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.product.name}",
								String.class));
				htmlPanelGrid.getChildren().add(productValue);
				if (viewedMessage.getVersioning() != null) {
					HtmlOutputText versioningLabel = (HtmlOutputText) application
							.createComponent(HtmlOutputText.COMPONENT_TYPE);
					versioningLabel.setId("versioningLabel");
					versioningLabel.setValue("Versioning:");
					htmlPanelGrid.getChildren().add(versioningLabel);

					HtmlOutputText versioningValue = (HtmlOutputText) application
							.createComponent(HtmlOutputText.COMPONENT_TYPE);
					versioningValue
							.setValueExpression(
									"value",
									expressionFactory
											.createValueExpression(
													elContext,
													"#{jJMessageBean.viewedMessage.versioning.name}",
													String.class));
					htmlPanelGrid.getChildren().add(versioningValue);
				}

			}
			if (viewedMessage.getProject() != null) {
				HtmlOutputText projectLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				projectLabel.setId("projectLabel");
				projectLabel.setValue("Project:");
				htmlPanelGrid.getChildren().add(projectLabel);

				HtmlOutputText projectValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				projectValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.project.name}",
								String.class));
				htmlPanelGrid.getChildren().add(projectValue);
			}
			if (viewedMessage.getRequirement() != null) {
				HtmlOutputText requirementLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				requirementLabel.setId("requirementLabel");
				requirementLabel.setValue("Requirement:");
				htmlPanelGrid.getChildren().add(requirementLabel);

				HtmlOutputText requirementValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				requirementValue
						.setValueExpression(
								"value",
								expressionFactory
										.createValueExpression(
												elContext,
												"#{jJMessageBean.viewedMessage.requirement.name}",
												String.class));
				htmlPanelGrid.getChildren().add(requirementValue);
			}
			if (viewedMessage.getSprint() != null) {
				HtmlOutputText sprintLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				sprintLabel.setId("sprintLabel");
				sprintLabel.setValue("Sprint:");
				htmlPanelGrid.getChildren().add(sprintLabel);

				HtmlOutputText sprintValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				sprintValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.sprint.name}",
								String.class));
				htmlPanelGrid.getChildren().add(sprintValue);
			}
			if (viewedMessage.getStatus() != null) {
				HtmlOutputText statusLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				statusLabel.setId("statusLabel");
				statusLabel.setValue("Status:");
				htmlPanelGrid.getChildren().add(statusLabel);

				HtmlOutputText statusValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				statusValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.status.name}",
								String.class));
				htmlPanelGrid.getChildren().add(statusValue);
			}
			if (viewedMessage.getTask() != null) {
				HtmlOutputText taskLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				taskLabel.setId("taskLabel");
				taskLabel.setValue("Task:");
				htmlPanelGrid.getChildren().add(taskLabel);

				HtmlOutputText taskValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				taskValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.task.name}",
								String.class));
				htmlPanelGrid.getChildren().add(taskValue);
			}
			if (viewedMessage.getTestcase() != null) {
				HtmlOutputText testcaseLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				testcaseLabel.setId("testcaseLabel");
				testcaseLabel.setValue("Testcase:");
				htmlPanelGrid.getChildren().add(testcaseLabel);

				HtmlOutputText testcaseValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				testcaseValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.testcase.name}",
								String.class));
				htmlPanelGrid.getChildren().add(testcaseValue);
			}
			if (viewedMessage.getTeststep() != null) {
				HtmlOutputText teststepLabel = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				teststepLabel.setId("teststepLabel");
				teststepLabel.setValue("Teststep:");
				htmlPanelGrid.getChildren().add(teststepLabel);

				HtmlOutputText teststepValue = (HtmlOutputText) application
						.createComponent(HtmlOutputText.COMPONENT_TYPE);
				teststepValue.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.teststep.name}",
								String.class));
				htmlPanelGrid.getChildren().add(teststepValue);
			}
		}
		return htmlPanelGrid;
	}
}

package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyMessageDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@Scope("session")
@Component("jJMessageBean")
public class JJMessageBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private JJMessageService jJMessageService;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJCriticityService jJCriticityService;

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJCriticityService(JJCriticityService jJCriticityService) {
		this.jJCriticityService = jJCriticityService;
	}

	private JJMessage message;
	private JJMessage resolvedJJMessage;
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
		@SuppressWarnings("unchecked")
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
				options[i + 1] = new SelectItem(
						contact.getFirstname() + " " + contact.getName(),
						contact.getFirstname() + " " + contact.getName());
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

	@SuppressWarnings("unchecked")
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

			JJCriticity criticity = jJCriticityService
					.getCriticityByName("INFO", true);
			if (criticity != null)
				message.setCriticity(criticity);

		} else {
			JJCriticity criticity = jJCriticityService
					.getCriticityByName("ALERT", true);
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
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(FacesContext.getCurrentInstance()
							.getExternalContext().getRequestContextPath()
							+ "/pages/bug.jsf?bug=" + mes.getBug().getId()
							+ "&faces-redirect=true");

		else if (mes.getRequirement() != null)
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(FacesContext.getCurrentInstance()
							.getExternalContext().getRequestContextPath()
							+ "/pages/requirement.jsf?requirement="
							+ mes.getRequirement().getId()
							+ "&faces-redirect=true");

		else if (mes.getTestcase() != null)
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(FacesContext.getCurrentInstance()
							.getExternalContext().getRequestContextPath()
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

		// setJJMessage_(null);
		// setCreateDialogVisible(false);
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
}

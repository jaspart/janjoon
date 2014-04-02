package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJMessage.class, beanName = "jJMessageBean")
public class JJMessageBean {

	private JJMessage message;	
	private List<JJMessage> allJJMessages;
	private List<String> columns;
	

	@PostConstruct
	public void init() {
		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("message");

		if (jJMessageService.findAllJJMessages().isEmpty()) {
			System.out
					.println("----------------------------------------------------------");
			for (int j = 0; j < 30; j++) {
				JJMessage mes = new JJMessage();
				mes.setName("mes : " + j);
				mes.setDescription("mesDescription : " + j);
				mes.setCreationDate(new Date());
				mes.setMessage("message tttttt" + j);
				jJMessageService.saveJJMessage(mes);
			}
		}
		allJJMessages = jJMessageService.findAllJJMessages();
		setMessage(new JJMessage());

	}

	public List<String> getColumns() {
		return columns;
	}

	public List<JJMessage> getAllJJMessages() {
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

	public void save(JJMessage mes) {
		String message = "";
		if (mes.getId() != null) {
			jJMessageService.updateJJMessage(mes);
			message = "message_successfully_updated";
		} else {
			jJMessageService.saveJJMessage(mes);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJMessage");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void createMessage(ActionEvent e) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJCriticityBean jJCriticityBean = (JJCriticityBean) session
				.getAttribute("jJCriticityBean");
		if (jJCriticityBean == null) {
			jJCriticityBean = new JJCriticityBean();
			jJCriticityBean.findAllJJCriticitys();
		
		}

		List<JJCriticity> criticity = jJCriticityBean.getAllJJCriticitys();

		if (e.getComponent().getId().contains("submit1")) {
			System.out.println("info");
			int i = 0;
			while (i < criticity.size()) {
				if (criticity.get(i).getName().equalsIgnoreCase("Info")) {
					message.setCriticity(criticity.get(i));
					i = criticity.size();
				}
				i++;
			}

		} else {
			int i = 0;
			while (i < criticity.size()) {
				if (criticity.get(i).getName().equalsIgnoreCase("Alert")) {
					message.setCriticity(criticity.get(i));
					i = criticity.size();
				}
				i++;
			}
			System.out.println("alert");
		}
		message.setCreationDate(new Date());
		message.setDescription(message.getName() + " : "
				+ message.getMessage());
		save(message);
		message = new JJMessage();
	}

	public void onRowSelect(SelectEvent event) {

		setJJMessage_((JJMessage) event.getObject());
	}	

	public void reset() {

		setAllJJMessages(jJMessageService.findAllJJMessages());
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("messagePanel");
		if(FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("main"))
			context.update("contentPanel");
		setJJMessage_(null);
		setCreateDialogVisible(false);
	}
}

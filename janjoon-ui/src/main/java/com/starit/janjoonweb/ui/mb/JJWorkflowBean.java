package com.starit.janjoonweb.ui.mb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyWorkFlowDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.WorkFlowsActions;

@RooSerializable
@RooJsfManagedBean(entity = JJWorkflow.class, beanName = "jJWorkflowBean")
public class JJWorkflowBean {

	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	private LazyWorkFlowDataTable workflowList;
	private JJWorkflow selectedWorkFlow;
	private boolean renderCreate;

	public LazyWorkFlowDataTable getWorkflowList() {

		if (workflowList == null)
			workflowList = new LazyWorkFlowDataTable(jJWorkflowService);
		return workflowList;
	}

	public void setWorkflowList(LazyWorkFlowDataTable workflowList) {
		this.workflowList = workflowList;
	}

	public JJWorkflow getSelectedWorkFlow() {
		return selectedWorkFlow;
	}

	public void setSelectedWorkFlow(JJWorkflow selectedWorkFlow) {
		this.selectedWorkFlow = selectedWorkFlow;
	}

	public boolean isRenderCreate() {
		return renderCreate;
	}

	public void setRenderCreate(boolean renderCreate) {
		this.renderCreate = renderCreate;
	}

	public void deleteWorkflow() {

		selectedWorkFlow.setEnabled(false);
		updateJJWorkflow(selectedWorkFlow);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "WorkFlow");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		workflowList = null;

	}

	public String persistConf() {

		if (getJJWorkflow_().getId() == null) {
			getJJWorkflow_().setEnabled(true);
		}

		return persist();
	}

	public String persist() {

		String message = "";
		if (getJJWorkflow_().getId() != null) {
			updateJJWorkflow(getJJWorkflow_());
			message = "message_successfully_updated";
		} else {
			saveJJWorkflow(getJJWorkflow_());
			message = "message_successfully_created";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createWorkFlowDialogWidget').hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"WorkFlow");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJWorkflows();
	}

	public void reset() {

		setJJWorkflow_(null);
		workflowList = null;
		setCreateDialogVisible(false);
	}

	public void beforeDialogShow(ActionEvent event) {

		setJJWorkflow_(new JJWorkflow());
		renderCreate = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createWorkFlowDialogWidget').show()");

	}

	public List<String> completeObject(String query) {

		String[] names = { "Task", "Requirement", "Bug", "Message", "Build" };
		return Arrays.asList(names);

	}

	public List<String> completeAction(String query) {
		List<String> suggestions = new ArrayList<String>();
		WorkFlowsActions workFlowsActions = new WorkFlowsActions();
		int k = 0;
		Method[] methods = workFlowsActions.getClass().getMethods();

		while (k < methods.length) {
			System.err.println(methods[k].getName());
			if (methods[k].getName().endsWith("WorkFlow"))
				suggestions.add(methods[k].getName());
			k++;
		}

		return suggestions;

	}

	public List<JJStatus> completeSource(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();

		String objet = (String) UIComponent
				.getCurrentComponent(FacesContext.getCurrentInstance())
				.getAttributes().get("objet");
		suggestions.add(jJStatusService.getOneStatus("Any", "*", true));
		if (objet != null)
			for (JJStatus jJStatus : jJStatusService.getStatus(objet, true,
					null, true)) {
				String jJStatusStr = String.valueOf(jJStatus.getName());
				if (jJStatusStr.toLowerCase().startsWith(query.toLowerCase())) {
					suggestions.add(jJStatus);
				}
			}
		return suggestions;
	}

	public List<JJStatus> completeTarget(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();

		String objet = (String) UIComponent
				.getCurrentComponent(FacesContext.getCurrentInstance())
				.getAttributes().get("objet");
		suggestions.add(jJStatusService.getOneStatus("Any", "*", true));
		if (objet != null)
			for (JJStatus jJStatus : jJStatusService.getStatus(objet, true,
					null, true)) {
				String jJStatusStr = String.valueOf(jJStatus.getName());
				if (jJStatusStr.toLowerCase().startsWith(query.toLowerCase())) {
					suggestions.add(jJStatus);
				}
			}
		return suggestions;
	}

	public void saveJJWorkflow(JJWorkflow b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJWorkflowService.saveJJWorkflow(b);
	}

	public void updateJJWorkflow(JJWorkflow b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJWorkflowService.updateJJWorkflow(b);
	}
}

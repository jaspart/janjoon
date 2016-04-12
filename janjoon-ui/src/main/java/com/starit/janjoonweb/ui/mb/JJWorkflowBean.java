package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyWorkFlowDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.WorkFlowsActions;


@RooJsfManagedBean(entity = JJWorkflow.class, beanName = "jJWorkflowBean")
public class JJWorkflowBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	private LazyWorkFlowDataTable workflowList;
	private JJWorkflow selectedWorkFlow;
	private SelectItem[] objectOptions;
	private boolean renderCreate;

	public LazyWorkFlowDataTable getWorkflowList() {

		if (workflowList == null) {
			workflowList = new LazyWorkFlowDataTable(jJWorkflowService);
		}
		return workflowList;
	}

	public void setWorkflowList(LazyWorkFlowDataTable workflowList) {
		this.workflowList = workflowList;
		this.objectOptions = null;
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

	public SelectItem[] getObjectOptions() {

		if (objectOptions == null) {

			Set<String> objects = jJWorkflowService.getAllObject();
			objectOptions = new SelectItem[objects.size() + 1];

			objectOptions[0] = new SelectItem("",
					MessageFactory.getMessage("label_all").getDetail());
			int i = 0;
			for (String comp : objects) {
				objectOptions[i + 1] = new SelectItem(comp, comp);
				i++;

			}

		}
		return objectOptions;

	}

	public void setObjectOptions(SelectItem[] objectOptions) {
		this.objectOptions = objectOptions;
	}

	public void deleteWorkflow() {

		selectedWorkFlow.setEnabled(false);
		updateJJWorkflow(selectedWorkFlow);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted",
				MessageFactory.getMessage("label_workflow", "").getDetail(),
				"");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		workflowList = null;
		this.objectOptions = null;

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
		context.execute("PF('workFlowDialogWidget').hide()");
		RequestContext.getCurrentInstance().update("growlForm");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				MessageFactory.getMessage("label_workflow", "").getDetail(),
				"");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJWorkflows();
	}

	public void reset() {

		setJJWorkflow_(null);
		workflowList = null;
		this.objectOptions = null;
		setCreateDialogVisible(false);
	}

	public void beforeDialogShow(ActionEvent event) {

		setJJWorkflow_(new JJWorkflow());
		renderCreate = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('workFlowDialogWidget').show()");

	}

	public List<String> completeObject(String query) {

		String[] names = {"Task", "Requirement", "Bug", "Message", "Build",
				"TestCase"};
		List<String> suggestions = new ArrayList<String>();

		for (String str : Arrays.asList(names)) {

			if (str.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(str);
			}
		}

		return suggestions;

	}

	public List<String> completeAction(String query) {
		List<String> suggestions = new ArrayList<String>();
		WorkFlowsActions workFlowsActions = new WorkFlowsActions();
		int k = 0;
		Method[] methods = workFlowsActions.getClass().getMethods();

		while (k < methods.length) {
			if (methods[k].getName().endsWith("WorkFlow") && methods[k]
					.getName().toLowerCase().startsWith(query.toLowerCase()))
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

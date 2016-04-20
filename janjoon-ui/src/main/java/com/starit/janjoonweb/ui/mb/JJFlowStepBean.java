package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJFlowStep;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyFlowStepDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooJsfManagedBean(entity = JJFlowStep.class, beanName = "jJFlowStepBean")
public class JJFlowStepBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean renderCreate;
	private LazyFlowStepDataModel lazyFlowStepList;
	private SelectItem[] objectOptions;
	private List<String> tableNames;

	public boolean isRenderCreate() {
		return renderCreate;
	}

	public void setRenderCreate(boolean renderCreate) {
		this.renderCreate = renderCreate;
	}

	public LazyFlowStepDataModel getLazyFlowStepList() {
		if (lazyFlowStepList == null)
			lazyFlowStepList = new LazyFlowStepDataModel(jJFlowStepService);
		return lazyFlowStepList;
	}

	public void setLazyFlowStepList(LazyFlowStepDataModel lazyFlowStepList) {
		this.lazyFlowStepList = lazyFlowStepList;
		this.objectOptions = null;
	}

	public SelectItem[] getObjectOptions() {

		if (objectOptions == null) {

			Set<String> objects = jJFlowStepService.getAllObject();
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
	public void persistFlowStep() {
		String message = "";

		if (getJJFlowStep_().getId() != null) {
			updateJJFlowStep(getJJFlowStep_());
			message = "message_successfully_updated";

		} else {
			getJJFlowStep_()
					.setDescription("Flow Step : " + getJJFlowStep_().getName()
							+ " for " + getJJFlowStep_().getObjet() + "object");
			getJJFlowStep_().setEnabled(true);
			saveJJFlowStep(getJJFlowStep_());
			message = "message_successfully_created";
		}

		if (getJJFlowStep_().getObjet().equalsIgnoreCase("Requirement")) {
			if (LoginBean.findBean("jJRequirementBean") != null) {
				((JJRequirementBean) LoginBean.findBean("jJRequirementBean"))
						.setFlowStepUtils(null);;
			}
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"FlowStep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('flowStepDialogWidget').hide()");

		RequestContext.getCurrentInstance().update("growlForm");

		reset();

	}

	public void deleteFlowStep() {

		getJJFlowStep_().setEnabled(false);
		updateJJFlowStep(getJJFlowStep_());
		if (getJJFlowStep_().getObjet().equalsIgnoreCase("Requirement")) {
			if (LoginBean.findBean("jJRequirementBean") != null) {
				((JJRequirementBean) LoginBean.findBean("jJRequirementBean"))
						.setFlowStepUtils(null);;
			}
		}
		FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_deleted", "FlowStep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		lazyFlowStepList = null;
		objectOptions = null;

	}

	public List<String> completeObject(String query) {

		List<String> suggestions = new ArrayList<String>();
		if (tableNames == null)
			tableNames = jJFlowStepService.getTablesName();

		suggestions.add(null);
		for (String req : tableNames) {

			if (req.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public void reset() {
		setJJFlowStep_(null);
		lazyFlowStepList = null;
		objectOptions = null;
		setCreateDialogVisible(false);
	}

	public boolean levelValid(Integer level, JJFlowStep c) {
		boolean valid = true;

		System.err.println("object: " + c.getObjet());
		JJFlowStep flow = jJFlowStepService.getFlowStepByLevel(level,
				c.getObjet(), true);

		if (flow != null) {
			if (c.getId() == null)
				valid = false;
			else {
				if (!flow.equals(c)) {
					valid = false;
				}

			}
		}
		return valid;
	}

	public void saveJJFlowStep(JJFlowStep b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJFlowStepService.saveJJFlowStep(b);
	}

	public void updateJJFlowStep(JJFlowStep b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJFlowStepService.updateJJFlowStep(b);
	}

}

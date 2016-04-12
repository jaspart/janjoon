package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConfDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooJsfManagedBean(entity = JJConfiguration.class, beanName = "jJConfigurationBean")
public class JJConfigurationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyConfDataTable configList;
	private JJConfiguration selectedConf;
	private List<String> columns;
	private JJConfiguration jJconfiguration;
	private boolean renderCreate;

	@PostConstruct
	public void init() {

		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("param");
		columns.add("val");
	}

	public JJConfigurationService getJjConfigurationService() {
		return jJConfigurationService;
	}

	public JJConfiguration getjJconfiguration() {
		return jJconfiguration;
	}

	public void setjJconfiguration(JJConfiguration jJconfiguration) {
		this.jJconfiguration = jJconfiguration;
	}

	public LazyConfDataTable getConfigList() {
		if (configList == null)
			configList = new LazyConfDataTable(jJConfigurationService);
		return configList;
	}

	public void setConfigList(LazyConfDataTable configList) {
		this.configList = configList;
	}

	public JJConfiguration getSelectedConf() {
		return selectedConf;
	}

	public void setSelectedConf(JJConfiguration selectedConf) {
		this.selectedConf = selectedConf;
	}

	public List<String> getColumns() {
		return columns;
	}

	public boolean isRenderCreate() {
		return renderCreate;
	}

	public void setRenderCreate(boolean renderCreate) {
		System.out.println("jJConfigurationBean renderCreate" + renderCreate);
		this.renderCreate = renderCreate;
	}

	public void deleteConfig() {

		selectedConf.setEnabled(false);
		updateJJConfiguration(selectedConf);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "Configuration", "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		configList = null;

	}

	public String persistConf() {

		if (getJJConfiguration_().getId() == null) {
			getJJConfiguration_().setEnabled(true);
		}

		return persist();
	}

	public String persist() {

		String message = "";
		if (getJJConfiguration_().getId() != null) {
			updateJJConfiguration(getJJConfiguration_());
			message = "message_successfully_updated";
		} else {
			saveJJConfiguration(getJJConfiguration_());
			message = "message_successfully_created";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('configDialogWidget').hide()");
		RequestContext.getCurrentInstance().update("growlForm");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Configuration", "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJConfigurations();
	}

	public void reset() {

		if (getJJConfiguration_().getName() != null && getJJConfiguration_()
				.getName().equalsIgnoreCase("MarqueeAlertMessage")) {
			((LoginBean) LoginBean.findBean("loginBean")).setShowMarquee(null);
		}

		setJJConfiguration_(null);
		configList = null;
		setCreateDialogVisible(false);
		// jJconfiguration = jJConfigurationService.getConfigurations(
		// "ConfigurationManager", "git", true).get(0);
	}

	public void beforeDialogShow(ActionEvent event) {

		setJJConfiguration_(new JJConfiguration());
		renderCreate = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('configDialogWidget').show()");

	}

	public void saveJJConfiguration(JJConfiguration b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJConfigurationService.saveJJConfiguration(b);
	}

	public void updateJJConfiguration(JJConfiguration b) {
		if (((LoginBean) LoginBean.findBean("loginBean"))
				.getPlanningConfiguration() != null
				&& b.equals(((LoginBean) LoginBean.findBean("loginBean"))
						.getPlanningConfiguration().getPlaningTabsConf()))
			((LoginBean) LoginBean.findBean("loginBean"))
					.setPlanningConfiguration(null);
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJConfigurationService.updateJJConfiguration(b);
	}

}

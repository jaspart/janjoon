package com.starit.janjoonweb.ui.mb;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJConfiguration.class, beanName = "jJConfigurationBean")
public class JJConfigurationBean {
	
	private List<JJConfiguration> configList;
	private JJConfiguration selectedConf;

	public List<JJConfiguration> getConfigList() {
		configList=jJConfigurationService.getConfigs(true);
	    System.out.println("gets");
		return configList;
	}

	public void setConfigList(List<JJConfiguration> configList) {
		this.configList = configList;
	}
	public JJConfiguration getSelectedConf() {
		return selectedConf;
	}
	public void setSelectedConf(JJConfiguration selectedConf) {
		this.selectedConf = selectedConf;
	}
	
	public void deleteConfig()
	{
		selectedConf.setEnabled(false);
		jJConfigurationService.updateJJConfiguration(selectedConf);
		FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJConfiguration");
	    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	    System.out.println("deleted--------------------------------------------------------");
	    configList=jJConfigurationService.getConfigs(true);
	}
}

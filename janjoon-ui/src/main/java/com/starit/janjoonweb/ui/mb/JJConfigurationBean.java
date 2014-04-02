package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private List<String> columns;
	private JJConfiguration jJconfiguration;

	@PostConstruct
	public void init() {

		System.out.println("------initConfiguration------");
		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("param");
		columns.add("val");

		if (jJConfigurationService.findAllJJConfigurations().isEmpty()) {
			JJConfiguration configuration = new JJConfiguration();
			configuration.setName("ConfigurationManager");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test Configuration Manager");
			configuration.setParam("git");
			configuration.setVal("https://github.com/janjoon/");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);
		}

		jJconfiguration = jJConfigurationService.findAllJJConfigurations().get(0);

	}

	public JJConfiguration getjJconfiguration() {
		return jJconfiguration;
	}

	public void setjJconfiguration(JJConfiguration jJconfiguration) {
		this.jJconfiguration = jJconfiguration;
	}

	public List<JJConfiguration> getConfigList() {
		configList = jJConfigurationService.getConfigs(true);
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

	public List<String> getColumns() {
		return columns;
	}

	public void deleteConfig() {
		selectedConf.setEnabled(false);
		jJConfigurationService.updateJJConfiguration(selectedConf);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "JJConfiguration");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		System.out
				.println("deleted--------------------------------------------------------");
		configList = jJConfigurationService.getConfigs(true);
		jJconfiguration=new JJConfiguration();
	}
	
	public void reset() {
        setJJConfiguration_(null);
        setCreateDialogVisible(false);
        jJconfiguration=new JJConfiguration();
    }
}

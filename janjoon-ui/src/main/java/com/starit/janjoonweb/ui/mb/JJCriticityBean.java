package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import javax.faces.component.html.HtmlPanelGrid;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;

@RooJsfManagedBean(entity = JJCriticity.class, beanName = "jJCriticityBean")
public class JJCriticityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JJCriticity getCriticityByName(String name) {
		return jJCriticityService.getCriticityByName(name, true);
	}

	public void saveJJCriticity(JJCriticity b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJCriticityService.saveJJCriticity(b);
	}

	public void updateJJCriticity(JJCriticity b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCriticityService.updateJJCriticity(b);
	}

	public HtmlPanelGrid populateCreatePanel() {
		return null;
	}

	public HtmlPanelGrid populateEditPanel() {
		return null;
	}

	public HtmlPanelGrid populateViewPanel() {
		return null;
	}
}

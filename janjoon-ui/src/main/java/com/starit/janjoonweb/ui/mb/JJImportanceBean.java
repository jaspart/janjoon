package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.component.html.HtmlPanelGrid;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJImportance;

@RooSerializable
@RooJsfManagedBean(entity = JJImportance.class, beanName = "jJImportanceBean")
public class JJImportanceBean {

	public void saveJJImportance(JJImportance b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJImportanceService.saveJJImportance(b);
	}

	public void updateJJImportance(JJImportance b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJImportanceService.updateJJImportance(b);
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

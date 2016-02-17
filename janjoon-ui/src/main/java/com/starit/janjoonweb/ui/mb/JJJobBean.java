package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJJob;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJJob.class, beanName = "jJJobBean")
public class JJJobBean {

	public void saveJJJob(JJJob b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);
		jJJobService.saveJJJob(b);
	}

	public void updateJJJob(JJJob b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJJobService.updateJJJob(b);
	}
}

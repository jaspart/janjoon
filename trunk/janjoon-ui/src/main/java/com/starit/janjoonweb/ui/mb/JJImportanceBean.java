package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJImportance;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJImportance.class, beanName = "jJImportanceBean")
public class JJImportanceBean {
	
	public void saveJJImportance(JJImportance b)
	{
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJImportanceService.saveJJImportance(b);
	}
	
	public void updateJJImportance(JJImportance b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJImportanceService.updateJJImportance(b);
	}
}

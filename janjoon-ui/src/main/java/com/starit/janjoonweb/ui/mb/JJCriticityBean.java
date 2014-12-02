package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJCriticity.class, beanName = "jJCriticityBean")
public class JJCriticityBean {

	public JJCriticity getCriticityByName(String name) {
		return jJCriticityService.getCriticityByName(name, true);
	}
	
	public void saveJJCriticity(JJCriticity b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJCriticityService.saveJJCriticity(b);
	}
	
	public void updateJJCriticity(JJCriticity b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCriticityService.updateJJCriticity(b);
	}
}

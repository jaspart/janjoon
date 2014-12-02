package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJSoftware;
import com.starit.janjoonweb.domain.JJSoftware;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJSoftware.class, beanName = "jJSoftwareBean")
public class JJSoftwareBean {
	
	public void saveJJSoftware(JJSoftware b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJSoftwareService.saveJJSoftware(b);
	}
	
	public void updateJJSoftware(JJSoftware b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJSoftwareService.updateJJSoftware(b);
	}
}

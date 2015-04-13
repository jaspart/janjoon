package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJJob;
import com.starit.janjoonweb.domain.JJPhase;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJPhase.class, beanName = "jJPhaseBean")
public class JJPhaseBean {
	
	public void saveJJPhase(JJPhase b)
	{
		b.setCreationDate(new Date());
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);		
		jJPhaseService.saveJJPhase(b);
	}
	
	public void updateJJPhase(JJPhase b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJPhaseService.updateJJPhase(b);
	}
}

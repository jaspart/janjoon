package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.domain.JJWorkflow;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJWorkflow.class, beanName = "jJWorkflowBean")
public class JJWorkflowBean {
	
	public void saveJJWorkflow(JJWorkflow b)
	{
		b.setCreationDate(new Date());
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);		
		jJWorkflowService.saveJJWorkflow(b);
	}
	
	public void updateJJWorkflow(JJWorkflow b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJWorkflowService.updateJJWorkflow(b);
	}
}

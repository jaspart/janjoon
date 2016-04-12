package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPhase;

@RooJsfManagedBean(entity = JJPhase.class, beanName = "jJPhaseBean")
public class JJPhaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJPhase(JJPhase b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJPhaseService.saveJJPhase(b);
	}

	public void updateJJPhase(JJPhase b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJPhaseService.updateJJPhase(b);
	}
}

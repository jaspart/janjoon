package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJJob;

@RooJsfManagedBean(entity = JJJob.class, beanName = "jJJobBean")
public class JJJobBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJJob(JJJob b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJJobService.saveJJJob(b);
	}

	public void updateJJJob(JJJob b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJJobService.updateJJJob(b);
	}
}

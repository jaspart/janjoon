package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJSoftware;

@RooJsfManagedBean(entity = JJSoftware.class, beanName = "jJSoftwareBean")
public class JJSoftwareBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJSoftware(final JJSoftware b) {
		b.setCreationDate(new Date());
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJSoftwareService.saveJJSoftware(b);
	}

	public void updateJJSoftware(final JJSoftware b) {
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJSoftwareService.updateJJSoftware(b);
	}
}

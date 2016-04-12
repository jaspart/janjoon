package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJHardware;

@RooJsfManagedBean(entity = JJHardware.class, beanName = "jJHardwareBean")
public class JJHardwareBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJHardware(final JJHardware b) {
		b.setCreationDate(new Date());
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJHardwareService.saveJJHardware(b);
	}

	public void updateJJHardware(final JJHardware b) {
		final JJContact contact = (JJContact) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
						.getAttribute("JJContact");
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJHardwareService.updateJJHardware(b);
	}
}

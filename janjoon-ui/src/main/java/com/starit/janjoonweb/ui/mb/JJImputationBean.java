package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJImputation;
import com.starit.janjoonweb.domain.JJImputation;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJImputation.class, beanName = "jJImputationBean")
public class JJImputationBean {

	public void saveJJImputation(JJImputation b) {
		jJImputationService.saveJJImputation(b);
	}

	public void updateJJImputation(JJImputation b) {
		jJImputationService.updateJJImputation(b);
	}
}

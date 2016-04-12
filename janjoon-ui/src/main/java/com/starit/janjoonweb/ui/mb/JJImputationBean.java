package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJImputation;

@RooJsfManagedBean(entity = JJImputation.class, beanName = "jJImputationBean")
public class JJImputationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJImputation(JJImputation b) {
		jJImputationService.saveJJImputation(b);
	}

	public void updateJJImputation(JJImputation b) {
		jJImputationService.updateJJImputation(b);
	}
}

package com.starit.janjoonweb.ui.mb;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJImputation;

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

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJCriticity;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJCriticity.class, beanName = "jJCriticityBean")
public class JJCriticityBean {

	public JJCriticity getCriticityByName(String name) {
		return jJCriticityService.getCriticityByName(name, true);
	}
}

package com.funder.janjoonweb.ui.mb;

import java.util.Date;

import com.funder.janjoonweb.domain.JJStatus;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJStatus.class, beanName = "jJStatusBean")
public class JJStatusBean {

	private JJStatus createANDpersistJJStatus(String name) {
		JJStatus newJJStatus = new JJStatus();
		newJJStatus.setName(name);
		newJJStatus.setCreationDate(new Date());
		newJJStatus.setDescription("A JJStatus defined as " + name);
		newJJStatus.setEnabled(true);
		jJStatusService.saveJJStatus(newJJStatus);
		return newJJStatus;
	}
}

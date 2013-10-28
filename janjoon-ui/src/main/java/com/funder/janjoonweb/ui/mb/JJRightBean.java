package com.funder.janjoonweb.ui.mb;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRight;

@RooSerializable
@RooJsfManagedBean(entity = JJRight.class, beanName = "jJRightBean")
public class JJRightBean {

	public JJRight createANDpersistJJRight(JJProject project,
			JJProduct product, JJPermission permission, JJCategory category,
			boolean read, boolean write, boolean execute) {

		JJRight newJJRight = new JJRight();

		newJJRight.setCategory(category);
		newJJRight.setR(read);
		newJJRight.setW(write);
		newJJRight.setX(execute);
		jJRightService.saveJJRight(newJJRight);
		return newJJRight;
	}
}
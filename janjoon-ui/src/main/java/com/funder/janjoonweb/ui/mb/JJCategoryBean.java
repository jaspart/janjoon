package com.funder.janjoonweb.ui.mb;

import java.util.Date;

import com.funder.janjoonweb.domain.JJCategory;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJCategory.class, beanName = "jJCategoryBean")
public class JJCategoryBean {

	public JJCategory createANDpersistJJCategory(String name) {
		JJCategory newJJCategory = new JJCategory();
		newJJCategory.setName(name);
		newJJCategory.setCreationDate(new Date());
		newJJCategory.setDescription("A JJCategory defined as " + name);
		newJJCategory.setEnabled(true);
		jJCategoryService.saveJJCategory(newJJCategory);
		return newJJCategory;
	}
}

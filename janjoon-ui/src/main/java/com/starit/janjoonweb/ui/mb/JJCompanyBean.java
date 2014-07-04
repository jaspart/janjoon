package com.starit.janjoonweb.ui.mb;

import java.util.List;

import com.starit.janjoonweb.domain.JJCompany;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJCompany.class, beanName = "jJCompanyBean")
public class JJCompanyBean {
	
	private List<JJCompany> companies;

	public List<JJCompany> getCompanies() {
		return companies;
	}

	public void setCompanies(List<JJCompany> companies) {
		this.companies = companies;
	}
	
}

package com.starit.janjoonweb.ui.mb.util;

import java.util.HashSet;
import java.util.Set;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;

public class Contact {
	 
	private String name;	
	private String email;
	private String firstname;		
	private JJProject lastProject;	
	private JJProduct lastProduct;	
	private JJVersion lastVersion;
	private Set<JJCategory> categories = new HashSet<JJCategory>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public JJProject getLastProject() {
		return lastProject;
	}
	public void setLastProject(JJProject lastProject) {
		this.lastProject = lastProject;
	}
	public JJProduct getLastProduct() {
		return lastProduct;
	}
	public void setLastProduct(JJProduct lastProduct) {
		this.lastProduct = lastProduct;
	}
	public JJVersion getLastVersion() {
		return lastVersion;
	}
	public void setLastVersion(JJVersion lastVersion) {
		this.lastVersion = lastVersion;
	}
	public Set<JJCategory> getCategories() {
		return categories;
	}
	public void setCategories(Set<JJCategory> categories) {
		this.categories = categories;
	}
	
	public Contact(JJContact contact) {
		
		this.firstname=contact.getFirstname();
		this.name=contact.getName();
		this.email=contact.getEmail();
		this.lastProduct=contact.getLastProduct();
		this.lastVersion=contact.getLastVersion();
		this.lastProject=contact.getLastProject();
		this.categories = contact.getCategories();
	}
	
	public JJContact getJJContact(JJContact contact)
	{
		contact.setName(this.name);
		contact.setLastProduct(this.lastProduct);
		contact.setLastVersion(this.lastVersion);
		contact.setLastProject(lastProject);
		contact.setCategories(categories);
		contact.setFirstname(firstname);
		return contact;
	}
	@Override
	public boolean equals(Object object) {
		return (object instanceof Contact) && (getEmail() != null) ? getEmail()
				.equals(((Contact) object).getEmail()) : (object == this);
	}
}

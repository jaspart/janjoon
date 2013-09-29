package com.funder.janjoonweb.ui.mb;

import java.util.Date;

import com.funder.janjoonweb.domain.JJContact;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	public JJContact createANDpersistJJContact(String firstname, String lastname, String email, String password) {
		JJContact newJJContact = new JJContact();
		newJJContact.setPassword(password);
		newJJContact.setEmail(email);
		newJJContact.setLdap(email.indexOf("@"));
		newJJContact.setFirstname(firstname);
		newJJContact.setLastname(lastname);
		newJJContact.setDateofbirth(new Date());
		newJJContact.setEnabled(true);
		newJJContact.setAccountNonExpired(true);
		newJJContact.setCredentialsNonExpired(true);
		newJJContact.setAccountNonLocked(true);
		jJContactService.saveJJContact(newJJContact);
		//newJJContact.setName(email.substring(0, email.indexOf("@")););

		return newJJContact;
	}
}

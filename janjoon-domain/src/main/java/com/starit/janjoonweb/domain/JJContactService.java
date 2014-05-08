package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJContact.class })
public interface JJContactService {
	
	public List<JJContact> getContacts(String email, boolean onlyActif);
	
	public boolean saveJJContactTransaction(JJContact contact);
	
	public boolean updateJJContactTransaction(JJContact contact);
	
	public List<JJRight>  getContactAuthorization(JJContact contact,JJProduct product,JJProject project,JJCategory category);
	
	public List<JJRight>  getContactAuthorization(JJContact contact,JJProduct product);
	
	public List<JJRight>  getContactAuthorization(JJContact contact,JJProduct product,JJProject project);
	
	public List<JJRight>  getContactAuthorization(JJContact contact,JJProject project);
	
	public List<JJPermission> getContactPermission(JJContact contact);
	

	
}

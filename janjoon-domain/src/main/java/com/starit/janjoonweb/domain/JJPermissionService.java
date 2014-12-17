package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJPermission.class })
public interface JJPermissionService {

	public List<JJPermission> getPermissions(JJContact contact,
			boolean onlyContact, JJProfile profile, JJProject project,
			JJProduct product);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet, JJCategory category, Boolean r,
			Boolean w, Boolean x);

	public boolean isAuthorized(JJContact contact, String objet,
			JJCategory category, Boolean r, Boolean w, Boolean x);

	public boolean isAuthorized(JJContact contact, String objet, Boolean r,
			Boolean w, Boolean x);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet, JJCategory category);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product);
	
	public boolean isSuperAdmin(JJContact contact);

	public boolean isAuthorized(JJContact contact, JJProject project);
	
	public JJProject getDefaultProject(JJContact contact);
	
	public JJProduct getDefaultProduct(JJContact contact);
	
	public List<JJCategory> getDefaultCategories(JJContact contact);
	
	public JJCategory getDefaultCategory(JJContact contact);

	public List<JJContact> areAuthorized(JJCompany company,JJContact contact,JJProject project, JJProduct product,
			String objet, JJCategory category, Boolean r, Boolean w, Boolean x);
	
	public List<JJContact> areSprintAuthorized(JJCompany company,JJProject project);

	public List<JJContact> getManagers(JJCompany company,JJContact contact,String objet);

	public List<JJContact> areAuthorized(JJCompany company,JJContact contact,JJProject project, JJProduct product,String objet);

}

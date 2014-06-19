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
			JJProduct product, String objet, JJCategory category, String operation);

	public boolean isAuthorized(JJContact contact, String objet,
			JJCategory category, String operation);

	public boolean isAuthorized(JJContact contact, String objet, String operation);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet, JJCategory category);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet);

	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product);

	public boolean isAuthorized(JJContact contact, JJProject project);

	public Set<JJContact> areAuthorized(JJProject project, JJProduct product,
			String objet, JJCategory category, Boolean r, Boolean w, Boolean x);

	public Set<JJContact> getManagers(String objet);

	public Set<JJContact> areAuthorized(JJProject project, JJProduct product);

	List<JJPermission> getContactPermission(JJContact contact);

}

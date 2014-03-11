package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJPermission.class })
public interface JJPermissionService {

	public List<JJPermission> getManagerPermissions(JJProfile profile);

	public List<JJPermission> getPermissions(JJContact contact,
			boolean onlyContact, JJProfile profile, JJProject project,
			JJProduct product);
}

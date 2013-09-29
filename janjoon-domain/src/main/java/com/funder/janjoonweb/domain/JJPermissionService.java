package com.funder.janjoonweb.domain;

import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJPermission.class })
public interface JJPermissionService {
	public JJPermission getJJPermissionWithName(String name);
	public List<JJPermission> getAllJJPermission();
}

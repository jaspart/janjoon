package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProfile.class })
public interface JJProfileService {

	public JJProfile getOneProfile(String name, boolean onlyActif);

	public List<JJProfile> getProfiles(boolean onlyActif,boolean isSuperAdmin);

	public List<JJProfile> load(MutableInt size,int first, int pageSize,boolean isSuperAdmin);
}

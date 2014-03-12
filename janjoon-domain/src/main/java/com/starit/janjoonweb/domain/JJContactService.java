package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJContact.class })
public interface JJContactService {
	public List<JJContact> getContacts(String email, boolean onlyActif);
}

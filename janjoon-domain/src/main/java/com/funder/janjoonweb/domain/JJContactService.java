package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJContact.class })
public interface JJContactService {
	public JJContact getJJContactWithEmail(String email);
	public List<JJContact> getAllJJContact();
}

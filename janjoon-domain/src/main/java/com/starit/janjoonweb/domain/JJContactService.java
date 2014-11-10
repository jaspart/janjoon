package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJContact.class })
public interface JJContactService {

	public JJContact getContactByEmail(String email, boolean onlyActif);

	public List<JJContact> getContacts(boolean onlyActif);

	public boolean saveJJContactTransaction(JJContact contact);

	public boolean updateJJContactTransaction(JJContact contact);

	public List<JJContact> load(int first, int pageSize);

}

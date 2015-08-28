package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJContact.class })
public interface JJContactService {

	public JJContact getContactByEmail(String email, boolean onlyActif);

	public JJContact getContactById(Long id);

	public List<JJContact> getContacts(boolean onlyActif, JJCompany company,
			JJContact contact);

	public boolean saveJJContactTransaction(JJContact contact);

	public boolean updateJJContactTransaction(JJContact contact);

	public List<JJContact> load(JJCompany company, MutableInt size, int first,
			int pageSize,List<SortMeta> multiSortMeta,
			Map<String, Object> filters);

}

package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJMessage.class })
public interface JJMessageService {

	public List<JJMessage> getMessages(boolean onlyActif);

	public List<JJMessage> getActifMessages(JJProject project, JJProduct product);
	
	public List<JJMessage> getActifMessages(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters,JJProject project,JJProduct product);
}

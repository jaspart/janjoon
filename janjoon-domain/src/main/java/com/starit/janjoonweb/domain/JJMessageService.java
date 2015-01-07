package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJMessage.class })
public interface JJMessageService {

	public List<JJMessage> getMessages(boolean onlyActif);

	public List<JJMessage> getActifMessages(JJProject project, JJProduct product);
	
	public List<JJMessage> getAlertMessages(JJProject project, JJProduct product);
	
	public List<JJMessage> getActifMessages(MutableInt size,int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters,JJProject project,JJProduct product);
}

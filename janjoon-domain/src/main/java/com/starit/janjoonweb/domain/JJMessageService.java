package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJMessage.class })
public interface JJMessageService {

	public List<JJMessage> getMessages(boolean onlyActif);
}

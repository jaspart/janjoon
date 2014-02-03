package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJRight.class })
public interface JJRightService {

	public List<JJRight> getObjectWriterList(String objet);

	public List<JJRight> getObjectWriterList(String objet, boolean w);

	public List<JJRight> getRightsWithoutProfile();

}

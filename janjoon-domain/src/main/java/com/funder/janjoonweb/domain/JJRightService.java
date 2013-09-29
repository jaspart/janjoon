package com.funder.janjoonweb.domain;

import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJRight.class })
public interface JJRightService {
	public JJRight getJJRightWithName(String name);
	public List<JJRight> getAllJJRight();
}

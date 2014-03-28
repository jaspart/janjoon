package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTeststep.class })
public interface JJTeststepService {

	public List<JJTeststep> getTeststeps(JJTestcase testcase,
			boolean onlyActif, boolean sortedByOrder);

	public void saveTeststeps(Set<JJTeststep> teststeps);

	public void updateTeststeps(Set<JJTeststep> teststeps);
}

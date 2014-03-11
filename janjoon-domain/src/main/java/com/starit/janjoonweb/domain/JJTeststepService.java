package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTeststep.class })
public interface JJTeststepService {
	public List<JJTeststep> getAllJJTeststeps();
	public List<JJTeststep> getJJTeststepWithTestcase(JJTestcase jJTestcase);
}

package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTeststepexecution.class })
public interface JJTeststepexecutionService {

	public List<JJTeststepexecution> getAllTeststepexecutions();

	public JJTeststepexecution getTeststepexecutionWithTeststepAndBuild(
			JJTeststep teststep, JJBuild build);
}

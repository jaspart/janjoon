package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJTestcase.class })
public interface JJTestcaseService {
	public List<JJTestcase> getAllJJTestcases();

	public List<JJTestcase> getAllJJTestcasesWithChapter(JJChapter chapter);
}

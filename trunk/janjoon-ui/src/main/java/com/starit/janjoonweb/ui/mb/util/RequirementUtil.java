package com.starit.janjoonweb.ui.mb.util;

import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;

public class RequirementUtil {

	private JJRequirement requirement;
	private String style;

	public RequirementUtil() {

	}

	public RequirementUtil(JJRequirement requirement, String style) {

		this.requirement = requirement;
		this.style = style;
	}

	public RequirementUtil(JJRequirement requirement,
			JJCategoryService jJCategoryService,
			JJRequirementService jJRequirementService,
			JJTaskService jJTaskService, JJTestcaseService jJTestcaseService,
			JJTestcaseexecutionService jJTestcaseexecutionService) {

		this.requirement = requirement;
		this.style = JJRequirementBean.getRowStyleClass(requirement,
				jJCategoryService, jJRequirementService, jJTaskService,
				jJTestcaseService, jJTestcaseexecutionService);
	}

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof RequirementUtil)
				&& (requirement.getId() != null) ? requirement.getId().equals(
				((RequirementUtil) object).getRequirement().getId())
				: (object == this);
	}

}

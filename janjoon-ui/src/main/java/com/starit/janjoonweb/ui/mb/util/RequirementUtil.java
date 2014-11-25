package com.starit.janjoonweb.ui.mb.util;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJRequirement;

public class RequirementUtil {

	private JJRequirement requirement;
	private String style;
	
	
	public RequirementUtil() {
		
	}
	public RequirementUtil(JJRequirement requirement, String style) {
		
		this.requirement = requirement;
		this.style = style;
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
		return (object instanceof RequirementUtil) && (requirement.getId() != null) ? requirement.getId()
				.equals(((RequirementUtil) object).getRequirement().getId()) : (object == this);
	}

}

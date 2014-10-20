package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJRequirement;
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
				.equals(((JJBug) object).getId()) : (object == this);
	}

}

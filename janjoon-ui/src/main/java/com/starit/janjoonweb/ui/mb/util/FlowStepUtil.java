package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.List;

import com.starit.janjoonweb.domain.JJFlowStep;
import com.starit.janjoonweb.domain.JJFlowStepService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class FlowStepUtil {

	private JJFlowStep flowStep;
	private List<JJRequirement> requirements;

	public FlowStepUtil(JJFlowStep flowStep, List<JJRequirement> requirements) {
		super();
		this.flowStep = flowStep;
		this.requirements = requirements;
	}

	public JJFlowStep getFlowStep() {
		return flowStep;
	}
	public void setFlowStep(JJFlowStep flowStep) {
		this.flowStep = flowStep;
	}
	public List<JJRequirement> getRequirements() {
		return requirements;
	}
	public void setRequirements(List<JJRequirement> requirements) {
		this.requirements = requirements;
	}

	public static List<FlowStepUtil> getFlowStepUtils(
			JJRequirementService jjRequirementService,
			JJFlowStepService jjFlowStepService) {

		List<FlowStepUtil> list = new ArrayList<FlowStepUtil>();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");

		for (JJFlowStep flow : jjFlowStepService.getFlowStep("Requirement",
				true, null, true)) {
			List<JJRequirement> l = jjRequirementService
					.getRequirementsByFlowStep(LoginBean.getCompany(),
							loginBean.getAuthorizedMap("Requirement",
									LoginBean.getProject(),
									LoginBean.getProduct()),
							LoginBean.getVersion(), flow, true, true);

			list.add(new FlowStepUtil(flow, l));
		}

		return list;

	}

}

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
	private JJFlowStep nextFlowStep;
	private List<JJRequirement> requirements;

	public FlowStepUtil(JJFlowStep flowStep, JJFlowStep nextFlowStep,
			List<JJRequirement> requirements) {
		super();
		this.flowStep = flowStep;
		this.nextFlowStep = nextFlowStep;
		this.requirements = requirements;
	}
	public JJFlowStep getNextFlowStep() {
		return nextFlowStep;
	}
	public void setNextFlowStep(JJFlowStep nextFlowStep) {
		this.nextFlowStep = nextFlowStep;
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
		List<JJFlowStep> flows = jjFlowStepService.getFlowStep("Requirement",
				true, null, true);
		int i = 1;

		for (JJFlowStep flow : flows) {
			List<JJRequirement> l = jjRequirementService
					.getRequirementsByFlowStep(LoginBean.getCompany(),
							loginBean.getAuthorizedMap("Requirement",
									LoginBean.getProject(),
									LoginBean.getProduct()),
							LoginBean.getVersion(), flow, true, true);

			List<String> flowSteps = new ArrayList<String>();

			for (JJFlowStep f : flows) {
				if (!f.equals(flow))
					flowSteps.add(f.getName());
			}

			list.add(new FlowStepUtil(flow, flows.get(i), l));
			i++;
			if (i == flows.size())
				i = 0;
		}

		return list;

	}

}

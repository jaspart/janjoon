package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

@FacesValidator("onSubmitTaskValidator")
public class OnSubmitTaskValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent component, Object value)
			throws ValidatorException {
		
		String type = (String) component.getAttributes().get("type");
		if(type.equalsIgnoreCase("workload"))
		{
			Integer workload=(Integer) value;
			if(workload == null)
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Workload is requierd", null));
			if(workload <= 0)
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Please set Workload More than 0", null));
		}
		
		if(type.equalsIgnoreCase("startDate"))
		{
			Date startDate=(Date) value;
			if(startDate == null)
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"start date is requierd", null));
			
			else
			{
				if(((JJTaskBean)LoginBean.findBean("jJTaskBean")).getMode().equalsIgnoreCase("scrum"))
				{
					if(startDate.before(((JJSprintBean)LoginBean.findBean("jJSprintBean")).getSprintUtil().getSprint().getStartDate()))
					{
						throw new ValidatorException(
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Start Date may not be before Sprint Start Date.",
										null));
					}else if(startDate.after(((JJSprintBean)LoginBean.findBean("jJSprintBean")).getSprintUtil().getSprint().getEndDate()))
					{
						throw new ValidatorException(new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"End Date may not be after Sprint end Date.",
								null));
					}
				}else
				{
					if(((JJTaskBean)LoginBean.findBean("jJTaskBean")).getSprint() != null)
					{
						if(startDate.before(((JJTaskBean)LoginBean.findBean("jJTaskBean")).getSprint().getStartDate()))
						{
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start Date may not be before Sprint Start Date.",
											null));
						}else if(startDate.after(((JJTaskBean)LoginBean.findBean("jJTaskBean")).getSprint().getEndDate()))
						{
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"End Date may not be after Sprint end Date.",
									null));
						}
					
					
					}
				}
				
			}
				
		}
		
		
	}

}

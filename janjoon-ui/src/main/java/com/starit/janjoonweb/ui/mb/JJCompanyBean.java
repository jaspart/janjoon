package com.starit.janjoonweb.ui.mb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJCompany.class, beanName = "jJCompanyBean")
public class JJCompanyBean {
	
	private List<JJCompany> companies;
	
	private CalendarUtil companyCalendar;

	public CalendarUtil getCompanyCalendar() {
		if(companyCalendar == null)
			companyCalendar=new CalendarUtil(jJCompanyService.findAllJJCompanys().get(0));
		return companyCalendar;
	}

	public void setCompanyCalendar(CalendarUtil companyCalendar) {
		this.companyCalendar = companyCalendar;
	}

	public List<JJCompany> getCompanies() {
		return companies;
	}

	public void setCompanies(List<JJCompany> companies) {
		this.companies = companies;
	}
	
	public void initCalendar()
	{
		for(JJCompany company:jJCompanyService.findAllJJCompanys())
		{
			CalendarUtil calendar=new CalendarUtil(company);
			int i=0;
			SimpleDateFormat output = new SimpleDateFormat("HH:mm");
			while(i<7)
			{
				if(i != 0 && i != 6)
					
				System.err.println(output.format(calendar.getWorkDays().get(i).getStartDate1()));
				i++;
			}
			output=new SimpleDateFormat("dd/MM/yyyy");
			i=0;
			while (i<calendar.getHolidays().size())
			{
				System.err.println(output.format(calendar.getHolidays().get(i)));
				i++;
			}
			
		}
	}
	
}

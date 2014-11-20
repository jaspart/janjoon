package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCompany.class, beanName = "jJCompanyBean")
public class JJCompanyBean {
	
	private String calendar;
	private boolean update;
	private List<JJCompany> companies;
	private String headerMessage;
	private JJCompany companie;	
	
	public String getCalendar() {
		return calendar;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	private CalendarUtil companyCalendar;

	public CalendarUtil getCompanyCalendar() {
		
		if(companyCalendar == null)
			companyCalendar=new CalendarUtil(jJCompanyService.findAllJJCompanys().get(0));
		return companyCalendar;
	}

	public void setCompanyCalendar(CalendarUtil companyCalendar) {
		this.companyCalendar = companyCalendar;
	}

	public String getHeaderMessage() {
		return headerMessage;
	}

	public void setHeaderMessage(String headerMessage) {
		this.headerMessage = headerMessage;
	}

	public JJCompany getCompanie() {
		
		if(companie==null)
		{
			companie=new JJCompany();
			update=true;
		}else if(companie.getId()==null)
			update=true;
		else 
			update=false;
		
		return companie;
	}

	public void setCompanie(JJCompany companie) {
		this.companie = companie;
	}

	public List<JJCompany> getCompanies() {
			
		
		if(companies == null)
			companies=jJCompanyService.getActifCompanies();
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
	
	public void closeDialog() {
		companie=null;
	}
	
	public void deleteCompany()
	{
		if (companie != null) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			JJContact contact = (JJContact) session
					.getAttribute("JJContact");
			companie.setEnabled(false);
			updateJJCompany(companie);
			companies=null;
			
			String message = "message_successfully_deleted";
			FacesMessage facesMessage = MessageFactory.getMessage(message, "Company");			

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}
	public void saveCompany()
	{
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext()
				.getSession(false);

		JJContact contact = (JJContact) session
				.getAttribute("JJContact");
		String message = "";
		FacesMessage facesMessage = null;
//		if(calendar != null)
//			companie.setCalendar(calendar);
	
			if (companie.getId() == null) {
				companie.setEnabled(true);
				saveJJCompany(companie);
				message = "message_successfully_created";

			}
			else {
				updateJJCompany(companie);
				message = "message_successfully_updated";
			
			}
			companies=null;
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('companyDialogWidget').hide()");

			facesMessage = MessageFactory.getMessage(message, "Company");			

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	
	}
	public void saveJJCompany(JJCompany b)
	{
		JJContact contact=(JJContact) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("JJContact");
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJCompanyService.saveJJCompany(b);
	}
	
	public void updateJJCompany(JJCompany b)
	{
		JJContact contact=(JJContact) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("JJContact");
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCompanyService.updateJJCompany(b);
	}
	
	public void handleFileUpload(FileUploadEvent event) throws IOException {
	
		Scanner s = new Scanner(event.getFile().getInputstream()).useDelimiter("\\A");
		calendar=(s.hasNext() ? s.next() : "");
	}
}

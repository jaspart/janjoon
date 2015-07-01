package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;
import com.starit.janjoonweb.ui.mb.util.ChunkTime;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCompany.class, beanName = "jJCompanyBean")
public class JJCompanyBean {

	private String calendar;
	private boolean update;
	private List<JJCompany> companies;
	private String headerMessage;
	private JJCompany companie;
	private byte[] logo;
	private CalendarUtil calendarUtil;
	private Date day;
	private List<ChunkTime> workDays;

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

		if (companyCalendar == null)
			companyCalendar = new CalendarUtil(jJCompanyService
					.findAllJJCompanys().get(0));
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

		if (companie == null) {
			companie = new JJCompany();
			logo = null;
			update = true;
		} else if (companie.getId() == null)
			update = true;
		else
			update = false;

		return companie;
	}

	public void setCompanie(JJCompany companie) {
		this.calendarUtil = null;
		this.workDays = null;
		this.companie = companie;
	}

	public JJCompanyService getJJCompanyService() {
		return jJCompanyService;
	}

	public List<JJCompany> getCompanies() {

		if (companies == null)
			companies = jJCompanyService.getActifCompanies();
		return companies;
	}

	public void setCompanies(List<JJCompany> companies) {
		this.companies = companies;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public List<ChunkTime> getWorkDays() {

		if (calendarUtil == null && companie != null)
			calendarUtil = new CalendarUtil(companie);
		if (workDays == null && companie != null)
			workDays = calendarUtil.getWorkDays();

		if (calendarUtil != null
				&& (workDays == null || workDays.contains(null))) {
			int i = 0;
			if (workDays == null)
				workDays = new ArrayList<ChunkTime>();
			while (i < 7) {

				if (workDays.get(i) == null)
					workDays.set(i, new ChunkTime(i));
				i++;
			}
		}

		return workDays;
	}

	public List<Date> getHolidays() {
		if (calendarUtil == null && companie != null)
			calendarUtil = new CalendarUtil(companie);
		if (companie != null)
			return calendarUtil.getHolidays();
		else
			return null;
	}

	public void addHoliday() throws IOException {
		String val = calendarUtil.addHoliday(day);
		companie.setCalendar(val);
		updateJJCompany(companie);
		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Company Calendar");
		day = null;
		companie = jJCompanyService.findJJCompany(companie.getId());
		calendarUtil = new CalendarUtil(companie);
		getWorkDays();
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void timeSelectListener(ChunkTime day, int type) throws IOException {
		if (type == 1
				&& ((day.getStartDate1() != null && day.getEndDate1() != null) || (day
						.getStartDate1() == null && day.getEndDate1() == null))
				|| (type == 2 && ((day.getStartDate2() != null && day
						.getEndDate2() != null) || (day.getStartDate2() == null && day
						.getEndDate2() == null)))) {

			boolean up = true;
			if (day.getStartDate1() != null
					&& day.getStartDate1().after(day.getEndDate1()))
				up = false;
			if (up && day.getStartDate2() != null
					&& day.getStartDate2().after(day.getEndDate2()))
				up = false;
			if (up) {
				String val = calendarUtil.editWorkday(day);
				companie.setCalendar(val);
				updateJJCompany(companie);
				String message = "message_successfully_updated";
				FacesMessage facesMessage = MessageFactory.getMessage(message,
						"Company Calendar");
				companie = jJCompanyService.findJJCompany(companie.getId());
				calendarUtil = new CalendarUtil(companie);
				getWorkDays();
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			} else {

				String message = "validator_date_startAfterEnd";
				FacesMessage facesMessage = MessageFactory.getMessage(message,
						FacesMessage.SEVERITY_ERROR, "Company Calendar",
						"Company Calendar");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			}

		}

	}

	public void initCalendar() {
		for (JJCompany company : jJCompanyService.findAllJJCompanys()) {
			CalendarUtil calendar = new CalendarUtil(company);
			int i = 0;
			SimpleDateFormat output = new SimpleDateFormat("HH:mm");
			while (i < 7) {
				if (i != 0 && i != 6)

					System.err.println(output.format(calendar.getWorkDays()
							.get(i).getStartDate1()));
				i++;
			}
			output = new SimpleDateFormat("dd/MM/yyyy");
			i = 0;
			while (i < calendar.getHolidays().size()) {
				System.err
						.println(output.format(calendar.getHolidays().get(i)));
				i++;
			}

		}
	}

	public void closeDialog() {
		companie = null;
		logo = null;
		calendarUtil = null;
		day = null;
		companies = null;
		workDays = null;
	}

	public void deleteCompany() {
		if (companie != null) {
			companie.setEnabled(false);
			updateJJCompany(companie);
			companies = null;

			String message = "message_successfully_deleted";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					"Company");

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void saveCompany() {
		String message = "";
		FacesMessage facesMessage = null;

		if (logo != null)
			companie.setLogo(logo);

		if (companie.getId() == null) {
			companie.setEnabled(true);
			saveJJCompany(companie);
			message = "message_successfully_created";

		} else {
			updateJJCompany(companie);
			message = "message_successfully_updated";

		}

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
		if (loginBean.getContact().getCompany().equals(companie))
			loginBean.getAuthorisationService().setSession(session);

		companies = null;
		logo = null;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('companyDialogWidget').hide()");
		facesMessage = MessageFactory.getMessage(message, "Company");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void saveJJCompany(JJCompany b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJCompanyService.saveJJCompany(b);
	}

	public void updateJJCompany(JJCompany b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCompanyService.updateJJCompany(b);
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		Scanner s = new Scanner(event.getFile().getInputstream())
				.useDelimiter("\\A");
		calendar = (s.hasNext() ? s.next() : "");
	}
}

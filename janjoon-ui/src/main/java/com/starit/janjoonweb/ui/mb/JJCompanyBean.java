package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyCompanyDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyStatusDataModel;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;
import com.starit.janjoonweb.ui.mb.util.ChunkTime;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJCompany.class, beanName = "jJCompanyBean")
public class JJCompanyBean {

	private String					calendar;
	private LazyCompanyDataModel	lazyCompanyList;
	private String					updatedCompanyCalendar;
	private boolean					update;
	private boolean					disabledsaveCalendar;
	private List<JJCompany>			companies;
	private SelectItem[]			companyOptions;
	private String					headerMessage;
	private JJCompany				companie;
	private byte[]					logo;
	private CalendarUtil			calendarUtil;
	private Date					day;
	private List<ChunkTime>			workDays;

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

	public boolean isDisabledsaveCalendar() {
		return disabledsaveCalendar;
	}

	public void setDisabledsaveCalendar(boolean disabledsaveCalendar) {
		this.disabledsaveCalendar = disabledsaveCalendar;
	}

	public String getUpdatedCompanyCalendar() {
		return updatedCompanyCalendar;
	}

	public void setUpdatedCompanyCalendar(String updatedCompanyCalendar) {
		this.updatedCompanyCalendar = updatedCompanyCalendar;
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
			disabledsaveCalendar = true;
		} else if (companie.getId() == null)
			update = true;
		else
			update = false;

		return companie;
	}

	public void setCompanie(JJCompany companie) {
		this.calendarUtil = null;
		this.workDays = null;
		this.holidays = null;
		this.disabledsaveCalendar = true;
		if (companie == null)
			this.updatedCompanyCalendar = null;
		else
			this.updatedCompanyCalendar = companie.getCalendar();
		this.companie = companie;
	}

	public LazyCompanyDataModel getLazyCompanyList() {

		if (lazyCompanyList == null)
			lazyCompanyList = new LazyCompanyDataModel(jJCompanyService);
		return lazyCompanyList;
	}

	public void setLazyCompanyList(LazyCompanyDataModel lazyCompanyList) {
		this.lazyCompanyList = lazyCompanyList;
	}

	public JJCompanyService getJJCompanyService() {
		return jJCompanyService;
	}

	public List<JJCompany> getCompanies() {

		if (companies == null) {
			if (((LoginBean) LoginBean.findBean("loginBean")).isEnable()
			        && ((LoginBean) LoginBean.findBean("loginBean")).getAuthorisationService().isAdminCompany())
				companies = jJCompanyService.getActifCompanies();
			else if (((LoginBean) LoginBean.findBean("loginBean")).isEnable()) {
				companies = new ArrayList<JJCompany>();
				companies.add(LoginBean.getCompany());
			} else {
				companies = jJCompanyService.getActifCompanies();
			}

			companyOptions = null;
		}

		return companies;
	}

	public void setCompanies(List<JJCompany> companies) {
		this.companyOptions = null;
		this.companies = companies;
	}

	public SelectItem[] getCompanyOptions() {

		if (companyOptions == null) {

			getCompanies();
			companyOptions = new SelectItem[companies.size() + 1];

			companyOptions[0] = new SelectItem("", MessageFactory.getMessage("label_all").getDetail());
			int i = 0;
			for (JJCompany comp : companies) {
				companyOptions[i + 1] = new SelectItem(comp.getName(), comp.getName());
				i++;

			}

		}
		return companyOptions;
	}

	public void setCompanyOptions(SelectItem[] companyOptions) {
		this.companyOptions = companyOptions;
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

		if (calendarUtil != null && (workDays == null || workDays.contains(null))) {
			int i = 0;
			if (workDays == null)
				workDays = new ArrayList<ChunkTime>();
			while (i < 7) {

				try {
					if (workDays.get(i) == null)
						workDays.set(i, new ChunkTime(i));

				} catch (IndexOutOfBoundsException e) {
					workDays.add(new ChunkTime(i));
				}

				i++;
			}
		}

		return workDays;
	}

	private List<Date> holidays;

	public List<Date> getHolidays() {

		if (holidays == null) {
			if (calendarUtil == null && companie != null)
				calendarUtil = new CalendarUtil(companie);
			if (companie != null)
				holidays = calendarUtil.getHolidays();
			else
				holidays = null;
		}

		return holidays;

	}

	public void addHoliday() throws IOException {
		updatedCompanyCalendar = CalendarUtil.addHoliday(day, updatedCompanyCalendar, holidays.size());
		disabledsaveCalendar = false;
		holidays.add(day);
		day = null;

	}

	public void removeHoliday(Date holiday) throws IOException {
		updatedCompanyCalendar = CalendarUtil.removeHoliday(holiday, updatedCompanyCalendar, holidays.indexOf(holiday));
		disabledsaveCalendar = false;
		holidays.remove(holiday);
	}

	public void timeSelectListener(ChunkTime day, int type) throws IOException {

		if (type == 1
		        && ((day.getStartDate1() != null && day.getEndDate1() != null)
		                || (day.getStartDate1() == null && day.getEndDate1() == null))
		        || (type == 2 && ((day.getStartDate2() != null && day.getEndDate2() != null)
		                || (day.getStartDate2() == null && day.getEndDate2() == null)))) {

			boolean up = true;
			if (day.getStartDate1() != null && day.getStartDate1().after(day.getEndDate1()))
				up = false;
			if (up && day.getStartDate2() != null && day.getStartDate2().after(day.getEndDate2()))
				up = false;

			if (up && type == 2 && day.getStartDate2() != null
			        && ((day.getStartDate1() != null && day.getStartDate2().before(day.getStartDate1()))
			                || (day.getEndDate1() != null && day.getStartDate2().before(day.getEndDate1()))))
				up = false;

			if (up) {
				updatedCompanyCalendar = calendarUtil.editWorkday(day);
				disabledsaveCalendar = false;
			} else {

				String message = "validator_date_startAfterEndCompany";
				FacesMessage facesMessage = MessageFactory.getMessage(message, FacesMessage.SEVERITY_ERROR,
				        new Object());
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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

					System.err.println(output.format(calendar.getWorkDays().get(i).getStartDate1()));
				i++;
			}
			output = new SimpleDateFormat("dd/MM/yyyy");
			i = 0;
			while (i < calendar.getHolidays().size()) {
				System.err.println(output.format(calendar.getHolidays().get(i)));
				i++;
			}

		}
	}

	public void closeDialog() {
		companie = null;
		logo = null;
		calendarUtil = null;
		day = null;
		disabledsaveCalendar = true;
		updatedCompanyCalendar = null;
		companies = null;
		companyOptions = null;
		workDays = null;
		holidays = null;
	}

	public void saveCalendar() {

		companie.setCalendar(updatedCompanyCalendar);
		updateJJCompany(companie);
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
		if (LoginBean.getCompany().equals(companie))
			loginBean.getAuthorisationService().setSession(session);

		companies = null;
		companyOptions = null;
		logo = null;

		FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_updated",
		        MessageFactory.getMessage("label_company", "").getDetail(), "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('companyDialogWidget').hide()");
		RequestContext.getCurrentInstance().update("growlForm");
	}

	public void deleteCompany() {
		if (companie != null) {
			companie.setEnabled(false);
			updateJJCompany(companie);
			companies = null;
			companyOptions = null;
			String message = "message_successfully_deleted";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
			        MessageFactory.getMessage("label_company", "").getDetail(), "e");

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void saveCompany() {
		String message = "";
		FacesMessage facesMessage = null;

		if (!disabledsaveCalendar)
			companie.setCalendar(updatedCompanyCalendar);

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

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
		if (LoginBean.getCompany().equals(companie))
			loginBean.getAuthorisationService().setSession(session);

		companies = null;
		companyOptions = null;
		logo = null;

		facesMessage = MessageFactory.getMessage(message, MessageFactory.getMessage("label_company", "").getDetail(),
		        "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('companyDialogWidget').hide()");
		RequestContext.getCurrentInstance().update("growlForm");

	}

	public void saveJJCompany(JJCompany b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);
		jJCompanyService.saveJJCompany(b);
	}

	public void updateJJCompany(JJCompany b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJCompanyService.updateJJCompany(b);
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		Scanner s = new Scanner(event.getFile().getInputstream()).useDelimiter("\\A");
		calendar = (s.hasNext() ? s.next() : "");
	}
}

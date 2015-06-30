package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.starit.janjoonweb.domain.JJCompany;

public class CalendarUtil {

	private static String WORK_DAYS = "weekday";
	private static String HOLIDAYS = "holiday";

	private JJCompany company;
	private String hour_format;
	private List<Date> holidays;
	private List<ChunkTime> workDays;

	public CalendarUtil(JJCompany company) {
		this.company = company;
		try {
			initCompanyCalendar();
		} catch (IOException e) {
			workDays = new ArrayList<ChunkTime>();
			e.printStackTrace();
		}
	}

	public void initCompanyCalendar() throws IOException {

		Properties properties = new Properties();
		String calendar = company.getCalendar();
		if (calendar == null) {
			calendar = "";
		}
		properties.load(new StringReader(calendar));
		// properties.load(new FileInputStream(string));
		// initWorkDays
		int i = 0;
		workDays = new ArrayList<ChunkTime>();
		hour_format = properties.getProperty("hour.format");

		if (hour_format == null)
			hour_format = "HH:mm";

		while (i < 7) {
			String workDay = properties.getProperty(WORK_DAYS + "." + i);

			if (workDay == null || workDay.contains("null")) {
				if (workDay != null)
					workDays.add(new ChunkTime(i));
				else
					workDays.add(null);
			}

			else {
				Date staDate1, enDate1, staDate2, endDate2;
				try {
					staDate1 = new SimpleDateFormat(hour_format).parse(workDay
							.substring(0, 5));

				} catch (ParseException e) {
					staDate1 = null;
				}
				try {
					enDate1 = new SimpleDateFormat(hour_format).parse(workDay
							.substring(6, 11));
				} catch (ParseException e) {
					enDate1 = null;
				}
				try {
					staDate2 = new SimpleDateFormat(hour_format).parse(workDay
							.substring(14, 19));
				} catch (ParseException | StringIndexOutOfBoundsException e) {
					staDate2 = null;
				}
				try {
					endDate2 = new SimpleDateFormat(hour_format).parse(workDay
							.substring(20, 25));
				} catch (ParseException | StringIndexOutOfBoundsException e) {
					endDate2 = null;
				}
				workDays.add(new ChunkTime(i, staDate1, enDate1, staDate2,
						endDate2));
			}

			i++;
		}

		i = 0;
		while (i < workDays.size() && workDays.get(i) == null)
			i++;

		if (workDays.size() == i)
			workDays = null;

		// initHolidays
		String day_format = properties.getProperty("day.format");
		if (day_format == null)
			day_format = "dd/MM/yyyy";
		i = 1;
		boolean nullPointerException = false;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		holidays = new ArrayList<Date>();
		while (!nullPointerException) {
			try {
				String hol = properties.getProperty(HOLIDAYS + "." + i)
						.toString().trim()
						+ year;
				try {
					holidays.add(getZeroTimeDate(new SimpleDateFormat(
							day_format).parse(hol)));
				} catch (ParseException e) {

				}
				i++;

			} catch (NullPointerException e) {
				nullPointerException = true;
			}
		}

	}

	public JJCompany getCompany() {
		return company;
	}

	public void setCompany(JJCompany company) {
		this.company = company;
	}

	public String getHour_format() {
		return hour_format;
	}

	public void setHour_format(String hour_format) {
		this.hour_format = hour_format;
	}

	public List<Date> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Date> holidays) {
		this.holidays = holidays;
	}

	public List<ChunkTime> getWorkDays() {
		return workDays;
	}

	public void setWorkDays(List<ChunkTime> workDays) {
		this.workDays = workDays;
	}

	public static Date getZeroTimeDate(Date fecha) {
		Date res = fecha;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(fecha);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		res = calendar.getTime();

		return res;
	}

	public static Date getPreviousDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return getZeroTimeDate(cal.getTime());
	}

	public static Date getAfterDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return getZeroTimeDate(cal.getTime());
	}

	public String addHoliday(Date date) throws IOException {
		Properties properties = new Properties();
		String calendar = company.getCalendar();
		if (calendar == null) {
			calendar = "";
		}
		properties.load(new StringReader(calendar));
		DateFormat f = new SimpleDateFormat("dd/MM/");
		properties.setProperty(HOLIDAYS + "." + String.format("%01d", holidays.size() + 1),
				f.format(date));

		StringWriter writer = new StringWriter();
		properties.list(new PrintWriter(writer));
		return writer.getBuffer().toString();

	}

	public String editWorkday(ChunkTime day) throws IOException {
		Properties properties = new Properties();
		String calendar = company.getCalendar();
		if (calendar == null) {
			calendar = "";
		}
		properties.load(new StringReader(calendar));
		DateFormat f = new SimpleDateFormat("HH:mm");
		String value;
		if (day.getStartDate1() != null && day.getStartDate2() != null)
			value = f.format(day.getStartDate1()) + "-"
					+ f.format(day.getEndDate1()) + " & "
					+ f.format(day.getStartDate2()) + "-"
					+ f.format(day.getEndDate2());
		else {
			if (day.getStartDate1() != null)
				value = f.format(day.getStartDate1()) + "-"
						+ f.format(day.getEndDate1());
			else if (day.getStartDate2() != null)
				value = f.format(day.getStartDate2()) + "-"
						+ f.format(day.getEndDate2());
			else
				value = "null";
		}

		properties.setProperty(WORK_DAYS + "." + day.getDayNumber(), value);

		StringWriter writer = new StringWriter();
		properties.list(new PrintWriter(writer));
		return writer.getBuffer().toString();

	}

	public static Date getZeroDate(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(1970, 0, 1);
			return calendar.getTime();

		} else
			return null;

	}
}

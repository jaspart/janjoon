package com.starit.janjoonweb.ui.mb.util;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyService;

public class CalendarUtil {

	private static String WORK_DAYS = "weekday";
	private static String HOLIDAYS = "holiday";

	private JJCompany company;
	private List<Date> holidays;
	private List<ChunkTime> workDays;

	@Autowired
	private JJCompanyService jCompanyService;

	public CalendarUtil(JJCompany company) {
		this.company = company;
		try {
			initCompanyCalendar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initCompanyCalendar() throws IOException {

		Properties properties = new Properties();
		properties.load(new StringReader(company.getCalendar()));
		// properties.load(new FileInputStream(string));
		// initWorkDays
		int i = 0;
		workDays = new ArrayList<ChunkTime>();
		String hour_format = properties.getProperty("hour.format");
		while (i < 7) {
			String workDay = properties.getProperty(WORK_DAYS + "." + i);
			// Pattern pattern=Pattern.compile("HH:mm-HH:mm & HH:mm-HH:mm");

			if (workDay.contains("null"))
				workDays.add(new ChunkTime(i));
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
				} catch (ParseException e) {
					staDate2 = null;
				}
				try {
					endDate2 = new SimpleDateFormat(hour_format).parse(workDay
							.substring(20, 25));
				} catch (ParseException e) {
					endDate2 = null;
				}
				workDays.add(new ChunkTime(i, staDate1, enDate1, staDate2,
						endDate2));
			}

			i++;
		}

		// initHolidays
		String day_format = properties.getProperty("day.format");
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

	public void setjCompanyService(JJCompanyService jCompanyService) {
		this.jCompanyService = jCompanyService;

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

	@SuppressWarnings("deprecation")
	public Date nextWorkingDate(Date date) {

		if (!isHoliday(date)) {

			ChunkTime workingDate = getWorkingChunkTime(date.getDay());

			if (workingDate.isWeekEnd()) {
				return nextWorkingDate(getZeroTimeDate(new Date(date.getTime()
						+ (1000 * 60 * 60 * 24))));
			} else {
				if (isWorkingTime(date, workingDate))
					return date;
				else
					return returnWorkingTime(date, workingDate);
			}

		} else
			return nextWorkingDate(getZeroTimeDate(new Date(date.getTime()
					+ (1000 * 60 * 60 * 24))));

	}

	@SuppressWarnings("deprecation")
	private Date returnWorkingTime(Date date, ChunkTime workingDate) {

		if (date.getHours() < workingDate.getStartDate1().getHours())
			date.setHours(workingDate.getStartDate1().getHours());
		else {
			if (date.getHours() < workingDate.getStartDate2().getHours())
				date.setHours(workingDate.getStartDate2().getHours());
			else
				date = nextWorkingDate(getZeroTimeDate(new Date(date.getTime()
						+ (1000 * 60 * 60 * 24))));

		}

		return date;
	}

	@SuppressWarnings("deprecation")
	private boolean isHoliday(Date date) {
		int i = 0;
		boolean isHoliday = false;
		while (i < holidays.size() && !isHoliday) {
			Date hol = holidays.get(i);
			hol.setYear(date.getYear());
			isHoliday = (getZeroTimeDate(date).compareTo(hol) == 0);
			i++;
		}
		return isHoliday;

	}

	private static Date getZeroTimeDate(Date fecha) {
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

	private ChunkTime getWorkingChunkTime(int i) {
		ChunkTime c = null;
		for (ChunkTime chunk : workDays) {
			if (chunk.getDayNumber() == i) {
				c = chunk;
				break;
			}
		}
		return c;
	}

	@SuppressWarnings("deprecation")
	private boolean isWorkingTime(Date date, ChunkTime chunk) {
		if (chunk.getStartDate1().getHours() <= date.getHours()
				&& chunk.getEndDate1().getHours() > date.getHours())
			return true;
		else if (chunk.getStartDate2().getHours() <= date.getHours()
				&& chunk.getEndDate2().getHours() > date.getHours())
			return true;
		else
			return false;

	}

	public static Date getPreviousDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return getZeroTimeDate(cal.getTime());
	}

	@SuppressWarnings("deprecation")
	public int calculateWorkLoad(Date start, Date end) {

		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		long workload = 0;
		if (start.before(end)) {
			ChunkTime startChunk = getWorkingChunkTime(start.getDay());
			ChunkTime endChunk = getWorkingChunkTime(end.getDay());

			int chunkNumber = (int) -((getZeroTimeDate(
					new Date(start.getTime() + (1000 * 60 * 60 * 24)))
					.getTime() - getPreviousDate(end).getTime()) / DAY_IN_MILLIS)+1;


			if (chunkNumber > 0) {
				int startChunkInt = getZeroTimeDate(
						new Date(start.getTime() + (1000 * 60 * 60 * 24))).getDay();
				Date startChunkDate = getZeroTimeDate(new Date(start.getTime()
						+ (1000 * 60 * 60 * 24)));
				int i = 0;
				while (i < chunkNumber) {
					if (startChunkInt == 7)
						startChunkInt = 0;
					if (!isHoliday(startChunkDate))
						workload = workload
								+ getWorkingChunkTime(startChunkInt)
										.getChunkWorkLoad();

					startChunkDate = getZeroTimeDate(new Date(
							startChunkDate.getTime() + (1000 * 60 * 60 * 24)));
					startChunkInt++;
					i++;
				}
			}
			if(chunkNumber == -1)
			{
				long diff = getZeroDate(end).getTime()
						- getZeroDate(start).getTime();
				diff = diff / (60 * 60 * 1000) % 24;
				
				if (getZeroDate(start).before(getZeroDate(startChunk.getEndDate1())))
					diff = diff
							- ((getZeroDate(startChunk.getStartDate2()).getTime() - getZeroDate(
									startChunk.getEndDate1()).getTime())
									/ (60 * 60 * 1000) % 24);
				
				else if (getZeroDate(end).after(getZeroDate(endChunk.getStartDate2())))
					diff = diff
					- ((getZeroDate(endChunk.getStartDate2()).getTime() - getZeroDate(
							endChunk.getEndDate1()).getTime())
							/ (60 * 60 * 1000) % 24);
				
				workload=workload+diff;				
				
			}else
			{
				workload = workload + getRestOfChunk(start, startChunk, true);
				workload = workload + getRestOfChunk(end, endChunk, false);
			}
			

			return Math.round(workload);
		} else
			return 0;

	}

	public static Date getZeroDate(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(1970, 0, 1);
		return calendar.getTime();

	}

	private long getRestOfChunk(Date date, ChunkTime Chunk, boolean after) {

		long diff;
		if (after) {
			diff = getZeroDate(Chunk.getEndDate2()).getTime()
					- getZeroDate(date).getTime();

			diff = diff / (60 * 60 * 1000) % 24;

			if (getZeroDate(date).before(getZeroDate(Chunk.getEndDate1())))
				diff = diff
						- ((getZeroDate(Chunk.getStartDate2()).getTime() - getZeroDate(
								Chunk.getEndDate1()).getTime())
								/ (60 * 60 * 1000) % 24);

		} else {
			diff = getZeroDate(date).getTime()
					- getZeroDate(Chunk.getStartDate1()).getTime();

			diff = diff / (60 * 60 * 1000) % 24;

			if (getZeroDate(date).after(getZeroDate(Chunk.getStartDate2())))
				diff = diff
						- ((getZeroDate(Chunk.getStartDate2()).getTime() - getZeroDate(
								Chunk.getEndDate1()).getTime())
								/ (60 * 60 * 1000) % 24);

		}

		return diff;
	}
}

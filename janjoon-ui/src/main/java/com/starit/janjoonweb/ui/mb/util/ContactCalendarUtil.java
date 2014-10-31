package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;

public class ContactCalendarUtil {

	private static String VACATION = "vacation";
	private static String WORK_DAYS = "weekday";
	private JJContact contact;
	private CalendarUtil calendarUtil;
	private List<ChunkTime> workDays;
	private List<ChunkPeriod> vacation;

	public ContactCalendarUtil(JJContact contact) {

		this.contact = contact;
		this.calendarUtil = new CalendarUtil(contact.getCompany());
		try {
			initVactions();
		} catch (IOException | ParseException e) {
			vacation = new ArrayList<ChunkPeriod>();
			workDays = this.calendarUtil.getWorkDays();
			e.printStackTrace();
		}
	}

	public ContactCalendarUtil(JJCompany company) {

		this.calendarUtil = new CalendarUtil(company);
		this.contact = null;
		vacation = new ArrayList<ChunkPeriod>();

	}

	private void initVactions() throws IOException, ParseException {
		workDays = this.calendarUtil.getWorkDays();
		vacation = new ArrayList<ChunkPeriod>();
		if (contact.getCalendar() != null) {
			Properties properties = new Properties();
			properties.load(new StringReader(contact.getCalendar()));

			int j = 0;
			while (j > 7) {
				try {
					String workDay = properties
							.getProperty(WORK_DAYS + "." + j);
					if (workDay.contains("null"))
						workDays.set(this.getWorkingChunkTimeIndex(j),
								new ChunkTime(j));
					else {
						Date staDate1, enDate1, staDate2, endDate2;
						try {
							staDate1 = new SimpleDateFormat(
									calendarUtil.getHour_format())
									.parse(workDay.substring(0, 5));

						} catch (ParseException e) {
							staDate1 = null;
						}
						try {
							enDate1 = new SimpleDateFormat(
									calendarUtil.getHour_format())
									.parse(workDay.substring(6, 11));
						} catch (ParseException e) {
							enDate1 = null;
						}
						try {
							staDate2 = new SimpleDateFormat(
									calendarUtil.getHour_format())
									.parse(workDay.substring(14, 19));
						} catch (ParseException
								| StringIndexOutOfBoundsException e) {
							staDate2 = null;
						}
						try {
							endDate2 = new SimpleDateFormat(
									calendarUtil.getHour_format())
									.parse(workDay.substring(20, 25));
						} catch (ParseException
								| StringIndexOutOfBoundsException e) {
							endDate2 = null;
						}
						workDays.set(this.getWorkingChunkTimeIndex(j),
								new ChunkTime(j, staDate1, enDate1, staDate2,
										endDate2));
					}
				} catch (NullPointerException e) {

				}

				j++;
			}

			if (contact.getCalendar() != null) {
				if (!contact.getCalendar().isEmpty()) {
					int i = 1;
					boolean nullPointerException = false;

					while (!nullPointerException) {
						try {
							String hol = properties
									.getProperty(
											VACATION + "."
													+ String.format("%02d", i))
									.toString().trim();
							int index = hol.indexOf("to");
							Date date1 = new SimpleDateFormat("dd/MM/yyyy")
									.parse(hol.substring(0, index - 1));
							Date date2 = new SimpleDateFormat("dd/MM/yyyy")
									.parse(hol.substring(index + 2, index + 13));
							vacation.add(new ChunkPeriod(date1, date2));

							i++;

						} catch (NullPointerException e) {
							nullPointerException = true;
						}
					}
				}
			}
		}		
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public CalendarUtil getCalendarUtil() {
		return calendarUtil;
	}

	public void setCalendarUtil(CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public List<ChunkTime> getWorkDays() {
		return workDays;
	}

	public void setWorkDays(List<ChunkTime> workDays) {
		this.workDays = workDays;
	}

	public List<ChunkPeriod> getVacation() {
		return vacation;
	}

	public void setVacation(List<ChunkPeriod> vacation) {
		this.vacation = vacation;
	}

	public void addVacation(Date date1, Date date2,
			JJContactService jJContactService) {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String nl = "";
		StringBuilder calendar;
		if (date1.before(date2)) {
			nl = VACATION + "." + vacation.size() + 1 + "=" + df.format(date1)
					+ " to " + df.format(date2);

		} else {
			nl = VACATION + "." + vacation.size() + 1 + "=" + df.format(date2)
					+ " to " + df.format(date1);
		}
		if (contact.getCalendar() != null) {
			calendar = new StringBuilder(contact.getCalendar());
			calendar.append(System.lineSeparator());
		} else
			calendar = new StringBuilder();

		calendar.append(nl);
		contact.setCalendar(calendar.toString());
		jJContactService.updateJJContact(contact);
	}

	@SuppressWarnings("deprecation")
	public Date nextWorkingDate(Date date) {

		if ((!isHoliday(date)) && (isVacation(date) == -1)) {

			ChunkTime workingDate = getWorkingChunkTime(date.getDay());

			if (workingDate.isWeekEnd()) {
				return nextWorkingDate(CalendarUtil.getZeroTimeDate(new Date(
						date.getTime() + (1000 * 60 * 60 * 24))));
			} else {
				if (isWorkingTime(date, workingDate))
					return date;
				else
					return returnWorkingTime(date, workingDate);
			}

		} else if (isHoliday(date))
			return nextWorkingDate(CalendarUtil.getZeroTimeDate(new Date(date
					.getTime() + (1000 * 60 * 60 * 24))));
		else
			return nextWorkingDate(CalendarUtil.getZeroTimeDate(vacation.get(
					isVacation(date)).getEndVacation()));

	}

	@SuppressWarnings("deprecation")
	public int calculateWorkLoad(Date start, Date end) {

		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		long workload = 0;
		if (start.before(end)) {
			ChunkTime startChunk = getWorkingChunkTime(start.getDay());
			ChunkTime endChunk = getWorkingChunkTime(end.getDay());

			int chunkNumber = (int) -((CalendarUtil.getZeroTimeDate(
					new Date(start.getTime() + (1000 * 60 * 60 * 24)))
					.getTime() - CalendarUtil.getPreviousDate(end).getTime()) / DAY_IN_MILLIS) + 1;

			if (chunkNumber > 0) {
				int startChunkInt = CalendarUtil.getZeroTimeDate(
						new Date(start.getTime() + (1000 * 60 * 60 * 24)))
						.getDay();
				Date startChunkDate = CalendarUtil.getZeroTimeDate(new Date(
						start.getTime() + (1000 * 60 * 60 * 24)));
				int i = 0;
				while (i < chunkNumber) {
					if (startChunkInt == 7)
						startChunkInt = 0;
					if (!isHoliday(startChunkDate)
							&& (isVacation(startChunkDate) == -1))
						workload = workload
								+ getWorkingChunkTime(startChunkInt)
										.getChunkWorkLoad();

					startChunkDate = CalendarUtil.getZeroTimeDate(new Date(
							startChunkDate.getTime() + (1000 * 60 * 60 * 24)));
					startChunkInt++;
					i++;
				}
			}
			if (chunkNumber == -1) {
				long diff = CalendarUtil.getZeroDate(end).getTime()
						- CalendarUtil.getZeroDate(start).getTime();
				diff = diff / (60 * 60 * 1000) % 24;

				if (startChunk.getStartDate2() != null) {
					if (CalendarUtil.getZeroDate(start).before(
							CalendarUtil.getZeroDate(startChunk.getEndDate1()))&&CalendarUtil.getZeroDate(end).after(CalendarUtil.getZeroDate(startChunk.getEndDate1())))
						diff = diff
								- ((CalendarUtil.getZeroDate(
										startChunk.getStartDate2()).getTime() - CalendarUtil
										.getZeroDate(startChunk.getEndDate1())
										.getTime())
										/ (60 * 60 * 1000) % 24);					
				}

				workload = workload + diff;

			} else {
				workload = workload + getRestOfChunk(start, startChunk, true);
				workload = workload + getRestOfChunk(end, endChunk, false);
			}

			return Math.round(workload);
		} else
			return 0;

	}

	@SuppressWarnings("deprecation")
	private Date returnWorkingTime(Date date, ChunkTime workingDate) {

		if (date.getHours() < workingDate.getStartDate1().getHours())
			date.setHours(workingDate.getStartDate1().getHours());
		else {
			if (workingDate.getStartDate2() != null) {
				if (date.getHours() < workingDate.getStartDate2().getHours())
					date.setHours(workingDate.getStartDate2().getHours());
				else
					date = nextWorkingDate(CalendarUtil
							.getZeroTimeDate(new Date(date.getTime()
									+ (1000 * 60 * 60 * 24))));
			} else
				date = nextWorkingDate(CalendarUtil.getZeroTimeDate(new Date(
						date.getTime() + (1000 * 60 * 60 * 24))));

		}

		return date;
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

	private int getWorkingChunkTimeIndex(int i) {

		int j = 0;
		int index = -1;
		while (j < workDays.size()) {
			if (workDays.get(j).getDayNumber() == i) {
				index = j;
				j = workDays.size();
			}
			j++;
		}
		return index;
	}

	private long getRestOfChunk(Date date, ChunkTime Chunk, boolean after) {

		long diff;
		if (after) {
			Date endDate;
			boolean secondSeance;
			if (Chunk.getStartDate2() != null) {
				endDate = Chunk.getEndDate2();
				secondSeance = true;
			} else {
				endDate = Chunk.getEndDate1();
				secondSeance = false;
			}

			diff = CalendarUtil.getZeroDate(endDate).getTime()
					- CalendarUtil.getZeroDate(date).getTime();

			diff = diff / (60 * 60 * 1000) % 24;

			if (secondSeance) {
				if (CalendarUtil.getZeroDate(date).before(
						CalendarUtil.getZeroDate(Chunk.getEndDate1())))
					diff = diff
							- ((CalendarUtil.getZeroDate(Chunk.getStartDate2())
									.getTime() - CalendarUtil.getZeroDate(
									Chunk.getEndDate1()).getTime())
									/ (60 * 60 * 1000) % 24);
			}

		} else {
			diff = CalendarUtil.getZeroDate(date).getTime()
					- CalendarUtil.getZeroDate(Chunk.getStartDate1()).getTime();

			diff = diff / (60 * 60 * 1000) % 24;
			boolean secondSeance = Chunk.getStartDate2() != null;

			if (secondSeance) {
				if (CalendarUtil.getZeroDate(date).after(
						CalendarUtil.getZeroDate(Chunk.getStartDate2())))
					diff = diff
							- ((CalendarUtil.getZeroDate(Chunk.getStartDate2())
									.getTime() - CalendarUtil.getZeroDate(
									Chunk.getEndDate1()).getTime())
									/ (60 * 60 * 1000) % 24);
			}

		}

		return diff;
	}

	@SuppressWarnings("deprecation")
	private boolean isWorkingTime(Date date, ChunkTime chunk) {
		if (chunk.getStartDate1().getHours() <= date.getHours()
				&& chunk.getEndDate1().getHours() > date.getHours())
			return true;
		else if (chunk.getStartDate2() != null) {
			if (chunk.getStartDate2().getHours() <= date.getHours()
					&& chunk.getEndDate2().getHours() > date.getHours())
				return true;
			else
				return false;
		} else
			return false;

	}

	@SuppressWarnings("deprecation")
	private boolean isHoliday(Date date) {
		int i = 0;
		boolean isHoliday = false;
		while (i < calendarUtil.getHolidays().size() && !isHoliday) {
			Date hol = calendarUtil.getHolidays().get(i);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			hol.setYear(calendar.get(Calendar.YEAR));

			isHoliday = (CalendarUtil.getZeroTimeDate(date).compareTo(hol) == 0);
			i++;
		}
		return isHoliday;
	}

	private int isVacation(Date date) {
		int i = -1;
		int j = 0;
		while (j < vacation.size()) {
			if (date.after(vacation.get(j).getStartVacation())
					&& (date.before(vacation.get(j).getEndVacation()))) {
				i = j;
				j = vacation.size();
			}
			j++;
		}
		return i;
	}

}

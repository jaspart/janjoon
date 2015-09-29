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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;

public class ContactCalendarUtil {

	public static String VACATION = "vacation";
	public static String WORK_DAYS = "weekday";
	public static String Planned = "planned";
	public static String Real = "real";
	public static String Revised = "revised";
	private JJContact contact;
	private CalendarUtil calendarUtil;
	private List<ChunkTime> workDays;
	private List<ChunkPeriod> vacation;

	public ContactCalendarUtil(JJContact contact) {

		this.contact = contact;
		this.calendarUtil = new CalendarUtil(contact.getCompany());
		this.workDays = this.calendarUtil.getWorkDays();
		try {
			initVacations();
		} catch (IOException | ParseException e) {
			vacation = new ArrayList<ChunkPeriod>();

			e.printStackTrace();
		}
	}

	public ContactCalendarUtil(JJCompany company) {

		this.calendarUtil = new CalendarUtil(company);
		this.workDays = this.calendarUtil.getWorkDays();
		this.contact = null;
		vacation = new ArrayList<ChunkPeriod>();

	}

	private void initVacations() throws IOException, ParseException {
		workDays = this.calendarUtil.getWorkDays();
		vacation = new ArrayList<ChunkPeriod>();
		if (contact.getCalendar() != null) {
			Properties properties = new Properties();
			properties.load(new StringReader(contact.getCalendar()));

			int j = 0;
			while (j < 7) {
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
							String pattern = "dd/MM/yyyy HH:mm";
							String v = VACATION + "."
									+ String.format("%02d", i);
							String hol = properties.getProperty(v).toString()
									.trim();
							int index = hol.indexOf("to");
							String s = hol.substring(0, index - 1);
							if (s.length() < pattern.length())
								s = s + " 09:00";
							Date date1 = new SimpleDateFormat(
									"dd/MM/yyyy HH:mm").parse(s);

							s = hol.substring(index + 2, hol.length());
							if (s.length() < pattern.length())
								s = s + " 18:00";
							Date date2 = new SimpleDateFormat(
									"dd/MM/yyyy HH:mm").parse(s);
							vacation.add(new ChunkPeriod(date1, date2));
							i++;

						} catch (NullPointerException e) {
							nullPointerException = true;
						}
					}
				}
			}
		}
		if (workDays != null) {
			List<ChunkTime> work = new ArrayList<ChunkTime>();
			int i = 1;
			while (i < 7) {
				work.add(workDays.get(i));
				i++;
			}
			work.add(6, workDays.get(0));
			workDays = work;
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

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String nl = "";
		StringBuilder calendar;
		if (date1.before(date2)) {
			nl = VACATION + "." + String.format("%02d", vacation.size() + 1)
					+ "=" + df.format(date1) + " to " + df.format(date2);

		} else {
			nl = VACATION + "." + String.format("%02d", vacation.size() + 1)
					+ "=" + df.format(date2) + " to " + df.format(date1);
		}
		if (contact.getCalendar() != null) {
			calendar = new StringBuilder(contact.getCalendar());
			calendar.append(System.lineSeparator());
		} else
			calendar = new StringBuilder();

		calendar.append(nl);
		contact.setCalendar(calendar.toString());
	}

	public Date nextWorkingDate(Date date) {

		if ((!isHoliday(date)) && (isVacation(date) == -1)) {

			if (workDays != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				ChunkTime workingDate = getWorkingChunkTime(cal
						.get(Calendar.DAY_OF_WEEK) - 1);

				if (workingDate.isWeekEnd()) {
					return nextWorkingDate(CalendarUtil.getAfterDay(date));
				} else {
					if (isWorkingTime(date, workingDate))
						return date;
					else
						return returnWorkingTime(date, workingDate);
				}
			} else
				return date;

		} else if (isHoliday(date))
			return nextWorkingDate(CalendarUtil.getAfterDay(date));
		else
			return nextWorkingDate(CalendarUtil.getAfterDay(vacation.get(
					isVacation(date)).getEndVacation()));

	}

	public long calculateWorkLoad(Date start, Date end,
			JJTaskService jJTaskService, JJTask task) {

		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		Calendar cal = Calendar.getInstance();
		long workload = 0;
		if (workDays == null) {
			final long Hours_IN_MILLIS = 60 * 60 * 1000;
			if (start.before(end))
				return end.getTime() / Hours_IN_MILLIS - start.getTime()
						/ Hours_IN_MILLIS;
			else
				return 0;

		} else {
			start = nextWorkingDate(start);
			end = nextWorkingDate(end);
			if (start.before(end)) {
				cal.setTime(start);
				ChunkTime startChunk = getWorkingChunkTime(cal
						.get(Calendar.DAY_OF_WEEK) - 1);
				cal.setTime(end);
				ChunkTime endChunk = getWorkingChunkTime(cal
						.get(Calendar.DAY_OF_WEEK) - 1);

				int chunkNumber = (int) -((CalendarUtil.getZeroTimeDate(
						new Date(start.getTime() + (1000 * 60 * 60 * 24)))
						.getTime() - CalendarUtil.getPreviousDate(end)
						.getTime()) / DAY_IN_MILLIS) + 1;

				if (chunkNumber > 0) {
					Date startChunkDate = CalendarUtil.getAfterDay(start);
					cal.setTime(startChunkDate);
					int startChunkInt = cal.get(Calendar.DAY_OF_WEEK) - 1;

					int i = 0;
					while (i < chunkNumber) {
						if (startChunkInt == 7)
							startChunkInt = 0;
						if (!isHoliday(startChunkDate)
								&& (isVacation(startChunkDate) == -1))
							workload = workload
									+ getWorkingChunkTime(startChunkInt)
											.getChunkWorkLoad();

						startChunkDate = CalendarUtil
								.getAfterDay(startChunkDate);
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
								CalendarUtil.getZeroDate(startChunk
										.getEndDate1()))
								&& CalendarUtil.getZeroDate(end).after(
										CalendarUtil.getZeroDate(startChunk
												.getEndDate1())))
							diff = diff
									- ((CalendarUtil.getZeroDate(
											startChunk.getStartDate2())
											.getTime() - CalendarUtil
											.getZeroDate(
													startChunk.getEndDate1())
											.getTime())
											/ (60 * 60 * 1000) % 24);
					}

					workload = workload + diff;

				} else {
					workload = workload
							+ getRestOfChunk(start, startChunk, true);
					workload = workload + getRestOfChunk(end, endChunk, false);
				}

				if (jJTaskService != null && task != null
						&& task.getAssignedTo() != null) {
					task.setStartDateReal(start);
					task.setEndDateReal(end);
					workload = workload
							- calculateSuperimposedWorkload(task, jJTaskService);
				}

				return Math.max(0, workload);
			} else
				return 0;
		}

	}

	public void getStartDate(JJTask task, String champ,
			JJTaskService jJTaskService) {
		if (champ.equalsIgnoreCase(Planned)) {
			int workload = task.getWorkloadPlanned();
			Date end = nextWorkingDate(task.getEndDatePlanned());
			task.setEndDatePlanned(end);
			task.setStartDatePlanned(getStartDate(end, workload, null, null));

		}

		if (champ.equalsIgnoreCase(Revised)) {
			if (task.getWorkloadRevised() == null)
				task.setWorkloadRevised(task.getWorkloadPlanned());
			int workload = task.getWorkloadRevised();
			Date end = nextWorkingDate(task.getEndDateRevised());
			task.setEndDateRevised(end);
			task.setStartDateRevised(getStartDate(end, workload, null, null));
		}

		if (champ.equalsIgnoreCase(Real)) {
			if (task.getWorkloadReal() == null)
				task.setWorkloadReal(task.getWorkloadPlanned());
			int workload = task.getWorkloadReal();
			Date end = nextWorkingDate(task.getEndDateReal());
			task.setEndDateReal(end);
			task.setStartDateReal(getStartDate(end, workload, jJTaskService,
					task));
		}
	}

	private Date getStartDate(Date end, int workload,
			JJTaskService jJTaskService, JJTask task) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);

		Date start = end;
		int kk = workload;
		while (kk > 0) {
			kk = workload;
			start = CalendarUtil.getPreviousDate(start);
			kk = workload
					- Math.round(calculateWorkLoad(nextWorkingDate(start), end,
							jJTaskService, task));
		}
		if (kk < 0) {
			int i = workload
					- Math.round(calculateWorkLoad(start, end, jJTaskService,
							task));
			while (i < 0) {
				cal.setTime(start);
				cal.add(Calendar.HOUR, 1);
				start = cal.getTime();
				i = workload
						- Math.round(calculateWorkLoad(nextWorkingDate(start),
								end, jJTaskService, task));
			}

		}
		return nextWorkingDate(start);
	}

	public void getEndDate(JJTask task, String champ,
			JJTaskService jJTaskService) {

		if (champ.equalsIgnoreCase(Planned)) {
			int workload = task.getWorkloadPlanned();
			Date start = nextWorkingDate(task.getStartDatePlanned());
			task.setStartDatePlanned(start);
			task.setEndDatePlanned(getEndDate(start, workload, null, null));

		}

		if (champ.equalsIgnoreCase(Revised)) {
			if (task.getWorkloadRevised() == null)
				task.setWorkloadRevised(task.getWorkloadPlanned());
			int workload = task.getWorkloadRevised();
			Date start = nextWorkingDate(task.getStartDateRevised());
			task.setStartDateRevised(start);
			task.setEndDateRevised(getEndDate(start, workload, null, null));
		}

		if (champ.equalsIgnoreCase(Real)) {
			if (task.getWorkloadReal() == null)
				task.setWorkloadReal(task.getWorkloadPlanned());
			int workload = task.getWorkloadReal();
			Date start = nextWorkingDate(task.getStartDateReal());
			task.setStartDateReal(start);
			task.setEndDateReal(getEndDate(start, workload, jJTaskService, task));
		}

	}

	private Date getEndDate(Date start, int workload,
			JJTaskService jJTaskService, JJTask task) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		Date end = start;
		int kk = workload;
		while (kk > 0) {
			kk = workload;
			end = nextWorkingDate(CalendarUtil.getAfterDay(end));
			kk = workload
					- Math.round(calculateWorkLoad(start, end, jJTaskService,
							task));
		}
		if (kk < 0) {
			Date endDate = end;
			int repeat = 1;
			while (end.equals(endDate)) {
				int r = 0;
				while (r < repeat) {
					endDate = CalendarUtil.getPreviousDate(endDate);
					r++;
				}
				endDate = nextWorkingDate(endDate);
				repeat++;
			}
			end = endDate;
			int i = workload
					- Math.round(calculateWorkLoad(start, end, jJTaskService,
							task));
			while (i > 0) {
				cal.setTime(end);
				cal.add(Calendar.HOUR_OF_DAY, 1);
				end = nextWorkingDate(cal.getTime());
				i = workload
						- Math.round(calculateWorkLoad(start, end,
								jJTaskService, task));
			}

		}
		return end;
	}

	private Date returnWorkingTime(Date date, ChunkTime workingDate) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(workingDate.getStartDate1());

		if (cal1.get(Calendar.HOUR_OF_DAY) < cal2.get(Calendar.HOUR_OF_DAY)) {
			cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
			date = cal1.getTime();
		} else {
			cal2.setTime(workingDate.getStartDate2());
			if (workingDate.getStartDate2() != null) {
				if (cal1.get(Calendar.HOUR_OF_DAY) < cal2
						.get(Calendar.HOUR_OF_DAY)) {
					cal1.set(Calendar.HOUR_OF_DAY,
							cal2.get(Calendar.HOUR_OF_DAY));
					date = cal1.getTime();
				} else
					date = nextWorkingDate(CalendarUtil.getAfterDay(date));
			} else
				date = nextWorkingDate(CalendarUtil.getAfterDay(date));

		}

		return date;
	}

	private ChunkTime getWorkingChunkTime(int i) {
		ChunkTime c = null;
		for (ChunkTime chunk : workDays) {
			if (chunk != null && chunk.getDayNumber() == i) {
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
		long diff = 0;
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

			if (CalendarUtil.getZeroDate(endDate) != null
					&& CalendarUtil.getZeroDate(date) != null) {
				diff = CalendarUtil.getZeroDate(endDate).getTime()
						- CalendarUtil.getZeroDate(date).getTime();
				diff = diff / (60 * 60 * 1000) % 24;
				if (secondSeance) {
					if (CalendarUtil.getZeroDate(date).before(
							CalendarUtil.getZeroDate(Chunk.getEndDate1()))) {
						diff = diff
								- ((CalendarUtil.getZeroDate(
										Chunk.getStartDate2()).getTime() - CalendarUtil
										.getZeroDate(Chunk.getEndDate1())
										.getTime())
										/ (60 * 60 * 1000) % 24);
					}
				}
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

	private boolean isWorkingTime(Date date, ChunkTime chunk) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(chunk.getStartDate1());
		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(chunk.getEndDate1());

		if (cal2.get(Calendar.HOUR_OF_DAY) <= cal1.get(Calendar.HOUR_OF_DAY)
				&& cal3.get(Calendar.HOUR_OF_DAY) > cal1
						.get(Calendar.HOUR_OF_DAY))
			return true;
		else if (chunk.getStartDate2() != null) {
			cal2.setTime(chunk.getStartDate2());
			cal3.setTime(chunk.getEndDate2());
			if (cal2.get(Calendar.HOUR_OF_DAY) <= cal1
					.get(Calendar.HOUR_OF_DAY)
					&& cal3.get(Calendar.HOUR_OF_DAY) > cal1
							.get(Calendar.HOUR_OF_DAY))
				return true;
			else
				return false;
		} else
			return false;

	}

	public boolean isHoliday(Date date) {
		int i = 0;
		boolean isHoliday = false;
		while (i < calendarUtil.getHolidays().size() && !isHoliday) {
			Date hol = calendarUtil.getHolidays().get(i);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Calendar cal = Calendar.getInstance();
			calendar.setTime(hol);
			cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

			isHoliday = (CalendarUtil.getZeroTimeDate(calendar.getTime())
					.compareTo(cal.getTime()) == 0);
			i++;
		}
		return isHoliday;
	}

	public int getNumberOfWorkingDay(Date start, Date end) {
		if (start.before(end)) {
			int i = 0;
			start = CalendarUtil.getZeroTimeDate(start);
			end = CalendarUtil.getZeroTimeDate(end);
			while (start.before(end)) {
				if (!isHoliday(start) && !isWeekEnd(start))
					i++;
				start = CalendarUtil.getAfterDay(start);

			}
			if (!isHoliday(end) && !isWeekEnd(end))
				i++;
			return i;
		} else
			return 0;
	}

	public boolean isWeekEnd(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (getWorkingChunkTime(i).isWeekEnd())
			return true;
		else
			return false;
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

	public long calculateSuperimposedWorkload(JJTask task,
			JJTaskService jJTaskService) {

		List<JJTask> tasks = jJTaskService.getSuperimposeTasks(
				task.getAssignedTo(), task.getStartDateReal(),
				task.getEndDateReal(), task);
		if (tasks != null && !tasks.isEmpty()) {
			List<SuperimposedShunkTime> list = new ArrayList<SuperimposedShunkTime>();
			int i = 0;
			List<Date> dates = new ArrayList<Date>();

			dates.add(task.getStartDateReal());
			dates.add(task.getEndDateReal());
			for (JJTask t : tasks) {
				if (t.getStartDateReal() != null
						&& t.getStartDateReal().after(task.getStartDateReal()))
					dates.add(t.getStartDateReal());
				if (t.getEndDateReal() != null
						&& t.getEndDateReal().before(task.getEndDateReal()))
					dates.add(t.getEndDateReal());
			}

			dates = new ArrayList<Date>(new HashSet<Date>(dates));
			Collections.sort(dates, new Comparator<Date>() {
				@Override
				public int compare(Date o1, Date o2) {
					return o1.compareTo(o2);

				}
			});

			// int pt = 0;
			// int tpt = 0;

			while (i < dates.size() - 1) {

				// tpt = tpt
				// + jJTaskService.getSuperimposeTasks(
				// task.getAssignedTo(), dates.get(i),
				// dates.get(i + 1), task).size();

				List<JJTask> periodPasks = getPeriodTasks(tasks, dates.get(i),
						dates.get(i + 1));
				// pt = pt + periodPasks.size();
				// System.out.println(pt + "/" + tpt);
				if (periodPasks != null && !periodPasks.isEmpty()
						&& periodPasks.size() > 0) {
					SuperimposedShunkTime element = new SuperimposedShunkTime(
							dates.get(i), dates.get(i + 1), periodPasks.size());
					element.setWorkload(calculateWorkLoad(dates.get(i),
							dates.get(i + 1), null, null));
					list.add(element);
				}

				i++;
			}
			long l = 0;

			for (SuperimposedShunkTime elt : list) {
				System.err.println((elt.getNbTask() + "/"
						+ (elt.getNbTask() + 1) + "*" + elt.getWorkload()));
				l = l
						+ (elt.getNbTask() * elt.getWorkload() / (elt
								.getNbTask() + 1));
			}

			return l;
		} else
			return 0;

	}

	public List<JJTask> getPeriodTasks(List<JJTask> tasks, Date startDate,
			Date endDate) {
		List<JJTask> periodTasks = new ArrayList<JJTask>();

		for (JJTask t : tasks) {

			if (((t.getStartDateReal().compareTo(startDate) >= 0) && t
					.getStartDateReal().before(endDate))
					|| (t.getEndDateReal() != null && (t.getEndDateReal()
							.after(startDate) && (t.getEndDateReal().compareTo(
							endDate) <= 0)))
					|| (t.getStartDateReal().before(startDate) && t
							.getEndDateReal() == null)
					|| (t.getStartDateReal().before(startDate) && t
							.getEndDateReal().after(endDate))
					&& ((!t.getStartDateReal().equals(endDate)) && (!t
							.getEndDateReal().equals(startDate))))
				periodTasks.add(t);

		}
		return periodTasks;
	}
	
	public String editWorkday(ChunkTime day) throws IOException {
		Properties properties = new Properties();
		String calendar = contact.getCalendar();
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


	public class SuperimposedShunkTime {

		Date startDate;
		Date endDate;
		int nbTask;
		long workload;

		public SuperimposedShunkTime(Date startDate, Date endDate, int nbTask) {

			this.startDate = startDate;
			this.endDate = endDate;
			this.nbTask = nbTask;
			this.workload = 0;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public int getNbTask() {
			return nbTask;
		}

		public void setNbTask(int nbTask) {
			this.nbTask = nbTask;
		}

		public long getWorkload() {
			return workload;
		}

		public void setWorkload(long workload) {
			this.workload = workload;
		}

	}

}

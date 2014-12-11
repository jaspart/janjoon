package com.starit.janjoonweb.ui.mb.util;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;

public class ChunkTime {

	int dayNumber;
	String day;
	Date startDate1;
	Date endDate1;
	Date startDate2;
	Date endDate2;
	long chunkWorkLoad;
	
	public ChunkTime() {
		
	}
	
	public ChunkTime(int i) {
		
		
		this.dayNumber = i;
		this.day=capitalizeDay(dayNumber);
		this.startDate1 = null;
		this.endDate1 = null;
		this.startDate2 = null;
		this.endDate2 = null;
		this.chunkWorkLoad=0;
	}

	public ChunkTime(int dayNumber, Date startDate1, Date endDate1,
			Date startDate2, Date endDate2) {
		super();
		this.dayNumber = dayNumber;
		this.startDate1 = CalendarUtil.getZeroDate(startDate1);
		this.endDate1 = CalendarUtil.getZeroDate(endDate1);
		this.startDate2 = CalendarUtil.getZeroDate(startDate2);
		this.endDate2 = CalendarUtil.getZeroDate(endDate2);
		long l;
		if(startDate2 != null)
			l=endDate2.getTime()-startDate2.getTime();
		else 
			l=0;
		this.chunkWorkLoad=((endDate1.getTime()-startDate1.getTime())+(l))/(60 * 60 * 1000) % 24;		
	    this.day=capitalizeDay(dayNumber);
		
	}

	public int getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(int dayNumber) {
		
		this.dayNumber = dayNumber;		
	    this.day=capitalizeDay(dayNumber);
	}
	public Date getStartDate1() {
		return startDate1;
	}
	public void setStartDate1(Date startDate1) {
		this.startDate1 = startDate1;
	}
	public Date getEndDate1() {
		return endDate1;
	}
	public void setEndDate1(Date endDate1) {
		this.endDate1 = endDate1;
	}
	public Date getStartDate2() {
		return startDate2;
	}
	public void setStartDate2(Date startDate2) {
		this.startDate2 = startDate2;
	}
	public Date getEndDate2() {
		return endDate2;
	}
	public void setEndDate2(Date endDate2) {
		this.endDate2 = endDate2;
	}
	
	public long getChunkWorkLoad() {
		return chunkWorkLoad;
	}

	public void setChunkWorkLoad(long chunkWorkLoad) {
		this.chunkWorkLoad = chunkWorkLoad;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public boolean isWeekEnd()
	{
		return startDate1==null && startDate2==null;
	}
	
	public String capitalizeDay(int i)
	{
		DateFormatSymbols dfs = new DateFormatSymbols(FacesContext.getCurrentInstance().getExternalContext()
				.getRequestLocale());
	    String weekdays[] = dfs.getWeekdays();
	    String s=weekdays[i+1];
	    
	    return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
}

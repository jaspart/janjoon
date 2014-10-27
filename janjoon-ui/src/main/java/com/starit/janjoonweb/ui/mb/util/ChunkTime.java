package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

public class ChunkTime {

	int dayNumber;
	
	Date startDate1;
	Date endDate1;
	Date startDate2;
	Date endDate2;
	long chunkWorkLoad;
	
	public ChunkTime() {
		
	}
	
	public ChunkTime(int i) {
		
		this.dayNumber = i;
		this.startDate1 = null;
		this.endDate1 = null;
		this.startDate2 = null;
		this.endDate2 = null;
		this.chunkWorkLoad=0;
	}

	@SuppressWarnings("deprecation")
	public ChunkTime(int dayNumber, Date startDate1, Date endDate1,
			Date startDate2, Date endDate2) {
		super();
		this.dayNumber = dayNumber;
		this.startDate1 = CalendarUtil.getZeroDate(startDate1);
		this.endDate1 = CalendarUtil.getZeroDate(endDate1);
		this.startDate2 = CalendarUtil.getZeroDate(startDate2);
		this.endDate2 = CalendarUtil.getZeroDate(endDate2);
		this.chunkWorkLoad=((endDate1.getTime()-startDate1.getTime())+(endDate2.getTime()-startDate2.getTime()))/(60 * 60 * 1000) % 24;
	}

	public int getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
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

	public boolean isWeekEnd()
	{
		return startDate1==null && startDate2==null;
	}
	
}

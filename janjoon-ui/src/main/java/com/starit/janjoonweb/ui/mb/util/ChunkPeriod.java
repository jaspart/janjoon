package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

public class ChunkPeriod {

	private Date startVacation;
	private Date endVacation;	
	
	public ChunkPeriod(Date startVacation, Date endVacation) {
		
		if(startVacation.before(endVacation))
		{
			this.startVacation = CalendarUtil.getZeroTimeDate(startVacation);
			this.endVacation = CalendarUtil.getZeroTimeDate(endVacation);	
		}else
		{
			this.startVacation = CalendarUtil.getZeroTimeDate(endVacation);
			this.endVacation = CalendarUtil.getZeroTimeDate(startVacation);	
		}
			
	}
	
	public Date getStartVacation() {
		return startVacation;
	}
	public void setStartVacation(Date startVacation) {
		this.startVacation = startVacation;
	}
	public Date getEndVacation() {
		return endVacation;
	}
	public void setEndVacation(Date endVacation) {
		this.endVacation = endVacation;
	}
	
	
}

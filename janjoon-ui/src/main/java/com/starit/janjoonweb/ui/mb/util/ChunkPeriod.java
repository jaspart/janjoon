package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

public class ChunkPeriod {

	private Date	startVacation;
	private Date	endVacation;

	public ChunkPeriod(Date startVacation, Date endVacation) {

		if (startVacation.before(endVacation)) {
			this.startVacation = startVacation;
			this.endVacation = endVacation;
		} else {
			this.startVacation = endVacation;
			this.endVacation = startVacation;
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

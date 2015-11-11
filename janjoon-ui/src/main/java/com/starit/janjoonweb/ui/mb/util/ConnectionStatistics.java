package com.starit.janjoonweb.ui.mb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJContact;

public class ConnectionStatistics {

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");
	public static String LOGIN_OBJECT = "login_date";
	public static String LOGOUT_OBJECT = "logout_date";
	private final long MINUTE_IN_MILLIS = 60 * 1000;
	private long id;
	private JJContact contact;
	private Date loginDate;
	private Date logoutDate;
	private Long duration;

	public ConnectionStatistics(JJAuditLog loginAuditLog,
			JJAuditLog logoutAuditLog) {

		try {
			id = loginAuditLog.getId();
			contact = loginAuditLog.getContact();
			loginDate = formatter.parse(loginAuditLog.getKeyValue());

		} catch (ParseException e) {
			this.contact = null;
			this.loginDate = null;
		}

		if (contact != null) {
			if (logoutAuditLog != null) {
				try {
					logoutDate = formatter.parse(logoutAuditLog.getKeyValue());
					this.setDuration(new Long(Math.round(this.getLogoutDate()
							.getTime()
							/ MINUTE_IN_MILLIS
							- this.getLoginDate().getTime() / MINUTE_IN_MILLIS)) + 1);

				} catch (ParseException e) {
					this.logoutDate = null;
					this.duration = null;
				}
			} else {
				this.logoutDate = null;
				this.duration = null;
			}

		}

	}

	public static SimpleDateFormat getFormatter() {
		return formatter;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

}

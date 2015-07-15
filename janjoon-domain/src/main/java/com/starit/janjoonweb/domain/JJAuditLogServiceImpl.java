package com.starit.janjoonweb.domain;

public class JJAuditLogServiceImpl implements JJAuditLogService {

	public void saveJJAuditLog(JJAuditLog JJAuditLog_) {
		jJAuditLogRepository.save(JJAuditLog_);
		JJAuditLog_ = jJAuditLogRepository.findOne(JJAuditLog_.getId());
	}

	public JJAuditLog updateJJAuditLog(JJAuditLog JJAuditLog_) {
		jJAuditLogRepository.save(JJAuditLog_);
		JJAuditLog_ = jJAuditLogRepository.findOne(JJAuditLog_.getId());
		return JJAuditLog_;
	}
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJAuditLogRepository;
import com.starit.janjoonweb.domain.JJAuditLogServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJAuditLogServiceImpl_Roo_Service {
    
    declare @type: JJAuditLogServiceImpl: @Service;
    
    declare @type: JJAuditLogServiceImpl: @Transactional;
    
    @Autowired
    JJAuditLogRepository JJAuditLogServiceImpl.jJAuditLogRepository;
    
    public long JJAuditLogServiceImpl.countAllJJAuditLogs() {
        return jJAuditLogRepository.count();
    }
    
    public void JJAuditLogServiceImpl.deleteJJAuditLog(JJAuditLog JJAuditLog_) {
        jJAuditLogRepository.delete(JJAuditLog_);
    }
    
    public JJAuditLog JJAuditLogServiceImpl.findJJAuditLog(Long id) {
        return jJAuditLogRepository.findOne(id);
    }
    
    public List<JJAuditLog> JJAuditLogServiceImpl.findAllJJAuditLogs() {
        return jJAuditLogRepository.findAll();
    }
    
    public List<JJAuditLog> JJAuditLogServiceImpl.findJJAuditLogEntries(int firstResult, int maxResults) {
        return jJAuditLogRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
}

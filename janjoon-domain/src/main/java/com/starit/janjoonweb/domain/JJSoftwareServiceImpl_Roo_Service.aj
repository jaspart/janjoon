// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJSoftware;
import com.starit.janjoonweb.domain.JJSoftwareRepository;
import com.starit.janjoonweb.domain.JJSoftwareServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJSoftwareServiceImpl_Roo_Service {
    
    declare @type: JJSoftwareServiceImpl: @Service;
    
    declare @type: JJSoftwareServiceImpl: @Transactional;
    
    @Autowired
    JJSoftwareRepository JJSoftwareServiceImpl.jJSoftwareRepository;
    
    public long JJSoftwareServiceImpl.countAllJJSoftwares() {
        return jJSoftwareRepository.count();
    }
    
    public void JJSoftwareServiceImpl.deleteJJSoftware(JJSoftware JJSoftware_) {
        jJSoftwareRepository.delete(JJSoftware_);
    }
    
    public JJSoftware JJSoftwareServiceImpl.findJJSoftware(Long id) {
        return jJSoftwareRepository.findOne(id);
    }
    
    public List<JJSoftware> JJSoftwareServiceImpl.findAllJJSoftwares() {
        return jJSoftwareRepository.findAll();
    }
    
    public List<JJSoftware> JJSoftwareServiceImpl.findJJSoftwareEntries(int firstResult, int maxResults) {
        return jJSoftwareRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
}

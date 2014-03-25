// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyRepository;
import com.starit.janjoonweb.domain.JJCompanyServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJCompanyServiceImpl_Roo_Service {
    
    declare @type: JJCompanyServiceImpl: @Service;
    
    declare @type: JJCompanyServiceImpl: @Transactional;
    
    @Autowired
    JJCompanyRepository JJCompanyServiceImpl.jJCompanyRepository;
    
    public long JJCompanyServiceImpl.countAllJJCompanys() {
        return jJCompanyRepository.count();
    }
    
    public void JJCompanyServiceImpl.deleteJJCompany(JJCompany JJCompany_) {
        jJCompanyRepository.delete(JJCompany_);
    }
    
    public JJCompany JJCompanyServiceImpl.findJJCompany(Long id) {
        return jJCompanyRepository.findOne(id);
    }
    
    public List<JJCompany> JJCompanyServiceImpl.findAllJJCompanys() {
        return jJCompanyRepository.findAll();
    }
    
    public List<JJCompany> JJCompanyServiceImpl.findJJCompanyEntries(int firstResult, int maxResults) {
        return jJCompanyRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJCompanyServiceImpl.saveJJCompany(JJCompany JJCompany_) {
        jJCompanyRepository.save(JJCompany_);
    }
    
    public JJCompany JJCompanyServiceImpl.updateJJCompany(JJCompany JJCompany_) {
        return jJCompanyRepository.save(JJCompany_);
    }
    
}

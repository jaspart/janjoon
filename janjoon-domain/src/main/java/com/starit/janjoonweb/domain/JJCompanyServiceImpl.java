package com.starit.janjoonweb.domain;

public class JJCompanyServiceImpl implements JJCompanyService {
	
	 public void saveJJCompany(JJCompany JJCompany_) {
	        jJCompanyRepository.save(JJCompany_);
	        JJCompany_=jJCompanyRepository.findOne(JJCompany_.getId());
	    }
	    
	    public JJCompany updateJJCompany(JJCompany JJCompany_) {
	    	
	        jJCompanyRepository.save(JJCompany_);
	        JJCompany_=jJCompanyRepository.findOne(JJCompany_.getId());
	        return JJCompany_;
	    }
}

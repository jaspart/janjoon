package com.starit.janjoonweb.domain;

public class JJWorflowServiceImpl implements JJWorflowService {
	
public void saveJJWorflow(JJWorflow JJWorflow_) {	
		
        jJWorkflowRepository.save(JJWorflow_);
        JJWorflow_=jJWorkflowRepository.findOne(JJWorflow_.getId());
    }
    
    public JJWorflow updateJJWorflow(JJWorflow JJWorflow_) {
    	jJWorkflowRepository.save(JJWorflow_);
        JJWorflow_=jJWorkflowRepository.findOne(JJWorflow_.getId());
        return JJWorflow_;
    }
}

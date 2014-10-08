package com.starit.janjoonweb.domain;

public class JJWorkflowServiceImpl implements JJWorkflowService {
	
public void saveJJWorflow(JJWorkflow JJWorflow_) {	
		
        jJWorkflowRepository.save(JJWorflow_);
        JJWorflow_=jJWorkflowRepository.findOne(JJWorflow_.getId());
    }
    
    public JJWorkflow updateJJWorflow(JJWorkflow JJWorflow_) {
    	jJWorkflowRepository.save(JJWorflow_);
        JJWorflow_=jJWorkflowRepository.findOne(JJWorflow_.getId());
        return JJWorflow_;
    }
}

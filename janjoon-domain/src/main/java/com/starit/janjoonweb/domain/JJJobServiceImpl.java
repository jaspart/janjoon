package com.starit.janjoonweb.domain;

public class JJJobServiceImpl implements JJJobService {
	
public void saveJJJob(JJJob JJJob_) {
		
        jJJobRepository.save(JJJob_);
        JJJob_=jJJobRepository.findOne(JJJob_.getId());
    }
    
    public JJJob updateJJJob(JJJob JJJob_) {
        jJJobRepository.save(JJJob_);
        JJJob_=jJJobRepository.findOne(JJJob_.getId());
        return JJJob_;
    }
}

package com.starit.janjoonweb.domain;

public class JJImputationServiceImpl implements JJImputationService {
	
	public void saveJJImputation(JJImputation JJImputation_) {

		jJImputationRepository.save(JJImputation_);
		JJImputation_ = jJImputationRepository.findOne(JJImputation_.getId());
	}

	public JJImputation updateJJImputation(JJImputation JJImputation_) {
		jJImputationRepository.save(JJImputation_);
		JJImputation_ = jJImputationRepository.findOne(JJImputation_.getId());
		return JJImputation_;
	}
}

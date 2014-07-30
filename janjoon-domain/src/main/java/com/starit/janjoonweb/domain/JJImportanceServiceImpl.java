package com.starit.janjoonweb.domain;

public class JJImportanceServiceImpl implements JJImportanceService {

	public void saveJJImportance(JJImportance JJImportance_) {

		jJImportanceRepository.save(JJImportance_);
		JJImportance_ = jJImportanceRepository.findOne(JJImportance_.getId());
	}

	public JJImportance updateJJImportance(JJImportance JJImportance_) {
		jJImportanceRepository.save(JJImportance_);
		JJImportance_ = jJImportanceRepository.findOne(JJImportance_.getId());
		return JJImportance_;
	}
}

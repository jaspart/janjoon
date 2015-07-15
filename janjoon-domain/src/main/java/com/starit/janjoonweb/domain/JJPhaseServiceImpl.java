package com.starit.janjoonweb.domain;

public class JJPhaseServiceImpl implements JJPhaseService {

	public void saveJJPhase(JJPhase JJPhase_) {

		jJPhaseRepository.save(JJPhase_);
		JJPhase_ = jJPhaseRepository.findOne(JJPhase_.getId());
	}

	public JJPhase updateJJPhase(JJPhase JJPhase_) {
		jJPhaseRepository.save(JJPhase_);
		JJPhase_ = jJPhaseRepository.findOne(JJPhase_.getId());
		return JJPhase_;
	}
}

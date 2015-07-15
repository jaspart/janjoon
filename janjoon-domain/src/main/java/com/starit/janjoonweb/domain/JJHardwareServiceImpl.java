package com.starit.janjoonweb.domain;

public class JJHardwareServiceImpl implements JJHardwareService {

	public void saveJJHardware(JJHardware JJHardware_) {

		jJHardwareRepository.save(JJHardware_);
		JJHardware_ = jJHardwareRepository.findOne(JJHardware_.getId());
	}

	public JJHardware updateJJHardware(JJHardware JJHardware_) {
		jJHardwareRepository.save(JJHardware_);
		JJHardware_ = jJHardwareRepository.findOne(JJHardware_.getId());
		return JJHardware_;
	}
}

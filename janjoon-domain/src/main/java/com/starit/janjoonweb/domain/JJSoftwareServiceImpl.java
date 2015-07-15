package com.starit.janjoonweb.domain;

public class JJSoftwareServiceImpl implements JJSoftwareService {

	public void saveJJSoftware(JJSoftware JJSoftware_) {

		jJSoftwareRepository.save(JJSoftware_);
		JJSoftware_ = jJSoftwareRepository.findOne(JJSoftware_.getId());
	}

	public JJSoftware updateJJSoftware(JJSoftware JJSoftware_) {
		jJSoftwareRepository.save(JJSoftware_);
		JJSoftware_ = jJSoftwareRepository.findOne(JJSoftware_.getId());
		return JJSoftware_;
	}
}

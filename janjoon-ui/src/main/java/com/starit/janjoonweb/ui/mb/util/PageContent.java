package com.starit.janjoonweb.ui.mb.util;

import java.util.List;

public class PageContent {

	String			page;
	List<String>	objects;

	public PageContent(String page, List<String> list) {

		this.page = page;
		this.objects = list;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public List<String> getObjects() {
		return objects;
	}

	public void setObjects(List<String> objects) {
		this.objects = objects;
	}

}

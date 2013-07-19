package com.funder.janjoonweb.ui.mb;

import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJChapter;

@RooSerializable
@RooJsfManagedBean(entity = JJChapter.class, beanName = "jJChapterBean")
public class JJChapterBean {

	private JJChapter myJJChapter;
	private List<JJChapter> myJJChapterList;

	public JJChapter getMyJJChapter() {
		return myJJChapter;
	}

	public void setMyJJChapter(JJChapter myJJChapter) {
		this.myJJChapter = myJJChapter;
	}

	public List<JJChapter> getMyJJChapterList() {
		myJJChapterList = jJChapterService.getAllJJChapter();
		return myJJChapterList;
	}

	public void setMyJJChapterList(List<JJChapter> myJJChapterList) {
		this.myJJChapterList = myJJChapterList;
	}
}

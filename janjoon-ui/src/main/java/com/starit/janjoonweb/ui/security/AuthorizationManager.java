package com.starit.janjoonweb.ui.security;

import java.io.Serializable;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.ui.mb.util.PageContent;

@Component("authorizationManager")
public class AuthorizationManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	JJContactService jJContactService;

	JJContact contact;
	List<JJRight> contactRight;

	public static List<PageContent> pageContents = new ArrayList<PageContent>() {
		{
			List<String> objects = new ArrayList<String>() {

				{
					add("JJBuild");
				}

			};
			add(new PageContent("development", objects));

			objects = new ArrayList<String>() {
				{
					add("JJRequirement");
				}

			};
			add(new PageContent("specifications", objects));

			objects = new ArrayList<String>() {
				{
					add("JJSprint");
					add("JJTask");
				}

			};
			add(new PageContent("project1", objects));
		}
	};

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public List<JJRight> getContactRight() {
		return contactRight;
	}

	public void setContactRight(List<JJRight> contactRight) {
		this.contactRight = contactRight;
	}

	public AuthorizationManager() {

	}

	public boolean getAuthorization(String page, JJProject project,
			JJProduct product) {
		int i = contain(page);
		if (i == -1)
			return true;
		else {

			contactRight = jJContactService.getContactAuthorization(
					pageContents.get(i).getObjects().get(0), contact, product,
					project, null);

			if (contactRight.isEmpty()) {
				
				return true;
			} else {
				boolean result = false;

				for (JJRight right : contactRight) {

					result = right.getW() || result;
				}

				return result;
			}
		}

	}

	public int contain(String page) {
		int i = -1;

		for (PageContent pages : pageContents) {
			if (page.contains(pages.getPage())) {
				i = pageContents.indexOf(pages);
				break;
			}
		}

		return i;
	}

}

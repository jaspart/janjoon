package com.starit.janjoonweb.ui.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.util.PageContent;

@Component("authorizationManager")
public class AuthorizationManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	JJContact contact;

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

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public AuthorizationManager() {

	}

	public boolean getAuthorization(String page, JJProject project,
			JJProduct product) {
		int i = contain(page);
		if (i == -1)
			return true;
		else {

			return jJPermissionService.isAuthorized(contact, project, product,
					pageContents.get(i).getObjects().get(0));

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

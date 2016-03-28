package com.starit.janjoonweb.domain.reference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.starit.janjoonweb.domain.JJContact;

public class Contacts {

	private JJContact contact1;
	private JJContact contact2;

	private JJContact contact3;
	private JJContact contact4;

	public Contacts() {
	}

	public Contacts(JJContact contact1, JJContact contact2, JJContact contact3,
			JJContact contact4) {
		super();
		this.contact1 = contact1;
		this.contact2 = contact2;
		this.contact3 = contact3;
		this.contact4 = contact4;
	}

	public JJContact getContact1() {
		return contact1;
	}
	public void setContact1(JJContact contact1) {
		this.contact1 = contact1;
	}
	public JJContact getContact2() {
		return contact2;
	}
	public void setContact2(JJContact contact2) {
		this.contact2 = contact2;
	}

	public JJContact getContact3() {
		return contact3;
	}

	public void setContact3(JJContact contact3) {
		this.contact3 = contact3;
	}

	public JJContact getContact4() {
		return contact4;
	}

	public void setContact4(JJContact contact4) {
		this.contact4 = contact4;
	}

	public static List<JJContact> getJJContacts(List<Contacts> contacts) {
		Set<JJContact> cts = new HashSet<JJContact>();
		for (Contacts c : contacts) {
			if (c.getContact1() != null)
				cts.add(c.getContact1());
			if (c.getContact2() != null)
				cts.add(c.getContact2());
			if (c.getContact3() != null)
				cts.add(c.getContact3());
			if (c.getContact4() != null)
				cts.add(c.getContact4());

		}

		return new ArrayList<JJContact>(cts);
	}

}

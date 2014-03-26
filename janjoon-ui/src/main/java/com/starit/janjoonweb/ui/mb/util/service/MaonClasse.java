package com.starit.janjoonweb.ui.mb.util.service;

import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJContact;

public class MaonClasse {

	public static void main(String[] args)  {

		GitConfigManager ggg=new GitConfigManager("https://github.com/janjoon/ProductName-1.git", "/home/lazher/git/.git/Repository/ProductName 1");
		TreeNode  t=ggg.listRepositoryContent("integ/14.1");
		System.out.println(t);
		JJContact contact=new JJContact();
		contact.setName("janjoon");
		contact.setPassword("BeHappy2012");
		ggg.setjJContact(contact);
		ggg.checkIn("fjgnkl,");
		ggg.pushRepository();
	}
}

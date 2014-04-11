package com.starit.janjoonweb.ui.mb.util.service;

import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJContact;

public class MaonClasse {

	public static void main(String[] args)  {

		GitConfigManager ggg=new GitConfigManager("https://github.com/janjoon/ProductName-1.git", "/home/lazher/git/ProductName 1");
		
		JJContact contact=new JJContact();
		contact.setName("janjoon");
		contact.setPassword("BeHappy2012");
		ggg.setjJContact(contact);
		ggg.addFile("/main/opooooo", "rrrrrrrrrrrrr",false);
	}
}

package com.starit.janjoonweb.ui.mb.util.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TestClass {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		Properties properties = new Properties();
		String path = TestClass.class.getResource(
				"/com/starit/janjoonweb/ui/mb/i18n/messages_fr.properties")
				.getFile();
		properties.load(new FileInputStream(path));
		List<Object> list1 = Collections.list(properties.keys());
		

		Properties prop = new Properties();
		String pa = TestClass.class.getResource(
				"/com/starit/janjoonweb/ui/mb/i18n/messages_en.properties")
				.getFile();
		prop.load(new FileInputStream(pa));
		List<Object> list2 = Collections.list(prop.keys());
		
		for (Object ob:list1) {
			
			if(!list2.contains(ob))
				System.out.println(ob);
			
		}
		
		

		

	}
}

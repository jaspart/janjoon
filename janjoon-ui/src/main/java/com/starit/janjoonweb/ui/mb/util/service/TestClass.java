package com.starit.janjoonweb.ui.mb.util.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestClass {

	// String path = TestClass.class
	// .getResource(
	// "/com/starit/janjoonweb/ui/mb/i18n/messages_fr.properties")
	// .getFile();

	public static void main(String[] args) {

		TestClass obj = new TestClass();
		obj.run();

	}

	public void run() {		
		String csvFile = TestClass.class.getResource("/mcc-mnc-table.csv")
				.getFile();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			Map<String, String> maps = new HashMap<String, String>();

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country = line.split(cvsSplitBy);

				maps.put(country[4], country[5]);

			}

			// loop map
			for (Map.Entry<String, String> entry : maps.entrySet()) {

				System.out.println("Country [code= " + entry.getKey()
						+ " , name=" + entry.getValue() + "]");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

}

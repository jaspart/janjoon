package com.starit.janjoonweb.ui.mb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opencsv.CSVReader;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.JJTestcaseBean;

public class ReadXMLFile {

	public static List<JJRequirement> getRequirementsFromCSV(InputStream file,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJCategory category) {
		List<JJRequirement> requirements = new ArrayList<JJRequirement>();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			byte[] buffer = new byte[1024];
			int len;
			while ((len = file.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			CSVReader reader = new CSVReader(
					new InputStreamReader(
							new ByteArrayInputStream(baos.toByteArray())),
					',', '\"', 1);
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {

				if (nextLine.length > 6 && nextLine[6] != null) {
					System.out.println(
							nextLine[6] + " " + nextLine[22] + " etc...");
					JJRequirement requirement = new JJRequirement();

					requirement.setName(nextLine[6]);
					requirement.setDescription(
							nextLine.length > 22 && nextLine[22] != null
									? nextLine[22]
									: nextLine[6]);
					requirement.setEnabled(true);
					requirement.setProject(project);
					requirement.setCategory(category);
					requirement.setProduct(product);
					requirement.setVersioning(version);
					requirement.setStatus(status);
					requirements.add(requirement);
				}

			}
			if (requirements.isEmpty()) {
				reader = new CSVReader(
						new InputStreamReader(
								new ByteArrayInputStream(baos.toByteArray())),
						';', '\"', 1);

				while ((nextLine = reader.readNext()) != null) {

					if (nextLine.length > 6 && nextLine[6] != null) {
						System.out.println(
								nextLine[6] + " " + nextLine[nextLine.length-1] + " etc...");
						JJRequirement requirement = new JJRequirement();

						requirement.setName(nextLine[6]);
						requirement.setDescription(nextLine[nextLine.length-1]);
						requirement.setEnabled(true);
						requirement.setProject(project);
						requirement.setCategory(category);
						requirement.setProduct(product);
						requirement.setVersioning(version);
						requirement.setStatus(status);
						requirements.add(requirement);
					}

				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return requirements;
	}
	public static List<JJRequirement> getRequirementsFromXml(InputStream file,
			JJCategoryService jJCategoryService,
			JJRequirementService jJRequirementService,
			JJChapterService jJChapterService, JJProject project,
			JJCompany company, JJProduct product, JJVersion version,
			JJStatus status) throws SAXParseException {
		List<JJRequirement> requirements = new ArrayList<JJRequirement>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println(
					"Root element :" + doc.getDocumentElement().getNodeName());
			JJCategory category = jJCategoryService.getCategory(
					doc.getDocumentElement().getAttribute("name"), company,
					true);

			NodeList nList = doc.getElementsByTagName("requirement");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					JJRequirement requirement = new JJRequirement();
					JJChapter chapter = jJChapterService.getChapterByName(
							category, eElement.getAttribute("chapter"), project,
							company);
					requirement.setName(eElement.getAttribute("name"));
					requirement.setDescription(
							eElement.getAttribute("description"));
					requirement.setEnabled(
							!eElement.getAttribute("enabled").equals("0"));
					requirement.setNote(eElement.getAttribute("note"));
					requirement.setChapter(chapter);
					requirement.setProject(project);
					requirement.setCategory(category);
					requirement.setProduct(product);
					requirement.setVersioning(version);
					requirement.setStatus(status);
					SortedMap<Integer, Object> elements = JJRequirementBean
							.getSortedElements(chapter, project, category, true,
									jJChapterService, jJRequirementService);
					requirement.setOrdering(elements.lastKey() + 1);
					requirements.add(requirement);

					System.out
							.println("name : " + eElement.getAttribute("name"));
					System.out.println("description : "
							+ eElement.getAttribute("description"));
					System.out.println(
							"enabled : " + eElement.getAttribute("enabled"));
					System.out
							.println("note : " + eElement.getAttribute("note"));
					System.out.println(
							"chapter : " + eElement.getAttribute("chapter"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requirements;
	}

	public static List<Object> getTestcasesFromXml(JJTestcaseBean testCaseBean,
			InputStream stream, JJTestcaseService jjTestcaseService,
			JJCategoryService jJCategoryService,
			JJRequirementService jJRequirementService, JJProject project,
			JJProduct product, JJCompany company) throws SAXParseException {

		List<JJTestcase> testests = new ArrayList<JJTestcase>();
		List<JJTeststep> teststeps = new ArrayList<JJTeststep>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();

			System.out.println(
					"Root element :" + doc.getDocumentElement().getNodeName());
			JJCategory category = jJCategoryService.getCategory(
					doc.getDocumentElement().getAttribute("name"), company,
					true);

			NodeList nList = doc.getElementsByTagName("testcase");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					JJRequirement requirement = jJRequirementService
							.getRequirementByName(category, project, product,
									eElement.getAttribute("Requirement"), null);
					if (requirement != null) {
						JJTestcase testcase = new JJTestcase();
						testcase.setName(eElement.getAttribute("name"));
						testcase.setDescription(
								eElement.getAttribute("description"));
						testcase.setRequirement(requirement);
						testcase.setEnabled(!eElement.getAttribute("enabled")
								.equalsIgnoreCase("0"));
						testcase.setAutomatic(
								!eElement.getAttribute("Automatic")
										.equalsIgnoreCase("0"));
						testcase.setOrdering(
								jjTestcaseService.getMaxOrdering(requirement));
						testCaseBean.saveJJTestcase(testcase);
						testcase = jjTestcaseService
								.findJJTestcase(testcase.getId());

						System.out.println(
								"name : " + eElement.getAttribute("name"));
						System.out.println("description : "
								+ eElement.getAttribute("description"));
						System.out.println("enabled : "
								+ eElement.getAttribute("enabled"));
						System.out.println(
								"note : " + eElement.getAttribute("Automatic"));
						System.out.println("chapter : "
								+ eElement.getAttribute("Requirement"));

						NodeList tList = eElement
								.getElementsByTagName("teststep");

						for (int i = 0; i < tList.getLength(); i++) {

							Node tNode = tList.item(i);

							System.out.println("\nCurrent Element :"
									+ tNode.getNodeName());

							if (tNode.getNodeType() == Node.ELEMENT_NODE) {

								Element tElement = (Element) tNode;
								JJTeststep teststep = new JJTeststep();
								teststep.setTestcase(testcase);
								teststep.setActionstep(
										tElement.getAttribute("actionstep"));
								teststep.setResultstep(
										tElement.getAttribute("actionstep"));
								teststep.setEnabled(true);
								teststep.setName(teststep.getActionstep() + " "
										+ teststep.getResultstep());
								teststep.setDescription(
										"This is " + teststep.getActionstep()
												+ " " + teststep.getResultstep()
												+ " description");
								// testests.add(testcase);
								teststeps.add(teststep);
								System.out.println("actionstep : "
										+ tElement.getAttribute("actionstep"));
								System.out.println("resultstep : "
										+ tElement.getAttribute("resultstep"));
							}
							testests.add(testcase);
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Object> objects = new ArrayList<Object>();
		objects.add(testests);
		objects.add(teststeps);
		return objects;
	}
}

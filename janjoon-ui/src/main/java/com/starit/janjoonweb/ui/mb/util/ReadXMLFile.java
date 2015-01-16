package com.starit.janjoonweb.ui.mb.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

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

public class ReadXMLFile {

	public static List<JJRequirement> getRequirementsFromXml(InputStream file,
			JJCategoryService jJCategoryService,JJRequirementService jJRequirementService,
			JJChapterService jJChapterService, JJProject project,
			JJCompany company, JJProduct product, JJVersion version,
			JJStatus status) {
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

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			JJCategory category = jJCategoryService.getCategory(doc
					.getDocumentElement().getAttribute("name"), true);

			NodeList nList = doc.getElementsByTagName("requirement");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					JJRequirement requirement = new JJRequirement();
					JJChapter chapter=jJChapterService.getChapterByName(
							category, eElement.getAttribute("chapter"),
							project, company);
					requirement.setName(eElement.getAttribute("name"));
					requirement.setDescription(eElement
							.getAttribute("description"));
					requirement.setEnabled(!eElement.getAttribute("enabled")
							.equals("0"));
					requirement.setNote(eElement.getAttribute("note"));
					requirement.setChapter(chapter);
					requirement.setProject(project);
					requirement.setCategory(category);
					requirement.setProduct(product);
					requirement.setVersioning(version);
					requirements.add(requirement);
					requirement.setStatus(status);
					SortedMap<Integer, Object> elements = JJRequirementBean.getSortedElements(chapter, project, category, true, jJChapterService, jJRequirementService);
					requirement.setOrdering(elements.lastKey()+1);

					System.out.println("name : "
							+ eElement.getAttribute("name"));
					System.out.println("description : "
							+ eElement.getAttribute("description"));
					System.out.println("enabled : "
							+ eElement.getAttribute("enabled"));
					System.out.println("note : "
							+ eElement.getAttribute("note"));
					System.out.println("chapter : "
							+ eElement.getAttribute("chapter"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requirements;
	}

	public static List<Object> getTestcasesFromXml(InputStream stream,JJTestcaseService jjTestcaseService,
			JJCategoryService jJCategoryService,JJRequirementService jJRequirementService,JJProject project,JJProduct product) {
		
		List<JJTestcase> testests = new ArrayList<JJTestcase>();
		List<JJTeststep> teststeps=new ArrayList<JJTeststep>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			JJCategory category = jJCategoryService.getCategory(doc
					.getDocumentElement().getAttribute("name"), true);

			NodeList nList = doc.getElementsByTagName("testcase");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					
					Element eElement = (Element) nNode;
					JJRequirement requirement=jJRequirementService.getRequirementByName(category, project, product, eElement.getAttribute("Requirement"), null);
					if(requirement != null)
					{
						JJTestcase testcase=new JJTestcase();
						testcase.setName(eElement.getAttribute("name"));
						testcase.setDescription(eElement.getAttribute("description"));
						testcase.setRequirement(requirement);
						testcase.setEnabled(!eElement.getAttribute("enabled").equalsIgnoreCase("0"));
						testcase.setAutomatic(!eElement.getAttribute("Automatic").equalsIgnoreCase("0"));
						testcase.setOrdering(jjTestcaseService.getJJtestCases(requirement).size());
					
					
					
					System.out.println("name : "
							+ eElement.getAttribute("name"));
					System.out.println("description : "
							+ eElement.getAttribute("description"));
					System.out.println("enabled : "
							+ eElement.getAttribute("enabled"));
					System.out.println("note : "
							+ eElement.getAttribute("Automatic"));
					System.out.println("chapter : "
							+ eElement.getAttribute("Requirement"));
					

					NodeList tList = eElement.getElementsByTagName("teststep");
					
					for (int i = 0; i < tList.getLength(); i++) {

						Node tNode = tList.item(i);

						System.out.println("\nCurrent Element :" + tNode.getNodeName());

						if (tNode.getNodeType() == Node.ELEMENT_NODE) {
							
							Element tElement = (Element) tNode;
							JJTeststep teststep=new JJTeststep();
							teststep.setTestcase(testcase);
							teststep.setActionstep(tElement.getAttribute("actionstep"));
							teststep.setResultstep(tElement.getAttribute("actionstep"));
							teststep.setEnabled(true);
							teststep.setName(teststep.getActionstep() + " "
									+ teststep.getResultstep());
							teststep.setDescription("This is " + teststep.getActionstep() + " "
									+ teststep.getResultstep() + " description");
							testests.add(testcase);
							teststeps.add(teststep);
							System.out.println("actionstep : "
									+ tElement.getAttribute("actionstep"));
							System.out.println("resultstep : "
									+ tElement.getAttribute("resultstep"));
						}}}
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Object> objects=new ArrayList<Object>();
		objects.add(testests);
		objects.add(teststeps);
		return objects;
	}
	
	//ordering a completr
}

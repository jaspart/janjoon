package com.starit.janjoonweb.service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepService;
import com.starit.janjoonweb.domain.JJTeststepexecution;
import com.starit.janjoonweb.domain.JJTeststepexecutionService;

@Component("testCaseWS")
@Path("/testcase")
public class TestCaseWS {

	@Autowired
	private JJBuildService jJBuildService;
	@Autowired
	private JJTestcaseService jJTestcaseService;
	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;
	@Autowired
	private JJTeststepService jJTeststepService;
	@Autowired
	private JJTeststepexecutionService jJTeststepexecutionService;

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	public void setjJTeststepexecutionService(
			JJTeststepexecutionService jJTeststepexecutionService) {
		this.jJTeststepexecutionService = jJTeststepexecutionService;
	}

	@POST
	@Path("validateTestStepExec")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String validateTestStepExec(MultivaluedMap<String, String> params) {
		Long teststepeexecutionId = null;
		Boolean status = null;

		try {
			teststepeexecutionId = Long.parseLong(params
					.getFirst("teststepeexecution"));
		} catch (NumberFormatException e) {
			teststepeexecutionId = null;
		}

		try {
			if (params.getFirst("status").equalsIgnoreCase("success"))
				status = true;
			else if (params.getFirst("status").equalsIgnoreCase("failed"))
				status = false;

		} catch (NumberFormatException e) {
			status = null;
		}

		if (status == null || teststepeexecutionId == null) {
			return "A Parameter has a FormatException";
		} else {
			JJTeststepexecution teststepexecution = jJTeststepexecutionService
					.findJJTeststepexecution(teststepeexecutionId);
			if (teststepexecution != null) {
				teststepexecution.setUpdatedDate(new Date());
				teststepexecution.setPassed(status);
				jJTeststepexecutionService
						.updateJJTeststepexecution(teststepexecution);
				return "teststepexecution :" + teststepexecution.getName()
						+ " Successfully Updated";
			} else {
				return "Teststep Execution not Found";
			}

		}
	}

	@POST
	@Path("validateTestCaseExec")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String validateTestCaseExec(MultivaluedMap<String, String> params) {
		Long testcaseeexecutionId = null;
		Boolean status = null;

		try {
			testcaseeexecutionId = Long.parseLong(params
					.getFirst("testcaseeexecution"));
		} catch (NumberFormatException e) {
			testcaseeexecutionId = null;
		}

		try {
			if (params.getFirst("status").equalsIgnoreCase("success"))
				status = true;
			else if (params.getFirst("status").equalsIgnoreCase("failed"))
				status = false;

		} catch (NumberFormatException e) {
			status = null;
		}

		if (status == null || testcaseeexecutionId == null) {
			return "A Parameter has a FormatException";
		} else {
			JJTestcaseexecution testcaseexecution = jJTestcaseexecutionService
					.findJJTestcaseexecution(testcaseeexecutionId);
			if (testcaseexecution != null) {
				testcaseexecution.setUpdatedDate(new Date());
				testcaseexecution.setPassed(status);
				jJTestcaseexecutionService
						.updateJJTestcaseexecution(testcaseexecution);
				return "testcaseexecution :" + testcaseexecution.getName()
						+ " Successfully Updated";
			} else {
				return "Testcase Execution not Found";
			}

		}
	}

	@POST
	@Path("createTestCaseExec")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createTestCaseExec(MultivaluedMap<String, String> params) {
		Long testcaseId = null;
		Long buildId = null;
		System.out.println(params.getFirst("testcaseId") + "/"
				+ params.getFirst("buildId"));
		try {
			testcaseId = Long.parseLong(params.getFirst("testcaseId"));
		} catch (NumberFormatException e) {
			testcaseId = null;
		}

		try {
			buildId = Long.parseLong(params.getFirst("buildId"));
		} catch (NumberFormatException e) {
			buildId = null;
		}

		if (buildId == null || testcaseId == null) {
			return "A Parameter has a NumberFormatException";
		} else {
			JJBuild build = jJBuildService.findJJBuild(buildId);
			JJTestcase testcase = jJTestcaseService.findJJTestcase(testcaseId);

			if (build == null) {
				return "Build Not Found";
			} else if (testcase == null) {
				return "TestCase not Found";
			} else {
				JJTestcaseexecution testcaseexecution = new JJTestcaseexecution();
				testcaseexecution.setName(testcase.getName());
				testcaseexecution.setDescription(testcase.getDescription());
				testcaseexecution.setEnabled(true);
				testcaseexecution.setTestcase(testcase);
				testcaseexecution.setBuild(build);
				testcaseexecution.setPassed(null);
				testcaseexecution.setCreationDate(new Date());
				jJTestcaseexecutionService
						.saveJJTestcaseexecution(testcaseexecution);
				String render = testcaseexecution.getId() + ",[";
				testcaseexecution = jJTestcaseexecutionService
						.findJJTestcaseexecution(testcaseexecution.getId());

				List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
						testcase, true, true);

				for (JJTeststep teststep : teststeps) {

					JJTeststepexecution teststepexecution = new JJTeststepexecution();

					teststepexecution.setName(teststep.getName());
					teststepexecution.setDescription(teststep.getDescription());
					teststepexecution.setEnabled(true);

					teststepexecution.setTeststep(teststep);
					teststepexecution.setPassed(null);

					teststepexecution.setTestcaseexecution(testcaseexecution);
					teststepexecution.setCreationDate(new Date());
					jJTeststepexecutionService
							.saveJJTeststepexecution(teststepexecution);
					render += testcaseexecution.getId() + ",";

				}

				return render.substring(0, render.length() - 1) + "],SUCCESS";
			}

		}

	}

}

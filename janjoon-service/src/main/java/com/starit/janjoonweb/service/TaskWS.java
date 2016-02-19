package com.starit.janjoonweb.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.service.entity.Task;

@Component("taskWS")
@Path("/tache")
public class TaskWS {
	@Autowired
	private JJTaskService jJTaskService;
	// @Autowired
	// private JJSprintService jJSprintService;
	@Autowired
	private JJProductService jJProductService;
	@Autowired
	private JJProjectService jJProjectService;
	// @Autowired
	// private JJContactService jJContactService;
	// @Autowired
	// private JJChapterService jJChapterService;
	// @Autowired
	// private JJBuildService jJBuidService;
	// @Autowired
	// private JJRequirementService jJRequirementService;
	// @Autowired
	// private JJTestcaseService jJTestcaseService;
	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	// public void setjJSprintService(JJSprintService jJSprintService) {
	// this.jJSprintService = jJSprintService;
	// }

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	// public void setjJContactService(JJContactService jJContactService) {
	// this.jJContactService = jJContactService;
	// }
	//
	// public void setjJChapterService(JJChapterService jJChapterService) {
	// this.jJChapterService = jJChapterService;
	// }
	//
	// public void setjJBuidService(JJBuildService jJBuidService) {
	// this.jJBuidService = jJBuidService;
	// }
	//
	// public void setjJRequirementService(
	// JJRequirementService jJResquirementService) {
	// this.jJRequirementService = jJResquirementService;
	// }
	//
	// public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
	// this.jJTestcaseService = jJTestcaseService;
	// }

	@POST
	@Path("/listetache")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getTask(MultivaluedMap<String, String> tasksParameter) {

		String productName = tasksParameter.getFirst("product");
		Long projectId = Long.parseLong(tasksParameter.getFirst("projectId"));
		JJProduct product = null;
		JJProject project = null;

		if (productName != null)
			product = jJProductService.getJJProductWithName(productName);

		if (projectId != null)
			project = jJProjectService.findJJProject(projectId);

		if (project != null && product != null)

			return Task.getListTaskFrommJJTask(
					jJTaskService.getTasksByProduct(product, project));
		else
			return null;
	}

	@PUT
	@Path("/updateTask")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String UpdateTask(MultivaluedMap<String, String> Param) {
		Long taskID = Long.parseLong(Param.getFirst("TaskId"));
		String status = Param.getFirst("statut");
		JJStatus jjstatus = jJStatusService.getOneStatus(status, "Task", true);
		JJTask task = null;

		if (taskID != null)
			task = (JJTask) jJTaskService.findJJTask(taskID);

		if (status == null || jjstatus == null)
			return "erreur";
		else if (status != null && task != null) {
			task.setStatus(jjstatus);
			jJTaskService.saveJJTask(task);
		} else
			return null;
		return "succes";
	}

	@GET
	@Path("/gettaches")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTasks() {
		return Task.getListTaskFrommJJTask(jJTaskService.findAllJJTasks());
	}

}

package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.extensions.component.codemirror.CodeMirror;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;
import com.starit.janjoonweb.ui.mb.util.service.AbstractConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.FileMap;
import com.starit.janjoonweb.ui.mb.util.service.GitConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.SvnConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.TreeOperation;

@Scope("session")
@Component("jJDevelopment")
public class DevelopmentBean implements Serializable {

	static Logger logger = Logger.getLogger("DevelopmentBean");
	private static final long serialVersionUID = 1L;
	private boolean render;

	@Autowired
	private JJStatusService jJStatusService;

	private JJConfigurationService jJConfigurationService;

	private JJTaskBean jJTaskBean;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJMessageService jJMessageService;

	@Autowired
	private JJCriticityService jJCriticityService;

	private boolean init;
	private AbstractConfigManager configManager;
	private TreeOperation treeOperation;
	private String type;
	private int activeTabIndex;
	private TreeNode tree;
	private TreeNode selectedTree;
	private ArrayList<FileMap> files;
	private String comment;
	private JJProject project;
	private JJProduct product;
	private JJVersion version;
	private JJContact contact;
	private JJConfiguration configuration;
	private List<JJTask> tasks;
	private boolean check;
	private JJTask task;
	private int fileIndex;
	private String createdFileName;
	private boolean fileOrFolder = true;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJTaskBean(JJTaskBean jJTaskBean) {
		this.jJTaskBean = jJTaskBean;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public void setjJCriticityService(JJCriticityService jJCriticityService) {
		this.jJCriticityService = jJCriticityService;
	}

	public DevelopmentBean() throws FileNotFoundException, IOException {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJConfigurationBean configurationBean = (JJConfigurationBean) session
				.getAttribute("jJConfigurationBean");
		if (configurationBean == null)
			configurationBean = new JJConfigurationBean();
		jJConfigurationService = configurationBean.getJjConfigurationService();
		init = false;
		initJJDevlopment();
	}

	public DevelopmentBean(DevelopmentBean devBean) {

		if (jJTaskBean == null) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
			if (jJTaskBean == null)
				jJTaskBean = new JJTaskBean();
		}

		init = false;
		this.jJConfigurationService = devBean.jJConfigurationService;
		this.jJStatusService = devBean.jJStatusService;
		this.jJTaskService = devBean.jJTaskService;
		this.jJMessageService = devBean.jJMessageService;
		this.jJCriticityService = devBean.jJCriticityService;
	}

	public void initJJDevlopment() throws FileNotFoundException, IOException {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if (jJTaskBean == null)
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();

		contact = ((LoginBean) session.getAttribute("loginBean")).getContact();

		init = true;
		version = LoginBean.getVersion();
		product = LoginBean.getProduct();
		project = LoginBean.getProject();

		configuration = jJConfigurationService.getConfigurations(
				"ConfigurationManager", null, true).get(0);

		if (configuration != null && getConfigManager() != null
				&& version != null && product != null) {

			render = true;
			treeOperation = new TreeOperation(configManager);
			tree = configManager.listRepositoryContent(version.getName());
			files = new ArrayList<FileMap>();

			selectedTree = getTree();
			while (selectedTree.getChildCount() != 0) {
				selectedTree = selectedTree.getChildren().get(0);

			}
			if (selectedTree != null && selectedTree.getData() instanceof File
					&& ((File) selectedTree.getData()).isFile()) {
				File file = (File) selectedTree.getData();
				files = new ArrayList<FileMap>();
				try (FileInputStream inputStream = new FileInputStream(file)) {
					String fileTexte = IOUtils.toString(inputStream);
					FileMap filemap = new FileMap(file.getName(), fileTexte,
							file);
					files.add(filemap);
				}
				selectedTree = null;
			}

		} else {
			render = false;
			String growlMessage = "";
			if (product == null) {
				growlMessage = "dev.nullProduct.label";
			} else {
				if (version == null) {
					growlMessage = "dev.nullVersion.label";

				} else {
					growlMessage = "dev.notAvailableProduct.label";

				}
			}
			FacesMessage facesMessage = MessageFactory.getMessage(growlMessage,
					FacesMessage.SEVERITY_ERROR, "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void reloadRepository() throws FileNotFoundException, IOException {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if (!(product == LoginBean.getProduct() && version == LoginBean
				.getVersion())) {

			version = LoginBean.getVersion();

			product = LoginBean.getProduct();
			if (version != null && product != null) {

				initJJDevlopment();

			} else {

				String growlMessage = "";
				if (product == null) {
					growlMessage = "dev.nullProduct.label";
				} else {
					if (version == null) {
						growlMessage = "dev.nullVersion.label";

					}
				}
				FacesMessage facesMessage = MessageFactory.getMessage(
						growlMessage, FacesMessage.SEVERITY_ERROR, "");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);

			}
		} else {

			if (project != LoginBean.getProject()) {
				DevelopmentBean jJDevelopment = (DevelopmentBean) session
						.getAttribute("jJDevelopment");
				jJDevelopment.setTasks(jJTaskService.getTasksByProduct(
						LoginBean.getProduct(), LoginBean.getProject()));
				jJDevelopment.setTask(null);
			}
			if (!render) {
				String growlMessage = "dev.notAvailableProduct.label";
				FacesMessage facesMessage = MessageFactory.getMessage(
						growlMessage, FacesMessage.SEVERITY_ERROR, "");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			}
		}

	}

	public AbstractConfigManager getConfigManager() {

		if (configuration.getParam().equalsIgnoreCase("git") && product != null
				&& version != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			String url = configuration.getVal()
					+ product.getName().replace(" ", "-") + ".git";
			if (testUrl(url)) {
				try {
					configManager = new GitConfigManager(1, contact.getName(),
							(String) session.getAttribute("password"));
					String path = System.getProperty("user.home")
							+ File.separator + "git" + File.separator
							+ contact.getName() + File.separator;

					path = configManager.cloneRemoteRepository(url,
							product.getName(), path);
					if (path != null
							&& !path.equalsIgnoreCase("InvalidRemoteException")
							&& !path.equalsIgnoreCase("TransportException")) {
						configManager = new GitConfigManager(url, path,
								contact.getName(),
								(String) session.getAttribute("password"));

					} else if (path == null) {
						path = System.getProperty("user.home") + File.separator
								+ "git" + File.separator + contact.getName()
								+ File.separator + product.getName()
								+ File.separator;
						configManager = new GitConfigManager(url, path,
								contact.getName(),
								(String) session.getAttribute("password"));

					} else {
						configManager = null;

					}
				} catch (JGitInternalException e) {
					String path = System.getProperty("user.home")
							+ File.separator + "git" + File.separator
							+ contact.getName() + File.separator
							+ product.getName() + File.separator;
					configManager = new GitConfigManager(url, path,
							contact.getName(),
							(String) session.getAttribute("password"));

				}
			} else {

				configManager = null;

			}

		} else if (configuration.getParam().equalsIgnoreCase("svn")
				&& product != null && version != null) {

			String path = System.getProperty("user.home") + File.separator
					+ "svn" + File.separator + contact.getName()
					+ File.separator;
			configManager = new SvnConfigManager("",
					"https://svn.riouxsvn.com/testchemakh", path, "chemakh",
					"taraji0000", product.getName(), version.getName());
			configManager
					.cloneRemoteRepository(
							"https://svn.riouxsvn.com/testchemakh/"
									+ product.getName(), product.getName(),
							path);

		} else
			configManager = null;

		return configManager;

	}

	public void setConfigManager(AbstractConfigManager configManager) {
		this.configManager = configManager;
	}

	public TreeOperation getTreeOperation() {
		return treeOperation;
	}

	public void setTreeOperation(TreeOperation treeOperation) {
		this.treeOperation = treeOperation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TreeNode getTree() {

		return tree;
	}

	public void setTree(TreeNode tree) {
		this.tree = tree;
	}

	public TreeNode getSelectedTree() {

		return selectedTree;
	}

	public void setSelectedTree(TreeNode selectedTree) {
		this.selectedTree = selectedTree;
	}

	public List<FileMap> getFiles() {

		return files;
	}

	public void setFiles(ArrayList<FileMap> files) {
		this.files = files;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public List<JJTask> getTasks() {

		if (task == null) {
			tasks = jJTaskService.getTasksByProduct(product, project);
			task = null;
		}

		return tasks;
	}

	public void setTasks(List<JJTask> tasks) {
		this.tasks = tasks;
	}

	public JJTask getTask() {
		return task;
	}

	public void setTask(JJTask task) {
		this.task = task;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public JJConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JJConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedFileName() {
		return createdFileName;
	}

	public void setCreatedFileName(String createdFileName) {
		this.createdFileName = createdFileName;
	}

	public boolean isFileOrFolder() {
		return fileOrFolder;
	}

	public void setFileOrFolder(boolean fileOrFolder) {
		this.fileOrFolder = fileOrFolder;
	}

	public int getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	public void pull() throws FileNotFoundException, IOException {
		FacesMessage growlMessage = null;
		if (configManager.pullRepository()) {
			tree = configManager.listRepositoryContent(version.getName());

			selectedTree = getTree();
			while (selectedTree.getChildCount() != 0) {
				selectedTree = selectedTree.getChildren().get(0);

			}
			File file = (File) selectedTree.getData();
			files = new ArrayList<FileMap>();
			try (FileInputStream inputStream = new FileInputStream(file)) {
				String fileTexte = IOUtils.toString(inputStream);
				FileMap filemap = new FileMap(file.getName(), fileTexte, file);
				files.add(filemap);

				filemap.setIndex(contains(filemap.getFile()));
				files.set(filemap.getIndex(), filemap);

			}

			growlMessage = MessageFactory.getMessage("dev.updateToHead.label",
					FacesMessage.SEVERITY_INFO, configManager.getUrl());
		} else {
			growlMessage = MessageFactory.getMessage("dev.errorSynchro.label",
					FacesMessage.SEVERITY_ERROR, configManager.getUrl());

		}
		FacesContext.getCurrentInstance().addMessage(null, growlMessage);
	}

	public void commit() {

		JJStatus status = null;
		for (FileMap fileMap : files) {
			configManager.setFileTexte(fileMap.getFile(), fileMap.getTexte());

		}
		task = jJTaskService.findJJTask(task.getId());
		if (check) {

			task.setEndDateReal(new Date());
			task.setCompleted(true);
			status = jJStatusService.getOneStatus("DONE", "Task", true);
			task.setStatus(status);

		} else {
			task.setCompleted(false);
		}
		JJMessage message = new JJMessage();
		message.setProduct(product);
		message.setContact(contact);
		message.setCompany(contact.getCompany());
		message.setProject(project);
		message.setVersioning(version);
		message.setEnabled(true);
		message.setTask(task);
		message.setCriticity(jJCriticityService
				.getCriticityByName("INFO", true));
		message.setMessage(comment);
		message.setDescription("Message For" + task.getName() + "nl"
				+ task.getDescription());
		message.setName("Message For" + task.getName());
		saveJJMessage(message);

		if (task.getStartDateReal() == null) {
			task.setStartDateReal(new Date());
		}

		if (!task.getCompleted()) {
			status = jJStatusService.getOneStatus("IN PROGRESS", "Task", true);
			task.setStatus(status);
		}

		jJTaskBean.saveJJTask(task, false);
		if (task.getSprint() != null
				&& LoginBean.findBean("jJSprintBean") != null) {
			JJSprintBean jJSprintBean = (JJSprintBean) LoginBean
					.findBean("jJSprintBean");
			if (jJSprintBean.contains(task.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(task.getSprint()
						.getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(jJSprintBean.getJJSprintService()
							.findJJSprint(task.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintBean.getJJSprintService()
											.findJJSprint(
													task.getSprint().getId()),
									LoginBean.getProduct()),
							jJSprintBean.getJJContactService());
					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}

		FacesMessage growlMessage = null;

		if (configManager.checkIn(null, task.getId() + ":" + task.getName()
				+ " : " + comment)) {
			FacesMessage commitMessage = MessageFactory.getMessage(
					"dev.commitSuccess.label", FacesMessage.SEVERITY_INFO, "");
			FacesContext.getCurrentInstance().addMessage(null, commitMessage);
			if (configManager.getType().equalsIgnoreCase("git")
					&& configManager.pushRepository()) {
				growlMessage = MessageFactory.getMessage(
						"dev.pushSucces.label", FacesMessage.SEVERITY_INFO, "");

				comment = "";
				task = null;
			} else if (configManager.getType().equalsIgnoreCase("git")) {
				growlMessage = MessageFactory.getMessage("dev.pushError.label",
						FacesMessage.SEVERITY_ERROR, "");
			} else {
				comment = null;
				task = null;
			}

		} else {

			growlMessage = MessageFactory.getMessage("dev.commitError.label",
					FacesMessage.SEVERITY_ERROR, "");

		}
		if (growlMessage != null)
			FacesContext.getCurrentInstance().addMessage(null, growlMessage);

	}

	public void startTask() {
		task.setStartDateReal(new Date());
		jJTaskBean.setTask(task);
		jJTaskBean.updateTask("dev");
		// task=jJTaskService.findJJTask(task.getId());
		// tasks = jJTaskService.getTasksByProduct(product, project);

	}

	public void onSelectTree(NodeSelectEvent event) {

		if (event.getTreeNode().getType().equalsIgnoreCase("file")) {
			selectedTree = event.getTreeNode();
			File file = (File) selectedTree.getData();
			int i = contains(file);
			if (i == -1) {

				try (FileInputStream inputStream = new FileInputStream(file)) {

					String fileTexte = IOUtils.toString(inputStream);
					FileMap filemap = new FileMap(file.getName(), fileTexte,
							file);
					files.add(filemap);
					i = contains(file);
					filemap.setIndex(i);
					files.set(filemap.getIndex(), filemap);
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			activeTabIndex = i;

		}

	}

	public void valueChangeHandlerCodeMirror(AjaxBehaviorEvent event)
			throws InterruptedException, IOException {

		CodeMirror cm = (CodeMirror) event.getComponent().getAttributes()
				.get("cm");
		FileInputStream inputStream = new FileInputStream(files.get(
				activeTabIndex).getFile());

		String fileTexte = IOUtils.toString(inputStream);
		String cmValue = cm.getValue().toString();

		if (!(cmValue.contains(fileTexte) && fileTexte.contains(cmValue))) {
			files.get(activeTabIndex).setChange(true);
			// files.get(activeTabIndex).setTexte(cmValue);

		}

	}

	public void onCloseTab(TabCloseEvent event) {

		FileMap index = (FileMap) event.getData();
		int j = contains(index.getFile());

		if (index.isChange()) {

			fileIndex = j;
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('saveFileDialogWidget').show()");

		} else {

			updatetabView(j, index);
		}
	}

	public void onChangeTab(TabChangeEvent event) {

		// FileMap index = (FileMap) event.getData();
		// System.out.println("onChangeTab-------------------------"
		// + index.getIndex() + ":" + files.get(index.getIndex()).getTitle()
		// + files.get(index.getIndex()).getIndex());
		// files.set(index.getIndex(), index);

	}

	public int contains(File f) {
		int i = 0;
		int j = -1;
		while (i < files.size()) {
			if (files.get(i).getFile().getAbsolutePath()
					.equalsIgnoreCase(f.getAbsolutePath())) {
				j = i;
				i = files.size();
			} else
				i++;
		}
		return j;

	}

	public void deleteFile() {

		File f = (File) selectedTree.getData();
		selectedTree.getChildren().clear();
		selectedTree.getParent().getChildren().remove(selectedTree);
		selectedTree.setParent(null);

		treeOperation.deleteFile(f, configManager);
		int i = contains(f);
		if (i != -1) {
			updatetabView(i, files.get(i));
		}
		FacesMessage msg = MessageFactory.getMessage(
				"dev.file_successfully_deleted", FacesMessage.SEVERITY_INFO,
				f.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	private void saveFiles(File f) throws IOException {
		if (!f.isDirectory()) {
			FileMap fMap = new FileMap(f.getName(), null, f);
			if (files.contains(fMap)
					&& files.get(files.indexOf(fMap)).isChange()) {
				configManager.setFileTexte(f, files.get(files.indexOf(fMap))
						.getTexte());
			}
		} else {
			if (f.list().length != 0) {

				String files[] = f.list();
				for (String temp : files) {
					File fileDelete = new File(f, temp);

					saveFiles(fileDelete);
				}
			}
		}
	}

	public void commitFile() throws IOException {

		File f = (File) selectedTree.getData();
		saveFiles(f);

		configManager.checkIn(f, "commitFile" + f.getName());

		FacesMessage msg = MessageFactory.getMessage(
				"dev.file_successfully_commited", FacesMessage.SEVERITY_INFO,
				f.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveFile() {

		configManager.setFileTexte(files.get(fileIndex).getFile(),
				files.get(fileIndex).getTexte());
		updatetabView(fileIndex, files.get(fileIndex));
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveFileDialogWidget').hide()");
		if (files.isEmpty())
			context.update(":form :growlForm");
		else
			context.update(":form:tabView :growlForm");
		fileIndex = -1;

	}

	public void notSaveFile() {

		updatetabView(fileIndex, files.get(fileIndex));
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveFileDialogWidget').hide()");
		fileIndex = -1;
	}

	public void CreateFile() {

		FacesMessage msg = null;
		if (selectedTree != null) {
			if (fileOrFolder) {
				if (treeOperation.addFile(version.getName(),
						(File) selectedTree.getData(), createdFileName)) {
					msg = MessageFactory.getMessage(
							"dev.file_successfully_created",
							FacesMessage.SEVERITY_INFO, createdFileName);
					tree = configManager.listRepositoryContent(version
							.getName());

				} else {
					msg = MessageFactory.getMessage(
							"dev.file_unsuccessfully_created",
							FacesMessage.SEVERITY_ERROR, createdFileName);

				}

			} else {
				if (treeOperation.addFolder(version.getName(),
						(File) selectedTree.getData(), createdFileName)) {
					msg = MessageFactory.getMessage(
							"dev.file_successfully_created",
							FacesMessage.SEVERITY_INFO, createdFileName);
					tree = configManager.listRepositoryContent(version
							.getName());

				} else {
					msg = MessageFactory.getMessage(
							"dev.file_unsuccessfully_created",
							FacesMessage.SEVERITY_ERROR, createdFileName);
				}
			}

		} else {
			if (fileOrFolder) {
				if (treeOperation.addFile(version.getName(), null,
						createdFileName)) {
					msg = MessageFactory.getMessage(
							"dev.file_successfully_created",
							FacesMessage.SEVERITY_INFO, createdFileName);
					tree = configManager.listRepositoryContent(version
							.getName());

				} else {
					msg = MessageFactory.getMessage(
							"dev.file_unsuccessfully_created",
							FacesMessage.SEVERITY_ERROR, createdFileName);

				}

			} else {
				if (treeOperation.addFolder(version.getName(), null,
						createdFileName)) {
					msg = MessageFactory.getMessage(
							"dev.file_successfully_created",
							FacesMessage.SEVERITY_INFO, createdFileName);
					tree = configManager.listRepositoryContent(version
							.getName());

				} else {
					msg = MessageFactory.getMessage(
							"dev.file_unsuccessfully_created",
							FacesMessage.SEVERITY_ERROR, createdFileName);

				}
			}

		}
		createdFileName = "";
		fileOrFolder = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createFileDialogWidget').hide()");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void handleFileUpload(FileUploadEvent event) {

		try {
			boolean upload;
			if (selectedTree != null)
				upload = treeOperation.uploadFile(version.getName(),
						(File) selectedTree.getData(), event.getFile()
								.getFileName(), event.getFile()
								.getInputstream());
			else
				upload = treeOperation.uploadFile(version.getName(), null,
						event.getFile().getFileName(), event.getFile()
								.getInputstream());

			if (upload) {
				FacesMessage msg = MessageFactory.getMessage(
						"dev.file_successfully_uploaded",
						FacesMessage.SEVERITY_INFO, event.getFile()
								.getFileName());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				tree = configManager.listRepositoryContent(version.getName());
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('createFileDialogWidget').hide()");
			} else {
				FacesMessage msg = MessageFactory.getMessage(
						"dev.file_unsuccessfully_uploaded",
						FacesMessage.SEVERITY_ERROR, event.getFile()
								.getFileName());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (IOException e) {
			FacesMessage msg = MessageFactory.getMessage(
					"dev.file_unsuccessfully_uploaded",
					FacesMessage.SEVERITY_ERROR, event.getFile().getFileName());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	private void updatetabView(int j, FileMap data) {
		int i = j + 1;
		while (i < files.size()) {
			files.get(i).setIndex(i - 1);
			i++;
		}
		files.remove(data);
		if (activeTabIndex == j) {
			boolean t = !files.isEmpty();
			while (t) {
				if (j < files.size()) {
					if (j < 0)
						t = false;
					else {
						activeTabIndex = j;
						files.set(activeTabIndex, files.get(activeTabIndex));
						t = false;
					}

				} else if (j < 0)
					t = false;
				j--;
			}

		}
		if (files.size() == 0) {

			RequestContext context = RequestContext.getCurrentInstance();
			context.update("applicatinPanelGrid");
		}

	}

	public void saveJJMessage(JJMessage m) {
		JJContact contact = ((LoginBean) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("loginBean")).getContact();
		m.setCreatedBy(contact);
		m.setCreationDate(new Date());
		jJMessageService.saveJJMessage(m);
	}

	public void addToRoot(ActionEvent e) {
		selectedTree = null;
	}

	public boolean testUrl(String urlPath) {

		URL url;
		try {
			url = new URL(urlPath);
			HttpURLConnection huc;
			huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod("HEAD");

			try {

				int responseCode = huc.getResponseCode();

				if (responseCode != 404) {

					logger.info("Valid URL");
					return true;
				} else {
					logger.error("BAD URL");
					return false;
				}
			} catch (SSLHandshakeException e) {
				System.err.println(e.getMessage());
				return false;
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}

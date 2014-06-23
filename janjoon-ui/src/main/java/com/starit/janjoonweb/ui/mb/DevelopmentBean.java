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

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJContact;
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
import com.starit.janjoonweb.ui.mb.util.service.AbstractConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.FileMap;
import com.starit.janjoonweb.ui.mb.util.service.GitConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.TreeOperation;

@Scope("session")
@Component("jJDevelopment")
public class DevelopmentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean render;

	static Logger logger = Logger.getLogger("DevelopmentBean");

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJMessageService jJMessageService;

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

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public DevelopmentBean() throws FileNotFoundException, IOException {

		init = false;
		initJJDevlopment();
	}

	public DevelopmentBean(DevelopmentBean devBean) {

		init = false;
		this.jJStatusService = devBean.jJStatusService;
		this.jJTaskService = devBean.jJTaskService;
		this.jJMessageService = devBean.jJMessageService;
	}

	public void initJJDevlopment() throws FileNotFoundException, IOException {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		contact = (JJContact) session.getAttribute("JJContact");
		JJVersionBean verbean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		JJConfigurationBean confbean = (JJConfigurationBean) session
				.getAttribute("jJConfigurationBean");
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");

		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");
		init = true;

		if (verbean != null) {
			if (verbean.getVersion() != null) {
				version = verbean.getVersion();
			}
		}
		product = prodbean.getProduct();
		project = projbean.getProject();

		if (confbean == null)
			confbean = new JJConfigurationBean();

		configuration = confbean.getjJconfiguration();

		if (getConfigManager() != null && version != null && product != null) {

			render = true;
			treeOperation = new TreeOperation(configManager);
			tree = configManager.listRepositoryContent(version.getName());
			files = new ArrayList<FileMap>();

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
			}
			selectedTree = null;

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

		JJVersionBean verbean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");

		if (!(product == prodbean.getProduct() && version == verbean
				.getVersion())) {
			if (verbean != null) {
				if (verbean.getVersion() != null) {
					version = verbean.getVersion();
				}
			}

			product = prodbean.getProduct();
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
			JJProjectBean jJProjectBean = (JJProjectBean) session
					.getAttribute("jJProjectBean");
			if (project != jJProjectBean.getProject()) {
				DevelopmentBean jJDevelopment = (DevelopmentBean) session
						.getAttribute("jJDevelopment");
				jJDevelopment.setTasks(jJTaskService.getTasksByProduct(
						prodbean.getProduct(), jJProjectBean.getProject()));
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
			
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(false);
			
			String url = configuration.getVal()
					+ product.getName().replace(" ", "-") + ".git";
			if (testUrl(url)) {
				try {
					configManager = new GitConfigManager(1, contact.getName(),(String) session.getAttribute("password"));
					String path = System.getProperty("user.home") + File.separator+"git"+File.separator+contact.getName()+File.separator;

					path = configManager.cloneRemoteRepository(url,
							product.getName(), path);
					if (path != null
							&& !path.equalsIgnoreCase("InvalidRemoteException")
							&& !path.equalsIgnoreCase("TransportException")) {
						configManager = new GitConfigManager(url, path, contact.getName(),(String)session.getAttribute("password"));

					} else if (path == null) {
						path = System.getProperty("user.home") + File.separator+"git"+File.separator+contact.getName()+File.separator
								+ product.getName() + File.separator;
						configManager = new GitConfigManager(url, path,contact.getName(),(String) session.getAttribute("password"));

					} else {
						configManager = null;

					}
				} catch (JGitInternalException e) {
					String path = System.getProperty("user.home") + File.separator+"git"+File.separator+contact.getName()+File.separator
							+ product.getName() + File.separator;
					configManager =  new GitConfigManager(url, path,contact.getName(),(String) session.getAttribute("password"));

				}
			} else {

				configManager = null;

			}

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
			status = jJStatusService.getOneStatus("DONE", "JJTask", true);
			task.setStatus(status);

		} else {
			task.setCompleted(false);
		}
		JJMessage message = new JJMessage();
		message.setProduct(product);
		message.setContact(contact);
		message.setProject(project);
		message.setVersioning(version);
		message.setCreatedBy(contact);
		message.setCreationDate(new Date());
		message.setEnabled(true);
		message.setMessage(comment);
		message.setDescription("JJmessage For" + task.getName() + "nl"
				+ task.getDescription());
		message.setName("JJmessage For" + task.getName());
		jJMessageService.saveJJMessage(message);

		task.getMessages().add(message);
		if (task.getStartDateReal() == null)
		{
			task.setStartDateReal(new Date());
		}
			

		if (!task.getCompleted()) {
			status = jJStatusService
					.getOneStatus("IN PROGRESS", "JJTask", true);
			task.setStatus(status);
		}

		jJTaskService.saveJJTask(task);
		FacesMessage growlMessage = null;

		if (configManager.checkIn(task.getId() + ":" + task.getName() + " : "
				+ comment)) {
			FacesMessage commitMessage = MessageFactory.getMessage(
					"dev.commitSuccess.label", FacesMessage.SEVERITY_INFO, "");
			FacesContext.getCurrentInstance().addMessage(null, commitMessage);
			if (configManager.pushRepository()) {
				growlMessage = MessageFactory.getMessage(
						"dev.pushSucces.label", FacesMessage.SEVERITY_INFO, "");

				comment = "";
			} else {
				growlMessage = MessageFactory.getMessage("dev.pushError.label",
						FacesMessage.SEVERITY_ERROR, "");
			}

		} else {

			growlMessage = MessageFactory.getMessage("dev.commitError.label",
					FacesMessage.SEVERITY_ERROR, "");

		}
		FacesContext.getCurrentInstance().addMessage(null, growlMessage);

	}

	public void startTask() {
		task.setStartDateReal(new Date());
		jJTaskService.saveJJTask(task);
		tasks = jJTaskService.getTasksByProduct(product, project);
		task = jJTaskService.findJJTask(task.getId());

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

		CodeMirror cm = (CodeMirror) event.getComponent();

		FileInputStream inputStream = new FileInputStream(files.get(
				activeTabIndex).getFile());

		String fileTexte = IOUtils.toString(inputStream);

		if (!cm.getValue().toString().contains(fileTexte)
				&& fileTexte.contains(cm.getValue().toString())) {
			files.get(activeTabIndex).setChange(true);

		}

	}

	public void onCloseTab(TabCloseEvent event) {

		FileMap index = (FileMap) event.getData();
		int j = contains(index.getFile());

		if (index.isChange()) {

			fileIndex = j;
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("saveFileDialogWidget.show()");
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
			if (files.get(i).getFile() == f) {
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

		treeOperation.deleteFile(f);
		int i = contains(f);
		if (i != -1) {
			updatetabView(i, files.get(i));
		}
		FacesMessage msg = MessageFactory.getMessage(
				"dev.file_successfully_deleted", FacesMessage.SEVERITY_INFO,
				f.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void commitFile() {

		File f = (File) selectedTree.getData();
		configManager.checkIn("commitFile" + f.getName());

		FacesMessage msg = MessageFactory.getMessage(
				"dev.file_successfully_commited", FacesMessage.SEVERITY_INFO,
				f.getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveFile() {

		configManager.setFileTexte(files.get(fileIndex).getFile(),
				files.get(fileIndex).getTexte());
		updatetabView(fileIndex, files.get(activeTabIndex));
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("saveFileDialogWidget.hide()");
		if (files.isEmpty())
			context.update(":form");
		else
			context.update(":form:tabView");
		fileIndex = -1;

	}

	public void notSaveFile() {

		updatetabView(fileIndex, files.get(activeTabIndex));
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("saveFileDialogWidget.hide()");
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
		context.execute("createFileDialogWidget.hide()");
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
				context.execute("createFileDialogWidget.hide()");
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
			int responseCode = huc.getResponseCode();

			if (responseCode != 404) {

				logger.info("Valid URL");
				return true;
			} else {
				logger.error("BAD URL");
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

package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.extensions.component.codemirror.CodeMirror;
import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.service.AbstractConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.FileMap;
import com.starit.janjoonweb.ui.mb.util.service.GitConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.TreeOperation;

@ManagedBean(name = "jJDevelopment")
@ViewScoped
public class DevelopmentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean render;
	private AbstractConfigManager configManager;
	private TreeOperation treeOperation;
	private String type;
	private int activeTabIndex;
	private TreeNode tree;
	private TreeNode selectedTree;
	private ArrayList<FileMap> files;
	private File file;
	private String comment;
	private JJMessage message;
	private JJProject project;
	private JJProduct product;
	private JJVersion version;
	private JJContact contact;
	private JJMessageBean messageBean;
	private JJConfiguration configuration;
	private List<JJTask> tasks;
	private boolean check;
	private JJTask task;
	private String createdFileName;
	private boolean fileOrFolder = true;

	public DevelopmentBean() throws FileNotFoundException, IOException {
		// getting value from session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		contact = (JJContact) session.getAttribute("JJContact");
		JJVersionBean verbean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		JJConfigurationBean confbean = (JJConfigurationBean) session
				.getAttribute("jJConfigurationBean");
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		setMessageBean((JJMessageBean) session.getAttribute("jJMessageBean"));
		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");
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

			message = new JJMessage();
			System.out.println(contact.getName());
			tasks = prodbean.getTasksByProduct(product, project);
			for (JJTask t : tasks) {
				System.out
						.println(t.getName() + "--" + configuration.getName());
			}

			render = true;
			treeOperation = new TreeOperation(configManager);
			tree = configManager.listRepositoryContent(version.getName());

			selectedTree = getTree();
			while (selectedTree.getChildCount() != 0) {
				selectedTree = selectedTree.getChildren().get(0);

			}
			file = (File) selectedTree.getData();
			files = new ArrayList<FileMap>();
			try (FileInputStream inputStream = new FileInputStream(file)) {
				String fileTexte = IOUtils.toString(inputStream);
				FileMap filemap = new FileMap(file.getName(), fileTexte, file);
				files.add(filemap);
			}

		} else {
			render = false;
			if (product == null) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Please, select a Project and a Version ",
						"Product and Version are set to null");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else {
				if (version == null) {

					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please Select a version ",
							"Version is set to null");
					FacesContext.getCurrentInstance().addMessage(null, message);

				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Product not available on the version control manager.",
							"Project  not available");
					FacesContext.getCurrentInstance().addMessage(null, message);

				}
			}
		}
		session.setAttribute("jJDevelopment", this);
	}

	public void reloadRepository(ComponentSystemEvent e)
			throws FileNotFoundException, IOException {

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
				DevelopmentBean jJDevelopment = (DevelopmentBean) session
						.getAttribute("jJDevelopment");
				jJDevelopment = new DevelopmentBean();

			} else {

				if (product == null) {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please Select a project and a version ",
							"Project and version are set to null");
					FacesContext.getCurrentInstance().addMessage(null, message);

				} else {
					if (version == null) {

						FacesMessage message = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Please Select a version ",
								"Version is set to null");
						FacesContext.getCurrentInstance().addMessage(null,
								message);

					}
				}
			}
		} else {
			JJProjectBean jJProjectBean = (JJProjectBean) session
					.getAttribute("jJProjectBean");
			if (project != jJProjectBean.getProject()) {
				DevelopmentBean jJDevelopment = (DevelopmentBean) session
						.getAttribute("jJDevelopment");
				jJDevelopment.setTasks(prodbean.getTasksByProduct(
						prodbean.getProduct(), jJProjectBean.getProject()));
			}
			if (!render) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Product not available on the version control manager.",
						"Project  not available");
				FacesContext.getCurrentInstance().addMessage(null, message);

			}
		}

	}

	public AbstractConfigManager getConfigManager() {

		if (configuration.getParam().equalsIgnoreCase("git") && product != null
				&& version != null) {
			String url = configuration.getVal()
					+ product.getName().replace(" ", "-") + ".git";
			try {
				configManager = new GitConfigManager(1, contact);
				String path = System.getProperty("user.home") + "/git/";

				path = configManager.cloneRemoteRepository(url,
						product.getName(), path);
				if (path != null
						&& !path.equalsIgnoreCase("InvalidRemoteException")
						&& !path.equalsIgnoreCase("TransportException")) {
					configManager = new GitConfigManager(url, path, contact);
					System.out.println(path);
				} else if (path == null) {
					path = System.getProperty("user.home") + "/git/"
							+ product.getName() + "/";
					configManager = new GitConfigManager(url, path, contact);
					System.out.println(path);
				} else {
					configManager = null;

				}
			} catch (JGitInternalException e) {
				String path = System.getProperty("user.home") + "/git/"
						+ product.getName() + "/";
				configManager = new GitConfigManager(url, path, contact);
				System.out.println(path);
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

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {

		return file;
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

	public JJMessage getMessage() {
		return message;
	}

	public void setMessage(JJMessage message) {
		this.message = message;
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

	public JJMessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(JJMessageBean messageBean) {
		this.messageBean = messageBean;
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

	public void pull() throws FileNotFoundException, IOException {
		if (configManager.pullRepository()) {
			tree = configManager.listRepositoryContent(version.getName());

			selectedTree = getTree();
			while (selectedTree.getChildCount() != 0) {
				selectedTree = selectedTree.getChildren().get(0);

			}
			file = (File) selectedTree.getData();
			files = new ArrayList<FileMap>();
			try (FileInputStream inputStream = new FileInputStream(file)) {
				String fileTexte = IOUtils.toString(inputStream);
				FileMap filemap = new FileMap(file.getName(), fileTexte, file);
				files.add(filemap);
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Updated To Head", configManager.getPath());
			FacesContext.getCurrentInstance().addMessage(null, message);

		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error with Synchronisation",
					configManager.getPath());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void commit() {

		if (!comment.replace(" ", "").equalsIgnoreCase("")) {
			for (FileMap fileMap : files) {
				configManager.setFileTexte(fileMap.getFile(),
						fileMap.getTexte());
				// System.out.println(fileMap.getFile().getName() + "  :"
				// + fileMap.getTexte());

			}
			System.out.println(task.toString());
			if (check) {

				task.setEndDateReal(new Date());
				task.setCompleted(true);

			}
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
			messageBean.save(message);
			task.getMessages().add(message);
			if (task.getStartDateReal() == null)
				task.setStartDateReal(new Date());
			task.persist();

			if (configManager.checkIn(task.getId() + ":" + task.getName()
					+ " : " + comment)) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Commited",
						configManager.getPath());
				FacesContext.getCurrentInstance().addMessage(null, message);
				if (configManager.pushRepository()) {
					message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Pushed to remote Repository",
							configManager.getPath());
					FacesContext.getCurrentInstance().addMessage(null, message);
					comment = "";
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Probleme Lors du Fetch ", configManager.getPath());
					FacesContext.getCurrentInstance().addMessage(null, message);
				}

			} else {

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Probleme Lors du Commit",
						configManager.getPath());
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} else {

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Probleme Lors du Commit",
					"You Have to create comment before commiting");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void onSelectTree(NodeSelectEvent event) {

		if (event.getTreeNode().getType().equalsIgnoreCase("file")) {
			System.out.println("fffffffffffffffffffffffffffd");
			selectedTree = event.getTreeNode();
			file = (File) selectedTree.getData();

			if (contains(file) == -1) {

				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Selected", event
								.getTreeNode().toString());
				FacesContext.getCurrentInstance().addMessage(null, message);
				try (FileInputStream inputStream = new FileInputStream(file)) {

					String fileTexte = IOUtils.toString(inputStream);
					// System.out.println(fileTexte);

					FileMap filemap = new FileMap(file.getName(), fileTexte,
							file);
					files.add(filemap);
					System.out.println(filemap.getMode());
				} catch (FileNotFoundException e) {
					System.out.println("filenotFound");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("filenotFound");
					e.printStackTrace();
				}
			}
			activeTabIndex = contains(file);

		}

	}

	public void valueChangeHandler(AjaxBehaviorEvent event) {

		System.out
				.println("---------------------------------------------------------");
		InputTextarea texte = (InputTextarea) event.getComponent();
		// File f=texte.

		// System.out.println(texte.getValue() + "---");
		texte.setSubmittedValue(texte.getValue());
	}

	public void valueChangeHandlerCodeMirror(AjaxBehaviorEvent event) {

		System.out
				.println("---------------------------------------------------------");
		CodeMirror texte = (CodeMirror) event.getComponent();
		// File f=texte.
		// System.out.println(texte.getValue() + "---");
		texte.setSubmittedValue(texte.getValue());
	}

	public void onCloseTab(TabCloseEvent event) {

		FileMap f = (FileMap) event.getData();
		int j = contains(f.getFile());
		files.get(j).setTexte(f.getTexte());
		configManager.setFileTexte(files.get(j).getFile(), files.get(j)
				.getTexte());
		files.remove((FileMap) event.getData());
	}

	public void onChangeTab(TabChangeEvent event) {

		FileMap f = (FileMap) event.getData();
		int j = contains(f.getFile());
		files.get(j).setTexte(f.getTexte());

	}

	public void PersistMessage(ActionEvent actionEvent) {

		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		boolean loggedIn = false;

		if (!message.getDescription().equalsIgnoreCase("")
				&& message.getName().equalsIgnoreCase("")
				&& message.getMessage().equalsIgnoreCase("") && task != null) {
			loggedIn = true;
			message.setCreatedBy(contact);
			message.setCreationDate(new Date());
			message.persist();
			Set<JJMessage> m = new HashSet<JJMessage>();
			m.add(message);
			task.setMessages(m);
			if (task.getStartDateReal() == null)
				task.setStartDateReal(new Date());
			task.persist();
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Message created", message.getName());
		} else {
			loggedIn = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Creation Error",
					"one or more  requeired field are set to null");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("loggedIn", loggedIn);

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
		System.out.println("deletFile" + f.getName());
		treeOperation.deleteFile(f);
		int i = contains(f);
		if (i != -1) {
			files.remove(i);
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"File deleted", f.getName() + " supprimé avec succes");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void commitFile() {
		File f = (File) selectedTree.getData();
		configManager.checkIn("commitFile" + f.getName());
		System.out.println("commitFile");
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"File commited", f.getName() + " ajouté au depôt avec succes");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void CreateFile() {

		FacesMessage msg = null;
		if (selectedTree != null) {
			if (fileOrFolder) {
				if (treeOperation.addFile(version.getName(),
						(File) selectedTree.getData(), createdFileName)) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"File Created", createdFileName
									+ " créé avec succes");
					tree = configManager.listRepositoryContent(version
							.getName());
					System.out.println("kamalna file ");
				} else {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Problem in File Creation",
							"erreur lors de la création du fichier "
									+ createdFileName);

				}

			} else {
				if (treeOperation.addFolder(version.getName(),
						(File) selectedTree.getData(), createdFileName)) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Folder Created", createdFileName
									+ " créé avec succes");
					tree = configManager.listRepositoryContent(version
							.getName());
					System.out.println("kamalna Folder ");
				} else {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Problem in Folder Creation",
							"erreur lors de la création du dossier "
									+ createdFileName);

				}
			}

		} else {
			if (fileOrFolder) {
				if (treeOperation.addFile(version.getName(), null,
						createdFileName)) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"File Created", createdFileName
									+ " créé avec succes");
					tree = configManager.listRepositoryContent(version
							.getName());
					System.out.println("kamalna file ");
				} else {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Problem in File Creation",
							"erreur lors de la création du fichier "
									+ createdFileName);

				}

			} else {
				if (treeOperation.addFolder(version.getName(), null,
						createdFileName)) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Folder Created", createdFileName
									+ " créé avec succes");
					tree = configManager.listRepositoryContent(version
							.getName());
					System.out.println("kamalna Folder ");
				} else {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Problem in Folder Creation",
							"erreur lors de la création du dossier "
									+ createdFileName);

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
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Succesful", event.getFile().getFileName()
								+ " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				tree = configManager.listRepositoryContent(version.getName());
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("createFileDialogWidget.hide()");
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error", event.getFile()
								.getFileName() + " is not uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error", event.getFile().getFileName()
							+ " is not uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void addToRoot(ActionEvent e) {
		selectedTree = null;
	}
}

package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.extensions.component.codemirror.CodeMirror;
import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.service.AbstractConfigManager;
import com.starit.janjoonweb.ui.mb.util.service.FileMap;
import com.starit.janjoonweb.ui.mb.util.service.GitConfigManager;

@ManagedBean(name = "jJDevelopment")
@ViewScoped
public class DevelopmentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static String URL = "https://github.com/janjoon/ProductName-1";
	private TabView tabView;
	private AbstractConfigManager configManager;
	private String type;
	private int activeTabIndex;
	private TreeNode tree;
	private TreeNode selectedTree;
	private ArrayList<FileMap> files;
	private File file;
	private JJProject project;
	private JJProduct product;
	private JJVersion version;
	private JJContact contact;
	private List<JJTask> tasks;
	private JJTask task;

	public DevelopmentBean() throws FileNotFoundException, IOException {
		// getting value from session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		contact = (JJContact) session.getAttribute("JJContact");
		JJVersionBean verbean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");
		if (verbean != null) {
			if (verbean.getVersion() != null) {
				version = verbean.getVersion();
			}
		}
		product = prodbean.getProduct();
		project = projbean.getProject();
		if (contact == null) {
			JJContactBean conbean = new JJContactBean();
			contact = conbean.getContactByEmail("admin@gmail.com");
		}		
		System.out.println(contact.getName());
		tasks = prodbean.getTasksByProduct(product, project);
		for (JJTask t : tasks) {
			System.out.println("111111" + t.getName());
		}

		try {
			configManager = new GitConfigManager(1, contact);
			String path = System.getProperty("user.home") + "/git/";
			path = configManager.cloneRemoteRepository(URL,
					"JanjonProduct", path);
			configManager = new GitConfigManager(URL, path, contact);
			System.out.println(path);

		} catch (Exception e) {
			String path = System.getProperty("user.home")
					+ "/git/JanjonProduct/";
			configManager = new GitConfigManager(URL, path, contact);
			System.out.println(path);
		}
		tree = getConfigManager().listRepositoryContent();
		selectedTree = getTree();
		while (selectedTree.getChildCount() != 0) {
			selectedTree = selectedTree.getChildren().get(0);

		}
		file = (File) selectedTree.getData();
		files = new ArrayList<FileMap>();
		try (FileInputStream inputStream = new FileInputStream(file)) {
			String fileTexte = IOUtils.toString(inputStream);
			FileMap filemap = new FileMap(file.getPath(), file.getName(),
					fileTexte, file);
			files.add(filemap);
		}
	}

	public AbstractConfigManager getConfigManager() {

		return configManager;
	}

	public void setConfigManager(AbstractConfigManager configManager) {
		this.configManager = configManager;
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

	public void setTree() {
		this.tree = configManager.listRepositoryContent();
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

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
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

	public void pull() {
		if (configManager.pullRepository()) {
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

		for (FileMap fileMap : files) {
			configManager.setFileTexte(fileMap.getFile(), fileMap.getTexte());
			System.out.println(fileMap.getFile().getName() + "  :"
					+ fileMap.getTexte());

		}		
		System.out.println(task.toString());
		if (configManager.checkIn(task.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Commited", configManager.getPath());
			FacesContext.getCurrentInstance().addMessage(null, message);
			if (configManager.pushRepository()) {
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Pushed to remote Repository", configManager.getPath());
				FacesContext.getCurrentInstance().addMessage(null, message);
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

	}

	public void onSelectTree(NodeSelectEvent event) {

		if (event.getTreeNode().getType().equalsIgnoreCase("file")) {
			System.out.println("fffffffffffffffffffffffffffd");
			selectedTree = event.getTreeNode();
			file = (File) selectedTree.getData();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Selected", event.getTreeNode().toString());
			FacesContext.getCurrentInstance().addMessage(null, message);

			if (contains(file) == -1) {
				try (FileInputStream inputStream = new FileInputStream(file)) {

					String fileTexte = IOUtils.toString(inputStream);
					// System.out.println(fileTexte);

					FileMap filemap = new FileMap(file.getPath(),
							file.getName(), fileTexte, file);
					files.add(filemap);
				} catch (FileNotFoundException e) {
					System.out.println("filenotFound");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("filenotFound");
					e.printStackTrace();
				}
			}
			tabView.setActiveIndex(contains(file));

		}

	}	

	public void valueChangeHandler(AjaxBehaviorEvent event) {

		System.out
				.println("---------------------------------------------------------");
		InputTextarea texte = (InputTextarea) event.getComponent();
		// File f=texte.

		System.out.println(texte.getValue() + "---");
		texte.setSubmittedValue(texte.getValue());
	}

	public void valueChangeHandlerCodeMirror(AjaxBehaviorEvent event) {

		System.out
				.println("---------------------------------------------------------");
		CodeMirror texte = (CodeMirror) event.getComponent();
		// File f=texte.

		System.out.println(texte.getValue() + "---");
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

}

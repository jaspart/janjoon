package com.funder.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.TreeNode;

import com.funder.janjoonweb.service.ConfigManagerAbstract;
import com.funder.janjoonweb.service.FileMap;
import com.funder.janjoonweb.service.GitConfigManager;

@ManagedBean
@ViewScoped
public class JJDevelopment implements Serializable {

	private TabView tabView;
	private static final long serialVersionUID = 1L;
	private ConfigManagerAbstract configManager;
	private String URL;
	private String path;
	private String userName;
	private String password;
	private String type;
	private int activeTabIndex;
	private TreeNode tree;
	private TreeNode selectedTree;
	private List<FileMap> files;
	private File file;

	public JJDevelopment() throws IOException {

		URL = "https://github.com/chemakh/TestRepository.git";
		path = "/home/lazher/git/.git/Repository/TestRepository/.git";
		userName = "";
		password = "";
		configManager = new GitConfigManager(path);

		tree = configManager.listRepositoryContent();
		selectedTree = tree;
		while (selectedTree.getChildCount() != 0) {
			selectedTree = selectedTree.getChildren().get(0);
			System.out.println(selectedTree.getData().getClass());
		}
		file = (File) selectedTree.getData();
		files = new ArrayList<FileMap>();
		try (FileInputStream inputStream = new FileInputStream(file)) {
			String fileTexte = IOUtils.toString(inputStream);
			FileMap filemap = new FileMap(0, file.getName(), fileTexte, file);
			files.add(filemap);
		}

	}

	public ConfigManagerAbstract getConfigManager() {
		return configManager;
	}

	public void setConfigManager() {
		this.configManager = new GitConfigManager(path);
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public void setFiles(List<FileMap> files) {
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

	public String commit()
	{
		//InputTextarea textArea = (InputTextarea) foundComponent;
		//System.out.println(textArea.getValue().toString());
		//files.get(activeTabIndex).setTexte(textArea.getValue().toString());
		for (FileMap fileMap : files) {
			configManager.setFileTexte(fileMap.getFile(), fileMap.getTexte());
			System.out.println(fileMap.getFile().getName() + "  :"
					+ fileMap.getTexte());

		}
		configManager.checkIn("FirstCommit");
		return "/pages/development.jsf?faces-redirect=true";

	}

	public void onSelectTree(NodeSelectEvent event) throws IOException {
		if (event.getTreeNode().getType().equalsIgnoreCase("file")) {
			selectedTree = event.getTreeNode();
			file = (File) selectedTree.getData();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Selected", event.getTreeNode().toString());
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println(selectedTree.toString());
			try (FileInputStream inputStream = new FileInputStream(file)) {

				String fileTexte = IOUtils.toString(inputStream);
				System.out.println(fileTexte);
				if (contains(file) == -1) {
					FileMap filemap = new FileMap(files.size(), file.getName(),
							fileTexte, file);
					files.add(filemap);
				}
				tabView.setActiveIndex(contains(file));

			}
		}

	}

	public void onCloseTab(TabCloseEvent event) {
		files.remove((FileMap) event.getData());
	}

	public void onChangeTab(TabChangeEvent event) {
		FileMap f = (FileMap) event.getData();
		int j = contains(f.getFile());
		System.out.println(f.getTexte());
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

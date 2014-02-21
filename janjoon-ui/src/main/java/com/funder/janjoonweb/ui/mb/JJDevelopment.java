package com.funder.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import com.funder.janjoonweb.service.ConfigManagerAbstract;
import com.funder.janjoonweb.service.GitConfigManager;

@ManagedBean
@ViewScoped
public class JJDevelopment implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigManagerAbstract configManager;	
	private String URL;
	private String path;
	private String userName;
	private String password;
	private String type;
	private String branche;
	private TreeNode tree;
	private TreeNode selectedTree;
	private ArrayList<String> branches;
	private String fileTexte;
	private File file;
	
	public JJDevelopment() {
		
		URL="https://github.com/chemakh/TestRepository.git";
		path="/home/lazher/git/Repository/.git/TestCloneRepository/.git";
		userName="";
		branche="master";
		password="";
		configManager=new GitConfigManager(path);
		branches=configManager.getAllBranches();	
		tree=configManager.listRepositoryContent(branche);	
		selectedTree=tree;
		while(selectedTree.getChildCount()!=0)
		{
			selectedTree=selectedTree.getChildren().get(0);
			System.out.println(selectedTree.getData().getClass());
		}	
		file=(File) selectedTree.getData();
		
		
	}

	
	
	public ArrayList<String> getBranches() {
		return branches;
	}
	
	public void setBranches() {
		this.branches = configManager.getAllBranches();
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
		this.tree = configManager.listRepositoryContent(branche);
	}

	public String getBranche() {
		return branche;
	}

	public void setBranche(String branche) {
		this.branche = branche;
	}	
	
	public String commit () throws IOException
	{
		
		System.out.println("commit "+file.getName()+fileTexte);
		configManager=new GitConfigManager(path);		
		configManager.setFileTexte(file, fileTexte);
		//configManager.checkIn("commit");
		branches=configManager.getAllBranches();
		branche=branches.get(0);
		tree=configManager.listRepositoryContent(branche);	
		selectedTree=tree;
		while(selectedTree.getChildCount()!=0)
		{
			selectedTree=selectedTree.getChildren().get(0);
			System.out.println(selectedTree.getData().getClass());
		}	
		file=(File) selectedTree.getData();
		getFileTexte();
		return "/pages/development.jsf?faces-redirect=true";
		
	}
	
	public String getFileTexte() throws IOException
	{
		return fileTexte;
	}



	public TreeNode getSelectedTree() {
		return selectedTree;
	}



	public void setSelectedTree(TreeNode selectedTree) {
		this.selectedTree = selectedTree;
	}
	
	public void onSelectTree(NodeSelectEvent event) throws IOException 
	{
		if(event.getTreeNode().getType().equalsIgnoreCase("file"))
		{
			selectedTree=event.getTreeNode();
			file=(File) selectedTree.getData();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println(selectedTree.toString());
			try(FileInputStream inputStream = new FileInputStream(file)) 
			{
		        
				fileTexte = IOUtils.toString(inputStream);
				System.out.println(fileTexte);
		        
		    } 
		}	
		
	}
	public void setFileTexte(String fileTexte) {
		this.fileTexte = fileTexte;
	}



	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}
	
	public void handleBrancheChange() {  
        if(branche !=null && !branche.equals(""))  
            tree=configManager.listRepositoryContent(branche); 
        else  
            tree=null;
    }  
		
	

}

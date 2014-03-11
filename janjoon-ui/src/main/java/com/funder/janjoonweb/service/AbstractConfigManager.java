package com.funder.janjoonweb.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.primefaces.model.TreeNode;

public abstract class AbstractConfigManager implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;	
	protected String url;
	protected String path;
	protected String userName,passWord;
	
	public AbstractConfigManager(String type, String url, String path,String name,String mdp)
	{		
		this.type = type;
		this.url = url;
		this.path = path;
		this.userName=name;
		this.passWord=mdp;
	}

	public String getType() {
		return type.toString();
	}

	public void setType(String type) {		
		
		this.type = type;
	}	

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public abstract boolean checkIn(String message);
	
	public abstract boolean checkOut(String branche);
	
	public abstract String cloneRemoteRepository(String url,String name,String path);
	
	public abstract boolean addFile(String Path,String name);
	
	public abstract boolean createRepository (String path);
	
	public abstract TreeNode listRepositoryContent();	
	
	public abstract boolean pushRepository();

	public abstract boolean pullRepository() ;
	
	public abstract ArrayList<String> getAllBranches();
	
	public abstract boolean setFileTexte(File file,String texte);

}

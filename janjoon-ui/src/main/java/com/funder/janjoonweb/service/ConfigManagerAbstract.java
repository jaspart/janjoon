package com.funder.janjoonweb.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.primefaces.model.TreeNode;

public abstract class ConfigManagerAbstract implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;	
	protected Object repository;
	
	public String getType() {
		return type.toString();
	}

	public void setType(String type) {		
		
		this.type = type;
	}	

	
	public abstract boolean checkIn(String message);
	
	public abstract boolean checkOut(String branche);
	
	public abstract String cloneRemoteRepository(String url,String name,String username,String password,String path);
	
	public abstract boolean addFile(String Path,String name);
	
	public abstract boolean createRepository (String path);
	
	public abstract TreeNode listRepositoryContent();	
	
	public abstract boolean pushRepository(String repoPath,String url,String userName,String password);

	public abstract boolean pullRepository(String repoPath, String url, String userName,
			String password) ;
	
	public abstract ArrayList<String> getAllBranches();
	
	public abstract boolean setFileTexte(File file,String texte);

}

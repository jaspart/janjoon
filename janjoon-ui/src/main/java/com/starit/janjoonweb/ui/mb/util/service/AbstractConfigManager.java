package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.primefaces.model.TreeNode;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJTask;

public abstract class AbstractConfigManager implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;	
	protected String url;
	protected String path;	
	protected JJContact jJContact;
	
	public AbstractConfigManager(String type, String url, String path,JJContact jJContact)
	{		
		this.type = type;
		this.url = url;
		this.path = path;
		this.jJContact=jJContact;
		
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
	

	public JJContact getjJContact() {
		return jJContact;
	}

	public void setjJContact(JJContact jJContact) {
		this.jJContact = jJContact;
	}

	public abstract boolean checkIn(String message);
	
	public abstract boolean checkOut(String branche);
	
	public abstract String cloneRemoteRepository(String url,String name,String path);
	
	public abstract boolean addFile(String Path,String name,boolean isFile);
	
	public abstract boolean createRepository (String path);
	
	public abstract TreeNode listRepositoryContent(String version);	
	
	public abstract boolean pushRepository();

	public abstract boolean pullRepository() ;
	
	public abstract ArrayList<String> getAllBranches();
	
	public abstract boolean setFileTexte(File file,String texte);

	

}

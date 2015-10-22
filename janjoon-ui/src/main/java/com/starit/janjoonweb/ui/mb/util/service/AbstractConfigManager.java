package com.starit.janjoonweb.ui.mb.util.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.primefaces.model.TreeNode;

public abstract class AbstractConfigManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;
	protected String url;
	protected String path;
	protected String login;
	protected String password;

	public AbstractConfigManager(String type, String url, String path,
			String login, String password) {
		this.type = type;
		this.url = url;
		this.path = path;
		this.login = login;
		this.password = password;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public abstract boolean checkIn(File file,String message);

	public abstract boolean checkOut(String branche);

	public abstract String cloneRemoteRepository(String url, String name,
			String path);

	public abstract boolean addFile(String Path, String name, boolean isFile);

	public abstract boolean createRepository(String path);

	public abstract TreeNode listRepositoryContent(String version);

	public abstract boolean pushRepository();

	public abstract boolean pullRepository();

	public abstract ArrayList<String> getAllBranches();
	
	public boolean setFileTexte(File file, String texte) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(texte);
			writer.close();
			return true;
		} catch (IOException e) {

			return false;
		}

	}
	
	public static void delete(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}


}

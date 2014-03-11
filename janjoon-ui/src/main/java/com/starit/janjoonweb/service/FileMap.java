package com.starit.janjoonweb.service;

import java.io.File;

public class FileMap {
	private String id;
	private String title;
	private String texte;
	private File file;
	
	public FileMap(String i, String title, String texte, File file) {
		
		this.title = title;
		this.texte = texte;
		this.file = file;
		this.id=i;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTexte() {
		return texte;
	}
	public void setTexte(String texte) {
		this.texte = texte;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}

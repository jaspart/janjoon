package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class FileMap {
	
	
	private String title;
	private String texte;
	private File file;
	private String mode;
	
	
	public FileMap(String title, String texte, File file) throws IOException {
		
		this.title = title;
		this.texte = texte;
		this.file = file;
		this.mode=Files.probeContentType(file.toPath());
		if(this.mode==null)
			this.mode="text";
		if (this.mode.equalsIgnoreCase("application/javascript"))
				this.mode="javascript";
		
		String s=FilenameUtils.getExtension(file.getPath());
		if(s.contains("properties"))
		{
			this.mode=s;
		}
		
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
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}

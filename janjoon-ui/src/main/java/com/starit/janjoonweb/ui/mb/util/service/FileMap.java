package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;

public class FileMap {

	private int		index;
	private String	title;
	private String	texte;
	private boolean	change	= false;
	private File	file;
	private String	mode;

	public FileMap(String title, String texte, File file) throws IOException {

		this.title = title;
		this.texte = texte;
		this.file = file;
		this.mode = Files.probeContentType(file.toPath());
		if (this.mode == null)
			this.mode = "text";
		if (this.mode.equalsIgnoreCase("application/javascript"))
			this.mode = "javascript";

		String s = FilenameUtils.getExtension(file.getPath());
		if (s.contains("properties")) {
			this.mode = s;
		}

	}

	public FileMap() {
		// TODO Auto-generated constructor stub
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
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

	@Override
	public boolean equals(Object object) {
		return (object instanceof FileMap) && (getFile() != null)
		        ? getFile().getAbsolutePath().equals(((FileMap) object).getFile().getAbsolutePath()) : (object == this);
	}

}

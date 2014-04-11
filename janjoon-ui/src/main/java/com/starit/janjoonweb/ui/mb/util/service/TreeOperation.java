package com.starit.janjoonweb.ui.mb.util.service;

import java.io.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

public class TreeOperation {

	private AbstractConfigManager configManager;

	public TreeOperation(AbstractConfigManager configManager) {

		this.configManager = configManager;
	}

	public AbstractConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(AbstractConfigManager configManager) {
		this.configManager = configManager;
	}

	public void deleteFile(File file) {

		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);

					deleteFile(fileDelete);
				}

				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			file.delete();
		}

	}

	public boolean commitFile(File file) {
		return false;
	}

	public boolean addFile(File parent, String name) {
		
		
		return configManager.addFile(parent.getPath(), name, true);
	}

	public boolean addFolder(File parent, String name) {

		return configManager.addFile(parent.getPath(), name, false);
	}

	public boolean uploadFile(File directory,UploadedFile uploadedFile)
	{

		System.out.println("File type: " + uploadedFile.getContentType());
		System.out.println("File name: " + uploadedFile.getFileName());
		System.out.println("File size: " + uploadedFile.getSize() + " bytes");

		String prefix = FilenameUtils.getBaseName(uploadedFile.getFileName());
		String suffix = FilenameUtils.getExtension(uploadedFile.getFileName());

		File file = null;
		OutputStream output = null;

		try {
			file = File.createTempFile(prefix + "_", "." + suffix, directory);
			output = new FileOutputStream(file);
			IOUtils.copy(uploadedFile.getInputstream(), output);			

			return true;
			
		} catch (IOException e) {
			
			if (file != null)
				file.delete();

			e.printStackTrace();
			return false;
			
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

}

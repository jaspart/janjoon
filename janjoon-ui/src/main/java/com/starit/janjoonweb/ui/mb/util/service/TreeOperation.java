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

	public boolean addFile(File parent, String name) {

		return configManager.addFile(parent.getPath(), name, true);
	}

	public boolean addFolder(File parent, String name) {

		return configManager.addFile(parent.getPath(), name, false);
	}

	public boolean uploadFile(File parent, String uploadedFile, InputStream in)
			{
		OutputStream out;
		try {
			out = new FileOutputStream(new File(parent.getPath()
					+"/"+uploadedFile));
			int read = 0;
			byte[] bytes = new byte[1024];
			try {
				while ((read = in.read(bytes)) != -1) {

					out.write(bytes, 0, read);
				}
				in.close();

				out.flush();

				out.close();

				System.out.println("New file created!");
				return addFile(parent, uploadedFile);
			} catch (IOException e) {
				
				e.printStackTrace();
				return false;
			}

			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		}
		

		

	}

}

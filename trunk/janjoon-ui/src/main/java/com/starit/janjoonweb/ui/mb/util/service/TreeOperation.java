package com.starit.janjoonweb.ui.mb.util.service;

import java.io.*;

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

	public boolean addFile(String version, File parent, String name) {
		if (parent != null)
			return configManager.addFile(parent.getPath(), name, true);
		else
			return configManager.addFile(version, name, true);
	}

	public boolean addFolder(String version, File parent, String name) {

		if (parent != null)
			return configManager.addFile(parent.getPath(), name, false);
		else
			return configManager.addFile(version, name, false);
	}

	public static boolean uploadFile(File parent, InputStream in, String name)
			throws IOException {

		OutputStream out = new FileOutputStream(new File(parent.getPath() + "/"
				+ name));
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = in.read(bytes)) != -1) {

			out.write(bytes, 0, read);
		}
		in.close();

		out.flush();

		out.close();

		System.out.println("New file created!");

		return true;

	}

	public boolean uploadFile(String version, File parent, String uploadedFile,
			InputStream in) {
		OutputStream out;
		try {
			if (parent != null)
				out = new FileOutputStream(new File(parent.getPath() + "/"
						+ uploadedFile));
			else
				out = new FileOutputStream(new File("/"
						+ configManager.getPath() + "/" + version + "/"
						+ uploadedFile));
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
				return addFile(version, parent, uploadedFile);
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

package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class LicenseBean implements Serializable {

	private UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		if (file != null) {

			System.out.println("Succesful " + file.getFileName()
					+ " is uploaded.");

		}
	}

	public void handleFileUpload(FileUploadEvent event) {

		try {

			File targetFolder = null;

			String userHome = System.getProperty("user.home");
			String classPath = System.getProperty("java.class.path");

			if (classPath.startsWith(userHome)) {
				String target = userHome + System.getProperty("file.separator")
						+ "license";
				File file = new File(target);
				if (!file.exists()) {

					file.mkdir();
				}
				targetFolder = new File(target);
			} else {
				targetFolder = new File("../license");
			}

			InputStream inputStream = event.getFile().getInputstream();

			OutputStream out = new FileOutputStream(new File(targetFolder,

			event.getFile().getFileName()));

			int read = 0;

			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {

				out.write(bytes, 0, read);

			}

			inputStream.close();

			out.flush();

			out.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
}

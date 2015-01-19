package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@ManagedBean
@SessionScoped
public class LicenseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public void handleContactFileUpload(FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		//
		// File targetFolder = null;
		// String userHome = System.getProperty("user.home");
		// String classPath = System.getProperty("java.class.path");
		// System.out.println(userHome + classPath);
		//
		// String target = userHome + System.getProperty("file.separator")
		// + "ContactPhoto";
		// File file = new File(target);
		// if (!file.exists())
		// file.mkdir();
		//
		// targetFolder = new File(target);
		//
		// try {
		// InputStream inputStream = event.getFile().getInputstream();
		//
		// OutputStream out = new FileOutputStream(new File(targetFolder,
		// contact.getEmail().replace(".","-")));
		// contact.setPicture(contact.getEmail().replace(".","-"));
		//
		// int read = 0;
		// byte[] bytes = new byte[1024];
		// while ((read = inputStream.read(bytes)) != -1) {
		// out.write(bytes, 0, read);
		// }
		//
		// inputStream.close();
		// out.flush();
		// out.close();

		byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();

			contact.setPicture(bFile);
			((LoginBean) LoginBean.findBean("loginBean")).getjJContactService()
					.updateJJContact(contact);
			((LoginBean) LoginBean.findBean("loginBean"))
					.getAuthorisationService().setSession(
							(HttpSession) FacesContext.getCurrentInstance()
									.getExternalContext().getSession(false));			
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_uploded", "Photo");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// convert file into array of bytes
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void handleCompLogoUpload(FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		JJCompany company = ((JJCompanyBean) LoginBean
				.findBean("jJCompanyBean")).getCompanie();
		byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();
			company.setLogo(bFile);

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_uploded", "Logo");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (IOException e) {

			e.printStackTrace();
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_unsuccessfully_uploded", "Logo");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}

	public void handleFileUpload(FileUploadEvent event) {

		File targetFolder = null;
		String userHome = System.getProperty("user.home");
		String classPath = System.getProperty("java.class.path");
		System.out.println(userHome + classPath);

		// if (classPath.startsWith(userHome)) {
		String target = userHome + System.getProperty("file.separator")
				+ "license";
		File file = new File(target);
		if (!file.exists())
			file.mkdir();

		targetFolder = new File(target);
		// } else {
		// targetFolder = new File("../license");
		// }
		try {
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
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_uploded", "Licence");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (IOException e) {

			e.printStackTrace();
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_unsuccessfully_uploded", "Licence");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}
}

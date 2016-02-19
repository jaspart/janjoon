package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@ManagedBean(name = "licenseBean")
@SessionScoped
public class LicenseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UploadedFile file;
	private Part fileUplaod;

	public UploadedFile getFile() {
		return file;
	}

	public Part getFileUplaod() {
		return fileUplaod;
	}

	public void handleCompLogoUpload(final FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		final JJCompany company = ((JJCompanyBean) LoginBean
				.findBean("jJCompanyBean")).getCompanie();
		final byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();
			company.setLogo(bFile);

			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Logo", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (final IOException e) {

			e.printStackTrace();
			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_unsuccessfully_uploded", "Logo", "du");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}

	public void handleContactFileUpload(final FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
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

		final byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();

			contact.setPicture(bFile);
			if (LoginBean.findBean("jJContactBean") == null) {
				final FacesContext fContext = FacesContext.getCurrentInstance();
				final HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);
			((LoginBean) LoginBean.findBean("loginBean"))
					.getAuthorisationService()
					.setSession((HttpSession) FacesContext.getCurrentInstance()
							.getExternalContext().getSession(false));
			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Photo", "e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// convert file into array of bytes
		catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void handleFileUpload(final FileUploadEvent event) {

		File targetFolder = null;
		final String userHome = System.getProperty("user.home");
		final String classPath = System.getProperty("java.class.path");
		System.out.println(userHome + classPath);

		// if (classPath.startsWith(userHome)) {
		final String target = userHome + System.getProperty("file.separator")
				+ "license";
		final File file = new File(target);
		if (!file.exists()) {
			file.mkdir();
		}

		targetFolder = new File(target);
		// } else {
		// targetFolder = new File("../license");
		// }
		try {
			final InputStream inputStream = event.getFile().getInputstream();

			final OutputStream out = new FileOutputStream(new File(targetFolder,

					event.getFile().getFileName()));

			int read = 0;

			final byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {

				out.write(bytes, 0, read);

			}

			inputStream.close();

			out.flush();

			out.close();
			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Licence", "e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (final IOException e) {

			e.printStackTrace();
			final FacesMessage facesMessage = MessageFactory.getMessage(
					"message_unsuccessfully_uploded", "Licence", "de la");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}

	public void handleProdLogoUpload(final FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		final JJProduct product = ((JJProductBean) LoginBean
				.findBean("jJProductBean")).getProductAdmin();
		final byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();
			product.setLogo(bFile);

			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Logo", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (final IOException e) {

			e.printStackTrace();
			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_unsuccessfully_uploded", "Logo", "du");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}

	public void handleProjLogoUpload(final FileUploadEvent event) {

		System.out.println("STARTING_OPERATION");
		final JJProject project = ((JJProjectBean) LoginBean
				.findBean("jJProjectBean")).getProjectAdmin();
		final byte[] bFile = new byte[(int) event.getFile().getSize()];

		InputStream inputStream;
		try {
			inputStream = event.getFile().getInputstream();
			inputStream.read(bFile);
			inputStream.close();
			project.setLogo(bFile);

			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Logo", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} catch (final IOException e) {

			e.printStackTrace();
			final FacesMessage facesMessage = MessageFactory
					.getMessage("message_unsuccessfully_uploded", "Logo", "du");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		}

	}

	public void setFile(final UploadedFile file) {
		this.file = file;
	}

	public void setFileUplaod(final Part fileUplaod) {
		this.fileUplaod = fileUplaod;
	}

	public void upload() {
		if (file != null) {

			System.out.println(
					"Succesful " + file.getFileName() + " is uploaded.");

		}
	}

	public void uploadTest() throws IOException {
		System.out.println("STARTING_OPERATION");
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		final byte[] bFile = new byte[(int) fileUplaod.getSize()];

		InputStream inputStream;
		FacesMessage facesMessage = null;
		try {
			inputStream = fileUplaod.getInputStream();
			inputStream.read(bFile);
			inputStream.close();

			contact.setPicture(bFile);
			if (LoginBean.findBean("jJContactBean") == null) {
				final FacesContext fContext = FacesContext.getCurrentInstance();
				final HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);
			((LoginBean) LoginBean.findBean("loginBean"))
					.getAuthorisationService()
					.setSession((HttpSession) FacesContext.getCurrentInstance()
							.getExternalContext().getSession(false));
			facesMessage = MessageFactory
					.getMessage("message_successfully_uploded", "Photo", "e");

		} catch (final FileNotFoundException e) {

		}
		// convert file into array of bytes
		catch (final IOException e) {

		}

		if (facesMessage == null) {
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Error while File Upload", "Photo");
		}

		((LoginBean) LoginBean.findBean("loginBean"))
				.setFacesMessage(facesMessage);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath()
						+ "/pages/contactConfig.jsf?&faces-redirect=true");

	}
}

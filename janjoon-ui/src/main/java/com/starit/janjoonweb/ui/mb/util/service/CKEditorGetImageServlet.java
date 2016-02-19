package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CKEditorGetImageServlet extends HttpServlet {

	private static final long serialVersionUID = -7570633768412575697L;

	static Logger logger = Logger.getLogger("CKEditorGetImageServlet");
	private static final String ERROR_FILE_DOWNLOAD = "An error occured when trying to get the image with id:";
	private static final String IMAGE_PARAMETER_NAME = "imageId";
	private static final long CACHE_AGE_MILISECONDS_TWO_WEEKS = 1209600000;
	private static final String CKEDITOR_CONTENT_EXPIRE = "Expires";
	private static final String CKEDITOR_CONTENT_TYPE = "Content-Type";
	private static final String CKEDITOR_CONTENT_LENGTH = "Content-Length";
	private static final String CKEDITOR_CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CKEDITOR_CONTENT_DISPOSITION_VALUE = "inline; filename=\"";
	private static final String CKEDITOR_HEADER_NAME = "Cache-Control";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String imageId = null;
		// ApplicationContext ctx = WebApplicationContextUtils
		// .getWebApplicationContext(request.getSession()
		// .getServletContext());
		// UploadedFileService uploadedFileService = (UploadedFileService) ctx
		// .getBean("uploadedFileService");
		String parameterId = request.getParameter(IMAGE_PARAMETER_NAME);

		try {
			imageId = parameterId;
			File uploadedFile = new File(CKEditorUploadServlet.CKEDITOR_DIR
					+ File.separator + imageId);
			if (uploadedFile != null && uploadedFile.getTotalSpace() > 0) {
				byte[] rb = Files.readAllBytes(uploadedFile.toPath());
				long expiry = new Date().getTime()
						+ CACHE_AGE_MILISECONDS_TWO_WEEKS;
				response.setDateHeader(CKEDITOR_CONTENT_EXPIRE, expiry);
				response.setHeader(CKEDITOR_HEADER_NAME,
						"max-age=" + CACHE_AGE_MILISECONDS_TWO_WEEKS);
				response.setHeader(CKEDITOR_CONTENT_TYPE,
						Files.probeContentType(uploadedFile.toPath()));
				response.setHeader(CKEDITOR_CONTENT_LENGTH,
						String.valueOf(rb.length));
				response.setHeader(CKEDITOR_CONTENT_DISPOSITION,
						CKEDITOR_CONTENT_DISPOSITION_VALUE
								+ uploadedFile.getName() + "\"");

				response.getOutputStream().write(rb, 0, rb.length);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e) {
			response.getOutputStream().close();
			logger.error(ERROR_FILE_DOWNLOAD + parameterId, e);
		}

		// imageId =parameterId;
		// File uploadedFile = new
		// File(CKEditorUploadServlet.CKEDITOR_DIR+File.separator+imageId);
		//
		//
		// String serverName = request.getServerName();
		// String janjoon_directory = "";
		// Properties properties = new Properties();
		// boolean contextPath =
		// request.getContextPath().contains("janjoon-ui");
		//
		// if (serverName.contains("localhost") && contextPath) {
		// janjoon_directory = "src" + File.separator + "main"
		// + File.separator + "webapp" + File.separator + "images";
		// } else {
		// ServletContext servletContext = (ServletContext) FacesContext
		// .getCurrentInstance().getExternalContext().getContext();
		// String path = servletContext.getRealPath("WEB-INF" + File.separator
		// + "classes")
		// + File.separator;
		// properties.load(new FileInputStream(path + "email.properties"));
		// janjoon_directory = "lib" + File.separator
		// + properties.getProperty("janjoon.version")
		// + File.separator + "images";
		// }
		//
		// try{
		//
		// File cretedDirectory = new File(janjoon_directory);
		//
		// if (!cretedDirectory.exists())
		// cretedDirectory.mkdirs();
		// //FileUtils.copyFile(uploadedFile, cretedDirectory);
		// Files.copy(uploadedFile.toPath(), cretedDirectory.toPath());
		//
		// String pathToFile="";
		// if(!request.getServerName().contains("localhost"))
		// pathToFile =
		// "https"+"://"+request.getServerName()+request.getContextPath() +
		// "/images/" + uploadedFile.getName();
		// else
		// pathToFile =
		// "http"+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()
		// +"/images/" + uploadedFile.getName();
		//
		// response.sendRedirect(pathToFile);
		// }catch(FileAlreadyExistsException e)
		// {
		// System.err.println("FileAlreadyExistsException");
		// }catch(NoSuchFileException e)
		// {
		// System.err.println("NoSuchFileException");
		// }

	}
}

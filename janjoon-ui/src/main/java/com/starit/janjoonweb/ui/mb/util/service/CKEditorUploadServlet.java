package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class CKEditorUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -7570633768412575697L;

	static Logger logger = Logger.getLogger("CKEditorUploadServlet");
	private static final String ERROR_FILE_UPLOAD = "An error occurred to the file upload process.";
	private static final String ERROR_NO_FILE_UPLOAD = "No file is present for upload process.";
	private static final String ERROR_INVALID_CALLBACK = "Invalid callback.";
	private static final String CKEDITOR_CONTENT_TYPE = "text/html; charset=UTF-8";
	private static final String CKEDITOR_HEADER_NAME = "Cache-Control";
	private static final String CKEDITOR_HEADER_VALUE = "no-cache";
	public static final String CKEDITOR_DIR = "upload" + File.separator
			+ "images";

	private static final Pattern PATTERN = Pattern.compile("[\\w\\d]*");

	private String errorMessage = "";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		File directory = new File(CKEDITOR_DIR);

		if (!directory.exists())
			directory.mkdirs();

		File f = new File(".");
		System.out.println("getCanonicalPath : " + f.getCanonicalPath());

		PrintWriter out = response.getWriter();

		response.setContentType(CKEDITOR_CONTENT_TYPE);
		response.setHeader(CKEDITOR_HEADER_NAME, CKEDITOR_HEADER_VALUE);

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// File uploadedFile = null;
		String imageString = "";
		try {
			List<FileItem> items = upload.parseRequest(request);
			if (!items.isEmpty() && items.get(0) != null) {

				// uploadedFile = new File(CKEDITOR_DIR + File.separator
				// + ((DiskFileItem) items.get(0)).getName());
				// uploadFile(((FileItem) items.get(0)).get(), uploadedFile);

				// imageString = Base64.encodeBase64URLSafeString(((FileItem)
				// items.get(0)).get());

				StringBuilder sb = new StringBuilder();
				sb.append("data:image;base64,");
				sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(
						((FileItem) items.get(0)).get(), false)));
				imageString = sb.toString();

			} else {
				errorMessage = ERROR_NO_FILE_UPLOAD;
			}

		} catch (FileUploadException e) {
			errorMessage = ERROR_FILE_UPLOAD;
			logger.error(errorMessage, e);
		}

		// CKEditorFuncNum Is the location to display when the callback
		String callback = request.getParameter("CKEditorFuncNum");
		// verify if the callback contains only digits and letters in order to
		// avoid vulnerability on parsing parameter
		if (!PATTERN.matcher(callback).matches()) {
			callback = "";
			errorMessage = ERROR_INVALID_CALLBACK;
		}
		// String pathToFile = "";
		// if (!request.getServerName().contains("localhost"))
		// pathToFile = "https" + "://" + request.getServerName()
		// + request.getContextPath()
		// + "/pages/ckeditor/getimage?imageId="
		// + uploadedFile.getName();
		// else
		// pathToFile = "http" + "://" + request.getServerName() + ":"
		// + request.getServerPort() + request.getContextPath()
		// + "/pages/ckeditor/getimage?imageId="
		// + uploadedFile.getName();

		// System.out.println(uploadedFile.getAbsolutePath() + "--" +
		// pathToFile);
		// System.out.println(uploadedFile.getCanonicalPath() + "--" +
		// pathToFile);
		// String pathToFile =uploadedFile.getPath();
		out.println("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("
				+ callback + ",'" + imageString + "','" + errorMessage + "')");
		out.println("</script>");
		out.flush();
		out.close();
		// System.out.println(pathToFile);
	}

	public boolean uploadFile(byte[] bs, File file) {
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bs);
			fos.close();
			return true;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

	}

}

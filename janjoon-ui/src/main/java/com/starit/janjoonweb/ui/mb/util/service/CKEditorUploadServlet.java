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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class CKEditorUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -7570633768412575697L;

	static Logger logger = Logger.getLogger("CKEditorUploadServlet");
	private static final String ERROR_FILE_UPLOAD = "An error occurred to the file upload process.";
	private static final String ERROR_NO_FILE_UPLOAD = "No file is present for upload process.";
	private static final String ERROR_INVALID_CALLBACK = "Invalid callback.";
	private static final String CKEDITOR_CONTENT_TYPE = "text/html; charset=UTF-8";
	private static final String CKEDITOR_HEADER_NAME = "Cache-Control";
	private static final String CKEDITOR_HEADER_VALUE = "no-cache";
	private static final String CKEDITOR_DIR= ".."+File.separator+".."+File.separator+"upload"+File.separator+"images";

	private static final Pattern PATTERN = Pattern.compile("[\\w\\d]*");

	private String errorMessage = "";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().
					getServletContext());
	  
	  File directory=new File(CKEDITOR_DIR);
	  
	  if(!directory.exists())
		  directory.mkdirs();
	 
	
	  PrintWriter out = response.getWriter();
	 
	  response.setContentType(CKEDITOR_CONTENT_TYPE);
	  response.setHeader(CKEDITOR_HEADER_NAME, CKEDITOR_HEADER_VALUE);
	 
	  FileItemFactory factory = new DiskFileItemFactory();
	  ServletFileUpload upload = new ServletFileUpload(factory);
	  File uploadedFile=null;
	  try {
	   List<FileItem> items = upload.parseRequest(request);
	   if (!items.isEmpty() && items.get(0) != null) {
		   
		uploadedFile = new File(CKEDITOR_DIR+File.separator+((DiskFileItem) items.get(0)).getName());
		uploadFile(((FileItem) items.get(0)).get(),uploadedFile);
//	    uploadedFile.setContent((FileItem)items.get(0).get());
//	    uploadedFile.setContentType(items.get(0).getContentType());
//	    uploadedFile.setFileName(((DiskFileItem) items.get(0)).getName());
//	    uploadedFileService.saveUploadedFile(uploadedFile);
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
	  String pathToFile = "https"+"://"+request.getServerName()+request.getContextPath() + "/pages/ckeditor/getimage?imageId=" + uploadedFile.getName();
	  
	  System.out.println(uploadedFile.getPath()+"--"+pathToFile);
	  //String pathToFile =uploadedFile.getPath();
	  out.println("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(" + 
			  callback + ",'" + pathToFile + "','" + errorMessage + "')");
	  out.println("</script>");
	  out.flush();
	  out.close();
	  System.out.println(pathToFile);
	 }
	
	public boolean uploadFile (byte[] bs,File file) 
	{
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

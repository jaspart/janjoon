package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
 
@FacesValidator("fileUploadValidator")
public class FileUploadValidator implements Validator {
 
	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {
 
		Part part = (Part) value;
 
		// 1. validate file name length
		String fileName = getFileName(part);
		System.out.println("----- validator fileName: " + fileName);
		if(fileName.length() == 0 ) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error: File name is invalid !!","");
			throw new ValidatorException(message);
		} else if (fileName.length() > 50) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error: File name is too long !!","");
			throw new ValidatorException(message);
		}
 
//		try {
//			ImageIO.read(part.getInputStream());
//			System.out.println(part.getContentType());
//		} catch (IOException e) {
			
//		}
		// 2. validate file type (only text files allowed)
		System.out.println(part.getContentType());
		String fileType = (String) uiComponent.getAttributes().get("fileType");
		if(fileType.equalsIgnoreCase("image"))
		{
			if (!part.getContentType().contains("image")) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error: File type is invalid !!","");
			throw new ValidatorException(message);
			  }
		}
		
 
		// 3. validate file size (should not be greater than 512 bytes)
		if (part.getSize() > 100000) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error: File size is too big !!","");
			throw new ValidatorException(message);
		}
	}
 
	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		System.out.println("----- validator partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return "";
	}
}

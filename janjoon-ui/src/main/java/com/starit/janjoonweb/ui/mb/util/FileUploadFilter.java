package com.starit.janjoonweb.ui.mb.util;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import javax.faces.context.FacesContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.primefaces.util.Constants;
import org.primefaces.webapp.MultipartRequest;

public class FileUploadFilter implements Filter {

	static Logger				logger					= Logger.getLogger("FileUploadFilter");

	private final static String	THRESHOLD_SIZE_PARAM	= "thresholdSize";

	private final static String	UPLOAD_DIRECTORY_PARAM	= "uploadDirectory";

	private String				thresholdSize;

	private String				uploadDir;

	private boolean				bypass;

	public void init(FilterConfig filterConfig) throws ServletException {
		boolean isAtLeastJSF22 = detectJSF22();
		String uploader = filterConfig.getServletContext().getInitParameter(Constants.ContextParams.UPLOADER);
		if (uploader == null || uploader.equals("auto"))
			bypass = isAtLeastJSF22 ? true : false;
		else if (uploader.equals("native"))
			bypass = true;
		else if (uploader.equals("commons"))
			bypass = false;

		thresholdSize = filterConfig.getInitParameter(THRESHOLD_SIZE_PARAM);
		uploadDir = filterConfig.getInitParameter(UPLOAD_DIRECTORY_PARAM);
		logger.info("FileUploadFilter initiated successfully");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
	        throws IOException, ServletException {
		if (bypass) {
			logger.info("Parsing file upload request bypass");
			filterChain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);

		if (isMultipart) {
			logger.info("Parsing file upload request");

			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			if (thresholdSize != null) {
				diskFileItemFactory.setSizeThreshold(Integer.valueOf(thresholdSize));
			}
			if (uploadDir != null) {
				diskFileItemFactory.setRepository(new File(uploadDir));
			}

			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			MultipartRequest multipartRequest = new MultipartRequest(httpServletRequest, servletFileUpload);

			logger.info(
			        "File upload request parsed succesfully, continuing with filter chain with a wrapped multipart request");

			filterChain.doFilter(multipartRequest, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public void destroy() {

		logger.info("Destroying FileUploadFilter");
	}

	private boolean detectJSF22() {
		String version = FacesContext.class.getPackage().getImplementationVersion();

		if (version != null) {
			return version.startsWith("2.2");
		} else {
			// fallback
			try {
				Class.forName("javax.faces.flow.Flow");
				return true;
			} catch (ClassNotFoundException ex) {
				return false;
			}
		}
	}

}

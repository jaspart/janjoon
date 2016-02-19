package com.starit.janjoonweb.ui.mb.util.itext;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;

@Deprecated
public interface HTMLTagProcessorImpl {

	/**
	 * Implement this class to tell the HTMLWorker what to do when an open tag
	 * is encountered.
	 * 
	 * @param htmlWorkerImpl
	 *            the HTMLWorker
	 * @param tag
	 *            the tag that was encountered
	 * @param attrs
	 *            the current attributes of the tag
	 * @throws DocumentException
	 * @throws IOException
	 */
	public abstract void startElement(HTMLWorkerImpl htmlWorkerImpl, String tag,
			Map<String, String> attrs) throws DocumentException, IOException;

	/**
	 * Implement this class to tell the HTMLWorker what to do when an close tag
	 * is encountered.
	 * 
	 * @param worker
	 *            the HTMLWorker
	 * @param tag
	 *            the tag that was encountered
	 * @throws DocumentException
	 */
	public abstract void endElement(HTMLWorkerImpl worker, String tag)
			throws DocumentException;
}

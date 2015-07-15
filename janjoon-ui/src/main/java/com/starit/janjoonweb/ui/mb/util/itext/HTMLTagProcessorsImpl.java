package com.starit.janjoonweb.ui.mb.util.itext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.simpleparser.TableWrapper;

@SuppressWarnings("deprecation")
@Deprecated
public class HTMLTagProcessorsImpl extends
		HashMap<String, HTMLTagProcessorImpl> {

	/**
	 * Creates a Map containing supported tags.
	 */
	public HTMLTagProcessorsImpl() {
		super();
		put(HtmlTags.A, A);
		put(HtmlTags.B, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.BODY, DIV);
		put(HtmlTags.BR, BR);
		put(HtmlTags.DIV, DIV);
		put(HtmlTags.EM, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.FONT, SPAN);
		put(HtmlTags.H1, H);
		put(HtmlTags.H2, H);
		put(HtmlTags.H3, H);
		put(HtmlTags.H4, H);
		put(HtmlTags.H5, H);
		put(HtmlTags.H6, H);
		put(HtmlTags.HR, HR);
		put(HtmlTags.I, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.IMG, IMG);
		put(HtmlTags.LI, LI);
		put(HtmlTags.OL, UL_OL);
		put(HtmlTags.P, DIV);
		put(HtmlTags.PRE, PRE);
		put(HtmlTags.S, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SPAN, SPAN);
		put(HtmlTags.STRIKE, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.STRONG, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SUB, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SUP, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.TABLE, TABLE);
		put(HtmlTags.TD, TD);
		put(HtmlTags.TH, TD);
		put(HtmlTags.TR, TR);
		put(HtmlTags.U, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.UL, UL_OL);
	}

	/**
	 * Object that processes the following tags: i, em, b, strong, s, strike, u,
	 * sup, sub
	 */
	public static final HTMLTagProcessorImpl EM_STRONG_STRIKE_SUP_SUP = new HTMLTagProcessorImpl() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) {
			tag = mapTag(tag);
			attrs.put(tag, null);
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag) {
			tag = mapTag(tag);
			worker.updateChain(tag);
		}

		/**
		 * Maps em to i, strong to b, and strike to s. This is a convention: the
		 * style parser expects i, b and s.
		 * 
		 * @param tag
		 *            the original tag
		 * @return the mapped tag
		 */
		private String mapTag(String tag) {
			if (HtmlTags.EM.equalsIgnoreCase(tag))
				return HtmlTags.I;
			if (HtmlTags.STRONG.equalsIgnoreCase(tag))
				return HtmlTags.B;
			if (HtmlTags.STRIKE.equalsIgnoreCase(tag))
				return HtmlTags.S;
			return tag;
		}

	};

	/**
	 * Object that processes the a tag.
	 */
	public static final HTMLTagProcessorImpl A = new HTMLTagProcessorImpl() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
			worker.flushContent();
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag) {
			worker.processLink();
			worker.updateChain(tag);
		}
	};

	/**
	 * Object that processes the br tag.
	 */
	public static final HTMLTagProcessorImpl BR = new HTMLTagProcessorImpl() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) {
			worker.newLine();
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag) {
		}

	};

	public static final HTMLTagProcessorImpl UL_OL = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LI);
			worker.setSkipText(true);
			worker.updateChain(tag, attrs);
			;
			worker.pushToStack(worker.createList(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessorImpls#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LI);
			worker.setSkipText(false);
			worker.updateChain(tag);
			worker.processList();
		}

	};

	public static final HTMLTagProcessorImpl HR = new HTMLTagProcessorImpl() {

		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			worker.pushToStack(worker.createLineSeparator(attrs));
		}

		public void endElement(HTMLWorkerImpl worker, String tag) {
		}

	};

	public static final HTMLTagProcessorImpl SPAN = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag) {
			worker.updateChain(tag);
		}

	};

	public static final HTMLTagProcessorImpl H = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(HtmlTags.SIZE)) {
				int v = 7 - Integer.parseInt(tag.substring(1));
				attrs.put(HtmlTags.SIZE, Integer.toString(v));
			}
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
		}

	};

	public static final HTMLTagProcessorImpl LI = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingLI(true);
			worker.updateChain(tag, attrs);
			worker.pushToStack(worker.createListItem());
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			worker.setPendingLI(false);
			worker.setSkipText(true);
			worker.updateChain(tag);
			worker.processListItem();
		}

	};

	public static final HTMLTagProcessorImpl PRE = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(HtmlTags.FACE)) {
				attrs.put(HtmlTags.FACE, "Courier");
			}
			worker.updateChain(tag, attrs);
			worker.setInsidePRE(true);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
			worker.setInsidePRE(false);
		}

	};

	public static final HTMLTagProcessorImpl DIV = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
		}

	};

	public static final HTMLTagProcessorImpl TABLE = new HTMLTagProcessorImpl() {

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			TableWrapper table = new TableWrapper(attrs);
			worker.pushToStack(table);
			worker.pushTableState();
			worker.setPendingTD(false);
			worker.setPendingTR(false);
			worker.setSkipText(true);
			// Table alignment should not affect children elements, thus remove
			attrs.remove(HtmlTags.ALIGN);
			// In case this is a nested table reset colspan and rowspan
			attrs.put(HtmlTags.COLSPAN, "1");
			attrs.put(HtmlTags.ROWSPAN, "1");
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement(HtmlTags.TR);
			worker.updateChain(tag);
			worker.processTable();
			worker.popTableState();
			worker.setSkipText(false);
		}

	};
	public static final HTMLTagProcessorImpl TR = new HTMLTagProcessorImpl() {

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement(tag);
			worker.setSkipText(true);
			worker.setPendingTR(true);
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement(HtmlTags.TD);
			worker.setPendingTR(false);
			worker.updateChain(tag);
			worker.processRow();
			worker.setSkipText(true);
		}

	};
	public static final HTMLTagProcessorImpl TD = new HTMLTagProcessorImpl() {

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingTD(true);
			worker.updateChain(HtmlTags.TD, attrs);
			worker.pushToStack(worker.createCell(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag)
				throws DocumentException {
			worker.carriageReturn();
			worker.setPendingTD(false);
			worker.updateChain(HtmlTags.TD);
			worker.setSkipText(true);
		}

	};

	public static final HTMLTagProcessorImpl IMG = new HTMLTagProcessorImpl() {

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorkerImpl worker, String tag,
				Map<String, String> attrs) throws DocumentException,
				IOException {
			worker.updateChain(tag, attrs);
			worker.processImage(worker.createImage(attrs), attrs);
			worker.updateChain(tag);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl,
		 *      java.lang.String)
		 */
		public void endElement(HTMLWorkerImpl worker, String tag) {
		}

	};

	/** Serial version UID. */
	private static final long serialVersionUID = -959260811961222824L;
}

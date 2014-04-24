package com.starit.janjoonweb.ui.mb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class UsageChecker is used as usage conformity check.
 * 
 * @author StarIT
 * @since 14 Apr. 2014
 */
public class UsageChecker {

	/**
	 * The Constant Field <code>JAXP_SCHEMA_LANGUAGE</code> is used to specify
	 * the jaxp schema language test.
	 */
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	/**
	 * The Constant Field <code>W3C_XML_SCHEMA</code> is used to specify the w3
	 * c_ xm l_ schema test.
	 */
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	static String workingdirectory = "";
	static Document license = null;

	static {
		org.apache.xml.security.Init.init();
	}

	private UsageChecker() {

	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public static boolean validate(final Document document) throws Exception {
		boolean result = false;
		X509Certificate usedCert = null;
		InputStream iStream = null;

		try {
			final String alias = "starit";
			final String keystorePath = "janjoon-base.jar";
			final String keystorpass = "JanJoon2014";

			try {
				final KeyStore keyStore = KeyStore.getInstance("JKS");
				iStream = new FileInputStream(workingdirectory + File.separator
						+ keystorePath);
				keyStore.load(iStream, keystorpass.toCharArray());

				usedCert = (X509Certificate) keyStore.getCertificate(alias);
			} catch (final IOException | KeyStoreException
					| NoSuchAlgorithmException | CertificateException e) {
				throw new IOException("Could not find certificate with alias: "
						+ alias);
			} finally {
				if (iStream != null)
					closeFile(iStream);
			}

			// init xml signature
			org.apache.xml.security.Init.init();

			final NodeList nodeList = document.getDocumentElement()
					.getElementsByTagNameNS(
							javax.xml.crypto.dsig.XMLSignature.XMLNS,
							"Signature");

			final Element signatureElement = (Element) nodeList.item(0);
			if (signatureElement != null) {
				final org.apache.xml.security.signature.XMLSignature signer = new org.apache.xml.security.signature.XMLSignature(
						signatureElement, "");

				final org.apache.xml.security.keys.KeyInfo keyInfo = signer
						.getKeyInfo();

				if (usedCert != null) {
					try {
						result = signer.checkSignatureValue(usedCert);
					} catch (final XMLSignatureException e) {
						throw new Exception(
								"Error to validate RemId signature.");
					}
				} else {
					if (keyInfo.containsKeyValue()) {
						result = signer.checkSignatureValue(keyInfo
								.getPublicKey());
					} else if (keyInfo.containsX509Data()) {
						result = signer.checkSignatureValue(keyInfo
								.getX509Certificate());
					} else {
						throw new Exception("Verification key is not found");
					}
				}
			} else {
				throw new Exception("Document is not signed");
			}
		} catch (Exception e) {
			throw new Exception("Error to validate signature:" + e);
		} finally {
			return result;
		}
	}

	/**
	 * Method to close the file.
	 * 
	 * @param iStream
	 *            : specify the xml request path value.
	 */
	public static void closeFile(InputStream iStream) {
		if (iStream != null) {
			try {
				iStream.close();
			} catch (IOException e) {
				System.out.println("Error to close file, " + e.getMessage());
			}
		}
	}

	public static boolean verifyFile(String filePath) {
		boolean result = false;
		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			result = true;
		}
		return result;
	}

	public static Document readFile(String input) throws IOException,
			SAXException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(
				new FileInputStream(input));
		return doc;
	}

	public static void writeFile(String output, Document doc)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		OutputStream os;
		os = new FileOutputStream(output);
		trans.transform(new DOMSource(doc), new StreamResult(os));
	}

	public static Date getExpiryDate() {
		Date date = null;
		Element root = license.getDocumentElement();
		String expiryDate = root.getElementsByTagName("expires").item(0)
				.getTextContent();
		try {
			date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
					.parse(expiryDate);
		} catch (ParseException e) {
			System.out.println("Exception : " + e);
		}
		return date;
	}

	public static boolean checkExpiryDate() {
		return getExpiryDate().after(new Date());
	}

	public static String getStringData(String data) {
		Element root = license.getDocumentElement();
		String result = root.getElementsByTagName(data).item(0)
				.getTextContent();
		return result;
	}

	public static int getIntData(String data) {
		Element root = license.getDocumentElement();
		String result = root.getElementsByTagName(data).item(0)
				.getTextContent();
		return Integer.parseInt(result);
	}

	public static boolean check() {
		boolean result = false;
		workingdirectory = System.getProperty("user.dir");
		if (!verifyFile(workingdirectory + File.separator + "janjoon-base.jar")) {
			workingdirectory = System.getProperty("user.dir") + File.separator
					+ "src" + File.separator + "run-distrib";
		}
		try {
			license = readFile(workingdirectory + File.separator
					+ "janjoon.lic");
			result = UsageChecker.validate(license);
		} catch (Exception e) {
			System.out.println("problem=" + e);
		}
		return result;
	}

	public static void main(String[] args) {
		check();
	}
}

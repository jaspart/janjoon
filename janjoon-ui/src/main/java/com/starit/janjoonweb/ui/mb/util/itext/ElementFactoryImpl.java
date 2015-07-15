package com.starit.janjoonweb.ui.mb.util.itext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.ElementFactory;
import com.itextpdf.text.html.simpleparser.ImageProvider;

@SuppressWarnings("deprecation")
public class ElementFactoryImpl extends ElementFactory {

	public Image createImage(String src, final Map<String, String> attrs,
			final ChainedProperties chain, final DocListener document,
			final ImageProvider img_provider,
			final HashMap<String, Image> img_store, final String img_baseurl)
			throws DocumentException, IOException {
		Image img = null;
		// getting the image using an image provider
		if (img_provider != null)
			img = img_provider.getImage(src, attrs, chain, document);
		// getting the image from an image store
		if (img == null && img_store != null) {
			Image tim = img_store.get(src);
			if (tim != null)
				img = Image.getInstance(tim);
		}
		if (img != null)
			return img;
		// introducing a base url
		// relative src references only
		if (!src.startsWith("http") && img_baseurl != null) {
			src = img_baseurl + src;
		} else if (img == null && !src.startsWith("http")
				&& !src.startsWith("data")) {
			String path = chain.getProperty(HtmlTags.IMAGEPATH);
			if (path == null)
				path = "";
			src = new File(path, src).getPath();
		}
		if (!src.startsWith("data"))
			img = Image.getInstance(src);
		else {
			String imgData = src.substring(src.indexOf(",") + 1);
			img = Image.getInstance(Base64.decodeBase64(imgData));
		}
		if (img == null)
			return null;

		// for (String key : attrs.keySet()) {
		//
		// System.err.println(key+":::"+attrs.get(key));
		// }
		float actualFontSize = HtmlUtilities.parseLength(
				chain.getProperty(HtmlTags.SIZE),
				HtmlUtilities.DEFAULT_FONT_SIZE);
		if (actualFontSize <= 0f)
			actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
		String width = attrs.get(HtmlTags.WIDTH);
		float widthInPoints = HtmlUtilities.parseLength(width, actualFontSize);
		String height = attrs.get(HtmlTags.HEIGHT);
		float heightInPoints = HtmlUtilities
				.parseLength(height, actualFontSize);
		if (widthInPoints > 0 && heightInPoints > 0) {
			img.scaleAbsolute(widthInPoints, heightInPoints);
		} else if (widthInPoints > 0) {
			heightInPoints = img.getHeight() * widthInPoints / img.getWidth();
			img.scaleAbsolute(widthInPoints, heightInPoints);
		} else if (heightInPoints > 0) {
			widthInPoints = img.getWidth() * heightInPoints / img.getHeight();
			img.scaleAbsolute(widthInPoints, heightInPoints);
		}

		String before = chain.getProperty(HtmlTags.BEFORE);
		if (before != null)
			img.setSpacingBefore(Float.parseFloat(before));
		String after = chain.getProperty(HtmlTags.AFTER);
		if (after != null)
			img.setSpacingAfter(Float.parseFloat(after));
		img.setWidthPercentage(0);
		return img;
	}
}

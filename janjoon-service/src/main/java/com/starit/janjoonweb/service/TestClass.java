package com.starit.janjoonweb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class TestClass {

	public static final String BASE_URI = "https://www.starit.fr/jenkins/job/janjoonCloud/175/api/xml?wrapper=changes&xpath=//changeSet//msg";

	// public static final String PATH_NAME =
	// "/175/api/xml?wrapper=changes&xpath=//changeSet//msg";

	// public static final String PATH_AGE = "/UserInfoService/age/";

	public static void main(String[] args)
			throws NoSuchAlgorithmException, KeyManagementException {

		InputStream is = null;
		try {
			@SuppressWarnings("resource")
			HttpClient httpClient = new DefaultHttpClient();
			// httpClient.

			// CredentialsProvider provider = new BasicCredentialsProvider();
			// UsernamePasswordCredentials credentials = new
			// UsernamePasswordCredentials("lazher", "root1234");
			// provider.setCredentials(AuthScope.ANY, credentials);
			// HttpClient httpClient =
			// HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpGet httpget = new HttpGet(BASE_URI);
			String basic_auth = new String(Base64
					.encodeBase64(("lazher" + ":" + "root1234").getBytes()));
			httpget.addHeader("Authorization", "Basic " + basic_auth);

			HttpResponse httpResponse = httpClient.execute(httpget);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(line);
			}

			// BuildWS.getCommitedTasks(sb.substring(0));
			is.close();
		} catch (Exception e) {
			System.err.println(
					"Buffer Error" + "Error converting result " + e.toString());
		}
	}
}

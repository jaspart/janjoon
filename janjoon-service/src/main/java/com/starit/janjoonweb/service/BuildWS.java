package com.starit.janjoonweb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;

@SuppressWarnings("deprecation")
@Component("buildWS")
@Path("/build")
public class BuildWS {

	public static final String BASE_URI = "https://www.starit.fr/jenkins/job/janjoonCloud/XXX/api/xml?wrapper=changes&xpath=//changeSet//msg";

	public static final String ZIP_URL = "https://www.starit.fr/jenkins/job/janjoonCloud/BUILD_NUMBER/artifact/janjoon-ui/target/janjoon-ui-3.0-REVISION_NUMBER-distrib.zip";
	@Autowired
	private JJBuildService jJBuildService;

	@Autowired
	private JJProductService jJProductService;

	@Autowired
	private JJVersionService jJVersionService;

	@Autowired
	private JJContactService jJContactService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public void setEncoder(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	// @GET
	// @Path("/{param}")
	// @Produces(MediaType.APPLICATION_XML)
	// public JJBuild getMsg(@PathParam("param") String msg) {
	//
	// return jJBuildService.getBuilds(null, null, true).get(0);
	//
	// }

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public String getWebServiceAction(String svnVersion) {

		InputStream is = null;
		try {

			@SuppressWarnings({"resource"})
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = null;
			if (svnVersion != null)
				httpget = new HttpGet(BASE_URI.replace("XXX", svnVersion));
			else
				httpget = new HttpGet(BASE_URI.replace("XXX", "175"));
			String basic_auth = new String(Base64
					.encodeBase64(("lazher" + ":" + "root1234").getBytes()));
			httpget.addHeader("Authorization", "Basic " + basic_auth);

			HttpResponse httpResponse = httpClient.execute(httpget);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			return sb.toString().replace("<changes>", " ").replace("<msg>", " ")
					.replace("</changes>", " ").replace("</msg>", " ");
		} catch (Exception e) {
			System.err.println(
					"Buffer Error" + "Error converting result " + e.toString());
			return null;
		}

	}

	@POST
	@Path("createBuild")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createBuild(MultivaluedMap<String, String> personParams) {

		String login = personParams.getFirst("mail");
		String password = personParams.getFirst("password");

		if (login != null && password != null) {
			JJContact contact = jJContactService.getContactByEmail(login, true);
			password = password.trim();
			if (contact == null) {
				return "user not Found";
			} else if (!encoder.matches(password, contact.getPassword())) {
				return "Wrong Password";
			} else {
				String productName = personParams.getFirst("product");
				if (productName == null)
					return "Null Product  Name";
				else {
					JJProduct product = jJProductService
							.getJJProductWithName(productName);
					if (product == null)
						return "Product Not Found";
					else {
						String versionName = personParams.getFirst("version");
						if (versionName == null)
							return "Null Version  Name";
						else {
							JJVersion version = jJVersionService
									.getVersionByName(versionName, product);
							if (version == null)
								return "Version Not Found";
							else {
								String buildName = personParams
										.getFirst("build");
								if (buildName == null)
									return "Null Build  Name";
								else {
									if (jJBuildService.getBuildByName(version,
											buildName) != null)
										return "Build " + buildName
												+ " already exist for product="
												+ productName + " and  version="
												+ versionName;
									else {
										String svnVersion = personParams
												.getFirst("svnVersion");
										String buildNumber = buildName
												.substring(4);
										JJBuild b = new JJBuild();
										b.setName(buildName);
										b.setEnabled(true);
										b.setVersion(version);
										b.setDescription("Build for Version "
												+ version.getName());

										b.setDescription(b.getDescription()
												+ System.getProperty(
														"line.separator")
												+ "[URL]="
												+ ZIP_URL
														.replace("BUILD_NUMBER",
																svnVersion)
														.replace(
																"REVISION_NUMBER",
																buildNumber));
										b.setCreatedBy(contact);
										b.setCreationDate(new Date());
										String commitMessage = getWebServiceAction(
												svnVersion);
										if (commitMessage != null) {
											List<JJTask> tasks = getCommitedTasks(
													commitMessage);
											if (tasks != null)
												b.setTasks(new HashSet<JJTask>(
														tasks));
										}
										jJBuildService.saveJJBuild(b);
										return "Build " + buildName
												+ " has been created for product="
												+ productName + " and  version="
												+ versionName;
									}
								}
							}

						}
					}

				}

			}
		} else {
			return "null Credentials";
		}

	}

	public static int nthOccurrence(String str, String c, int n) {
		int pos = str.indexOf(c, 0);
		while (n-- > 0 && pos != -1)
			pos = str.indexOf(c, pos + 1);
		return pos;
	}

	public String getSubString(String s, int index, String c) {
		String[] temp = s.split(c);
		return temp[index];
	}

	public List<JJTask> getCommitedTasks(String commitMessage) {
		int imgNumber = Math.min(
				StringUtils.countOccurrencesOf(commitMessage, "]-"),
				StringUtils.countOccurrencesOf(commitMessage, "TASK["));
		if (imgNumber > 0) {

			int k = 0;
			List<Long> ids = new ArrayList<Long>();
			List<JJTask> tasks = new ArrayList<JJTask>();
			while (k < imgNumber) {
				System.out.println(commitMessage.substring(
						nthOccurrence(commitMessage, "TASK[", k) + 5,
						nthOccurrence(commitMessage, "]-", k)));
				String id = commitMessage.substring(
						nthOccurrence(commitMessage, "TASK[", k) + 5,
						nthOccurrence(commitMessage, "]-", k));
				System.err.println(id);
				try {
					ids.add(Long.parseLong(id));
					JJTask task = jJTaskService.findJJTask(Long.parseLong(id));
					if (task != null) {
						tasks.add(task);
					}

				} catch (NumberFormatException e) {
					System.err.println("NumberFormatException");
				}

				k++;
			}
			if (tasks.isEmpty())
				return null;
			else
				return tasks;
		} else
			return null;
	}

}

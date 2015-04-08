package com.starit.janjoonweb.ui.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.lowagie.text.html.simpleparser.StyleSheet;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.UsageChecker;
import com.starit.janjoonweb.ui.mb.util.service.CKEditorUploadServlet;
import com.starit.janjoonweb.ui.security.AuthorisationService;
import com.sun.faces.component.visit.FullVisitContext;

@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AuthenticationManager authenticationManager;
	private Integer messageCount;
	private AuthorisationService authorisationService;
	static Logger logger = Logger.getLogger("loginBean-Logger");
	private boolean mobile;
	private String showMarquee;
	private FacesMessage facesMessage;

	@Autowired
	private JJContactService jJContactService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJCompanyService jJCompanyService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJConfigurationService jJConfigurationService;

	@Autowired
	private JJMessageService jJMessageService;

	private String username = "";
	private String password;
	private boolean agreeTerms = false;
	private boolean loading = false;
	private boolean loadMain = false;
	private JJContact contact;
	private boolean enable = false;
	private int activeTabAdminIndex;
	private int activeTabProjectIndex;
	private int menuIndex;
	private JJConfiguration planingTabsConf;

	@Autowired
	public LoginBean(AuthenticationManager authenticationManager) {

		this.authenticationManager = authenticationManager;
		this.mobile = (((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getHeader("User-Agent")
				.indexOf("Mobile")) != -1;
	}

	public AuthorisationService getAuthorisationService() {
		return authorisationService;
	}

	public void setAuthorisationService(
			AuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public JJContactService getjJContactService() {
		return jJContactService;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	public boolean isEnable() {

		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean getAgreeTerms() {
		return agreeTerms;
	}

	public void setAgreeTerms(boolean agreeTerms) {
		this.agreeTerms = agreeTerms;

	}

	public FacesMessage getFacesMessage() {
		return facesMessage;
	}

	public void setFacesMessage(FacesMessage facesMessage) {
		this.facesMessage = facesMessage;
	}

	protected String getRedirectUrl(HttpSession session) {
		if (session != null) {
			SavedRequest savedRequest = (SavedRequest) session
					.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			if (savedRequest != null) {
				String s = savedRequest.getRedirectUrl();

				s = s.substring(s.lastIndexOf("/") + 1);
				int index = s.indexOf(".");
				if (index != -1) {
					s = s.replace(s.substring(index), "");
				}

				if (s.contains("development") || s.contains("login"))
					s = "main";
				else if(getProject() == null && getProduct() == null && s.contains("delivery"))
				{
					s = "main";
					facesMessage = MessageFactory.getMessage(
							"dev.nullProject.label",
							FacesMessage.SEVERITY_ERROR, "");
				}
				else if (s.contains("planning") || s.contains("test")
						|| s.contains("stats")) {

					if (getProject() == null) {
						s = "main";
						facesMessage = MessageFactory.getMessage(
								"dev.nullProject.label",
								FacesMessage.SEVERITY_ERROR, "");
					}

				} else if (savedRequest.getParameterMap() != null
						&& !savedRequest.getParameterMap().isEmpty()) {
					s = s + ".jsf?";
					Iterator<Entry<String, String[]>> it = savedRequest
							.getParameterMap().entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						for (String st : (String[]) pairs.getValue()) {
							System.err.println(pairs.getKey() + ":" + st);
							s = s + pairs.getKey() + "=" + st + "&";
						}
					}
					s = s.substring(0, s.length() - 1);
					System.out.println(s);
				}
				return s;
			}
		}

		return "main";
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JJContact getContact() {

		if (enable && !username.isEmpty()) {
			contact = jJContactService.getContactByEmail(username, true);
		} else
			contact = null;

		return contact;
	}

	public void setContact(JJContact contact) {

		this.contact = contact;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public boolean isLoadMain() {
		return loadMain;
	}

	public void setLoadMain(boolean loadMain) {
		this.loadMain = loadMain;
	}

	public int getActiveTabAdminIndex() {
		return activeTabAdminIndex;
	}

	public void setActiveTabAdminIndex(int activeTabAdminIndex) {
		this.activeTabAdminIndex = activeTabAdminIndex;
	}

	public int getActiveTabProjectIndex() {
		return activeTabProjectIndex;
	}

	public void setActiveTabProjectIndex(int activeTabProjectIndex) {
		this.activeTabProjectIndex = activeTabProjectIndex;
	}

	public int getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(int menuIndex) {

		this.menuIndex = menuIndex;
	}

	public JJConfiguration getPlaningTabsConf() {
		if (planingTabsConf == null) {
			List<JJConfiguration> conf = jJConfigurationService
					.getConfigurations("planning", "project.type", true);
			if (conf != null && !conf.isEmpty())
				planingTabsConf = conf.get(0);
			else {
				JJConfiguration configuration = new JJConfiguration();
				configuration.setName("planning");
				configuration
						.setDescription("specify available tab in planing vue");
				configuration.setCreatedBy(getContact());
				configuration.setCreationDate(new Date());
				configuration.setEnabled(true);
				configuration.setParam("project.type");
				configuration.setVal("gantt,scrum");
				jJConfigurationService.saveJJConfiguration(configuration);
				planingTabsConf = jJConfigurationService.getConfigurations(
						"planning", "project.type", true).get(0);
			}

		}
		return planingTabsConf;
	}

	public void setPlaningTabsConf(JJConfiguration planingTabsConf) {
		this.planingTabsConf = planingTabsConf;
	}

	public boolean isMobile() {

		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	public String getShowMarquee() {

		if (showMarquee == null)
			if (jJConfigurationService.getConfigurations("MarqueeAlertMessage",
					null, true).isEmpty()) {
				JJConfiguration config = new JJConfiguration();
				config.setName("MarqueeAlertMessage");
				config.setEnabled(true);
				config.setCreatedBy(getContact());
				config.setCreationDate(new Date());
				config.setDescription("Marquee Alert Message configuration");
				config.setVal("true");
				config.setParam("header.showMarquee");
				jJConfigurationService.saveJJConfiguration(config);
				showMarquee = "true";
			} else {
				if (jJConfigurationService
						.getConfigurations("MarqueeAlertMessage", null, true)
						.get(0).getVal().equalsIgnoreCase("true"))
					showMarquee = "true";
				else
					showMarquee = "false";
			}
		return showMarquee;
	}

	public void setShowMarquee(String showMarquee) {
		this.showMarquee = showMarquee;
	}

	public String getColumns() {
		if (mobile)
			return "3";
		else
			return "2";
	}

	public Integer getMessageCount() {

		if (messageCount == null)
			messageCount = jJMessageService.getMessagesCount(getProject(),
					getProduct(), getContact().getCompany(), getContact());

		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

	public String logout() {
		FacesContext fContext = FacesContext.getCurrentInstance();
		logger.info("Contact logged out");
		HttpSession session = (HttpSession) fContext.getExternalContext()
				.getSession(false);
		session.invalidate();
		SecurityContextHolder.clearContext();
		return "loggedout";

	}

	@SuppressWarnings("deprecation")
	public String login() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);
		String prevPage = "";
		try {
			Authentication authentication = authenticationManager
					.authenticate(token);
			SecurityContext sContext = SecurityContextHolder.getContext();
			sContext.setAuthentication(authentication);
			enable = true;

		} catch (AuthenticationException loginError) {

			FacesMessage message = MessageFactory.getMessage("login_invalid_credentials",
					FacesMessage.SEVERITY_ERROR, loginError.getMessage());			
			FacesContext.getCurrentInstance().addMessage("login", message);

			enable = false;
			prevPage = "fail";
		}
		if (enable) {

			if (UsageChecker.check()) {
				contact = jJContactService.getContactByEmail(username, true);
				contact = jJContactService.findJJContact(contact.getId());

				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);

				session.putValue("password", password);

				logger.info("login operation success " + contact.getName()
						+ " logged in");
				if (session.getAttribute("jJProjectBean") == null)
					session.setAttribute("jJProjectBean", new JJProjectBean());

				if (session.getAttribute("jJProductBean") == null)
					session.setAttribute("jJProductBean", new JJProductBean());

				if (session.getAttribute("jJVersionBean") == null)
					session.setAttribute("jJVersionBean", new JJVersionBean());

				JJProjectBean jjProjectBean = (JJProjectBean) session
						.getAttribute("jJProjectBean");
				JJProductBean jjProductBean = (JJProductBean) session
						.getAttribute("jJProductBean");
				JJVersionBean jjVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");

				boolean save = false;

				if (contact.getCategories() == null
						|| contact.getCategories().isEmpty()) {
					save = true;
					contact.setCategories(new HashSet<JJCategory>(
							jJPermissionService.getDefaultCategories(contact)));

				}

				if (contact.getLastProject() == null) {
					save = true;
					contact.setLastProject(jJPermissionService
							.getDefaultProject(contact));
				}

				if (contact.getLastProduct() == null) {
					save = true;
					contact.setLastProduct(jJPermissionService
							.getDefaultProduct(contact));
				}
				if (save) {
					jJContactService.updateJJContact(contact);
					contact = jJContactService
							.getContactByEmail(username, true);
				}

				jjProjectBean.getProjectList();
				if (jjProjectBean.getProjectList().contains(
						contact.getLastProject()))
					jjProjectBean.setProject(contact.getLastProject());

				jjProductBean.getProductList();
				if (jjProductBean.getProductList().contains(
						contact.getLastProduct()))
					jjProductBean.setProduct(contact.getLastProduct());

				jjVersionBean.getVersionList();
				if (jjVersionBean.getVersionList().contains(
						contact.getLastVersion()))
					jjVersionBean.setVersion(contact.getLastVersion());

				authorisationService = new AuthorisationService(session,
						contact);	
				
				
				facesMessage = MessageFactory.getMessage("login_welcome_message",
						FacesMessage.SEVERITY_INFO, getContact().getName());	

				if (!UsageChecker.checkExpiryDate()) {				
					facesMessage = MessageFactory.getMessage("login_licence_expired",
							FacesMessage.SEVERITY_WARN);	
				}

				prevPage = getRedirectUrl(session);
			} else {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.invalidate();
				SecurityContextHolder.clearContext();				
				facesMessage = MessageFactory.getMessage("login_licence_notCorrect",
						FacesMessage.SEVERITY_ERROR);	

				prevPage = "fail";
			}

		}
		try {
			return prevPage;

		} catch (Exception e) {
			System.err.println(e.getMessage() + prevPage);
			prevPage = "main";
			return prevPage;
		}

	}

	public void initMenuIndexvalue(ComponentSystemEvent e) {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath();
		String view = viewId.replace(path, "");
		view = view.replace("/pages/", "");
		view = view.replace(".jsf?faces-redirect=true", "");
		view = view.replace(".jsf", "");
		view = view.replace(".xhtml", "");

		switch (view) {
		case "main":
			menuIndex = 0;
			break;
		case "planning":
			menuIndex = 1;
			break;
		case "specifications":
			menuIndex = 2;
			break;
		case "bugs":
			menuIndex = 3;
			break;
		case "development":
			menuIndex = 4;
			break;
		case "test":
			menuIndex = 5;
			break;
		case "delivery":
			menuIndex = 6;
			break;
		case "teams":
			menuIndex = 7;
			break;
		case "stats":
			menuIndex = 8;
			break;
		case "administration":
			menuIndex = 9;
			break;

		default:
			menuIndex = 0;
			break;
		}
		// menuIndex++;

	}

	public static Object findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		if (session.getAttribute(beanName) != null)
			return session.getAttribute(beanName);
		else
			return context.getApplication().evaluateExpressionGet(context,
					"#{" + beanName + "}", Object.class);

	}

	public void onTabAdminChange(TabChangeEvent event) {

		TabView tv = (TabView) event.getComponent();
		System.out.println(this.activeTabAdminIndex);
		this.activeTabAdminIndex = tv.getChildren().indexOf(event.getTab());
		System.out.println("###### ACtive tab: " + activeTabAdminIndex);

	}

	public void onTabProjectChange(TabChangeEvent event) {
		TabView tv = (TabView) event.getComponent();
		this.activeTabProjectIndex = tv.getChildren().indexOf(event.getTab());
		if (activeTabProjectIndex == 0) {
			JJTaskBean jJTaskBean = (JJTaskBean) findBean("jJTaskBean");
			jJTaskBean.loadData();
			System.out.println("jJTaskBean");
		}
		System.out.println("###### ACtive tab: " + activeTabProjectIndex);

	}

	public void changeEvent(ValueChangeEvent event) throws IOException {

		if (enable) {
			FacesContext ctx = FacesContext.getCurrentInstance();
			String viewId = ctx.getViewRoot().getViewId();

			JJVersionBean jJVersionBean = (JJVersionBean) findBean("jJVersionBean");
			JJProjectBean jJProjectBean = (JJProjectBean) findBean("jJProjectBean");

			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(false);
			if (session.getAttribute("jJTestcaseBean") != null) {
				JJCategory cat = ((JJTestcaseBean) session
						.getAttribute("jJTestcaseBean")).getCategory();
				session.setAttribute("jJTestcaseBean", new JJTestcaseBean());
				((JJTestcaseBean) session.getAttribute("jJTestcaseBean"))
						.setCategory(cat);
			}
			
			messageCount = null;
			session.setAttribute("jJBugBean", new JJBugBean());
			session.setAttribute("jJMessageBean", null);
			session.setAttribute("jJRequirementBean", null);
			
			if(session.getAttribute("jJBuildBean") != null)
			{
				((JJBuildBean)session.getAttribute("jJBuildBean")).
					setBuildDataModelList(null);
			}

			if (event != null) {
				if (session.getAttribute("requirementBean") != null) {
					((RequirementBean) session.getAttribute("requirementBean"))
							.setRootNode(null);
					((RequirementBean) session.getAttribute("requirementBean"))
							.setRequirement(null);
				}

				if (event.getComponent().getClientId()
						.contains("projectSelectOneMenu")) {

					JJProject project = (JJProject) event.getNewValue();
					jJProjectBean.setProject(project);
					// authorisationService.initFields();
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());

				} else if (event.getComponent().getClientId()
						.contains("productSelectOneMenu")) {
					JJProductBean productBean = (JJProductBean) findBean("jJProductBean");
					productBean.setProduct((JJProduct) event.getNewValue());
					session.setAttribute("jJTaskBean", new JJTaskBean());
					session.setAttribute("jJSprintBean", new JJSprintBean());
					if (session.getAttribute("jJStatusBean") != null)
						((JJStatusBean) session.getAttribute("jJStatusBean"))
								.setFirst(true);

				} else if (event.getComponent().getClientId()
						.contains("versionSelectOneMenu")) {
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion((JJVersion) event.getNewValue());
					if (session.getAttribute("jJStatusBean") != null)
						((JJStatusBean) session.getAttribute("jJStatusBean"))
								.setFirst(true);
				}

				if (viewId.contains("development")) {

					RequestContext context = RequestContext
							.getCurrentInstance();

					DevelopmentBean jJDevelopment = (DevelopmentBean) session
							.getAttribute("jJDevelopment");

					if (event.getComponent().getClientId()
							.contains("productSelectOneMenu")) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"dev.nullVersion.label",
								FacesMessage.SEVERITY_ERROR, "");
						FacesContext.getCurrentInstance().addMessage(null,
								facesMessage);

					} else if (event.getComponent().getClientId()
							.contains("versionSelectOneMenu")) {
						jJVersionBean.setVersion((JJVersion) event
								.getNewValue());
						if (jJVersionBean.getVersion() != null) {

							jJDevelopment.reloadRepository();

							if (jJDevelopment.isRender()) {

								context.update(":contentPanel:devPanel:form");
								context.update(":contentPanel:errorPanel");
								context.update(":growlForm");
							} else {
								context.update(":contentPanel:devPanel");
								context.update(":contentPanel:errorPanel");
								context.update(":growlForm");
							}
						} else {
							FacesMessage facesMessage = MessageFactory
									.getMessage("dev.nullVersion.label",
											FacesMessage.SEVERITY_ERROR, "");
							FacesContext.getCurrentInstance().addMessage(null,
									facesMessage);
						}

					} else {
						jJProjectBean.setProject((JJProject) event
								.getNewValue());
						if (jJDevelopment.isRender()) {
							jJDevelopment
									.setProject(jJProjectBean.getProject());
							jJDevelopment.setTasks(null);
							jJDevelopment.setTask(null);
							context.update(":contentPanel:devPanel:form:taskSelectOneMenu");
						}
					}
				} else if (viewId.contains("specifications")) {

					JJRequirementBean requirementBean = (JJRequirementBean) findBean("jJRequirementBean");
					if (requirementBean == null)
						requirementBean = new JJRequirementBean();
					if (requirementBean.getTableDataModelList() == null)
						requirementBean.fullTableDataModelList();

					ExternalContext ec = FacesContext.getCurrentInstance()
							.getExternalContext();
					ec.redirect(((HttpServletRequest) ec.getRequest())
							.getRequestURI());

				}

			}
			authorisationService = null;

		}

	}

	// public void messageListener(HttpSession session) {
	//
	// JJMessageBean messageBean = (JJMessageBean) session
	// .getAttribute("jJMessageBean");
	// boolean messPanel = collapsedMesPanel;
	// boolean appPanel = !messageBean.isCollapsedLayoutPanel();
	// messageBean = new JJMessageBean();
	// RequestContext context = RequestContext.getCurrentInstance();
	//
	// if (messPanel) {
	// setCollapsedMesPanel(false);
	// context.update(":messagePanel");
	// context.update(":menuPanel");
	// context.update(":contentPanel");
	// context.update(":applicatinPanelGrid");
	//
	// }
	//
	// if (appPanel) {
	// messageBean.setCollapsedLayoutPanel(false);
	// context.update(":menuPanel");
	// context.update(":contentPanel");
	// context.update(":messagePanel");
	//
	// }
	//
	// session.setAttribute("jJMessageBean", messageBean);
	//
	// }

	public void loadingPage(ComponentSystemEvent e) throws IOException {

		if (!loading) {
			loading = true;
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							path + "/pages/development.jsf?faces-redirect=true");

		}
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		DevelopmentBean jJDevelopment = (DevelopmentBean) session
				.getAttribute("jJDevelopment");

		if (jJDevelopment != null) {
			if (!jJDevelopment.isInit()) {
				jJDevelopment.initJJDevlopment();
			}
		}

	}

	// public void loginRedirect(ComponentSystemEvent e) throws IOException {
	//
	// if (enable) {
	// String path = FacesContext.getCurrentInstance()
	// .getExternalContext().getRequestContextPath();
	//
	// FacesContext.getCurrentInstance().getExternalContext()
	// .redirect(path + "/pages/main.jsf?faces-redirect=true");
	// }
	// }

	public void loadingMain(ComponentSystemEvent e) throws IOException {

		if (enable) {

			if (!loadMain) {
				loadMain = true;
				String path = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestContextPath();

				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								path
										+ FacesContext.getCurrentInstance()
												.getViewRoot().getViewId()
												.replace(".xhtml", ".jsf"));
			}

			String viewId = FacesContext.getCurrentInstance().getViewRoot()
					.getViewId();
			if (!viewId.contains("development")) {
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				DevelopmentBean jJDevelopment = (DevelopmentBean) session
						.getAttribute("jJDevelopment");
				if (jJDevelopment != null) {
					if (jJDevelopment.isInit()) {
						session.setAttribute("jJDevelopment",
								new DevelopmentBean(jJDevelopment));
					}
				}
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('blockUIWidget').unblock()");

			}
			String previos = getPreviousPage();

			if (viewId.contains("administration")
					&& !previos.contains("administration")) {

				if (activeTabAdminIndex == 0 || activeTabAdminIndex == 1) {

					if (!authorisationService.isAdminContact()) {
						activeTabAdminIndex = 5;

						RequestContext context = RequestContext
								.getCurrentInstance();
						context.execute("PF('AdmintabView').select("
								+ activeTabAdminIndex + ")");
					}
				} else if (activeTabAdminIndex == 2) {
					if (!authorisationService.isAdminCompany())
						activeTabAdminIndex = 5;
					RequestContext context = RequestContext
							.getCurrentInstance();
					context.execute("PF('AdmintabView').select("
							+ activeTabAdminIndex + ")");
				} else if (activeTabAdminIndex == 3) {
					if (!authorisationService.isAdminProduct())
						activeTabAdminIndex = 5;
					RequestContext context = RequestContext
							.getCurrentInstance();
					context.execute("PF('AdmintabView').select("
							+ activeTabAdminIndex + ")");
				} else if (activeTabAdminIndex == 4) {
					if (!authorisationService.isAdminProject())
						activeTabAdminIndex = 5;
					RequestContext context = RequestContext
							.getCurrentInstance();
					context.execute("PF('AdmintabView').select("
							+ activeTabAdminIndex + ")");
				}

			}

		}
	}

	public void redirection(ActionEvent e) throws IOException {

		if (getProject() == null) {
			FacesMessage message = MessageFactory.getMessage(
					"dev.nullProject.label", FacesMessage.SEVERITY_ERROR, "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().execute(
					"PF('blockUIWidget').unblock()");
		} else {
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();
			if (e.getComponent().getId().equalsIgnoreCase("proAction")) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								path
										+ "/pages/planning.jsf?faces-redirect=true");
			} else if (e.getComponent().getId().equalsIgnoreCase("testAction")) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/test.jsf?faces-redirect=true");
			} else if (e.getComponent().getId().equalsIgnoreCase("statsAction")) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(path + "/pages/stats.jsf?faces-redirect=true");
			}
		}

	}

	public void redirectToDev(ActionEvent e) throws IOException {

		if (getVersion() != null && getProduct() != null
				&& getProject() != null) {

			if (!FacesContext.getCurrentInstance().getViewRoot().getViewId()
					.contains("development")) {

				String path = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestContextPath();
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								path
										+ "/pages/development.jsf?faces-redirect=true");
			} else {

				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("updateHeader()");

			}
		} else {

			if (getProduct() == null) {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullProduct.label", FacesMessage.SEVERITY_ERROR,
						"");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else if (getProject() == null) {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullProject.label", FacesMessage.SEVERITY_ERROR,"Project");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullVersion.label", FacesMessage.SEVERITY_ERROR,"");
				FacesContext.getCurrentInstance().addMessage(null, message);

			}
		}
	}

	public void checkAuthorities(ComponentSystemEvent e) throws IOException {

		if (enable) {

			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot root = context.getViewRoot();

			if (authorisationService == null)
				authorisationService = new AuthorisationService(
						(HttpSession) FacesContext.getCurrentInstance()
								.getExternalContext().getSession(false),
						getContact());

			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			if (getProduct() == null
					&& root.getViewId().contains("development")) {
				facesMessage = MessageFactory.getMessage(
						"dev.nullProduct.label", FacesMessage.SEVERITY_ERROR,
						"Project");
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/main.jsf?faces-redirect=true");

			} else if (getProject() == null
					&& (root.getViewId().contains("planning")
							|| root.getViewId().contains("test")
							|| root.getViewId().contains("stats") || root
							.getViewId().contains("development"))) {
				facesMessage = MessageFactory.getMessage(
						"dev.nullProject.label", FacesMessage.SEVERITY_ERROR,
						"Project");
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/main.jsf?faces-redirect=true");

			} else if (getProject() == null && getProduct() == null && root.getViewId().contains("delivery")) {
				
				facesMessage = MessageFactory.getMessage(
						"dev.nullProject.label", FacesMessage.SEVERITY_ERROR,
						"Project");
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/main.jsf?faces-redirect=true");
				
			} else {
				String previos = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestHeaderMap()
						.get("referer");
				String viewID = FacesContext.getCurrentInstance().getViewRoot()
						.getViewId();

				if (previos != null && viewID != null) {
					if (!previos.contains(viewID.replace(".xhtml", ".jsf"))) {
						if (viewID.contains("specifications")) {
							if (authorisationService.isrRequiement()) {

								JJRequirementBean requirementBean = (JJRequirementBean) findBean("jJRequirementBean");
								if (requirementBean == null)
									requirementBean = new JJRequirementBean();
								if (requirementBean.getTableDataModelList() == null) {
									requirementBean.fullTableDataModelList();
									ExternalContext ec = FacesContext
											.getCurrentInstance()
											.getExternalContext();
									ec.redirect(((HttpServletRequest) ec
											.getRequest()).getRequestURI());
								}

							} else {
								facesMessage = MessageFactory.getMessage(
										"header_noPermission_menuitem",
										FacesMessage.SEVERITY_ERROR, "");

								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");
							}

						} else if (viewID.contains("development")) {
							if (authorisationService.isRwDev())
								loadingPage(e);
							else {
								facesMessage = MessageFactory.getMessage(
										"header_noPermission_menuitem",
										FacesMessage.SEVERITY_ERROR, "");

								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");

							}

						} else if ((!authorisationService.isrBug() && root
								.getViewId().contains("bug"))
								|| ((!authorisationService.isrProject()) && (root
										.getViewId().contains("planning") || root
										.getViewId().contains("stats")))
								|| (!authorisationService.isrBuild() && viewID
										.contains("delivery"))
								|| ((!authorisationService.isrTest()) && viewID
										.contains("test"))
								|| (!authorisationService.isrContact() && root
										.getViewId().contains("team"))
								|| (!authorisationService.isRenderAdmin() && root
										.getViewId().contains("administration"))) {

							facesMessage = MessageFactory.getMessage(
									"header_noPermission_menuitem",
									FacesMessage.SEVERITY_ERROR, "");

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");
						} else if (viewID.contains("requirement")) {
							if (!authorisationService.isrRequiement()) {

								facesMessage = MessageFactory.getMessage(
										"header_noPermission_menuitem",
										FacesMessage.SEVERITY_ERROR, "");
								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");

							}
						}
					} else {
						if (viewID.contains("development")) {
							if (authorisationService.isRwDev())
								loadingPage(e);
							else {
								facesMessage = MessageFactory.getMessage(
										"header_noPermission_menuitem",
										FacesMessage.SEVERITY_ERROR, "");

								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");

							}
						} else if ((!authorisationService.isrBug() && root
								.getViewId().contains("bug"))
								|| ((!authorisationService.isrProject()) && (root
										.getViewId().contains("planning") || root
										.getViewId().contains("stats")))
								|| (!authorisationService.isrBuild() && viewID
										.contains("delivery"))
								|| ((!authorisationService.isrTest()) && viewID
										.contains("test"))
								|| (!authorisationService.isrContact() && root
										.getViewId().contains("team"))
								|| (!authorisationService.isRenderAdmin() && root
										.getViewId().contains("administration"))
								|| (!authorisationService.isrRequiement() && root
										.getViewId().contains("specifications"))
								|| (!authorisationService.isrRequiement() && root
										.getViewId().contains("requirement"))) {

							facesMessage = MessageFactory.getMessage(
									"header_noPermission_menuitem",
									FacesMessage.SEVERITY_ERROR, "");

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");
						}
					}
				} else {
					if ((!authorisationService.isrBug() && root.getViewId()
							.contains("bug"))
							|| ((!authorisationService.isrProject()) && (root
									.getViewId().contains("planning") || root
									.getViewId().contains("stats")))
							|| (!authorisationService.isrBuild() && viewID
									.contains("delivery"))
							|| ((!authorisationService.isrTest()) && viewID
									.contains("test"))
							|| (!authorisationService.isrContact() && root
									.getViewId().contains("team"))
							|| (!authorisationService.isRenderAdmin() && root
									.getViewId().contains("administration"))) {

						facesMessage = MessageFactory.getMessage(
								"header_noPermission_menuitem",
								FacesMessage.SEVERITY_ERROR, "");//

						FacesContext
								.getCurrentInstance()
								.getExternalContext()
								.redirect(
										path
												+ "/pages/main.jsf?faces-redirect=true");
					} else if (root.getViewId().contains("specifications")) {

						if (authorisationService.isrRequiement()) {
							JJRequirementBean requirementBean = (JJRequirementBean) findBean("jJRequirementBean");
							if (requirementBean == null)
								requirementBean = new JJRequirementBean();
							if (requirementBean.getTableDataModelList() == null) {
								requirementBean.fullTableDataModelList();
								ExternalContext ec = FacesContext
										.getCurrentInstance()
										.getExternalContext();
								ec.redirect(((HttpServletRequest) ec
										.getRequest()).getRequestURI());
							}
						} else {
							facesMessage = MessageFactory.getMessage(
									"header_noPermission_menuitem",
									FacesMessage.SEVERITY_ERROR, "");

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");
						}

					} else if (viewID.contains("development")) {
						if (authorisationService.isRwDev())
							loadingPage(e);
						else {
							facesMessage = MessageFactory.getMessage(
									"header_noPermission_menuitem",
									FacesMessage.SEVERITY_ERROR, "");

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");

						}
					} else if (viewID.contains("requirement")) {
						if (!authorisationService.isrRequiement()) {
							facesMessage = MessageFactory.getMessage(
									"header_noPermission_menuitem",
									FacesMessage.SEVERITY_ERROR, "");
							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");

						}
					}
				}

			}

		}
	}

	public List<JJRequirement> noCouvretReq;

	public void setNoCouvretReq(List<JJRequirement> noCouvretReq) {
		this.noCouvretReq = noCouvretReq;
	}

	public List<JJRequirement> getNoCouvretReq() {

		if (noCouvretReq == null) {
			if (jJPermissionService.isAuthorized(getContact(), "Requirement",
					null, true, null, null))
				noCouvretReq = jJRequirementService.getNonCouvredRequirements(
						getContact().getCompany(), null);
			else {

				List<JJProject> projects = ((JJProjectBean) findBean("jJProjectBean"))
						.getProjectList();

				List<JJProduct> products = ((JJProductBean) findBean("jJProductBean"))
						.getProductList();

				Map<JJProject, JJProduct> map = new HashMap<JJProject, JJProduct>();

				for (JJProject p : projects) {

					if (jJPermissionService.isAuthorized(getContact(), p, null,
							"Requirement"))
						map.put(p, null);
					else {
						for (JJProduct pr : products)
							if (jJPermissionService.isAuthorized(getContact(),
									p, pr, "Requirement"))
								map.put(p, pr);

					}
				}
				noCouvretReq = jJRequirementService.getNonCouvredRequirements(
						getContact().getCompany(), map);
			}
		}
		if (noCouvretReq == null || noCouvretReq.isEmpty())
			noCouvretReq = null;
		return noCouvretReq;

	}

	public static boolean isEqualPreviousPage(String page) {
		String referer = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestHeaderMap().get("referer");
		if (referer == null)
			referer = "";

		return referer.contains(page);
	}

	public static String getPreviousPage() {
		String referer = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestHeaderMap().get("referer");
		if (referer == null)
			referer = "";

		return referer;
	}

	public void updateGrowl(ComponentSystemEvent e) {

		if (facesMessage != null) {
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			RequestContext.getCurrentInstance().execute("updateGrowl()");
			facesMessage = null;
		}

	}

	public void showAndHideDialog(String widgetVar, Dialog dialog) {

		if (dialog.isVisible())
			RequestContext.getCurrentInstance().execute(
					"PF('" + widgetVar + "').hide();");
		else
			RequestContext.getCurrentInstance().execute(
					"PF('" + widgetVar + "').show();");
		dialog.setVisible(!dialog.isVisible());

	}

	public static JJProject getProject() {

		if (((JJProjectBean) findBean("jJProjectBean")) != null)
			return ((JJProjectBean) findBean("jJProjectBean")).getProject();
		else
			return null;
	}

	public static JJProduct getProduct() {

		if (((JJProductBean) findBean("jJProductBean")) != null)
			return ((JJProductBean) findBean("jJProductBean")).getProduct();
		else
			return null;
	}

	public static JJVersion getVersion() {
		if (((JJVersionBean) findBean("jJVersionBean")) != null)
			return ((JJVersionBean) findBean("jJVersionBean")).getVersion();
		else
			return null;
	}

	public boolean isRenderScrum() {
		return getPlaningTabsConf().getVal().toLowerCase()
				.contains("scrum".toLowerCase());

	}

	public boolean isRenderGantt() {
		return getPlaningTabsConf().getVal().toLowerCase()
				.contains("gantt".toLowerCase());

	}

	public UIComponent findComponent(final String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		final UIComponent[] found = new UIComponent[1];
		root.visitTree(new FullVisitContext(context), new VisitCallback() {

			@Override
			public VisitResult visit(VisitContext context, UIComponent component) {
				if (component.getId().equals(id)) {
					found[0] = component;
					return VisitResult.COMPLETE;
				}
				return VisitResult.ACCEPT;
			}
		});
		return found[0];
	}

	public String getStyleClass() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		if (viewId.contains("login"))
			return "login-content";
		else if (viewId.contains("signup"))
			return "signup-content";
		else
			return null;
	}

	public void loadStyleSheet(StyleSheet style, String param) {
		List<JJConfiguration> configs = jJConfigurationService
				.getConfigurations(null, param, true);
		if (configs != null && !configs.isEmpty()) {
			JJConfiguration specificationStylesheet = configs.get(0);
			String styleSheet = specificationStylesheet.getVal().substring(1,
					specificationStylesheet.getVal().length() - 1);
			String[] keyValuePairs = styleSheet.split(";"); // split the string
															// to creat
															// key-value pairs
			// Map<String,Map<String,String>> map = new HashMap<>();

			for (String pair : keyValuePairs) // iterate over the pais
			{
				String[] entry = pair.split(":"); // split the pairs to get key
													// and value
				// map.put(entry[0].trim(), entry[1].trim()); //add them to the
				// hashmap
				String tag = entry[1].replace("'", " ");
				String[] styleKeyValue = tag.trim()
						.substring(1, tag.trim().length() - 1).split(",");
				HashMap<String, String> styleMap = new HashMap<>();
				for (String stylePair : styleKeyValue) // iterate over the pais
				{
					String[] styleEntry = stylePair.split("=");
					System.err.println(styleEntry[0].trim() + "="
							+ styleEntry[1].trim());
					styleMap.put(styleEntry[0].trim(), styleEntry[1].trim());
				}
				System.err.println(entry[0].replace("'", " ") + "= XXX");
				if (!entry[0].replace("'", " ").startsWith("."))
					style.loadTagStyle(entry[0].replace("'", " ").trim(),
							styleMap);
				else
					style.loadStyle(entry[0].replace("'", " ").trim()
							.substring(1), styleMap);

			}
		} else {
			JJConfiguration configuration = new JJConfiguration();
			configuration.setName(param);
			configuration.setDescription("specify PDF Style");
			configuration.setCreatedBy(((LoginBean) LoginBean
					.findBean("loginBean")).getContact());
			configuration.setCreationDate(new Date());
			configuration.setEnabled(true);
			configuration.setParam(param);
			configuration
					.setVal("{'logo':('background'='/intern/images/logo.png');"
							+ "'title':('font'='verdana', 'size'='12', 'font-weight'='bold', 'style'='normal', 'align'='center');"
							+ "'h1':('font'='verdana', 'size'='10', 'font-weight'='normal', 'style'='normal', 'align'='left')}");
			jJConfigurationService.saveJJConfiguration(configuration);
			loadStyleSheet(style, param);
		}

	}

	public static void copyUploadImages(boolean copy)
			throws FileNotFoundException, IOException {
		Properties properties = new Properties();

		String serverName = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getServerName();
		String janjoon_directory = "";

		boolean contextPath = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getContextPath().contains("janjoon-ui");

		if (serverName.contains("localhost") && contextPath) {
			janjoon_directory = "src" + File.separator + "main"
					+ File.separator + "webapp" + File.separator + "images";
		} else {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String path = servletContext.getRealPath("WEB-INF" + File.separator
					+ "classes")
					+ File.separator;
			properties.load(new FileInputStream(path + "email.properties"));
			janjoon_directory = "lib" + File.separator
					+ properties.getProperty("janjoon.version")
					+ File.separator + "images";
		}

		System.err.println("JANJOON_DIRECTORY = " + janjoon_directory);
		File deletedFile = new File(janjoon_directory);
		FileUtils.deleteDirectory(deletedFile);
		if (copy) {
			File fromPath = new File(CKEditorUploadServlet.CKEDITOR_DIR);
			if (!fromPath.exists())
				fromPath.mkdirs();
			File toPath = new File(janjoon_directory);
			if (!toPath.exists())
				toPath.mkdirs();

			FileUtils.copyDirectory(fromPath, toPath);
		}

	}

	// public StreamedContent getFile() {
	// InputStream stream = FacesContext.getCurrentInstance()
	// .getExternalContext().getResourceAsStream("/resources/aaa");
	// return new DefaultStreamedContent(stream, "image/jpg",
	// "downloaded_optimus");
	// }
}

package com.starit.janjoonweb.ui.mb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.UsageChecker;
import com.starit.janjoonweb.ui.security.AuthorisationService;
import com.sun.faces.component.visit.FullVisitContext;

@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AuthenticationManager authenticationManager;
	private AuthorisationService authorisationService;
	static Logger logger = Logger.getLogger("loginBean-Logger");
	private List<JJRequirement> noCouvretReq;
	private boolean mobile;
	private boolean collapsedMesPanel = true;
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

	private String username = "";// "janjoon.mailer@gmail.com";
	private String password;
	private boolean agreeTerms = false;
	private boolean loading = false;
	private boolean loadMain = false;
	private JJContact contact;
	private boolean enable = false;
	private int activeTabAdminIndex;
	private int activeTabProjectIndex;
	private int menuIndex;

	@Autowired
	public LoginBean(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.mobile = (((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getHeader("User-Agent")
				.indexOf("Mobile")) != -1;
		// this.mobile=true;
	}

	public boolean isCollapsedMesPanel() {
		return collapsedMesPanel;
	}

	public void setCollapsedMesPanel(boolean collapsedMesPanel) {
		this.collapsedMesPanel = collapsedMesPanel;
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

				if (s.contains("development"))
					s = "main";
				else if (s.contains("project1") || s.contains("test")
						|| s.contains("stats")) {
					JJProjectBean jJProjectBean = (JJProjectBean) findBean("jJProjectBean");
					if (jJProjectBean.getProject() == null) {
						s = "main";
						facesMessage = MessageFactory.getMessage(
								"dev.nullProject.label",
								FacesMessage.SEVERITY_ERROR, "");
					}

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

		if (contact != null) {
			enable = !(contact == null || contact.getEmail().equals(""));
			return contact;
		} else if (enable && !username.isEmpty()) {
			contact = jJContactService.getContactByEmail(username, true);
			return contact;
		} else
			return null;

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

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Invalid username/password. Reason :",
					loginError.getMessage());
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
				jjProjectBean.setProject(contact.getLastProject());

				jjProductBean.getProductList();
				jjProductBean.setProduct(contact.getLastProduct());

				jjVersionBean.getVersionList();
				jjVersionBean.setVersion(contact.getLastVersion());

				authorisationService = new AuthorisationService(session,
						contact);

				facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Welcome ", getContact().getName());

				if (!UsageChecker.checkExpiryDate()) {

					facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"License expiry date is NOT valid!", null);
				}

				prevPage = getRedirectUrl(session);
			} else {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.invalidate();
				SecurityContextHolder.clearContext();
				facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"License is NOT correct!", null);

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
		case "project1":
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
			if (session.getAttribute("jJTestcaseBean") != null)
				session.setAttribute("jJTestcaseBean", new JJTestcaseBean());
			authorisationService = new AuthorisationService(session, contact);
			// messageListener(session);
			session.setAttribute("jJBugBean", new JJBugBean());
			session.setAttribute("jJMessageBean", null);
			session.setAttribute("jJRequirementBean", null);

			if (event != null) {
				if (session.getAttribute("requirementBean") != null)
					((RequirementBean) session.getAttribute("requirementBean"))
							.setRootNode(null);
				if (event.getComponent().getClientId()
						.contains("projectSelectOneMenu")) {

					JJProject project = (JJProject) event.getNewValue();
					jJProjectBean.setProject(project);
					authorisationService.initFields();
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());

				} else if (event.getComponent().getClientId()
						.contains("productSelectOneMenu")) {
					JJProductBean productBean = (JJProductBean) findBean("jJProductBean");
					productBean.setProduct((JJProduct) event.getNewValue());

				} else if (event.getComponent().getClientId()
						.contains("versionSelectOneMenu")) {
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion((JJVersion) event.getNewValue());
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
							} else {
								context.update(":contentPanel:devPanel");
								context.update(":contentPanel:errorPanel");
							}
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
		}

	}

	public void messageListener(HttpSession session) {

		JJMessageBean messageBean = (JJMessageBean) session
				.getAttribute("jJMessageBean");
		boolean messPanel = collapsedMesPanel;
		boolean appPanel = !messageBean.isCollapsedLayoutPanel();
		messageBean = new JJMessageBean();
		RequestContext context = RequestContext.getCurrentInstance();

		if (messPanel) {
			setCollapsedMesPanel(false);
			context.update(":messagePanel");
			context.update(":menuPanel");
			context.update(":contentPanel");
			context.update(":applicatinPanelGrid");

		}

		if (appPanel) {
			messageBean.setCollapsedLayoutPanel(false);
			context.update(":menuPanel");
			context.update(":contentPanel");
			context.update(":messagePanel");

		}

		session.setAttribute("jJMessageBean", messageBean);

	}

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

	public void loginRedirect(ComponentSystemEvent e) throws IOException {

		if (enable) {
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(path + "/pages/main.jsf?faces-redirect=true");
		}
	}

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

					if (!authorisationService.isAdminContactProfil()) {
						activeTabAdminIndex = 3;

						RequestContext context = RequestContext
								.getCurrentInstance();
						context.execute("PF('AdmintabView').select("
								+ activeTabAdminIndex + ")");
					}
				} else if (activeTabAdminIndex == 2) {
					if (!authorisationService.isAdminCompany())
						activeTabAdminIndex = 3;
					RequestContext context = RequestContext
							.getCurrentInstance();
					context.execute("PF('AdmintabView').select("
							+ activeTabAdminIndex + ")");
				}

			}

		}
	}

	public void redirection(ActionEvent e) throws IOException {

		JJProjectBean jJProjectBean = (JJProjectBean) findBean("jJProjectBean");

		if (jJProjectBean.getProject() == null) {
			FacesMessage message = MessageFactory.getMessage(
					"dev.nullProject.label", FacesMessage.SEVERITY_ERROR, "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();
			if (e.getComponent().getId().equalsIgnoreCase("proAction")) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								path
										+ "/pages/project1.jsf?faces-redirect=true");
			} else if (e.getComponent().getId().equalsIgnoreCase("testAction")) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/test.jsf?faces-redirect=true");
			} else {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(path + "/pages/stats.jsf?faces-redirect=true");
			}
		}

	}

	public void redirectToDev(ActionEvent e) throws IOException {

		JJVersionBean jjVersionBean = (JJVersionBean) findBean("jJVersionBean");
		JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");
		JJProjectBean jJProjectBean = (JJProjectBean) findBean("jJProjectBean");

		if (jjVersionBean.getVersion() != null
				&& jJProductBean.getProduct() != null
				&& jJProjectBean.getProject() != null) {

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

			if (jJProductBean.getProduct() == null) {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullProduct.label", FacesMessage.SEVERITY_ERROR,
						"");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else if (jJProjectBean.getProject() == null) {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullProject.label", FacesMessage.SEVERITY_ERROR,
						"Project");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullVersion.label", FacesMessage.SEVERITY_ERROR,
						"");
				FacesContext.getCurrentInstance().addMessage(null, message);

			}
		}
	}

	public void checkAuthorities(ComponentSystemEvent e) throws IOException {

		if (enable) {

			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot root = context.getViewRoot();

			JJProjectBean jjProjectBean = (JJProjectBean) findBean("jJProjectBean");

			if (authorisationService == null)
				authorisationService = new AuthorisationService(
						(HttpSession) FacesContext.getCurrentInstance()
								.getExternalContext().getSession(false),
						getContact());

			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			if (jjProjectBean.getProject() == null
					&& (root.getViewId().contains("project1")
							|| root.getViewId().contains("test") || root
							.getViewId().contains("stats"))) {
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
								if (requirementBean.getTableDataModelList() == null)
									requirementBean.fullTableDataModelList();

							} else {
								facesMessage = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"You have no permission to access this resource",
										null);

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
								facesMessage = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"You have no permission to access this resource",
										null);

								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");

							}

						} else if (!authorisationService.isrProject()
								&& root.getViewId().contains("project1")) {

							facesMessage = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"You have no permission to access this resource",
									null);

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");
						} else if (viewID.contains("requirement")) {
							if (!authorisationService.isrRequiement()) {
								facesMessage = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"You have no permission to access this resource",
										null);
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
								facesMessage = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"You have no permission to access this resource",
										null);

								FacesContext
										.getCurrentInstance()
										.getExternalContext()
										.redirect(
												path
														+ "/pages/main.jsf?faces-redirect=true");

							}
						} else if ((!authorisationService.isrProject() && root
								.getViewId().contains("project1"))
								|| (!authorisationService.isrBuild() && viewID
										.contains("delivery"))
								|| ((!authorisationService.isrTest()) && viewID
										.contains("test"))) {

							facesMessage = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"You have no permission to access this resource",
									null);//

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");
						}
					}
				} else {

					if ((!authorisationService.isrProject() && root.getViewId()
							.contains("project1"))
							|| (!authorisationService.isrBuild() && viewID
									.contains("delivery"))
							|| (!authorisationService.isrTest() && viewID
									.contains("test"))) {

						facesMessage = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"You have no permission to access this resource",
								null);//

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
							if (requirementBean.getTableDataModelList() == null)
								requirementBean.fullTableDataModelList();
						} else {
							facesMessage = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"You have no permission to access this resource",
									null);//

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
							facesMessage = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"You have no permission to access this resource",
									null);

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");

						}
					} else if (viewID.contains("requirement")) {
						if (!authorisationService.isrRequiement()) {
							facesMessage = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"You have no permission to access this resource",
									null);
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

	public List<JJRequirement> getNoCouvretReq() {
		return noCouvretReq;
	}

	public void setNoCouvretReq(List<JJRequirement> noCouvretReq) {
		this.noCouvretReq = noCouvretReq;
	}

	public void initNonCouvretSpec(ComponentSystemEvent e) {

		if (noCouvretReq == null && isEnable()) {
			noCouvretReq = jJRequirementService
					.getNonCouvredRequirements(getContact().getCompany());

		}
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

	public void handleMesToggle(ToggleEvent event) {

		this.collapsedMesPanel = !this.collapsedMesPanel;

	}

	public void updateGrowl(ComponentSystemEvent e) {

		if (facesMessage != null) {
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			RequestContext.getCurrentInstance().execute("updateGrowl()");
			facesMessage = null;
		}

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
}

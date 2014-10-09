package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.io.Serializable;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.UsageChecker;
import com.starit.janjoonweb.ui.security.AuthorizationManager;
import com.sun.faces.component.visit.FullVisitContext;

@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthorizationManager authorizationManager;

	static Logger logger = Logger.getLogger("loginBean-Logger");

	@Autowired
	private JJContactService jJContactService;

	@Autowired
	JJCompanyService jJCompanyService;

	private String username = "";// "janjoon.mailer@gmail.com";
	private String password;
	private boolean loading = false;
	private boolean loadMain = false;
	private JJContact contact;
	private boolean enable = false;
	private int activeTabAdminIndex;
	private int activeTabProjectIndex;
	private int menuIndex;

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	public void setAuthorizationManager(
			AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public boolean isEnable() {

		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Autowired
	public LoginBean(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
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
						FacesMessage message = MessageFactory.getMessage(
								"dev.nullProject.label",
								FacesMessage.SEVERITY_ERROR, "");
						FacesContext.getCurrentInstance().addMessage(null,
								message);
					}

				}
				return s;
			}
		}

		return "main";
	}

	public String logout() {
		FacesContext fContext = FacesContext.getCurrentInstance();
		logger.info("JJContact logged out");
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
				authorizationManager.setContact(contact);
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);

				session.putValue("JJContact", contact);
				session.putValue("password", password);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Welcome ",
						contact.getName());
				
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage("login", message);
				
				logger.info("login operation success " + contact.getName()
						+ " logged in");

				if (UsageChecker.checkExpiryDate()) {
				} else {

					FacesMessage fExpiredMessage = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"License expiry date is NOT valid!", null);
					context.addMessage(null, fExpiredMessage);
				}
				context.getExternalContext().getFlash().setKeepMessages(true);
				prevPage = getRedirectUrl(session);
			} else {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.invalidate();
				SecurityContextHolder.clearContext();
				FacesMessage fMessage = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "License is NOT correct!",
						null);
				FacesContext.getCurrentInstance().addMessage(null, fMessage);
				// FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
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
		enable = !(contact == null || contact.getEmail().equals(""));
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

	}

	public static Object findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}

	public void onTabAdminChange(TabChangeEvent event) {
		TabView tv = (TabView) event.getComponent();
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
			JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");

			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(false);
			if (enable)
				messageListener(session);

			if (event.getComponent().getClientId()
					.contains("projectSelectOneMenu")
					&& enable) {

				session.setAttribute("jJSprintBean", new JJSprintBean());
				session.setAttribute("jJTaskBean", new JJTaskBean());
				session.setAttribute("jJBugBean", new JJBugBean());
				session.setAttribute("jJRequirementBean",
						new JJRequirementBean());
			}

			if (viewId.contains("development")) {

				RequestContext context = RequestContext.getCurrentInstance();

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
					jJVersionBean.setVersion((JJVersion) event.getNewValue());
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
					jJProjectBean.setProject((JJProject) event.getNewValue());
					if (jJDevelopment.isRender()) {
						jJDevelopment.setProject(jJProjectBean.getProject());
						jJDevelopment.setTasks(null);
						jJDevelopment.setTask(null);
						context.update(":contentPanel:devPanel:form:taskSelectOneMenu");
					}
				}
			} else if (viewId.contains("specifications")) {

				System.out.println("in spec");

				JJRequirementBean jJRequirementBean = (JJRequirementBean) findBean("jJRequirementBean");

				if (event.getComponent().getClientId()
						.contains("projectSelectOneMenu")) {

					JJProject project = (JJProject) event.getNewValue();

					jJProjectBean.setProject(project);
					jJRequirementBean.setProject(project);

				} else if (event.getComponent().getClientId()
						.contains("productSelectOneMenu")) {

					JJProduct product = (JJProduct) event.getNewValue();

					jJProductBean.setProduct(product);

					jJRequirementBean.setProduct(product);

				} else if (event.getComponent().getClientId()
						.contains("versionSelectOneMenu")) {

					JJVersion version = (JJVersion) event.getNewValue();

					jJVersionBean.setVersion(version);
					jJRequirementBean.setVersion(version);
				}

				jJRequirementBean.loadData();

				ExternalContext ec = FacesContext.getCurrentInstance()
						.getExternalContext();
				ec.redirect(((HttpServletRequest) ec.getRequest())
						.getRequestURI());

			}

		}
	}

	public void messageListener(HttpSession session) {

		JJMessageBean messageBean = (JJMessageBean) session
				.getAttribute("jJMessageBean");
		boolean messPanel = !messageBean.isCollapsedMesPanel();
		boolean appPanel = !messageBean.isCollapsedLayoutPanel();
		messageBean = new JJMessageBean();
		RequestContext context = RequestContext.getCurrentInstance();

		if (messPanel) {
			messageBean.setCollapsedMesPanel(false);
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

		if (jjVersionBean.getVersion() != null
				&& jJProductBean.getProduct() != null) {

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
				context.update(":contentPanel:devPanel");
				context.update(":contentPanel:errorPanel");
			}
		} else {

			if (jJProductBean.getProduct() == null) {
				FacesMessage message = MessageFactory.getMessage(
						"dev.nullProduct.label", FacesMessage.SEVERITY_ERROR,
						"");
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

			JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");
			JJProjectBean jjProjectBean = (JJProjectBean) findBean("jJProjectBean");

			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			if (jjProjectBean.getProject() == null
					&& (root.getViewId().contains("project1")
							|| root.getViewId().contains("test") || root
							.getViewId().contains("stats"))) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Please Select Project",
						null));

				context.getExternalContext().getFlash().setKeepMessages(true);

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
						if (!authorizationManager.getAuthorization(
								root.getViewId(), jjProjectBean.getProject(),
								jJProductBean.getProduct())) {

							context.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"You have no permission to access this resource",
											null));

							context.getExternalContext().getFlash()
									.setKeepMessages(true);

							FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.redirect(
											path
													+ "/pages/main.jsf?faces-redirect=true");

						} else if (viewID.contains("specification")) {

							JJRequirementBean jJRequirementBean = (JJRequirementBean) findBean("jJRequirementBean");
							jJRequirementBean.loadData();

							// RequestContext ct =
							// RequestContext.getCurrentInstance();
							// ct.update("contentPanel:specForm");
							// ct.update("contentPanel:categoryForm");
						} else if (viewID.contains("development")) {

							loadingPage(e);
						}
					}
				} else {
					if (!authorizationManager.getAuthorization(
							root.getViewId(), jjProjectBean.getProject(),
							jJProductBean.getProduct())) {

						context.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"You have no permission to access this resource",
										null));

						context.getExternalContext().getFlash()
								.setKeepMessages(true);

						FacesContext
								.getCurrentInstance()
								.getExternalContext()
								.redirect(
										path
												+ "/pages/main.jsf?faces-redirect=true");
					} else {

						if (root.getViewId().contains("specifications")) {

							JJRequirementBean jJRequirementBean = (JJRequirementBean) findBean("jJRequirementBean");
							jJRequirementBean.loadData();

						}
					}
				}
			}

		}
	}

	// public void handleFileUpload(FileUploadEvent event) throws IOException {
	//
	// CKEditor description = null;
	// for(UIComponent component:event.getComponent().getParent().getChildren())
	// {
	// if(component.getId().contains("description"))
	// {
	//
	// description=(CKEditor) component;
	//
	// break;
	// }
	// }
	// System.err.println("finded"+description.getId());
	//
	// ServletContext servletContext = (ServletContext) FacesContext
	// .getCurrentInstance().getExternalContext().getContext();
	//
	// // File targetFolder = new File(
	// // servletContext.getRealPath("/CKEditorImage"));
	// File targetFolder = new File(
	// getClass().getResource("/CKEditorImage").getPath());
	//
	// //servletContext.getResource("/resources/images/CKeditorImages");
	//
	// //System.out.println(description.getId());
	//
	// System.out.println(targetFolder.getPath());
	// TreeOperation.uploadFile(targetFolder, event.getFile().getInputstream(),
	// event.getFile().getFileName());
	// description.setSubmittedValue(description.getValue()+"image File <img src='resources/images/CKeditorImages/"
	// + event.getFile().getFileName() + "' />");
	//
	// }

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

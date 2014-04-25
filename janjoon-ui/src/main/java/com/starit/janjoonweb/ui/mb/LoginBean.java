package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.component.visit.FullVisitContext;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuthenticationManager authenticationManager;
	private String username = "janjoon.mailer@gmail.com";
	private String password;
	private boolean loading = false;
	private boolean loadMain = false;
	private JJContact contact;
	private boolean enable = false;

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
				s = s.replace(s.substring(s.indexOf(".")), "");
				return s;
			}
		}

		return "main";
	}

	public String logout() {
		FacesContext fContext = FacesContext.getCurrentInstance();
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
			JJContactBean service = new JJContactBean();
			contact = service.getContactByEmail(username);
			FacesContext fContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fContext.getExternalContext()
					.getSession(false);
			session.putValue("JJContact", contact);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Welcome ", contact.getName());
			FacesContext.getCurrentInstance().addMessage("login", message);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			prevPage = getRedirectUrl(session);

		}
		System.out.println(prevPage);
		return prevPage;
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

	public static Object findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}

	public void changeEvent(ValueChangeEvent event) throws IOException {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId();
		
		if (event.getComponent().getClientId()
				.contains("projectSelectOneMenu")) 
		{
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(false);

			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			jJSprintBean=null;
		}

		if (viewId.contains("development")) {

			RequestContext context = RequestContext.getCurrentInstance();			
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(false);

			JJVersionBean jJVersionBean = (JJVersionBean) session
					.getAttribute("jJVersionBean");
			JJProductBean jJProductBean = (JJProductBean) session
					.getAttribute("jJProductBean");
			JJProjectBean jJProjectBean = (JJProjectBean) session
					.getAttribute("jJProjectBean");
			DevelopmentBean jJDevelopment = (DevelopmentBean) session
					.getAttribute("jJDevelopment");

			if (event.getComponent().getClientId()
					.contains("productSelectOneMenu")) {
				System.out
						.println("---------------ProjectUpdate---------------");

			} else if (event.getComponent().getClientId()
					.contains("versionSelectOneMenu")) {
				jJVersionBean.setVersion((JJVersion) event.getNewValue());
				if (jJVersionBean.getVersion() != null) {
					// jJDevelopment = new DevelopmentBean();
					if (jJDevelopment.isRender()) {
						System.out
								.println("---------------versionUpdate---------------");

						context.update(":contentPanel:devPanel:form");
						context.update(":contentPanel:errorPanel");
					} else {
						System.out
								.println("---------------versionUpdateError---------------");

						context.update(":contentPanel:devPanel");
						context.update(":contentPanel:errorPanel");
					}
				}
			} else {
				jJProjectBean.setProject((JJProject) event.getNewValue());
				if (jJDevelopment.isRender()) {
					System.out
							.println("---------------ProjectUpdate---------------");
					// jJDevelopment.setTasks(jJProductBean.getTasksByProduct(
					// jJProductBean.getProduct(),
					// jJProjectBean.getProject()));
					context.update(":contentPanel:devPanel:form:taskSelectOneMenu");
				}
			}

		}

	}

	public void loadingPage(ComponentSystemEvent e) throws IOException {

		if (!loading) {
			loading = true;

			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();
			System.out.println(path);
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							path + "/pages/development.jsf?faces-redirect=true");

		}
	}

	public void loginRedirect(ComponentSystemEvent e) throws IOException {

		if (enable) {
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();
			System.out.println(path);
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

		}
	}

	public void redirectToDev(ActionEvent e) throws IOException {

		JJVersionBean jjVersionBean = (JJVersionBean) findBean("jJVersionBean");
		JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");

		System.out.println("------------------------------------");
		if (jjVersionBean.getVersion() != null && jJProductBean != null) {

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

			FacesMessage message =MessageFactory.getMessage("dev.nullProject.label", FacesMessage.SEVERITY_ERROR, "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println(message.getDetail());

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

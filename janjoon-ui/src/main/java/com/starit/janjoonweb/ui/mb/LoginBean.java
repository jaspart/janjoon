package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
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
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;

@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuthenticationManager authenticationManager;
	private String username;
	private String password;
	private boolean loading = false;
	private boolean loadMain=false;
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

		String s = "";
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);
		try {
			Authentication authentication = authenticationManager
					.authenticate(token);
			SecurityContext sContext = SecurityContextHolder.getContext();
			sContext.setAuthentication(authentication);
			enable = true;
			s = "success";
		} catch (AuthenticationException loginError) {

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Invalid username/password. Reason ",
					loginError.getMessage());
			FacesContext.getCurrentInstance().addMessage("login", message);

			enable = false;
			s = "fail";
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
		}

		return s;
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

		if (viewId.contains("development")) {

			RequestContext context = RequestContext.getCurrentInstance();
			FacesContext fContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fContext.getExternalContext()
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
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Please Select a version ", "version is set to null");
				fContext.addMessage(null, message);
			} else if (event.getComponent().getClientId()
					.contains("versionSelectOneMenu")) {
				jJVersionBean.setVersion((JJVersion) event.getNewValue());
				if (jJVersionBean.getVersion() != null) {
					jJDevelopment = new DevelopmentBean();
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
				} else {
					redirectToDev(null);
				}
			} else {
				jJProjectBean.setProject((JJProject) event.getNewValue());
				System.out.println("dev");
				if (jJDevelopment.isRender()) {
					System.out
							.println("---------------ProjectUpdate---------------");
					jJDevelopment.setTasks(jJProductBean.getTasksByProduct(
							jJProductBean.getProduct(),
							jJProjectBean.getProject()));
					context.update(":contentPanel:devPanel:form:taskSelectOneMenu");
				}
			}

		}

	}

	public void loadingPage(ComponentSystemEvent e) throws IOException {

		if (!loading) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			DevelopmentBean jJDevelopment = (DevelopmentBean) session
					.getAttribute("jJDevelopment");
			jJDevelopment = new DevelopmentBean();
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/janjoon-ui/pages/development.jsf?faces-redirect=true");
			loading = true;
		}
	}
	public void loadingMain(ComponentSystemEvent e) throws IOException {
		
		if (!loadMain && enable) {
			
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/janjoon-ui/pages/main.jsf?faces-redirect=true");
			loadMain = true;
		}
	}

	public void redirectToDev(ActionEvent e) throws IOException {

		JJVersionBean jjVersionBean = (JJVersionBean) findBean("jJVersionBean");
		JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");

		System.out.println("------------------------------------");
		if (jjVersionBean.getVersion() != null && jJProductBean != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			DevelopmentBean jJDevelopment = (DevelopmentBean) session
					.getAttribute("jJDevelopment");
			jJDevelopment = new DevelopmentBean();
			if (!FacesContext.getCurrentInstance().getViewRoot().getViewId()
					.contains("development")) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"/janjoon-ui/pages/development.jsf?faces-redirect=true");
			}
			else{
				RequestContext context=RequestContext.getCurrentInstance();
				context.update(":contentPanel:devPanel");
				context.update(":contentPanel:errorPanel");
			}

		} else {
			// s=FacesContext.getCurrentInstance().getViewRoot().getViewId();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Please Select a project and a version ",
					"Project or version is set to null");
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

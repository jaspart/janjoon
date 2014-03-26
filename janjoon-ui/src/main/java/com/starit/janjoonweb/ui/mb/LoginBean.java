package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.config.element.FacesConfig;
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
			FacesContext fContext = FacesContext.getCurrentInstance();
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
					.getSession(true);
			session.putValue("JJContact", contact);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Welcome ", contact.getFirstname());
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

	public static Object findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}

	public void redirectToDev(ActionEvent e) throws IOException {
		JJVersionBean jjVersionBean = (JJVersionBean) findBean("jJVersionBean");
		JJProductBean jJProductBean = (JJProductBean) findBean("jJProductBean");
		String s = "";
		System.out.println("------------------------------------");
		if (jjVersionBean.getVersion() != null && jJProductBean != null) {

			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/janjoon-ui/pages/development.jsf?faces-redirect=true");

		} else {
			// s=FacesContext.getCurrentInstance().getViewRoot().getViewId();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Please Select a project and a version ",
					"Project or version is set to null");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println(s + message.getDetail());

		}

	}

}

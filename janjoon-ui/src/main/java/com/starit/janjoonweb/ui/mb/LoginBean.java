package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
public class LoginBean implements Serializable{
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
			JJVersionBean jjVersionBean=(JJVersionBean) findBean("jJVersionBean");
			JJProjectBean jjProjectBean=(JJProjectBean) findBean("jJProjectBean");
			JJProductBean jJProductBean=(JJProductBean) findBean("jJProductBean");
			//gérer la session spring security 
			session.putValue("JJContact", contact);
			session.putValue("jJVersionBean", jjVersionBean);
			session.putValue("jJProjectBean", jjProjectBean);
			session.putValue("jJProductBean", jJProductBean);
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

}

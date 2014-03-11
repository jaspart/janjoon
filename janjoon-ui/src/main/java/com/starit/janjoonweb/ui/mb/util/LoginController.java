package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;

@ManagedBean(name="loginController")
@RequestScoped
public class LoginController implements PhaseListener{
    
    private static final long serialVersionUID = 1L;
    
    /**
    *
    * Redirects the login request directly to spring security check.
    * Leave this method as it is to properly support spring security.
    *
    * @return
    * @throws ServletException
    * @throws IOException
    */
    
   public String doLogin() throws ServletException, IOException {
       ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

       RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
               .getRequestDispatcher("/j_spring_security_check");

       dispatcher.forward((ServletRequest) context.getRequest(),
               (ServletResponse) context.getResponse());

       FacesContext.getCurrentInstance().responseComplete();

       return null;
   }

    @Override
    public void afterPhase(PhaseEvent arg0) {        
    }

    @Override
    public void beforePhase(PhaseEvent arg0) {
        Exception e = (Exception) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
       
              if (e instanceof BadCredentialsException) {
                  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                          WebAttributes.AUTHENTICATION_EXCEPTION, null);
                  FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                      "Username or password not valid.", "Username or password not valid"));
              }
        
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
}

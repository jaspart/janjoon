package com.starit.janjoonweb.ui.mb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJProjectService;

@Named
@ApplicationScoped
public class ImageStreamer {

	@Autowired
	JJContactService jJContactService;

	@Autowired
	JJCompanyService jJCompanyService;

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	@Autowired
	JJProjectService jJProjectService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	@Autowired
	JJProductService jJProductService;

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	public StreamedContent getStar() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			String rated = context.getExternalContext()
					.getRequestParameterMap().get("rated");

			if (rated.equalsIgnoreCase("true")) {
				InputStream stream = FacesContext.getCurrentInstance()
						.getExternalContext()
						.getResourceAsStream("/resources/images/yelstar.ico");
				return new DefaultStreamedContent(stream);

			} else {
				InputStream stream = FacesContext.getCurrentInstance()
						.getExternalContext()
						.getResourceAsStream("/resources/images/blackstar.ico");
				return new DefaultStreamedContent(stream);
			}

		}
	}

	public StreamedContent getImageProd() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			// for (Map.Entry<String, String> entry :
			// context.getExternalContext()
			// .getRequestParameterMap().entrySet()) {
			// System.out.println("Displays ==== " + entry.getKey() + " : "
			// + entry.getValue());
			// }
			String prodId = context.getExternalContext()
					.getRequestParameterMap().get("ProdId");
			System.out.println("Displays ProdId : " + prodId);

			return new DefaultStreamedContent(new ByteArrayInputStream(
					jJProductService.findJJProduct(Long.valueOf(prodId))
							.getLogo()));

		}
	}

	public StreamedContent getImageProj() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String projId = context.getExternalContext()
					.getRequestParameterMap().get("ProjId");
			System.out.println("Displays ProjId : " + projId);

			return new DefaultStreamedContent(new ByteArrayInputStream(
					jJProjectService.findJJProject(Long.valueOf(projId))
							.getLogo()));

		}
	}

	public StreamedContent getImageComp() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String CompId = context.getExternalContext()
					.getRequestParameterMap().get("CompId");
			System.out.println("Displays CompId : " + CompId);
			JJCompany comp = null;
			if (CompId != null && !CompId.isEmpty())
				comp = jJCompanyService.findJJCompany(Long.valueOf(CompId));
			if (comp != null && comp.getLogo() != null) {
				return new DefaultStreamedContent(new ByteArrayInputStream(
						comp.getLogo()));
			} else {
				InputStream stream = FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.getResourceAsStream(
								"/resources/images/empty_company.png");
				return new DefaultStreamedContent(stream, "image/jpg");
			}
		}
	}

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			String contactId = context.getExternalContext()
					.getRequestParameterMap().get("contactId");
			System.out.println("Displays contactId : " + contactId);
			if (contactId == null) {
				return new DefaultStreamedContent(new ByteArrayInputStream(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact().getPicture()));
			} else {
				String company = context.getExternalContext()
						.getRequestParameterMap().get("company");
				if (company == null) {
					JJContact contact = jJContactService.findJJContact(Long
							.valueOf(contactId));
					if (contact.getPicture() != null) {
						return new DefaultStreamedContent(
								new ByteArrayInputStream(contact.getPicture()));
					} else {

						InputStream stream = FacesContext
								.getCurrentInstance()
								.getExternalContext()
								.getResourceAsStream(
										"/resources/images/default-user-2.jpg");
						return new DefaultStreamedContent(stream, "image/jpg");

					}
				} else {
					return new DefaultStreamedContent(new ByteArrayInputStream(
							jJContactService
									.findJJContact(Long.valueOf(contactId))
									.getCompany().getLogo()));
				}

			}

		}
	}

	public String getCkEditorToolBar() {
		return "[['Source','Bold','Italic','Underline','Strike','NumberedList',"
				+ "'BulletedList','Image','TextColor','BGColor','Undo','Table','-', 'RemoveFormat']]";
	}

}
package com.starit.janjoonweb.ui.mb.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJBuildBean;
import com.starit.janjoonweb.ui.mb.JJVersionBean;

@FacesValidator("unicityNameValidator")
public class UnicityNameValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value)
			throws ValidatorException {
		
		JJVersion version = (JJVersion) component.getAttributes()
				.get("version");
		
		if(version != null)
		{
			JJBuildBean buildBean = (JJBuildBean) component.getAttributes()
					.get("buildBean");
			
			if (value == null) {
				return; // Let required="true" handle.
			}
			
			if(buildBean.buildNameExist((String) value, version))
			{
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Build Name Exist", "JJBuild"));
			}
			
		}else 
		{
			JJProduct product = (JJProduct) component.getAttributes()
					.get("product");
			if(product != null)
			{
				JJVersionBean versionBean = (JJVersionBean) component.getAttributes()
						.get("versionBean");
				if (value == null) {
					return; // Let required="true" handle.
				}
				
				if(versionBean.versionNameExist((String) value, product))
				{
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Version Name Exist", "JJVersion"));
				}
			}
		}
		
	}

}
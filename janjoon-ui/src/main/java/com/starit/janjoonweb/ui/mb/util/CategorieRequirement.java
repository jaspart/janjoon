package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.List;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJRequirement;

public class CategorieRequirement {

	private JJCategory category;
	private List<JJRequirement> requirements;

	public CategorieRequirement(JJCategory category) {

		this.category = category;
		requirements = new ArrayList<JJRequirement>();
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public List<JJRequirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<JJRequirement> requirements) {
		this.requirements = requirements;
	}

	public static List<CategorieRequirement> initCategorieRequirement(
			JJRequirement requirement, List<JJCategory> categories) {

		List<CategorieRequirement> categorieRequirements = new ArrayList<CategorieRequirement>();

		for (JJCategory category : categories) {
			if (!category.equals(requirement.getCategory())) {
				CategorieRequirement cateReq = new CategorieRequirement(
						category);
				for (JJRequirement req : requirement.getRequirementLinkDown()) {

					if (req.getCategory().getName()
							.equalsIgnoreCase(category.getName())
							&& req.getEnabled())
						cateReq.getRequirements().add(req);

				}

				for (JJRequirement req : requirement.getRequirementLinkUp()) {

					if (req.getCategory().getName()
							.equalsIgnoreCase(category.getName())
							&& req.getEnabled())
						cateReq.getRequirements().add(req);

				}
				categorieRequirements.add(cateReq);

			}
		}
		// for (CategorieRequirement cc : categorieRequirements) {
		// if (requirement.getCategory().equals(cc.getCategory()))
		// cc.setRequirements(null);
		// }

		return categorieRequirements;

	}

}

package com.starit.janjoonweb.ui.mb.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.CategoryDataModel;

public class CategoryUtil implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JJCategory			category;
	private boolean				checked;

	public CategoryUtil(JJCategory category, boolean checked) {

		this.category = category;
		this.checked = checked;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public static List<CategoryUtil> getCategoryList(List<JJCategory> categories,
	        List<CategoryDataModel> tableDataModelList) {
		List<CategoryUtil> categoryUtils = new ArrayList<CategoryUtil>();

		for (JJCategory c : categories) {
			if (tableDataModelList != null) {
				boolean checked = false;
				int i = 0;

				while (i < tableDataModelList.size() && !checked) {
					checked = tableDataModelList.get(i).getCategoryId() == c.getId()
					        && tableDataModelList.get(i).getRendered();
					i++;
				}

				categoryUtils.add(new CategoryUtil(c, checked));

			} else {
				categoryUtils.add(new CategoryUtil(c, false));
			}
		}

		return categoryUtils;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof CategoryUtil) && (getCategory() != null)
		        ? getCategory().equals(((CategoryUtil) object).getCategory()) : (object == this);
	}

	public static List<CategoryUtil> getCategoryList(List<JJCategory> categories, JJCategory category) {
		List<CategoryUtil> categoryUtils = new ArrayList<CategoryUtil>();

		for (JJCategory c : categories) {
			if (category != null) {
				categoryUtils.add(new CategoryUtil(c, c.equals(category)));

			} else {
				categoryUtils.add(new CategoryUtil(c, false));
			}
		}

		return categoryUtils;
	}

}

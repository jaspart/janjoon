package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class LazyProfileDataModel extends LazyDataModel<JJProfile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJProfileService profileService;
	private JJPermissionService permissionService;

	public LazyProfileDataModel(JJProfileService profileService,
			JJPermissionService jJPermissionService) {

		this.profileService = profileService;
		this.permissionService = jJPermissionService;
	}

	@Override
	public JJProfile getRowData(String rowKey) {

		return profileService.findJJProfile(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJProfile bug) {
		return bug.getId();
	}

	@Override
	public List<JJProfile> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		SelectOneMenu objectComponent = (SelectOneMenu) LoginBean
				.findComponent("objectComponent");
		SelectOneMenu rComponent = (SelectOneMenu) LoginBean
				.findComponent("rComponent");
		SelectOneMenu wComponent = (SelectOneMenu) LoginBean
				.findComponent("wComponent");
		SelectOneMenu xComponent = (SelectOneMenu) LoginBean
				.findComponent("xComponent");

		System.out.println(rComponent.getValue());

		if (rComponent.getValue() != null
				&& !rComponent.getValue().equals("nottrue"))
			filters.put("rComponent", rComponent.getValue().equals("true"));

		if (wComponent.getValue() != null
				&& !wComponent.getValue().equals("nottrue"))
			filters.put("wComponent", wComponent.getValue().equals("true"));

		if (xComponent.getValue() != null
				&& !xComponent.getValue().equals("nottrue"))
			filters.put("xComponent", xComponent.getValue().equals("true"));

		if (objectComponent.getValue() != null
				&& !objectComponent.getValue().equals("all"))
			filters.put("objectComponent", objectComponent.getValue());

		List<JJProfile> data = new ArrayList<JJProfile>();
		MutableInt size = new MutableInt(0);
		data = profileService.load(size, first, pageSize, multiSortMeta,
				filters,
				permissionService.isSuperAdmin(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact()),
				LoginBean.getCompany());
		setRowCount(size.getValue());
		System.err.println("SIZE :" + data.size());

		int dataSize = data.size();

		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}

}
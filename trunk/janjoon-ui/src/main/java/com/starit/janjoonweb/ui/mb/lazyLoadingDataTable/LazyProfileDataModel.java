package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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

	public LazyProfileDataModel(JJProfileService profileService,JJPermissionService jJPermissionService) {
		
		this.profileService = profileService;
		this.permissionService=jJPermissionService;
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
	public List<JJProfile> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {

		List<JJProfile> data = new ArrayList<JJProfile>();
		MutableInt size=new MutableInt(0);
		data = profileService.load(size,first, pageSize,permissionService
				.isSuperAdmin(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact()));
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
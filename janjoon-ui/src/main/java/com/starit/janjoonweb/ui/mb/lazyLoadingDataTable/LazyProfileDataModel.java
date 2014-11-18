package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;

public class LazyProfileDataModel extends LazyDataModel<JJProfile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJProfileService profileService;	

	public LazyProfileDataModel(JJProfileService profileService) {
		
		this.profileService = profileService;
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
	public List<JJProfile> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		List<JJProfile> data = new ArrayList<JJProfile>();

		data = profileService.load(first, pageSize);
		setRowCount(profileService.getProfiles(true).size());
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
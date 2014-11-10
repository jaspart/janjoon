package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;

public class LazyProductDataModel extends LazyDataModel<JJProduct> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJProductService productService;	

	public LazyProductDataModel(JJProductService productService) {
		
		this.productService = productService;
	}

	@Override
	public JJProduct getRowData(String rowKey) {

		return productService.findJJProduct(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJProduct bug) {
		return bug.getId();
	}

	@Override
	public List<JJProduct> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		List<JJProduct> data = new ArrayList<JJProduct>();

		data = productService.load(first, pageSize);
		setRowCount(productService.getProducts(true).size());
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
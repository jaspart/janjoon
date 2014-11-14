package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.ui.mb.util.TestCaseChartUtil;

@ManagedBean
@RequestScoped
@Configurable
public class ChartBean implements Serializable {

	@Autowired
	JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	private CartesianChartModel categoryModel;
	private String seriesColors = "000000, 097D0B, D8115A";

	public ChartBean() {

		createCategoryModel();

	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	private void createCategoryModel() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		
		List<TestCaseChartUtil> testcases = TestCaseChartUtil.getTestCaseUtilFromJJTesCase(jJTestcaseBean.getTestcases());
		
		

		Set<String> datesTMP = new HashSet<String>();

		for (TestCaseChartUtil testcase : testcases) {
			Date creationdate = testcase.getTestcase().getCreationDate();
			String date = creationdate.toString().substring(0, 10);

			datesTMP.add(date);

		}	

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		if (jJTestcaseexecutionBean == null) {
			jJTestcaseexecutionBean = new JJTestcaseexecutionBean();
		}

		Set<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionBean
				.getTestcaseexecutions();	

		for (JJTestcaseexecution tce : testcaseexecutions) {
			Date creationdate = tce.getUpdatedDate();
			String date = creationdate.toString().substring(0, 10);

			datesTMP.add(date);
		}
		List<String> dates = new ArrayList<String>();
		dates.addAll(datesTMP);	

		Map<String, String> mapTotalTC = new LinkedHashMap<String, String>();
		Map<String, String> mapSuccessTC = new LinkedHashMap<String, String>();
		Map<String, String> mapFailedTC = new LinkedHashMap<String, String>();

		for (int i = 0; i < dates.size(); i++) {
			String date = dates.get(i);
			int compteur = 0;

			int compteurSuccess = 0;
			int compteurFailed = 0;

			for (TestCaseChartUtil testcase : testcases) {
				String creationDate = testcase.getTestcase().getCreationDate().toString()
						.substring(0, 10);
				if (creationDate.equalsIgnoreCase(date)) {
					compteur++;
				}

			}

			if (i == 0) {
				mapTotalTC.put(date, String.valueOf(compteur));

				for (TestCaseChartUtil testcase : testcases) {
					List<JJTestcaseexecution> exec = new ArrayList<JJTestcaseexecution>();
					for (JJTestcaseexecution tce : testcaseexecutions) {
						Date updatedDate = tce.getUpdatedDate();
						if ((updatedDate != null)
								&& (updatedDate.toString().substring(0, 10)
										.equals(date))
								&& (tce.getTestcase().equals(testcase.getTestcase()))) {

							if (tce.getPassed() != null) {
								exec.add(tce);
							}

						}
					}
					if (!exec.isEmpty()) {
						if (exec.size() == 1) {
							if (exec.get(0).getPassed()) {
								compteurSuccess++;	
								testcase.setSuccess("compteurSuccess");
							} else {
								compteurFailed++;
								testcase.setSuccess("compteurFailed");
							}
						} else {
							Collections.sort(exec, new Comparator<JJTestcaseexecution>() {

								@Override
								public int compare(JJTestcaseexecution o1, JJTestcaseexecution o2) {									
									return o2.getUpdatedDate().compareTo(o1.getUpdatedDate());
									}
							});		
							if (exec.get(0).getPassed()) {
								compteurSuccess++;	
								testcase.setSuccess("compteurSuccess");
							} else {
								compteurFailed++;
								testcase.setSuccess("compteurFailed");
							}
						}
					}
				}

				mapSuccessTC.put(date, String.valueOf(compteurSuccess));
				mapFailedTC.put(date, String.valueOf(compteurFailed));

			} else {
				compteurFailed = Integer.parseInt(mapFailedTC.get(dates
						.get(i - 1)));
				compteurSuccess = Integer.parseInt(mapSuccessTC.get(dates
						.get(i - 1)));
				mapTotalTC.put(
						date,
						String.valueOf(compteur
								+ Integer.parseInt(mapTotalTC.get(dates
										.get(i - 1)))));

				for (TestCaseChartUtil testcase : testcases) {
					List<JJTestcaseexecution> exec = new ArrayList<JJTestcaseexecution>();
					for (JJTestcaseexecution tce : testcaseexecutions) {
						Date updatedDate = tce.getUpdatedDate();
						if ((updatedDate != null)
								&& (updatedDate.toString().substring(0, 10)
										.equals(date))
								&& (tce.getTestcase().equals(testcase.getTestcase()))) {

							if (tce.getPassed() != null) {
								exec.add(tce);							
							}

						}
					}
					if (!exec.isEmpty()) {
						if (exec.size() == 1) {
							if (exec.get(0).getPassed()) {								
								if(testcase.getSuccess()!=null)
								{
									if(testcase.getSuccess().equalsIgnoreCase("compteurFailed"))
									{
										compteurFailed--;compteurSuccess++;
										testcase.setSuccess("compteurSuccess");
									}
								}else
								{
									compteurSuccess++;
									testcase.setSuccess("compteurSuccess");
								}
								
							} else {
								if(testcase.getSuccess()!=null)
								{
									if(testcase.getSuccess().equalsIgnoreCase("compteurSuccess"))
									{
										compteurFailed++;compteurSuccess--;
										testcase.setSuccess("compteurFailed");
									}
								}else
								{
									compteurFailed++;
									testcase.setSuccess("compteurFailed");
								}
							}
						} else {
							Collections.sort(exec, new Comparator<JJTestcaseexecution>() {

								@Override
								public int compare(JJTestcaseexecution o1, JJTestcaseexecution o2) {									
									return o2.getUpdatedDate().compareTo(o1.getUpdatedDate());
									}
							});	
							if (exec.get(0).getPassed()) {								
								if(testcase.getSuccess()!=null)
								{
									if(testcase.getSuccess().equalsIgnoreCase("compteurFailed"))
									{
										compteurFailed--;compteurSuccess++;
										testcase.setSuccess("compteurSuccess");
									}
								}else
								{
									compteurSuccess++;
									testcase.setSuccess("compteurSuccess");
								}
								
							} else {
								if(testcase.getSuccess()!=null)
								{
									if(testcase.getSuccess().equalsIgnoreCase("compteurSuccess"))
									{
										compteurFailed++;compteurSuccess--;
										testcase.setSuccess("compteurFailed");
									}
								}else
								{
									compteurFailed++;
									testcase.setSuccess("compteurFailed");
								}
							}						
						}
					}
				}
				if(compteurFailed<0)
					compteurFailed=0;
				if(compteurSuccess<0)
					compteurSuccess=0;

				mapSuccessTC.put(date, String.valueOf(compteurSuccess));
				mapFailedTC.put(date, String.valueOf(compteurFailed));

			}
		}

		ChartSeries totalTC = new ChartSeries("Total TC");
		if (mapTotalTC.isEmpty()) {
			totalTC.set("0", 0);
		} else

			for (Map.Entry<String, String> entry : mapTotalTC.entrySet()) {
				totalTC.set(entry.getKey(), Integer.parseInt(entry.getValue()));
			}

		ChartSeries successTC = new ChartSeries("Success TC");
		if (mapSuccessTC.isEmpty()) {
			successTC.set("0", 0);
		} else

			for (Map.Entry<String, String> entry : mapSuccessTC.entrySet()) {
				successTC.set(entry.getKey(),
						Integer.parseInt(entry.getValue()));
			}

		ChartSeries failedTC = new ChartSeries("Failed TC");
		if (mapFailedTC.isEmpty()) {
			failedTC.set("0", 0);
		} else

			for (Map.Entry<String, String> entry : mapFailedTC.entrySet()) {
				failedTC.set(entry.getKey(), Integer.parseInt(entry.getValue()));
			}

		categoryModel = new CartesianChartModel();
		categoryModel.addSeries(totalTC);
		categoryModel.addSeries(successTC);
		categoryModel.addSeries(failedTC);

	}

	public String getSeriesColors() {
		return seriesColors;
	}

	public void setSeriesColors(String seriesColors) {
		this.seriesColors = seriesColors;
	}
}

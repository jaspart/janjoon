package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;

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
		System.out.println("In model");
		createCategoryModel();
		System.out.println("Fin model");
	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	private void createCategoryModel() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		List<JJTestcase> testcases = jJTestcaseBean.getTestcases();

		Set<String> datesTMP = new HashSet<String>();

		for (JJTestcase testcase : testcases) {
			Date creationdate = testcase.getCreationDate();
			String date = creationdate.toString().substring(0, 10);
			System.out.println(date);

			datesTMP.add(date);

		}

		List<String> dates = new ArrayList<String>();
		dates.addAll(datesTMP);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		if (jJTestcaseexecutionBean == null) {
			jJTestcaseexecutionBean = new JJTestcaseexecutionBean();
		}

		Set<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionBean
				.getTestcaseexecutions();

		for (String date : dates) {
			System.out.println("date " + date);
		}

		Map<String, String> mapTotalTC = new LinkedHashMap<String, String>();
		Map<String, String> mapSuccessTC = new LinkedHashMap<String, String>();
		Map<String, String> mapFailedTC = new LinkedHashMap<String, String>();

		for (int i = 0; i < dates.size(); i++) {
			String date = dates.get(i);
			int compteur = 0;

			int compteurSuccess = 0;
			int compteurFailed = 0;

			for (JJTestcase testcase : testcases) {
				String creationDate = testcase.getCreationDate().toString()
						.substring(0, 10);
				if (creationDate.equalsIgnoreCase(date)) {
					compteur++;
				}

			}

			if (i == 0) {
				mapTotalTC.put(date, String.valueOf(compteur));

				for (JJTestcase testcase : testcases) {
					for (JJTestcaseexecution tce : testcaseexecutions) {
						Date updatedDate = tce.getUpdatedDate();
						if ((updatedDate != null)
								&& (updatedDate.toString().substring(0, 10)
										.equals(date))
								&& (tce.getTestcase().equals(testcase))) {

							if (tce.getPassed() != null) {
								if (tce.getPassed()) {
									compteurSuccess++;
								} else {
									compteurFailed++;
								}
							}
							break;

						}
					}
				}

				mapSuccessTC.put(date, String.valueOf(compteurSuccess));
				mapFailedTC.put(date, String.valueOf(compteurFailed));

			} else {

				mapTotalTC.put(
						date,
						String.valueOf(compteur
								+ Integer.parseInt(mapTotalTC.get(dates
										.get(i - 1)))));

				for (JJTestcase testcase : testcases) {
					for (JJTestcaseexecution tce : testcaseexecutions) {
						Date updatedDate = tce.getUpdatedDate();
						if ((updatedDate != null)
								&& (updatedDate.toString().substring(0, 10)
										.equals(date))
								&& (tce.getTestcase().equals(testcase))) {

							if (tce.getPassed() != null) {
								if (tce.getPassed()) {
									compteurSuccess++;
									compteurFailed--;
								} else {
									compteurSuccess--;
									compteurFailed++;
								}
							}
							break;

						}
					}
				}

				mapSuccessTC.put(
						date,
						String.valueOf(compteurSuccess
								+ Integer.parseInt(mapSuccessTC.get(dates
										.get(i - 1)))));
				mapFailedTC.put(
						date,
						String.valueOf(compteurFailed
								+ Integer.parseInt(mapFailedTC.get(dates
										.get(i - 1)))));

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

		// for (String date : dates) {
		// int compteur = 0;
		// System.out.println("date " + date);
		//
		// for (JJTestcaseexecution testcaseexecution : testcaseexecutions) {
		// String creationDate = testcaseexecution.getCreationDate()
		// .toString().substring(0, 10);
		// if (creationDate.equalsIgnoreCase(date)
		// && (testcaseexecution.getPassed() == null)) {
		// compteur++;
		// }
		// }
		// totalTC.set(date, compteur);
		// }

		// ChartSeries girls = new ChartSeries("Girls");
		//
		// girls.set("2004", 52);
		// girls.set("2005", 60);
		// girls.set("2006", 110);
		// girls.set("2007", 135);
		// girls.set("2008", 120);

		categoryModel = new CartesianChartModel();
		categoryModel.addSeries(totalTC);
		categoryModel.addSeries(successTC);
		categoryModel.addSeries(failedTC);
		// categoryModel.addSeries(girls);
	}

	public String getSeriesColors() {
		return seriesColors;
	}

	public void setSeriesColors(String seriesColors) {
		this.seriesColors = seriesColors;
	}
}

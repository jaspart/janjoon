package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

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

		System.out.println("Start");

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		if (jJTestcaseexecutionBean == null) {
			jJTestcaseexecutionBean = new JJTestcaseexecutionBean();
		}

		Set<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionBean
				.getTestcaseexecutions();

		Set<String> dates = new HashSet<String>();

		for (JJTestcaseexecution testcaseexecution : testcaseexecutions) {
			Date creationdate = testcaseexecution.getCreationDate();
			String dateWithoutTime = creationdate.toString();
			System.out.println(dateWithoutTime);

			dates.add(dateWithoutTime);

		}

		for (String date : dates) {
			System.out.println("date " + date);
		}

		System.out.println("Fin");

		categoryModel = new CartesianChartModel();

		ChartSeries nonexe = new ChartSeries("NON EXE");

		for (String date : dates) {
			int compteur = 0;
			System.out.println("date " + date);

			for (JJTestcaseexecution testcaseexecution : testcaseexecutions) {
				String creationDate = testcaseexecution.getCreationDate()
						.toString();
				if (creationDate.equalsIgnoreCase(date)
						&& (testcaseexecution.getPassed() == null)) {
					compteur++;
				}
			}
			nonexe.set(date, compteur);
		}

		if (dates.isEmpty()) {
			nonexe.set("0", 0);
		}

		// ChartSeries girls = new ChartSeries("Girls");
		//
		// girls.set("2004", 52);
		// girls.set("2005", 60);
		// girls.set("2006", 110);
		// girls.set("2007", 135);
		// girls.set("2008", 120);

		categoryModel.addSeries(nonexe);
		// categoryModel.addSeries(girls);
	}

	public String getSeriesColors() {
		return seriesColors;
	}

	public void setSeriesColors(String seriesColors) {
		this.seriesColors = seriesColors;
	}
}

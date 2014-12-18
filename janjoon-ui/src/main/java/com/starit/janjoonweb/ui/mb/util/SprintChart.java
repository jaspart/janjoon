package com.starit.janjoonweb.ui.mb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class SprintChart {
	
	private CartesianChartModel chartModel;
	private JJSprint sprint;
	
	public SprintChart(JJSprint sprint, List<JJTask> tasks) {
		this.chartModel=new CartesianChartModel();		
		this.sprint = sprint;			
		initChartModel(tasks);
	}
	private void initChartModel(List<JJTask> tasks) {
		
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dates=new ArrayList<String>();
		int workload=0;	int workload2=0;
		dates.add(f.format(sprint.getEndDate()));
		
		for(JJTask task:tasks)
		{
			if(task.getWorkloadPlanned() != null)
				workload=workload+task.getWorkloadPlanned();
			if(task.getEndDateReal() != null)
				dates.add(f.format(task.getEndDateReal()));
		}	
		
		Set<String> set=new HashSet<String>();
		set.addAll(dates) ;
		dates=new ArrayList<String>(set);
		Collections.sort(dates, new Comparator<String>() {
	        DateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
	        @Override
	        public int compare(String o1, String o2) {
	            try {
	                return ff.parse(o1).compareTo(ff.parse(o2));
	            } catch (ParseException e) {
	                throw new IllegalArgumentException(e);
	            }
	        }
	    });	
		workload2=workload;
		ChartSeries chartSeries=new ChartSeries();		
		LineChartSeries lineSeries=new LineChartSeries();
		ContactCalendarUtil calendar=new ContactCalendarUtil(((LoginBean)LoginBean.findBean("loginBean")).getContact().getCompany());
		chartSeries.set(f.format(sprint.getStartDate()), workload);
		lineSeries.set(f.format(sprint.getStartDate()),workload2);
		chartSeries.setLabel(sprint.getName()+" Workload");	
		lineSeries.setLabel("BurnDown Ideal");		
		for(String date:dates)
		{
			for(JJTask task:tasks)
			{
				if(task.getEndDateReal() != null)
				if(f.format(task.getEndDateReal()).equalsIgnoreCase(date) && task.getWorkloadPlanned() != null)
				{
					workload=workload-task.getWorkloadPlanned();				
				}				
			}
			chartSeries.set(date, workload);
			try {
				int diff=workload2-calendar.calculateWorkLoad(sprint.getStartDate(),calendar.nextWorkingDate(f.parse(date)));
				if(diff<0)
					diff=0;
				lineSeries.set(date,diff);
			} catch (ParseException e) {			
			}
		}		
		chartModel.addSeries(chartSeries);
		chartModel.addSeries(lineSeries);
	}
	
	public CartesianChartModel getChartModel() {
		return chartModel;
	}
	public void setChartModel(CartesianChartModel chartModel) {
		this.chartModel = chartModel;
	}
	public JJSprint getSprint() {
		return sprint;
	}
	public void setSprint(JJSprint sprint) {
		this.sprint = sprint;
	}
	
	public static List<SprintChart> generateSprintChartList(
			List<JJSprint> sprints, JJTaskService jJTaskService) {
		List<SprintChart> sprintUtils = null;
		if (!sprints.isEmpty()) {
			sprintUtils = new ArrayList<SprintChart>();
			for (JJSprint s : sprints) {
				SprintChart ss = new SprintChart(s,
						jJTaskService.getSprintTasks(s));
				sprintUtils.add(ss);
			}
		}
		return sprintUtils;
	}

}

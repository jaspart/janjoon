package com.starit.janjoonweb.ui.mb.util.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mysql.fabric.xmlrpc.base.Data;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;

public class TestClass {

	public static void main(String[] args) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
//		Properties pro1=new Properties();
//		pro1.load( new FileInputStream(TestClass.class.getResource("/starItCalendar.properties").getFile()));
//		
//		System.out.println(pro1.getProperty("weekday.1").trim().indexOf("&"));
//		System.out.println(pro1.getProperty("weekday.1").trim());
//		System.out.println(pro1.getProperty("weekday.1"));
//		String workDay=pro1.getProperty("weekday.1");
//		
//		System.out.println(workDay.substring(0,5));
//		System.out.println(workDay.substring(6,11));
//		System.out.println(workDay.substring(14,19));
//		System.out.println(workDay.substring(20,25));
//		int i=1;
//		boolean nullPointerException=false;
//		while(!nullPointerException)
//		{
//			try {
//				System.out.println(pro.getProperty("holiday."+i).toString().trim());
//				i++;
//				
//			} catch (NullPointerException e) {
//				nullPointerException=true;
//			}
//		}
//		
		
//		List<Date> workingDay=new ArrayList<Date>();
//		int i=1;
//		while(i<8)
//		{
//			String s=pro.get("weekday."+i).toString().trim();
//			if(!s.isEmpty())
//			{
//				
//				s=s.substring(0,s.indexOf("&")-1);
//				//Date t=new Date(Date.parse(s.substring(0,s.indexOf("-")-1)));
//				//workingDay.add(t);
//				System.out.println(s.substring(0,s.indexOf("-")));
//				
//				
//			}
//			
//			i++;
//		}
//		CalendarUtil calendarUtil=new CalendarUtil(null);
//		calendarUtil.initCompanyCalendar(TestClass.class.getResource("/starItCalendar.properties").getFile());
//		int i=0;
//		SimpleDateFormat output = new SimpleDateFormat("HH:mm");
//		while(i<7)
//		{
//			if(i != 0 && i != 6)
//			System.out.println(output.format(calendarUtil.getWorkDays().get(i).getStartDate1()));
//			i++;
//		}
//		output=new SimpleDateFormat("dd/MM/yyyy");
//		i=0;
//		while (i<calendarUtil.getHolidays().size())
//		{
//			System.out.println(output.format(calendarUtil.getHolidays().get(i)));
//			i++;
//		}
		
//		Date date=new Date();
//		Date start=new Date(date.getTime()+ (1000 * 60 * 60 * 50));
//
//		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
//
//		int chunkNumber= (int) ((start.getTime() - date.getTime())/ DAY_IN_MILLIS );
//		if(((start.getTime() - date.getTime())% DAY_IN_MILLIS )!= 0)
//			chunkNumber=chunkNumber+1;
//		System.out.println(chunkNumber);
		
		//@SuppressWarnings("deprecation")
		Date date=new Date();
//		
//		Calendar cal = Calendar.getInstance();
//	    cal.setTime(date);
//	    cal.set(Calendar.DAY_OF_MONTH, 1);
//	    cal.add(Calendar.DAY_OF_MONTH, -1);
//	   date=cal.getTime();
//	   System.out.println(date);
	   
		
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(1,0, 1);		
		date=calendar.getTime();
		System.out.println(date);
		
		
		
		
	}

}

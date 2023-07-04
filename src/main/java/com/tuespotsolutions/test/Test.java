package com.tuespotsolutions.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Test {

	@SuppressWarnings("deprecation")
	public static void main(String[] args)  {
//		java.util.Date utilDate = new java.util.Date();
//		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
//		SimpleDateFormat  timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		timeStamp.setTimeZone(istTimeZone);
//		String format = timeStamp.format(utilDate);
//		
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date()); // Using today's date
//		c.add(Calendar.DATE, 5); // Adding 5 days
//		String format2 = timeStamp.format(c.getTime());
//		System.out.println("timeStamp "+format);
//		
//		
//		Date parse;
		try {
//			parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format);
//			System.out.println("real date "+parse);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
		
//		String datetime = "2023-03-07 16:35:42";
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//		SimpleDateFormat formatters = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
//		try {
//			Date parse = formatter.parse(datetime);
//			System.out.println(parse.getTime());
//			 //String format = formatters.format(parse.getTime());
//			
//			 Date now = new Date();
//			 long nowTime = now.getTime();
//			 long time = parse.getTime();
//			long total =  nowTime - time;
//			
//			long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(total);
//			long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(total);
//			long diffInHours = TimeUnit.MILLISECONDS.toHours(total);
//			long diffInDays = TimeUnit.MILLISECONDS.toDays(total);
//			
//			System.err.println(diffInHours);
			 
			//System.out.println(format);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		    Date dateBefore = sdf.parse("04/21/2022");
		    Date dateAfter = sdf.parse("04/25/2022");

		// Calculate the number of days between dates
		    long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
		    long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		    System.out.println("The number of days between dates: " + daysDiff);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

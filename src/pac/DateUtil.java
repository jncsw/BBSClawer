package pac;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
 
public class DateUtil {
 
    public static void main(String[] args) throws ParseException, java.text.ParseException {
        DateUtil du = new DateUtil();
        long time = du.stringToLong("2012-10-15 8:44:53", "yyyy-MM-dd hh:mm:ss")/1000;
        long time1 = du.stringToLong("2012-10-15 20:44:53", "yyyy-MM-dd hh:mm:ss")/1000;
        String date = du.longToString(1350470693,"yyyy-MM-dd hh:mm:ss" );
        System.out.println(time);
        System.out.println(time1);
        System.out.println(date);
         
 
 
    }
      public static String dateToString(Date data, String formatType) {
      return new SimpleDateFormat(formatType).format(data);
      }
      
      public static String longToString(long currentTime, String formatType)
      throws ParseException, java.text.ParseException {
      Date date = longToDate(currentTime, formatType);  
      String strTime = dateToString(date, formatType);  
      return strTime;
      }
      
      public static Date stringToDate(String strTime, String formatType)
      throws ParseException, java.text.ParseException {
      SimpleDateFormat formatter = new SimpleDateFormat(formatType);
      Date date = null;
      date = formatter.parse(strTime);
      return date;
      }
      
      public static Date longToDate(long currentTime, String formatType)
      throws ParseException, java.text.ParseException {
      Date dateOld = new Date(currentTime); 
      String sDateTime = dateToString(dateOld, formatType); 
      Date date = stringToDate(sDateTime, formatType); 
      return date;
      }
      
      public static long stringToLong(String strTime, String formatType)
      throws ParseException, java.text.ParseException {
      Date date = stringToDate(strTime, formatType); 
      if (date == null) {
      return 0;
      } else {
      long currentTime = dateToLong(date); 
      return currentTime;
      }
      }
      
      public static long dateToLong(Date date) {
      return date.getTime();
      }
      public static String numToDate(int number,String formatType){
          Date date = new Date(number);
          SimpleDateFormat sdf = new SimpleDateFormat(formatType);
          return sdf.format(date);
      }
 
}
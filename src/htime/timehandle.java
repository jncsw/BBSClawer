package htime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;

import Gcont.HExtractorImp;
import Gcont.HResult;
import content.CExtract;


public class timehandle {
	public static String handleTime(String time){
		String t2 ="";
		if(time.contains("年")){
			int year = Integer.parseInt(time.substring(0,time.indexOf("年")));
			int mon = Integer.parseInt(time.substring(time.indexOf("年")+1,time.indexOf("月")));
			int dat = Integer.parseInt(time.substring(time.indexOf("月")+1,time.indexOf("日")));
			t2 = year+"-"+mon+"-"+dat;
		}else t2=time;
		Date d = new Date();
		String pat1 = "yyyy-MM-dd";
		SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;
		try {
			d = sdf1.parse(t2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(d);
		return dateString;
	}
	public static String getTime(Document doc){
		
		String ans1="",ans2="",ans3=""; 
		//way1
		try {
			String ti = time.getTime(doc);
			ans1 = handleTime(ti);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		//way2
		ans2="";
		
		
		try {
			ans2 = CExtract.getNewsByDoc(doc).getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//way3
		try {
			HExtractorImp e = new HExtractorImp();
			HResult r = e.extractContent(doc);
			ans3 = "";
			if (r != null) 
			{
				if (r.getState() == "ok") 
				{
					ans3=r.getDate();
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!ans1.equals("")) return ans1;
		if(!ans2.equals("")) return ans2;
		return ans3;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(handleTime("2016-1-01 15:00:01"));
	}

}

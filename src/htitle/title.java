package htitle;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;

public class title {
	private static String getMaxString(String str1, String str2) {
		String max = null;
		String min = null;
		max = (str1.length() > str2.length() ? str1 : str2);
		min = max.equals(str1) ? str2 : str1;
		for (int i = 0; i < min.length(); i++) {
			for (int start = 0, end = min.length() - i; end != min.length() + 1; start++, end++) {
				String sub = min.substring(start, end);
				if (max.contains(sub))
					return sub;
			}
		}
		return null;
	}
	static Document doc;
	public static String dealingTitle(Document d){
		try{
			//System.out.println(url);
		   doc=d;
		   doc.select("script").remove();
	        doc.select("style").remove();
		   String title=doc.getElementsByTag("title").first().text();
		   Elements hh1=doc.getElementsByTag("h1");
		   String h1=hh1.text();
		   System.out.println("title:"+title);
		   System.out.println("h1:"+h1);
		   
		   if(h1.equals("")) 
		   {
		 	     if(title.contains("-"))
		 		  h1=title.split("-")[0];
		 	     else if(title.contains("_"))
		 	    	 h1=title.split("_")[0];
		 	     else h1=title;
		   }
		   if(title.equals("")) 
		   {
			   
			   
		 	     if(h1.contains("-"))
		 	    	title=h1.split("-")[0];
		 	     else if(h1.contains("_"))
		 	    	title=h1.split("_")[0];
		 	     else title=h1;
		   }
		   String Common=getMaxString(title,h1);
		   if(Common==null)
		   {
			   if(title!=null) Common=title;
			   if(h1!=null) Common=h1;
		   }
		   return Common;
		}
		catch(Exception e)
		{
		//e.printStackTrace();
		System.out.println("*******************************发生错误****************");
		}
		return "";
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 String fileurl="D:/bbs_urls.txt";
	     File file=new File(fileurl);
	     InputStreamReader read=new InputStreamReader(new FileInputStream(file));
	     BufferedReader bfr=new BufferedReader(read);
	     List<String> list=new ArrayList<String>();
	     String text=null;
	     while((text=bfr.readLine())!=null)
	    	 {
	    	 list.add(text);
	    	 }
	 	
	for(int i=0;i<list.size();i++)
		{
		String url = list.get(i);
		//dealingTitle(url);
		
	}

}
}

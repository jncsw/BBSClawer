package hwriter;


import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class classauthi {
	public static String getA(Document d){
		Document doc = null;
	
			 doc = d;
		
		String s  ="";
		try{
			
			s  = doc.getElementsByClass("personinfo-name").first().text();
			
		}catch(Exception e){
			
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("lt_oneboxL_avatar").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("authi").first().text();
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("user_name").first().text();
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("author").first().text();
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("postauthor").first().text();
				
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("postinfo").first().toString();
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("username").first().text();
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("name").first().text();
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("xm_tp_action").first().text();
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("pi").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("ilt_name").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("tit").first().text(); 
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("user-info").first().text(); 
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("name_txt").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("readName").first().text(); 
			}catch(Exception e){
				
			}
		}
		

		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("zhanzhuai_authi").first().text(); 
			}catch(Exception e){
				
			}
		}
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("t_user").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("t_user").first().text(); 
			}catch(Exception e){
				
			}
		}

		if(s.equals("")){
			try{
				
				s  = doc.getElementsByClass("user_center_name").first().text(); 
			}catch(Exception e){
				
			}
		}
		
		Scanner in = new Scanner(s);
		if(in.hasNext())
		s=in.next();
		if(!s.equals("")){
	        s = s.replaceAll( "在线|离线","");//.replaceAll( ",122.jpg|122.jpg,","");  
			
			
		}
		return s;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(getA("http://bbs1.people.com.cn/post/129/1/2/159391004.html"));
	}

}

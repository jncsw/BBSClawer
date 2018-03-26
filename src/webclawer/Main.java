package webclawer;


import net.sf.json.JSON;  
import net.sf.json.JSONArray;  
import net.sf.json.JSONObject;  
import net.sf.json.JSONSerializer;

import java.io.FileWriter;
import java.io.IOException;

import javax.print.Doc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import bean.Json;
import bean.post;
import bean.replys;
import content.hcontent;
import htime.time;
import htime.timehandle;
import htitle.title;
import htitle.titlehandle;
import hwriter.hwriter;
import net.sf.json.JSONArray;


public class Main{
	
	static Document doc;
	
    public static Document getDoc() {
		return doc;
	}

	public static void setDoc(Document doc) {
		Main.doc = doc;
	}

	public static void main(String[] args){
    	String url = "http://bbs.sh021.cc/thread-193234-1-1.html";
    	post p = new post();
    	Json j = new Json();
    	
    	try {
			 doc = Jsoup.connect(url)
			  .data("query",
			"Java")
			  .userAgent("Mozilla")
			  .cookie("auth",
			"token")
			  .timeout(3000)
			  .get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	doc.select("script").remove();
        doc.select("style").remove();
        doc.select("iframe").remove();
        doc.select("ul").remove();
        doc.select("li").remove();
        doc.select("img").remove();

    	p.setTitle(titlehandle.getTitle(doc).toString());
    	p.setAuthor(hwriter.getWriter(doc));
    	p.setPublish_date(timehandle.getTime(doc));
    	p.setContent(hcontent.getCont(doc));
    	j.setPost(p);
    	
    	String tsting = doc.toString();
		String  s  = p.getTitle(); 
		tsting = tsting.replaceAll(s,"");
		
		
		s = p.getAuthor();
		if(tsting.indexOf(s)!=-1)
		tsting=tsting.substring(0,tsting.indexOf(s))+tsting.substring(tsting.indexOf(s)+s.length(),tsting.length());
		s = p.getPublish_date();
		if(tsting.indexOf(s)!=-1)
		tsting=tsting.substring(0,tsting.indexOf(s))+tsting.substring(tsting.indexOf(s)+s.length(),tsting.length());
		s = p.getContent();
		String sc1 = s.substring(0,5);
		String sc2 = s.substring(s.length()-5,s.length());
		if(tsting.indexOf(sc1)!=-1 && tsting.indexOf(sc2)!=-1)
		tsting=tsting.substring(0,tsting.indexOf(sc1))+tsting.substring(tsting.indexOf(sc2)+sc2.length(),tsting.length());
		
		doc = Jsoup.parse(tsting);
    		int cnt = 0;
    	while(true){
    		cnt++;
    		replys re = new replys();
    		re.setAuthor(hwriter.getWriter(doc));
    		re.setContent(hcontent.getCont(doc));
    		re.setPublish_date(timehandle.getTime(doc));
    		re.setTitle(titlehandle.getTitle(doc).toString());
    		if(re.getAuthor()==null || re.getAuthor().equals("") || re.getContent()==null || re.getContent().equals(""))break;
    		j.addReply(re);
    		if(cnt>=20)break;
    	}
    	String ans = url+"\r\n"+j.getJson();
    	
    	FileWriter fw;
		try {
			fw = new FileWriter("D:/taidi.txt");
			fw.write(ans);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
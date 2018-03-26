package hwriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class classwri2 {
	public static String getAuthor(Document d){
		ArrayList<String> arr = new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		Document doc=null;
		
		 
			doc = d;
	
		Elements newsHeadlines = doc.select("*");
		String classname="",idname="";
		for (Element element : newsHeadlines) {
		  {
			  classname=element.className();
			  idname=element.id();
			  if(!classname.equals(""))arr.add(classname);
			  if(!idname.equals(""))arr.add(idname);
			  // 标签名
		  }
		}
		
		String pattern="^.*person.*$|^.*oneboxL.*$|^.*authi.*$|^.*name.*$|^.*user.*$|^.*author.*$|^.*auth.*$|^.*postauthor.*$|^.*postinfo.*$|^.*username.*$|^.*xm_tp_action.*$|^.*pi.*$|^.*ilt_name.*$|^.*tit.*$|^.*user-info.*$|^.*readName.*$|^.*zhanzhuai_authi.*$|^.*t_user.*$|^.*user_center_name.*$";
		Matcher ma=null;
		
		
		
		
		String tmp = "";
		Pattern pr = Pattern.compile(pattern);				
		int sz = arr.size();
		for(int i=0;i<sz;i++){
			ma = pr.matcher(arr.get(i));
			if(ma.find()){
				try {
					tmp=doc.getElementsByClass(arr.get(i)).first().text();
					if(!tmp.equals(""))
					res.add(tmp);
				} catch (Exception e) {
					// TODO: handle exception
					continue;
				}
				try {
					
					tmp=doc.getElementById(arr.get(i)).text();
					if(!tmp.equals(""))
					res.add(tmp);
				} catch (Exception e) {
					// TODO: handle exception
					continue;
				}
			}
		}
		sz = res.size();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < sz; i++) {
			if (map.containsKey(res.get(i))) {
				int val = map.get(res.get(i)) + 1;
				map.put(res.get(i), val);
			} else {
				map.put(res.get(i), 1);
			}
		}
		int maxc = 0;
		int sumc = 0;
		String ret = "";
		for (Entry<String, Integer> entry : map.entrySet()) {
			  if(entry.getValue()>maxc){
				  maxc = entry.getValue();
				  sumc+= entry.getValue();
				  ret = entry.getKey() ;
			  }
		  
		}  
		System.out.println("ret= "+ret);
		int incx = (int)(sumc*0.8);
		String a2 = classauthi.getA(d);
		System.out.println("a2=" + a2);
		if (map.containsKey(a2)) {
			int val = map.get(a2) + incx;
			map.put(a2, val);
		} else {
			map.put(a2, incx);
		}
		
		for (Entry<String, Integer> entry : map.entrySet()) {
			  if(entry.getValue()>maxc){
				  maxc = entry.getValue();
				  sumc+= entry.getValue();
				  ret = entry.getKey() ;
			  }
		  
		}  
		
		
		
		return ret;
		
		
		
	}

		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(getAuthor("http://bbs.huanqiu.com/thread-3877830-1-1.html"));

	}

}

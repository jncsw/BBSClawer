package bean;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Json {
	public static   JSONObject json,jpost;
	public static ArrayList<replys> re = new ArrayList<replys>();
	public static void setPost(post p){
		 jpost = new JSONObject();
		 jpost.put("author", p.author);
		 jpost.put("title", p.title);
		 jpost.put("content", p.content);
		 jpost.put("publish_date", p.publish_date);
		 
	}
	public static void addReply(replys rep){
		re.add(rep);
	}
	public static String getJson(){
		json = new JSONObject();
		json.put("post", jpost);
		 JSONArray result = JSONArray.fromObject(re);  
		json.put("replys", result);
		return json.toString();
	}
	
}

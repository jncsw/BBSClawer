package content;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.nodes.Document;


public class Textcont {
	
	public static String getC(Document doc) {
		
		String content=doc.html();
		TExtract.setthreshold(86);  
		System.out.println(1111);
		System.out.println(TExtract.parse(content));
		return TExtract.parse(content);
	}

	public static String getHTML(String strURL) throws IOException {
		URL url = new URL(strURL);
		
        String line = "";  
        StringBuffer buffer = new StringBuffer();  
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();  
        String contentType="";
        try {
			
        	int responsecode = urlConnection.getResponseCode();  
        	contentType = urlConnection.getContentType(); 
        	contentType=contentType.substring(contentType.lastIndexOf("=")+1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),contentType));
		String ans = "";
		String s = "";
		while ((s = br.readLine()) != null) {
			ans+=s;
		}
		try{
			byte[] temp=ans.getBytes(contentType);
			ans=new String(temp,"utf-8");
				}catch (UnsupportedEncodingException e) {
			}
		return ans;
	}
}


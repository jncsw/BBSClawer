package hwriter;

import org.jsoup.nodes.Document;

import Gcont.HExtractorImp;
import Gcont.HResult;

public class hwriter {
	public static String getWriter(Document doc){
		String ans0="",ans1="",ans2="",ans3=""; 
		//way0
		try {
			
			ans0 = classwri2.getAuthor(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
    	//way1
		try {
			
			ans1 = classauthi.getA(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//way2		
		try {
			
			HExtractorImp e = new HExtractorImp();
			HResult r = e.extractContent(doc);
			
			if (r != null) 
			{
				ans2 =  r.getAuth();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(ans0==null)ans0="";
		if(ans1==null)ans1="";
		if(ans2==null)ans2="";
		if(!ans0.equals("")) return ans0;
		if(!ans1.equals("")) return ans1;
		if(!ans2.equals("")) return ans2;
		return "";
			
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

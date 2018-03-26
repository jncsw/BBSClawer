package content;

import org.jsoup.nodes.Document;

import Gcont.HExtractorImp;
import Gcont.HResult;

public class hcontent {
	public static String getC1(Document d){
		String content="";
		try {
			content = CExtract.getContentByDoc(d);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}
	public static String getC3(Document doc){
		HExtractorImp e = new HExtractorImp();
        HResult r = e.extractContent(doc);
        String ans = "";
		if (r != null) 
		{
		    if (r.getState() == "ok") 
			ans= r.getText();
		}
		return ans.trim();
			
	}
	public static String getC2(Document doc){
		String content="";
		try {
			content = CExtract.getNewsByDoc(doc).getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}
	public static String getCont(Document doc){
		String ans1="",ans2="",ans3="",ans4=""; 
		try {
			ans1 = getC1(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			
			ans2 = getC2(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			ans3 = getC3(doc);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			ans4 = Textcont.getC(doc);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(ans1);
		System.out.println(ans2);
		System.out.println(ans3);
		System.out.println(ans4);
		if(!ans1.equals("")) return ans1;
		if(!ans2.equals("")) return ans2;
		if(!ans3.equals("")) return ans3;
		return ans4;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}

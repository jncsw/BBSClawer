package htitle;

import org.jsoup.nodes.Document;

import Gcont.HExtractorImp;
import Gcont.HResult;
import content.CExtract;

public class titlehandle {
	public static String getTitle(Document doc){
		
		String ans1="",ans2="",ans3=""; 
		
		//way1
		try {
			
			ans1=title.dealingTitle(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//way2
		try {
			ans2 = CExtract.getNewsByDoc(doc).getTitle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//way3
		try {
			
			HExtractorImp e = new HExtractorImp();
			HResult r = e.extractContent(doc);
			if (r != null) 
			{
				if (r.getState() == "ok") 
				{
					ans3=r.getTitle();
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
		
	}

}

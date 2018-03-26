package content;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HBt {

    public static DPage getDomPageByURL(Document d) throws IOException {
        Document doc=d;
        return new DPage(doc);
    }

    public static DPage getDomPageByHtml(String html){
        return getDomPageByHtml(html,null);
    }

    public static DPage getDomPageByHtml(String html,String url){

        Document doc= Jsoup.parse(html);
        if(url!=null){
            doc.setBaseUri(url);
        }
        DPage domPage=new DPage(doc);
        return domPage;
    }
}

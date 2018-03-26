package content;


import org.jsoup.nodes.Document;

public class DPage {

    public DPage(Document doc) {
        this.doc = doc;
    }

    private Document doc=null;


    public String getURL(){
        return doc.baseUri();
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
}

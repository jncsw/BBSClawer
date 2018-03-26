package htime;



import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import content.DPage;
import content.GaussSmooth;
import content.HBt;
import content.JHelper;
import content.TextUtils;
import pac.DateUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class time {

    public static class CountInfo {

        TextNode tNode;
        public int textCount;
        public int puncCount;

        public CountInfo(TextNode tNode) {
            this.tNode = tNode;
            String text = tNode.text();
            this.textCount = TextUtils.countText(text);
            this.puncCount = TextUtils.countPunc(text);
        }
    }

    public static class ComputeInfo {

        double tpr;
        double ppr;
        double cs;
        double ps;
        double etpr;

        public ComputeInfo(double tpr, double ppr, double cs, double ps) {
            this.tpr = tpr;
            this.ppr = ppr;
            this.cs = cs;
            this.ps = ps;
            this.etpr = tpr * ppr * cs * ps;
        }
    }
    private DPage domPage;
    private Document doc;
    private ArrayList<TextNode> tNodeList = new ArrayList<TextNode>();
    private HashMap<TextNode, String> xpathMap = new HashMap<TextNode, String>();
    private HashMap<String, ArrayList<CountInfo>> countMap = new HashMap<String, ArrayList<CountInfo>>();
    private HashMap<String, ComputeInfo> computeMap = new HashMap<String, ComputeInfo>();
    private ArrayList<Double> etprList = new ArrayList<Double>();
    private ArrayList<Double> gaussEtprList = new ArrayList<Double>();
    private double threshold;

    private void clean() {
        doc.select("script").remove();
        doc.select("style").remove();
        doc.select("iframe").remove();
    }

    private double computeDeviation(ArrayList<Double> list) {
        if (list.size() == 0) {
            return 0;
        }
        double ave = 0;
        for (Double d : list) {
            ave += d;
        }
        ave = ave / list.size();
        double sum = 0;
        for (Double d : list) {
            sum += (d - ave) * (d - ave);
        }
        sum = sum / list.size();
        return Math.sqrt(sum);
    }

    private double computeThreshold() {
        double d = computeDeviation(gaussEtprList);
        return d * 0.8;
    }

    private ComputeInfo getComputeInfo(ArrayList<CountInfo> countInfoList) {
        double textSum = 0;
        double puncSum = 0;
        ArrayList<Double> textCountList = new ArrayList<Double>();
        ArrayList<Double> puncCountList = new ArrayList<Double>();
        for (CountInfo countInfo : countInfoList) {

            textSum += countInfo.textCount;
            puncSum += countInfo.puncCount;
            textCountList.add(countInfo.textCount + 0.0);
            puncCountList.add(countInfo.puncCount + 0.0);
        }
        double tpr = textSum / countInfoList.size();
        double ppr = puncSum / countInfoList.size();
        double cs = computeDeviation(textCountList);
        double ps = computeDeviation(puncCountList);
        return new ComputeInfo(tpr, ppr, cs, ps);
    }

    private void addTextNode(TextNode tNode) {

        String text = tNode.text().trim();
        if (text.isEmpty()) {
            return;
        }
        String xpath = JHelper.getXpath(tNode);
        tNodeList.add(tNode);
        xpathMap.put(tNode, xpath);

        CountInfo countInfo = new CountInfo(tNode);
        ArrayList<CountInfo> countInfoList = countMap.get(xpath);
        if (countInfoList == null) {
            countInfoList = new ArrayList<CountInfo>();
            countMap.put(xpath, countInfoList);
        }
        countInfoList.add(countInfo);
    }
    public static void getTree(Node d){
    	if(d.childNodeSize()==0)return;
    	else
    	{
    		int cnt = d.childNodeSize();
    		for(int i=0;i<cnt;i++){
    			for(int j=0;j<cnt;j++){
    				if(i==j)continue;
    				simTree(d.childNode(i),d.childNode(j));
    			}
    			getTree(d.childNode(i));
    		}
    	}
    	
    }

    private void buildHisto() {
    	
    	//getTree(doc);
    	
    	
    	try {
			FileWriter fw = new FileWriter("D:/123.ttt");
//			fw.write(doc.childNode(1).toString());
			fw.write("\r\n===================================================================\r\n");
			fw.write(doc.childNode(1).toString());
			fw.write("\r\n===================================================================\r\n");
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        doc.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int i) {
                if (node instanceof TextNode) {
                    TextNode tNode = (TextNode) node;
                    addTextNode(tNode);
                }
            }

            @Override
            public void tail(Node node, int i) {

            }
        });
        for (Map.Entry<String, ArrayList<CountInfo>> entry : countMap.entrySet()) {
            ComputeInfo computeInfo = getComputeInfo(entry.getValue());
            computeMap.put(entry.getKey(), computeInfo);
        }

        for (TextNode tNode : tNodeList) {
            String xpath = xpathMap.get(tNode);
            double etpr = computeMap.get(xpath).etpr;
            etprList.add(etpr);
        }
        gaussEtprList = GaussSmooth.gaussSmooth(etprList, 1);
        threshold = computeThreshold();
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tNodeList.size(); i++) {
            TextNode tNode = tNodeList.get(i);
            double gEtpr = gaussEtprList.get(i);
           // System.out.println(tNode.text().trim()+ "\n---------------------------------");
            String checkValue = tNode.text().trim();   
            Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);  
            Matcher matcher = p.matcher(checkValue);  
            if (matcher.find()){
            	return matcher.group(0);
            }
            
            if (gEtpr > threshold) {
                sb.append(tNode.text().trim() + "\n");
            }
        }
        return "";
    }
    public static String getContentByURL(Document dom) throws Exception {
        DPage domPage = HBt.getDomPageByURL(dom);
        time contentExtractor = new time(domPage);
        return contentExtractor.getContent();
    }

    public static String getContentByHtml(String html) throws Exception {
        DPage domPage = HBt.getDomPageByHtml(html);
        time contentExtractor = new time(domPage);
        return contentExtractor.getContent();
    }

    public time(DPage domPage) {
        this.domPage = domPage;
        this.doc = domPage.getDoc();
        clean();
        buildHisto();
    }
    
    
    public static void simTree(Node tree1,Node tree2)
    {
    }
    

    public static String getTime(Document d) {

        String content;
		try {
			content = time.getContentByURL(d);
			Scanner sin = new Scanner(content);
			String time  = sin.next();
			return time;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }	

}

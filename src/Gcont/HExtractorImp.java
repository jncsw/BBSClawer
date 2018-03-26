﻿package Gcont;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.jsoup.nodes.Document;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import javax.swing.text.html.HTMLDocument.Iterator;

public class HExtractorImp implements HExtractor {

    public HResult result = null;
    private int maxBytes = 1000000 / 2;
    public Pattern p;
    public Matcher m;
    public String format_html;  
    public Boolean DEBUG=false;
    @Override
    public HResult extractContent(Document doc) {
	String html = "";
	String temptitle = "";
	
	try {
	    html = doc.html();
	} catch (Exception e) {
	}
	if (html.length() == 0) {
	}
	String str = html.toString();
         str = str.toLowerCase();
	 str = str.replace("\r", "");
	 str = str.replace("\n", ""); 
	 
	 if (str != null)  
	 {
             p = Pattern.compile("<!--");
             m = p.matcher(str);
             int i=0;
             Pattern p2=Pattern.compile("-->");
             Matcher m2=p2.matcher(str);
             StringBuilder sb=new StringBuilder(str);
             int start=0,end=0;
	     while(m.find()&& m2.find())
	     {
		 if(m.start()>end && m2.end()>m.start())
		 {
		     start=m.start();
		     end=m2.end();
		 }
		 else if(m.start()<end)
		 {
		     m.find();
		 }
		 else if(m2.end()<m.start())
		 {
		     m2.find();
		 }
		 sb.delete(start-i, end-i);
		 i=i+(m2.end()-m.start());
		 
	     }
	     str=sb.toString();
	 }
	 ////System.out.println(str);
	 if(str!=null)  //根据网页的视觉呈现来添加回车换行符号
	 {
	     int i=1;
	     StringBuilder sb=new StringBuilder(str);
	     p = Pattern.compile("(<br>|<br />|</p>|</tr>|</table>|</form>|</div>|</head>)\\s*");
	     m = p.matcher(str);
	     while (m.find()) {
		 sb.insert(m.end()+i-1, "\n");
		 i++;
	     }
	     Pattern p2=Pattern.compile("<h1>|<h2>");
	     Matcher m2=p2.matcher(sb.toString());
	     i=0;
	     while(m2.find())
	     {
		 sb.insert(m2.end()+i-4, "\n\n");
		 i+=2;
	     }
             str=sb.toString();
	  }
	
	if (str != null) {
	    p = Pattern.compile(".*<title>(.*?)</title>.*");
	    m = p.matcher(str);
	    while (m.find()) {
		temptitle= m.group(1);
	    }
	}
	if (str != null) // 去除style标签
	{
	    p = Pattern.compile("(?is)<style[^>]*?>.*?<\\/style>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	if (str != null) // 去除html的转义字符
	{
	    p = Pattern.compile("&[a-z]*;?");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	
	if (str != null) // 去除script代码
	{
	    p = Pattern.compile("(?is)<script[^>]*?>.*?<\\/script>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	
	if(str!=null)  //根据文本逻辑加上换行符
	{
	    int i=1;
	    StringBuilder sb=new StringBuilder(str);
	    p = Pattern.compile("(</[a-z0-9]+>\\s*)+[^\n]");
	    m = p.matcher(str);
	    while (m.find()) 
	    {
	        if(sb.charAt(m.end()+i-3)!='\n')
	        {
	            sb.insert(m.end()+i-2, "\n");
		    i++;
	        } 
	    }
	    p=Pattern.compile("</span>|</strong>|</b>");
	    m=p.matcher(sb.toString());
	    i=0;
            while(m.find())
            {
        	if(m.end()!=sb.length() && sb.charAt(m.end()-i) == '\n')
        	{
        	    sb.deleteCharAt(m.end()-i);
        	    i++;
        	}
        	
            }
	    str=sb.toString();
	}
	format_html = str;
	////System.out.println(str);
	
	String[] html_blocks = str.split("\n");
	List<String> tag_blocks = new ArrayList<String>();
	for (int i = 0; i < html_blocks.length; i++) {
	    tag_blocks.add(html_blocks[i].trim());
	}
	if (str != null) // 去除所有不带换行的标签
	{
	    p = Pattern.compile("<[^>\n]*>");
	    m = p.matcher(str);
	    str = m.replaceAll("");
	}
	if (!str.isEmpty()) // 去除所有带换行的标签
	{
	    p = Pattern.compile("<[^>\n]*\n|[^<\n]*>\n");
	    m = p.matcher(str);
	    str = m.replaceAll("\n");
	}
	else{
	     //System.out.println("提取之后的文本为空！");
	     return result;
	}
	
        String[] str_blocks = str.split("\n");
	List<String> word_blocks = new ArrayList<String>();
	for (int i = 0; i < str_blocks.length; i++) {
	    word_blocks.add(str_blocks[i].trim());
	}
	
	if(word_blocks.isEmpty())
	{
	    //System.out.println("提取之后的文本为空！");
	    return result;
	}
	print_test(word_blocks);
	int em_raw=0;
	for(int i=0;i<word_blocks.size();i++)
	{
	    if(word_blocks.get(i).length()==0)
		em_raw++;
	}
	////System.out.println(word_blocks.size()+"***"+em_raw);
	int k=em_raw/(word_blocks.size()-em_raw)+2;
    
	List<CBlock> contents = new ArrayList<CBlock>();
	Get_Content(contents,word_blocks,tag_blocks,k);   //根据行块粗略的得到可能是正文的块，相关信息记录在contents里面
	result=ExtractMainContent(contents,word_blocks,tag_blocks);
	if(temptitle.length()!=0 && result !=null)
	{
	    if(result.getTitle()==null)
	        result.setTitle(temptitle);
	}
	//extractMainText(blocks, titles, 4);
	//System.out.println("步长****"+k);
	return result;
    }
    private HResult ExtractMainContent(List<CBlock> contents,List<String> blocks,List<String> tag_blocks)
    {
	HResult temp_result=new HResult();
	int longst=0;
	if(contents.isEmpty())
	{
	    temp_result.setState("fail");
	    temp_result.setMsg("页面行块提取失败");
	    return temp_result;
	}   
	
	for(int i=0;i<contents.size();i++)
	{
	    if (contents.get(i).getlength() > contents.get(longst).getlength()) 
	    {
	        longst = i;
	    }
            if(contents.get(i).getlink()!=0) //字数和链接比
            {
        	if(contents.get(i).getlength()/contents.get(i).getlink()>50)
                    contents.get(i).weight+=4;
        	else if(contents.get(i).getlength()/contents.get(i).getlink()>20)
                    contents.get(i).weight+=2;
        	else
        	    contents.get(i).weight-=20;
            }
            else{
        	contents.get(i).weight+=5;
            }
            //行和链接比
            if((float)(contents.get(i).getEnd()-contents.get(i).getStart()-contents.get(i).getemptyrow())/contents.get(i).getlink()>2.5)
        	contents.get(i).weight+=2;
            else
        	contents.get(i).weight-=50;
            //如果这个content只有一个段落则减分
            if(contents.get(i).getStart() == contents.get(i).getEnd())
        	contents.get(i).weight-=5;
        }
	int tt;
        tt=contents.size()-1;
	if(tt>1)
	{
	    contents.get(longst).weight+=5;//绝对字长最大
	    contents.get(0).weight-=4;
	    contents.get(tt).weight-=5;     //相对位置，在头尾的字块减
	}
	for(int i=0;i<contents.size();i++)
	{
	    if (contents.get(i).getweight() > contents.get(longst).getweight()) 
	    {
	        longst = i;   
	    }
	}
	if(contents.get(longst).getweight()<80)
	{
	    temp_result.setState("false");
	    temp_result.setMsg("很可能是列表页面");
	    return temp_result;
	}
	else{
	    for(int i=longst;i<contents.size()-1;i++)
	    {
		int s=contents.get(i+1).getStart();
		int e=contents.get(i).getEnd();
		int judge=1;
		if(s-e<=11)
		{
		    for(int count=e+1;count<s;count++)
		    {
			if(blocks.get(count).length()!=0)
			{
			    judge=0;
			    break;
			}
		    }
		}
		else{
		    break;
		}
		if(judge == 1)
		{
		    if(contents.get(longst).getweight()-contents.get(i+1).getweight()<=25)
		    {
			int tend=contents.get(i+1).getEnd();
			contents.get(longst).setEnd(tend);
		    }
		}
		else{
		    break;
		}
	    }
	    for(int i=longst;i>0;i--)
	    {
		int s=contents.get(i).getStart();
		int e=contents.get(i-1).getEnd();
		int judge=1;
		if(s-e<=11)
		{
		    for(int count=e+1;count<s;count++)
		    {
			if(blocks.get(count).length()!=0)
			{
			    judge=0;
			    break;
			}
		    }
		}
		else{
		    break;
		}
		if(judge == 1)
		{
		    if(contents.get(longst).getweight()-contents.get(i-1).getweight()<=25)
		    {
			int tend=contents.get(i-1).getStart();
			contents.get(longst).setStart(tend);
		    }
		}
		else{
		    break;
		}
	    }
	    Get_Detail(contents.get(longst),temp_result,blocks,tag_blocks);
	    StringBuffer currentText= new StringBuffer();
	    for (int count = contents.get(longst).getStart(); count <= contents.get(longst).getEnd(); count++) 
	    {
		if(blocks.get(count).length()!=0)
		{
		    currentText.append(blocks.get(count));
		    currentText.append("\n");
		}
	    }
	    temp_result.setText(currentText.toString());
	    temp_result.setState("ok");
	    
	    return temp_result;
	}
    }
    private void Get_Detail(CBlock longst,HResult result,List<String> blocks,List<String> tag_blocks)
    {
	int tt=longst.getStart();
	int raw=0;
	int maxline=99999;
	int minline=longst.getStart();
	while(tt >=0 && raw <15)
	{
	    while(tt >=0 && blocks.get(tt).length()<=1)
		tt--;
	    raw++;
	    if (tt >= 0) 
	    {
		String temptt = blocks.get(tt);
		p = Pattern.compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setDate(m.group());
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("(来源|来源于)[:：\\s]([^\\s]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setFrom(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("(笔者|编辑|作者|记者|楼主)[:：\\s]([^\"“]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setAuth(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    break;
		}
		p = Pattern.compile("[\u4e00-\u9fa5]+(公司|网)$");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setCompany(m.group());
		    break;
		}
		p = Pattern.compile("(关键字|关键词)[:：\\s]([\u4e00-\u9fa5]*)");
		m = p.matcher(temptt);
		while (m.find()) {
		    if (tt < maxline)
			maxline = tt;
		    result.addkeywords(m.group(1));
		    break;
		}
		tt--;
	    }
	}
	if(result.getDate()==null)
	{
	    raw=0;
	    tt=longst.getStart()+1;
	    while(tt <longst.getEnd() && raw <8)
            {
		while (tt <longst.getEnd() && blocks.get(tt).length() == 0)
		    tt++;
		raw++;
		String temptt = blocks.get(tt);
		if(result.getDate()==null)
		{
		    p = Pattern.compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setDate(m.group());
			if (tt < maxline)
			   maxline = tt;
			if (tt > minline && tt <longst.getlongst_row())
			    minline = tt+1;
			break;
		   }
		}
		p = Pattern.compile("(来源|来源于)[:：]\\s?([^\\s]*)\\s*");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setFrom(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    break;
		}
		p = Pattern.compile("(笔者|编辑|作者|记者|楼主)[:：\\s]([^\"“]+)\\s+");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setAuth(m.group(2));
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    break;
		}
		p = Pattern.compile("[\u4e00-\u9fa5]+(公司|网)$");
		m = p.matcher(temptt);
		while (m.find()) {
		    result.setCompany(m.group());
		    break;
		}
		p = Pattern.compile("(关键字|关键词)[:：\\s]([\u4e00-\u9fa5]*)");
		m = p.matcher(temptt);
		while (m.find()) {
		    if (tt < maxline)
			maxline = tt;
		    if (tt > minline && tt <longst.getlongst_row())
			minline = tt+1;
		    result.addkeywords(m.group(1));
		    break;
		}
		if(temptt.contains("摘要") && tt >minline && tt<longst.getlongst_row())
		{
		    minline = tt;
		}
		tt++;
	    }
	}
	
	if(maxline != 99999)
        {
	   int j=maxline-1;
	   int r=0;
	   String[] titles=new String[3];
	   int[] weight={100,100,100};
	   Pattern pt2=Pattern.compile("<h1>");
           Matcher mt2;
	   while(j>=0 && r<3)
	   {
	       while(j>=0 &&blocks.get(j).length()<=4)
		   j--;
	       if(j>=0)
	       {
		   titles[r]=blocks.get(j);
		   mt2=pt2.matcher(tag_blocks.get(j));
		   if(mt2.find())
		   {
		       weight[r]+=10;
		   }
	       }
	       
	       r++;
	       j--;
	   }
	   for (int i = 0; i <3 ; i++) 
	   {
	       if (titles[i] == null || titles[i].length() > 100)
	       {
	           weight[i] = 0;
	           continue;
	       }
	       if (titles[i].length() > 50 || titles[i].length() < 6)
	       {
	           weight[i] -= 10;
	       }
	       if( titles[i].contains("http://www")||titles[i].contains("链接"))
	       {
	           weight[i] -= 10;
	       }
	       if (titles[i].contains("位置") || titles[i].contains("新闻")||titles[i].contains("网")|| titles[i].contains("报")||titles[i].contains(">")) 
	       {
		   weight[i] -= 4;
	       }
	       if (titles[i].contains("标题")||titles[i].contains("题目")) 
	       {
	           weight[i] += 5;	
	       }
	       p = Pattern.compile("[\\._|,:：，、0-9a-z]");
	       m = p.matcher(titles[i]);
	       while (m.find()) {
	           weight[i] -= 1;
	       }
	   }
	   int max = 0;
	   for (int m = 0; m < 3; m++) {
	        ////System.out.println("!!!!!!!!!!!!!!!!!!!"+titles[m]);
	        ////System.out.println(weight[m]);
	        if (weight[m] > weight[max])
	        max = m;
	   }
	   if(weight[max]>96)
	       result.setTitle(titles[max]);
	}
	if(result.getDate()==null)
	{
	    raw=0;
	    for(int mm=longst.getEnd();mm<blocks.size() && raw<3;mm++)
	    {
		while (mm<blocks.size() && blocks.get(mm).length() == 0)
		    mm++;
		raw++;
		if(mm!=blocks.size())
		{
		    String temptt = blocks.get(mm);
		    p = Pattern
			    .compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setDate(m.group());
			break;
		    }
		    p = Pattern.compile("(来源|来源于)[:：\\s]([^\\s]*)\\s*");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setFrom(m.group(2));
			break;
		    }
		    p = Pattern.compile("(笔者|编辑|作者|记者|楼主)[:：\\s]([\u4e00-\u9fa5]*)\\s+");
		    m = p.matcher(temptt);
		    while (m.find()) {
			result.setAuth(m.group(2));
			break;
		    }
		}
	    }
	}
	//System.out.println("maxline"+maxline+"minline+"+minline+"longstline"+longst.getlongst_row());
	longst.setStart(minline);
    }
    private void Get_Content(List<CBlock> contents,List<String> blocks,List<String> tag_blocks,int k)
    {
	int len = 0;
	int threshold=0;
	int start=0, end=0;
	List<Block> candidates = new ArrayList<Block>();
	Block current = null;
	CBlock temp=null;
	for (int i = 0; i + k <= blocks.size(); i++) {
	    len = 0;
	    for (int j = i; j < i + k; j++) {
		len = len + blocks.get(j).length();
	    }
	    current = new Block();
	    current.setStart(i);
	    current.setEnd(i + k - 1);
	    current.setlength(len);
	    candidates.add(current);
	    current = null;
	}
	if (candidates.size() == 0) {
	    result.setState("fail");
	    result.setMsg("行块无法获得");
	}
	else{
	    Block longst = candidates.get(0);
	    for (Block b : candidates) {
		if (b.getlength() > longst.getlength()) {
		    longst = b;
		}
	    }
	    threshold=longst.getlength()/3;
	    int i=0;
	    int start_judge=1;
	    while(i<candidates.size())
	    {
		if(start_judge==1)
		{
		    if(candidates.get(i).getlength()>=10)
		    {
			start=i;
			start_judge=0;
		    }
		}
		if(candidates.get(i).getlength()>=threshold)
		{
		    temp=new CBlock();
		}
		if(start_judge==0)
		{
		    if(candidates.get(i).getlength()<=4)
		    {
			end=i;
			start_judge=1;
			if(temp!=null)
			{
                            if(start==96)
                        	DEBUG=true;
                            else
                        	DEBUG=false;
			    temp.setStart(start);
			    temp.setEnd(end-1);
			    int templen=0;
			    int templonst=start+k-1;
			    for (int j = start+k-1; j <=end-1; j++) {
				templen = templen + blocks.get(j).length();
				if(blocks.get(j).length()>blocks.get(templonst).length())
				    templonst=j;
			    }
			    temp.setlongst_row(templonst);
			    temp.setlength(templen);
			    Get_Articleweight(temp,blocks,tag_blocks);
			    contents.add(temp);
			    //System.out.println("start****"+(start));
			    //System.out.println("end****"+(end-1));
			    temp=null;
			}
		    }
		    if(i==candidates.size()-1)
		    {
			start_judge=1;
			if(temp!=null)
			{
			    temp.setStart(start);
			    temp.setEnd(i);
			    int templen=0;
			    int templonst=start+k-1;
			    for (int j = start+k-1; j <=end-1; j++) {
				templen = templen + blocks.get(j).length();
				if(blocks.get(j).length()>blocks.get(templonst).length())
				    templonst=j;
			    }
			    temp.setlongst_row(templonst);
			    temp.setlength(templen);
			    Get_Articleweight(temp,blocks,tag_blocks);
			    contents.add(temp);
			    //System.out.println("start****"+(start));
			    //System.out.println("end****"+(end-1));
			    temp=null;
			}
		    }
		}
		i++;
	    }
	}
    }
    private void Get_Articleweight(CBlock temp,List<String> word_blocks,List<String> tag_blocks)
    {
	Pattern pt=Pattern.compile("，|。|？|！|；|、");
	Matcher mt;
	Pattern pt2=Pattern.compile("<a\\s*[^>]*>");
	Matcher mt2;
	Pattern pt3=Pattern.compile("(电话|手机|联系人|地址|传真|mail|邮编|邮箱|Tel|Fax|版权|邮件)\\s?([:：])");
	Matcher mt3;
	Pattern pt4=Pattern.compile("[京浙]\\s*icp\\s*[证备]?");
	Matcher mt4;
	Pattern pt5=Pattern.compile("[\u4e00-\u9fa5]|[\\x21-\\x7E]");
	Matcher mt5;
	int link_num=0;
	int empty_row=0;
	int temp_weight=temp.getweight();
	for(int i=temp.getStart();i<=temp.getEnd();i++) //有标点符号权值加2
	{
	    if(word_blocks.get(i).length()>=2)
	    {
		mt=pt.matcher(word_blocks.get(i));
		while(mt.find())
		{
		    if(DEBUG)
			    //System.out.println("有标点符号，权值加2");
		    temp_weight+=2;
		}
		mt3=pt3.matcher(word_blocks.get(i));
		while(mt3.find())
		{
		    if(DEBUG)
			    //System.out.println("有联系方式，权值减5");
		    temp_weight-=8;
		    
		}
		mt4=pt4.matcher(word_blocks.get(i));
		while(mt4.find())
		{
		    if(DEBUG)
		    temp_weight-=50;
		}
		mt5=pt5.matcher(word_blocks.get(i));
		int utfword=0;
		while(mt5.find())
		{
		    utfword++;
		}
		int temprow=temp.getEnd()-temp.getStart()-temp.getemptyrow();
		if(utfword!=0 && temprow!=0)
		   temp_weight-=((word_blocks.get(i).length()-utfword)*80/temprow/utfword);
	    }
	    else{
		empty_row++;
	    }
	    mt2=pt2.matcher(tag_blocks.get(i));
	    while(mt2.find())                          //有链接，权值减2
	    {
		if(DEBUG)
		    //System.out.println("有链接，权值减2");
		temp_weight-=2;
		link_num++;
	    }
	    
	}
	temp.setlink(link_num);
	temp.setweight(temp_weight);
	temp.setemptyrow(empty_row);
    }
    private void extractMainText(List<String> blocks, String[] titles, int k) {
	int len = 0;
	int start, end;
	List<Block> candidates = new ArrayList<Block>();
	Block current = null;
	StringBuffer currentText = new StringBuffer("");
	for (int i = 0; i + k <= blocks.size(); i++) {
	    len = 0;
	    for (int j = i; j < i + k; j++) {
		len = len + blocks.get(j).length();
	    }
	    current = new Block();
	    current.setStart(i);
	    current.setEnd(i + k - 1);
	    current.setlength(len);
	    candidates.add(current);
	    current = null;
	    // //System.out.println(i+"第i个快的长度"+len);
	}
	if (candidates.size() == 0) {
	    result.setState("fail");
	    result.setMsg("行块无法获得");
	    return;
	} else {
	    Block longst = candidates.get(0);
	    for (Block b : candidates) {
		if (b.getlength() > longst.getlength()) {
		    longst = b;
		}
	    }
	    //System.out.println( "***最长的行块是：" + longst.getStart());
	    for (start = longst.getStart(); start >= 0
		    && candidates.get(start).getlength() > 10; start--)
		;
	    for (end = longst.getStart(); end < candidates.size()
		    && candidates.get(end).getlength() > 0; end++)
		;
	    //System.out.println("开始行号："+start + "******结束行号：" + end);
	    for (int count = start + k; count < end; count++) {
		currentText.append(blocks.get(count));
		currentText.append("\n");
	    }
	    if (currentText.length() == 0) {
		result.setState("fail");
		result.setMsg("无法抽取正文");
	    } else {
		result.setText(currentText.toString());
	    }
            int empty_row=0;
            for(int i= start+k;i<end;i++)
            {
        	if(blocks.get(i).length() == 0)
        	{
        	    empty_row++;
        	}
            }
	   
	    if (Is_Article(start, end, currentText.length(),empty_row)) {
		int i = start + k;
		int m; // 最终得到titles的实际长度
		for (m = 1; m < titles.length; m = m + 2) {
		    while (i < blocks.size() && blocks.get(i).length() == 0)
			i++;
		    if (i < blocks.size()) {
			titles[m] = blocks.get(i); // 获取离正文最近的一段title
		    } else {
			break;
		    }
		    i++;
		}
		m = m - 2;
		i = start;
		int l;
		for (l = 2; l < titles.length; l = l + 2) {
		    while (i >= 0 && blocks.get(i).length() == 0)
			i--;
		    if (i >= 0) {
			titles[l] = blocks.get(i); // 获取离正文最近的一段title
		    } else {
			break;
		    }
		    i--;
		}
		l = l - 2;
		// 注意在titles里面可以为空
		String abs_title = Get_Title(titles, m > l ? m : l);
		if (abs_title.length() != 0) {
		    result.setTitle(abs_title);
		}
	    }
	}
    }

    private String Get_Title(String[] titles, int len) {
	Pattern p;
	Matcher m;
	int[] weight = new int[len];
	for (int i = 0; i < len; i++) {
	    // //System.out.println(titles[i]);
	    weight[i] = 100;
	}
	weight[0]+=2;
	for (int i = len - 1; i >= 0; i--) {
	    if (titles[i] == null || titles[i].length() == 0
		    || titles[i].length() > 100) {
		weight[i] = 0;
		continue;
	    }
	    if (titles[i].length() > 50 || titles[i].length() < 10) {
		weight[i] -= 2;
	    }
	    if( titles[i].contains("http://www"))
	    {
		weight[i] -= 5;
	    }
	    
	    p = Pattern.compile("[\\._|,:：，、]+");
	    m = p.matcher(titles[i]);
	    while (m.find()) {
		weight[i] -= 2;
	    }
		
	    if (titles[i].contains("时间") || titles[i].contains("日期")
		    || titles[i].contains("-") || titles[i].contains("/")
		    || titles[i].contains("年")) {
		p = Pattern
			.compile("[0-9]{2,4}[年/-][0-9]{1,2}[月/-][0-9]{1,4}[日]*");
		m = p.matcher(titles[i]);
		while (m.find()) {
		    result.setDate(m.group());
		    weight[i] -= 5;
		    break;
		}
	    }
	    if (titles[i].contains("作者") || titles[i].contains("笔者")
		    || titles[i].contains("编辑")) {
		p = Pattern.compile("(笔者|编辑|作者|记者|楼主)：?:?\\s*([^\\s\n]*)\\s*");
		m = p.matcher(titles[i]);
		while (m.find()) {
		    result.setAuth(m.group(2));
		    break;
		}
		weight[i] -= 2;
	    }
	    if (titles[i].contains("公司") || titles[i].contains("网")) {
		p = Pattern.compile("[\u4e00-\u9fa5]+(公司|网)");
		m = p.matcher(titles[i]);
		while (m.find()) {
		    result.setCompany(m.group());
		    break;
		}
		weight[i] -= 1;
	    }
	    if (titles[i].contains("来源")) {
		p = Pattern.compile("(来源|来源于)：?:?\\s*([^\\s]*)\\s*");
		m = p.matcher(titles[i]);
		while (m.find()) {
		    result.setFrom(m.group(2));
		    break;
		}
		weight[i] -= 1;
	    }
	    if (titles[i].contains("关键")) {
		p = Pattern.compile("(关键字|关键词)：?:?\\s*(.*)\\s*");
		m = p.matcher(titles[i]);

		int iskey = 0;
		while (m.find()) {
		    String temp = m.group();
		    String temp2 = m.group(1);
		    //System.out.println(temp + temp2);
		    result.addkeywords(m.group(1));
		    iskey = 1;
		}
		if (iskey == 1)
		    weight[i] -= 5;
	    }
	    if (titles[i].contains("位置") || titles[i].contains("新闻")) {
		weight[i] -= 2;
	    }
	    if (titles[i].contains("标题")) {
		weight[i] += 5;
	    }
	}
	int max = 0;
	for (int i = 0; i < len; i++) {
	    ////System.out.println(titles[i]);
	    ////System.out.println(weight[i]);
	    if (weight[i] > weight[max])
		max = i;
	}
	return titles[max];
    }
    
    
    private boolean Is_Article(int start, int end, int word,int empty_row) 
    {
	if (format_html != null)
	{
	    p = Pattern.compile("\n");
	    m = p.matcher(format_html);
	    int startlink = 0, endlink = 0;
	    int i = 0;
	    while (m.find()) 
	    {
		if (i == start - 1)
		{
		    startlink = m.end();
		}
		if (i == end) 
		{
		    endlink = m.end();
		    break;
		}
		i++;
	    }
	    i = 0;
	    String temp_str=format_html.substring(startlink, endlink);
	    if (startlink != 0 && endlink != 0)
	    {
		p = Pattern.compile("<a\\s*[^>]*>");
		m = p.matcher(temp_str);
		while (m.find())
		{
		    i++;
		}
		//System.out.println("内容段链接数："+i+"&&&&&&&&&内容段的空行数目："+empty_row);
		if(i!=0)
		{
		    if ( word/i >50 && (((float)end-start-empty_row)/i)>2)
		    {
			if(!Is_Tail(temp_str))
			{
			    result.setMsg("该页面很可能是内容页");
			    result.setState("ok");
			    return true;
			}
			else{
			    result.setMsg("该页面很可能是列表页");
		            result.setState("fail");
		            return false;
			}
		    } 
		    else {
			 result.setMsg("该页面很可能是列表页");
			 result.setState("fail");
			 return false;
		    }
		}
		else
		{
		    if(!Is_Tail(temp_str))
		    {
		        result.setMsg("该页面很可能是内容页");
		        result.setState("ok");
		        return true;
		    }
		    else {
			 result.setMsg("该页面很可能是列表页");
			 result.setState("fail");
			 return false;
		    }
		}
	    } 
	    else{
		result.setMsg("无法定位到开头和结尾");
		result.setState("fail");
		return false;
	    }
	}
	else {
	    result.setMsg("无法得到一个格式化的html");
	    result.setState("fail");
	    return false;
	}
    }

    private void print_test(List<String> blocks) {
	for (int i = 0; i < blocks.size(); i++) {
	    // //System.out.println(i);
	    //System.out.println(blocks.get(i));
	}
    }
    
    private boolean Is_Tail(String str){
	Pattern p;
	Matcher m;
	int weight=100;
	p = Pattern.compile("(电话|地址|传真|mail|邮编|邮箱|Tel|Fax|版权|邮件):?：?");
	m = p.matcher(str);
	while (m.find()) {
		weight -= 5;
	}
	//System.out.println("是否为html尾部的权值："+weight);
	if(weight >80)
	    return false;
	else
	    return true;
    } 
    private String fetchHtmlString(String url, int timeout)
	    throws MalformedURLException, IOException {
	HttpURLConnection connection = createHttpConnection(url, timeout);
	connection.setInstanceFollowRedirects(true);
	String encoding = connection.getContentEncoding();

	InputStream is;
	if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
	    is = new GZIPInputStream(connection.getInputStream());
	} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
	    is = new InflaterInputStream(connection.getInputStream(),
		    new Inflater(true));
	} else {
	    is = connection.getInputStream();
	}
	String streamEncoding=null;
	byte[] data = streamToData(is);
	String strtemp=connection.getContentType();
	int m = strtemp.indexOf("charset=");
        if (m != -1) {
            streamEncoding = strtemp.substring(m + 8).replace("]", "");
        }
	////System.out.println("streamEncoding is "+streamEncoding);
	if(streamEncoding == null){
	    streamEncoding = detectEncoding(data);
	}
	if (data == null || streamEncoding == null) {
	    //System.out.println("streamEncoding is null");
	    return "";
	}
	return new String(data, streamEncoding);
    }

    private HttpURLConnection createHttpConnection(String url, int timeout)
	    throws MalformedURLException, IOException {
	HttpURLConnection connection = (HttpURLConnection) new URL(url)
		.openConnection(Proxy.NO_PROXY);
	connection
		.setRequestProperty(
			"User-Agent",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	connection
		.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	connection.setRequestProperty("content-charset", "UTF-8");
	connection.setRequestProperty("Cache-Control", "max-age=0");
	connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
	connection.setConnectTimeout(timeout);
	connection.setReadTimeout(timeout);
	return connection;
    }

    private String detectEncoding(byte[] data) {
	UniversalDetector detector = new UniversalDetector(null);
	detector.handleData(data, 0, data.length);
	detector.dataEnd();
	String encoding = detector.getDetectedCharset();
	detector.reset();
	return encoding;
    }

    private byte[] streamToData(InputStream is) {
	BufferedInputStream in = null;
	try {
	    in = new BufferedInputStream(is, 2048);
	    ByteArrayOutputStream output = new ByteArrayOutputStream();

	    int bytesRead = output.size();
	    byte[] arr = new byte[2048];
	    while (true) {
		if (bytesRead >= maxBytes) {
		    break;
		}
		int n = in.read(arr);
		if (n < 0)
		    break;
		bytesRead += n;
		output.write(arr, 0, n);
	    }
	    return output.toByteArray();
	} catch (SocketTimeoutException e) {

	    return null;
	} catch (IOException e) {

	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		}
	    }
	}
    }
}

package content;


import org.jsoup.nodes.TextNode;

public class TextUtils {
    public static int countText(String text){
        return text.trim().length();
    }
    
    public static boolean isEmptyNode(TextNode node){
        int count=countText(node.text());
         return count==0;
    }
           
    
    public static char[] puncs=new char[]{',','.',';','\'','\"',
    ',','。',';','‘','’','“'
    };
    
    public static int countPunc(String text){
        text=text.trim();
        int sum=0;
        for(int i=0;i<text.length();i++){
            char c=text.charAt(i);
            for(char punc:puncs){
                if(punc==c){
                    sum++;
                    break;
                }
            }
        }
        return sum;
    }
}

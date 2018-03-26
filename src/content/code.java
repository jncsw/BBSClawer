package content;

public class code {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {  
	            java.net.URL url = new java.net.URL("http://www.iteye.com/problems/20542");  
	            java.net.URLConnection conn = url.openConnection();  
	            conn.connect();  
	            System.out.println("getContentType " + conn.getContentType());  
	            System.out.println("getContentEncoding " + conn.getContentEncoding());//获取页面编码  
	            System.out.println("getDate " + conn.getDate());  

	            System.out.println("" + conn.getContentLength());  
	            System.out.println("" + conn.getDate());  
	            System.out.println("" + conn.getExpiration());  
	            System.out.println("" + conn.getLastModified()); 
	            StringBuffer contentBuffer = new StringBuffer();  
	            java.io.InputStream is = conn.getInputStream();  
	            //java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is, conn.getContentEncoding()));  
	            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));  
	            String inputLine = null;  
	            while ((inputLine = reader.readLine()) != null) {  
	                contentBuffer.append(inputLine);  
	                contentBuffer.append("\n");  
	            }  
	            is.close();  
	            System.out.println("" + contentBuffer.toString());  
	        } catch (java.net.MalformedURLException e) {  
	            e.printStackTrace();  
	        } catch (java.io.IOException e) {  
	            e.printStackTrace();  
	        }  

	}

}

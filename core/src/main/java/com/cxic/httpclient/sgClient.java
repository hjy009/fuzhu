package com.cxic.httpclient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class sgClient {
	protected CloseableHttpClient httpclient;
	protected String serverAddress;
	protected String CWSSESSID;
	protected String passportSession;
	protected String weeCookie;
	protected JSONObject weeObj;
	
    BasicCookieStore cookieStore;
	
	public sgClient() {
        cookieStore = new BasicCookieStore();
		httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        
		//httpclient = HttpClients.createDefault();
		serverAddress = "https://passport.9wee.com/";
		SetCWSSESSID();
	}
	
	protected String GetTime(){
		long mtime;
		String stime;

		mtime = System.currentTimeMillis();
		stime = String.valueOf(mtime);
		return stime;
	};

	protected void printCookies(){
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None cookie");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
            	System.out.println("- " + cookies.get(i).toString());
            }       
         }
	};

	protected void refreshCookies() {
		CWSSESSID = "";
		passportSession = "";
		weeCookie = "";

		List<Cookie> cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			String name = cookies.get(i).getName();
			if (name.equals("CWSSESSID")) {
				CWSSESSID = cookies.get(i).getValue();
			}
			if (name.equals("passportSession")) {
				passportSession = cookies.get(i).getValue();
			}
			if (name.equals("weeCookie")) {
				weeCookie = cookies.get(i).getValue();
				String decode;
				try {
					decode = java.net.URLDecoder.decode(weeCookie,"UTF8");
//					System.out.println("decode:" + decode);
					decode = convertUnicode(decode);
					weeObj = new JSONObject(decode);
//			        decode = weeObj.getString("nickname");  
//					System.out.println("json:" + decode);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	protected void printHeaders(CloseableHttpResponse response){
		Header[] hs = response.getAllHeaders();
		for(Header h1:hs){
			System.out.println(h1.toString());
		}
	}

	protected URI GetURI(String host, String path, String... args) {
		String name = "";
		try {
			URIBuilder bld = new URIBuilder();
			bld.setScheme("http").setHost(host).setPath(path);
			for (int i = 0; i < args.length; i++) {
				if (i % 2 == 0) {
					name = args[i];
				} else {
					bld.setParameter(name, args[i]);
				}
			}
			URI uri = bld.build();
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected UrlEncodedFormEntity GetFormEntity(String... args){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String name="";
		for(int i=0;i<args.length;i++){
			if(i % 2 ==0){
				name = args[i];
			} else {
				nvps.add(new BasicNameValuePair(name, args[i]));
			}
		}
		UrlEncodedFormEntity entity=null;
		try {
			entity = new UrlEncodedFormEntity(nvps, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}
	
	protected String GetSetCookie(String name){
		String id="";
		
//		System.out.println("Initial set of cookies:");
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None cookie");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
            	if (cookies.get(i).getName().equals(name)){
            		id=cookies.get(i).getValue();
            	}
            	//System.out.println("- " + cookies.get(i).toString());
            }       
            //id = cookies.get(0).getValue();
            //System.out.println(id);
         }
		return id;
	}
	
	protected String GetSetCookie(CloseableHttpResponse response , String name){
		Header[] hs = response.getHeaders("Set-Cookie");
		for(Header h1:hs){
			String str = h1.toString();
	        //System.out.println(id);
	        String[] strs = str.split(":|=|;");
	        for(int i=0;i<strs.length;i++){
	        	if(strs[i].trim().equals(name)){
	        		return strs[i+1];
	        	}
	        }
		}
        //String value=strs[2];
		return "";
	}
	
	protected boolean SaveImage(CloseableHttpResponse response, String filename){
		boolean r=false;
		
		HttpEntity entity1 = response.getEntity();
	    InputStream in;
		try {
			in = entity1.getContent();
			FileOutputStream output = new FileOutputStream(new File(filename));
			BufferedImage img = ImageIO.read(in);
			ImageIO.write(img, "BMP", output);
			r=true;
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
	public void initHeader(HttpRequestBase request){
		/*
		request.setHeader("POST /app/register.php?ac=add HTTP/1.1","");
		request.setHeader("Host","passport.9wee.com");
		*/
		request.setHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:40.0) Gecko/20100101 Firefox/40.0");
		request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		request.setHeader("Accept-Encoding","gzip, deflate");
		//request.setHeader("Cookie","db_idx_session=0; CWSSESSID=6377b597ddc09229ddc3b7f451155521; verifyCode=5229");
		//request.setHeader("Cookie","db_idx_session=0; "+"CWSSESSID="+CWSSESSID+"; verifyCode="+code);
		request.setHeader("Host","passport.9wee.com");
		request.setHeader("Connection","keep-alive");
	}
	
	public void addRef(HttpRequestBase request,String value){
		request.setHeader("Referer",value);
	}

	public String postResponseStr(String url, String ref, String... args){
		String str="";
		UrlEncodedFormEntity entity = GetFormEntity(args);
		/*
	    str = EntityUtils.toString(entity);
	    System.out.println(str);
		printCookies();
		*/
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		//initHeader(httpPost);
		addRef(httpPost,ref);
		CloseableHttpResponse response1;
		try {
			response1 = httpclient.execute(httpPost);
		    HttpEntity entity1 = response1.getEntity();
		    str = EntityUtils.toString(entity1);
		    //System.out.println(str);
		    //r=str.indexOf("恭喜")>=0;
		    //printCookies();
		    EntityUtils.consume(entity1);
		    response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public String postResponseCookie(String url, String ref, String cookie, String... args){
		String str="";
		
		UrlEncodedFormEntity entity = GetFormEntity(args);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		//initHeader(httpPost);
		addRef(httpPost,ref);
		CloseableHttpResponse response1;
		try {
			response1 = httpclient.execute(httpPost);
			//printCookies();
			str = GetSetCookie(response1, cookie);
		    response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public String getResponseStr(String host, String path, String ref, String... args){
		URI uri = GetURI(host, path, args);
		
		return getResponseStr(uri.toString(), ref);
	}
	
	public String getResponseStr(String url,String ref){
		String r="";
		
		HttpGet httpGet = new HttpGet(url);
		addRef(httpGet,ref);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
		    HttpEntity entity1 = response1.getEntity();
			if (response1.getStatusLine().getReasonPhrase().equals("OK")) {
				r = EntityUtils.toString(entity1);
//				System.out.println(r);
			    // do something useful with the response body
			    // and ensure it is fully consumed
//			    InputStream in = entity1.getContent();
//			    System.out.println(EntityUtils.toString(entity1,"GB2312"));
			    //System.out.println(in.toString());
			} 
		    // do something useful with the response body
		    // and ensure it is fully consumed
//		    InputStream in = entity1.getContent();
//		    System.out.println(EntityUtils.toString(entity1,"GB2312"));
		    //System.out.println(in.toString());

		    //System.out.println(EntityUtils.toString(entity1));
		    EntityUtils.consume(entity1);
		    response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	public String getResponseCookie(String url,String ref,String name){
		String r="";
		
		HttpGet httpGet = new HttpGet(url);
		addRef(httpGet,ref);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
		    HttpEntity entity1 = response1.getEntity();
			if (response1.getStatusLine().getReasonPhrase().equals("OK")) {
				r=GetSetCookie(response1,name);
			} 
		    EntityUtils.consume(entity1);
		    response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
	public String SetCWSSESSID(){
		String url;
		String str;

		url = serverAddress+"app/" + "register.php?step=2";
		HttpGet httpGet = new HttpGet(url);
/*
		httpGet.setHeader("Cookie", "__utma=1831918.1379181802.1441183404.1441959515.1442553530.6;"
				+ " __utmz=1831918.1442553530.6.6.utmcsr=wz24.sg.9wee.com|utmccn=(referral)|utmcmd=referral|utmcct=/main.php; "
				+ "registerTime=Am5UYAAxBzQAPlQ1B2EANQY0ADQ%3D;"
				+ " CWSSESSID=def886b778d0bd82bf51c6391677bb00");//设置cookie
*/
		/*
		POST /app/register.php?ac=add HTTP/1.1

				Host: passport.9wee.com

				User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:40.0) Gecko/20100101 Firefox/40.0
*/
	//			Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
/*
				Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3

				Accept-Encoding: gzip, deflate

				Referer: http://passport.9wee.com/app/register.php?step=2

				Cookie: db_idx_session=0; CWSSESSID=6377b597ddc09229ddc3b7f451155521; verifyCode=5229

				Connection: keep-alive
*/
		CloseableHttpResponse response1;
		try {
			response1 = httpclient.execute(httpGet);

            CWSSESSID=GetSetCookie(response1,"CWSSESSID");
            //System.out.println(id);
            
			HttpEntity entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
			response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CWSSESSID;
	}
	
	public static String convertUnicode(String ori){
        char aChar;
        int len = ori.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = ori.charAt(x++);
            if (aChar == '\\') {
                aChar = ori.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = ori.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
 
        }
        
        return outBuffer.toString();
	}

	
}

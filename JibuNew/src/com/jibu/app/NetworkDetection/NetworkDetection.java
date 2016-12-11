package com.jibu.app.NetworkDetection;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkDetection {
	
	
	public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
    try { 
        ConnectivityManager connectivity = (ConnectivityManager) context 
                .getSystemService(Context.CONNECTIVITY_SERVICE); 
        if (connectivity != null) { 
            // 获取网络连接管理的对象 
            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
            if (info != null&& info.isConnected()) { 
                // 判断当前网络是否已经连接 
                if (info.getState() == NetworkInfo.State.CONNECTED ) { 
                    return ping(); 
                } 
            } 
        } 
    } catch (Exception e) { 
    	// TODO: handle exception 
    		Log.v("error",e.toString()); 
    } 
        return false; 
	}
	
	
	public static final boolean ping() { 
	     
        String result = null; 
        try { 
                String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网 
                Process p = Runtime.getRuntime().exec("ping -c 1 -w 3 " + ip);// ping网址1次 
                // 读取ping的内容，可以不加 
//                InputStream input = p.getInputStream(); 
//                BufferedReader in = new BufferedReader(new InputStreamReader(input)); 
//                StringBuffer stringBuffer = new StringBuffer(); 
//                String content = ""; 
//                while ((content = in.readLine()) != null) { 
//                        stringBuffer.append(content); 
//                } 
//                Log.d("------ping-----", "result content : " + stringBuffer.toString()); 
                // ping的状态 
                int status = p.waitFor(); 
                if (status == 0) { 
                        result = "success"; 
                        return true; 
                } else { 
                        result = "failed"; 
                } 
        } catch (IOException e) { 
                result = "IOException"; 
        } catch (InterruptedException e) { 
                result = "InterruptedException"; 
        } finally { 
                Log.d("----result---", "result = " + result); 
        } 
        return false;
	}
	
	public static final boolean ping2() {

        String address="www.baidu.com";  
        try {  
        	Process process = Runtime.getRuntime().exec("ping  -c 1 -w 3"+address);  
        	InputStreamReader r = new InputStreamReader(process.getInputStream());  
        	LineNumberReader returnData = new LineNumberReader(r);  
 
        	String returnMsg="";  
        	String line = "";  
        	while ((line = returnData.readLine()) != null) {  
        		System.out.println(line);  
        		returnMsg += line;  
        	}  
            Log.e("TAG", "returnMsg = " + returnMsg);
        	if(returnMsg.indexOf("0% loss")!=-1){  
        		Log.e("TAG"," www.baidu.com 连接畅通."); 
        		return true;
        	}  else{  
        		Log.e("TAG"," www.baidu.com 连接不畅通.");  
//        		return true;
        	}  
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
	}
}

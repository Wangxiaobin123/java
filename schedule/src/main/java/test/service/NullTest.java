package test.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by BFD-194 on 2017/4/5.
 */
public class NullTest {
//    public static void main(String[] args){
//        try{
//           String str = null;
//            if(null == str){
//                System.out.println(str);
//            }
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }
public static String sendGet(String url, String charset, int timeout)
{
    String result = "";
    try
    {
        URL u = new URL(url);
        try
        {
            URLConnection conn = u.openConnection();
            conn.connect();
            conn.setConnectTimeout(timeout);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String line="";
            while ((line = in.readLine()) != null)
            {

                result = result + line;
            }
            in.close();
        } catch (IOException e) {
            return result;
        }
    }
    catch (MalformedURLException e)
    {
        return result;
    }

    return result;
}

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String access_token="";//
        String product_id="";
        String data = NullTest.sendGet("https://api.weixin.qq.com/device/getqrcode?access_token="+access_token+"&product_id="+product_id, "utf-8", 30000);
        System.out.println("获取设备的deviceid和二维码:"+data);
    }
}

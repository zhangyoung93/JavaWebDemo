package com.zy.demo.util;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP请求工具类
 * @author zy
 */
@Slf4j
public class HttpClientUtil {

    /**
     * HTTP请求
     * @param url 请求URL
     * @param reqStr 请求报文
     * @param headerMap 请求Header
     * @param getParamMap GET请求参数
     * @param method HTTP方法
     * @param contentType 文本类型
     * @return 返回报文
     */
    private static String request(String url,String reqStr,Map<String,String> headerMap,Map<String,String> getParamMap,String method,String contentType){
        log.info("send request--->URL={},Header={},Body={},getParamMap={}",url,headerMap,reqStr,getParamMap);
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader in = null;
        StringBuilder rsp = new StringBuilder();
        try {
            //GET请求参数赋值
            if(getParamMap != null){
                StringBuilder urlSb = new StringBuilder(url).append("?");
                for(Map.Entry<String,String> entry : getParamMap.entrySet()){
                    urlSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                urlSb.deleteCharAt(urlSb.length()-1);
            }
            //创建URL连接实例
            conn = (HttpURLConnection)new URL(url).openConnection();
            //允许使用URL连接输入
            conn.setDoInput(true);
            //允许使用URL连接输出
            conn.setDoOutput(true);
            //设置客户端从服务端读取应答数据的超时时间
            conn.setReadTimeout(3000);
            //设置建立HTTP连接的超时时间
            conn.setConnectTimeout(3000);
            //设置请求方式
            conn.setRequestMethod(method);
            //设置Body数据类型
            conn.setRequestProperty("Content-Type",contentType);
            //设置HTTP连接持久化
            conn.setRequestProperty("Connection","keep-alive");
            //设置自定义Header
            if(headerMap != null){
                for(Map.Entry<String,String> entry : headerMap.entrySet()){
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            //开启连接
            conn.connect();
            //GET请求不输出数据
            if(!"GET".equals(method)){
                //创建输出流实例用于发送客户端请求
                out = new DataOutputStream(conn.getOutputStream());
                //设置输出内容编码
                out.write(reqStr.getBytes(StandardCharsets.UTF_8));
            }
            log.info("response HTTP status code="+conn.getResponseCode());
            if(200 == conn.getResponseCode()){
                //接收服务端应答
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
                String line;
                while((line = in.readLine()) != null){
                    rsp.append(line);
                }
            }else{
                //接收服务端应答
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(),StandardCharsets.UTF_8));
                String line;
                while((line = in.readLine()) != null){
                    rsp.append(line);
                }
            }
        } catch (IOException e) {
            log.error("HTTP请求异常！",e);
        } finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            }catch (IOException e) {
                log.error("释放HTTP连接资源异常！",e);
            }
        }
        log.info("receive response--->" + rsp.toString());
        return rsp.toString();
    }

    /**
     * post请求(JSON)
     * @param url 请求URL
     * @param reqStr 请求报文
     * @return 返回报文
     */
    public static String postByJson(String url,String reqStr){
        return request(url,reqStr,null,null,"POST","application/json");
    }

    /**
     * POST请求(JSON)
     * @param url 请求URL
     * @param reqStr 请求报文
     * @param headerMap 请求Header
     * @return 返回报文
     */
    public static String postByJson(String url,String reqStr,Map<String,String> headerMap){
        return request(url,reqStr,headerMap,null,"POST","application/json");
    }

    /**
     * GET请求
     * @param url 请求URL
     * @return 返回报文
     */
    public static String get(String url){
        return request(url,null,null,null,"GET",null);
    }

    /**
     * GET请求
     * @param url 请求URL
     * @param paramMap 请求参数
     * @return 返回报文
     */
    public static String get(String url,Map<String,String> paramMap){
        return request(url,null,null,paramMap,"GET",null);
    }

    /**
     * GET请求
     * @param url 请求URL
     * @param paramMap 请求参数
     * @param headerMap 请求Header
     * @return 返回报文
     */
    public static String get(String url,Map<String,String> headerMap,Map<String,String> paramMap){
        return request(url,null,headerMap,paramMap,"GET",null);
    }
}

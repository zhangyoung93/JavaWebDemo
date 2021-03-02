package com.zy.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP连接工具类
 * @author zy
 */
@Slf4j
public class HttpClientUtil {

    /**
     * post请求
     * @param url 请求URL
     * @param reqStr 请求报文
     * @param headerMap 请求Header
     * @return 返回报文
     */
    public static String post(String url,String reqStr,Map<String,String> headerMap){
        //声明HTTP连接
        HttpURLConnection conn = null;
        //声明输出流用于发送请求
        OutputStream out = null;
        //声明输入流用于接收应答
        BufferedReader in = null;
        //声明字符串对象存储应答报文
        StringBuilder rsp = new StringBuilder();
        try {
            //创建URL连接实例
            conn = (HttpURLConnection)new URL(url).openConnection();
            //允许使用URL连接输入
            conn.setDoInput(true);
            //允许使用URL连接输出
            conn.setDoOutput(true);
            //设置客户端从服务端读取应答数据的超时时间
            conn.setReadTimeout(2000);
            //设置建立HTTP连接的超时时间
            conn.setConnectTimeout(2000);
            //设置请求方式
            conn.setRequestMethod("POST");
            //设置HTTP数据类型
            conn.setRequestProperty("Content-Type","application/json");
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
            //创建输出流实例用于发送客户端请求
            out = new DataOutputStream(conn.getOutputStream());
            //以UTF-8编码发送客户端请求
            out.write(reqStr.getBytes(StandardCharsets.UTF_8));
            //以UTF-8编码接收服务端应答
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
            //读取服务端应答
            String line;
            while((line = in.readLine()) != null){
                rsp.append(line);
            }
        } catch (IOException e) {
            log.error("HTTP请求异常！",e);
        } finally {
            //释放连接资源
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
        return rsp.toString();
    }

    /**
     * post请求
     * @param url 请求URL
     * @param reqStr 请求报文
     * @return 返回报文
     */
    public static String post(String url,String reqStr){
        return post(url,reqStr,null);
    }
}

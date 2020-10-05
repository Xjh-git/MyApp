package com.example.auction.chat.utils;

import android.util.Log;

import com.example.auction.chat.bean.ChatMessage;
import com.example.auction.chat.bean.result.Result;
import com.example.auction.chat.bean.result.Results;
import com.example.auction.chat.bean.send.InputText;
import com.example.auction.chat.bean.send.Perception;
import com.example.auction.chat.bean.send.Send;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class HttpUtils {
    private static final String MURL = "http://openapi.tuling123.com/openapi/api/v2";

    private static final String API_KEY = "a891f405f1964affbac6ea11c9cd27ac";

    //发送一个消息，得到返回的消息
    public static ChatMessage sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        String jsonRes = doPost(msg);
        Gson gson = new Gson();
        Result result = null;
        Log.d("tag1", "0" + jsonRes);
        try {
            result = gson.fromJson(jsonRes, Result.class);
            Log.d("tag1", "1: ");
            Results results = result.getResults().get(0);
            Log.d("tag1", "2: ");
            chatMessage.setMsg(results.getValues().getText());
        } catch (JsonSyntaxException e) {
            chatMessage.setMsg("服务器繁忙，请稍后再试");
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);


        return chatMessage;
    }

    public static String doPost(String msg) {
        String toJson = setParams(msg);

        try {
            URL urlNet = new URL(MURL);

            HttpURLConnection conn = (HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            //设置请求体的类型是文本类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            conn.setRequestProperty("Content-Length", String.valueOf(toJson.getBytes().length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(toJson.getBytes());

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return dealResponseResult(inputStream);
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String dealResponseResult(InputStream inputStream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        while ((str = reader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }

    private static String setParams(String msg) {
        InputText inputText = new InputText(msg);
        Perception perception = new Perception(inputText);
        Send send = new Send(perception);

        Gson gson = new Gson();
        String toJson = gson.toJson(send);

        return toJson;
    }
}

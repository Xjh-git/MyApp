package com.example.chat.utils;

import android.util.Log;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUser {
    private static String TAG = "tag1";


    //服务器的IP
    private final static String ServivceIP = "192.168.0.103";

    //传输的控制信息
    private final static String REGISTER = "1";
    private final static String LOGIN = "2";
    private final static String SEND = "3";
    static String control;

    public static boolean register(User user) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        String msg = "";

        //控制信息
        control = REGISTER;

        String sendMSG = control + ";" + user.getUsername() + ";" + user.getPassword();

        try {
            socket = new Socket(ServivceIP, 8888);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write(sendMSG);
            pw.flush();
            socket.shutdownOutput();

            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            bf = new BufferedReader(isr);

            String line;
            while ((line = bf.readLine()) != null) {
                msg += line;
            }

            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (isr != null)
                    isr.close();
                if (bf != null)
                    bf.close();

                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (msg.equals("true")) {
            return true;
        }

        return false;
    }

    public static boolean login(User user) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        String msg = "";

        control = LOGIN;

        String sendMSG = control + ";" + user.getUsername() + ";" + user.getPassword();

        try {
            socket = new Socket(ServivceIP, 8888);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write(sendMSG);
            pw.flush();
            socket.shutdownOutput();

            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            bf = new BufferedReader(isr);

            String line;
            while ((line = bf.readLine()) != null) {
                msg += line;
            }

            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (isr != null)
                    isr.close();
                if (bf != null)
                    bf.close();

                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (msg.equals("true")) {
            return true;
        }
        return false;
    }

    public static boolean sendMsg(ChatMessage chatMessage) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        String msg = "";

        control = SEND;

        String sendMSG = control + ";" + chatMessage.toString();
        Log.d(TAG, "sendMsg: " + sendMSG);

        try {
            socket = new Socket(ServivceIP, 8888);
            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write(sendMSG);
            pw.flush();
            socket.shutdownOutput();

            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            bf = new BufferedReader(isr);

            String line;
            while ((line = bf.readLine()) != null) {
                msg += line;
            }

            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (isr != null)
                    isr.close();
                if (bf != null)
                    bf.close();

                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (msg.equals("true")) {
            return true;
        }
        return false;
    }

    public static boolean addContact(User user, User contact) {
        boolean result = false;

        return result;
    }
}

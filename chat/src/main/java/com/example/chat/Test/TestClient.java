package com.example.chat.Test;

import android.util.Log;

import java.io.*;
import java.net.Socket;

public class TestClient {

    private final static String ServivceIP = "192.168.0.103";

    //    private final static String ServivceIP = "fe80::19a5:4b84:d5db:eb6a%10";
    private static String TAG = "tag1";

    public static String testChat() {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        String msg = "";

        Log.d(TAG, "testChat: 1111");

        try {
            socket = new Socket(ServivceIP, 8888);
            Log.d(TAG, "testChat: 2222");
            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write("hello!");
            pw.flush();
            Log.d(TAG, "testChat: 3333");
            socket.shutdownOutput();

            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            bf = new BufferedReader(isr);

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println("server say:" + line);
                msg += line;
            }
            Log.d(TAG, "testChat: 4444");
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
        Log.d(TAG, "testChat: hhhh");
        return msg + "  hhhh";
    }

}

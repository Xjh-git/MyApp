package com.example.chat.utils;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ConnServer {
    //服务器的IP
    private final static String ServivceIP = "192.168.43.109";

//    private final static String ServivceIP  = "10.12.100.88";

    private static final String TAG = "tag1";

    public static ResultMsg conn(RequestMsg requestMsg) {
        ResultMsg resultMsg = null;

        Socket socket = null;
        OutputStream os = null;
        ObjectOutputStream oos = null;

        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            socket = new Socket(ServivceIP, 8888);
            System.out.println("client start");
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);


            oos.writeObject(requestMsg);
            socket.shutdownOutput();

            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            resultMsg = (ResultMsg) ois.readObject();
//            doResult(resultMsg);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                if (ois != null)
                    ois.close();
                if (is != null)
                    is.close();

                if (oos != null)
                    oos.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return resultMsg;
    }


    private static void doResult(ResultMsg resultMsg) {
        switch (resultMsg.getControl()) {
            case ResultMsg.LOGIN:
                System.out.println("login result:" + resultMsg.getResult());
                break;
            case ResultMsg.REGISTER:
                System.out.println("register result:" + resultMsg.getResult());
                break;
            case ResultMsg.SEND_MSG:
                System.out.println("send msg result:" + resultMsg.getResult());
                break;
            case ResultMsg.GET_MSG:
                List<ChatMessage> chatMessageList = resultMsg.getChatMessageList();
                for (int i = 0; i < chatMessageList.size(); i++) {
                    System.out.println("msg:" + chatMessageList.get(i).getMsg());
                }
                break;
            case ResultMsg.GET_NEW_MSG:
                List<ChatMessage> chatMessageNewList = resultMsg.getChatMessageList();
                for (int i = 0; i < chatMessageNewList.size(); i++) {
                    System.out.println("msg:" + chatMessageNewList.get(i).getMsg());
                }
                break;

            default:
                break;
        }
    }
}

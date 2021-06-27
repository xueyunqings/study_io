package com.study.io.IO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        final int PORT = 8888;
        //创建ServerSocket监听8888端口
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("ServerSocket start,The Port is:"+PORT);

        while (true) {//不停地监听该端口
            //阻塞式的监听，如果没有客户端请求,则一直阻塞在accept阶段，直到收到客户端发的消息
            Socket socket = serverSocket.accept();
            System.out.println("Client[" + socket.getPort() + "]Online");
            //接收消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //发送消息
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String msg = null;
            while ((msg = reader.readLine()) != null) {
                System.out.println("Client[" + socket.getPort() + "]:" + msg);
                //服务端发送消息
                writer.write("Server receive msg:[" + msg + "] success\n");
                writer.flush();
                //如果客户端的消息是quit代表他退出了，并跳出循环，不用再接收他的消息了。如果客户端再次连接就会重新上线
                if (msg.equals("quit")) {
                    System.out.println("Client[" + socket.getPort() + "]:Offline");
                    break;
                }
            }
        }
    }
}



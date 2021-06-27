package com.study.io.BIO.client;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    //发送消息给服务器
    public void sendToServer(String msg) throws IOException {
        //发送之前，判断socket的输出流是否关闭
        if (!socket.isOutputShutdown()) {
            //如果没有关闭就把用户键入的消息放到writer里面
            writer.write(msg + "\n");
            writer.flush();
        }
    }
    //从服务器接收消息
    public String receive() throws IOException {
        String msg = null;
        //判断socket的输入流是否关闭
        if (!socket.isInputShutdown()) {
            //没有关闭的话就可以通过reader读取服务器发送来的消息。注意：如果没有读取到消息线程会阻塞在这里
            msg = reader.readLine();
        }
        return msg;
    }

    public void start() throws IOException {
        //和服务创建连接
        try {
            socket = new Socket("127.0.0.1", 8888);
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //新建一个线程去监听用户输入的消息
            new Thread(new UserInputHandler(this)).start();
            String msg=null;
            // 不停的读取服务器转发的其他客户端的信息
            while ((msg=receive())!=null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatClient().start();
    }
}

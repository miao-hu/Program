package tcp.threads;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);
        //1.创建一个客户端的套接字
        Socket tcpClientSocket=new Socket();

        //连接本机的IP地址
        byte[] ipv4={127,0,0,1};
        //服务器的IP地址
        InetAddress serverAddress=InetAddress.getByAddress(ipv4);
        //服务器的地址：IP+端口确定
        SocketAddress serverSocketAddress=new InetSocketAddress(serverAddress,9999);

        //2.客户端连接服务器
        tcpClientSocket.connect(serverSocketAddress);

        while(true){
            System.out.print("客户端:");
            String request=sc.nextLine();

            // 通过字节流直接从客户端向服务器 写入请求
            OutputStream os=tcpClientSocket.getOutputStream();
            PrintStream out=new PrintStream(os,true,"UTF-8");
            out.println(request);

            // 通过字节流直接从服务器向客户端 读取回复
            InputStream is=tcpClientSocket.getInputStream();
            InputStreamReader isReader=new InputStreamReader(is,"UTF-8");
            BufferedReader reader=new BufferedReader(isReader);

            String response=reader.readLine();
            //写出服务器回复的消息
            System.out.println("服务器："+response);
        }
    }
}


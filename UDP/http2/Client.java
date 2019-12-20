package tcp.http2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);
        //1.先建立一个客户端的套接字
        Socket tcpClientSocket=new Socket();
        //连接本机的IP为127.0.0.1
        byte[] serverIp={127,0,0,1};
        InetAddress serverAddress=InetAddress.getByAddress(serverIp);
        SocketAddress serverSocketAddress=new InetSocketAddress(
                serverAddress,
                7788
        );
        //2.客户端与服务器连接
        tcpClientSocket.connect(serverSocketAddress);
        while(true){
            System.out.print("客户端:");
            String request=sc.nextLine();

            //从客户端往服务器，通过字节流直接写入请求
            OutputStream os=tcpClientSocket.getOutputStream();
            PrintStream out=new PrintStream(os,true,"UTF-8");
            out.println(request);  //往服务器写入请求
            out.flush();           //刷新

            //从服务器往客户端，通过字节流直接读取回复内容
            InputStream is=tcpClientSocket.getInputStream();
            InputStreamReader isReader=new InputStreamReader(is,"UTF-8");
            BufferedReader reader=new BufferedReader(isReader);
            String response=reader.readLine();
            System.out.println(response); //打印出回复的消息
        }
    }
}

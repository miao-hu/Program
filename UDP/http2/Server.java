package tcp.http2;
/*
    这个版本的客户端和服务器，服务器只能接收一条客户端发来的消息
    当客户端发了第二条消息的时候，那么就会出现异常报错
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket tcpserverSocket=new ServerSocket(7788);
        Scanner sc=new Scanner(System.in);
        while(true){
            Socket clientSocket=tcpserverSocket.accept();
            //得到客户端的地址
            InetAddress clientAddress=clientSocket.getInetAddress();
            System.out.printf("我从 %s : %d 收到了消息\n",
                    clientAddress.getHostAddress(),   //客户端IP地址
                    clientSocket.getPort()            //客户端端口号
            );

            // 从客户端向服务器获取输入字节流
            InputStream is=clientSocket.getInputStream();
            // 将字节流转换为字符流
            InputStreamReader isReader=new InputStreamReader(is,"UTF-8");
            // 将字符流转换缓冲字符流
            BufferedReader reader=new BufferedReader(isReader);

            // 从客户端向服务器获取输出字节流
            OutputStream os=clientSocket.getOutputStream();
            //将字节流转换为字符流
            PrintStream out=new PrintStream(os,true,"UTF-8");

            //读一条从客户端向服务器发送来的消息
             String line=reader.readLine();
             System.out.println("客户端："+line);
            System.out.print("服务器：");
             String response=sc.nextLine();
             //向客户端回复一条消息
             out.println("服务器:"+response);
             //关闭客户端的连接
             clientSocket.close();
        }
    }
}

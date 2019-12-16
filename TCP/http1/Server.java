package tcp.http1;
/*
    这个客户端服务器实现了简单的聊天功能，客户端和服务器可以互相发送任意多条消息
    客户端：......
    服务器：......
    客户端：......
    服务器：......
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/*
    创建服务器套接字用ServerSocket
    创建客户端套接字用Socket
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //1.创建绑定到指定端口的服务器套接字
        ServerSocket tcpServerSocket=new ServerSocket(1234);

        Scanner sc=new Scanner(System.in);
        while(true){
            //2.服务器和客户端的链接，如果链接不上一直在这里停留
            Socket cilentSocket=tcpServerSocket.accept();
            InetAddress cilentAddress=cilentSocket.getInetAddress();
            System.out.printf("有客户端 %s ：%d 连接上来\n",
                    cilentAddress.getHostAddress(),  //客户端的IP
                    cilentSocket.getPort()           //客户端的端口号
            );

             //获取输入字节流
            InputStream is=cilentSocket.getInputStream();
             //字节流转换为字符流
            InputStreamReader isReader=new InputStreamReader(is,"UTF-8");
             //字符流转换缓冲字符流（主要是为了用它的 readLine() 方法）
            BufferedReader reader=new BufferedReader(isReader);

            //获取输出字节流
            OutputStream os=cilentSocket.getOutputStream();  //从客户端得到的输出字节流
            //将字节流转换为字符流（主要是为了用它的 println() 方法）
            PrintStream out=new PrintStream(os);

            String line;
            while((line=reader.readLine())!=null){  //从客户端读发来的消息
                System.out.println("客户端："+line);
                System.out.print("服务器：");
                String response=sc.nextLine();
                out.println(response);   //输出到客户端
            }
        }
    }
}

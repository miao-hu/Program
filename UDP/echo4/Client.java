package udp.echo4;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //1.创建一个客户端的套接字
        DatagramSocket udpClientSocket=new DatagramSocket();

        Scanner sc=new Scanner(System.in);
        while(true){
            //把请求放到缓冲区中
            System.out.print("客户端：");
            String request=sc.nextLine();
            byte[] sendBuffer=request.getBytes("UTF-8");

            //本机的IP为127.0.0.1
            byte[] serverIp={127,0,0,1};
            InetAddress serverAddress=InetAddress.getByAddress(serverIp);
            DatagramPacket sendPacket=new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    serverAddress,
                    8888
            );

            //2.从客户端向服务器端发送数据报文
            udpClientSocket.send(sendPacket);

            //3.接受服务器端回复的消息
            byte[] receiveBuffer=new byte[1024];
            DatagramPacket receivePacket=new DatagramPacket(
                    receiveBuffer,
                    receiveBuffer.length
            );
            udpClientSocket.receive(receivePacket);

            //4.打印出服务器端回复的消息
            String response=new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    "UTF-8"
            );
            System.out.println("服务器："+response);
           // udpClientSocket.close(); 这条语句去掉可以实现多次查询
        }
    }
}

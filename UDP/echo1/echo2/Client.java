package udp.echo2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        // 1. 新建一个 DatagramSocket (新建一个数据报套接字）
        DatagramSocket udpClientSocket=new DatagramSocket();  //客户端不需要绑定端口号
        String message="你收到我的消息没？";
        byte[] sendBuffer=message.getBytes("UTF-8");  //把要发送的消息放到缓冲区
        // 127.0.0.1是本机的IP  要想连接自己的服务器  那么IP就为它
        byte[] serverIp=new byte[4];  //服务器的IP
        serverIp[0]=127;
        serverIp[1]=0;
        serverIp[2]=0;
        serverIp[3]=1;
        InetAddress serverAddress=InetAddress.getByAddress(serverIp);  //得到服务器端的地址
        DatagramPacket sendPacket=new DatagramPacket(
                sendBuffer,
                sendBuffer.length,
                serverAddress,  //服务器的地址
                5055      //服务器的端口号
        );
        udpClientSocket.send(sendPacket);
        udpClientSocket.close();
    }
}

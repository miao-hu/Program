package udp.echo3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
                6233      //服务器的端口号
        );
        udpClientSocket.send(sendPacket);
        //------------------以上部分从客户端往服务器端发送了一条消息

        //------------------以下部分从服务器端发送了刚刚原样的消息给客户端（即服务器端回复的消息）
        byte[] receiveBuffer=new byte[1024];
        DatagramPacket receivePacket=new DatagramPacket(
                receiveBuffer,
                receiveBuffer.length   //把服务器端回复的消息放到数据报文中
        );
        udpClientSocket.receive(receivePacket);  //客户端收到回复的消息

        String responseMessage=new String(
                receivePacket.getData(),
                0,
                receivePacket.getLength(),
                "UTF-8"
        );
        System.out.println(responseMessage);  //打印服务器端回复的消息

        udpClientSocket.close(); //关闭套接字
    }
}

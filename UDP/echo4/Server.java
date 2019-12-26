package udp.echo4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    //类一进来先加载属性，然后静态代码快
    private static final Map<String,String> dict=new HashMap<>();
    static{
        dict.put("cat","喵喵");
        dict.put("dog","汪汪");
        dict.put("fish","好吃");
        dict.put("pig","哼哼");
    }

    public static void main(String[] args) throws IOException {
        //1.创建一个服务器端的套接字，并且绑定端口号
        DatagramSocket udpServerSocket=new DatagramSocket(8888);
        while(true){
            byte[] receiveBuffer=new byte[1024];
            DatagramPacket receivePacket=new DatagramPacket(
                    receiveBuffer,
                    receiveBuffer.length
            );
            //从客户端来收到数据报文（消息）
            udpServerSocket.receive(receivePacket);

            InetAddress clientAddress=receivePacket.getAddress();
            System.out.printf("我从 %s : %d 收到了 %d 字节长度的消息\n",
                    clientAddress.getHostAddress(),  //客户端IP
                    receivePacket.getPort(),         //客户端端口号
                    receivePacket.getLength()
            );

            //把从客户端收到的消息打印出来
            String message=new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    "UTF-8"
            );
            System.out.println("客户端:"+message);

            //服务器给客户端回复一条消息
            String response=dict.getOrDefault(message,"我听不懂你说的");
            //把要回复的消息放到缓冲区中
            byte[] sendBuffer=response.getBytes("UTF-8");

            DatagramPacket sendPacket=new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    clientAddress,           //客户端的InetAddress型的IP地址
                    receivePacket.getPort()  //客户端的端口号（上面已经得到）
            );

            //服务器向客户端回复一条数据报文
            udpServerSocket.send(sendPacket);
        }
    }
}

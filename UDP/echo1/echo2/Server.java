package udp.echo2;
/*  UDP是面向报文的
在这个包下的服务器和客户端下，服务器接收客户端发来的消息并且将原样消息发送给客户端
但是客户端没有接收，所以客户端那边没有显示任何消息
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) throws IOException {
        // 1. 新建一个 DatagramSocket (新建一个数据报套接字）
        DatagramSocket udpServerSocket=new DatagramSocket(5055); //绑定一个端口号

        while(true){
            byte[] receiveBuffer=new byte[1024];//接受消息缓冲区
            DatagramPacket receivePacket=new DatagramPacket(receiveBuffer,receiveBuffer.length);

            // 2. 等着客户端来撩
            udpServerSocket.receive(receivePacket);  //从此套接字接收数据报

            //得到客户端的地址
            InetAddress cilentAddress=receivePacket.getAddress();
            System.out.printf("我从客户端 %s ： %d 收到了消息\n",
                    cilentAddress.getHostAddress(), //客户端的IP地址
                    receivePacket.getPort());       //客户端的端口号

            String message=new String(
                    receivePacket.getData(),  //从数据报文中取到数据
                    0,
                    receivePacket.getLength(),
                    "UTF-8");
            System.out.println(message); //打印从客户端收到的消息
        }
    }
}

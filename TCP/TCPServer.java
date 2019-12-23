import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/*
    这个版本的客户端服务器实现的功能：
        1.客户端给服务器发送消息（早上好，中午好，晚上好）
        2.服务器给客户端回复消息（早上好，中午好，晚上好）
 */
public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(8088);
        while (true){
            // 等待客户端建立连接
            Socket clientSocket=serverSocket.accept();   // 阻塞，时间沧海桑田

            // 有客户端建立了连接
            InputStream is=clientSocket.getInputStream();
            Reader reader=new InputStreamReader(is,"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(reader);

            OutputStream os=clientSocket.getOutputStream();
            Writer writer=new OutputStreamWriter(os,"UTF-8");
            PrintWriter out=new PrintWriter(writer,false);

            String request;
            while((request=bufferedReader.readLine())!=null){
                System.out.println("客户端:"+request);

                out.println(request);
                out.flush();
            }
            serverSocket.close();
        }
    }
}

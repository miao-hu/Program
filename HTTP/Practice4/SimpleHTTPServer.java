import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Http服务器
public class SimpleHTTPServer {
    public static void main(String[] args) throws IOException {
        //创建服务器套接字（80端口）
        ServerSocket serverSocket=new ServerSocket(80);
        //创建10个大小的线程池
        ExecutorService pool=Executors.newFixedThreadPool(10);
        while(true){
            //客户端套接字（若没有客户端连接上来则一直在这里等待连接）
            Socket socket=serverSocket.accept();
            //把任务交给线程池中的线程去完成
            pool.execute(new Task(socket));
        }
    }
}

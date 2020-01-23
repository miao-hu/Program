package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        //1.创建服务器套接字（设置端口：1234）
        ServerSocket serverSocket = new ServerSocket(1234);
        //2.创建固定大小（10个大小）的线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);
        while (true) {
            //3.等待客户端来连接服务器（若没有客户端连接上，则在此一直等待）
            Socket socket = serverSocket.accept();
            //4.把客户端带来的任务交给线程池中的线程去完成
            pool.execute(new Task(socket));
        }
    }
}


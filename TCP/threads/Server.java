package tcp.threads;
/*
    多线程版本的服务器和客户端
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {
    private static class TalkRunnable implements Runnable{
        private Socket clientSocket;  //客户端套接字
        TalkRunnable(Socket socket){
            this.clientSocket=socket;
        }

        @Override
        public void run() {
            try {
                //客户端的地址
                InetAddress clientAddress = clientSocket.getInetAddress();
                System.out.printf("客户端 %s : %d 连接上来%n",
                        clientAddress.getHostAddress(),  //客户端IP
                        clientSocket.getPort()          //客户端端口号
                );

                // 从客户端向服务器获取输入字节流
                InputStream is = clientSocket.getInputStream();
                // 字节流转换为字符流
                InputStreamReader isReader =new InputStreamReader(is, "UTF-8");
                // 字符流转换缓冲字符流
                BufferedReader reader = new BufferedReader(isReader);

                // 从客户端向服务器获取输出字节流
                OutputStream os = clientSocket.getOutputStream();
                PrintStream out = new PrintStream(os, true, "UTF-8");

                Scanner sc=new Scanner(System.in);
                while (true) {
                    String line = reader.readLine();
                    System.out.println("客户端：" + line);
                    System.out.print("服务器：");
                    String response =sc.nextLine();
                    out.println(response);
                    out.flush();  //刷新
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //1.创建一个服务器的套接字，并且绑定它的端口号
        ServerSocket tcpServerSocket=new ServerSocket(9999);

        //2.创建一个阻塞队列，用在线程池中，但是这个阻塞队列是链表阻塞队列，最大上限为INT取值范围的最大值
        BlockingQueue<Runnable> queue=new LinkedBlockingDeque<>();

        //3.创建一个线程池
        ExecutorService pool=new ThreadPoolExecutor(
                100,       //正式员工
                110,  //正式员工+临时员工
                10,      //临时员工最长空闲时间
                TimeUnit.MICROSECONDS, //计时单位
                queue                   //阻塞队列
        );

        while(true){
            //4.服务器与客户端进行连接，如果连接不上，则一直停留在这里
            Socket clientSocket=tcpServerSocket.accept();

            //5.如果连接上了，则交给线程池去做该做的事情
            pool.execute(new TalkRunnable(clientSocket));
            /*
                 new TalkRunnable(clientSocket)  创建一个它，则要去执行他的run()方法
             */
        }
    }
}

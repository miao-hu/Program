import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerThreads {
    private static class MyRunnable implements Runnable{
        private Socket clientSocket;

        private MyRunnable(Socket socket){
            this.clientSocket=socket;
        }
        @Override
        public void run() {
            try{
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
                clientSocket.close();   //关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        //创建一个容量为10的线程池
        ExecutorService pool= Executors.newFixedThreadPool(10);
        ServerSocket serverSocket=new ServerSocket(8088);

        while(true){
            // 等待客户端建立连接
            Socket clientSocket=serverSocket.accept();  // 阻塞，时间沧海桑田

            // 有客户端建立了连接,将任务交给线程池
            pool.execute(new MyRunnable(clientSocket));
        }
    }
}

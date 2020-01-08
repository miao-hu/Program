import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    服务器功能：解析请求，发送响应
           解析客户端发来的请求
           发送自己生成的响应
 */
public class SimpleHTTPServer {
    private static class Task implements Runnable{
        private static Socket socket;
        public Task(Socket socket){
            this.socket=socket;
        }
        @Override
        public void run() {
            try{
                //获取从客户端的输入流
                InputStream is=socket.getInputStream();
                //获取往客户端写的输出流
                OutputStream os=socket.getOutputStream();

                //解析请求，并把请求封装在一个Request对象中
                Request request=Request.parse(is);
                System.out.println(request);

                if(request.path.equalsIgnoreCase("/")){
                    String body="<h1>成功访问</h1>";
                    byte[] bodyBuffer=body.getBytes("UTF-8");
                    StringBuilder response=new StringBuilder();
                    response.append("HTTP/1.0 200 OK\r\n");
                    response.append("Content-Type: text/html; charset=UTF-8\r\n");
                    //因为响应正文是按html来进行识别的，所以body内容按照html来处理，显示body为一号标题
                    /*
                    response.append("Content-Type: text/plain; charset=UTF-8\r\n");
                    //因为响应正文是按plain来进行识别的，所以body内容按照plain来处理,直接显示body内容
                    */
                    response.append("Content-Length: ");
                    response.append(bodyBuffer.length);
                    response.append("\r\n");
                    response.append("\r\n");//空行

                    os.write(response.toString().getBytes("UTF-8"));
                    os.write(bodyBuffer);
                    os.flush();
                }else{
                    StringBuilder response=new StringBuilder();
                    response.append("HTTP/1.0 404 Not Found\r\n");
                    response.append("\r\n");  //没有响应头，直接空行
                    os.write(response.toString().getBytes("UTF-8"));
                    os.flush();
                }
                // 处理业务（请求）：........

                /* 发送响应：
                    String body = "<h1>一切正常</h1>";
                    // 拼接响应
                    Response response = Response.build(os);
                    response.println(body);  // 发送响应
                    response.flush();
                 */

                socket.close();//关闭客户端的连接
            }catch(Exception e){

            }
        }
    }
    public static void main(String[] args) throws IOException {
        // 创建一个 监听 socket，80 监听端口用来分用，一个端口只能属于一个进程
        ServerSocket serverSocket=new ServerSocket(80);
        //创建线程池，最多启动10个线程，类比平时开公司， 只有 10 个员工的公司
        ExecutorService poll= Executors.newFixedThreadPool(10);

        // 不断地接待客人
        while(true){
            //没有客户端来连接，方法就不会返回,返回的是一个通信 socket
            Socket socket=serverSocket.accept();
            //把任务交给线程池去做
            poll.execute(new Task(socket));

            /*  类似：
                    主线程只负责前台任务
                    对接客户任务全部交给工作线程去处理
                    为了效率，我们采用线程池的方式管理工作线程
                    把来的客人引荐给工作线程
             */
        }
    }
}

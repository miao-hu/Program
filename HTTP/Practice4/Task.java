import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Task implements Runnable {
    private Socket socket;

    public Task(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            InputStream is=socket.getInputStream();   //从客户端读输入流
            OutputStream os=socket.getOutputStream(); //从客户端读输出流

            Request request=Request.parse(is);  //解析客户端发来的请求，并把结果封装成一个对象
            System.out.println(request);        //打印请求内容

            Response response=new Response();   //构造一个响应对象
            HttpServlet servlet;
            if(request.path.equals("/joke.js")){
                servlet=new JokeJSServlet();
                servlet.doGet(request,response);
            }else{
                servlet=new NotFoundServlet();
                servlet.doGet(request,response);
            }
            response.writeAndFlush(os);         //往客户端回复响应
            socket.close();                    //关闭连接

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

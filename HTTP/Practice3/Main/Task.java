package Main;

import Servlet.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Task implements Runnable {
    private Socket socket;   //客户端套接字
    private Map<String, HttpServlet> urlMap=new HashMap<>();        //存储不同的url对应的HttpSerlvet
    private HttpServlet notFoundServlet = new NotFoundServlet();   //404对应的HttpSerlvet
    private HttpServlet staticServlet = new StaticServlet();       //本地的HttpSerlvet

    public Task(Socket socket) {
        this.socket=socket;
        urlMap.put("/login", new LoginServlet());         //127.0.0.1:1234/login
        urlMap.put("/joke.js",new JokeJsServlet());        //127.0.0.1:1234/joke.js
        urlMap.put("/plain",new PlainServlet());          //127.0.0.1:1234/plain
        urlMap.put("/run",new RunServlet());              //127.0.0.1:1234/run
        urlMap.put("/redirect",new RedirectServlet());   //127.0.0.1:1234/redirect
    }

    @Override
    public void run() {
        try {
            InputStream is=socket.getInputStream();   //得到客户端的输入流
            OutputStream os=socket.getOutputStream(); //得到客户端的输出流

            //1.分析并解析客户端发送来的请求(将解析的请求封装成一个对象)
            Request request=Request.parse(is);
            System.out.println(request);        //打印请求
            Response response=new Response();

            //2.根据不同的url请求，处理相应的业务
            HttpServlet serlvet=urlMap.get(request.url);
            if(serlvet==null){   //如果urlMap中没有相应的url
                String fileName="LocalFile"+request.url;
                File file=new File(fileName);
                if(!file.exists()){   //相对路径下寻找
                    serlvet=notFoundServlet;
                }else{
                    serlvet=staticServlet;   //在这个LocalFile目录下有这个文件
                }
            }
            serlvet.doGet(request,response);

            //3.向客户端发送响应
            response.writeAndFlush(os);

            //4.关闭连接
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

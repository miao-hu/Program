package Servlet;

import Main.Request;
import Main.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class StaticServlet extends HttpServlet {
    @Override
    //例如：     127.0.0.1:1234/hello.html
    //默认响应行： HTTP/1.0 200 OK
    //默认响应头："Content-Type", "text/html; charset=utf-8"
    public void doGet(Request req, Response resp) {
        // 0. 取 url 的后缀
        int index = req.url.lastIndexOf('.');
        String suffix = req.url.substring(index + 1);   //html
        if (suffix.equals("css")) {
            resp.setHeader("Content-Type", "text/css; charset=UTF-8");
            //默认：("Content-Type", "text/html; charset=UTF-8");
        }

        // 1. 根据 url 拼接本地对应的文件名
        String filename = "LocalFile" + req.url;     //  LocalFile/hello.html

        // 2. 读取文件内容并写入 resp 中,然后发给客户端
        try {
            InputStream is = new FileInputStream(filename);            //字节流
            Scanner scanner = new Scanner(is, "UTF-8"); //字符流
            while (scanner.hasNextLine()) {
                String line=scanner.nextLine();
                resp.println(line);   //响应正文中增加line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

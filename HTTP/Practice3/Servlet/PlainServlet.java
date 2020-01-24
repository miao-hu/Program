package Servlet;

import Main.Request;
import Main.Response;

public class PlainServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //默认响应行： HTTP/1.0 200 OK
        resp.setHeader("Content-Type","text/plain; charset=utf-8");
        resp.println("<h1>我不是html元素，我是plain元素</h1>");
    }
}

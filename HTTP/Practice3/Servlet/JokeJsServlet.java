package Servlet;

import Main.Request;
import Main.Response;

public class JokeJsServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //默认响应行： HTTP/1.0 200 OK
        resp.setHeader("Content-Type", "application/javascript; charset=utf-8");
        resp.println("alert('你好');");
    }
}

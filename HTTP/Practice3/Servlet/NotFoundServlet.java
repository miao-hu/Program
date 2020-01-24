package Servlet;

import Main.Request;
import Main.Response;

public class NotFoundServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //默认版本： HTTP/1.0
        resp.setStatus("404 Not Found");
        resp.println("<h1>该请求对应的页面没有找到</h1>");
    }
}

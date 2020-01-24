package Servlet;

import Main.Request;
import Main.Response;

public class RunServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //默认响应行： HTTP/1.0 200 OK
        //默认响应头："Content-Type", "text/html; charset=utf-8"
        resp.println("<script src='joke.js'></script>");
    }
}

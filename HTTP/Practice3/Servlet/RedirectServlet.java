package Servlet;

import Main.Request;
import Main.Response;

public class RedirectServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //默认响应行： HTTP/1.0 200 OK
        //默认响应头："Content-Type", "text/html; charset=utf-8"
        //307临时重定向：重定向位置寻找响应头中Location所对应的value
        resp.setStatus("307 Temporary Redirect");
        resp.setHeader("Location", "https://www.qq.com/");
    }
}

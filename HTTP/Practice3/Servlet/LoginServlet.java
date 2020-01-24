package Servlet;

import Main.Request;
import Main.Response;

// 格式：    127.0.0.1:1234/login?username=用户名
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        String username=req.parameters.get("username");  //两种结果
        if (username.isEmpty()) {
            //默认版本： HTTP/1.0
            resp.setStatus("401 Unauthorized");   //401（未授权）需要客户端对自己认证
            //默认响应头："Content-Type", "text/html; charset=utf-8"
            resp.println("<h1>登录失败</h1>");
            return;
        }
        //走到这说明登陆成功,Set-Cookie
        //默认响应行： HTTP/1.0 200 OK
        //默认响应头："Content-Type", "text/html; charset=utf-8"
        resp.setHeader("Set-Cookie", "username=" + username);
        resp.println("<h1>Set-Cookie成功！欢迎 " + username + " 访问</h1>");  //默认html格式
    }
}

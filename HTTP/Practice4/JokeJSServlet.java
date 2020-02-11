public class JokeJSServlet extends HttpServlet {
    @Override
    public void doGet(Request req, Response resp) {
        //设置响应状态码+状态码描述
        resp.setStatus("200 OK");
        //设置响应头
        resp.setHeader("Content-Type","application/javascript; charset=utf-8");
        //设置响应正文
        resp.println("alert('很好很好');");
    }
}

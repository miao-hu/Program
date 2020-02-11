public class NotFoundServlet extends HttpServlet{
    @Override
    public void doGet(Request req, Response resp) {
        resp.setStatus("404 Not Found");
        resp.setHeader("Content-Type","text/html; charset=utf-8");
        resp.println("<h1>没有找到页面</h1>");
    }
}

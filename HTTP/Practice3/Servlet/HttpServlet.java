package Servlet;

import Main.Request;
import Main.Response;

public abstract class HttpServlet {
    public abstract void doGet(Request req, Response resp);
}

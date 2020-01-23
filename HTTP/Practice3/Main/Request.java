package Main;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Request {
    public String method;   //方法
    public String url;      //路径
    public String version;  //版本
    public Map<String, String> parameters = new HashMap<>();   //查询字符串
    public Map<String, String> headers = new HashMap<>();      //请求头

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", parameters=" + parameters +
                ", headers=" + headers +
                '}';
    }

    //解析请求
    public static Request parse(InputStream is) throws UnsupportedEncodingException {
        Request request = new Request();
        Scanner scanner = new Scanner(is, "UTF-8");  //字节流到字符流的转换

        parseRequestLine(request,scanner);    //解析请求行
        parseRequestHeaders(request,scanner); //解析请求头

        return request;
    }

    //解析请求头
    private static void parseRequestHeaders(Request request, Scanner scanner) {
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            String[] kv = line.split(":");
            String key = kv[0].trim();
            String value = kv[1].trim();
            request.headers.put(key, value);
        }
    }

    //解析请求行
    private static void parseRequestLine(Request request, Scanner scanner) throws UnsupportedEncodingException {
        String line=scanner.nextLine();
        String[] group = line.split(" ");
        request.method = group[0];   //方法
        parseUrl(group[1], request); //解析URL    /login?name=hello&age=18&sex=
        request.version = group[2];  //版本
    }

    /* 解析路径group[1]部分：
           /c%2B%2B?name=hello&age=18&sex=
           ?前面的是带层次的文件路径    ?后面的是查询字符串
           url= c++        {name = hello}
           1. url部分考虑需要 URL 解码
           2. 把查询字符串部分分离到 parameters 中
    */
    private static void parseUrl(String s, Request request) throws UnsupportedEncodingException {
        String[] group = s.split("\\?");   //转义字符
        request.url = URLDecoder.decode(group[0], "UTF-8");
        if (group.length == 2) {  //有无查询字符串,需要判断
            String[] segment = group[1].split("&");
            for (String kvString : segment) {    //name=hello     age=18    sex=
                String[] kv = kvString.split("=");
                String key = URLDecoder.decode(kv[0], "UTF-8");
                String value = "";
                if (kv.length == 2) {
                    value = URLDecoder.decode(kv[1], "UTF-8");
                }
                request.parameters.put(key, value);
            }
        }
    }
}

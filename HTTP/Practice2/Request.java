import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
一个请求对象：请求行
              请求头
              \r\n
              请求正文
 */
public class Request {
    //请求行
    String method;   //方法
    String path;     //路径
    String version;  //版本

    //请求头
    Map<String,String> headers =new HashMap<>();

    //解析请求方法
    public static Request parse(InputStream is) {
        Request request=new Request();
        // is 是 字节流，不太容易读一行，用熟悉的 Scanner 处理
        Scanner sc=new Scanner(is,"UTF-8");//把字节流转换为字符流
        // 1. 解析请求行
        String requestLine=sc.nextLine();
        parseRequestLine(request,requestLine);

        // 2. 解析请求头(直到空行出现)
        String line;
        while(!(line=sc.nextLine()).isEmpty()){
            String[] kv=line.split(":");
            String key=kv[0].trim();
            String value=kv[1].trim();
            request.headers.put(key,value);
        }
        return request;
    }

    private static void parseRequestLine(Request request, String requestLine) {
        String[] group=requestLine.split(" ");
        request.method=group[0];
        request.path=group[1];
        request.version=group[2];
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                '}';
    }
}

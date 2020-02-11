import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Request {
    String method;  //请求方法
    String path;    //请求路径
    String version; //请求版本

    Map<String,String> parameters=new HashMap<>();   //查询字符串
    Map<String,String> headers=new HashMap<>();      //请求报头

    //解析客户端发来的请求
    public static Request parse(InputStream is) throws UnsupportedEncodingException {
        Request request=new Request();
        Scanner scanner=new Scanner(is,"UTF-8");
        parseLine(request,scanner);     //解析请求行
        parseHeaders(request,scanner);  //解析请求报头
        return request;
    }

    private static void parseHeaders(Request request, Scanner scanner) {
        String line;
        while(!(line=scanner.nextLine()).isEmpty()){
            String[] kv=line.split(":");
            String key=kv[0].trim();
            String value=kv[1].trim();
            request.headers.put(key,value);
        }
    }

    private static void parseLine(Request request, Scanner scanner) throws UnsupportedEncodingException {
        String line;
        line=scanner.nextLine();    //请求行
        String[] group=line.split(" ");
        request.method=group[0];
        /* 解析路径group[1]部分
                /c%2B%2B?name=hello&age=18&sex=
                ?前面的是带层次的文件路径    ?后面的是查询字符串
                 path = c++        {name = hello}
                1. 路径部分考虑需要 URL 解码
                2. 把查询字符串部分分离到 parameters 中
         */
        String[] segment=group[1].split("\\?");   //转义字符
        request.path=segment[0];
        if(segment.length!=1){   //有查询字符串
            String[] kv=segment[1].split("&");
            for(String s:kv){  //name=hello    age=18    sex=
                String[] kv2=s.split("=");
                String key=URLDecoder.decode(kv2[0],"UTF-8");
                String value="";
                if(kv2.length==2){
                    value=URLDecoder.decode(kv2[1],"UTF-8");
                }
                request.parameters.put(key,value);
            }
        }
        request.path=URLDecoder.decode(request.path,"UTF-8");
        request.version=group[2];
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", parameters=" + parameters +
                ", headers=" + headers +
                '}';
    }
}

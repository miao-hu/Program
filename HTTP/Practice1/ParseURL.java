import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
/*
    功能：解析URL
 */
public class ParseURL {
    private static Map<String,Integer> ports=new HashMap<>();
    static{
        ports.put("http",80);
        ports.put("https",443);
        ports.put("jdbc:mysql",3306);
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        String url="https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=C%2B%2B&rsv_pq=da170afd00081fa7&rsv_t=5465knISa4V8JKHBDgkWAlG7Ik1%2B2Vc18bs%2BBiIdQnVLs8Km%2F%2FyNrI%2BpUk4&rqlang=cn&rsv_dl=tb&rsv_enter=1&rsv_sug3=4&rsv_sug1=4&rsv_sug7=101&rsv_sug2=0&inputT=2543&rsv_sug4=3135";
        int index;
        index=url.indexOf("://");
        String schema=url.substring(0,index);
        url=url.substring(index+3);
        System.out.println("协议名:"+schema);

        index=url.indexOf("/");
        String hostAndPort=url.substring(0,index);
        url=url.substring(index);
        String host;
        int port;
        if(hostAndPort.contains(":")){   //字符串包含端口号
            String[] arr=hostAndPort.split(":");
            host=arr[0];
            port=Integer.valueOf(arr[1]);
        }else{    //字符串不包含端口号
            host=hostAndPort;
            port=ports.get(schema);   //协议对应端口号
        }
        System.out.println("服务器地址:"+host);
        System.out.println("服务器端口号:"+port);

        index=url.indexOf("?");
        String path=url.substring(0,index);
        url=url.substring(index+1);
        System.out.println("带层次的文件路径:"+path);

        index=url.indexOf("#");
        String queryString;   //查询字符串
        String segment=" ";       //片段标识符
        if(index!=-1) {
            queryString = url.substring(0, index);
            schema=url.substring(index+1);
        }else{
            queryString=url;
        }
        System.out.println("查询字符串："+queryString);
        System.out.println("片段标识符:"+segment);  //为空就表明没有片段标识符

        System.out.println("查询字符串进行分割如下所示：");
        String[] kv=queryString.split("&");
        for(String e:kv){
            System.out.println(URLDecoder.decode(e,"UTF-8"));  //解码
        }

    }
}

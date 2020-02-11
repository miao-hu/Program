import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    String status;   //响应行：状态+状态码描述   200 OK
    Map<String,String> headers=new HashMap<>();   //响应头
    StringBuilder body=new StringBuilder();   //响应正文

    //设置状态
    public void setStatus(String status){
        this.status=status;
    }

    //设置响应头
    public void setHeader(String key,String value){
        headers.put(key,value);
    }

    //设置响应正文
    public void print(String s){
        body.append(s);
    }

    //设置响应正文
    void println(String s) {
        body.append(s);
        body.append("\r\n");
    }

    //往客户端发送响应
    public void writeAndFlush(OutputStream os) throws IOException {
        //1.组装响应
        StringBuilder responseBuilder=new StringBuilder();
        //组装响应行
        responseBuilder.append("HTTP/1.0 ");
        responseBuilder.append(status);
        responseBuilder.append("\r\n");

        //组装响应头
        if(body.length()!=0){   //存在响应正文
            // 计算 Content-Length
            int contentLength=body.toString().getBytes("UTF-8").length;
            setHeader("Content-Length",String.valueOf(contentLength));
        }
        for(Map.Entry<String,String> e:headers.entrySet()){
            String key=e.getKey();
            String value=e.getValue();
            responseBuilder.append(key);
            responseBuilder.append(": ");
            responseBuilder.append(value);
            responseBuilder.append("\r\n");
        }
        responseBuilder.append("\r\n");   //标志响应头结束

        //组装响应正文
        responseBuilder.append(body.toString());

        //2.发送响应
        os.write(responseBuilder.toString().getBytes("UTF-8"));
        os.flush();
    }
}

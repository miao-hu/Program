package Main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    String version="HTTP/1.0 ";   //默认版本
    String status = "200 OK";     //默认状态码+状态码描述
    Map<String, String> headers = new HashMap<>();   //响应头
    StringBuilder body = new StringBuilder();        //响应正文

    public Response() {
        headers.put("Content-Type", "text/html; charset=utf-8");
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void print(String s) {
        body.append(s);
    }

    public void println(String s) {
        body.append(s);
        body.append("\r\n");
    }

    //组装并发送响应
    public void writeAndFlush(OutputStream os) throws IOException {
        // 1. 组装响应
        String response = buildResponse();
        System.out.println("准备发送的响应: \r\n" + response);  //打印将要发送的响应
        os.write(response.getBytes("UTF-8"));      //转换为字节流写
        os.flush();
    }

    //组装响应
    private String buildResponse() throws UnsupportedEncodingException {
        StringBuilder responseBuilder = new StringBuilder();
        // 拼接响应行
        responseBuilder.append(version);
        responseBuilder.append(status);
        responseBuilder.append("\r\n");

        // 拼接响应头
        int contentLength = body.toString().getBytes("UTF-8").length;  //正文字节长度
        setHeader("Content-Length", String.valueOf(contentLength));

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseBuilder.append(entry.getKey());
            responseBuilder.append(": ");
            responseBuilder.append(entry.getValue());
            responseBuilder.append("\r\n");
        }

        // 空行
        responseBuilder.append("\r\n");

        // 响应正文
        responseBuilder.append(body);

        return responseBuilder.toString();
    }
}

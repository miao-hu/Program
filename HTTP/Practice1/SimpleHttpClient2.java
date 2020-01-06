import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleHttpClient2 {
    public static void main(String[] args) throws IOException {
        String request = "GET / HTTP/1.0\r\nHost: www.baidu.com\r\n\r\n";

        Socket socket = new Socket("www.baidu.com", 80);
        socket.getOutputStream().write(request.getBytes("UTF-8"));
        // 版本   状态码     状态描述
        // 响应头打印
        // 把响应正文保存下来
        byte[] bytes = new byte[4096];
        int len = socket.getInputStream().read(bytes);
        // 假设 4096 字节已经包含 响应行 + 所有响应头 + 一部分正文
        int index = -1;
        for (int i = 0; i < len - 3; i++) {
            if (bytes[i] == '\r' && bytes[i+1] == '\n' && bytes[i+2] == '\r' && bytes[i+3] == '\n') {
                index = i;
                break;
            }
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes, 0, index + 4);
        Scanner scanner = new Scanner(byteArrayInputStream, "UTF-8");
        String statusLine = scanner.nextLine();
        System.out.println("状态行: " + statusLine);
        String line;
        int contentLength=-1;
        while (!(line = scanner.nextLine()).isEmpty()) {
            String[] kv = line.split(":");
            String key = kv[0].trim();
            String value = kv[1].trim();
            if(key.equalsIgnoreCase("Content-Length")){
                contentLength=Integer.valueOf(value);
            }
            System.out.println("响应头: " + key + " = " + value);
        }
        System.out.println("正文长度:"+contentLength);
        System.out.println("响应行与响应头的总长度:"+index);

        //读取响应正文：
        int readContentLength=len-index-4;  //已经读了 = 第一次读到的数据长度 - index - 4;
        int toReadLength=contentLength-readContentLength; //还需再读=总的长度-已经读了
        byte[] body=new byte[contentLength]; //存放响应文本
        System.arraycopy(bytes,index+4,body,0,readContentLength);
        //实际读的长度
        int trueReadLength=socket.getInputStream().read(body,readContentLength,toReadLength);
        System.out.println("已经读了的长度："+readContentLength);
        System.out.println("还应再读的长度："+toReadLength);
        System.out.println("实际读了的长度："+trueReadLength);
        System.out.println(new String(body,0,body.length,"UTF-8"));

        FileOutputStream fileOutputStream=new FileOutputStream("百度.html");
        fileOutputStream.write(body);
    }
}

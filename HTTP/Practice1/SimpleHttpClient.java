import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SimpleHttpClient {
    public static void main(String[] args) throws IOException {
        String request = "GET / HTTP/1.0\r\nHost: www.bitedu.vip\r\n\r\n";  //这里的www.bitedu.vip用于协议
        byte[] buffer=request.getBytes("UTF-8");  //把请求放到数组缓冲区中

        Socket clientSocket=new Socket("www.bitedu.vip",80); //这里的www.bitedu.vip用于IP地址
        OutputStream os=clientSocket.getOutputStream();
        os.write(buffer);   //向服务器发送请求

        /*  接下来就是收到服务器的回复了：
                版本  状态码  状态码描述
                响应头
                响应正文
        */
        InputStream is=clientSocket.getInputStream();
        byte[] bytes=new byte[4096];  //开辟一个字节数组，用于存放服务器的回复内容
        int firstLength=is.read(bytes);
        /*  将服务器的回复内容读到bytes数组：
                1.firstLength用于存放第一次读到的长度，肯定是<=4096的
                2.假设 4096 字节已经包含  ：响应行 + 所有响应头 + 一部分正文
        */
        int index=-1;
        for(int i=0;i<firstLength-3;i++){
            if(bytes[i]=='\r'&&bytes[i+1]=='\n'&&bytes[i+2]=='\r'&&bytes[i+3]=='\n'){
                index=i;
                break;
            }
        }
        //将响应行 +所有响应头截取出来（包括\r\n\r\n)
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes,0,index+4);
        Scanner sc=new Scanner(byteArrayInputStream,"UTF-8");

        //打印响应行信息
        String responseLine=sc.nextLine();
        System.out.println("响应行:"+responseLine);
        String[] arr=responseLine.split(" ");
        System.out.println("响应版本:"+arr[0]);
        System.out.println("响应状态码:"+arr[1]);
        System.out.println("响应状态码描述:"+arr[2]);

        //打印所有响应头  ，并且找出Content-Length对应的值
        String line;
        int contentLength=-1;
            //这里不能用(line=sc.nextLine())!=null   因为后面有\r\n\r\n，进行数组访问的时候回越界
        while(!(line=sc.nextLine()).isEmpty()){
            String[] kv=line.split(":");
            String key=kv[0].trim();
            String value=kv[1].trim();
            if(key.equalsIgnoreCase("Content-Length")){
                contentLength=Integer.valueOf(value);
            }
            System.out.println("响应头："+key+"="+value);
        }
        System.out.println("正文长度:"+contentLength);
        System.out.println(index);

        /*   读取响应正文：
        int readContentLength=firstLength-index-4;  //已经读了 = 第一次读到的数据长度 - index - 4;
        int toReadLength=contentLength-readContentLength; //还需再读=总的长度-已经读了
        byte[] body=new byte[contentLength]; //存放响应文本
        System.arraycopy(bytes,index+4,body,0,readContentLength);
        //实际读的长度
        int trueReadLength=clientSocket.getInputStream().read(body,readContentLength,toReadLength);
        System.out.println("已经读了的长度："+readContentLength);
        System.out.println("还应再读的长度："+toReadLength);
        System.out.println("实际读了的长度："+trueReadLength);
        System.out.println(new String(body,0,body.length,"UTF-8"));
        */

        FileOutputStream fileOutputStream=new FileOutputStream("比特.html");
        fileOutputStream.write(bytes);
    }
}

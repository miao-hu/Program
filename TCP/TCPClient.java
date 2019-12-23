import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        // socket.bind()        socket.connect()
        Socket cilentSocket=new Socket("127.0.0.1",8088);

        OutputStream os=cilentSocket.getOutputStream();
        Writer writer=new OutputStreamWriter(os,"UTF-8");
        PrintWriter out=new PrintWriter(writer,false);

        InputStream is=cilentSocket.getInputStream();
        Reader reader=new InputStreamReader(is,"UTF-8");
        BufferedReader bufferedReader=new BufferedReader(reader);

        String[] message={"早上好","中午好","晚上好"};
        for(String s:message){
            out.println(s);
            out.flush();

            String ss=bufferedReader.readLine();
            System.out.println("服务器回复的消息:"+ss);
        }
        cilentSocket.close();
    }
}

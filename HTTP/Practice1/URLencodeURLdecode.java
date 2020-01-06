import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLencodeURLdecode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s="?";
        String s1=URLEncoder.encode(s,"UTF-8");
        String s2=URLDecoder.decode(s1,"UTF-8");
        System.out.println(s);
        System.out.println("对字符串进行加码："+s1);
        System.out.println("对字符串进行解码："+s2);
    }
}

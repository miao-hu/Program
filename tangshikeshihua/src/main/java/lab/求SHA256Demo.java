package lab;
/*
    预言：此第三方库的简单使用
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class 求SHA256Demo {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //  MD5 算法已经快被淘汰，因为没有 SHA-256 算法安全
        MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
        String s="你好世界";
        byte[] bytes=s.getBytes("UTF-8");
        messageDigest.update(bytes);  //先把要加密的内容的字节数组放进去（此方法要求传入一个字节数组，所以要把字符串进行转换）
        byte[] result=messageDigest.digest();  //把加密完算出来的值取出来
        System.out.println(result.length);

        for(byte b:result){
            System.out.printf("%02x",b);  // UTF-8 一个字节占两位，把数据一个字节一个字节往出打
        }
        // SHA-256 是哈希算法：单向计算，不能根据加密后的值推出原串
        //beca6335b20ff57ccc47403ef4d9e0b8fccb4442b3151c2e7d50050673d43172
    }
}












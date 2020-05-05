package lab;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class 求SHA256Demo {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //  MD5 (快被淘汰，因为没有 SHA-256安全)
        //  SHA-256
        MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
        String s="你好世界";
        byte[] bytes=s.getBytes("UTF-8");
        messageDigest.update(bytes);  //先放进去（此方法要求传入一个字节数组，所以要把字符串进行转换）
        byte[] result=messageDigest.digest();  //再取出来（加密完算出来的值）
        System.out.println(result.length);
        for(byte b:result){
            System.out.printf("%02x",b);  //UTF-8一个字节占两位，把数据一个字节一个字节往出打
        }
        //哈希：单向的   不能返回去
        //beca6335b20ff57ccc47403ef4d9e0b8fccb4442b3151c2e7d50050673d43172
    }
}












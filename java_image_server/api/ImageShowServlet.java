package api;

import dao.Image;
import dao.ImageDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

public class ImageShowServlet extends HttpServlet {
    static private HashSet<String> whiteList=new HashSet<>();
    static{
        whiteList.add("http://127.0.0.1:8080/java_image_server/index.html");
        //  你想要哪个网站可以访问你的图片，在 whiteList 中添加一下即可
        //  http://127.0.0.1:8080/java_image_server/imageShow?imageId=22 就不可以访问你的图片
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referer=req.getHeader("Referer");
        // 如果你的白名单中没有存储这个网址，那就无法访问你的图片内容
        if(!whiteList.contains(referer)){
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("{ \"ok\":false,\"reason\":\"该网站未授权访问\" }");
            return;
        }
        // 1.已授权，从请求中解析出 imageId
        String imageId=req.getParameter("imageId");
        if(imageId==null||imageId.equals("")){
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("{ \"ok\":false,\"reason\":\"传入的 imageId 是错误的，解析失败\" }");
            return;
        }
        // 2.根据 imageId 查找数据库中对应的图片，得到对应图片的属性信息 Path
        // （得到 path: 需要知道图片在磁盘上的存储路径 ，这样才能得到图片的内容)
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        if(image==null){  //没有该 imageId 对应的图片
            resp.getWriter().write("{ \"ok\":false,\"reason\":\"传入的 imageId 是错误的，没有对应的图片\" }");
            return;
        }
        // 3.image 对象已经得到，相当于 path 已经得到，根据路径打开图片文件，读取其中的图片内容，写入到响应对象中
        resp.setContentType(image.getContentType());  //相应的识别方式就是图片的类型

        File file=new File(image.getPath());
        FileInputStream fileInputStream=new FileInputStream(file); //由于图片内容是二进制文件，应该使用字节流的方式来读取文件

        OutputStream outputStream=resp.getOutputStream();   //往响应中写
        byte[] buffer=new byte[1024];
        while(true){
            int len=fileInputStream.read(buffer); //把图片内容读到 buffer 字节数组中去
            if(len==-1){
                //文件读取结束（图片内容读取结束）
                break;
            }
            //此时已经读到了一部分数据，放到了 buffer 里，把 buffer 中的内容要写到响应对象中
            outputStream.write(buffer);
        }
        fileInputStream.close();  //关闭输入流
        outputStream.close();     //关闭输出流
    }
}

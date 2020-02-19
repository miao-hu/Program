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

public class ImageShowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.从请求中解析出 imageId
        String imageId=req.getParameter("imageId");
        if(imageId==null||imageId.equals("")){
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("{ \"ok\":false,\"reason\":\"传入的 imageId 是错误的，解析失败\" }");
            return;
        }
        // 2.根据 imageId 查找数据库，得到对应的图片属性信息（主要是为了得到 path，需要知道图片存储的路径 )
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        if(image==null){
            resp.getWriter().write("{ \"ok\":false,\"reason\":\"传入的 imageId 是错误的，没有对应的图片\" }");
            return;
        }
        // 3.根据路径打开文件，读取其中的图片内容，写入到响应对象中
        resp.setContentType(image.getContentType());
        File file=new File(image.getPath());
        //由于图片是二进制文件，应该使用字节流的方式来读取文件
        FileInputStream fileInputStream=new FileInputStream(file);
        OutputStream outputStream=resp.getOutputStream();   //往响应中写
        byte[] buffer=new byte[1024];
        while(true){
            int len=fileInputStream.read(buffer);
            if(len==-1){
                //文件读取结束
                break;
            }
            //此时已经读到了一部分数据，放到了 buffer 里，把 buffer 中的内容要写到响应对象中
            outputStream.write(buffer);
        }
        fileInputStream.close();
        outputStream.close();
    }
}

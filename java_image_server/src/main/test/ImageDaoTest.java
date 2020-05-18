package dao;
//生成一个类的测试类    ctrl+shift+t
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ImageDaoTest {

    @Test
    public void insert() {
        Image image=new Image();
        image.setImageName("202005");
        image.setSize(12345);
        image.setUploadTime("20200517");
        image.setContentType("image/png");
        image.setPath("./image/c7d3c5389c35f9e5b4ad783fdc92aaaa");
        image.setMd5("1111c183d822a04754d187b862bc75a7");

        ImageDao imageDao=new ImageDao();
        imageDao.insert(image);
    }

    @Test
    public void selectAll() {
        ImageDao imageDao=new ImageDao();
        List<Image> list=imageDao.selectAll();
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }

    @Test
    public void selectOne() {
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(80);
        System.out.println(image);
    }

    /*
        发现 BUG：每次删除都是单个删除，不能进行批量数据删除
     */
    @Test
    public void delete() {
        ImageDao imageDao=new ImageDao();
        imageDao.delete(46);
    }

    @Test
    public void selectByMd5() {
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectByMd5("d0d9c183d822a04754d187b862bc75a7");
        System.out.println(image);
    }
}

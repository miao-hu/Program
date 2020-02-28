package dao;

import common.JavaImageServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    /**
     * 把一个 image 对象插入到数据库中
     */
    public void insert(Image image){
        // 1.获取数据库连接
        Connection connection=DBUtil.getConnection();
        // 2.创建并拼装 SQL 语句
        String sql="insert into image_table values(null,?,?,?,?,?,?)";
        PreparedStatement statement=null;
        try {
            statement=connection.prepareStatement(sql);
            statement.setString(1,image.getImageName());
            statement.setInt(2,image.getSize());
            statement.setString(3,image.getUploadTime());
            statement.setString(4,image.getContentType());
            statement.setString(5,image.getPath());
            statement.setString(6,image.getMd5());

            // 3.执行 SQL 语句
            int ret=statement.executeUpdate();    //插入一条记录,影响到的行数应该为1
            if(ret!=1){
                //说明程序插入失败了，需要抛出一个异常
                throw new JavaImageServerException("插入数据库出错");
            }
        } catch (SQLException | JavaImageServerException e) {
            e.printStackTrace();
        }finally {
            // 4.关闭连接和statement对象
            DBUtil.close(connection,statement,null);
        }
    }

    /**
     * 查找数据库中所有图片的信息
     */
    public List<Image> selectAll(){
        List<Image> images=new ArrayList<>();
        // 1.获取数据库连接
        Connection connection=DBUtil.getConnection();
        // 2.创建并拼装 SQL 语句
        String sql="select * from image_table";
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            // 3.执行 SQL 语句
            statement=connection.prepareStatement(sql);
            resultSet =statement.executeQuery();
            // 4.处理结果集 resultSet ，把结果放到我们的返回值当中去
            while(resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                images.add(image);
            }
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 5.关闭连接和 statement 对象，resultSet 对象
            DBUtil.close(connection,statement,resultSet);
        }
        return null;   //出错的话返回空的结果集
    }

    /**
     * 根据 imageId 查找指定图片的信息
     * @param imageId
     * @return
     */
    public Image selectOne(int imageId){
        // 1.获取数据库连接
        Connection connection=DBUtil.getConnection();
        // 2.创建并拼装 SQL 语句
        String sql="select * from image_table where imageId = ?";
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            // 3.执行 SQL 语句
            statement=connection.prepareStatement(sql);
            statement.setInt(1,imageId);
            resultSet=statement.executeQuery();
            // 4.处理结果集
            if(resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 5.关闭连接和statement对象
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

    /**
     * 根据 imageId 删除指定图片的信息
     * @param imageId
     */
    public void delete(int imageId){
        // 1. 获取数据库连接
        Connection connection=DBUtil.getConnection();
        // 2. 拼装 SQL 语句
        String sql="delete from image_table where imageId = ?";
        PreparedStatement statement=null;
        try {
            // 3. 执行 SQL 语句
            statement=connection.prepareStatement(sql);
            statement.setInt(1,imageId);
            int ret=statement.executeUpdate();
            if(ret!=1){
                throw new JavaImageServerException("删除操作出错！");
            }
        } catch (SQLException | JavaImageServerException e) {
            e.printStackTrace();
        }finally {
            // 4. 关闭连接
            DBUtil.close(connection,statement,null);
        }
    }

    // 如果你的数据库是云服务器上, 没有在本地服务器上. 这个程序在本地直接运行就会无法访问数据库
    // 此处需要把这个程序部署到云服务器上执行，才能看到效果
    // 打一个 jar 包, 把 jar 包拷贝到云服务器上, 就可以执行了
    public static void main(String[] args) {
        // 用于进行简单的测试
        // 1. 测试插入数据是否正确
        /*  Image image = new Image();
            image.setImageName("1.png");
            image.setSize(100);
            image.setUploadTime("20200217");
            image.setContentType("image/png");
            image.setPath("./data/1.png");
            image.setMd5("11223344");
            ImageDao imageDao = new ImageDao();
            imageDao.insert(image);
        */

        // 2.测试查找所有图片信息
        /*  ImageDao imageDao=new ImageDao();
            List<Image> images=imageDao.selectAll();
            System.out.println(images);
        */

        // 3.测试根据 imageId 查找指定图片的信息
        /*  ImageDao imageDao=new ImageDao();
            Image image=imageDao.selectOne(1);
            System.out.println(image);
        */

        // 4.测试删除图片
        ImageDao imageDao=new ImageDao();
        imageDao.delete(1);
    }

    /**
     *  按照 md5 来查找数据库中的 图片
     * @param md5
     * @return
     */
    public Image selectByMd5(String md5){
        // 1.获取数据库连接
        Connection connection=DBUtil.getConnection();
        // 2.创建并拼装 SQL 语句
        String sql="select * from image_table where md5 = ?";
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            // 3.执行 SQL 语句
            statement=connection.prepareStatement(sql);
            statement.setString(1,md5);    //md5 在数据库中是按字符串形式存储的
            resultSet=statement.executeQuery();
            // 4.处理结果集
            if(resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 5.关闭连接和statement对象
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }
}

package lab;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class 插入诗词Demo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String 朝代="唐代";
        String 作者="白居易";
        String 标题="问刘十九";
        String 正文="绿蚁新醅酒，红泥小火炉。晚来天欲雪，能饮一杯无？";

        /*  获取Connection的第一种方法
                //1.注册 Driver
                Class.forName("com.mysql.jdbc.Driver");
                //2.通过 DriverManger 获取 Connection
                String url="jdbc:mysql://127.0.0.1/tangshi?useSSL=false&characterEncoding=utf8";
                Connection connection=DriverManager.getConnection(
                        url,
                        "root",
                        "123456"
                );
                System.out.println(connection);
                Statement statement=connection.createStatement();
                String sql="insert into tangshi(sha256,dynasty,author,content,words) values()";
                statement.executeUpdate(sql);
        */

        // 获取 Connection 的第二种方法(通过 DataSource 获取 Connection )
        Class.forName("com.mysql.jdbc.Driver");
        // DataSource dataSource=new MysqlDataSource();  //不带有连接池
        MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();  // 这个带有连接池（有利于管理连接）
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("tangshi");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("UTF-8");

        try(Connection connection=dataSource.getConnection()) {  //拿到连接
            String sql="insert into tangshi(sha256,dynasty,author,title,content,words) values(?,?,?,?,?,?)"; //占位符
            try(PreparedStatement statement=connection.prepareStatement(sql)) {
                statement.setString(1, "qazwsxedcrfvtgbyhnujmikolp");
                statement.setString(2, 朝代);
                statement.setString(3, 作者);
                statement.setString(4, 标题);
                statement.setString(5, 正文);
                statement.setString(6, "");
                statement.executeUpdate();   //插入
            }
        }
    }
}

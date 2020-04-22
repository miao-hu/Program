import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DBConfig {
    //饿汉模式的单例
    private static final DataSource dataSource;

    static {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("123456");
        mysqlDataSource.setDatabaseName("tangshi");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    //得到一个数据库连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

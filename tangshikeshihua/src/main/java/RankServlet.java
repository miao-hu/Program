import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    统计作者对应的诗词数量
 */
public class RankServlet extends HttpServlet {

    // GET 请求则调用该方法
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        //是否带有参数
        String condition = req.getParameter("condition");
        if (condition == null) {
            condition = "8";   //默认为 5
        }

        JSONArray jsonArray = new JSONArray();

        try {
            Connection connection = DBConfig.getConnection();   //得到数据库连接
            String sql = "SELECT author, count(*) cnt FROM tangshi GROUP BY author HAVING cnt >= ? ORDER BY cnt DESC";  //降序
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, condition);
            ResultSet resultSet = statement.executeQuery();     //返回结果集

            while(resultSet.next()) {    //结果集中有数据
                String author = resultSet.getString("author");    //作者姓名
                int count = resultSet.getInt("cnt");              //对应的诗词数量
                JSONArray item = new JSONArray();
                item.add(author);
                item.add(count);
                jsonArray.add(item);
            }

            resp.getWriter().println(jsonArray.toJSONString());    //返回 json 格式的响应

            resultSet.close();    //关闭结果集
            statement.close();    //关闭 statement
            connection.close();   //关闭数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject object = new JSONObject();
            object.put("error", e.getMessage());
            resp.getWriter().println(object.toJSONString());   // 返回 json 格式的错误响应
        }

    }
}

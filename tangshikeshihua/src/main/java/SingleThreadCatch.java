import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
/*
    单线程版本爬取数据
 */
public class SingleThreadCatch {
    public static void main(String[] args) throws Exception {
        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        String baseUrl="https://so.gushiwen.org";  //列表页的前半部分 URL
        String pathUrl="/gushi/tangshi.aspx";      //列表页的后半部分 URL

        //注意：每个详情页的 baseUrl 一样，每个详情页的 pathUrl 不一样

        //引入 SHA-256 哈希算法
        MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");

        //存储每一个详情页的 URL
        List<String> detailUrlList=new ArrayList<>();

        //1.列表页的请求和解析
        {
            String url=baseUrl+pathUrl;             // 列表页的 URL
            HtmlPage page=webClient.getPage(url);   // 请求列表页
            List<HtmlElement> divs=page.getBody().getElementsByAttribute("div","class","typecont");

            /* divs 中的内容
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont" style="border:0px;">]
             */
            for(HtmlElement div:divs){
                List<HtmlElement> as=div.getElementsByTagName("a");   // 返回每一个模块中的所有 a 标签的内容

                /* as 中的内容
                HtmlAnchor[<a href="/shiwenv_45c396367f59.aspx" target="_blank">]
                HtmlAnchor[<a href="/shiwenv_c90ff9ea5a71.aspx" target="_blank">]
                HtmlAnchor[<a href="/shiwenv_5917bc6dca91.aspx" target="_blank">]
                HtmlAnchor[<a href="/shiwenv_f324eea45183.aspx" target="_blank">]
                HtmlAnchor[<a href="/shiwenv_8d889937d1fe.aspx" target="_blank">]
                 */

                for(HtmlElement a:as){
                    String detailUrl=a.getAttribute("href");   // 返回 href 属性的值
                    detailUrlList.add(baseUrl+detailUrl);  // 将每一个详情页的 url 存储到同一个 List 列表中
                }
            }
        }

        //2.详情页的请求和解析（ detailUrlList 这个 List 列表存储每一个详情页的 url）
        for(String url:detailUrlList){
            HtmlPage page=webClient.getPage(url);  //请求每一个详情页
            String xpath;
            DomText domText;
            Object o;

            //标题
            xpath="//div[@class='cont']/h1/text()";
            //   public <T> List<T> getByXPath(final String xpathExpr);
            o=page.getBody().getByXPath(xpath).get(0);   //只取 List 列表的第一个，因为 h1 标签在整个 body 中不止一个
            domText=(DomText)o;
            String title=domText.asText();

            //朝代   （第一个 a 标签）
            xpath="//div[@class='cont']/p[@class='source']/a[1]/text()";
            //   public <T> List<T> getByXPath(final String xpathExpr);
            o=page.getBody().getByXPath(xpath).get(0);   //只取 List 列表的第一个
            domText=(DomText)o;
            String dynasty=domText.asText();

            //作者   （第二个 a 标签）
            xpath="//div[@class='cont']/p[@class='source']/a[2]/text()";
            domText=(DomText)page.getBody().getByXPath(xpath).get(0);   //只取 List 列表的第一个
            String author=domText.asText();

            //正文
            xpath="//div[@class='cont']/div[@class='contson']";
            HtmlElement element=(HtmlElement)page.getBody().getByXPath(xpath).get(0);   //只取 List 列表的第一个
            String content=element.getTextContent().trim();    //去掉正文的前后空格

            //3.计算 SHA-256
            String s=title+content;   // 标题+正文 来计算 sha-256 的值
            messageDigest.update(s.getBytes("UTF-8"));   //先放入要加密的内容
            byte[] result=messageDigest.digest();                     //加密后的值
            StringBuilder sha256=new StringBuilder();
            for(byte b:result){
                sha256.append(String.format("%02x",b));               // UTF-8 一个字节占两位
            }

            //4.计算分词
            List<Term> termList=new ArrayList<>();                   // 一个 Term  就是一个词
            // NlpAnalysis.parse(title).getTerms() 的返回值是 List 类型
            termList.addAll(NlpAnalysis.parse(title).getTerms());    //只对标题和正文进行分词
            termList.addAll(NlpAnalysis.parse(content).getTerms());

            List<String> words=new ArrayList<>();  // words 存储最终的词
            for(Term term:termList){
                if(term.getNatureStr().equals("w")){
                    continue;  //根据词性判断（标点符号不要）
                }
                if(term.getNatureStr().equals("null")){
                    continue;  //根据词性判断（null不要）
                }
                if(term.getRealName().length()<2){
                    continue;  //根据词的长度判断（词的长度小于 2 的不要）
                }
                words.add(term.getRealName());
            }
            String insertWords=String.join(",",words);     // 把 List 列表里面的内容用 ,连接

            //3.建立数据库连接
            MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();  //这个带有连接池
            dataSource.setServerName("127.0.0.1");
            dataSource.setPort(3306);
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            dataSource.setDatabaseName("tangshi");
            dataSource.setUseSSL(false);
            dataSource.setCharacterEncoding("UTF-8");

            Connection connection=dataSource.getConnection();           //得到数据库连接
            String sql="insert into tangshi(sha256,dynasty,author,title,content,words) values(?,?,?,?,?,?)";
            PreparedStatement statement=connection.prepareStatement(sql);  //创建 PreparedStatement 对象

            statement.setString(1, sha256.toString());   // sha256 本来是 String Builder类型的
            statement.setString(2, dynasty);
            statement.setString(3, author);
            statement.setString(4, title);
            statement.setString(5, content);
            statement.setString(6, insertWords);
            statement.executeUpdate();   //往数据库中插入一行数据

            //查看插入的数据
            com.mysql.jdbc.PreparedStatement mysqlStatement=(com.mysql.jdbc.PreparedStatement)statement;
            System.out.println(mysqlStatement.asSql());
        }
    }
}

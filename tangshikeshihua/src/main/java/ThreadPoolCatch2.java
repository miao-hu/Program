/*
    为了提高效率，改为多线程版本
    列表页的请求和解析在主线程中去做
    详情页的请求和解析交给线程池中的线程去做
    提取诗词信息（标题，作者，内容），计算 sha256 的值，计算分词，数据存到数据库，交给线程池中的线程去做（因为 320 首唐诗每一首都要走一遍这个流程）
    MessageDigest 不是线程安全的：每个线程创建自己的 MessageDigest 对象就可以保证使用时是安全的

    改进：① 规定如果是插入重复的数据，则忽略错误，不报错，不打印错误信息
          ②进程无法结束，鼠标一直在闪动
          ----JVM 结束的条件：所有的非后台线程结束，才会结束
              而线程池中的线程是不会结束的
              解决办法：等所有的诗都上传数据库成功后，调用 pool.shutdown();  让线程池显示结束即可
 */
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolCatch2 {
    private static AtomicInteger successCount=new AtomicInteger(0);   //原子类
    private static AtomicInteger failureCount=new AtomicInteger(0);   //原子类

    private static class Job implements Runnable{
        private String url;                      // 详情页的 url
        private DataSource dataSource;          // 数据库数据源（不想多次初始化，从主线程中直接传）

        public Job(String url , DataSource dataSource ) {
            this.url = url;
            this.dataSource = dataSource;
        }

        public void run(){
            // WebClient 不是线程安全的，每个线程创建自己的 WebClient 就可以保证使用时是安全的
            WebClient webClient=new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);

            try{
                MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");    // SHA-256 哈希算法
                //MessageDigest 不是线程安全的，每个线程创建自己的 MessageDigest 对象就可以保证使用时是安全的

                HtmlPage page=webClient.getPage(url);    // 请求详情页
                String xpath;
                DomText domText;

                //标题
                xpath="//div[@class='cont']/h1/text()";
                domText=(DomText)page.getBody().getByXPath(xpath).get(0);   //h1 标签可能有很多个，我们只取第一个 h1 标签
                String title=domText.asText();

                //朝代
                xpath="//div[@class='cont']/p[@class='source']/a[1]/text()";
                domText=(DomText)page.getBody().getByXPath(xpath).get(0);   //第一个 a 标签对应的值
                String dynasty=domText.asText();

                //作者
                xpath="//div[@class='cont']/p[@class='source']/a[2]/text()";
                domText=(DomText)page.getBody().getByXPath(xpath).get(0);   //第二个 a 标签对应的值
                String author=domText.asText();

                //正文
                xpath="//div[@class='cont']/div[@class='contson']";
                HtmlElement element=(HtmlElement)page.getBody().getByXPath(xpath).get(0);   //只取第一个
                String content=element.getTextContent().trim();

                //3.计算 SHA-256 的值
                String s=title+content;    //利用 标题和正文 计算 sha256 的值
                     //  messageDigest.update（）; 方法要求传入一个 byte[] 类型的数组
                messageDigest.update(s.getBytes("UTF-8"));     //先把要加密的内容放进去
                byte[] result=messageDigest.digest();          //得到加密后的值

                StringBuilder sha256=new StringBuilder();
                for(byte b:result){
                    sha256.append(String.format("%02x",b));    //UTF-8 一个字节占两位
                }

                //4.计算分词
                List<Term> termList=new ArrayList<>();      //每个词都是一个 Term
                // NlpAnalysis.parse(title).getTerms() 的返回值是 List
                termList.addAll(NlpAnalysis.parse(title).getTerms());       //根据标题和正文进行分词
                termList.addAll(NlpAnalysis.parse(content).getTerms());

                List<String> words=new ArrayList<>();  //存储最终的词
                for(Term term:termList){
                    if(term.getNatureStr().equals("w")){
                        continue;      //利用词性判断（标点符号不要）
                    }
                    if(term.getNatureStr().equals("null")){
                        continue;      //利用词性判断（null不要）
                    }
                    if(term.getRealName().length()<2){
                        continue;      //利用词的长度判断（长度小于2 的词不要）
                    }
                    words.add(term.getRealName());
                }
                String insertWords=String.join(",",words);    //把 List 列表里面的内容用 ,连接

                //往数据库中插入数据
                try(Connection connection=dataSource.getConnection()){
                    // Connection 不是线程安全的，每个线程创建自己的 Connection 对象就可保证线程安全

                    String sql="insert into tangshi(sha256,dynasty,author,title,content,words) values(?,?,?,?,?,?)";
                    try(PreparedStatement statement=connection.prepareStatement(sql)){
                        //PreparedStatement 不是线程安全的，每个线程创建自己的 PreparedStatement 对象就可保证线程安全

                        statement.setString(1, sha256.toString());   //sha256 的原类型是 String Builder
                        statement.setString(2, dynasty);
                        statement.setString(3, author);
                        statement.setString(4, title);
                        statement.setString(5, content);
                        statement.setString(6, insertWords);
                        statement.executeUpdate();                   //往数据库插入一行数据

                        successCount.getAndIncrement();    //插入成功  successCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                failureCount.getAndIncrement();     //插入失败  failureCount++;
            } catch (SQLException e) {
                if(!e.getMessage().contains("Duplicate entry")){  //重复插入对我们来说不是错误，不用打印错误信息
                    e.printStackTrace();
                    failureCount.getAndIncrement();      //插入失败  failureCount++;
                }else{
                    successCount.getAndIncrement();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                failureCount.getAndIncrement();          //插入失败  failureCount++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //创建一个固定大小的线程池
        ExecutorService pool=Executors.newFixedThreadPool(30);

        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        String baseUrl="https://so.gushiwen.org";  //列表页的前半部分 url
        String pathUrl="/gushi/tangshi.aspx";      //列表页的后半部分 url

        //存储每一个详情页的完整 URL
        List<String> detailUrlList=new ArrayList<>();

        //0.列表页的请求和解析放在主线程去做
        {
            String url=baseUrl+pathUrl;               //列表页的 URL
            HtmlPage page=webClient.getPage(url);     //请求列表页
            List<HtmlElement> divs=page.getBody().getElementsByAttribute("div","class","typecont");

            /* divs 中的内容：
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont">]
                  HtmlDivision[<div class="typecont" style="border:0px;">]
             */

            for(HtmlElement div:divs){
                List<HtmlElement> as=div.getElementsByTagName("a");

                /* as 中的内容：
                      HtmlAnchor[<a href="/shiwenv_45c396367f59.aspx" target="_blank">]
                      HtmlAnchor[<a href="/shiwenv_c90ff9ea5a71.aspx" target="_blank">]
                      HtmlAnchor[<a href="/shiwenv_5917bc6dca91.aspx" target="_blank">]
                      HtmlAnchor[<a href="/shiwenv_f324eea45183.aspx" target="_blank">]
                      HtmlAnchor[<a href="/shiwenv_8d889937d1fe.aspx" target="_blank">]
                 */

                for(HtmlElement a:as){
                    String detailUrl=a.getAttribute("href");    //返回 href 属性对应的值（即：详情页的后半部分 url）
                    detailUrlList.add(baseUrl+detailUrl);     //将每一个详情页的完整 url 加入到 List 列表中
                }
            }
        }

        //1.建立数据源
        MysqlConnectionPoolDataSource dataSource=new MysqlConnectionPoolDataSource();  //这个带有连接池
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("tangshi");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("UTF-8");

        System.out.println("一共有多少首诗？"+detailUrlList.size());    //320

        //2.详情页的请求和解析（要进行 320 次，把任务放到线程池中，交给线程池的线程去执行）
        for(String url:detailUrlList){
            pool.execute(new Job(url,dataSource));
        }

        //当插入成功与插入失败的个数和 < 总的个数
        while(successCount.get()+failureCount.get()<detailUrlList.size()){
            // 没有加 \n 表示只回头，不换行，每次都覆盖上次的数据
            System.out.printf("一共 %d 首诗，成功插入 %d 首诗\r",detailUrlList.size(),successCount.get());
            TimeUnit.SECONDS.sleep(1);  //1秒打印一次
        }
        System.out.println();

        System.out.println("全部上传成功");

        //显示让线程池关闭
        pool.shutdown();
    }
}

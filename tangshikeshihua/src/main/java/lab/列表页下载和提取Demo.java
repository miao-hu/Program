package lab;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class 列表页下载和提取Demo{
    public static void main(String[] args) throws IOException {
        //0.创建一个浏览器（浏览器的版本为：Chrome）  目的：爬虫的话必须假装自己是一个真实的用户
        try(WebClient webClient=new WebClient(BrowserVersion.CHROME)){
            //1.关闭了浏览器的 JS,CSS 执行引擎，不再执行网页中的 JS 脚本和 CSS 布局
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);

            //2.对该 url 对应的页面进行请求，请求得到的内容放在 HtmlPage 中
            String url="https://so.gushiwen.org/gushi/tangshi.aspx";    //列表页的 URL
            HtmlPage page=webClient.getPage(url);

            //3.取出页面的 body 部分
            HtmlElement body=page.getBody();
            List<HtmlElement> elements=body.getElementsByAttribute(
                    "div",
                    "class",
                    "typecont");

            /*
                HtmlDivision[<div class="typecont">]
                HtmlDivision[<div class="typecont">]
                HtmlDivision[<div class="typecont">]
                HtmlDivision[<div class="typecont">]
             */

            int count=0;   //计数（统计共有几个 url )
            for(HtmlElement element:elements){
                /*
                    System.out.println(element);
                    打印结果为：HtmlDivision[<div class="typecont">]
                               这个下面又有它的子内容，再对它的子内容进行提取，提取 a 标签
                 */
                List<HtmlElement> aElements=element.getElementsByTagName("a");
                for(HtmlElement a:aElements){
                    //打印 a 标签中 href 对应的属性（属性就是：每一个详情页的后半部分 url）
                    System.out.println(a.getAttribute("href"));
                    count++;
                }
            }

            System.out.println(count);  //320
        }
    }
}













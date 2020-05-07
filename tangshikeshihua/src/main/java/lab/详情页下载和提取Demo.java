package lab;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class 详情页下载和提取Demo {
    public static void main(String[] args) throws IOException {
        try(WebClient webClient=new WebClient(BrowserVersion.CHROME)){
            //不执行js,css
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            String url="https://so.gushiwen.org/shiwenv_45c396367f59.aspx";
            HtmlPage page=webClient.getPage(url);    //请求该网址

            HtmlElement body=page.getBody();  //得到该网页的 body 部分

            /*  第一种方法提取古诗的内容部分
                    List<HtmlElement> elements=body.getElementsByAttribute(
                           "div",
                           "class",
                           "contson");

                    for(HtmlElement element:elements){
                        System.out.println(element);

                            //HtmlDivision[<div class="contson" id="contson45c396367f59">]
                            //HtmlDivision[<div class="contson" id="contson731e2a19594e">]
                            //HtmlDivision[<div class="contson" id="contson4809b5e7a16a">]
                            //HtmlDivision[<div class="contson" id="contsone9b1a8b4def0">]
                            但是打印的第一个才是我们要的诗，后面三首是猜你喜欢的诗，我们不需要
                    }

                    //打印第一首诗（我们需要的那首诗）的正文部分
                    System.out.println(elements.get(0).getTextContent().trim());
            */

            //第二种方法X-Path
            //标题
            {
                String xpath="//div[@class='cont']/h1/text()";
                //   public <T> List<T> getByXPath(final String xpathExpr);
                Object o=body.getByXPath(xpath).get(0);   //只取 List 列表的第一个（第一个h1标签，因为 h1 标签不止一个）
                DomText domText=(DomText)o;
                System.out.println(domText.asText());  //打印出标题
            }
            //朝代
            {   //第一个a标签
                String xpath="//div[@class='cont']/p[@class='source']/a[1]/text()";

                Object o=body.getByXPath(xpath).get(0);   //只取 List 列表的第一个
                DomText domText=(DomText)o;
                System.out.println(domText.asText());
            }
            //作者
            {
                //第二个a标签
                String xpath="//div[@class='cont']/p[@class='source']/a[2]/text()";
                Object o=body.getByXPath(xpath).get(0);   //只取 List 列表的第一个
                DomText domText=(DomText)o;
                System.out.println(domText.asText());
            }
            //正文
            {
                String xpath="//div[@class='cont']/div[@class='contson']";
                Object o=body.getByXPath(xpath).get(0);    //只取 List 列表的第一个
                HtmlElement element=(HtmlElement)o;
                System.out.println(element.getTextContent().trim());
            }
        }
    }
}

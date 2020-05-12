package lab;
/*
    预言：此第三方库的简单使用
 */
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HtmlUtilDemo {
    public static void main(String[] args) throws IOException {
        WebClient webClient=new WebClient(BrowserVersion.CHROME);    //无界面的浏览器(相当于一个 HTTP 客户端)
        webClient.getOptions().setJavaScriptEnabled(false);          //关闭了浏览器的 js 执行引擎，不再执行网页中的 js 脚本
        webClient.getOptions().setCssEnabled(false);                 //关闭了浏览器的 css 执行引擎，不再执行网页中的 css 布局

        HtmlPage page = webClient.getPage("https://so.gushiwen.org/gushi/tangshi.aspx");    //请求此 URL 对应的页面
        System.out.println(page);    //打印内容为：HtmlPage(https://so.gushiwen.org/gushi/tangshi.aspx)@1424108509

        File file=new File("唐诗三百首\\列表页.html");
        file.delete();
        page.save(new File("唐诗三百首\\列表页.html"));    //把请求得到的页面内容保存到此文件夹

        //如何从 html 文件中提取我们需要的信息？
        HtmlElement body= page.getBody();
        List<HtmlElement> elements=body.getElementsByAttribute(
                "div",
                "class",
                "typecont");
        for(HtmlElement element:elements){
            System.out.println(element);
        }
        /* 打印结果：
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont">]
            HtmlDivision[<div class="typecont" style="border:0px;">]
         */

        System.out.println("----------------------------------------");
        HtmlElement divElement=elements.get(0);   //取第一个模块
        List<HtmlElement> aElements=divElement.getElementsByAttribute(
                "a",
                "target",
                "_blank");
        for(HtmlElement element:aElements){
            System.out.println(element);
        }
        /* 打印结果为：
            HtmlAnchor[<a href="/shiwenv_4f7e2f94ec4a.aspx" target="_blank">]
            HtmlAnchor[<a href="/shiwenv_9cee4425b019.aspx" target="_blank">]
            HtmlAnchor[<a href="/shiwenv_63d3ff8f6b61.aspx" target="_blank">]
            HtmlAnchor[<a href="/shiwenv_ccee5691ba93.aspx" target="_blank">]
            HtmlAnchor[<a href="/shiwenv_11889cf7beab.aspx" target="_blank">]
            HtmlAnchor[<a href="/shiwenv_58313be2d918.aspx" target="_blank">]
         */
        System.out.println(aElements.size());         //29
        System.out.println(aElements.get(0).getAttribute("href"));
        //         /shiwenv_45c396367f59.aspx
    }
}

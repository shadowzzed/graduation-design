package com.zed.design.demo;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Zed
 * @date 2019/12/11 下午2:26
 * @contact shadowl91@163.com
 */
public class HtmlUnitCNKIDemo {

    private static String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    public static void main(String[] args) throws IOException {
        // get webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(30000);

        // get htmlpage
        HtmlPage page = null;
        try {
            page = webClient.getPage("https://kns.cnki.net/kns/brief/result.aspx?dbprefix=SCDB&crossDbcodes=CJFQ,CDFD,CMFD,CPFD,IPFD,CCND,CCJD");

            if (page == null)
                return;
            HtmlTextInput input = (HtmlTextInput) page.getElementById("txt_1_value1");
            input.setValueAttribute("中文分词");
            DomElement btnSearch = page.getElementById("btnSearch");
            HtmlPage htmlPage = (HtmlPage) btnSearch.click();
            System.out.println(htmlPage.asXml());
            CookieManager cookieManager = webClient.getCookieManager();
            Set<Cookie> cookies = cookieManager.getCookies();
            Map<String, String> cookieMap = new HashMap<>();
            cookies.forEach(cookie -> cookieMap.put(cookie.getName(), cookie.getValue()));
            String url = "https://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&isinEn=1&dbPrefix=SCDB&dbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&research=off&t=";
            url += System.currentTimeMillis();
            String url_more = "&keyValue=%E4%B8%AD%E6%96%87%E5%88%86%E8%AF%8D&S=1&sorttype=";
            url += url_more;
            Document document = Jsoup.connect(url)
                    .userAgent(UA)
                    .cookies(cookieMap)
                    .get();
            System.out.println("start here********************");
            System.out.println(document);
            System.out.println("end here");
            HtmlPage page1 = (HtmlPage) webClient.getPage(url);
            System.out.println(page1.asXml());
        } catch (IOException e) {
            e.printStackTrace();
        }
        webClient.close();


////        HtmlForm f = page.getFormByName("CustomizeForm");
////        HtmlSubmitInput btn = f.getInputByValue("百度一下");
////        HtmlTextInput text = f.getInputByName("wd");
////        text.setValueAttribute("mvn");
//        Page nextPage =  page.getEnclosingWindow().getTopWindow().getEnclosedPage();
//        HtmlPage htmlPage = btn.click();

//        System.out.println(htmlPage.getBaseURL());
//        Document document = Jsoup.parse(htmlPage.asXml());
//        System.out.println(document.body());


    }
}

package com.zed.design.reptile;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.zed.design.core.Config;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zed
 * @date 2019/12/9 下午11:05
 * @contact shadowl91@163.com
 */
@Component
public class WebReptile {

    @Autowired
    private Config config;

    private String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    //get cnki content
    public Document getCNKIContent(String msg) {
        // get webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(3000);

        // get html page
        HtmlPage htmlPage = null;
        Document document = null;
        try {
            htmlPage = webClient.getPage(config.cnkiURL);
            if (htmlPage == null)
                throw new NullPointerException("get no page");
            HtmlTextInput input = (HtmlTextInput) htmlPage.getElementById("txt_1_value1");
            input.setValueAttribute(msg);
            DomElement btnSearch = htmlPage.getElementById("btnSearch");
            HtmlPage htmlPage1 = (HtmlPage) btnSearch.click();
            Map<String, String> cookies = new HashMap<>();
            webClient.getCookieManager().getCookies().forEach(cookie -> cookies.put(cookie.getName(), cookie.getValue()));
            String url = "https://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&isinEn=1&dbPrefix=SCDB&dbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&research=off&t=";
            url += System.currentTimeMillis();
            url += "&keyValue=";
            url += URLEncoder.encode(msg, "utf-8");
            url += "&S=1&sorttype=";
            HtmlPage targetPage = (HtmlPage) webClient.getPage(url);
            document = Jsoup.parse(targetPage.asXml());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }
        return document;
    }
}

package com.zed.design.reptile;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.zed.design.core.Config;
import com.zed.design.core.pojo.Paper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zed
 * @date 2019/12/9 下午11:05
 * @contact shadowl91@163.com
 */
@Component
public class WebReptileCNKI {

    @Autowired
    private Config config;

    private String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    private static Pattern dbcode_pattern;

    private static Pattern dbname_pattern;

    private static Pattern filename_pattern;
    static {
        String dbcode = "DbCode=.*?&";
        String dbname = "DbName=.*?&";
        String filename = "FileName=.*?&";
        dbcode_pattern = Pattern.compile(dbcode);
        dbname_pattern = Pattern.compile(dbname);
        filename_pattern = Pattern.compile(filename);
    }

    /**
     * 1. use the keyword to search for papers
     * 2. get papers in first page
     * 3. get total pages of result of this search
     * 4. get papers in urls of list
     * @param keyword
     * @return
     */
    public Document getCNKIContent(String keyword) {
        List<String> nextPageList = new ArrayList<>();
        WebClient webClient = this.getWebClient();
        // get html page
        HtmlPage htmlPage = null;
        Document document = null;
        try {
            htmlPage = webClient.getPage(config.cnkiURL);
            if (htmlPage == null)
                throw new NullPointerException("get no page");
            HtmlTextInput input = (HtmlTextInput) htmlPage.getElementById("txt_1_value1");
            input.setValueAttribute(keyword);
            DomElement btnSearch = htmlPage.getElementById("btnSearch");
            HtmlPage htmlPage1 = (HtmlPage) btnSearch.click();
            String url = "https://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&isinEn=1&dbPrefix=SCDB&dbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&research=off&t=";
            url += System.currentTimeMillis();
            url += "&keyValue=";
            url += URLEncoder.encode(keyword, "utf-8");
            url += "&S=1&sorttype=";
            HtmlPage targetPage = (HtmlPage) webClient.getPage(url);
            // get content
            document = Jsoup.parse(targetPage.asXml());
            // get table
            Elements content = document.getElementsByClass("pageBar_top").select("tr");
            // get each row
            for (Element row: content)
                this.getPaper(row);
            // get all page urls
            String total_temp = content.get(0).select("span.countPageMark").text();
            int totalPages = Integer.parseInt(total_temp.substring(2));
            this.addURLs(nextPageList, totalPages);
            for (String nextURL: nextPageList)
                this.getNextPagePaper(nextURL, webClient);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }
        return document;
    }

    // add urls to list
    private void addURLs(List<String> urls, int total) {
        StringBuilder url = new StringBuilder("https://kns.cnki.net/kns/brief/brief.aspx?curpage=");
        for (int i = 2; i < total; i++) {
            url.append(i);
            url.append("&RecordsPerPage=20&QueryID=3&ID=&turnpage=1&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_result_aspx&isinEn=1&");
            urls.add(url.toString());
        }
    }

    // get papers in next page
    private void getNextPagePaper(String url, WebClient webClient) throws IOException {
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        Document document = Jsoup.parse(page.asXml());
        Elements rows = document.getElementsByClass("pageBar_top").select("tr");
        for (Element row: rows)
            this.getPaper(row);
    }

    private WebClient getWebClient(){
        // get webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(3000);
        return webClient;
    }

    private Paper getPaper(Element row) throws IOException {
        if (StringUtils.isBlank(row.attr("bgcolor")))
            return null;
        Elements td = row.getElementsByTag("td");
        Paper paper = new Paper();
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 1:
                    paper.setName(td.get(i).text());
                    String href = td.get(i).getElementsByTag("a").attr("href");
                    paper = this.getSummary(href);
                    break;
                case 2:
                    paper.setAuthor(td.get(i).text());
                    break;
                case 3:
                    paper.setSource(td.get(i).text());
                    break;
                case 4:
                    paper.setDate(td.get(i).text());
                    break;
                case 5:
                    paper.setDb(td.get(i).text());
                    break;
                case 6:
                    paper.setRef(td.get(i).text());
                    break;
                case 7:
                    paper.setDownloads(td.get(i).text());
                    break;
                default:
                    break;
            }
        }
        // TODO save into db
        return paper;
    }

    private Paper getSummary(String href) throws IOException {
        Paper paper = new Paper();
        String url = "https://kns.cnki.net/KCMS/detail/detail.aspx?";
        Matcher dbcode = dbcode_pattern.matcher(href);
        Matcher dbname = dbname_pattern.matcher(href);
        Matcher filename = filename_pattern.matcher(href);
        if (dbcode.find()) {
            String dbcode_temp = dbcode.group();
            String dbcode_str = dbcode_temp.substring(7, dbcode_temp.length() - 1);
            url = url + "&dbcode=" + dbcode_str;
        }
        if (dbname.find()) {
            String dbname_temp = dbname.group();
            String dbname_str = dbname_temp.substring(7, dbname_temp.length() - 1);
            url = url + "&dbname=" + dbname_str;
        }
        if (filename.find()) {
            String filename_temp = filename.group();
            String filename_str = filename_temp.substring(9, filename_temp.length() - 1);
            url = url + "&filename=" + filename_str;
            paper.setFilecode(filename_str);
        }
        Document document = Jsoup.connect(url)
                .userAgent(UA)
                .get();
        // TODO if is null there seem to be post on other websites still need to process
        if (document.getElementById("ChDivSummary") == null)
            paper.setSummary(null);
        else
            paper.setSummary(document.getElementById("ChDivSummary").text());
        return paper;
    }
}

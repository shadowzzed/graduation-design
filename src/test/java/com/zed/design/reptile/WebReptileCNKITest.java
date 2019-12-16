package com.zed.design.reptile;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zed
 * @date 2019/12/9 下午11:12
 * @contact shadowl91@163.com
 */
@SpringBootTest
class WebReptileCNKITest {

    private String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    @Autowired
    WebReptileCNKI reptile;


    @Test
    public void testGetHTML() throws IOException {
        Document document = Jsoup.connect("https://kns.cnki.net/kns/brief/result.aspx?dbprefix=SCDB&crossDbcodes=CJFQ,CDFD,CMFD,CPFD,IPFD,CCND,CCJD").userAgent(UA)
                .get();
        System.out.println(document.body());
    }

//    @Test
//    public void testRep() {
//        System.out.println(reptile.rep("https://kns.cnki.net/kns/brief/default_result.aspx"));
//    }

    @Test
    public void testURL() throws UnsupportedEncodingException {
        String str = "%5B%22%22%2C%22%22%2C1575954872%2C%22https%3A%2F%2Fwww.cnki.net%2F%22%5D";
        System.out.println(URLDecoder.decode(str, "utf-8"));
    }

//    @Test
//    public void testRead() throws UnsupportedEncodingException {
//        System.out.println(reptile.readCNKI("数据挖掘"));
//    }

    @Test
    public void testCookie() throws IOException {
        Connection connection = Jsoup.connect("https://kns.cnki.net/kns/brief/result.aspx?dbprefix=SCDB&crossDbcodes=CJFQ,CDFD,CMFD,CPFD,IPFD,CCND,CCJD").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        Connection.Response response = connection
                .execute();
        Map<String, String> cookies = response.cookies();
//        System.out.println(cookies.size());
        Map<String, String> cookiesMap = new HashMap<>();
        for (Map.Entry entry: cookies.entrySet()) {
            System.out.println(entry.getKey() + "-" + entry.getValue());
            cookiesMap.put(((String) entry.getKey()), ((String) entry.getValue()));
        }
//        cookiesMap.put("ASP.NET_SessionId", "b11hnxddxglox3dphpjng1fl");
//        cookiesMap.put("Ecp_ClientId", "2191210170803359924");
//        cookiesMap.put("SID_kns", "123113");

//        cookiesMap.put("cnkiUserKey", "a21c6bc9-696e-5967-dff1-0fe712106a17");
//        cookiesMap.put("Ecp_IpLoginFail", "19121036.153.167.193");
//        cookiesMap.put("SID_krsnew", "125132");
//        cookiesMap.put("SID_kns_new", "123121");

        //        System.out.println(connection.get());
        long l = System.currentTimeMillis();
        String url = "https://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&isinEn=1&dbPrefix=SCDB&dbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&research=off&t=";
        url += l;
        url += "&keyValue=%E5%88%86%E8%AF%8D&S=1&sorttype=";
        System.out.println(url);
        System.out.println(Jsoup.connect(url).cookies(cookiesMap).userAgent(UA).get());
    }


    @Test
    public void testCookie1() throws IOException {
//        Connection connection = Jsoup.connect("https://login.cnki.net/login/?platform=kns&ForceReLogin=1&ReturnURL=https://www.cnki.net/").userAgent(UA);
//        Map<String, String> cookies = connection.execute().cookies();
//        for (Map.Entry entry: cookies.entrySet()) {
//            System.out.println(entry.getKey()+ "-" + entry.getValue());
//        }
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testGetCNKIContent() {
        System.out.println(reptile.getCNKIContent("中文分词"));
//        Elements elementsByTag = reptile.getCNKIContent("中文分词").getElementsByClass("pageBar_top").select("tr");
//        AtomicInteger count = new AtomicInteger();
//        elementsByTag.forEach(element -> {
//            if (StringUtils.isNotBlank(element.attr("bgcolor")))
//                count.getAndIncrement();
//        });
//        System.out.println(count);
        System.out.println(1);
//        System.out.println(1);
//        reptile.getCNKIContent("")
//        Document document = reptile.getCNKIContent("中文分词");
//        System.out.println(document.getElementsByTag("table").size());
    }


    @Test
    public void testSummary() throws IOException {
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.waitForBackgroundJavaScript(3000);
//
//        String summary = reptile.getSummary("<a class=\"fz14\" href='/kns/detail/detail.aspx?QueryID=6&CurRec=2&recid=&FileName=NJLD201906010&DbName=CJFDLAST2019&DbCode=CJFQ&yx=&pr=&URLID=&bsm=QS0103;' target='_blank'>区块链技术在学生档案管理中的应用模式探究</a>\n" +
//                "\n", webClient);
//        System.out.println(summary);

//        reptile.getCNKIContent("中文分词");
        Document document = Jsoup.connect("https://kns.cnki.net/KCMS/detail/detail.aspx?&dbcode=CJFQ&dbname=CJFDLAST2019&filename=NJLD201906010")
                .userAgent(UA)
                .get();
        System.out.println(document.getElementById("ChDivSummary"));
    }

    @Test
    public void testRep() {
        reptile.getCNKIContent("中文分词");
    }
}
package org.zh.elasticsearch.elasticsearchUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zh.elasticsearch.entity.Content;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class HtmlParseUtil {


    public List<Content> parseJD(String keywords) throws IOException {

        String url = "http://search.jd.com/Search?keyword="+keywords;

        Document document = Jsoup.parse(new URL(url), 30000);

        Element element = document.getElementById("J_goodsList");
        List<Content> contentList = new ArrayList<>();

        Elements elements = element.getElementsByTag("li");
        System.out.println(elements.size());
        for (Element el : elements) {
            //source-data-lazy-img
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            if(StringUtils.isEmpty(img)) img = null;
            if(StringUtils.isEmpty(price)) price = null;
            if(StringUtils.isEmpty(title)) title = null;

            Content content = new Content();
            content.setTitle(title);
            content.setImg(img);
            content.setPrice(price);
            if(!(Objects.isNull(content.getImg()) && Objects.isNull(content.getTitle()) && Objects.isNull(content.getPrice())))
                contentList.add(content);
        }
        return contentList;
    }


}

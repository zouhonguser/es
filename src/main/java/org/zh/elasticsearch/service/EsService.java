package org.zh.elasticsearch.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zh.elasticsearch.elasticsearchUtils.HtmlParseUtil;
import org.zh.elasticsearch.entity.Content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EsService {


    private void createIndex(RestHighLevelClient client) throws IOException {
        CreateIndexRequest req = new CreateIndexRequest("jd");

        req.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));

        XContentBuilder mappings = JsonXContent.contentBuilder().startObject()
                .startObject("properties")
                .startObject("title")//默认使用standard分词器
                .field("type", "text")
                .field("analyzer", "ik_smart")//使用ik_smart分词器
                .startObject("fields")
                .startObject("title_ik_smart")
                .field("type", "text")
                .field("analyzer", "ik_smart")//使用ik_smart分词器
                .endObject()
                .startObject("title_ik_max_word")
                .field("type", "text")
                .field("analyzer", "ik_max_word")//使用ik_max_word分词器
                .endObject()
                .endObject()
                .endObject()
                .endObject().endObject();

        req.mapping(mappings);
        client.indices().create(req,RequestOptions.DEFAULT);
    }

    public void createJdIndex() throws IOException {
        createIndex(restHighLevelClient);
    }


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private HtmlParseUtil htmlParseUtil;

    /**
     * 根据关键字爬取京东商品数据存储到es中
     * @param keyword  商品关键字
     * @return
     * @throws IOException
     */
    public Boolean saveInfo(String keyword) throws IOException {
        List<Content> contentList = htmlParseUtil.parseJD(keyword);
        //插入数据到es
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        ObjectMapper mapper = new ObjectMapper();
        for (Content content : contentList) {
            String writeValueAsString = mapper.writeValueAsString(content);
            bulkRequest.add(
                    new IndexRequest("jd")
                            .source(writeValueAsString, XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }





    public List<Map<String,Object>> searchPage(String keyWord,int pageNo,int pageSize) throws IOException {
        if(pageNo <= 1) pageNo =1;

        //条件搜索
        SearchRequest searchRequest = new SearchRequest("jd");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);
        //精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyWord);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        List<Map<String,Object>> lists = new ArrayList<>();
        for(SearchHit searchHit : search.getHits().getHits()) {
            lists.add(searchHit.getSourceAsMap());
        }
        return lists;
    }



}

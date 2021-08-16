package com.ant.mall.search;

import com.alibaba.fastjson.JSON;
import com.ant.mall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class MallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 检索es中的数据
     */
    @Test
    void searchData() throws IOException{
        SearchRequest request = new SearchRequest();
        //指定索引
        request.indices("bank");
        //检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        request.source(sourceBuilder);
        //检索
        SearchResponse response = client.search(request, ElasticSearchConfig.COMMON_OPTIONS);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //JSON.parseObject()
        }
    }


    /**
     * 向es中保存数据
     * @throws IOException
     */
    @Test
    void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setName("John");
        user.setAge(20);
        user.setGender("F");
        String s = JSON.toJSONString(user);
        indexRequest.source(s, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Data
    class User{
        private String name;
        private Integer age;
        private String gender;
    }

    @Test
    void contextLoads() {
        System.out.println(client);
    }

}

package com.zhai.es;

import org.apache.lucene.search.FuzzyQuery;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class ElasticSearchManage {
    TransportClient client = null;
    @Before
    public void initClient() throws Exception{
        //用的tcp协议传输
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @Test
    public void testQuery(){
        // 搜索类型 两个参数  1.字段名(域名) 2.关键字
//        QueryBuilder qb = termQuery("goodsName", "手机");
//        System.out.println("term搜索类型");

//        QueryBuilder qb = matchAllQuery();
//        System.out.println("matchAll搜索类型");

//        FuzzyQueryBuilder qb = fuzzyQuery("goodsName", "手激");
//        qb.fuzziness(Fuzziness.ONE);
//        System.out.println("fuzzy搜索类型");

//        QueryBuilder qb = wildcardQuery("goodsName", "*米*");
//        System.out.println("wildcard搜索类型");

//        QueryBuilder qb = rangeQuery("price").gte(1500).lte(1800);
//        System.out.println("range搜索类型");

        BoolQueryBuilder qb = boolQuery();
        qb.must(termQuery("goodsName","小米")).mustNot(rangeQuery("price").gte(1500).lte(1800));
        System.out.println("Bool 组合搜索类型");

        result(qb);
    }

    private void result(QueryBuilder qb) {
        //搜索请求  prepareSearch()方法指定索引库中的类型(表)
        // setQuery(类型)方法把搜索类型放进搜索请求  get()方法 不是获取方法 而是执行器
        SearchResponse scrollResp = client.prepareSearch("heima").setQuery(qb).get();

        //获取搜索结果总数
        long totalHits = scrollResp.getHits().getTotalHits();
        System.out.println("总数为:"+totalHits);

        SearchHit[] searchHits = scrollResp.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            String SourceAsString = searchHit.getSourceAsString();
            System.out.println(SourceAsString);
        }
    }


    @After
    public void end(){
        client.close();
    }

}

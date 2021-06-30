package top.sorie;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import lombok.Data;
import org.apache.http.HttpHost;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonMap;

public class EsClientTest {
    @Data
    public class AA {
        private String a = "aa";
    }
    @Data
    public static class TestClass {
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date authDate = new Date();
    }
    @Test
    public void createIndexTest2() throws IOException {
        String index = "test-time";
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("1");
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        indexRequest.source(objectMapper.writeValueAsString(new TestClass()), XContentType.JSON);
        try {
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createIndexTest() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));
        // 创建request
        CreateIndexRequest request = new CreateIndexRequest("twitter");
        // index设置
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );
        // index mapping
        request.mapping(
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        // 另外一种形式的设置mapping
//        Map<String, Object> message = new HashMap<>();
//        message.put("type", "text");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("message", message);
//        Map<String, Object> mapping = new HashMap<>();
//        mapping.put("properties", properties);
//        request.mapping(mapping);
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.startObject("properties");
//            {
//                builder.startObject("message");
//                {
//                    builder.field("type", "text");
//                }
//                builder.endObject();
//            }
//            builder.endObject();
//        }
//        builder.endObject();
//        request.mapping(builder);
        // 别名
        request.alias(new Alias("twitter_alias").filter(QueryBuilders.termQuery("user", "kimchy")));

        // 以上配置等同于
//        request.source("{\n" +
//                "    \"settings\" : {\n" +
//                "        \"number_of_shards\" : 1,\n" +
//                "        \"number_of_replicas\" : 0\n" +
//                "    },\n" +
//                "    \"mappings\" : {\n" +
//                "        \"properties\" : {\n" +
//                "            \"message\" : { \"type\" : \"text\" }\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"aliases\" : {\n" +
//                "        \"twitter_alias\" : {}\n" +
//                "    }\n" +
//                "}", XContentType.JSON);
        // 可选参数
        // 等待所有节点返回的过期时间
        request.setTimeout(TimeValue.timeValueMinutes(2));
        // 主节点的过期时间
        request.setMasterTimeout(TimeValue.timeValueMinutes(1));
        // The number of active shard copies to wait for before the create index API returns a response, as an int
        request.waitForActiveShards(ActiveShardCount.from(2));
        // The number of active shard copies to wait for before the create index API returns a response,
        // as an ActiveShardCount
        request.waitForActiveShards(ActiveShardCount.DEFAULT);
        // 同步请求
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        // 异步请求
//        ActionListener<CreateIndexResponse> listener =
//                new ActionListener<CreateIndexResponse>() {
//
//                    @Override
//                    public void onResponse(CreateIndexResponse createIndexResponse) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                };
//        client.indices().createAsync(request, RequestOptions.DEFAULT, listener);
        // 获取结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        client.close();
    }

    @Test
    public void updateMapping() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));

        PutMappingRequest request = new PutMappingRequest("twitter");
        // 设置mapping source
        request.source(
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        // 其他形式设置mapping 略

        // 同步请求
        AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        // 异步请求
        ActionListener<AcknowledgedResponse> listener =
                new ActionListener<AcknowledgedResponse>() {
                    @Override
                    public void onResponse(AcknowledgedResponse putMappingResponse) {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
        client.indices().putMappingAsync(request, RequestOptions.DEFAULT, listener);
        boolean acknowledged = putMappingResponse.isAcknowledged();
    }

    @Test
    public void updateDoc() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));
        // index
        // doc id
        UpdateRequest request = new UpdateRequest(
                "blog",
                "3");
        String jsonString = "{\n" +
                "  \n" +
                "    \"id\": 3,\n" +
                "    \"title\": \"新添加\",\n" +
                "    \"content\": \"新添加\"\n" +
                "  \n" +
                "}\n";
        request.doc(jsonString, XContentType.JSON);
        Map<String, Object> parameters = new HashMap<>();
//
        // 如果不存在就插入就 写一个upsert
//        request.upsert(jsonString, XContentType.JSON);
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("id", 1);
//        jsonMap.put("title", "新添加");
//        jsonMap.put("content", "新添加");

//        UpdateRequest request = new UpdateRequest("blog/article", "1")
//                .doc(jsonMap);
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.timeField("updated", new Date());
//            builder.field("reason", "daily update");
//        }
//        builder.endObject();
//        UpdateRequest request = new UpdateRequest("posts", "1")
//                .doc(builder);
//        UpdateRequest request = new UpdateRequest("posts", "1")
//                .doc("updated", new Date(),
//                        "reason", "daily update");

        // 可选参数 略

        // 同步请求
        UpdateResponse updateResponse = client.update(
                request, RequestOptions.DEFAULT);

        // 异步请求
//        ActionListener listener = new ActionListener<UpdateResponse>() {
//            @Override
//            public void onResponse(UpdateResponse updateResponse) {
//
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//
//            }
//        };
//        client.updateAsync(request, RequestOptions.DEFAULT, listener);

        // 结果
//        String index = updateResponse.getIndex();
//        String id = updateResponse.getId();
//        long version = updateResponse.getVersion();
//        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
//
//        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//
//        } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
//
//        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
//
//        }
    }

    @Test
    public void SearchTest() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchRequest searchRequest = new SearchRequest("auth_result_log_2021-06");
        sourceBuilder.fetchSource("obj", null);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.filter(QueryBuilders.rangeQuery("timeMillis").
//                from(1624291200000L).to(1624550400000L));
        boolQueryBuilder.filter(QueryBuilders.matchQuery("obj.reasonType", 2));
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(Arrays.toString(searchResponse.getHits().getHits()));
        client.close();
    }


    @Test
    public void SearchTotalTest() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.170.200", 9200, "http")));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.trackTotalHits(true);
//        sourceBuilder.fetchSource("xxx", null);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchRequest searchRequest = new SearchRequest("auth_result_log_2021-06");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("timeMillis").
                from(1624291200000L).to(1624550400000L));
//        boolQueryBuilder.filter(QueryBuilders.matchQuery("id", 3));
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse.getHits().getTotalHits().value);
        client.close();
    }
}

package excel1;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.slice.SliceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : shengbin
 * @date : 2018/08/23
 */
public class QaValidDataUtils {
    private static final Logger logger = LoggerFactory.getLogger(QaValidDataUtils.class);

    private static final Integer scrollSize = 200;
    private static final Integer sliceMax = 3;


    public static void main(String[] args) {
        // String hostName = "192.168.67.104";
        // Integer port = 9302;
        String hostName = "172.24.8.244";
        Integer port = 9302;
        try {
            // settings
            Settings settings = Settings.builder()
                    .put("cluster.name", "CL_Normal")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient transportClient = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            // Map places = EsQueryConditionUtils.getPlaces();
            // Map places = EsQueryConditionUtils.getPingjia();
            // Map places = EsQueryConditionUtils.getPingjia();
            Map places = EsQueryConditionUtils.getQingGan();
            List<String> list = (List<String>) places.getOrDefault("source", new ArrayList<>());
            String[] str = (String[]) places.getOrDefault("condition", new String[]{});
            String docType = (String) places.getOrDefault("docType", "");
            for (String source : list) {
                // String path = "/Users/wangshengbin/excel/nlpqinggan/" + source + "/";
                // String path = "/Users/wangshengbin/excel/nlpplaces/" + source + "/";
                String path = "/Users/wangshengbin/excel/nlpqinggan1/" + source + "/";
                // String path = "/Users/wangshengbin/excel/nlpqinggan/" + source + "/";
                // String path = "/Users/wangshengbin/excel/nlpspamtag/" + source + "/";
                creteFile(path);
                // matchAll
                SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("cl_index_*").setTypes("docs");
                SearchRequestBuilder searchCountQuery = getPlaceOrPingjiasQueryBuildMatchAll(searchRequestBuilder, source, str, docType);
                // SearchRequestBuilder searchCountQuery = getPingjiasQueryBuildMatchAll(searchRequestBuilder, source, str, docType);
                // SearchRequestBuilder searchCountQuery = getSpamTagQueryBuildMatchAll2(searchRequestBuilder, source, str, docType);
                // Es json
                getEsJson(searchCountQuery, transportClient, path);

                logger.info("========={}", source + " success !!!=============");
            }

            // write
            //writeEsData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void creteFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 写入txt
     *
     * @param data
     */
    private static void writeEsData(JSONObject data, String file) {
        // 写入
        File file1 = new File(file);
        try {
            FileWriter fw = new FileWriter(file1);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data.toJSONString());
            bw.flush();
            bw.close();
            fw.close();
            System.out.println("success!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * get Es params
     *
     * @param searchCountQuery
     * @param transportClient
     * @param path
     * @return
     */
    private static JSONObject getEsJson(SearchRequestBuilder searchCountQuery, TransportClient transportClient, String path) {
        Map<String, Integer> map = new HashMap<>();
        // 去重
        Map<String, JSONObject> objectMap = new HashMap<>();
        map.put("num", 0);
        for (int j = 0; j < sliceMax; j++) {
            SliceBuilder sliceBuilder = new SliceBuilder(ESConstant.PUBTIME, j, sliceMax);
            // 游标分页
            searchCountQuery.setSize(scrollSize)
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .slice(sliceBuilder);
            SearchResponse searchResponse = searchCountQuery.get();
            String scrollId = searchResponse.getScrollId();
            // 解析数据
            parseEsResponse(searchResponse, path, map, objectMap);
            while (searchResponse.getHits().getHits().length == scrollSize) {
                searchResponse = transportClient
                        .prepareSearchScroll(scrollId)
                        .setScrollId(scrollId)
                        .setScroll(new Scroll(TimeValue.timeValueMinutes(8)))
                        .get();
                parseEsResponse(searchResponse, path, map, objectMap);
                scrollId = searchResponse.getScrollId();
                logger.info("进入while，size = {}", searchResponse.getHits().getHits().length);
                if (map.get("num") >= scrollSize) {
                    break;
                }

            }
            if (map.get("num") >= scrollSize) {
                break;
            }
        }

        Integer sum = 0;
        for (String key : objectMap.keySet()) {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("data", objectMap.get(key));
            String writePath = path + sum + ".txt";
            System.out.println(writePath);
            writeEsData(jsonObject2, writePath);
            sum++;
        }
        logger.info("成功写入：{}", sum);
        return null;

    }

    private static Map<? extends String, ? extends Object> parseEsResponse(SearchResponse searchResponse, String path, Map<String, Integer> map, Map<String, JSONObject> objectMap) {
        Integer sum = map.get("num");
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String titleSimHash = (String) sourceAsMap.get("titleSimHash");
            if (objectMap.containsKey(titleSimHash)) {
                continue;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", sourceAsMap.getOrDefault("title", ""));
            jsonObject.put("url", sourceAsMap.getOrDefault("url", ""));
            String string = sourceAsMap.getOrDefault("content", "").toString();
            if (string.length() > 32767) {
                string = string.substring(0, 32767);
            }
            jsonObject.put("content", string);
            jsonObject.put("pubTimeStr", sourceAsMap.getOrDefault("pubTimeStr", ""));


            Object sysSentiment = sourceAsMap.get("sysSentiment");
            Object commentSentiment = sourceAsMap.get("commentSentiment");
            if (sysSentiment == null || commentSentiment == null) {
                continue;
            }
            jsonObject.put("sysSentiment", sysSentiment);
            jsonObject.put("commentSentiment", commentSentiment);

//            String spamTag = (String) sourceAsMap.get("spamTag");
//            if (spamTag == null) {
//                continue;
//            }
//            jsonObject.put("spamTag", spamTag);

//            List<String> placesList = (List<String>) sourceAsMap.getOrDefault("opinions", new ArrayList<>());
//            if (placesList == null || placesList.size() == 0) {
//                continue;
//            }
//            StringBuffer str = new StringBuffer();
//            for (String expressionMap : placesList) {
//                str.append(expressionMap).append(" ");
//            }
//            jsonObject.put("opinions", str);
            Object commentScore = sourceAsMap.get("commentScore");
            if (commentScore == null) {
                continue;
            }
            jsonObject.put("commentScore", commentScore);


//            List<String> placesList = (List<String>) sourceAsMap.getOrDefault("places", new ArrayList<>());
//            if (placesList == null || placesList.size() == 0) {
//                continue;
//            }
//            StringBuffer str = new StringBuffer();
//            for (String place : placesList) {
//                str.append(place).append("  ");
//            }
//            jsonObject.put("places", str);


            objectMap.put(titleSimHash, jsonObject);
            sum++;
            if (sum >= scrollSize) {
                break;
            }
        }
        map.put("num", sum);
        return null;
    }


    /**
     * matchAll query
     *
     * @param searchRequestBuilder
     * @param source
     * @param str
     * @param docType
     * @return
     */
    private static SearchRequestBuilder getSpamTagQueryBuildMatchAll(SearchRequestBuilder searchRequestBuilder, String source, String[] str, String docType) {

        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(docType, source);
        // 闲置转让
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("title", "闲置");
        MatchPhraseQueryBuilder matchPhraseQueryBuilder1 = QueryBuilders.matchPhraseQuery("title", "转让");
        TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("spamTag", "transfer");
        boolQueryBuilder1.should(matchPhraseQueryBuilder).should(matchPhraseQueryBuilder1).should(termQueryBuilder2).minimumShouldMatch(1);


        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        boolQueryBuilder2.must(termQueryBuilder1).must(boolQueryBuilder1);

        return searchRequestBuilder
                .setQuery(boolQueryBuilder2)
                .setFetchSource(str, null)
                .addSort("docId", SortOrder.ASC)
                .setSearchType(SearchType.DEFAULT);
    }


    private static SearchRequestBuilder getSpamTagQueryBuildMatchAll2(SearchRequestBuilder searchRequestBuilder, String source, String[] str, String docType) {

        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(docType, source);

        // rent ,recruitment
        TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("spamTag.keyword", "marriageSeeking");
        boolQueryBuilder1.must(termQueryBuilder2).must(termQueryBuilder1);

        return searchRequestBuilder
                .setQuery(boolQueryBuilder1)
                .setFetchSource(str, null)
                .addSort("docId", SortOrder.ASC)
                .setSearchType(SearchType.DEFAULT);
    }

    private static SearchRequestBuilder getPlaceOrPingjiasQueryBuildMatchAll(SearchRequestBuilder searchRequestBuilder, String source, String[] str, String docType) {

        return searchRequestBuilder
                .setQuery(
                        QueryBuilders.boolQuery()
                                .filter(QueryBuilders.termQuery(docType, source)))
                .setFetchSource(str, null)
                .addSort("docId", SortOrder.ASC)
                .setScroll(TimeValue.timeValueMinutes(8))
                .setSearchType(SearchType.DEFAULT);
    }


    private static SearchRequestBuilder getPingjiasQueryBuildMatchAll(SearchRequestBuilder searchRequestBuilder, String source, String[] str, String docType) {

        return searchRequestBuilder
                .setQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery(docType, source)))
                .setFetchSource(str, null)
                .addSort("docId", SortOrder.ASC)
                .setScroll(TimeValue.timeValueMinutes(8))
                .setSearchType(SearchType.DEFAULT);
    }

    @Test
    public void test() {
        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery("docType", "weibo");

        TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("spamTag", "rent");
        boolQueryBuilder1.must(termQueryBuilder2).must(termQueryBuilder1);

        logger.info("{}", boolQueryBuilder1);
    }


}


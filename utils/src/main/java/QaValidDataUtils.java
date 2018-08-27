//import com.alibaba.fastjson.JSONObject;
//import excel1.ESConstant;
//import excel1.EsQueryConditionUtils;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.Scroll;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.slice.SliceBuilder;
//import org.elasticsearch.search.sort.SortOrder;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author : shengbin
// * @date : 2018/08/23
// */
//public class QaValidDataUtils {
//    private static final Logger logger = LoggerFactory.getLogger(QaValidDataUtils.class);
//
//    private static final Integer scrollSize = 200;
//    private static final Integer sliceMax = 5;
//
//
//    public static void main(String[] args) {
//        String hostName = "172.24.8.244";
//        Integer port = 9301;
//        try {
//            // settings
//            Settings settings = Settings.builder()
//                    .put("cluster.name", "CL_Normal")
//                    .put("client.transport.sniff", true)
//                    .build();
//            // 准备client
//            TransportClient transportClient = new PreBuiltTransportClient(settings).
//                    addTransportAddress(new TransportAddress(
//                            InetAddress.getByName(hostName), port));
//            String[] indexList = new String[]{"cl_index_*"};
//            SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(indexList).setTypes("docs");
//
//            Map places = EsQueryConditionUtils.getPlaces();
//            List<String> list = (List<String>) places.getOrDefault("source", new ArrayList<>());
//            String[] str = (String[]) places.getOrDefault("condition", new String[]{});
//            String docType = (String) places.getOrDefault("docType", "");
//            for (String source : list) {
//                String path = "/Users/wangshengbin/excel/nlpplaces/" + source + "/";
//                creteFile(path);
//                // matchAll
//                SearchRequestBuilder searchCountQuery = getQueryBuildMatchAll(searchRequestBuilder, source, str, docType);
//                // Es json
//                getEsJson(searchCountQuery, transportClient, path);
//
//                logger.info("========={}", source + " success !!!=============");
//            }
//
//            // write
//            //writeEsData(jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void creteFile(String path) {
//        File f = new File(path);
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//    }
//
//    /**
//     * 写入txt
//     *
//     * @param data
//     */
//    private static void writeEsData(JSONObject data, String file) {
//        // 写入
//        File file1 = new File(file);
//        try {
//            FileWriter fw = new FileWriter(file1);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(data.toJSONString());
//            bw.flush();
//            bw.close();
//            fw.close();
//            System.out.println("success!!!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * get Es params
//     *
//     * @param searchCountQuery
//     * @param transportClient
//     * @param path
//     * @return
//     */
//    private static JSONObject getEsJson(SearchRequestBuilder searchCountQuery, TransportClient transportClient, String path) {
//        Map<String, Integer> map = new HashMap<>();
//        map.put("num", 0);
//        for (int j = 0; j < sliceMax; j++) {
//            SliceBuilder sliceBuilder = new SliceBuilder(ESConstant.PUBTIME, j, sliceMax);
//            // 游标分页
//            searchCountQuery.setSize(scrollSize)
//                    .setScroll(TimeValue.timeValueMinutes(8))
//                    .slice(sliceBuilder);
//            SearchResponse searchResponse = searchCountQuery.get();
//            String scrollId = searchResponse.getScrollId();
//            // 解析数据
//            parseEsResponse(searchResponse, path, map);
//            while (searchResponse.getHits().getHits().length == scrollSize) {
//                searchResponse = transportClient
//                        .prepareSearchScroll(scrollId)
//                        .setScrollId(scrollId)
//                        .setScroll(new Scroll(TimeValue.timeValueMinutes(8)))
//                        .get();
//                parseEsResponse(searchResponse, path, map);
//                scrollId = searchResponse.getScrollId();
//                logger.info("进入while，size = {}", searchResponse.getHits().getHits().length);
//                if (map.get("num") >= scrollSize * 2) {
//                    break;
//                }
//
//            }
//            if (map.get("num") >= scrollSize * 2) {
//                break;
//            }
//        }
//        return null;
//
//    }
//
//    private static Map<? extends String, ? extends Object> parseEsResponse(SearchResponse searchResponse, String path, Map<String, Integer> map) {
//        Integer sum = map.get("num");
//        SearchHit[] hits = searchResponse.getHits().getHits();
//        for (SearchHit searchHit : hits) {
//            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
//            JSONObject jsonObject = new JSONObject();
//            for (String key : sourceAsMap.keySet()) {
//                jsonObject.put(key, sourceAsMap.getOrDefault(key, ""));
//            }
//            writeEsData(jsonObject, path + sum + ".txt");
//            sum++;
//            if (sum >= scrollSize * 2) {
//                break;
//            }
//        }
//        map.put("num", sum);
//        return null;
//    }
//
//
//    /**
//     * matchAll query
//     *
//     * @param searchRequestBuilder
//     * @param source
//     * @param str
//     * @param docType
//     * @return
//     */
//    private static SearchRequestBuilder getQueryBuildMatchAll(SearchRequestBuilder searchRequestBuilder, String source, String[] str, String docType) {
//        return searchRequestBuilder
//                .setQuery(
//                        QueryBuilders.boolQuery()
//                                .filter(QueryBuilders.termQuery(docType, source)))
//                .setFetchSource(str, null)
//                .addSort("docId", SortOrder.ASC)
//                .setScroll(TimeValue.timeValueMinutes(8))
//                .setSearchType(SearchType.DEFAULT);
//    }
//
//
//}

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.slice.DocValuesSliceQuery;
import org.elasticsearch.search.slice.SliceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lubin
 */
public class EsScrollSliceUtils {
    private static final Logger logger = LoggerFactory.getLogger(EsScrollSliceUtils.class);

    //public static void main(String[] args) {
    @Test
    public void test() {
        String[] include = {"title", "docType", "content"};
        // String[] exclude = {"url"};
        String hostName = "172.24.5.131";
        Integer port = 9309;
        try {
            // 高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("title", 5)
                    .field("content", 3)
                    // .fragmenter("span")
                    .preTags("<em>").postTags("</em>");
            // settings
            Settings settings = Settings.builder()
                    .put("cluster.name", "MF")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch("mf_index_2017-12-03", "mf_index_2017-12-04")
                    .setTypes("docs");
            // timeRange
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(1512252000000L, true)
                    .to(1512252600000L, true);
            long sum = 0;
            long beginTime = System.currentTimeMillis();
            Integer max = 2;
            // 循环
            for (int i = 0; i < max; i++) {
                // slice
                SliceBuilder sliceBuilder = new SliceBuilder(i, max);
                SearchResponse searchResponse = searchRequestBuilder
                        .setQuery(QueryBuilders.boolQuery().
                                must(rangeQueryBuilder).
                                filter(QueryBuilders.matchAllQuery()))
                        .setSize(188)
                        .addSort("docId", SortOrder.ASC)
                        .setScroll(TimeValue.timeValueMinutes(8))
                        .setFetchSource(include, null)
                        .slice(sliceBuilder)
                        .highlighter(highlightBuilder)
                        .get();
                SearchHits hits = searchResponse.getHits();
                SearchHit[] searchHits = hits.getHits();
                for (SearchHit searchHit : searchHits) {
                    System.out.println(searchHit.getSourceAsMap().get("title"));
                }
                logger.info("第" + i + "次总数：{}", searchResponse.getHits().totalHits);
                sum += searchResponse.getHits().totalHits;
            }
            System.out.println("时间消耗:" + (System.currentTimeMillis() - beginTime));
            logger.info("搜索总数：{}", sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        String[] include = {"title", "docType", "content"};
        // String[] exclude = {"url"};
        String hostName = "172.24.5.132";
        Integer port = 9305;
        try {
            // 高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("title", 5)
                    .field("content", 3)
                    .preTags("<em>").postTags("</em>");
            // settings
            Settings settings = Settings.builder()
                    .put("cluster.name", "MF")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch("mf_index_2017-12-03", "mf_index_2017-12-04")
                    .setTypes("docs");
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // timeRange
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(1512230400000L, true)
                    .to(1512403199000L, true);
            long beginTime = System.currentTimeMillis();
            SearchResponse searchResponse = searchRequestBuilder
                    .setQuery(QueryBuilders.boolQuery().
                            must(rangeQueryBuilder).
                            filter(matchAllQueryBuilder))
                    .setSize(10000)
                    .addSort("docId", SortOrder.ASC)
                    //.setScroll(TimeValue.timeValueMinutes(5))
                    .setFetchSource(include, null)
                    .highlighter(highlightBuilder)
                    .get();

            System.out.println("时间消耗:" + (System.currentTimeMillis() - beginTime));
            logger.info("总数：{}", searchResponse.getHits().totalHits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证数据是不是会丢失
     */
    @Test
    public void test3() {
        String[] include = {"title", "docType", "source"};
        String[] exclude = {"content"};
        String hostName = "172.24.5.131";
        Integer port = 9309;
        try {
            // 高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("title", 5)
                    .field("content", 3)
                    .fragmenter("span")
                    .preTags("<em>").postTags("</em>");
            // settings
            Settings settings = Settings.builder()
                    .put("cluster.name", "MF")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            String[] indexList = new String[]{"mf_index_2017-12-14"};
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch(indexList)
                    .setTypes("docs");
            // timeRange
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(1502252000000L, true)
                    .to(1522252600000L, true);
            long sum = 0;
            long beginTime = System.currentTimeMillis();
            SearchRequestBuilder searchRequestBuilder1 = searchRequestBuilder.setQuery(QueryBuilders.boolQuery().
                    must(rangeQueryBuilder).
                    filter(QueryBuilders.matchAllQuery()))
                    .setSize(10000)
                    .addSort("docId", SortOrder.ASC)
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .setFetchSource(include, exclude);
            Integer max = 3;
            // 存放title
            List<String> titleList = new ArrayList<String>();
            // keys存放hitId
            List<String> keys = new ArrayList<String>();
            // 循环
            for (int i = 0; i < max; i++) {
                logger.info("\n------->第" + (i + 1) + "次slice开始<--------");
                // slice
                SliceBuilder sliceBuilder = new SliceBuilder("pubTime", i, max);
                SearchResponse searchResponse = searchRequestBuilder1
                        .slice(sliceBuilder)
                        .highlighter(highlightBuilder)
                        .get();
                String scrollId = searchResponse.getScrollId();
                int totalResults = searchResponse.getHits().getHits().length;
                // 切片总量
                long numSliceResults = searchResponse.getHits().totalHits;
                logger.info("\n【first totalResults ,numSliceResults is :{},{}】", totalResults, numSliceResults);

                while ( searchResponse.getHits().getHits().length> 0) {
                    searchResponse = client.prepareSearchScroll(scrollId)
                            .setScrollId(scrollId)
                            .setScroll(new Scroll(TimeValue.timeValueMinutes(8)))
                            .get();
                    scrollId = searchResponse.getScrollId();
                    totalResults += searchResponse.getHits().getHits().length;
                    logger.info("\n【while totalResults ,numSliceResults is :{},{}】", totalResults, numSliceResults);

                }
                logger.info("\n------->第" + (i + 1) + "次slice结束<--------切片结果为总数为：{}", titleList.size());
                logger.info("第" + (i + 1) + "次总数：{}", searchResponse.getHits().totalHits);
                logger.info("\nkeyId is:{}\n,size is:{}", keys, keys.size());
                sum += searchResponse.getHits().totalHits;
                keys.clear();
                titleList.clear();
            }
            logger.info("时间消耗:" + (System.currentTimeMillis() - beginTime));
            logger.info("搜索总数：{}", sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {

    }
}

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
import java.util.Map;

/**
 * @author Lubin
 */
public class EsScrollSliceUtils {
    private static final Logger logger = LoggerFactory.getLogger(EsScrollSliceUtils.class);

    public static void main(String[] args) {
        String[] include = {"title", "docType", "content"};
        // String[] exclude = {"url"};
        String hostName = "172.24.5.132";
        Integer port = 9305;
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
                    .from(1512230400000L, true)
                    .to(1512403199000L, true);
            long sum = 0;
            long beginTime = System.currentTimeMillis();
            Integer max = 3;
            // 循环
            for (int i = 0; i < max; i++) {
                // slice
                SliceBuilder sliceBuilder = new SliceBuilder(i, max);
                SearchResponse searchResponse = searchRequestBuilder
                        .setQuery(QueryBuilders.boolQuery().
                                must(rangeQueryBuilder).
                                filter(QueryBuilders.matchAllQuery()))
                        .setSize(10000)
                        .addSort("docId", SortOrder.ASC)
                        .setScroll(TimeValue.timeValueMinutes(8))
                        .setFetchSource(include, null)
                        .slice(sliceBuilder)
                        .highlighter(highlightBuilder)
                        .get();
                //SearchHits hits = searchResponse.getHits();
                //SearchHit[] searchHits = hits.getHits();
                //for (SearchHit searchHit : searchHits) {
                //Map<String, HighlightField> map = searchHit.getHighlightFields();
                //System.out.println(searchHit.getSourceAsMap().get("title"));
                //}
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
}

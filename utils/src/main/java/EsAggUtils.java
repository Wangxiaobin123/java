import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.slice.SliceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Map;

/**
 * @author LubinKivi
 */
public class EsAggUtils {
    private static final Logger logger = LoggerFactory.getLogger(EsAggUtils.class);

    // public static void main(String[] args) {
    @Test
    public void test() {
        String hostName = "172.24.8.244";
        Integer port = 9301;
        try {
            long startTime = 1512294313696L;
            long endTime = 1512398713000L;
            // time
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(startTime, true)
                    .to(endTime, true);
            Settings settings = Settings.builder()
                    .put("cluster.name", "CL_Normal")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch("cl_index_*")
                    .setTypes("docs");

            for (int i = 0; i < 3; i++) {
                SliceBuilder sliceBuilder = new SliceBuilder(i, 3);
                SearchResponse searchResponse = searchRequestBuilder
                        .setQuery(QueryBuilders.boolQuery().must(rangeQueryBuilder))
                        .setSize(3)
                        .setScroll(TimeValue.timeValueMinutes(5))
                        .slice(sliceBuilder)
                        .addAggregation(
                                AggregationBuilders.terms("by_docType")
                                        .field("url")
                                        //AggregationBuilders.histogram("by_pubTime")
                                        //      .interval(1000 * 60 * 60 * 24)
                                        //.timeZone(DateTimeZone.forID("+08:00"))
//                                AggregationBuilders
//                                        .dateHistogram("by_pubTime")
                                        // .field("pubTime")
                                        //.dateHistogramInterval(DateHistogramInterval.DAY)
                                        .minDocCount(0L)
                                //.timeZone(DateTimeZone.forID("+08:00"))
                        )
                        .get();
                Terms terms = searchResponse.getAggregations().get("by_pubTime" + i);
                logger.info("返回桶总数：{}", terms.getBuckets().get(0).getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

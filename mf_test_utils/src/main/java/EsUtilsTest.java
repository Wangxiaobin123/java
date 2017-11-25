import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.NetworkModule;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.join.ParentJoinPlugin;
import org.elasticsearch.percolator.PercolatorPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.mustache.MustachePlugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.Netty4Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ES 6.0 Transport client test
 *
 * @author shengbin
 */
public class EsUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(EsUtilsTest.class);

    public static void main(String[] args) {
        //9300端口是tcp
        try {
            long startTime = 1507518000000L;
            long endTime = 1511539200000L;
            TransportClient client = new PreBuiltTransportClient(clientSettings()).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName("172.24.5.132"), 9305));
            while (startTime <= endTime) {
                long t1 = startTime;
                startTime = startTime + 1000 * 60 * 60;
                SearchResponse response = client.prepareSearch("bfd_mf")
                        .setTypes("logs")
                        .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                        .setScroll(new TimeValue(120000))
                        .setSize(50)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(conditionQuery(t1, startTime))
                        .setExplain(true)
                        .get();
                logger.info("response return totalNum:{},startTime:{}", response.getHits().totalHits,t1);
                //批量写入
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                do {
                    long start = System.currentTimeMillis();
                    for (SearchHit hit : response.getHits().getHits()) {
                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        bulkRequest.add(client.prepareIndex("bfd_mf_v1", "doc", hit.getId())
                                .setSource(sourceAsMap));
                        BulkResponse bulkItemResponses = bulkRequest.get();
                        if (bulkItemResponses.hasFailures()) {
                            logger.warn("写入ES失败：{}", sourceAsMap);
                        }
                    }
                    long end = System.currentTimeMillis();
                    logger.info("成功写入ES总计时：{},总量为：{}",
                            (double) (end - start) / (1000 * 60), response.getHits().getHits().length);
                    response = client.prepareSearchScroll(response.getScrollId())
                            .setScroll(new TimeValue(120000)).execute().actionGet();
                } while (response.getHits().getHits().length != 0);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return settings
     */
    private static Settings clientSettings() {
        return Settings.builder()
                .put("cluster.name", "MF")
                .put("client.transport.sniff", true)
                .build();
    }

    /**
     * @param starTime startTime
     * @param endTime  endTime
     * @return 各种条件 query
     */
    private static BoolQueryBuilder conditionQuery(long starTime, long endTime) {
        return QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchAllQuery())
                .must(rangeQuery(starTime, endTime));
    }

    /**
     * @param startTime startTime
     * @param endTime
     * @return 时间查询 query
     */
    private static RangeQueryBuilder rangeQuery(long startTime, long endTime) {
        return QueryBuilders.rangeQuery("pubTime")
                .from(startTime, true)
                .to(endTime, true);
    }

    @Test
    public void testPluginInstalled() {
        try {
            logger.info("start");
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
            Settings settings = client.settings();
            assertEquals(Netty4Plugin.NETTY_TRANSPORT_NAME,
                    NetworkModule.HTTP_DEFAULT_TYPE_SETTING.get(settings));
            assertEquals(Netty4Plugin.NETTY_TRANSPORT_NAME,
                    NetworkModule.TRANSPORT_DEFAULT_TYPE_SETTING.get(settings));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstallPluginTwice() {
        for (Class<? extends Plugin> plugin :
                Arrays.asList(ParentJoinPlugin.class, ReindexPlugin.class, PercolatorPlugin.class, MustachePlugin.class)) {
            try {
                new PreBuiltTransportClient(Settings.EMPTY, plugin);
                fail("exception expected");
            } catch (IllegalArgumentException ex) {
                assertTrue("Expected message to start with [plugin already exists: ] but was instead [" + ex.getMessage() + "]",
                        ex.getMessage().startsWith("plugin already exists: "));
            }
        }
    }

}

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author shengbin
 */
public class ThreadUtils implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 结束时间
     */
    private long endTime;

    /**
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public ThreadUtils(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        //9300端口是tcp
        try {
            TransportClient client = new PreBuiltTransportClient(clientSettings()).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName("172.24.5.132"), 9305));
            SearchResponse response = client.prepareSearch("bfd_mf")
                    .setTypes("logs")
                    .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                    .setScroll(new TimeValue(60000))
                    .setSize(100)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(conditionQuery())
                    .setExplain(true)
                    .get();
            logger.info("query:{}", client.prepareSearch("bfd_mf")
                    .setTypes("logs")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(conditionQuery()));
            logger.info("response return totalNum:{}", response.getHits().totalHits);
            //批量写入
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            do {
                long startTime = System.currentTimeMillis();
                for (SearchHit hit : response.getHits().getHits()) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    bulkRequest.add(client.prepareIndex("bfd_mf_v1", "doc", hit.getId())
                            .setSource(sourceAsMap));
                    BulkResponse bulkItemResponses = bulkRequest.get();
                    if (bulkItemResponses.hasFailures()) {
                        logger.warn("写入ES失败：{}", sourceAsMap);
                    }
                }
                long endTime = System.currentTimeMillis();
                logger.info("成功写入ES总计时：{},总量为：{}",
                        (double) (endTime - startTime) / (1000 * 60), response.getHits().getHits().length);
                response = client.prepareSearchScroll(response.getScrollId())
                        .setScroll(new TimeValue(60000)).execute().actionGet();
            } while (response.getHits().getHits().length != 0);
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
     * @return 各种条件 query
     */
    private BoolQueryBuilder conditionQuery() {
        return QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchAllQuery())
                .must(rangeQuery());
    }

    /**
     * @return 时间查询 query
     */
    private RangeQueryBuilder rangeQuery() {
        return QueryBuilders.rangeQuery("pubTime")
                .from(startTime, true)
                .to(endTime, true);
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

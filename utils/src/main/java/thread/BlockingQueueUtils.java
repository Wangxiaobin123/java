package thread;

import com.oracle.jrockit.jfr.Producer;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.slice.SliceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Lubin
 */
public class BlockingQueueUtils {
    private static final Logger logger = LoggerFactory.getLogger(BlockingQueueUtils.class);

    public static void main(String[] args) throws InterruptedException {
        String[] include = {"title", "docType"};
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
            String[] indexList = new String[]{"mf_index_2017-12-01", "mf_index_2017-12-04"};
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch(indexList)
                    .setTypes("docs");
            // timeRange
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(1512057600000L, true)
                    .to(1512252600000L, true);
            long sum = 0;
            long beginTime = System.currentTimeMillis();
            Integer max = 3;
            // 存放title
            // keys存放hitId
            final List<String> keys = new ArrayList<String>();
            SearchRequestBuilder searchRequestBuilder1 = searchRequestBuilder.setQuery(QueryBuilders.boolQuery().
                    must(rangeQueryBuilder).
                    filter(QueryBuilders.matchAllQuery()))
                    .setSize(10000)
                    .addSort("docId", SortOrder.ASC)
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .setFetchSource(include, exclude)
                    .highlighter(highlightBuilder);
            // 借助Executors
            ExecutorService service = Executors.newFixedThreadPool(3);
            // 启动线程
            //service.submit(new ThreadUtils());
            // 执行10s
            //Thread.sleep(10 * 1000);
            final List<String> titleList = new ArrayList<String>();
            // 循环
            for (int i = 0; i < 3; i++) {
                final int sliceMax = 3;
                service.execute(new ThreadUtils(i, sliceMax, searchRequestBuilder1, keys, titleList));
            }
            //System.out.println(service.);
            // 退出Executor
            service.shutdown();
            if (service.isShutdown()) {
                logger.info("时间消耗:" + (System.currentTimeMillis() - beginTime));
                logger.info("搜索总数：{}", titleList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

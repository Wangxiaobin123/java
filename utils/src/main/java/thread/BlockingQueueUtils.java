package thread;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
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

   // public static void main(String[] args) throws InterruptedException {
   @Test
   public void test() {
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
                    .put("cluster.name", "MF_Mini")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            String[] indexList = new String[]{"mf_index_2018-01-03"};
            SearchRequestBuilder searchRequestBuilder = client
                    .prepareSearch(indexList)
                    .setTypes("docs");
            // timeRange
            // timeRange
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("pubTime")
                    .from(1502252000000L, true)
                    .to(1522252600000L, true);
            long sum = 0;
            long beginTime = System.currentTimeMillis();
            Integer max = 3;
            // 存放title
            // keys存放hitId
            List<String> keys = new CopyOnWriteArrayList<>();
            SearchRequestBuilder searchRequestBuilder1 = searchRequestBuilder.setQuery(QueryBuilders.boolQuery().
                    must(rangeQueryBuilder).
                    filter(QueryBuilders.matchAllQuery()))
                    .setSize(10000)
                    .addSort("docId", SortOrder.ASC)
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .setFetchSource(include, exclude)
                    .highlighter(highlightBuilder);
            ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils("test");
            // 借助Executors
            //ThreadPoolExecutor pool = new ThreadPoolExecutor(1,5,50,TimeUnit.MINUTES);
            ExecutorService service = Executors.newFixedThreadPool(3);
            final List<String> titleList = new ArrayList<>();
            // 循环
            for (int i = 0; i < 3; i++) {
                //final int sliceMax = 3;
                service.execute(threadPoolUtils.newThread(
                        new ThreadUtils("test",i, 3, searchRequestBuilder1, keys, titleList)));
            }
            // 退出Executor,不再接受新的线程进来
            service.shutdown();
            while (true) {
                if (service.isTerminated()) {
                    System.out.println("所有的子线程都结束了");
                    break;
                }
            }
            System.out.println(threadPoolUtils.getStas());
            long endTime = System.currentTimeMillis();
            logger.info("时间消耗:{}", endTime - beginTime);
            logger.info("搜索总数：{}", titleList.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程结束");
        }
    }

}

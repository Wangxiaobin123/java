package thread;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.slice.SliceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Lubin
 */
public class ThreadUtils implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    private String threadName;
    private int threadId;
    private Integer sliceMax;
    private SearchRequestBuilder searchRequestBuilder1;
    private List<String> keys;
    private final List<String> titleList;
    private static TransportClient client;
    private Long sum = 0L;

    public ThreadUtils(String threadName, int i, Integer max, SearchRequestBuilder searchRequestBuilder1,
                       List<String> keys, List<String> titleList) {
        this.threadName = threadName;
        this.threadId = i;
        this.sliceMax = max;
        this.searchRequestBuilder1 = searchRequestBuilder1;
        this.keys = keys;
        this.titleList = titleList;
        client = getClient();
    }

    @Override
    public void run() {
        // slice
        SliceBuilder sliceBuilder = new SliceBuilder("pubTime", threadId, sliceMax);
        SearchResponse searchResponse = searchRequestBuilder1
                .slice(sliceBuilder)
                .get();
        String scrollId = searchResponse.getScrollId();
        int totalResults = searchResponse.getHits().getHits().length;
        // 切片总量
        long numSliceResults = searchResponse.getHits().totalHits;
        logger.info("\n【first totalResults , is :{}】", totalResults);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            keys.add(hit.getId());
            synchronized (titleList) {
                titleList.add((String) hit.getSourceAsMap().get("title"));
            }
        }
        logger.info("\n========" + Thread.currentThread().getName()+threadName + "+,keyId is:{}\n,size is:{}=========", keys, keys.size());
        keys.clear();
        while (searchResponse.getHits().getHits().length >= 60) {
            searchResponse = client.prepareSearchScroll(scrollId)
                    .setScrollId(scrollId)
                    .setScroll(new Scroll(TimeValue.timeValueMinutes(8)))
                    .get();
            scrollId = searchResponse.getScrollId();
            totalResults += searchResponse.getHits().getHits().length;
            logger.info("\n【while totalResults ,numSliceResults is :{},{}】", totalResults, numSliceResults);

            for (SearchHit hit : searchResponse.getHits().getHits()) {
                keys.add(hit.getId());
                synchronized (titleList) {
                    titleList.add((String) hit.getSourceAsMap().get("title"));
                }
            }
            logger.info("\n========" + Thread.currentThread().getName() + ",keyId is:{}\n,size is:{}=========", keys.toString(), keys.size());
            keys.clear();
        }
        logger.info("\n------->第" + (threadName + 1) + "次slice结束<--------切片结果为总数为：{}", titleList.size());
        logger.info("第" + (threadName + 1) + "次总数：{}", searchResponse.getHits().totalHits);
        System.out.println("====" + threadName + "====" + titleList.size());
        sum += searchResponse.getHits().totalHits;
        //titleList.clear();
    }

    private TransportClient getClient() {
        try {
            Settings mf = Settings.builder()
                    .put("cluster.name", "MF_Mini")
                    .put("client.transport.sniff", true)
                    .build();
            return new PreBuiltTransportClient(mf).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName("172.24.5.131"), 9309));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}

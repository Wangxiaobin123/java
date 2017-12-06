import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * {
 * "query": {
 * "match_phrase": {
 * "title":"大数据"
 * }
 * },
 * "highlight": {
 * "fields": {
 * "title": {
 * "type":"plain",
 * "fragment_size":15,
 * "number_of_fragments":3,
 * "fragmenter":"span" //指定文本应该如何在高亮片段中分解：
 * //simple或span。只适用于类型为plain的高亮。默认为span
 * //simple:将文本分成大小相同的片段；span:将文本分成大小相同片段，
 * //但是避免了高亮文本拆解
 * }
 * }
 * }
 * }
 * elasticsearch 6 for highlight
 *
 * @author LubinKivi
 */
public class ESHighlightUtils {
    private static final Logger logger = LoggerFactory.getLogger(EsUtilsTest.class);

    public static void main(String[] args) {
        String[] include = {"title", "docType"};
        String[] exclude = {"url"};
        String hostName = "172.24.5.132";
        Integer port = 9305;
        try {
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("title", 5)
                    .field("content", 3)
                   // .fragmenter("span")
                    .preTags("<em>").postTags("</em>");

            Settings settings = Settings.builder()
                    .put("cluster.name", "MF")
                    .put("client.transport.sniff", true)
                    .build();
            // 准备client
            TransportClient client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new TransportAddress(
                            InetAddress.getByName(hostName), port));
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch("bfd_mf_v1")
                    .setTypes("doc");
            SearchResponse searchResponse = searchRequestBuilder
                    .setQuery(QueryBuilders.boolQuery().
                            filter(QueryBuilders.matchPhraseQuery("title", "大数据")).
                            filter(QueryBuilders.matchPhraseQuery("content", "大数据")))
                    .setSize(3)
                    .addSort("docId", SortOrder.ASC)
                    .setFetchSource(include, null)
                    .highlighter(highlightBuilder)
                    .get();
            logger.info("总数：{}", searchResponse.getHits().totalHits);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit searchHit : searchHits) {
                Map<String, HighlightField> map = searchHit.getHighlightFields();
                System.out.println(map.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package excel1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * query condition for ES fetchSource
 *
 * @author shengbin
 */
public class EsQueryConditionUtils {

    /**
     * fetchSource includeField
     *
     * @param includeField 包含field
     * @return includeField[]
     */
    public static String[] getIncludeQueryField(String... includeField) {
        String[] includeFields = new String[includeField.length];
        System.arraycopy(includeField, 0, includeFields, 0, includeField.length - 1 + 1);
        return includeFields;
    }

    /**
     * fetchSource excludeField
     *
     * @param excludeField 不包含Field
     * @return excludeField[]
     */
    public static String[] getExcludeQueryField(String... excludeField) {
        String[] excludeFields = new String[excludeField.length];
        System.arraycopy(excludeField, 0, excludeFields, 0, excludeField.length - 1 + 1);
        return excludeFields;
    }


    static Map getEnSource() {

        Map map = new HashMap();

        List<String> list = new ArrayList<>();
        list.add("suning");
        list.add("beibeiwang");
        list.add("dangdang");
        list.add("ejingdong");
        list.add("eymx");
        list.add("jumeiapp");
        list.add("meilishuo");
        list.add("miya");
        list.add("mogujie");
        list.add("sephora");
        list.add("taobao");
        list.add("tmall");
        list.add("wangyikaola");
        list.add("weipinhui");

        String[] str = new String[]{
                "opinions",
                "crawlTimeStr",
                "titleLength",
                "pubTime",
                "crawlDate",
                "enSource",
                "pubDate",
                "author",
                "createDate",
                "sysSentimentTag",
                "titleSimHash",
                "content",
                "source",
                "channel",
                "crawlTime",
                "originalContentSimHash",
                "docId",
                "originalTitleSimHash",
                "sysSentiment",
                "docType",
                "sysKeywords",
                "createDay",
                "crawlDay",
                "lastModifiedTime",
                "createTimeStr",
                "spamTag",
                "commentScore",
                "places",
                "language",
                "pubDay",
                "url",
                "pubTimeStr",
                "createTime",
                "title",
                "avatar",
                "sysAbstract",
                "urlHash",
                "contentTag",
                "hlKeywords"
        };

        map.put("source",list);
        map.put("condition",str);
        map.put("docType","enSource");
        return map;
    }


    public static Map getPlaces() {

        Map map = new HashMap();

        List<String> list = new ArrayList<>();
        list.add("news");
        list.add("app");
        list.add("weibo");
        list.add("weixin");
        list.add("media");
        list.add("wenda");
        list.add("tieba");
        list.add("bbs");
        list.add("blog");
        list.add("item");
        list.add("video");

        String[] str = new String[]{
                "title",
                "url",
                "content",
                "pubTimeStr",
                "places",
                "titleSimHash"
        };
        map.put("source",list);
        map.put("condition",str);
        map.put("docType","docType");

        return map;
    }


    static Map getSpamTag() {

        Map map = new HashMap();

        List<String> list = new ArrayList<>();
        list.add("news");
        list.add("app");
        list.add("weibo");
        list.add("weixin");
        list.add("media");
        list.add("wenda");
        list.add("tieba");
        list.add("bbs");
        list.add("blog");
        list.add("item");
        list.add("video");

        String[] str = new String[]{
                "title",
                "url",
                "content",
                "spamTag",
                "pubTimeStr",
                "titleSimHash"
        };

        map.put("source",list);
        map.put("condition",str);
        map.put("docType","docType");

        return map;
    }


    public static Map getPingjia() {

        Map map = new HashMap();

        List<String> list = new ArrayList<>();

        list.add("weibo");

        list.add("item");

        String[] str = new String[]{
                "title",
                "url",
                "content",
                "pubTimeStr",
                "expression",
                "titleSimHash"
        };
        map.put("source",list);
        map.put("condition",str);
        map.put("docType","docType");

        return map;
    }


    public static Map getQingGan() {

        Map map = new HashMap();

        List<String> list = new ArrayList<>();

        list.add("news");
        list.add("app");
        list.add("weibo");
        list.add("weixin");
        list.add("media");
        list.add("wenda");
        list.add("tieba");
        list.add("bbs");
        list.add("blog");
        list.add("item");
        list.add("video");

        String[] str = new String[]{
                "title",
                "url",
                "content",
                "pubTimeStr",
                "commentSentiment",
                "sysSentiment",
                "titleSimHash"
        };
        map.put("source",list);
        map.put("condition",str);
        map.put("docType","docType");

        return map;
    }
}
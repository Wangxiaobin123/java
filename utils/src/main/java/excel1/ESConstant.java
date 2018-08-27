package excel1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESConstant {

    public static final String COMPANY_ID = "companyId";
    public static final String INDUSTRY_ID = "industryId";

    public static final String SPAM_TAG = "spamTag";

    public static final String MEDIA_AREA_KEY = "dict";


    public static final float BOOST_WEIGHT = 10f;
    // 相似查询返回的最大数据量
    public static final Integer MAX_RESULT_WINDOWS_SIZE = 10000;
    public static final Integer MAX_SCROLL_SIZE = 5000;
    public static final Long MAX_SEARCH_TIME_MINUTES = 2L;
    public static final String SUBJECT_CLUSTER_INDEX_PREFIX = "mf_subject";
    public static final String ES_KEY = "es";
    public static final String ES_NORMAL_KEY = "es_normal";
    public static final String CACHE_KEY = "cache";
    public static final int BULK_SIZE = 100;
    public static final String INDEX_TYPE = "docs";
    public static final String bigramPostfix = ".shingles";
    public static final String bigramKey = "bigram";
    public static final String filterTypeKey = "filter_type";
    public static final String queryTypeKey = "query_type";
    public static final Double aggOffset = 16 * 6
            * 60 * 1000d;
    public static final Double ONE_DAY = 24 * 60 * 60 * 1000d;
    public static final Double ONE_HOUR = 60 * 60 * 1000d;

    /**
     * *
     *
     * @FIXED 2017.11.13 increase the runtime,from 3 min to 6 min
     */
    public static final long THREAD_TIME = 15 * 60 * 1000L;

    public static String DESC = "desc";

    public static String ASC = "asc";

    /**
     * pubTime时间由远及近
     */
    public static String PUBTIME_ASC_SORT_FLAG = "timeAsc";


    /**
     * pubTime时间由近及远
     */
    public static String PUBTIME_DESC_SORT_FLAG = "timeDesc";

    /**
     * createTime时间由近及远
     */
    public static String CREATE_TIME_DESC_SORT_FLAG = "createdTimeDesc";

    /**
     * createTime时间由远及近
     */
    public static String CREATE_TIME_ASC_SORT_FLAG = "createdTimeAsc";

    /**
     * 重要度排序
     */
    public static String SCORE_DESC_SORT_FLAG = "scoreDesc";

    /**
     * Alexa
     */
    public static String ALEXA_ASC_SORT_FLAG = "alexaAsc";
    /***
     *
     */
    public static Float ALEXA_DEFAULT_SCORE = 1000000.00F;
    /**
     * 转载量排序
     */
    public static String FOLDER_NUMBER_DESC_SORT_FLAG = "folderNumberDescSortFlag";
    /**
     * 综合排序：1:转载量排序;2:转载量一样按照得分排序;3:随后按照pubTime时间倒序
     */
    public static String REPORT_SORT_FLAG = "reportSortFlag";

    public static String SCORE = "_score";
    public static String SCORE_FROM_SOURCE = "score";


    /**
     * 关键词前缀
     */
    public static final String TAG_START = "<em>";
    /**
     * 关键词后缀
     */
    public static final String TAG_END = "</em>";

    /**
     * 标题关键词分隔符
     */
    public static final String TITLE_KEYWORDS_SPLIT_SYSBOL = ",";
    /**
     * 相关度等级词分隔符
     */
    public static final String RELATEDWORDS_HIGH = "high";
    public static final String RELATEDWORDS_MEDIUM = "medium";
    public static final String RELATEDWORDS_LOW = "low";
    /**
     * 相关度分级
     */
    public static final float RELATEDRATE_MIN = 0.00F;
    public static final float RELATEDRATE_MAX = 1.00F;
    public static final int RELATEDRATE_MIN_SEARCH_NUMBER = 10;
    /**
     * 排除关键词分隔符
     */
    public static final String EXCLUDE_KEYWORDS_SPLIT_SYSBOL = ",";
    public static final String EXCLUDE_KEYWORDS_SPLIT_CN_SYSBOL = "，";


    /**
     * 最小匹配度
     */
    public static final Integer MINI_NUMBER_SHOULD_MATCH = 1;
    /**
     * 最大摘要长度
     */
    public static final Integer MAX_FRAGMENT_SIZE = 10000;
    /**
     * 数据监控页面摘要长度
     */
    public static final Integer MONITOR_FRAGMENT_SIZE = 130;
    // 报告摘要长度
    public static final Integer Report_FRAGMENT_SIZE = 100;
    public static final Integer Foreign_Report_FRAGMENT_SIZE = 200;
    /**
     * 数据监控页面标题高亮长度
     */
    public static final Integer MONITOR_FRAGMENT_TITLE_SIZE = 50;
    /**
     * 数据导出查询大集群分页查询，每次最大查询量
     */
    public static final Integer EXPORT_SCROLL_SIZE = 1000;
    /**
     * 自定义长度
     */
    // title 长度
    public static final Integer MONITOR_FRAGMENT_USER_DEFINED_TITLE_SIZE = 30;
    // 系统报告自定义长度 40
    public static final Integer SYS_REPORT_FRAGMENT_USER_DEFINED_TITLE_SIZE = 40;
    public static final Integer FOREIGN_SYS_REPORT_FRAGMENT_USER_DEFINED_TITLE_SIZE = 80;

    public static final Integer MAX_FRAGMENT_USER_DEFINED_TITLE_SIZE = 50;

    // 预警报告的摘要长度
    public static final Integer MONITOR_FRAGMENT_USER_DEFINED_DIGEST_SIZE = 150;
    public static final String DEFAULT_JOIN_SYMBOL = ".";
    /**
     * 主体情感默认名称
     */
    public static final String DEFAULT_ENTRY_NAME = "default";
    public static final String EMOTION_ENTRY = "emotionEntry";
    public static final String EMOTION_VALUE = "emotionValue";
    public static final String EMOTION_NAME = "entryName";
    public static final String OFF_SET = "offSet";
    public static final String MINIMUM_SHOULD_MATCH = "1";
    /*
     * docType(news\bbs...):int 类型常量
     */
    public static final int DOCTYPENEWS = 0;
    public static final int DOCTYPEBBS = 1;
    /*
     * 组合查询语句key值类型
     */
    public static final String KEYTYPEOPERA = "operator";
    public static final String KEYTYPEWORD = "word";
    /*
     * 数据来源渠道 (hl \ bfd)
     */
    public static final String CHANNELHL = "HL";
    public static final String CHANNELBFD = "BFD";
    /*
     *1:type:content、title、all
     *2: text:words(目前要是关键词列表)
     */
    public static final String SEARCH_SCOPE_TYPE_TITLE = "title";

    public static final String SEARCH_SCOPE_TYPE_CONTENT = "content";

    //标题+正文
    public static final String SEARCH_SCOPE_TYPE_TITLE_ALL = "title_all";

    public static final String SEARCH_SCOPE_TYPE_AUTHOR = "author";

    public static final String SEARCH_SCOPE_TYPE_FORWARD = "forwardContent";

    public static final String SEARCH_SCOPE_TYPE_TITLE_ORIGIN = "title_origin";

    public static final String SEARCH_SCOPE_TYPE_CONTENT_ORIGIN = "content_origin";

    public static final String SEARCH_SCOPE_TYPE_ALL = "all";
    /**
     * 关键词
     */
    public static final String SEARCH_SCOPE_TYPE_KEYWORDS = "keyWords";

    public static final String REL_TYPE = "relType";

    /**
     * 回溯开始时间
     */
    public static final String CON_START_TIME = "conStartTime";
    /**
     * 回溯结束时间
     */
    public static final String CON_END_TIME = "conEndTime";
    public static final String SUBJECT_ID = "subjectId";
    /*
     *1:非敏感 positive
     *2:敏感 negative
     */
    public static final String COMMON_TAG = "common";
    public static final String NEGATIVE_TAG = "negative";
    public static final String NEUTER_TAG = "neuter";

    public static final String COMMON_CN_TAG = "正面";
    public static final String NEGATIVE_CN_TAG = "负面";
    public static final String NEUTER_CN_TAG = "中性";

    public static final List<String> SENTIMENT_LIST = new ArrayList<String>();
    public static String URL_HASH = "urlHash";

    static {
        SENTIMENT_LIST.add(COMMON_TAG);
        SENTIMENT_LIST.add(NEGATIVE_TAG);
        SENTIMENT_LIST.add(NEUTER_TAG);
    }

    // 正面 [0.8-1]
    // 中性（0.2-0.8)
    // 负面 [0-0.2]
    public static final Double SENTIMENTAL_MID = 0.8;
    public static final Double SENTIMENTAL_THRESHOLD = 0.2;
    public static final Double SENTIMENTAL_MIN = 0.0;
    public static final Double SENTIMENTAL_MAX = 1.0;
    public static final String OPERATOR_OR = "or";
    public static final String OPERATOR_EQUAL = "=";
    public static final String OPERATOR_SPACE = " ";
    public static final String OPERATOR_ALL = "*:*";
    public static final String OPERATOR_CONNENT_ALL = ":";
    public static final String OPERATOR_SINGLE_ALL = "*";


    /*
     *define es mapping fields
     */
    public static String PUBTIME = "pubTime";
    public static String CHANNEL_HYLANDA = "hylanda";

    public static String PUBTIME_STR = "pubTimeStr";

    public static String CREATETIME = "createTime";
    public static String PUTTIME = "putTime";
    public static String CRAWLTIME = "crawlTime";
    public static String ORIGINAL_SOURCE = "originalSource";
    public static String CONTENT_SIMHASH = "contentSimHash";
    public static String QUOTE_COUNT = "quoteCount";
    /**
     * 内容
     */
    public static String CONTENT_TAG = "contentTag";
    public static String COMMENTS_COUNT = "commentsCount";
    public static String ATTITUDES_COUNT = "attitudesCount";
    public static String TITLE_SIMHASH = "titleSimHash";
    public static String ORIGINAL_CONTENT_SIMHASH = "originalContentSimHash";
    public static String ORIGINAL_TITLE_SIMHASH = "originalTitleSimHash";
    public static String DOC_TYPE = "docType";
    public static String SYS_SENTIMENT_TAG = "sysSentimentTag";
    public static final String TITLE = "title";
    public static String SOURCE = "source";
    public static String CHANNEL = "channel";
    public static final String CONTENT = "content";
    public static final String SYS_SENTIMENT = "sysSentiment";
    public static String URL = "url";
    public static String DOC_ID = "docId";
    public static String SYS_ABSTRACT = "sysAbstract";
    public static String SYS_KEYWORDS = "sysKeywords";
    // 所有热词
    public static String HL_KEYWORDS = "hlKeywords";
    // 地名：places
    public static String PLACES = "places";
    // 微博话题：hashTag
    public static String HASH_TAG = "hashTag";
    public static String PICTURE_LIST = "pictureList";

    // 表情：expression
    public static String EXPRESSION = "expression";
    // 评价：opinions
    public static String OPINIONS = "opinions";

    public static String WEIBO_AUTHOR = "author";
    public static String WEIBO_USER_ID = "userId";
    public static String AUTHOR = "author";
    public static String WEIXIN_NAME = "weixinName";
    public static final String CHANNEL_WEIXIN = "weixin";
    public static final String HL_DOCID = "hylandaDocId";
    public static final String LANGUAGE = "language";


    /**
     * _all字段
     */
    public static String _ALL = "_all";

    /**
     * 口碑
     */
    public static final String SHOP_MD5 = "shopUrlHash";
    public static final String HAS_PICTURE = "isCommentImg";
    public static final String HAS_CONTENT = "isComment";
    public static final String CUSTOMER_LEVEL = "customerLevel";
    public static final String COMMENT_STAR_LEVEL = "starLevel";
    public static final String SHOP_NAME = "shopName";
    public static final String NICKNAME = "nickName";
    public static final String COMMENT_SEARCH_ALL = "comment_all";
    public static final String USER_AVATAR = "userAvatar";
    public static final String UNIQUE_ID = "uniqueId";
    public static final String SHOP_URL = "shopUrl";
    public static final String USER_URL = "userUrl";
    public static final String CONTRIBUTION = "contribution";
    public static final String SCORE_FACTOR = "scoreFactor";
    public static final String FACTOR_KEY = "factorKey";
    public static final String FACTOR_VALUE = "factorValue";
    public static final String CONTENT_SIZE = "commentLength";
    public static final String SHOP_LABEL = "label";
    public static final String SHOP_CITY = "shopCity";
    public static final String SHOP_PROVINCE = "shopProvince";
    public static final String USER_CITY = "userCity";
    public static final String COUNTRY = "country";
    public static final String USER_PROVINCE = "userProvince";
    public static final String SHOP_LABELS = "shopLabels";
    public static final String COMMENT_IMG = "commentImg";

    /**
     * 用户头像
     */
    public static String WEIBO_AVATOR = "avatar";
    public static final String BBS = "bbs";//论坛
    public static final String WEI_BO = "weibo";//微博
    public static final String WEI_XIN = "weixin";//微信
    public static final String BLOG = "blog";//博客
    public static final String NEWS = "news";//网站
    public static final String TIEBA = "tieba";//贴吧
    public static final String MEDIA = "media";//报刊
    public static final String VIDEO = "video";//视频
    public static final String WENDA = "wenda";
    public static final String APP = "app";
    // 电商
    public static final String ITEM = "item";
    public static final String ZIXUN = "zixun";
    public static final String FOREIGN_NEWS = "foreign_news";
    public static final String FOREIGN_MEDIA = "foreign_media";
    //电商星级分数
    public static final String COMMENT_SCORE = "commentScore";
    //电商数据没有星级时返回-1
    public static final Integer COMMENT_SCORE_NULL = -1;
    public static final String EN_SOURCE = "enSource";

    //内容识别
    public static final String NORMAL = "normal";
    public static final String MARKETING = "marketing";

    public static final String AVATAR = "avatar";
    public static final String COMMENT_ID = "commentId";


    /**
     * 微博信息 added by Eric 2016-10-28 15:03:31
     */
    static String WEIBO_USER_SEX = "sex";// 性别 female \ male
    public static String WEIBO_USER_SEX_FEMALE = "female";// 性别 female
    public static String WEIBO_USER_SEX_MALE = "male";

    static String WEIBO_USER_FRIENDS_COUNT = "friendsCount";// 关注数
    static String WEIBO_USER_FANS_COUNT = "fansCount";// 粉丝数
    static String WEIBO_ATTITUDES_COUNT = "attitudesCount";// 赞数
    static String WEIBO_COMMENTS_COUNT = "commentsCount";// 评论数
    static String WEIBO_QUOTE_COUNT = "quoteCount";// 转发数
    static String WEIBO_USER_COUNTRY = "country";// 国家
    static String WEIBO_USER_PROVINCE = "province";// 省份
    static String WEIBO_USER_CITY = "city";// 城市 verified
    static String WEIBO_USER_AREA = "area";
    public static String WEIBO_MUNICIPALITY_BEIJING = "北京";
    public static String WEIBO_MUNICIPALITY_TIANJIN = "天津";
    public static String WEIBO_MUNICIPALITY_CHONGQING = "重庆";
    public static String WEIBO_MUNICIPALITY_SHANGHAI = "上海";

    static String WEIBO_USER_VERIFIED = "verified";// 认证类型
    public static String WEIBO_USER_NOT_VERIFIED_VALUE = "0";// 非认证类型
    public static String WEIBO_ORIGINAL_URL = "originalUrl";// 上层url
    public static String WEIBO_EXPRESSION = "expression";// 表情图
    public static String WEIBO_EXPRESSION_TEXT = "expressionText";// 表情文本
    public static String WEIBO_FORWARD_URL = "forwardUrl";// 原文url
    static String WEIBO_USER_TYPE = "userType";
    public static String WEIBO_POST_SOURCE = "postSource";
    public static String WEIBO_LEVEL = "level";
    public static String WEIBO_REPLY_COMMENT = "replycomment";


    /**
     * 微博转发信息
     */
    public static final String FORWARD_CONTENT = "forwardContent";             // 转发原文内容
    public static final String FORWARD_RELEASE_DATE = "forwardReleaseDate";    // 转发原文发布时间
    public static final String FORWARD_AUTHOR = "forwardAuthor";               // 转发原文作者昵称

    public static final String FORWARD_QUOTE_COUNT = "forwardQuoteCount";
    public static final String FORWARD_COMMENT_COUNT = "forwardCommentsCount";
    public static final String FORWARD_ATTITUDES_COUNT = "forwardAttitudesCount";
    public static final String FORWARD_PUBTIME = "forwardPubTime";


    public static final String FORWARD_FLAG = "forwardFlag";                   // 转发、回帖标志 1(转发、回帖) 0(原贴)

    public static final int REGULAR_PUBTIME_QUERY = 0;

    public static final int REGULAR_PUBTIME_AND_CREATETIME_QUERY = 1;


    public static final List<String> TYPE_LIST = new ArrayList<>();

    static {
        TYPE_LIST.add(BBS);
        TYPE_LIST.add(WEI_XIN);
        TYPE_LIST.add(BLOG);
        TYPE_LIST.add(WEI_BO);
        TYPE_LIST.add(TIEBA);
        TYPE_LIST.add(MEDIA);
        TYPE_LIST.add(VIDEO);
        TYPE_LIST.add(WENDA);
        TYPE_LIST.add(ZIXUN);

    }

    public static final List<String> APP_TYPE_LIST = new ArrayList<String>();

    static {
        APP_TYPE_LIST.add(BBS);
        APP_TYPE_LIST.add(WEI_XIN);
        APP_TYPE_LIST.add(BLOG);
        APP_TYPE_LIST.add(WEI_BO);
        APP_TYPE_LIST.add(ZIXUN);
        APP_TYPE_LIST.add(TIEBA);
        APP_TYPE_LIST.add(MEDIA);
        APP_TYPE_LIST.add(VIDEO);
        APP_TYPE_LIST.add(WENDA);
        APP_TYPE_LIST.add(FOREIGN_MEDIA);
        APP_TYPE_LIST.add(FOREIGN_NEWS);
    }

    /**
     * 口碑评价对应渠道类型
     */
    public static final List<String> ITEM_TYPE_LIST = new ArrayList<String>();

    private static final String MEITUAN = "meituan";

    private static final String DIAPING = "dianping";



    static {
        ITEM_TYPE_LIST.add(MEITUAN);
        ITEM_TYPE_LIST.add(DIAPING);
    }

    public static final Map<String, String> SORT_FLAG_MAPPING_MYSQL_SORT = new HashMap<>();

    static {
        SORT_FLAG_MAPPING_MYSQL_SORT.put(PUBTIME_DESC_SORT_FLAG, "pub_time desc");
        SORT_FLAG_MAPPING_MYSQL_SORT.put(PUBTIME_ASC_SORT_FLAG, "pub_time asc");
        SORT_FLAG_MAPPING_MYSQL_SORT.put(FOLDER_NUMBER_DESC_SORT_FLAG, "similarity_number desc");
        SORT_FLAG_MAPPING_MYSQL_SORT.put(CREATE_TIME_DESC_SORT_FLAG, "create_time desc");
        SORT_FLAG_MAPPING_MYSQL_SORT.put(CREATE_TIME_ASC_SORT_FLAG, "create_time asc");
    }
    public static final String[] includeQueryField = EsQueryConditionUtils.getIncludeQueryField(ESConstant.CONTENT_SIMHASH
            , ESConstant.TITLE_SIMHASH
            , ESConstant.TITLE
            , ESConstant.SOURCE
            , ESConstant.PUBTIME
            , ESConstant.DOC_TYPE
            , ESConstant.URL
            , ESConstant.SYS_SENTIMENT
            , ESConstant.EMOTION_ENTRY + ESConstant.DEFAULT_JOIN_SYMBOL + ESConstant.EMOTION_NAME
            , ESConstant.EMOTION_ENTRY + ESConstant.DEFAULT_JOIN_SYMBOL + ESConstant.EMOTION_VALUE
            , ESConstant.DOC_ID
            , ESConstant.SYS_ABSTRACT
            , ESConstant.WEIBO_AUTHOR
            , ESConstant.WEIBO_AVATOR
            , ESConstant.WEIBO_USER_SEX
            , ESConstant.WEIBO_USER_FRIENDS_COUNT
            , ESConstant.WEIBO_USER_FANS_COUNT
            , ESConstant.WEIBO_ATTITUDES_COUNT
            , ESConstant.WEIBO_COMMENTS_COUNT
            , ESConstant.WEIBO_QUOTE_COUNT
            , ESConstant.WEIBO_USER_TYPE
            , ESConstant.WEIBO_USER_COUNTRY
            , ESConstant.WEIBO_USER_PROVINCE
            , ESConstant.WEIBO_USER_CITY
            , ESConstant.WEIBO_USER_AREA
            , ESConstant.WEIBO_USER_VERIFIED
            , ESConstant.FORWARD_AUTHOR
            , ESConstant.FORWARD_RELEASE_DATE
            , ESConstant.FORWARD_CONTENT
            , ESConstant.FORWARD_FLAG
            , ESConstant.COMMENT_SCORE
            , ESConstant.ATTITUDES_COUNT
            , ESConstant.FORWARD_QUOTE_COUNT
            , ESConstant.FORWARD_ATTITUDES_COUNT
            , ESConstant.FORWARD_COMMENT_COUNT
            , ESConstant.FORWARD_PUBTIME
            , ESConstant.COMMENT_ID
            , ESConstant.QUOTE_COUNT
            , ESConstant.COMMENTS_COUNT
            , ESConstant.ORIGINAL_SOURCE
            , "enSource"
            , ESConstant.CONTENT_TAG);
}
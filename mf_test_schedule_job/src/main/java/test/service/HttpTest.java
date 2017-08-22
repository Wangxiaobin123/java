package test.service;

/**
 * Created by BFD-194 on 2017/4/6.
 */
public class HttpTest {
//    private String sendReq(String chartParam, String chartName, String width, String scale) {
//        String chartServerUrl = bfdApiConfig.getImage().getChartServerUrl();
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod(chartServerUrl);
//        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//        postMethod.addParameter("async", "true");
//        postMethod.addParameter("content", "options");
//        postMethod.addParameter("options", chartParam);
//        postMethod.addParameter("type", "image/png");
//        postMethod.addParameter("width", width);
//        postMethod.addParameter("scale", scale);
//        postMethod.addParameter("constr", "Chart");
//        postMethod.addParameter("filename", chartName);
//        try {
//            httpClient.executeMethod(postMethod);
//            if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
//                return postMethod.getResponseBodyAsString();
//            } else {
//                logger.error("[sendReq method] get chart url error,the chartParam is {}, width is {},scale is {},chartName is {}", chartParam, width, scale, chartName);
//                ReportMailUtils.notifyDeveloper("get chart url error", "[sendReq method] the chartParam is " + chartParam + ", width is " + width + ",scale is " + scale + ",chartName is " + chartName + ", status code " + postMethod.getStatusCode() + ", response body: " + postMethod.getResponseBodyAsString());
//            }
//        } catch (Exception e) {
//            logger.error("[sendReq method] get chart url error,the error message is:{} ", e);
//            ReportMailUtils.notifyDeveloper("get chart url error", ExceptionToStringUtils.getStackTrace(e));
//        }
//        return bfdApiConfig.getImage().getDefaultChartUrl();
//    }

}

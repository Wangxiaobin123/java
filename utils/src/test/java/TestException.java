import org.junit.Test;

import java.util.Random;

/**
 * @author :shengbin
 */
public class TestException {
    @Test
    public void test() {
        //JSONObject json = new JSONObject();
        try {
            //json.put("reportId",1);
            //json.put("ruleId",2);
            int i = 1/0;
            System.out.println(i);
        } catch (Exception e) {
            ReportLogUtils reportLogUtils = new ReportLogUtils();
            reportLogUtils.getException(e);
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void tes2() {
        //JSONObject json = new JSONObject();
        try {
            //json.put("reportId",1);
            //json.put("ruleId",2);
            int i = 1/0;
            System.out.println(i);
        } catch (Exception e) {
            ReportLogUtils reportLogUtils = new ReportLogUtils();
            reportLogUtils.getException(e);
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void test3() throws InterruptedException {
        Random random1 = new Random(System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            Thread.sleep(random1.nextInt(1000));
            System.out.println("==============");
        }
    }
}

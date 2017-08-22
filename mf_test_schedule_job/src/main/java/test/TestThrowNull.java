package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by BFD-194 on 2017/4/20.
 */
public class TestThrowNull {
    private static final Logger logger = LoggerFactory.getLogger(TestThrowNull.class);
    public static void main(String[] args){
        String str = null;
        try{
            String str1 = getString(str);
            System.out.println(str1);

            String str2 = getString("a");
            System.out.println(str2);
        }catch (Exception e){
            logger.error("error",e);
        }

    }

    private static String getString(String str) {
        // 此方法中可以捕获
        try{
        if(!str.isEmpty()){
        }else{
            //logger.error("error:",null);
            throw new NullPointerException(str);
        }
        }catch (Exception e){
            logger.error("error:",e);
            throw new NullPointerException(str);
        }
        return str;
    }
}

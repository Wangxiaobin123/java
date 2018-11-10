import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;

/**
 * @author : shengbin
 */
public class StringConvertToList {
    @Test
    public void test(){

        String str = "";
        List list = Splitter.on(",").trimResults().splitToList(str);
        System.out.println(list.size());
    }
}

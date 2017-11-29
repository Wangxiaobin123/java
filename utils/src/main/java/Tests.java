import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shengbin
 */
public class Tests {
    @Test
    public void test1(){
        List<String> str = new ArrayList<String>();
        str.add("a");
        str.add("b");
        System.out.println(str);
        DateTimeZone dateTimeZone = DateTimeZone.forID("+08:00");
        System.out.println(dateTimeZone);
    }
}

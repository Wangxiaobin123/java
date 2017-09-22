import com.google.api.translate.Language;
import com.google.api.translate.Translate;


/**
 * Created by shengbin on 2017/8/22.
 */
public class DataSourceUtils {
    /**
     * @param args
     */
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String trans;
        Translate.setHttpReferrer("http://translate.google.cn");
        try {
            trans = Translate.translate("你好", Language.CHINESE,
                    Language.ENGLISH);
            //		汉译英
//			trans = Translate.translate("hello", Language.ENGLISH, Language.CHINESE);
            System.out.println(trans);
//             英译汉
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}

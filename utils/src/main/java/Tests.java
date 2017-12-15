import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shengbin
 */
public class Tests {
    @Test
    public void test1() {

        //设定为当前文件夹
        File directory = new File("");
        try {
            //获取标准的路径
            System.out.println(directory.getCanonicalPath());
            //获取绝对路径
            System.out.println(directory.getAbsolutePath());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            int count = 0;
            String regex = "[\\u4e00-\\u9fa5]";

            String words = "";
            Pattern p = Pattern.compile(regex);

            // read file content from file
            StringBuffer sb = new StringBuffer("");
            FileReader reader = new FileReader("D://mygitHub//utils//6m.txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                Matcher m = p.matcher(str);
                System.out.println("提取出来的中文有：");
                while (m.find()) {
                    count++;
                    System.out.print(m.group() + " ");
                }
                sb.append(str + "/n");

                System.out.println(str);
            }

            br.close();
            reader.close();

            // write string to file
            // FileWriter writer = new FileWriter("c://test2.txt");
            //BufferedWriter bw = new BufferedWriter(writer);
            // bw.write(sb.toString());

            //bw.close();
            //writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

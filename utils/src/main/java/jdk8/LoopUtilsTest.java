package jdk8;

import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 针对大量数据循环的改善的测试
 * 遵循的原则：
 * 1.外大内小(嵌套循环)
 * 2.循环终止条件明确
 * 3.提取与循环无关的表达式
 *
 * @author shengbin
 */
public class LoopUtilsTest {
    private static final int LOOP_VALUE = 10000;

    /**
     * for循环
     */
    @Test
    public void forTest() {
        int k = 2;
        long start = System.currentTimeMillis();
        while (k > 0) {
            for (int i = 0; i < LOOP_VALUE; i++) {
                System.out.println("loop:" + i);
            }
            k--;
        }
        System.out.println("for loop time is:" + (double) (System.currentTimeMillis() - start) / (1000));
    }

    /**
     * IntStream 的 range 方法
     */
    @Test
    public void streamTest() {
        long start = System.currentTimeMillis();
        int k =2;
        while(k>0){
            IntStream.range(0, LOOP_VALUE).forEach(System.out::println);
            k--;
        }
        System.out.println("IntStream time is:" + (double) (System.currentTimeMillis() - start) / (1000));
    }
    @Test
    public void maxTest(){
        //获取数字的个数、最小值、最大值、总和以及平均值
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }
}

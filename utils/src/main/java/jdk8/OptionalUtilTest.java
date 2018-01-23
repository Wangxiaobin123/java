package jdk8;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * java 8 optional使用
 *
 * @author shengbin
 * @date 2017-12-22
 */
public class OptionalUtilTest {

    @Test
    public void testOf() {
        // Optional.of(obj):
        // 它要求传入的 obj 不能是 null 值的,
        // 否则还没开始进入角色就倒在了 NullPointerException 异常上了
        Users user = null;
        Optional.of(user);
    }

    @Test
    public void testOfNull() {
        // Optional.ofNullable(obj): 它以一种智能的,
        // 宽容的方式来构造一个 Optional 实例. 来者不拒, 传 null 进到就得到 Optional.empty(),
        // 非 null 就调用 Optional.of(obj).
        Users users = null;
        //users.setAge(25);
        // users.setName("shengbin");
        testEmpty(users);
    }

    public void testEmpty(Users users) {
        Users users1 = Optional.ofNullable(users).orElse(new Users());
        System.out.println(users1.getAge()
                // .map(age -> users.getAge())
        );
    }

    @Test
    public void testFilter() {
        BoolQueryBuilder qb = null;
        BoolQueryBuilder filterBuilder = null;
        qb = Optional.ofNullable(filterBuilder).orElse(new BoolQueryBuilder()).filter(Optional.ofNullable(filterBuilder).orElse(new BoolQueryBuilder()));

        System.out.println(qb);
    }

    /**
     * 过滤重复的
     */
    @Test
    public void testRepeat() {
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s, Square Without duplicates : %s %n", numbers, distinct);

        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy","U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(G7Countries);

        //创建一个长度大于两个字符的字符串List
        List<String> strList = new ArrayList<>();

        List<String> b = new ArrayList<>();
        b.add("a");
        strList.add(b.toString());
        List<String> filtered = strList.stream().filter(x -> x.length()>
                2).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);
    }
    @Test
    public void test4(){
        long l = 7000000000L;
        Long currentTime = System.currentTimeMillis();
        System.out.println((double)currentTime/1000>l);
        System.out.println(currentTime);
    }
}

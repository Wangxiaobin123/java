package jdk8;

import org.junit.Test;

import java.util.Optional;

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
}

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created by shengbin on 2017/8/22.
 */
@EnableAutoConfiguration
@Slf4j
public class ServiceMain {
    public static void main(String[] args) {
        log.info("[ServiceApi Rest start]");
        SpringApplication.run(ServiceMain.class);
        log.info("[ServiceApi Rest start successful !]");
    }
}

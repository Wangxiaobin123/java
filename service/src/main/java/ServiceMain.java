import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author shengbin on 2017/8/22.
 */
@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = "rest")
public class ServiceMain extends WebMvcConfigurerAdapter {
    /**
     * @param args DataManage
     */
    public static void main(String[] args) {
        log.info("[ServiceApi Rest start]");
        SpringApplication.run(ServiceMain.class);
        log.info("[ServiceApi Rest start successful !]");
    }
}

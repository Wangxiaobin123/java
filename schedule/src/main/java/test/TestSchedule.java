package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 2017/2/10.
 *
 * @author tag: shengbin
 */
@Configuration
@EnableAutoConfiguration
@ServletComponentScan
@SpringBootApplication
@EnableScheduling
public class TestSchedule {

    /**
     * log
     */
    private static Logger logger = LoggerFactory.getLogger(TestSchedule.class);


    /**
     * @param args DataManager
     */
    public static void main(String[] args) {
        SpringApplication.run(TestSchedule.class, args);
        logger.info("[TestSchedule Model] start report job successful");
    }
}

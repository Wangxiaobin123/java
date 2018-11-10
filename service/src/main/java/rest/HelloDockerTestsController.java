package rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : shengbin
 * @date : 2018-10-31
 */
@RestController
@RequestMapping("/test")
public class HelloDockerTestsController {

    @GetMapping("/helloDocker")
    public String index() {
        return "Hello Docker!";
    }
}

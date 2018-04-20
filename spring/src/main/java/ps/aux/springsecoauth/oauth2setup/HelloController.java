package ps.aux.springsecoauth.oauth2setup;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/foo/hello")
    public String helloFoo() {
        return "Hello foo";
    }

    @GetMapping("/bar/hello")
    public String helloBar() {
        return "Hello bar";
    }
}

package kho_cang.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public/api")
public class MyController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}


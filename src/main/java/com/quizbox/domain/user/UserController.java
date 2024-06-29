package com.quizbox.domain.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String mainAPI() {
        System.out.println("test");
        return "main route";
    }
}

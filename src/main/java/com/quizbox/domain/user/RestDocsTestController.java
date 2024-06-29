package com.quizbox.domain.user;

import com.quizbox.global.payload.ApiResponse;
import com.quizbox.global.payload.ResponseCustom;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    @GetMapping("/restDocsTest")
    public ResponseCustom<String> restDocsTestAPI() {
        return ResponseCustom.OK("hihihihi");
    }
}

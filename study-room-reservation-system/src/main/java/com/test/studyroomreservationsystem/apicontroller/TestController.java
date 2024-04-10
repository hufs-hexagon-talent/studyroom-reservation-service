package com.test.studyroomreservationsystem.apicontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    ResponseEntity<Object> testApi() {
        String info = "Successful API connection";
        return new ResponseEntity<>(info,HttpStatus.OK );

    }

}

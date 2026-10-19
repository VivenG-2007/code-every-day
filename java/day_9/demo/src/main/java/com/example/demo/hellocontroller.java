
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class hellocontroller {

    @GetMapping("/hello")
    public String hello() {
        return "<h1>hello world</h1>";
    }
    @GetMapping("/bye")
    public String bye() {
        return "<h1>bye world</h1>";
    }
}


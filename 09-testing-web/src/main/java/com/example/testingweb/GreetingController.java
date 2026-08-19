package com.example.testingweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final GreetingService service;

    public GreetingController(GreetingService service) {
        this.service = service;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(required = false) String name) {
        if (name != null && name.isBlank()) {
            throw new InvalidGreetingRequestException("name 파라미터는 비어있을 수 없습니다.");
        }
        if (name != null && name.length() > 20) {
            throw new InvalidGreetingRequestException("name 파라미터는 20자를 초과할 수 없습니다.");
        }
        return service.greet();
    }

}

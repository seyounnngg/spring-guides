package com.example.restservice;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final List<Greeting> history = new CopyOnWriteArrayList<>();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
        Greeting greeting = new Greeting(counter.incrementAndGet(), template.formatted(name));
        history.add(greeting);
        return greeting;
    }

    @GetMapping("/greetings")
    public List<Greeting> allGreetings(){
        return history;
    }
}
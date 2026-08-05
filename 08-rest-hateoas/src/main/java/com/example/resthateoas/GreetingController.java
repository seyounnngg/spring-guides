package com.example.resthateoas;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GreetingController {

    private static final String TEMPLATE_EN = "Hello, %s!";
    private static final String TEMPLATE_JP = "こんにちは、%sさん!";

    @RequestMapping("/greeting")
    public HttpEntity<Greeting> greeting(
            @RequestParam(value = "name", defaultValue = "World") String name) {

        Greeting greeting = new Greeting(String.format(TEMPLATE_EN, name));
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
        greeting.add(linkTo(methodOn(GreetingController.class).greetingJapanese(name)).withRel("japanese"));

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @RequestMapping("/greeting/jp")
    public HttpEntity<Greeting> greetingJapanese(
            @RequestParam(value = "name", defaultValue = "World") String name) {

        Greeting greeting = new Greeting(String.format(TEMPLATE_JP, name));
        greeting.add(linkTo(methodOn(GreetingController.class).greetingJapanese(name)).withSelfRel());
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withRel("english"));

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
}
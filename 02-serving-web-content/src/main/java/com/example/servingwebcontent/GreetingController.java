package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class GreetingController {

    private final List<String> history = new CopyOnWriteArrayList<>();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        history.add("Hello, " + name + "!");
        return "greeting";
    }

    @GetMapping("/greetings")
    public String greetings(Model model) {
        model.addAttribute("greetings", history);
        return "greetings-list";
    }
}
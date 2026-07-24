package com.example.handlingformsubmission;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GreetingController {

    private final List<Greeting> history = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        greeting.setId(counter.incrementAndGet());
        history.add(greeting);
        return "result";
    }

    @GetMapping("/greeting/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Greeting target = history.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElseThrow();
        model.addAttribute("greeting", target);
        return "greeting";
    }

    @PostMapping("/greeting/{id}")
    public String greetingUpdate(@PathVariable long id, @ModelAttribute Greeting greeting) {
        history.removeIf(g -> g.getId() == id);
        greeting.setId(id);
        history.add(greeting);
        return "result";
    }

    @GetMapping("/greetings")
    public String greetingList(Model model) {
        model.addAttribute("greetings", history);
        return "greetings-list";
    }
}
package com.example.testingweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.mockito.Mockito.when;

@WebMvcTest(GreetingController.class)
@AutoConfigureRestTestClient
class GreetingExceptionTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private GreetingService service;

    @Test
    void blankNameShouldReturnBadRequest() {
        restTestClient.get().uri("/greeting?name= ")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void tooLongNameShouldReturnBadRequest() {
        String longName = "a".repeat(21);
        restTestClient.get().uri("/greeting?name=" + longName)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void validNameShouldReturnOk() {
        when(service.greet()).thenReturn("Hello, World");
        restTestClient.get().uri("/greeting?name=Seyoung")
                .exchange()
                .expectStatus().isOk();
    }
}
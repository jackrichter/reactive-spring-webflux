package com.reactivespring.moviesinfo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void flux() {
        webTestClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void flux_approach2() {
        var flux = webTestClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    void flux_approach3() {
        webTestClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.size() == 3;
                });
    }

    @Test
    void mono() {
        webTestClient
                .get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("Hello Mono!", responseBody);
                });
    }

    @Test
    void stream() {
        var flux = webTestClient
                .get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(0L,1L,2L, 3L)
                .thenCancel()
                .verify();
    }
}
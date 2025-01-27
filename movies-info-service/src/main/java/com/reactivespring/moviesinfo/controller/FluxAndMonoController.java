package com.reactivespring.moviesinfo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/")
public class FluxAndMonoController {

    @GetMapping(value = "greet")
    public String showGreeting() {
        return "Hello Reactive Spring WebFlux!";
    }

    @GetMapping(value = "flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3).log();
    }

    @GetMapping(value = "mono")
    public Mono<String> helloWorldMono() {
        return Mono.just("Hello Mono!").log();
    }

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1)).log();
    }
}

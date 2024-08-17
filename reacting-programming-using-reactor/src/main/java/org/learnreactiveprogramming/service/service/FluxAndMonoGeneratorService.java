package org.learnreactiveprogramming.service.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {
    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();     // from a db or a remote service call
    }

    public Flux<String> namesFluxMap(int strLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s -> s.toUpperCase())
                .filter(str -> str.length() > strLength)
                .map(str -> str.length() + "-" + str)
                .log();
    }

    public Flux<String> namesFluxImmutable() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux.map(String::toUpperCase);

        return namesFlux.log();
    }

    public Flux<String> namesFluxFlatMap(int strLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(str -> str.length() > strLength)
                .flatMap(s -> splitNames(s))    // A,L,E,X,C,H,L,O,E
                .log();
    }

    public Flux<String> namesFluxFlatMapAsync(int strLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(str -> str.length() > strLength)
                .flatMap(s -> splitNamesWithDelay(s))    // A,L,E,X,C,H,L,O,E
                .log();
    }

    public Flux<String> namesFluxConcatMap(int strLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(str -> str.length() > strLength)
                .concatMap(s -> splitNamesWithDelay(s))    // A,L,E,X,C,H,L,O,E
                .log();
    }

    public Flux<String> namesFluxTransform(int strLength) {

        Function<Flux<String>, Flux<String>> filtermap = name -> name.map(String::toUpperCase)
                .filter(str -> str.length() > strLength);

//        Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .flatMap(s -> splitNames(s))    // A,L,E,X,C,H,L,O,E
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFluxTransformSwitchIfEmpty(int strLength) {

        Function<Flux<String>, Flux<String>> filtermap = name ->
                name.map(String::toUpperCase)
                .filter(str -> str.length() > strLength)
                .flatMap(s -> splitNames(s));

        var defaultFlux = Flux.just("default")     // "D","E","F","A","U","L","T"
                .transform(filtermap);

//        Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filtermap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> exploreConcatWith() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> exploreConcatWithMono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();   // Flux of A,B
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100)); // 100: A, 200: B, 300: C

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125)); // 125: D, 300: E, 425; F

        return Flux.merge(abcFlux,defFlux).log();
    }

    public Flux<String> exploreMergeWith() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100)); // 100: A, 200: B, 300: C

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125)); // 125: D, 300: E, 425; F

//        return Flux.merge(abcFlux,defFlux).log();
        return abcFlux.mergeWith(defFlux).log();        // The result is blend: A,D,B,E,C,F
    }

    public Flux<String> exploreMergeSequential() {
        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100)); // 100: A, 200: B, 300: C

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125)); // 125: D, 300: E, 425; F

        return Flux.mergeSequential(abcFlux,defFlux).log();     // The result is sequential: A,B,C,D,E,F
    }

    public Flux<String> exploreZip() {
        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second ).log();        // The result is zipped: AD,BE,CF
    }

    public Flux<String> exploreZipTuple4() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        var _123Flux = Flux.just("1","2","3");
        var _456Flux = Flux.just("4","5","6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)       // It's a Tuple
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())       // -> "AD14","BE25","CF36"
                .log();
    }

    public Flux<String> exploreZipWith() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second )
                .log();        // The result is zipped: AD,BE,CF
    }

    public Flux<String> exploreMergeWithMono() {
        var aMono = Mono.just("A");   // A
        var bMono = Mono.just("B");   // D

        return aMono.mergeWith(bMono).log();
    }

    public Mono<String> exploreZipWithMono() {
        var aMono = Mono.just("A");   // A
        var bMono = Mono.just("B");   // D

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();    // AD
    }

    public Mono<String> namesMono() {

        return Mono.just("alex").log();
    }

    public Mono<String> namesMonoMapFilter(int strLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(str -> str.length() > strLength);
    }

    public Mono<List<String>> namesMonoFlatMap(int strLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(str -> str.length() > strLength)
                .flatMap(this::splitStringMono)             // Mono<List> of A, L, E, X
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);      // ALEX -> A, L, E, X
        return Mono.just(charList);
    }

    // ALEX -> Flux(A,L,E,X)
    private Flux<String> splitNames(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    private Flux<String> splitNamesWithDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux()
                .subscribe(name -> System.out.println("The name is: " + name));

        service.namesMono().subscribe(name -> System.out.println("Mono name is: " + name));

        service.namesFluxMap(3)
                .subscribe(s -> System.out.println("FlatMapped: " + s));
    }
}
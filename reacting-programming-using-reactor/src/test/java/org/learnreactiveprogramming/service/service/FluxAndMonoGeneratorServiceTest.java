package org.learnreactiveprogramming.service.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        // given

        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        int strLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxMap(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutable() {

        var namesFluxImmutable = fluxAndMonoGeneratorService.namesFluxImmutable();
        StepVerifier.create(namesFluxImmutable)
//                .expectNext("ALEX", "BEN", "CHLOE") // This should fail
                .expectNext("alex", "ben", "chloe") // The original array is immutable!
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        int strLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        int strLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(strLength);
        StepVerifier.create(namesFlux)
//                .expectNext("A","L","E","X","C","H","L","O","E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMap() {
        int strLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMap(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        int strLength = 3;

        var value = fluxAndMonoGeneratorService.namesMonoFlatMap(strLength);
        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        int strLength = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformDefaultReturn() {
        int strLength = 6;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformSwitchIfEmpty() {
        int strLength = 6;

        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransformSwitchIfEmpty(strLength);
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {

        var concatFlux = fluxAndMonoGeneratorService.exploreConcat();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWith() {

        var value = fluxAndMonoGeneratorService.exploreMerge();
        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeSequential() {
        var value = fluxAndMonoGeneratorService.exploreMergeSequential();
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        var value = fluxAndMonoGeneratorService.exploreZip();
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZipTuple4() {
        var value = fluxAndMonoGeneratorService.exploreZipTuple4();
        StepVerifier.create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }
}
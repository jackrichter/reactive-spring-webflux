package com.reactivespring.moviesinfo.repository;

import com.reactivespring.moviesinfo.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}

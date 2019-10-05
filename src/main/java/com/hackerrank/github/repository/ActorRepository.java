package com.hackerrank.github.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackerrank.github.model.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>{
}

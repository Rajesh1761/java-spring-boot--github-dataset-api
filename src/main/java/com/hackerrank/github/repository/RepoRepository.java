package com.hackerrank.github.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackerrank.github.model.Repo;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long>{
}

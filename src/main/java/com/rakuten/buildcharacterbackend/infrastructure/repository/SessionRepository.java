package com.rakuten.buildcharacterbackend.infrastructure.repository;

import com.rakuten.buildcharacterbackend.domain.entity.Session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {

}
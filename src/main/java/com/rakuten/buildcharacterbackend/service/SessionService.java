package com.rakuten.buildcharacterbackend.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.repository.SessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    SessionRepository repo;
    RedisTemplate<String, String> redis;

    @Autowired
    SessionService(SessionRepository repo, RedisTemplate<String, String> redis) {
        this.repo = repo;
        this.redis = redis;
    }

    public String create(final Session session) {
        return repo.save(session).getId();
    }

    public Session get(final String sessionID) {
        return repo.findById(sessionID).get();
    }

    public Session set(final Session sessionCur) {
        return repo.save(sessionCur);
    }

    public void delete(final Session sessionCur) {
        repo.delete(sessionCur);
    }

    public List<Session> getAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void delete() {
        repo.deleteAll();
    }

    public void setTtl(final String sessionID, final Long ttl) {
        String hashName = Session.class.getAnnotation(RedisHash.class).value();
        String key = String.format("%s:%s", hashName, sessionID);
        redis.expire(key, ttl, TimeUnit.SECONDS);
    }
}

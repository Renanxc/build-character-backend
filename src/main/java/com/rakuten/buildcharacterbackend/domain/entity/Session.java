package com.rakuten.buildcharacterbackend.domain.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash( value = "Session", timeToLive = 600)
public class Session implements Serializable {

    private static final long serialVersionUID = 6010735310270804119L;

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private Integer age;
    private String charClass;
    private String charSubclass;
    private String charRace;
    private List<String> charEquipments;
    private List<String> charSpells;

    @TimeToLive
    private Long ttl;
    
}

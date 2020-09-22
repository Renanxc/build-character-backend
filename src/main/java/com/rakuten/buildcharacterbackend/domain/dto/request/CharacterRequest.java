package com.rakuten.buildcharacterbackend.domain.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterRequest {
    @NotNull(message = "{CharacterRequest.age.required}")
    private Integer age;
    @NotEmpty(message = "{CharacterRequest.charClass.required}")
    private String charClass;
    @NotEmpty(message = "{CharacterRequest.charSubclass.required}")
    private String charSubclass;
    @NotEmpty(message = "{CharacterRequest.charRace.required}")
    private String charRace;
    private List<String> charEquipments;
    private List<String> charSpells;
}

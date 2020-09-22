package com.rakuten.buildcharacterbackend.testUtil;

import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptions;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptionsToClasses;

public class OptionsStrategyRequestCreator {

    public static RpgCharacterClientOptions[] createValidRequest() {
        return RpgCharacterClientOptions.values();
    }

    public static RpgCharacterClientOptions[] createValidPartialRequest() {
        return new RpgCharacterClientOptions[] {RpgCharacterClientOptions.classes,RpgCharacterClientOptions.races};
    }

    public static RpgCharacterClientOptionsToClasses[] createValidWithSearchValueRequest() {
        return RpgCharacterClientOptionsToClasses.values();
    } 

    public static RpgCharacterClientOptionsToClasses[] createValidPartialWithSearchValueRequest() {
        return new RpgCharacterClientOptionsToClasses[] {RpgCharacterClientOptionsToClasses.spells};
    } 

}

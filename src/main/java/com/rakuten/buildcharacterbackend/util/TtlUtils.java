package com.rakuten.buildcharacterbackend.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.rakuten.buildcharacterbackend.api.v1.exception.TTlOutOfRangeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TtlUtils {

    private long lower;	
    private long upper;

    public TtlUtils(@Value("${utils.ttl.range.lower}") long lower, @Value("${utils.ttl.range.upper}") long upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public void verifyRange(Long ttl) {
        Range<Long> testRange = Range.range(lower, BoundType.CLOSED, upper, BoundType.CLOSED); 

        if(ttl != null && testRange.negate().test(ttl)) 
            throw new TTlOutOfRangeException(String.format(
                "Setted TTL was %d, but the accpeted rage is (%d to %d)!", ttl, testRange.lowerEndpoint(), testRange.upperEndpoint()));
    }
}